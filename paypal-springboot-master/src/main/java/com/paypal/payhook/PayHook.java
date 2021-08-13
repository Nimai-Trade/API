package com.paypal.payhook;

import com.google.gson.*;
import com.paypal.Application;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.payhook.exceptions.ParseBodyException;
import com.paypal.payhook.exceptions.ParseHeaderException;
import com.paypal.payhook.exceptions.WebHookValidationException;
import com.paypal.payhook.paypal.Constants;
import com.paypal.payhook.paypal.SSLUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Contains methods for validating
 * WebHook events/notifications and some
 * utility methods.
 */
public class PayHook {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
    private boolean isSandboxMode = false;
    private boolean isWarnIfSandboxModeIsEnabled = true;

    @Value("${paypal.mode}")
	private String mode;
	
	@Value("${paypal.client.app}")
	private String clientID;
	
	@Value("${paypal.client.secret}")
	private String secret;
	
	@Value("${paypal.service.endpoint}")
	private String serviceEndPoint;
	
	@Value("${paypal.validatewebhooksig.url}")
	private String validateWebhookSigURL;
	
    
    /**
     * Parses the provided header {@link Map}
     * into a {@link WebhookEventHeader} object and returns it.
     */
    public WebhookEventHeader parseAndGetHeader(Map<String, String> headerAsMap) throws ParseHeaderException {
        // Check if all keys we need exist
        String transmissionId        = checkKeyAndGetValue(headerAsMap, Constants.PAYPAL_HEADER_TRANSMISSION_ID);
        String timestamp             = checkKeyAndGetValue(headerAsMap, Constants.PAYPAL_HEADER_TRANSMISSION_TIME);
        String transmissionSignature = checkKeyAndGetValue(headerAsMap, Constants.PAYPAL_HEADER_TRANSMISSION_SIG);
        String certUrl               = checkKeyAndGetValue(headerAsMap, Constants.PAYPAL_HEADER_CERT_URL);
        String authAlgorithm         = checkKeyAndGetValue(headerAsMap, Constants.PAYPAL_HEADER_AUTH_ALGO);

        // Note that the webhook id and crc32 get set after the validation was run
        return new WebhookEventHeader(transmissionId, timestamp, transmissionSignature, authAlgorithm, certUrl);
    }

    /**
     * Parses the provided body {@link String}
     * into a {@link JsonObject} and returns it.
     */
    public JsonObject parseAndGetBody(String bodyAsString) throws ParseBodyException {
        try{
            return JsonParser.parseString(bodyAsString).getAsJsonObject();
        } catch (Exception e) {
            throw new ParseBodyException(e.getMessage());
        }
    }

    /**
     * Checks if the provided key exists in the provided map and returns its value. <br>
     * The keys existence is checked by {@link String#equalsIgnoreCase(String)}, so that its case is ignored. <br>
     * @return the value mapped to the provided key.
     */
    public String checkKeyAndGetValue(Map<String, String> map, String key) throws ParseHeaderException {
        Objects.requireNonNull(map);
        Objects.requireNonNull(key);

        String value = map.get(key);
        if (value == null || value.equals("")) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    value = entry.getValue();
                    break;
                }
            }

            if (value == null || value.equals("")) {
                throw new ParseHeaderException("Header is missing the '"+key+"' key or its value!");
            }
        }
        return value;
    }

    /**
     * Creates a new {@link WebhookEvent} and validates it. <br>
     * See {@link #validateWebhookEvent(WebhookEvent)} for details. <br>
     * @param validId your webhooks valid id. Get it from here: https://developer.paypal.com/developer/applications/
     * @param validTypes your webhooks valid types/names. Here is a full list: https://developer.paypal.com/docs/api-basics/notifications/webhooks/event-names/
     * @param header the http messages header as string.
     * @param body the http messages body as string.
     */
    public void validateWebhookEvent(String validId, List<String> validTypes, WebhookEventHeader header, String body) throws ParseBodyException, WebHookValidationException, CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        validateWebhookEvent(new WebhookEvent(validId, validTypes, header, body));
    }

    /**
     * Validate the provided {@link WebhookEvent}.<br>
     * Throws an {@link Exception} if the validation fails. <br>
     * Performed checks: <br>
     * <ul>
     *   <li>Is the name/type valid?</li>
     *   <li>Are the certificates valid?</li>
     *   <li>Is the data/transmission-signature valid? (not in sandbox-mode)</li>
     *   <li>Do the webhook ids match? (not in sandbox-mode)</li>
     * </ul>
     * Note: {@link WebhookEventHeader#getWebhookId()} and {@link WebhookEventHeader#getCrc32()} return null,
     * if you have sandbox-mode <u>enabled</u> since the transmission-signature is <u>not</u> decoded in sandbox-mode.
     *
     * @param event The {@link WebhookEvent} to validate.
     * @throws WebHookValidationException <b style='color:red' >IMPORTANT: MESSAGE MAY CONTAIN SENSITIVE INFORMATION!</b>
     */
    public void validateWebhookEvent(WebhookEvent event) throws WebHookValidationException, ParseBodyException, IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

       // if (isSandboxMode && isWarnIfSandboxModeIsEnabled)
       //     System.out.println("[PAYHOOK] NOTE THAT SANDBOX-MODE IS ENABLED!");
    	WebhookEventHeader header = event.getHeader();

        // Check if the webhook types match
        List<String> validEventTypes = event.getValidTypesList();
        logger.info("Event Types: "+validEventTypes);
        // event_type can be either an json array or a normal field. Do stuff accordingly.
        JsonElement elementEventType = event.getBody().get("event_type");
        if (elementEventType==null) elementEventType = event.getBody().get("event_types"); // Check for event_types
        if (elementEventType==null) throw new ParseBodyException("Failed to find key 'event_type' or 'event_types' in the provided json body."); // if the element is still null

        if (elementEventType.isJsonArray()){
            // This means we have multiple event_type objects in the array
            JsonArray arrayEventType = elementEventType.getAsJsonArray();
            logger.info("JSON array: "+arrayEventType);
            for (JsonElement singleElementEventType :
                    arrayEventType) {
                JsonObject o = singleElementEventType.getAsJsonObject();
                logger.info("validEVentTypes: "+validEventTypes.contains(o.get("name").getAsString()));
                if (!validEventTypes.contains(o.get("name").getAsString()))
                    throw new WebHookValidationException("No valid type("+o.get("name")+") found in the valid types list: "+validEventTypes.toString());
            }
        }
        else{
            // This means we only have one event_type in the json and not an array.
            String webHookType = event.getBody().get("event_type").getAsString();
            logger.info("WebhookType: "+webHookType);
            if (!validEventTypes.contains(webHookType))
            {
            	logger.info("No valid type("+webHookType+") found in the valid types list: "+validEventTypes.toString());
                throw new WebHookValidationException("No valid type("+webHookType+") found in the valid types list: "+validEventTypes.toString());
            }
        }
		
        // Load certs
        String clientCertificateLocation = event.getHeader().getCertUrl();
        logger.info("event.getHeader(): "+event.getHeader());
        logger.info("event.getHeader().getCertUrl(): "+event.getHeader().getCertUrl());
        String trustCertificateLocation = Constants.PAYPAL_TRUST_DEFAULT_CERT;
        Collection<X509Certificate> clientCerts = SSLUtil.getCertificateFromStream(new BufferedInputStream(new URL(clientCertificateLocation).openStream()));
        Collection<X509Certificate> trustCerts = SSLUtil.getCertificateFromStream(PayHook.class.getClassLoader().getResourceAsStream(trustCertificateLocation));

        logger.info("trustCertificateLocation: "+trustCertificateLocation);
        logger.info("clientCerts: "+clientCerts);
        logger.info("trustCerts: "+trustCerts);
        
        // Check the chain
        SSLUtil.validateCertificateChain(clientCerts, trustCerts, "RSA");

        // Validate the encoded signature.
        // Note:
        // If we are in sandbox mode, we are done with validation here,
        // because the next part will always fail if this event is a mock, sandbox event.
        // For more information see: https://developer.paypal.com/docs/api-basics/notifications/webhooks/notification-messages/
  
        // Construct expected signature
        String validWebhookId           = event.getValidWebhookId();
        String actualEncodedSignature   = header.getTransmissionSignature();
        String authAlgo                 = header.getAuthAlgorithm();
        String transmissionId           = header.getTransmissionId();
        String transmissionTime         = header.getTimestamp();
        String bodyString               = event.getBodyString();
        String expectedDecodedSignature = String.format("%s|%s|%s|%s", transmissionId, transmissionTime, validWebhookId, SSLUtil.crc32(bodyString));

        logger.info("validWebhookId: "+validWebhookId);
        logger.info("actualEncodedSignature: "+actualEncodedSignature);
        logger.info("auth Algorithm : "+authAlgo);
        logger.info("transmissionId: "+transmissionId);
        logger.info("transmissionTime: "+transmissionTime);
        logger.info("bodyString: "+bodyString);
        logger.info("expectedDecodedSignature: "+expectedDecodedSignature);
        // Decode the actual signature and update the event object with its data
        String decodedSignature = SSLUtil.decodeTransmissionSignature(actualEncodedSignature);
        logger.info("Decoded Signature: "+decodedSignature);
        String[] arrayDecodedSignature = decodedSignature.split("\\|"); 
        // Split by | char, because the decoded string should look like this: <transmissionId>|<timeStamp>|<webhookId>|<crc32>

        logger.info("Array Decoded Signature Length: "+arrayDecodedSignature.length);
        if(arrayDecodedSignature.length==1)
        	logger.info("webhookId array:"+arrayDecodedSignature[0]);
        if(arrayDecodedSignature.length==2)
        	logger.info("Array:"+arrayDecodedSignature[1]);
        if(arrayDecodedSignature.length==3)
        	logger.info("webhookId array:"+arrayDecodedSignature[2]);
        if(arrayDecodedSignature.length==4)	
        logger.info("Crc32:"+arrayDecodedSignature[3]);
           
        logger.info("Sandbox mode: "+isSandboxMode);
        if (isSandboxMode) 
        {
            event.setValid(true);
            return;
        }
                
        header.setWebhookId(arrayDecodedSignature[2]);
        header.setCrc32(arrayDecodedSignature[3]);
        
        boolean isSigValid = SSLUtil.validateTransmissionSignature(clientCerts, authAlgo, actualEncodedSignature, expectedDecodedSignature);
        logger.info("isSigValid: "+isSigValid);
        if (isSigValid)
            event.setValid(true);
        else
        {
        	logger.info("Exception: Transmission signature is not valid! Expected: '"+expectedDecodedSignature+"' Provided: '"+decodedSignature+"'");
            throw new WebHookValidationException("Transmission signature is not valid! Expected: '"+expectedDecodedSignature+"' Provided: '"+decodedSignature+"'");    
        }
    }

    /**
     * Formats all of the {@link WebhookEvent} information to a {@link String} and returns it.
     */
    public String getWebhookEventAsString(WebhookEvent webHookEvent) {
        Objects.requireNonNull(webHookEvent);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("Information for object: "+webHookEvent +System.lineSeparator());

        // Add your info
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("VALID-webhook-id: "+webHookEvent.getValidWebhookId() +System.lineSeparator());
        stringBuilder.append("VALID-webhook-types: "+webHookEvent.getValidTypesList().toString() +System.lineSeparator());

        // Add header info
        stringBuilder.append(System.lineSeparator());
        WebhookEventHeader header = webHookEvent.getHeader();
        stringBuilder.append("header stuff: "+System.lineSeparator());
        stringBuilder.append("webhook-id: "+header.getWebhookId() +System.lineSeparator());
        stringBuilder.append("transmission-id: "+header.getTransmissionId() +System.lineSeparator());
        stringBuilder.append("timestamp: "+header.getTimestamp() +System.lineSeparator());
        stringBuilder.append("transmission-sig: "+header.getTransmissionSignature() +System.lineSeparator());
        stringBuilder.append("auth-algo: "+header.getAuthAlgorithm() +System.lineSeparator());
        stringBuilder.append("cert-url: "+header.getCertUrl() +System.lineSeparator());
        stringBuilder.append("crc32: "+header.getCrc32() +System.lineSeparator());

        // Add the json body in a pretty format
        stringBuilder.append(System.lineSeparator());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(webHookEvent.getBodyString());
        stringBuilder.append("body-string: "+webHookEvent.getBodyString() +System.lineSeparator());
        stringBuilder.append("body: "+jsonOutput +System.lineSeparator());

        return stringBuilder.toString();
    }

    /**
     * See {@link PayHook#setSandboxMode(boolean)} for details.
     */
    public boolean isSandboxMode() {
        return isSandboxMode;
    }

    /**
     * Enable/Disable the sandbox-mode. <br>
     * Disabled by default. <br>
     * If enabled some validation checks, which only succeed for
     * live applications, wont be done. <br>
     * See {@link PayHook#validateWebhookEvent(WebhookEvent)} for details.
     */
    public void setSandboxMode(boolean sandboxMode) {
        isSandboxMode = sandboxMode;
    }

    /**
     * See {@link PayHook#setWarnIfSandboxModeIsEnabled(boolean)} for details.
     */
    public boolean isWarnIfSandboxModeIsEnabled() {
        return isWarnIfSandboxModeIsEnabled;
    }

    /**
     * If enabled a warning is printed to {@link System#out}
     * every time before performing a validation, stating that the sandbox-mode is enabled. <br>
     * Enabled by default. <br>
     */
    public void setWarnIfSandboxModeIsEnabled(boolean warnIfSandboxModeIsEnabled) {
        isWarnIfSandboxModeIsEnabled = warnIfSandboxModeIsEnabled;
    }
    
    public ResponseEntity<String> validateWebhookSignature(WebhookEvent event,String clientID,String secret,String serviceEndPoint,String validateWebhookSigURL) throws PayPalRESTException
    {
    	logger.info("---- In Validate Webhook Signature ----");
    	logger.info("clientID: "+clientID);
    	logger.info("secret: "+secret);
    	logger.info("serviceEndPoint: "+serviceEndPoint);
    	logger.info("URL for validating Signature: "+validateWebhookSigURL);
    	Map<String, String> configurationMap = new HashMap<String, String>();
		//configurationMap.put("service.EndPoint",serviceEndPoint);
    	
    	//UAT
    	configurationMap.put("service.EndPoint",serviceEndPoint);
    	OAuthTokenCredential oc=new OAuthTokenCredential(clientID, secret, configurationMap);
		
    	
		//PROD
    	/*
    	String uatClientID="ARakRQp7F_xil8H_kNWG8a7aOEf4ElCzumdY4vIGmbaRtQv8HKFLRw_pTLVKV4pKv4N8IbnjwDzyz5cP";
    	String uatSecret="EKdLnZRgT7LusUEPyUlAH0O5Fwv5xD1TiBTVhzF4vxO1PdoBnGQG2KJkMM7v-BwyXbs0XkeMpRTIMk_V";
		configurationMap.put("service.EndPoint","https://api-m.paypal.com");
		OAuthTokenCredential oc=new OAuthTokenCredential(uatClientID, uatSecret, configurationMap);
		String url = "https://api-m.paypal.com/v1/notifications/verify-webhook-signature";
		*/
    	
		logger.info("service endpoint URL: "+configurationMap);
		
		System.out.println("token: "+oc.getAccessToken());
		logger.info("Token created: "+oc.getAccessToken());
		
		
		logger.info("URL to verify webhook signature: "+validateWebhookSigURL);
		
    	WebhookEventHeader header = event.getHeader();
        
        String transmission_id="{\"transmission_id\":\""+header.getTransmissionId()+"\",";
		String transmission_time="\"transmission_time\":\""+header.getTimestamp()+"\",";
		String cert_url="\"cert_url\":\""+header.getCertUrl()+"\",";
		String auth_algo="\"auth_algo\":\""+header.getAuthAlgorithm()+"\",";
		String transmission_sig="\"transmission_sig\":\""+header.getTransmissionSignature()+"\",";
		String webhook_id="\"webhook_id\":\""+event.getValidWebhookId()+"\",";
		String webhook_event="\"webhook_event\":"+event.getBodyString()+"}";
		
		logger.info("webhookId: "+event.getValidWebhookId());
        logger.info("transmissionId: "+header.getTransmissionId());
        logger.info("transmissionTime: "+header.getTimestamp());
        logger.info("certURL: "+header.getCertUrl());
        logger.info("authAlgorithm: "+header.getAuthAlgorithm());
        logger.info("transmissionSignature: "+header.getTransmissionSignature());
        logger.info("bodyString: "+event.getBodyString());
		
		String finalRequestBody=transmission_id+transmission_time+cert_url+auth_algo+transmission_sig+webhook_id+webhook_event;
        
		System.out.println("finalRequest: "+finalRequestBody);
		logger.info("Final Request body: "+finalRequestBody);
		RestTemplate restTemplate = new RestTemplate();
		
		// create headers
		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", oc.getAccessToken());
		
		// build the request
		HttpEntity<String> entity = new HttpEntity<>(finalRequestBody, headers);

		// send POST request
		ResponseEntity<String> response = restTemplate.postForEntity(validateWebhookSigURL, entity, String.class);
		
		return response;
    }
}
