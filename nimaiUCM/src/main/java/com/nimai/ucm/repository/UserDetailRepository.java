package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.NimaiCustomer;


@Repository
public interface UserDetailRepository extends JpaRepository<NimaiCustomer, String>,  JpaSpecificationExecutor<NimaiCustomer>{
	
	boolean existsByEmailAddress(String emailAddress);
	
	 @Query("from NimaiCustomer where emailAddress= :emailId")
	 NimaiCustomer isEmailExsists(String emailId);
	 
	 @Query("select nc from NimaiCustomer nc where nc.accountSource= (:userid) and nc.accountType='SUBSIDIARY'")
	 List<NimaiCustomer> findRegisterUser(@Param("userid") String userid);
	 
	 @Query("select nc from NimaiCustomer nc where nc.accountSource= (:userid) and nc.accountType='BANKUSER'")
	 List<NimaiCustomer> findAdditionalRegisterUser(@Param("userid") String userid);
	 
	 @Query("select nc.registredCountry from NimaiCustomer nc where nc.userid=(:userid)")
	 String findCountry(@Param("userid") String userid);

	 @Modifying
	 @Query(value = "update nimai_subscription_details set SUBSIDIARIES_UTILIZED_COUNT=ifnull(SUBSIDIARIES_UTILIZED_COUNT,0)+1 where userid=(:userId) and status='ACTIVE'", nativeQuery = true)
	 void updateSubsidiaryCount(String userId);

	 @Query(value = "select SUBSIDIARIES from nimai_subscription_details where userid=(:userId) and status='ACTIVE'", nativeQuery = true)
	String getSubsidiaryCount(String userId);

	 @Query(value = "select SUBSIDIARIES_UTILIZED_COUNT from nimai_subscription_details where userid=(:userId) and status='ACTIVE'", nativeQuery = true)
	String getSubsidiaryUtilizedCount(String userId);

	 @Query(value = "select count(*) from nimai_mm_transaction where user_id=(:userid)", nativeQuery = true)
	Integer getCreditUtilized(String userid);

	 @Query(value = "select count(*) from nimai_m_quotation where userid=(:userid) and quotation_status like '%Placed'", nativeQuery = true)
	Integer getQuoteReceived(String userid);

	 @Query(value = "select count(*) from nimai_m_quotation where bank_userid=(:userid)", nativeQuery = true)
		Integer getCreditUtilizedOfAdditionalUser(String userid);
	 
	 @Query(value = "select count(*) from nimai_m_quotation where bank_userid=(:userid) and quotation_status like '%Placed'", nativeQuery = true)
		Integer getQuotePlaced(String userid);
	 
	 @Query(value = "select * from nimai_m_customer where email_address=(:emailAddress) and account_type='Refer'", nativeQuery = true)
	NimaiCustomer getUserIdByEmailId(@Param("emailAddress") String emailAddress);

	 @Query(value = "select grand_amount from nimai_subscription_details where userid=(:referUserId) and status='Active'", nativeQuery = true) 
	String getGrandAmountByReferUserId(String referUserId);

	 @Query(value = "select subscription_id from nimai_subscription_details where userid=(:referUserId) and status='Active'", nativeQuery = true) 
	List getActivePlanByReferUserId(String referUserId);

	 @Query(value = "select subscription_id from nimai_subscription_details where userid=(:referUserId) and status='Inactive'", nativeQuery = true) 
	 List getInactivePlanByReferUserId(String referUserId);
	
	@Query(value = "select subscription_name from nimai_subscription_details where userid=(:referUserId) and status='Active'", nativeQuery = true) 	
	String getPlanPurchasedByReferUserId(String referUserId);

	@Modifying
	@Query(value = "delete from nimai_f_owner where owner_id=(:ownerID)", nativeQuery = true)
	void removeOwnerDet(Long ownerID);
	 
	 //@Query("from NimaiCustomer where userid= :userid")
	 //NimaiCustomer findUser(String userid);
	 
	 
	
}
