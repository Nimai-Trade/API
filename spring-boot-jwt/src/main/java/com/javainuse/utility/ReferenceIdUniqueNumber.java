package com.javainuse.utility;

import java.util.Random;

public class ReferenceIdUniqueNumber {

	public String uniqueNUmberGenerator() {

		Random randomNUmber = new Random(System.currentTimeMillis());
		StringBuilder builder = new StringBuilder();
		int randomNUmnberLength = 3;
		for (int i = 1; i < randomNUmnberLength; i++) {
			int digit = randomNUmber.nextInt(3);
			builder.append(digit);

		}

		String number = builder.toString();
		return number;
	}

	protected String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public String uniqueNumberReferenceId() {
		ReferenceIdUniqueNumber nu = new ReferenceIdUniqueNumber();
		String str = (nu.getSaltString().concat(nu.uniqueNUmberGenerator()));

		return str;

	}

}
