package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;

public interface InterestedCountryRepository extends JpaRepository<InterestedCountry, Long> {

	List<InterestedCountry> findByUserId(NimaiCustomer nc);
	
	@Modifying
	@Query("delete from InterestedCountry b where b.userId.userid =:userId")
	void deleteInterestedCountryUserId(@Param("userId") String userId);



}
