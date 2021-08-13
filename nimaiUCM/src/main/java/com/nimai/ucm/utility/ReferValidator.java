package com.nimai.ucm.utility;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.service.ReferService;

@Component
public class ReferValidator {
	
	@Autowired
	ReferService referservice;

	

	public String validateReferDetail(ReferBean referbean) {

		final Pattern COUNTRY_NAME = Pattern.compile("^[a-zA-Z]*$");
		final Pattern EMAIADDRESS = Pattern.compile("^(.+)@(.+)$");
		final Pattern FIRSTNAME = Pattern.compile("[a-zA-Z]*$");
		final Pattern LASTNAME = Pattern.compile("^[a-zA-Z]*$");
		final Pattern MOBILENO = Pattern.compile("^[0-9]*$");
		final Pattern COMPANYNAME = Pattern.compile("^[a-zA-Z]*$");
		// final Pattern LANDLINENUMBER = Pattern.compile("^[0-9]*$");

		String returnStr = "";
		try {
			if (referbean != null) {
				
					boolean isEmailExist = referservice.checkEmailId(referbean.getEmailAddress());

					if (isEmailExist) {
						return "Email Id already exists. Please try another email Address.";
					}
				  
				if ((referbean.getFirstName() == null) || (referbean.getFirstName().trim().isEmpty())) {
					return "Please enter first name";
				}
				if (!FIRSTNAME.matcher(referbean.getFirstName()).matches()) {
					return "FirstName should be  always characcter.No Space Allowed in Firstname";
				}
				if ((referbean.getFirstName().length() <= 2)
						|| (referbean.getFirstName().length() > 15)) {
					return "First Name should be greater then 3 characters and less then 15 characters";
				}
				if ((referbean.getLastName() == null) || (referbean.getLastName().trim().isEmpty())) {
					return "Please enter last name";
				}
				if (!LASTNAME.matcher(referbean.getLastName()).matches()) {
					return "Last Name always only characters, No Space Allowed";
				}
				if ((referbean.getLastName().length() <= 2)
						|| (referbean.getLastName().length() > 15)) {
					return "Last Name should be greater then 3 characters and less then 15 characters";
				}
				if ((referbean.getEmailAddress() == null)
						|| (referbean.getEmailAddress().trim().isEmpty())) {
					return "Please enter email address";
				}
				if (!EMAIADDRESS.matcher(referbean.getEmailAddress()).matches()) {
					return "Please enter valid email address";
				}
				if (!(referbean.getMobileNo() == null)
						|| (referbean.getMobileNo().trim().isEmpty())) {
					if (!MOBILENO.matcher(referbean.getMobileNo()).matches()) {
						return "Mobile Number always only digits";
					}
					if ((referbean.getMobileNo().length() != 10)) {
						return "Mobile Number must be 10 digits";
					}
				}
				if ((referbean.getCountryName() == null)
						|| (referbean.getCountryName().trim().isEmpty())) {
					return "Please enter country name";
				}
				if (!COUNTRY_NAME.matcher(referbean.getCountryName()).matches()) {
					return "Country name always only characters";
				}
				if ((referbean.getCountryName().length() <= 2)
						|| (referbean.getCountryName().length() > 15)) {
					return "Country Name should be greater then 3 characters and less then 15 characters";
				}
				if(referbean.getCompanyName().trim().isEmpty())
				{
					return "Company Name is required";
				}
				
				
				returnStr = "Success";
			} else {
				returnStr = "Failure";
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failure : " + e.getMessage();
		}
		return returnStr;
	}

}
