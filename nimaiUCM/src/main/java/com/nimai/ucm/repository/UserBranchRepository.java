package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.UserBranchEntity;

public interface UserBranchRepository extends JpaRepository<UserBranchEntity , Integer>{

	@Query(value ="SELECT * FROM NIMAI_M_BRANCH nm WHERE nm.USERID =:userId GROUP BY nm.email_id" , nativeQuery = true)
	List<UserBranchEntity> getBranchUserList(@Param("userId") String userId);

	@Query(value ="SELECT * FROM NIMAI_M_BRANCH nm WHERE nm.EMAIL_ID =:emailId order by nm.id desc limit 1" , nativeQuery = true)
	UserBranchEntity getBranchUser(@Param("emailId")String email_id);

}
