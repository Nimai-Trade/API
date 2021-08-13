package com.nimai.ucm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.NimaiCustomer;

public interface BlackListedGoodsRepository extends JpaRepository<BlackListedGoods, Long> {

	List<BlackListedGoods> findByUserId(NimaiCustomer nc);
	
	@Modifying
	@Query("delete from BlackListedGoods b where b.userId.userid =:userId")
	void deleteBlackListedGoodsUserId(@Param("userId") String userId);

}
