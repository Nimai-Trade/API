package com.nimai.ucm.service;

import java.util.List;

import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;

public interface PersonalDetailsService {

	public List<InterestedCountry> getIntCountries(NimaiCustomer userId);

	public List<BlackListedGoods> getGoodsName(NimaiCustomer userId);

}
