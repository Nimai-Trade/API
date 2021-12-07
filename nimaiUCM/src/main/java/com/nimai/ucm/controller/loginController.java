package com.nimai.ucm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.bean.ResetPasswordBean;
import com.nimai.ucm.bean.TermsAndPolicyBean;
import com.nimai.ucm.service.UserBranchService;
import com.nimai.ucm.utility.ErrorDescription;

@RestController
public class loginController {

	@Autowired
	private UserBranchService userBranchService;
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/resetpassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> resetPassword(@RequestBody ResetPasswordBean resetBean) {

		try {

		} catch (Exception exception) {
			return null;
		}
		return null;

	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewTermsAndPolicy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> viewBusinessDetails() {
		GenericResponse<Object> response = new GenericResponse<Object>();
		System.out.println("===== Getting terms and policy details =====");
		try
		{
			TermsAndPolicyBean tp=userBranchService.getTermsAndPolicy();
			response.setData(tp);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println("Exeption: "+e);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
	} 

	

}
