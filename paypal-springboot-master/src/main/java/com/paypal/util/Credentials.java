package com.paypal.util;

import org.springframework.beans.factory.annotation.Value;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class Credentials 
{
	static String clientId = "ARakRQp7F_xil8H_kNWG8a7aOEf4ElCzumdY4vIGmbaRtQv8HKFLRw_pTLVKV4pKv4N8IbnjwDzyz5cP";
    static String secret = "EKdLnZRgT7LusUEPyUlAH0O5Fwv5xD1TiBTVhzF4vxO1PdoBnGQG2KJkMM7v-BwyXbs0XkeMpRTIMk_V";
    
    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Live(clientId, secret);
    
    // Creating a client for the environment
    public static PayPalHttpClient client = new PayPalHttpClient(environment);
}