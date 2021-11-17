package com.javainuse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.javainuse.model.NimaiCustomer;
import com.javainuse.model.Refer;

@Repository
public interface ReferRepository extends JpaRepository<Refer, Integer> {

	@Query(value = "select * from nimai_m_refer rl where rl.EMAIL_ADDRESS= :emailAddress or rl.MOBILE_NO= :mobileNo or COMPANY_NAME= :orgName ORDER BY rl.ID DESC LIMIT 1",nativeQuery = true)
	Refer getRefelDetails(String emailAddress, String mobileNo,String orgName);

	
}
