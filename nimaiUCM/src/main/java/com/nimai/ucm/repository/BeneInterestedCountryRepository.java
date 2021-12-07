package com.nimai.ucm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.BeneInterestedCountry;

public interface BeneInterestedCountryRepository extends JpaRepository<BeneInterestedCountry, Long> {

	@Modifying
	@Query("delete from BeneInterestedCountry b where b.userId.userid =:userId")
	void deleteBeneInterestedCountryUserId(@Param("userId") String userId);
}
