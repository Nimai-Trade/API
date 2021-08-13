package com.nimai.splan.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.nimai.splan.model.NimaiCustomerSubscriptionGrandAmount;
import com.nimai.splan.model.NimaiMSubscription;
import com.nimai.splan.model.OnlinePayment;
import com.nimai.splan.payload.CustomerSubscriptionGrandAmountBean;
import com.nimai.splan.payload.SplanRequest;
import com.nimai.splan.payload.SubscriptionBean;
import com.nimai.splan.payload.SubscriptionPaymentBean;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface SubscriptionPlanService {

	ResponseEntity<?> saveUserSubscriptionPlan(SubscriptionBean subscriptionRequest, String userID);

	ResponseEntity<?> renewSubscriptionPlan(SubscriptionBean subscriptionRequest, String userID);

	ResponseEntity<?> findSPlanDetailsByUserId(String userId);

	ResponseEntity<?> getSPlanByUserId(String userId);
	
	ResponseEntity<?> findMSPlanDetails(String userId);

	ResponseEntity<?> findCustomerSPlanDetails(SplanRequest splanRequest);

	//SubscriptionPaymentBean initiatePayment(SubscriptionPaymentBean sPymentRequest);
	
	Map<String, Object> initiatePayment(SubscriptionPaymentBean sPymentRequest, Double grandAmt, String subsCurrency) throws PayPalRESTException;

	//Map<String, Object> executePayment(String paymentId, String payerId) throws PayPalRESTException;
	
	//HashMap<String,String> getPaymentResponse(String encResp);

	OnlinePayment checkPayment(SubscriptionPaymentBean sPymentRequest);

	NimaiMSubscription getPlanDetailsBySubscriptionId(String string);

	ResponseEntity<?> findAllSPlanDetailsForCustomer();

	ResponseEntity<?> checkForSubsidiary(SubscriptionBean subscriptionRequest);
	
	ResponseEntity<?> insertGrandAmountData(CustomerSubscriptionGrandAmountBean subscriptionRequest);

	NimaiCustomerSubscriptionGrandAmount getCustomerAmount(String userId);

	boolean checkPaymentData(int id, Double amt);

	Map<String, Object> completePayment(Payment payment,String paymentId);

	//void saveData(Payment payment, String paymentId) throws IOException;

	void saveData(String orderId, String status) throws IOException;

	Map<String, Object> executePayment(String orderId) throws PayPalRESTException;


}
