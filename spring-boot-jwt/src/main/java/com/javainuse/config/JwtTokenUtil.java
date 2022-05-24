package com.javainuse.config;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.RsaKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.javainuse.model.JwtRequest;
import com.javainuse.model.ReferralLeads;
import com.javainuse.repository.CustomerRepository;
import com.javainuse.repository.ReferRepository;
import com.javainuse.repository.ReferralLeadsRepo;
import com.javainuse.bean.ReferBean;
import com.javainuse.utility.ReferenceIdUniqueNumber;
import com.mysql.cj.util.Base64Decoder;
import com.javainuse.model.NimaiCustomer;
import com.javainuse.model.Refer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 115*60*60;

	@Autowired
	ReferralLeadsRepo refRepo;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.fieosecret}")
	private byte[] fieoSecret;
	
	@Value("${jwt.rxilsecret}")
	private byte[] rxilSecret;
	
	@Value("${referrer.fieo}")
	private String fieoRefId;
	
	@Value("${fieo.promocode}")
	private String fieoPromocode;
	
	@Autowired
	CustomerRepository cuRepo;
	
	@Autowired
	ReferRepository referRepo;
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public ReferralLeads retrieveClaims(String token,String referType) throws Exception
	{
		Claims body = null;
		
		byte[] key;
		if(referType.equalsIgnoreCase("fieo"))
		{
			key=fieoSecret;
			body = Jwts.parser()
					.setSigningKey(key)
	                //.setSigningKey(fieoSecret)
	                .parseClaimsJws(token)
	                .getBody();
			return retrieveFIEOClaims(body,referType);
		}
		else
		{
			key=rxilSecret;
			
			
			Base64Decoder base64Decoder = new Base64Decoder();
	        byte[] publicKeyBytes = base64Decoder.decode(key, 0, key.length);
	        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(keySpec);
	        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	                //.setRequireExpirationTime()
	                .setVerificationKey(publicKey)
	                .build();
	        JwtClaims jwtDecoded = jwtConsumer.processToClaims(token);
	        Map<String, Object> jwtClaims = jwtDecoded.getClaimsMap();
	        String username = (String) jwtClaims.get("pb_referral_code"); // "MChambe4"
	        System.out.println("jwtDecoded: "+jwtDecoded.toString());
	        
	        System.out.println("Claims: "+jwtClaims);
	        System.out.println("Referral code: "+username);
	        return retrieveRXILClaims(jwtClaims, referType);
	        
		}
		
		
		
		
		//System.out.println("EmailId: "+body.get("email"));
		//System.out.println("Mobile no: "+body.get("mobile"));
		//System.out.println("All claims: "+getAllClaimsFromToken(token));
		
	}
	
	private ReferralLeads retrieveRXILClaims(Map<String, Object> body, String referType) {
		// TODO Auto-generated method stub
		Date d=new Date();
		System.out.println("Claims: "+body.toString());
		String firstName="",lastName="";
		String[] userName=null;
		if(body.get("pb_name")==null)
		{
			firstName="";
			lastName="";
		}
		else
		{
			userName=body.get("pb_name").toString().split(" ");
			if(userName.length>2)
			{
				firstName=userName[0]+" "+userName[1];
				lastName=userName[2];
			}
			else
			{
				firstName=userName[0];
				try
				{
					lastName=userName[1];
				}
				catch(Exception e)
				{
					lastName="";
				}
			}
		}
		System.out.println("First name: "+firstName);
		System.out.println("Last name: "+lastName);
		String mobNo="";
		if(body.get("pb_mobile")==null)
			mobNo="";
		else
			mobNo=body.get("pb_mobile").toString().replace(" ", "");
		String upToNCharacters = mobNo.substring(0, Math.min(mobNo.length(), 3));
		System.out.println("Mobile 3 char: "+upToNCharacters);
		if(!upToNCharacters.contains("+91"))
			mobNo=mobNo;
		else
			mobNo=mobNo.substring(3);
		System.out.println("Mobile no: "+mobNo);
		String emailID="";
		if(body.get("pb_email")==null)
			emailID="";
		else
			emailID=body.get("pb_email").toString().toLowerCase();
		ReferralLeads rl=new ReferralLeads();
		ReferralLeads updateRl;
		if(body.get("pb_organization")==null)
			updateRl=refRepo.getRlDetails(emailID,mobNo,"");
		else
			updateRl=refRepo.getRlDetails(emailID,mobNo,body.get("pb_organization").toString());
		System.out.println("ReferralLeads: "+updateRl);
		if(updateRl==null ) {
			System.out.println("Saving Referral leads....");
			rl.setpId(""+body.get("pb_p_id"));
			if(referType.equalsIgnoreCase("fieo"))
				rl.setReferralId("FIEO");
			else
				rl.setReferralId("RXIL");
			//rl.setReferBy(fieoRefId);
			rl.setReferBy(""+body.get("pb_referral_code"));
			rl.setFirstName(""+firstName);
			rl.setLastName(""+lastName);
			rl.setOrgName(""+body.get("pb_organization"));
			rl.setDesignation(""+body.get("pb_designation"));
			rl.setEmailId(""+emailID);
			rl.setMobileNo(mobNo);
			//rl.setLandline(""+body.get("landline"));
			rl.setAddress(""+body.get("pb_street"));
			rl.setCity(""+body.get("pb_city"));
			rl.setState(""+body.get("pb_state"));
			if(body.get("pb_country")==null)
				rl.setCountry("INDIA");
			else
				rl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			rl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			if(referType.equalsIgnoreCase("rxil"))
			{
				if(body.get("pb_membership").toString().equalsIgnoreCase("NonMember"))
					rl.setMembershipStatus(""+body.get("pb_membership"));
				else
					rl.setMembershipStatus("Member");
			}
			else
				rl.setMembershipStatus(""+body.get("pb_membership"));
			rl.setRedirectUrl(""+body.get("pb_redirecturl"));
			rl.setIec(""+body.get("pb_iec"));
			rl.setFax(""+body.get("pb_fax"));
			rl.setGstin(""+body.get("pb_gstin"));
			rl.setInsertedBy(""+body.get("pb_name"));
			rl.setInsertedDate(d);
			rl.setModifiedBy(""+body.get("pb_name"));
			rl.setModifiedDate(d);
			rl.setJsonBody(body.toString());
			refRepo.save(rl);
			saveReferData("save",rl);
			return rl;
		}else {
			updateRl=refRepo.getOne(updateRl.getLeadId());
			updateRl.setpId(""+body.get("pb_p_id"));
			if(referType.equalsIgnoreCase("fieo"))
				updateRl.setReferralId("FIEO");
			else
				updateRl.setReferralId("RXIL");
			//updateRl.setReferBy(fieoRefId);
			updateRl.setReferBy(""+body.get("pb_referral_code"));
			updateRl.setFirstName(""+firstName);
			updateRl.setLastName(""+lastName);
			updateRl.setOrgName(""+body.get("pb_organization"));
			updateRl.setDesignation(""+body.get("pb_designation"));
			updateRl.setEmailId(""+emailID);
			
			updateRl.setMobileNo(mobNo);
			//rl.setLandline(""+body.get("landline"));
			updateRl.setAddress(""+body.get("pb_street"));
			updateRl.setCity(""+body.get("pb_city"));
			updateRl.setState(""+body.get("pb_state"));
			if(body.get("pb_country")==null)
				updateRl.setCountry("INDIA");
			else
				updateRl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			updateRl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			updateRl.setMembershipStatus(""+body.get("pb_membership"));
			updateRl.setRedirectUrl(""+body.get("pb_redirecturl"));
			updateRl.setIec(""+body.get("pb_iec"));
			updateRl.setFax(""+body.get("pb_fax"));
			updateRl.setGstin(""+body.get("pb_gstin"));
			//updateRl.setInsertedBy(""+body.get("pb_name"));
			//updateRl.setInsertedDate(d);
			updateRl.setModifiedBy(""+body.get("pb_name"));
			updateRl.setModifiedDate(d);
			updateRl.setJsonBody(body.toString());
			refRepo.save(updateRl);
			saveReferData("update",updateRl);
			return updateRl;
		}
		
	}

	public ReferralLeads retrieveFIEOClaims(Claims body,String referType)
	{
		Date d=new Date();
		System.out.println("Claims: "+body.toString());
		String firstName="",lastName="";
		String[] userName=null;
		if(body.get("pb_name")==null)
		{
			firstName="";
			lastName="";
		}
		else
		{
			userName=body.get("pb_name").toString().split(" ");
			if(userName.length>2)
			{
				firstName=userName[0]+" "+userName[1];
				lastName=userName[2];
			}
			else
			{
				firstName=userName[0];
				try
				{
					lastName=userName[1];
				}
				catch(Exception e)
				{
					lastName="";
				}
			}
		}
		System.out.println("First name: "+firstName);
		System.out.println("Last name: "+lastName);
		String mobNo="";
		if(body.get("pb_mobile")==null)
			mobNo="";
		else
			mobNo=body.get("pb_mobile").toString().replace(" ", "");
		String upToNCharacters = mobNo.substring(0, Math.min(mobNo.length(), 3));
		System.out.println("Mobile 3 char: "+upToNCharacters);
		if(!upToNCharacters.contains("+91"))
			mobNo=mobNo;
		else
			mobNo=mobNo.substring(3);
		System.out.println("Mobile no: "+mobNo);
		String emailID="";
		if(body.get("pb_email")==null)
			emailID="";
		else
			emailID=body.get("pb_email").toString().toLowerCase();
		ReferralLeads rl=new ReferralLeads();
		ReferralLeads updateRl;
		if(body.get("pb_organization")==null)
			updateRl=refRepo.getRlDetails(emailID,mobNo,"");
		else
			updateRl=refRepo.getRlDetails(emailID,mobNo,body.get("pb_organization").toString());
		System.out.println("ReferralLeads: "+updateRl);
		if(updateRl==null ) {
			System.out.println("Saving Referral leads....");
			rl.setpId(""+body.get("pb_p_id"));
			if(referType.equalsIgnoreCase("fieo"))
				rl.setReferralId("FIEO");
			else
				rl.setReferralId("RXIL");
			//rl.setReferBy(fieoRefId);
			rl.setReferBy(""+body.get("pb_referral_code"));
			rl.setFirstName(""+firstName);
			rl.setLastName(""+lastName);
			rl.setOrgName(""+body.get("pb_organization"));
			rl.setDesignation(""+body.get("pb_designation"));
			rl.setEmailId(""+emailID);
			rl.setMobileNo(mobNo);
			//rl.setLandline(""+body.get("landline"));
			rl.setAddress(""+body.get("pb_street"));
			rl.setCity(""+body.get("pb_city"));
			rl.setState(""+body.get("pb_state"));
			if(body.get("pb_country")==null)
				rl.setCountry("INDIA");
			else
				rl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			rl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			if(referType.equalsIgnoreCase("rxil"))
			{
				if(body.get("pb_membership").toString().equalsIgnoreCase("NonMember"))
					rl.setMembershipStatus(""+body.get("pb_membership"));
				else
					rl.setMembershipStatus("Member");
			}
			else
				rl.setMembershipStatus(""+body.get("pb_membership"));
			rl.setRedirectUrl(""+body.get("pb_redirecturl"));
			rl.setIec(""+body.get("pb_iec"));
			rl.setFax(""+body.get("pb_fax"));
			rl.setGstin(""+body.get("pb_gstin"));
			rl.setInsertedBy(""+body.get("pb_name"));
			rl.setInsertedDate(d);
			rl.setModifiedBy(""+body.get("pb_name"));
			rl.setModifiedDate(d);
			rl.setJsonBody(body.toString());
			refRepo.save(rl);
			saveReferData("save",rl);
			return rl;
		}else {
			updateRl=refRepo.getOne(updateRl.getLeadId());
			updateRl.setpId(""+body.get("pb_p_id"));
			if(referType.equalsIgnoreCase("fieo"))
				updateRl.setReferralId("FIEO");
			else
				updateRl.setReferralId("RXIL");
			//updateRl.setReferBy(fieoRefId);
			updateRl.setReferBy(""+body.get("pb_referral_code"));
			updateRl.setFirstName(""+firstName);
			updateRl.setLastName(""+lastName);
			updateRl.setOrgName(""+body.get("pb_organization"));
			updateRl.setDesignation(""+body.get("pb_designation"));
			updateRl.setEmailId(""+emailID);
			
			updateRl.setMobileNo(mobNo);
			//rl.setLandline(""+body.get("landline"));
			updateRl.setAddress(""+body.get("pb_street"));
			updateRl.setCity(""+body.get("pb_city"));
			updateRl.setState(""+body.get("pb_state"));
			if(body.get("pb_country")==null)
				updateRl.setCountry("INDIA");
			else
				updateRl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			updateRl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			updateRl.setMembershipStatus(""+body.get("pb_membership"));
			updateRl.setRedirectUrl(""+body.get("pb_redirecturl"));
			updateRl.setIec(""+body.get("pb_iec"));
			updateRl.setFax(""+body.get("pb_fax"));
			updateRl.setGstin(""+body.get("pb_gstin"));
			//updateRl.setInsertedBy(""+body.get("pb_name"));
			//updateRl.setInsertedDate(d);
			updateRl.setModifiedBy(""+body.get("pb_name"));
			updateRl.setModifiedDate(d);
			updateRl.setJsonBody(body.toString());
			refRepo.save(updateRl);
			saveReferData("update",updateRl);
			return updateRl;
		}
	}
	
	private void saveReferData(String flag, ReferralLeads rl) {
		// TODO Auto-generated method stub
		Refer refer = new Refer();
		Date dNow = new Date();
		NimaiCustomer cusdetails = cuRepo.getOne(rl.getReferBy());
		ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
		try {
			
			
			System.out.println("Referrer Details: "+cusdetails);
			System.out.println("Referrer First name: "+cusdetails.getFirstName());
			
			Refer updateReferl=referRepo.getRefelDetails(rl.getEmailId(),rl.getMobileNo(),rl.getOrgName());
			System.out.println("ReferDetails: "+updateReferl);
			//if(updateReferl==null) {
			if(flag.equalsIgnoreCase("save")) {
				String rid = refernceId.uniqueNumberReferenceId();
				
				refer.setReferenceId(rl.getLeadId().toString());
				refer.setUserid(cusdetails);
				refer.setFirstName(rl.getFirstName());
				refer.setLastName(rl.getLastName());
				refer.setEmailAddress(rl.getEmailId());
				refer.setReferrer_Email_Id(cusdetails.getEmailAddress());
				refer.setMobileNo(rl.getMobileNo());
				refer.setCompanyName(rl.getOrgName());
				refer.setCountryName(rl.getCountry());
				refer.setStatus("PENDING");
				refer.setInsertedBy(rl.getFirstName());
				refer.setInsertedDate(dNow);
				refer.setModifiedBy(rl.getFirstName());
				refer.setModifiedDate(dNow);
				if(rl.getReferralId().equalsIgnoreCase("fieo") && (rl.getMembershipStatus().equalsIgnoreCase("Member") || rl.getMembershipStatus().equalsIgnoreCase("NonPaidMember")))
					refer.setPromoCode(fieoPromocode);
				
				referRepo.save(refer);
			}
			else
			{
				updateReferl=referRepo.getOne(updateReferl.getId());
				updateReferl.setUserid(cusdetails);
				updateReferl.setFirstName(rl.getFirstName());
				updateReferl.setLastName(rl.getLastName());
				updateReferl.setEmailAddress(rl.getEmailId());
				updateReferl.setReferrer_Email_Id(cusdetails.getEmailAddress());
				updateReferl.setMobileNo(rl.getMobileNo());
				updateReferl.setCompanyName(rl.getOrgName());
				updateReferl.setCountryName(rl.getCountry());
				updateReferl.setStatus("PENDING");
				//updateReferl.setInsertedBy(rl.getFirstName());
				//updateReferl.setInsertedDate(dNow);
				updateReferl.setModifiedBy(rl.getFirstName());
				updateReferl.setModifiedDate(dNow);
				if(rl.getReferralId().equalsIgnoreCase("fieo") && (rl.getMembershipStatus().equalsIgnoreCase("Member") || rl.getMembershipStatus().equalsIgnoreCase("NonPaidMember")))
						updateReferl.setPromoCode(fieoPromocode);
				referRepo.save(updateReferl);
			}
		}
		catch (Exception e) {
			System.out.println("Exception: "+e);
			String rid = refernceId.uniqueNumberReferenceId();
			
			refer.setReferenceId(rl.getLeadId().toString());
			refer.setUserid(cusdetails);
			refer.setFirstName(rl.getFirstName());
			refer.setLastName(rl.getLastName());
			refer.setEmailAddress(rl.getEmailId());
			refer.setReferrer_Email_Id(cusdetails.getEmailAddress());
			refer.setMobileNo(rl.getMobileNo());
			refer.setCompanyName(rl.getOrgName());
			refer.setCountryName(rl.getCountry());
			refer.setStatus("PENDING");
			refer.setInsertedBy(rl.getFirstName());
			refer.setInsertedDate(dNow);
			refer.setModifiedBy(rl.getFirstName());
			refer.setModifiedDate(dNow);
			if(rl.getReferralId().equalsIgnoreCase("fieo") && (rl.getMembershipStatus().equalsIgnoreCase("Member") || rl.getMembershipStatus().equalsIgnoreCase("NonPaidMember")))
				refer.setPromoCode(fieoPromocode);
			
			referRepo.save(refer);
		}
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		System.out.println("Username: "+userDetails.getUsername());
		//claims.put("emailId", "hello");
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
