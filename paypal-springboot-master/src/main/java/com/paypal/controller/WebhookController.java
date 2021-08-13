package com.paypal.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.paypal.Application;
import com.paypal.api.payments.Event;
import com.paypal.base.rest.APIContext;
import com.paypal.model.WebhookEventDump;
import com.paypal.payhook.PayHook;
import com.paypal.payhook.WebhookEvent;
import com.paypal.payhook.WebhookEventHeader;
import com.paypal.repository.WebhookEventDumpRepo;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping(value = "captureWebhookEvent", method = RequestMethod.POST)
public class WebhookController {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static final String PAYPAL_WEBHOOK_ID = "webhook.id";
	
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
	
	@Value("${paypal.webhookid}")
	private String webhookId;
	
	@Autowired
	WebhookEventDumpRepo eventRepo;
	
    // This listens at https://.../paypal-hook
    // for paypal notification messages and returns a "OK" text as response.
    @GetMapping(produces = "text/plain")
    public @ResponseBody String receiveAndRespond(HttpServletRequest request) {

    	logger.info("Received webhook event at .../captureWebhookEvent/..."+request);
        System.out.println("Received webhook event at .../captureWebhookEvent/...");
        try{
        	
            
        	PayHook payHook = new PayHook();
            //payHook.setSandboxMode(false); // Default is false. Remove this in production.
            //payHook.setSandboxMode(true);
            // Get the header and body
            logger.info("In receiveAndRespond  -> Sandbox Mode: "+payHook.isSandboxMode());
            WebhookEventHeader header = payHook.parseAndGetHeader(getHeadersAsMap(request));
            JsonObject         body   = payHook.parseAndGetBody(getBodyAsString(request));

            // Create this event
            WebhookEvent event = new WebhookEvent(
            		webhookId, // Get it from here: https://developer.paypal.com/developer/applications/
                    Arrays.asList("PAYMENT.CAPTURE.COMPLETED","PAYMENT.CAPTURE.PENDING","PAYMENT.SALE.COMPLETED","PAYMENT.SALE.PENDING", "PAYMENT.SALE.REFUNDED","PAYMENTS.PAYMENT.CREATED"), // Insert your valid event types/names here. Full list of all event types/names here: https://developer.paypal.com/docs/api-basics/notifications/webhooks/event-names
                    header,
                    body);
           
            logger.info("Webhook ID: "+webhookId);
            logger.info("Header: "+header);
            logger.info("Body"+body);
            // Do event validation
            //payHook.validateWebhookEvent(event); 
        	
            //19 JULY 2021 - Adil
            
            ResponseEntity<String> response=payHook.validateWebhookSignature(event,clientID,secret,serviceEndPoint,validateWebhookSigURL);
            
            if (response.getStatusCode() == HttpStatus.OK) 
            {
    		    System.out.println("Request Successful");
    		    logger.info("Validation Succesfull");
    		    System.out.println(response.getStatusCode());
    		    System.out.println(response.getBody());
    		} 
            else 
            {
    		    System.out.println("Request Failed");
    		    logger.info("Validation Failed");
    		    System.out.println(response.getStatusCode());
    		}
            
        	System.out.println("Data: "+event.getBody());
            logger.info(""+event.getBody());
            String dump=event.getBody().toString();
            String invoiceId,transactionId,orderId;
    		
            JSONParser parser = new JSONParser(); 
    		JSONObject json = (JSONObject) parser.parse(dump);
    		System.out.println("JSON OBject: "+json);
    		logger.info("JSON Object: "+json);
    		JSONObject jsonResource =(JSONObject) json.get("resource");
    		System.out.println("Resource: "+jsonResource);
    		logger.info("Resource: "+jsonResource);
    		
    		try
    		{
    			invoiceId=(String)jsonResource.get("invoice_id");
    			logger.info("invoice ID: "+invoiceId);
    			System.out.println("invoice ID: "+invoiceId);
    		}
    		catch(Exception e)
    		{
    			invoiceId="";
    			logger.info("invoice ID: "+invoiceId);
    		}
    		
    		try
    		{
    			transactionId=(String)jsonResource.get("id");
    			logger.info("transaction ID: "+transactionId);
    			System.out.println("transaction ID: "+transactionId);
    		}
    		catch(Exception e)
    		{
    			transactionId="";
    			logger.info("transaction ID: "+transactionId);
    		}
    		
    		try
    		{
    			JSONObject jsonSupplimentaryData =(JSONObject) jsonResource.get("supplementary_data");
    			JSONObject jsonRelatedId =(JSONObject) jsonSupplimentaryData.get("related_ids");
    			orderId=(String)jsonRelatedId.get("order_id");
    			System.out.println("order ID: "+orderId);
    		}
    		catch(Exception e)
    		{
    			orderId="";
    			logger.info("order ID: "+orderId);
    		}
    		
            WebhookEventDump wed=new WebhookEventDump();
            wed.setOrderId(orderId);
            wed.setInvoiceId(invoiceId);
            wed.setTransactionId(transactionId);
            wed.setPayloadDump(event.getBody().toString());
            Date now = new Date();
            wed.setCaptureDate(now);
            eventRepo.save(wed);
            logger.info("Validation Successfull...");
            System.out.println("Validation successfull!");
            
        } catch (Exception e) {
        	logger.info(""+e.getMessage());
            e.printStackTrace();
            System.out.println("Validation failed: "+e.getMessage());
        }
        return "OK";
    }

    // Simple helper method to help you extract the headers from HttpServletRequest object.
    private Map<String, String> getHeadersAsMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        @SuppressWarnings("rawtypes")
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    // Simple helper method to fetch request data as a string from HttpServletRequest object.
    private String getBodyAsString(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))){
            String line = "";
            while ((line=reader.readLine())!=null)
                stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
    
   
}