package com.nimai.ucm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.ucm.entity.NimaiToken;
import com.nimai.ucm.repository.CustomerRepository;
import com.nimai.ucm.repository.UserTokenRepository;

@Service
public class TokenServiceImpl implements TokenService
{

	@Autowired
	private UserTokenRepository userTokenRepository;
	
	@Override
	public boolean validateToken(String userId, String token) {
		// TODO Auto-generated method stub
		
		NimaiToken nt=userTokenRepository.isTokenExists(userId, token);
		if(nt!=null)
			return true;
		else
			return false;
	}

}
