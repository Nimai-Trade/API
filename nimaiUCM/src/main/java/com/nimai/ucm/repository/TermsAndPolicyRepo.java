package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.TermsAndPolicy;

public interface TermsAndPolicyRepo extends JpaRepository<TermsAndPolicy , Integer>
{
	@Query(value ="SELECT * FROM NIMAI_TERMS_POLICY where status='ACTIVE'" , nativeQuery = true)
	TermsAndPolicy getTermsAndPolicyDetails();
}
