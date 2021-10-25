package com.nimai.lc.utility;

import java.util.Scanner;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class AESUtil {

	//final static Logger logger = Logger.getLogger(ASEUtil.class);

	public static final String ALGORITHM = AppConstants.algorithm;
	public static final String SECRET_KEY = AppConstants.key;

	public String encrypt(String text) {
	//	logger.debug("IN DASEUtil -- Encryption starts : " + text);
		byte[] raw;
		String encryptedString;
		SecretKeySpec skeySpec;
		byte[] encryptText = text.getBytes();
		Cipher cipher;
		try {
			raw = Base64.decodeBase64(SECRET_KEY);

			skeySpec = new SecretKeySpec(raw, ALGORITHM);
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedString = Base64.encodeBase64String(cipher.doFinal(encryptText));

	//		logger.debug("IN ASEUtil -- Encrypted string : " + encryptedString);
		} catch (Exception e) {
	//		logger.debug("IN ASEUtil -- encryption catch block : " + e.getMessage());
			System.out.println("encrypted catch block : " + e.getMessage());
			e.printStackTrace(System.out);
			return text;
		}
		return encryptedString;
	}

	public String decrypt(String text) {
	//	logger.debug("IN ASEUtil -- decryption starts : " + text);
// do some decryption
		Cipher cipher;
		String decryptString;
		byte[] encryptText = null;
		byte[] raw;
		SecretKeySpec skeySpec;
		try {
			raw = Base64.decodeBase64(SECRET_KEY);

			skeySpec = new SecretKeySpec(raw, ALGORITHM);
			encryptText = Base64.decodeBase64(text);
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decryptString = new String(cipher.doFinal(encryptText));

	//		logger.debug("IN ASEUtil decrypt -- decrypted string : " + decryptString);
		} catch (Exception e) {
			System.out.println("decrypted catch block : " + e.getMessage());
	//		logger.debug("IN ASEUtil decrypt -- decryption catch block : " + e.getMessage());
			e.printStackTrace(System.out);
			return text;
		}
		return decryptString;
	}

	private static AESUtil aSEUtil = null;

	public static AESUtil getInstance() {
		if (aSEUtil == null) {
			aSEUtil = new AESUtil();
		}
		return aSEUtil;
	}

	public static void main(String[] args) {

		Scanner readData = new Scanner(System.in);
		System.out.println("Enter Password :");
		String str = readData.nextLine();
		String[] str1 = str.split(" ");

		AESUtil aSEUtil = AESUtil.getInstance();
		String encrypt = aSEUtil.encrypt(str1[0]);
		System.out.println("[encrypt]" + encrypt);
		String decrypt = aSEUtil.decrypt(encrypt);
		System.out.println("Decrypt : " + decrypt);

 //System.out.println("Decrypt:[] " + aSEUtil.decrypt(aSEUtil.encrypt("pravin")));

	}
}