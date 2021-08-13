package com.paypal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="webhook_event")
public class WebhookEventDump 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="order_id")
	private String orderId;
	
	@Column(name="invoice_id")
	private String invoiceId;
	
	@Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="payload_dump")
	private String payloadDump;

	@Column(name="CAPTURE_DATE")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date captureDate;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayloadDump() {
		return payloadDump;
	}

	public void setPayloadDump(String payloadDump) {
		this.payloadDump = payloadDump;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}
	
	
}
