package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiToken;


@Repository
public interface UserTokenRepository extends JpaRepository<NimaiToken, String>
{
	 @Query("from NimaiToken where userId= :userId and token= :token")
	 NimaiToken isTokenExists(@Param("userId")String userId,@Param("token")String token);
}
