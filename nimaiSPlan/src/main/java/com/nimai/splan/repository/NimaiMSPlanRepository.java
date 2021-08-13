package com.nimai.splan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nimai.splan.model.NimaiMCustomer;
import com.nimai.splan.model.NimaiMSubscription;

public interface NimaiMSPlanRepository extends JpaRepository<NimaiMSubscription, String> {

	@Query("From NimaiMSubscription s where s.customerType=:customerType AND s.sPLanCountry=:countrName AND s.status = 'Active'" )
	List<NimaiMSubscription> findByCountry(String customerType, String countrName);
	
	@Query("From NimaiMSubscription s where s.subscriptionId=:subscriptionid" )
	NimaiMSubscription findDetailBySubscriptionId(String subscriptionid);

	@Query("From NimaiMSubscription s where s.customerType=:string AND s.status = 'Active'" )
	List<NimaiMSubscription> findByCustomerType(String string);

	@Query(value="SELECT system_config_entity_value from nimai_system_config where system_config_entity='invoice_gst'", nativeQuery = true )
	Double getGSTValue();
	
}
