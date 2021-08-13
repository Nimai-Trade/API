package com.paypal.controller;

import java.io.IOException;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.apache.bcel.generic.Instruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.config.PaypalPaymentIntent;
import com.paypal.config.PaypalPaymentMethod;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.service.PaypalService;
import com.paypal.util.URLUtils;

@Controller
@RequestMapping("/")
public class PaymentController {
	
	public static final String PAYPAL_SUCCESS_URL = "pay/success";
	public static final String PAYPAL_CANCEL_URL = "pay/cancel";
	
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaypalService paypalService;
	
	//@Autowired
	//private APIContext apiContext;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(){
		return "paypalindex";
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(method = RequestMethod.POST, value = "pay")
	public String pay(HttpServletRequest request,HttpServletResponse servResponse) throws IOException{
		String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
		String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
		//String approveURL="";
		try {
						
			Order payment =	paypalService.createPayment(
					123.00, 
					"USD", 
					PaypalPaymentMethod.paypal, 
					PaypalPaymentIntent.sale,
					"Gold Plan", 
					cancelUrl, 
					successUrl);
			payment.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
			
			for(LinkDescription o:payment.links())
			{
				if(o.rel().equalsIgnoreCase("approve"))
					return "redirect:" + o.href();
				//System.out.println("---"+o.href());
			}
			System.out.println("Order: "+payment.links().get(0).rel());
			//payment.links().forEach(link -> System.out.println(link.rel() + " => " + link.method() + ":" + link.href()));
			
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
	public String cancelPay(){
		return "cancel";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
	public String successPay(@RequestParam("token") String orderId,@RequestParam("PayerID") String payerId, Model model) throws PayPalRESTException{
		//System.out.println("PaymentId: "+paymentId);
		System.out.println("PayerId: "+payerId);
		System.out.println("OrderId: "+orderId);
		paypalService.executePayment(orderId, payerId);
		/*Payment payment = paypalService.executePayment(paymentId, payerId);
		String planSplit[]=payment.getTransactions().get(0).getCustom().split("-", 3);
		System.out.println("PayerInfo-FirstName: "+payment.getPayer().getPayerInfo().getFirstName());
		System.out.println("PayerInfo-Suffix: "+payment.getPayer().getPayerInfo().getSuffix());
		System.out.println("Description: "+payment.getTransactions().get(0).getDescription());
		System.out.println("Plan Split 1: "+planSplit[0]);
		System.out.println("Plan Split 2: "+planSplit[1]);
		System.out.println("Plan Split 3: "+planSplit[2]);
		if(payment.getState().equals("approved")){
		*/
			model.addAttribute("paymentId", orderId);
			/*model.addAttribute("planPurchased", payment.getTransactions().get(0).getDescription());
			System.out.println("Product 1: "+payment.getTransactions().get(0).getItemList().getItems().get(0));
			System.out.println("Product 2: "+payment.getTransactions().get(0).getItemList().getItems().get(1));
			System.out.println("Product 3: "+payment.getTransactions().get(0).getItemList().getItems().get(2));
			System.out.println("merchanrParam3: "+payment.getTransactions().get(0).getCustom());
			
			model.addAttribute("PayerInfo-FirstName", payment.getPayer().getPayerInfo().getFirstName());*/
			return "success";
		//}
		//return "redirect:/";
	}
	
}
