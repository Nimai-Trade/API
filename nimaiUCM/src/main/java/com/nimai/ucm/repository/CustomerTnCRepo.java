package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nimai.ucm.entity.CustomerTnC;
import com.nimai.ucm.entity.NimaiCustomer;

public interface CustomerTnCRepo extends JpaRepository<CustomerTnC , Integer>{

}
