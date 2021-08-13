package com.nimai.ucm.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.service.RegisterUserService;

@Component
public class PersonalDetailsValidator 
{
	
	@Autowired
	RegisterUserService registerUser;
	
	public String Validation(PersonalDetailsBean personDetailsBean) {
		
		boolean isEmailExist = registerUser.checkEmailId(personDetailsBean.getEmailAddress());

		String returnStr = "";
		try {
			if (isEmailExist) {
				return "Please enter country name";
			}
			
			
			returnStr = "success";
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failed : " + e.getMessage();
		}
			return returnStr;
	}
}
