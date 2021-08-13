package com.nimai.ucm.utility;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
	private static SecureRandom random = new SecureRandom();
    private static final String CAPS_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SMALL_ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%^&*";
    private static Pattern passwdPtrn = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,}$");

    // Generates the Password of the length as provided, with respect to the combinations.
    public static String generatePassword(int len, String combinations) {
	String result = "";
	for (int i = 0; i < len; i++) {
	    int index = random.nextInt(combinations.length());
	    result += combinations.charAt(index);
	}
	return result;
    }
    
    public static boolean validatePwd(String pwd){
        
        Matcher mtch = passwdPtrn.matcher(pwd);
        if(mtch.matches()){
            return true;
        }
        return false;
    }
	


	public static String getInitialPassword(int i) {
		
		return generatePassword(i, CAPS_ALPHA + SMALL_ALPHA + SPECIAL_CHAR + NUMERIC);
	}
}
