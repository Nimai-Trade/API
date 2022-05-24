package com.nimai.lc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.lc.entity.NimaiClient;

public interface NimaiClientRepository extends JpaRepository<NimaiClient, String>
{
	@Query("SELECT nc FROM NimaiClient nc WHERE nc.userid= (:userId) or (nc.accountSource= (:userId) and nc.accountType='SUBSIDIARY')")
	List<NimaiClient> findCreditTransactionByUserId(@Param("userId") String userId);
	
	@Query("SELECT nc FROM NimaiClient nc WHERE nc.userid= (:userId)")
	List<NimaiClient> findCreditTransactionByOnlyUserId(@Param("userId") String userId);
	
	@Query("SELECT nc FROM NimaiClient nc WHERE nc.userid= (:userId)")
	NimaiClient findCreditTransactionByUserIdForPasscode(@Param("userId") String userId);
	
	@Query("SELECT nc FROM NimaiClient nc WHERE nc.companyName= (:subsidiaryName) and nc.accountSource= (:userId) and nc.accountType='SUBSIDIARY'")
	List<NimaiClient> findCreditTransactionByUserIdSubsidiary(@Param("userId") String userId,@Param("subsidiaryName") String subsidiaryName);
	
	@Query("SELECT nc FROM NimaiClient nc WHERE nc.userid= (:userId) or nc.accountSource= (:userId) and nc.accountType='BANKUSER'")
	List<NimaiClient> findCreditTransactionByBankUserId(@Param("userId") String userId);

	@Query("SELECT nc.companyName FROM NimaiClient nc WHERE nc.userid= (:userId)")
	String findCompanyNameByUserId(String userId);

	@Query(value="select * from nimai_m_customer\r\n" + 
			"nc where nc.USERID!=userId and \r\n" + 
			"nc.Bank_type='UNDERWRITER' and nc.KYC_STATUS='Approved'\r\n" + 
			"AND (nc.PAYMENT_STATUS='Approved' or nc.PAYMENT_STATUS='Success')",nativeQuery = true)
	List<NimaiClient> getAllElBank(String userId);

}
