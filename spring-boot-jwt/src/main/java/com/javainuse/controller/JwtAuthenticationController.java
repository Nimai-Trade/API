package com.javainuse.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.model.FieoMember;
import com.javainuse.model.JwtRequest;
import com.javainuse.model.JwtResponse;
import com.javainuse.model.ReferralLeads;
import com.javainuse.repository.UserTokenRepository;
import com.javainuse.service.UserTokenService;
import com.javainuse.bean.GenericResponse;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserTokenRepository jwtTokenRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Autowired
	private UserTokenService userTokenService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {
		
		String appendedID="";
		if(!authenticationRequest.getEmailId().equalsIgnoreCase(""))
			appendedID=authenticationRequest.getUserId()+"-"+authenticationRequest.getEmailId();
		else
			if(authenticationRequest.getEmailId().equalsIgnoreCase("") && authenticationRequest.getUserId().substring(0, 2).equalsIgnoreCase("BA"))
				appendedID=authenticationRequest.getUserId();
			else
			{
				String email=jwtTokenRepo.findEmailIdByUseridOnly(authenticationRequest.getUserId());
				System.out.println("Email: "+email);
				appendedID=authenticationRequest.getUserId()+"-"+email;
			}
		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(appendedID);
		authenticate(appendedID, authenticationRequest.getPassword());

		final String token = jwtTokenUtil.generateToken(userDetails);
		
		userTokenService.saveTokenData(appendedID, token);
		//jwtTokenUtil.retrieveClaims(token);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getDetailsFromToken/{token}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> getDetailsFromToken(@PathVariable String token, HttpServletRequest request) throws Exception 
	{
		//token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYl9wX2lkIjoiMSIsInBiX3JlZGlyZWN0dXJsIjoiaHR0cHM6Ly9lYmF5LmluIiwicGJfaWVjIjoiQWRpcGlzY2kgZnVnaWF0IG5paCIsInBiX25hbWUiOiJLZW5uZWR5IFdyaWdodCIsInBiX2Rlc2lnbmF0aW9uIjoiRWEgZXhlcmNpdGF0aW9uIGRvbG8iLCJwYl9lbWFpbCI6InByYXRlZWttaXR0YWxAZ21haWwuY29tIiwicGJfb3JnYW5pemF0aW9uIjoiTW9vcmUgYW5kIFdpbGRlciBUcmFkZXJzIiwicGJfc3RyZWV0IjoiUXVhbSBldCBzaXQgYXJjaGl0ZSIsInBiX2NpdHkiOiJBdCBtb2xlc3RpYXMgaW4gcG9zcyIsInBiX3N0YXRlIjoiTW9sZXN0aWFzIG9jY2FlY2F0IGEiLCJwYl9jb3VudHJ5IjoiSW5kaWEiLCJwYl9waW5jb2RlIjoiT21uaXMgIiwicGJfbW9iaWxlIjoiKzkxOTc3MDk5MDA5MCIsInBiX2ZheCI6IisxICg3NjQpIDQ4OC0xNzA3IiwicGJfZ3N0aW4iOiJSZWN1c2FuZGFlIn0.qrm2OAFcIRn1o6AfbAZUefET8HIOyVm5gC-NrogdkUo";
		GenericResponse<Object> response = new GenericResponse<Object>();
		ReferralLeads refLeads=jwtTokenUtil.retrieveClaims(token);
		FieoMember fm=new FieoMember();
		fm.setLeadId(refLeads.getLeadId());
		fm.setFirstName(refLeads.getFirstName());
		fm.setLastName(refLeads.getLastName());
		fm.setEmailId(refLeads.getEmailId());
		fm.setCountry(refLeads.getCountry());
		fm.setMobileNo(refLeads.getMobileNo());
		fm.setLandline(refLeads.getLandline());
		
		response.setData(fm);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getLeadsCouponCode/{emailId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> getLeadsCouponCodeByEmailId(@PathVariable String emailId, HttpServletRequest request) throws Exception 
	{
		GenericResponse<Object> response = new GenericResponse<Object>();
		try
		{
			String couponCode=jwtTokenRepo.findPromoCodeByEmailId(emailId);
			if(couponCode!=null)
			{
				response.setData(couponCode);
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
			else
			{
				response.setData("");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		}
		catch(Exception e)
		{
			response.setData("");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		
	}
}
