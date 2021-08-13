package com.nimai.uam.utility;

import java.util.HashMap;
import java.util.Map;

public class ErrorDescription {

	private static Map<String, String> codeToDescriptionMap = new HashMap<String, String>();

	static {
		codeToDescriptionMap.put("NIM000", "SUCCESS");
		codeToDescriptionMap.put("NIM001", "FAILURE");
		
		codeToDescriptionMap.put("EXE000", "Exception Occurred : ");
				
		// For Personal Details and Business Details  Sign Up Process 
		codeToDescriptionMap.put("ASA001", "Personal Details saved Successfully");
		codeToDescriptionMap.put("ASA002", "Pasword has reset Successfully");
		codeToDescriptionMap.put("ASA003","Password Already Reset!");
		codeToDescriptionMap.put("ASA004","Reset Password Link is Expired!");
		codeToDescriptionMap.put("ASA005","No rights exist for given Employee!");
		codeToDescriptionMap.put("ASA006","Sign In Successfully");
		codeToDescriptionMap.put("ASA007","Invalid Login Crendentials");
		codeToDescriptionMap.put("ASA008","Password Change Succesfully");
		codeToDescriptionMap.put("ASA009","Opps! something went wrong");
		codeToDescriptionMap.put("ASA010","Invalid User ID or Password ");
		codeToDescriptionMap.put("ASA011","Invalid User Password ");
		//codeToDescriptionMap.put("ASA008","Incorrect User Creddentials");
		
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

