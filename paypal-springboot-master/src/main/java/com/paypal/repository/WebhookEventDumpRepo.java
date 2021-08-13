package com.paypal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.model.WebhookEventDump;

@Transactional
@Repository
public interface WebhookEventDumpRepo extends JpaRepository<WebhookEventDump, Integer> {

	
	
}
