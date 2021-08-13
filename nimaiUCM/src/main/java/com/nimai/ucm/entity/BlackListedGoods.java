package com.nimai.ucm.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "NIMAI_F_BLKGOODS")
public class BlackListedGoods {

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GOODS_ID")
	private Long goods_ID;


	@Column(name = "GOODS_NAME")
	private String goodsName;

	@Column(name = "INSERTED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertedDate;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	
	@Column(name = "GOODS_MID")
	private Integer goodsMId;
	


	@JoinColumn(name = "USERID", referencedColumnName = "USERID")
	@ManyToOne(optional = false)
	private NimaiCustomer userId;

	public Long getGoods_ID() {
		return goods_ID;
	}

	public void setGoods_ID(Long goods_ID) {
		this.goods_ID = goods_ID;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Date getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public NimaiCustomer getUserId() {
		return userId;
	}

	public void setUserId(NimaiCustomer userId) {
		this.userId = userId;
	}

	public Integer getGoodsMId() {
		return goodsMId;
	}

	public void setGoodsMId(Integer goodsMId) {
		this.goodsMId = goodsMId;
	}

}
