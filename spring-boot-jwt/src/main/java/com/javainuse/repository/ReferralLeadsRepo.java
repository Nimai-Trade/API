package com.javainuse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.javainuse.model.NimaiToken;
import com.javainuse.model.ReferralLeads;

@Repository
public interface ReferralLeadsRepo extends JpaRepository<ReferralLeads, Integer>
{
	@Query(value="select distinct * from referral_leads  rl where rl.EMAIL=:emailId and rl.MOBILE=:mobileNo group by email,mobile", nativeQuery=true)
	ReferralLeads getRlDetails(String emailId,String mobileNo);
}
