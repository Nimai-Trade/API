package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.Refer;
import com.nimai.ucm.entity.ReferralLeads;

@Repository
public interface ReferralLeadsRepo extends JpaRepository<ReferralLeads, Integer> {
	
	@Query(value = "select userid from nimai_m_customer where lead_id=(:leadid)", nativeQuery = true)
	String getUserId(Integer leadid);
	
	@Query(value = "select rl from ReferralLeads rl where rl.referBy=:referUserId order by rl.leadId desc")
	List<ReferralLeads> getSortedReferralLeads(String referUserId);
}