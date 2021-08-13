package com.nimai.ucm.utility;

import java.util.Arrays;

import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nimai.ucm.service.RegisterServiceImpl;

@Component
public class RegistrationId {

	@Autowired
	RegisterServiceImpl registerService;

	public String username(String subscribeType, String bankType) {

		Random randomNumber = new Random(System.currentTimeMillis());
		DateTime dt = new DateTime();  // current time
		//int month = dt.dayOfMonth();     // gets the current month
	
		int seconds=dt.getSecondOfMinute();
		System.out.println("================Seconds:"+seconds);
		int number = randomNumber.nextInt(90) + 100;
		Random random = new Random(System.currentTimeMillis());
		
		String snumber = String.valueOf(number);
		String userID = "";
		String initials = null;

			if ((subscribeType.equalsIgnoreCase("CUSTOMER")) || (subscribeType.equalsIgnoreCase("BANK"))
				|| (subscribeType.equalsIgnoreCase("REFERRER"))) {
			if (bankType.equalsIgnoreCase("CUSTOMER") && (subscribeType.equalsIgnoreCase("BANK")))
				initials = "BC";
			else
				initials = subscribeType.substring(0, 2);
			userID = initials.toUpperCase().concat(String.valueOf(seconds)).concat(snumber);
			
		
			return userID.replaceAll("\\s", "");
		}
		return userID.replaceAll("\\s", "");
	}
	
	
	public String newUsername(String subscribeType, String bankType) {
		DateTime dt = new DateTime(); 
		int seconds=dt.getSecondOfMinute();
		Random random = new Random(System.currentTimeMillis());
		String id = String.format("%02d", random.nextInt(1000));
		
		String userID = "";
		String initials = null;

			if ((subscribeType.equalsIgnoreCase("CUSTOMER")) || (subscribeType.equalsIgnoreCase("BANK"))
				|| (subscribeType.equalsIgnoreCase("REFERRER"))) {
			if (bankType.equalsIgnoreCase("CUSTOMER") && (subscribeType.equalsIgnoreCase("BANK")))
				initials = "BC";
			else
				initials = subscribeType.substring(0, 2);
		
			userID = initials.toUpperCase().concat(String.valueOf(seconds)).concat(id);
			return userID.replaceAll("\\s", "");
		}
		return userID.replaceAll("\\s", "");
	}
	

	public String number() {
		Random randomNUmber = new Random(System.currentTimeMillis());
		StringBuilder stringNUmber = new StringBuilder();

		int numbers = 9;
		for (int i = 0; i < numbers; i++) {
			int digit = randomNUmber.nextInt(9);
			stringNUmber.append(digit);
		}
		String randomNUmbers = stringNUmber.toString();
		return randomNUmbers;
	}

	
	
	public static void main(String[] args) {
		RegistrationId id=new RegistrationId();
		id.username("CUSTOMER", "BANK");
		System.out.println(id.username("CUSTOMER", "BANK"));
		id.newUsername("CUSTOMER", "BANK");
		//System.out.println(id.username("CUSTOMER", "BANK"));
	}

	
	
	
	
	
	
	
	
	
	
	
	

	
	
}

