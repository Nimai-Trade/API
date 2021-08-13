package com.nimai.ucm.bean;

public class BlackListedGoodsBean {
	
	private Long goods_ID;
	private Integer goodsMId;
	private String blackListGoods;
	
	public Integer getGoodsMId() {
		return goodsMId;
	}

	public void setGoodsMId(Integer goodsMId) {
		this.goodsMId = goodsMId;
	}
	
	

	public String getBlackListGoods() {
		return blackListGoods;
	}

	public void setBlackListGoods(String blackListGoods) {
		this.blackListGoods = blackListGoods;
	}

	public Long getGoods_ID() {
		return goods_ID;
	}

	public void setGoods_ID(Long goods_ID) {
		this.goods_ID = goods_ID;
	}
	
	
	
	
}
