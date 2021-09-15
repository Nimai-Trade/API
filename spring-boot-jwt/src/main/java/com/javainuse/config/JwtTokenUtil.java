package com.javainuse.config;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

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

	public ReferralLeads retrieveClaims(String token) throws Exception
	{
		Date d=new Date();
		byte[] key=fieoSecret;
		Claims body = Jwts.parser()
				.setSigningKey(key)
                //.setSigningKey(fieoSecret)
                .parseClaimsJws(token)
                .getBody();
		
		System.out.println("Claims: "+body.toString());
		String[] userName=body.get("pb_name").toString().split(" ");
		String firstName,lastName;
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
		System.out.println("First name: "+firstName);
		System.out.println("Last name: "+lastName);
		String mobNo=body.get("pb_mobile").toString().replace(" ", "");
		String upToNCharacters = mobNo.substring(0, Math.min(mobNo.length(), 3));
		System.out.println("Mobile 3 char: "+upToNCharacters);
		if(!upToNCharacters.contains("+91"))
			mobNo=mobNo;
		else
			mobNo=mobNo.substring(3);
		System.out.println("Mobile no: "+mobNo);
		String emailID=body.get("pb_email").toString().toLowerCase();
		ReferralLeads rl=new ReferralLeads();
		ReferralLeads updateRl=refRepo.getRlDetails(emailID,mobNo);
		
		if(updateRl==null ) {
			rl.setpId(""+body.get("pb_p_id"));
			rl.setReferralId("FIEO");
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
			rl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			rl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			rl.setMembershipStatus(""+body.get("pb_membership"));
			rl.setRedirectUrl(""+body.get("pb_redirecturl"));
			rl.setIec(""+body.get("pb_iec"));
			rl.setFax(""+body.get("pb_fax"));
			rl.setGstin(""+body.get("pb_gstin"));
			rl.setInsertedBy(""+body.get("pb_name"));
			rl.setInsertedDate(d);
			rl.setModifiedBy(""+body.get("pb_name"));
			rl.setModifiedDate(d);
			refRepo.save(rl);
			saveReferData("save",rl);
			return rl;
		}else {
			updateRl=refRepo.getOne(updateRl.getLeadId());
			updateRl.setpId(""+body.get("pb_p_id"));
			updateRl.setReferralId("FIEO");
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
			updateRl.setCountry(""+body.get("pb_country").toString().toUpperCase());
			updateRl.setPincode(""+body.get("pb_pincode"));
			//rl.setMembershipStatus("Paid Member");
			updateRl.setMembershipStatus(""+body.get("pb_membership_status"));
			updateRl.setRedirectUrl(""+body.get("pb_redirecturl"));
			updateRl.setIec(""+body.get("pb_iec"));
			updateRl.setFax(""+body.get("pb_fax"));
			updateRl.setGstin(""+body.get("pb_gstin"));
			//updateRl.setInsertedBy(""+body.get("pb_name"));
			//updateRl.setInsertedDate(d);
			updateRl.setModifiedBy(""+body.get("pb_name"));
			updateRl.setModifiedDate(d);
			refRepo.save(updateRl);
			saveReferData("update",updateRl);
			return updateRl;
		}
		
		
		//System.out.println("EmailId: "+body.get("email"));
		//System.out.println("Mobile no: "+body.get("mobile"));
		//System.out.println("All claims: "+getAllClaimsFromToken(token));
		
	}
	
	private void saveReferData(String flag, ReferralLeads rl) {
		// TODO Auto-generated method stub
		Refer refer = new Refer();
		Date dNow = new Date();
		try {
			ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
			NimaiCustomer cusdetails = cuRepo.getOne(rl.getReferBy());
			Refer updateReferl=referRepo.getRefelDetails(rl.getEmailId(),rl.getMobileNo());
			//if(updateReferl==null) {
			if(flag.equalsIgnoreCase("save")) {
				String rid = refernceId.uniqueNumberReferenceId();
				
				refer.setReferenceId(rid);
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
				if(rl.getMembershipStatus().equalsIgnoreCase("Member") || rl.getMembershipStatus().equalsIgnoreCase("NonPaidMember"))
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
				if(rl.getMembershipStatus().equalsIgnoreCase("Member") || rl.getMembershipStatus().equalsIgnoreCase("NonPaidMember"))
						updateReferl.setPromoCode(fieoPromocode);
				referRepo.save(updateReferl);
			}
		}
		catch (Exception e) {
			System.out.println("Exception: "+e);
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
