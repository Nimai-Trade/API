package com.nimai.ucm.utility;

import java.util.regex.Pattern;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.service.RegisterUserService;

@Component
public class BusinessDetailsValidator {
	@Autowired
	RegisterUserService registerUser;

	static Logger logbusinessdetails = Logger.getLogger(BusinessDetailsBean.class.getName());

	

}
