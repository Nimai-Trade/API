package com.nimai.ucm.service;

public interface TokenService 
{
	public boolean validateToken(String userId,String token);
}
