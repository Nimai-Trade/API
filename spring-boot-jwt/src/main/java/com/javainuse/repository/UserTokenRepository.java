package com.javainuse.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.javainuse.model.NimaiToken;


@Repository
public interface UserTokenRepository extends JpaRepository<NimaiToken, String>
{
	@Query(value="SELECT email_address from nimai_m_customer where userid=(:userId)", nativeQuery = true)
	String findEmailIdByUseridOnly(@Param("userId") String userId);
	
	@Query(value="SELECT nr.promo_code from nimai_m_refer nr where nr.email_address=(:emailId) and nr.EMAIL_ADDRESS=(select \n"+
					"nc.EMAIL_ADDRESS from nimai_m_customer nc where nc.LEAD_ID!=0 and nr.EMAIL_ADDRESS=nc.EMAIL_ADDRESS)", nativeQuery = true)
	String findPromoCodeByEmailId(@Param("emailId") String emailId);
	
	
}
