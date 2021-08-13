package com.nimai.uam.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimai.uam.entity.NimaiClient;
import com.nimai.uam.entity.NimaiMLogin;

@Repository
public interface LoginRepository extends JpaRepository<NimaiMLogin, Long>{

	
	@Query("FROM NimaiMLogin n where n.userid.userid = :userid and n.isActPassed='INACTIVE'")
	Optional<NimaiMLogin> findInactiveLoginByUserId(@Param("userid") String userid);
	
	@Query("FROM NimaiMLogin n where n.userid.userid = :userid and n.isActPassed='ACTIVE' and n.password= :oldpassword")
	Optional<NimaiMLogin> findActiveLoginByUserId(@Param("userid") String userid,@Param("oldpassword") String oldpassword);
	
	@Query("FROM NimaiMLogin n where n.userid.userid = :userid")
	Optional<NimaiMLogin> findByUserId(@Param("userid") String userid);

	@Query("FROM NimaiMLogin n where n.token = :token and n.isActPassed='INACTIVE'")
	NimaiMLogin findUserByToken(String token);

	@Query("select n.token FROM NimaiMLogin n WHERE n.token=:tokenKey")
	String checkUserToken(String tokenKey);

	//@Modifying
	//@Transactional
	//@Query(value ="update LAST_LOGIN_TIME=NOW() FROM nimai_m_login n WHERE n.userid = :userId",nativeQuery = true)
	//void updateLastLogin(String userId);

	@Modifying
	@Transactional
	@Query("update NimaiMLogin n set n.lastLoginTime=:d WHERE n.userid.userid = :userId")
	void updateLastLogin(String userId,Date d);

	@Query(value ="SELECT COUNT(*)\n" + 
			"  FROM nimai_m_login\n" + 
			" WHERE LAST_LOGIN_TIME >= CURDATE() - INTERVAL 1 DAY and (userid like 'CU%' or userid like 'BC%')",nativeQuery = true)
	int getCountOfLastLoginCustomer();
	
	@Query(value ="SELECT COUNT(*)\n" + 
			"  FROM nimai_m_login\n" + 
			" WHERE LAST_LOGIN_TIME >= CURDATE() - INTERVAL 1 DAY and userid like 'BA%'",nativeQuery = true)
	int getCountOfLastLoginBank();

	@Modifying
	@Transactional
	@Query(value ="delete from nimai_m_token where user_id=:userId",nativeQuery = true)
	void deleteToken(String userId);

	@Query("FROM NimaiMLogin n where n.token = :newtoken and n.isActPassed='ACTIVE'")
	NimaiMLogin findUserByActiveToken(String newtoken);
	
	
	@Query("FROM NimaiMLogin")
	List<NimaiMLogin> loginList(String newtoken);
	
	
	
	
}
