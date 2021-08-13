package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiMSubscription;
import com.nimai.ucm.entity.NimaiSubscriptionDetails;

@Repository
public interface getRegisterUserRepo extends JpaRepository<NimaiCustomer, String> {
	
	
	@Query("select nc from NimaiCustomer nc where nc.userid= (:userid)")
	NimaiCustomer findRegisterUser(@Param("userid") String userid);

	@Query("select nc from NimaiCustomer nc where nc.userid= (:userid)")
	List<NimaiCustomer> findRegisterUserById(@Param("userid") String userid);
	
	@Query("select nc from NimaiCustomer nc where nc.accountSource= (:userid) and nc.accountType='REFER'")
	List<NimaiCustomer> findRegisterUserByReferrerUser(@Param("userid") String userid);
 
	@Query(value="SELECT DATEDIFF(nsd.SPLAN_END_DATE,CURDATE()) FROM nimai_subscription_details nsd \r\n" + 
			"WHERE nsd.userid=(:userid) and nsd.STATUS=(:status)", nativeQuery = true )
	Integer findExpiryIn(@Param("userid") String userid , @Param("status") String status);

	@Query(value="SELECT ((nsd.SUBSCRIPTION_AMOUNT+nsd.VAS_AMOUNT)-(nsd.DISCOUNT)) FROM nimai_subscription_details nsd \r\n" + 
			"	WHERE nsd.userid=(:userid) and nsd.`STATUS`='Active'", nativeQuery = true )
	Integer findEarning(@Param("userid") String userid);

	@Query(value="select emp_first_name from nimai_m_employee where emp_code=(:rmId)", nativeQuery = true )
	String findRmFirstName(@Param("rmId") String rmId);
	
	@Query(value="select emp_last_name from nimai_m_employee where emp_code=(:rmId)", nativeQuery = true )
	String findRmLastName(@Param("rmId") String rmId);

	@Query(value="select subscription_name from nimai_subscription_details where userid=(:userid) and status='Active'", nativeQuery = true )
	String findSubscriptionName(@Param("userid") String userid);

	@Query(value="select subscription_amount from nimai_subscription_details where userid=(:userid) and status='Active'", nativeQuery = true )
	Integer findSubscriptionFee(@Param("userid") String userid);

	@Query(value="SELECT sum((nsd.SUBSCRIPTION_AMOUNT+nsd.VAS_AMOUNT)-(nsd.DISCOUNT)) FROM nimai_subscription_details nsd \r\n" + 
			"	WHERE nsd.userid=(:userid) and nsd.PAYMENT_STATUS!='Rejected' AND nsd.PAYMENT_STATUS!='Pending'", nativeQuery = true )
	Integer findTotalEarning(@Param("userid")String userid);

	@Query(value="select subscription_id from nimai_subscription_details where userid=(:userid) and status='Active'", nativeQuery = true )
	String findSubscriptionId(@Param("userid") String userid);
	
	

	@Query(value="select * from nimai_m_subscription where subscription_id=(:subscriptionId) and status='Active'", nativeQuery = true )
	NimaiMSubscription findSubscriptionCurrency(@Param("subscriptionId")String subscriptionId);

	@Query(value="select * from NIMAI_M_CUSTOMER nc where nc.EMAIL_ADDRESS= (:emailAddress)",nativeQuery = true)
	NimaiCustomer findRegisterUserByReferrerEmail(@Param("emailAddress") String emailAddress);

	@Query(value="SELECT nmc.CURRENCY AS currency,nsd.SPLAN_END_DATE AS splanENddate\r\n" + 
			",((nsd.SUBSCRIPTION_AMOUNT + nsd.VAS_AMOUNT)-(nsd.DISCOUNT)) AS grandAmount,nc.COMPANY_NAME AS \r\n" + 
			"companyNAme,nc.COUNTRY_NAME AS countryName,DATEDIFF(nsd.SPLAN_END_DATE,CURDATE()) AS expiredInDays FROM nimai_m_customer nc JOIN nimai_subscription_details\r\n" + 
			"nsd ON nc.USERID=nsd.userid JOIN nimai_m_subscription nmc ON nsd.SUBSCRIPTION_ID\r\n" + 
			"=nmc.SUBSCRIPTION_ID WHERE nc.USERID=:userid \r\n" + 
			"AND nsd.`STATUS`=:status AND nmc.`STATUS`=:status",nativeQuery = true)
	List<Object[]> findRegisterUser(@Param("userid")String userid,@Param("status") String status);
}
