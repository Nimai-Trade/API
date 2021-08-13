package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.NimaiSubscriptionDetails;
import com.nimai.ucm.entity.UserBranchEntity;

public interface NimaiSubscriptionDetailsRepo extends JpaRepository<NimaiSubscriptionDetails , Integer> {

	
	@Query(value = "from NimaiSubscriptionDetails re where re.userid.userid= (:userId)")
	List<NimaiSubscriptionDetails> finSplanByReferId(@Param("userId") String userId);
	
//	@Query(value="from NimaiSubscriptionDetails nsd where nsd.userid.userid= (:userid) "
//			+ "and nsd.status=  (:splanstatus)", nativeQuery = true )
	@Query(value="select * from nimai_subscription_details ns where ns.userid=(:userid) and ns.STATUS=(:splanstatus)", nativeQuery = true )
	NimaiSubscriptionDetails findSubscriptionDetails(@Param("userid") String userid,@Param("splanstatus") String status);

}
