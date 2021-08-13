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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.javainuse.model.JwtRequest;
import com.javainuse.model.ReferralLeads;
import com.javainuse.repository.ReferralLeadsRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;

	@Autowired
	ReferralLeadsRepo refRepo;
	
	@Value("${jwt.secret}")
	private String secret;

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
		Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
		
		System.out.println("Claims: "+body.toString());
		String[] userName=body.get("name").toString().split(" ");
		System.out.println("First name: "+userName[0]);
		System.out.println("Last name: "+userName[1]);
		
		ReferralLeads rl=new ReferralLeads();
		rl.setReferralId("FIEO");
		rl.setFirstName(""+userName[0]);
		rl.setLastName(""+userName[1]);
		rl.setOrgName(""+body.get("organization_name"));
		rl.setDesignation(""+body.get("designation"));
		rl.setEmailId(""+body.get("email"));
		rl.setMobileNo(""+body.get("mobile").toString().replace(" ", ""));
		rl.setLandline(""+body.get("landline"));
		rl.setAddress(""+body.get("address"));
		rl.setCity(""+body.get("city"));
		rl.setState(""+body.get("state"));
		rl.setCountry(""+body.get("country").toString().toUpperCase());
		rl.setInsertedBy(""+body.get("name"));
		rl.setInsertedDate(d);
		rl.setModifiedBy(""+body.get("name"));
		rl.setModifiedDate(d);
		
		refRepo.save(rl);
		
		//System.out.println("EmailId: "+body.get("email"));
		//System.out.println("Mobile no: "+body.get("mobile"));
		//System.out.println("All claims: "+getAllClaimsFromToken(token));
		return rl;
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
