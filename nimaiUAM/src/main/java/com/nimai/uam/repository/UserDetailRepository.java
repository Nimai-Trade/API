package com.nimai.uam.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.uam.entity.NimaiClient;



@Repository
public interface UserDetailRepository extends JpaRepository<NimaiClient, String>,  JpaSpecificationExecutor<NimaiClient>{
	
	boolean existsByEmailAddress(String emailAddress);
	
	@Query("FROM NimaiClient n where n.userid = :userId and n.accountType='SUBSIDIARY'")
	NimaiClient checkSubsidiary(@Param("userId") String userId);

	@Query("FROM NimaiClient n where n.userid = :userId and n.accountType='SUBSIDIARY' and (n.accountStatus='ACTIVE' or n.accountStatus='PENDING')")
	NimaiClient checkSubsidiaryActive(@Param("userId") String userId);
	
}
