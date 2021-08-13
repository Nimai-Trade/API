package com.nimai.splan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.splan.model.NimaiAdvisory;
import com.nimai.splan.model.NimaiSubscriptionVas;

@Repository
public interface NimaiAdvisoryRepo extends JpaRepository<NimaiAdvisory, String> {

	@Query("SELECT na FROM NimaiAdvisory na WHERE na.customerType=(:customerType) and na.country_name = (:country_name) and na.status='Active'")
	List<NimaiAdvisory> findByCountryName(@Param("country_name") String country_name, @Param("customerType") String customerType);

	@Query("SELECT na FROM NimaiAdvisory na WHERE na.vas_id = (:vasId) and na.status='Active'")
	NimaiAdvisory getDataByVasId(int vasId);
	
	@Query("SELECT na FROM NimaiAdvisory na WHERE na.vas_id = (:vasId)")
	NimaiAdvisory getVASDetByVasId(int vasId);
	
	@Query(value = "select pricing from nimai_m_vas where vas_id=(:vasId)", nativeQuery = true)
	Double findPricingByVASId(Integer vasId);
	
	@Query(value="SELECT system_config_entity_value from nimai_system_config where system_config_entity='invoice_gst'", nativeQuery = true )
	Double getGSTValue();
}
