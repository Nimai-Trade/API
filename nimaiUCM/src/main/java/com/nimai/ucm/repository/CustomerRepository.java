package com.nimai.ucm.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.TermsAndPolicy;

@Repository
public interface CustomerRepository  extends JpaRepository<NimaiCustomer , String>{
	
	@Query(value ="SELECT * FROM NIMAI_M_CUSTOMER WHERE USERID =:userid" , nativeQuery = true)
	NimaiCustomer findByUSERID(@Param("userid") String userid);

	

	@Query(value ="SELECT * FROM NIMAI_M_CUSTOMER nm WHERE nm.ACCOUNT_SOURCE =:userid AND nm.KYC_STATUS=:kycStatus" , nativeQuery = true)
	List<NimaiCustomer> getSubUserList(@Param("userid")String userid,@Param("kycStatus")String kycStatus);


	
}
