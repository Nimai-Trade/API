package com.nimai.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nimai.uam.entity.NimaiEmployeeLoginAttempt;
import com.nimai.uam.entity.NimaiMLogin;

public interface NimaiEmployeeLoginAttemptRepo  extends JpaRepository<NimaiEmployeeLoginAttempt, Long> {

	@Query("FROM NimaiEmployeeLoginAttempt n where n.empCode = :userId")
	NimaiEmployeeLoginAttempt getcountDetailsByEmpCode(String userId);


	@Query("FROM NimaiEmployeeLoginAttempt n where n.id = :id")
	NimaiEmployeeLoginAttempt getOne(int id);

}
