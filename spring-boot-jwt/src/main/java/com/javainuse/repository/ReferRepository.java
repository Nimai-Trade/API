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

	@Query(value = "select * from nimai_m_refer rl where rl.EMAIL_ADDRESS= :emailAddress and rl.FIRST_NAME= :firstName and rl.LAST_NAME= :lastName",nativeQuery = true)
	Refer getRefelDetails(String emailAddress, String firstName, String lastName);

	
}
