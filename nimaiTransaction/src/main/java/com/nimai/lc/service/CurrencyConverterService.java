package com.nimai.lc.service;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.nimai.lc.utility.AppConstants;


@Service
public class CurrencyConverterService {

	public static Double  sendHttpGetRequest(String fromCode,String toCode, String decryPtId, String currencyConversionUrl) throws IOException, JSONException{
		//String AppId="6d22174cd4d740a1a602c55c4a78d44b";
				//String url=https://openexchangerates.org/api/latest.json?app_id=
		String GET_URL=currencyConversionUrl+decryPtId+AppConstants.urlDetails+fromCode;
		
		URL url=new URL(GET_URL);
		HttpURLConnection httpUrlConnection=(HttpURLConnection) url.openConnection();
		httpUrlConnection.setRequestMethod("GET");
		int responseCode=httpUrlConnection.getResponseCode();
		Double exchangerates=null;
		if(responseCode==HttpURLConnection.HTTP_OK) {
			BufferedReader in=new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
			String inputline;
			
			StringBuffer response=new StringBuffer();
			while((inputline =in.readLine())!=null) {
				response.append(inputline);
				System.out.println();
			}in.close();
			
			JSONObject obj=new JSONObject(response.toString());
			System.out.println("===========************"+response.toString());
			 exchangerates=obj.getJSONObject("rates").getDouble(fromCode);
			System.out.println("=========************"+exchangerates);
			return exchangerates;
		}
		return null;
		
		
 	}
}
