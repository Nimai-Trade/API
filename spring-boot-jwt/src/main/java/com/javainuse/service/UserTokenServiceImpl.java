package com.javainuse.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javainuse.model.NimaiToken;
import com.javainuse.repository.UserTokenRepository;


@Service
public class UserTokenServiceImpl implements UserTokenService {

	@Autowired
	UserTokenRepository userTokenRepository;

	@Override
	public void saveTokenData(String userId, String token) {
		// TODO Auto-generated method stub
		NimaiToken nt=new NimaiToken();
		nt.setUserId(userId);
		//nt.setEmailId(emailId);
		nt.setToken(token);
		nt.setInsertedDate(new Date());
		
		userTokenRepository.save(nt);
	}

	
}
