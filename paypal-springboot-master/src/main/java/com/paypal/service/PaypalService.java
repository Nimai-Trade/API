package com.paypal.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CartBase;
import com.paypal.api.payments.CreateProfileResponse;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.InputFields;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Order;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.api.payments.WebProfile;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.config.PaypalPaymentIntent;
import com.paypal.config.PaypalPaymentMethod;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.http.serializer.Json;
import com.paypal.orders.AddressPortable;
import com.paypal.orders.AmountBreakdown;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Money;
import com.paypal.orders.Name;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.OrdersGetRequest;
import com.paypal.orders.Phone;
import com.paypal.orders.PhoneWithType;
import com.paypal.orders.PurchaseUnit;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.util.Credentials;

@Service
public class PaypalService {


	@Value("${paypal.mode}")
	private String mode;
	
	@Value("${paypal.client.app}")
	private String clientID;
	
	@Value("${paypal.client.secret}")
	private String secret;
	
	@Autowired
	private APIContext apiContext;
	
	public com.paypal.orders.Order createPayment(
			Double total, 
			String currency, 
			PaypalPaymentMethod method, 
			PaypalPaymentIntent intent, 
			String description,
			String cancelUrl, 
			String successUrl) throws PayPalRESTException{
		
		//APIContext apiContext = new APIContext(clientID, secret, mode);
		
		System.out.println("Request Id: "+apiContext.getRequestId());
		
		/*com.paypal.orders.Item item1=new com.paypal.orders.Item();
		item1.name("SUBSCRIPTION");
		item1.description("A");
		Money m1=new Money();
		m1.currencyCode(currency);
		m1.value("80");
		item1.unitAmount(m1);
		item1.quantity("1");
		
		com.paypal.orders.Item item2=new com.paypal.orders.Item();
		item2.name("VAS");
		item2.description("B");
		Money m2=new Money();
		m2.currencyCode(currency);
		m2.value("30");
		item2.unitAmount(m2);
		item2.quantity("1");
		
		com.paypal.orders.Item item3=new com.paypal.orders.Item();
		item3.name("DISCOUNT");
		item3.description("C");
		Money m3=new Money();
		m3.currencyCode(currency);
		m3.value("10");
		item3.unitAmount(m1);
		item3.quantity("1");
		
		List<com.paypal.orders.Item> itemList=new ArrayList<com.paypal.orders.Item>();
		itemList.add(item1);
		itemList.add(item2);
		itemList.add(item3);
		*/
		com.paypal.orders.Order order = null;
		// Construct a request object and set desired parameters
		// Here, OrdersCreateRequest() creates a POST request to /v2/checkout/orders
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.checkoutPaymentIntent("CAPTURE");
		
		ApplicationContext ac=new ApplicationContext();
		ac.cancelUrl(cancelUrl);
		ac.returnUrl(successUrl);
		ac.shippingPreference("NO_SHIPPING");
		orderRequest.applicationContext(ac);
		
		com.paypal.orders.Payer payer=new com.paypal.orders.Payer();
		Name name=new Name();
		name.givenName("Adil");
		name.surname("Bhati");
		payer.name(name);
		payer.email("adil.bhati@cloverinfotech.com");
		/*PhoneWithType phoneWithType=new PhoneWithType();
		Phone phoneNumber=new Phone();
		phoneNumber.nationalNumber("23239822833196");
		phoneWithType.phoneNumber(phoneNumber);
		payer.phoneWithType(phoneWithType);
		AddressPortable addressPortable=new AddressPortable();
		addressPortable.addressLine1("One Janpat Marg");
		addressPortable.addressLine2("Maharastra");
		addressPortable.countryCode("INDIA");
		//addressPortable.addressLine3("INDIA");
		addressPortable.postalCode("400059");
		payer.addressPortable(addressPortable);
		*/
		orderRequest.payer(payer);
		
		//List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
		System.out.println("Subscription Amount:"+total);
		Double gst=total*0.18;
		Double amountWithGST=total+gst;
		System.out.println("Amount with GST: "+amountWithGST);
		List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
		PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId("MDNM-DND-234323-1222")
				//.customId("MDNM-DND-234323-1222")
		    //.description("Sporting Goods").customId("CUST-HighFashions").softDescriptor("HighFashions")
		    .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(""+(amountWithGST-10))
		        .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode("USD").value(""+total))
		            //.shipping(new Money().currencyCode("USD").value("30.00"))
		            //.handling(new Money().currencyCode("USD").value("10.00"))
		        		.discount(new Money().currencyCode("USD").value(""+10))
		        		.taxTotal(new Money().currencyCode("USD").value(""+gst))
		            //.shippingDiscount(new Money().currencyCode("USD").value("10.00"))
		        		)
		    )
		    .items(new ArrayList<com.paypal.orders.Item>() {
		      {
		    	  if(currency.equalsIgnoreCase("USD"))
		    	  {
		        add(new com.paypal.orders.Item().name("Subscription")
		            .unitAmount(new Money().currencyCode("USD").value("100.00"))
		            .quantity("1")
		            //.tax(new Money().currencyCode("USD").value("10.00")).quantity("1")
		            .category("PHYSICAL_GOODS"));
		    	  }
		        add(new com.paypal.orders.Item().name("VAS")
		            .unitAmount(new Money().currencyCode("USD").value("23.00"))
		            .quantity("1")
		            //.tax(new Money().currencyCode("USD").value("5.00")).quantity("2")
		            .category("PHYSICAL_GOODS"));
		      }
		    })
		    ;
		purchaseUnitRequests.add(purchaseUnitRequest);
		
		
		//purchaseUnits.add(new PurchaseUnitRequest());
		//purchaseUnits.add(new PurchaseUnitRequest().items(itemList));
		orderRequest.purchaseUnits(purchaseUnitRequests);
		OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
		
		try 
		{
			// Call API with your client and get a response for your call
			HttpResponse<com.paypal.orders.Order> response = Credentials.client.execute(request);

			// If call returns body in response, you can get the de-serialized version by
			// calling result() on the response
			order = response.result();
			System.out.println("Order ID: " + order.id());
			
		} 
		catch (IOException ioe) 
		{
			if (ioe instanceof HttpException) 
			{
				// Something went wrong server-side
				HttpException he = (HttpException) ioe;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			} 
			else 
			{
				// Something went wrong client-side
			}
		}
		return order;
	}
	
	public void executePayment(String orderId, String payerId) throws PayPalRESTException{
		com.paypal.orders.Order order = null;
		//OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);

		try {
			OrdersGetRequest request = new OrdersGetRequest(orderId);
		    //3. Call PayPal to get the transaction
		    HttpResponse<com.paypal.orders.Order> response = Credentials.client.execute(request);
		    //4. Save the transaction in your database. Implement logic to save transaction to your database for future reference.
		    System.out.println("Full response body:");
		    System.out.println(new JSONObject(new Json().serialize(response.result())).toString(4));
		    order = response.result();
		    Integer itemsSize=order.purchaseUnits().get(0).items().size();
		    System.out.println("Purchase Unit Size: "+itemsSize);
		    String vasPrice="";
		    if(itemsSize==2)
		    {
		    	System.out.println("It contain Subscription + VAS");
		    	vasPrice=order.purchaseUnits().get(0).items().get(1).unitAmount().value();
		    	System.out.println("VAS Price: "+vasPrice);
		    }
		    try
		    {
			    if(order.purchaseUnits().get(0).amountWithBreakdown().amountBreakdown().discount().value().equalsIgnoreCase("0")
			    		|| order.purchaseUnits().get(0).amountWithBreakdown().amountBreakdown().discount().value()==null)
			    {
			    	System.out.println("It doesn't contain Discount");
			    }
		    }
		    catch(NullPointerException ne)
		    {
		    	System.out.println("It doesn't contain Discount ----> NullPointerException");
		    }
		    	//Transaction transaction = 
		    
			// Call API with your client and get a response for your call
		/*	HttpResponse<com.paypal.orders.Order> response = Credentials.client.execute(request);

			// If call returns body in response, you can get the de-serialized version by
			// calling result() on the response
			order = response.result();
			//System.out.println("Order Response: "+order.i);
			System.out.println("Capture ID: " + order.purchaseUnits().get(0).payments().captures().get(0).id());
			order.purchaseUnits().get(0).payments().captures().get(0).links()
					.forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
			System.out.println("Order Status: "+order.status());
			System.out.println("Purchase Unit Size: "+order.purchaseUnits().size());
			System.out.println("Purchase Unit Size: "+order.purchaseUnits().get(0).toString());
			System.out.println("Details: "+order.purchaseUnits().get(0).referenceId());
			*/
		} catch (IOException ioe) {
			if (ioe instanceof HttpException) {
				// Something went wrong server-side
				System.out.println("Issue On Server Side");
				HttpException he = (HttpException) ioe;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			} else {
				// Something went wrong client-side
			}
		}
	}
}
