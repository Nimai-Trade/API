package com.paypal.payhook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.payhook.exceptions.ParseBodyException;

import java.util.List;


/**
 * The in-memory representation of a Webhook event/notification. <br>
 * Can be validated through {@link PayHook#validateWebhookEvent(WebhookEvent)}.
 */
public class WebhookEvent {
    private String validWebhookId;
    private List<String> validTypesList;
    private String bodyString;
    private WebhookEventHeader header;
    private JsonObject body;
    private boolean isValid = false;

    /**
     * The in-memory representation of a Webhook event/notification. <br>
     * Can be validated through {@link PayHook#validateWebhookEvent(WebhookEvent)}.
     * @param validWebhookId your webhooks valid id. Get it from here: https://developer.paypal.com/developer/applications/
     * @param validTypesList your webhooks valid types/names. Here is a full list: https://developer.paypal.com/docs/api-basics/notifications/webhooks/event-names/
     * @param header the http messages header as {@link WebhookEventHeader}.
     * @param body the http messages body as {@link JsonObject}.
     */
    public WebhookEvent(String validWebhookId, List<String> validTypesList, WebhookEventHeader header, JsonObject body) {
        this.validWebhookId = validWebhookId;
        this.validTypesList = validTypesList;
        this.header = header;
        this.body = body;
        this.bodyString = new Gson().toJson(body);
    }

    /**
     * The in-memory representation of a webhook event.
     * @param validWebhookId your webhooks valid id. Get it from here: https://developer.paypal.com/developer/applications/
     * @param validTypesList your webhooks valid types/names. Here is a full list: https://developer.paypal.com/docs/api-basics/notifications/webhooks/event-names/
     * @param header the http messages header as {@link WebhookEventHeader}.
     * @param bodyString the http messages body as {@link String}.
     * @throws ParseBodyException
     */
    public WebhookEvent(String validWebhookId, List<String> validTypesList, WebhookEventHeader header, String bodyString) throws ParseBodyException {
        this.validWebhookId = validWebhookId;
        this.validTypesList = validTypesList;
        this.header = header;
        this.bodyString = bodyString;
        this.body = new PayHook().parseAndGetBody(bodyString);
    }

    public String getValidWebhookId() {
        return validWebhookId;
    }

    public List<String> getValidTypesList() {
        return validTypesList;
    }

    public String getBodyString() {
        return bodyString;
    }

    public WebhookEventHeader getHeader() {
        return header;
    }

    public JsonObject getBody() {
        return body;
    }

    /**
     * Perform {@link PayHook#validateWebhookEvent(WebhookEvent)} on this event, so this method
     * returns the right value.
     * @return true if this event is a valid paypal webhook event.
     */
    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
