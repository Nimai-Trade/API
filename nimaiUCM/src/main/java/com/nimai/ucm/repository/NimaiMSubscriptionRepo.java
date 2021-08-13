package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.NimaiMSubscription;



public interface NimaiMSubscriptionRepo extends JpaRepository<NimaiMSubscription , Integer> {
	@Query(value="from NimaiMSubscription where subscriptionId=(:subscriptionId) and status=(:status)" )
	NimaiMSubscription findSubscriptionCurrency(@Param("subscriptionId")String subscriptionId,@Param("status")String status);
	
	@Query(value="SELECT system_config_entity_value from nimai_system_config where system_config_entity='referrer_earnings'", nativeQuery = true )
	String getReferEarningsPercent();
}
