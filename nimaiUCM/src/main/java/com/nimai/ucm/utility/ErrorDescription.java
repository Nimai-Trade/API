package com.nimai.ucm.utility;

import java.util.HashMap;
import java.util.Map;

public class ErrorDescription {

	private static Map<String, String> codeToDescriptionMap = new HashMap<String, String>();

	static {
		codeToDescriptionMap.put("NIM000", "SUCCESS");
		codeToDescriptionMap.put("NIM001", "FAILURE");
		
		codeToDescriptionMap.put("EXE000", "Validation Failed : ");
		codeToDescriptionMap.put("EXE001", "FAILURE_Exception");	
		
		// For Personal Details and Business Details  Sign Up Process 
		codeToDescriptionMap.put("ASA001","Congratulations! Your account has been successfully created !");
		codeToDescriptionMap.put("ASA002","Business Details saved Successfully");
		codeToDescriptionMap.put("ASA003","Email Id already exists");
		codeToDescriptionMap.put("ASA004","Email Id does not exists.");
		codeToDescriptionMap.put("ASA005","Business details are not allowed for the referrer.");
		codeToDescriptionMap.put("ASA006","Reference id generated.");
		codeToDescriptionMap.put("ASA008","You are trying to change the BusinessCountry.This will Deactivate your Account!!!");
		codeToDescriptionMap.put("ASA009","Congratulations! Your account has been successfully updated");
		codeToDescriptionMap.put("ASA010","OOPS! Invalid Captch");
		codeToDescriptionMap.put("ASA011","OOPS! Something went wrong while updating business details");
		codeToDescriptionMap.put("ASA012","OOPS! Something went wrong");
		codeToDescriptionMap.put("ASA013","OOPS! Something went wrong while updating personal details");
		
	}
	
	public static String getDescription(String code) 
	{
		String description = codeToDescriptionMap.get(code);
		if (description == null) 
		{
			description = "Invalid Error Code!";
		}
		return description;
	}

}

