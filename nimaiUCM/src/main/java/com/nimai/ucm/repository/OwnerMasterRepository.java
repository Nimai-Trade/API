package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.OwnerMaster;

public interface OwnerMasterRepository extends JpaRepository<OwnerMaster, Long>{

	List<OwnerMaster> findByUserId(NimaiCustomer nc);
}

