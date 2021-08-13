package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.ucm.entity.NimaiMLogin;

public interface LoginRepository extends JpaRepository<NimaiMLogin, Integer>{
	
}
