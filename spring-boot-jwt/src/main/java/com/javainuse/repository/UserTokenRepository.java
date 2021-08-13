package com.javainuse.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.javainuse.model.NimaiToken;


@Repository
public interface UserTokenRepository extends JpaRepository<NimaiToken, String>
{
	@Query(value="SELECT email_address from nimai_m_customer where userid=(:userId)", nativeQuery = true)
	String findEmailIdByUseridOnly(@Param("userId") String userId);
}
