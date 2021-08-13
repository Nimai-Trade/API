package com.nimai.uam.bean;

/**
 * Created by 
 */
public class JwtAuthenticationResponse<E> {
    private String accessToken;
    private String tokenType = "Bearer";
    private E data;
//    private String flag;   
    
    

    public JwtAuthenticationResponse() {
		super();
	}

	public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

//	public String getFlag() {
//		return flag;
//	}
//
//	public void setFlag(String flag) {
//		this.flag = flag;
//	}

	

	

	
    
    
}
