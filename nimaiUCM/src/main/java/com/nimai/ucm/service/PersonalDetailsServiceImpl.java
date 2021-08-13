package com.nimai.ucm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.repository.BlackListedGoodsRepository;
import com.nimai.ucm.repository.InterestedCountryRepository;

@Service
@Transactional
public class PersonalDetailsServiceImpl implements PersonalDetailsService {
	
	//Changes From Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonalDetailsService.class);
	
	@Autowired
	private InterestedCountryRepository icr;

	@Autowired
	private BlackListedGoodsRepository bgr;
	
	@Override
	public List<InterestedCountry> getIntCountries(NimaiCustomer userId) {
		//Changes From Sravan
		LOGGER.info("GetIntCountries servies method is invoked");
		LOGGER.info(" Address1 "+userId.getAddress1()+" Address2 "+userId.getAddress2()+"Address3  "+userId.getAddress3()+" Bank Nbf Name "+userId.getBankNbfcName()+" Bank Type "+userId.getBankType()+" Branch Name "+userId.getBranchName()+" Business Type "+userId.getBusinessType()+" City "+userId.getCity()+" Company Name "+userId.getCompanyName()+" Country Name "+userId.getCountryName()+" Designation "+userId.getDesignation()+" Email Address "+userId.getEmailAddress()+" First Name "+userId.getFirstName()+" Land Line "+userId.getLandline()+" Last Name "+userId.getLastName()+" Min Value Of LC "+userId.getMinValueofLc()+" Mobile Number "+userId.getMobileNumber()+" Pin Code "+userId.getPincode()+" Province Name "+userId.getProvincename()+" Registration Type "+userId.getRegistrationType()+" Registred Country "+userId.getRegistredCountry()+" Subscriber Type "+userId.getSubscriberType()+" Swift Code "+userId.getSwiftCode()+" Telephone "+userId.getTelephone()+" User Id "+userId.getUserid());
		List<InterestedCountry> icl = icr.findByUserId(userId);	
		return icl;
	}

	@Override
	public List<BlackListedGoods> getGoodsName(NimaiCustomer userId) {
		//Changes From Sravan
		LOGGER.info("GetGoodsName service method is invoked");
		LOGGER.info(" Address1 "+userId.getAddress1()+" Address2 "+userId.getAddress2()+"Address3  "+userId.getAddress3()+" Bank Nbf Name "+userId.getBankNbfcName()+" Bank Type "+userId.getBankType()+" Branch Name "+userId.getBranchName()+" Business Type "+userId.getBusinessType()+" City "+userId.getCity()+" Company Name "+userId.getCompanyName()+" Country Name "+userId.getCountryName()+" Designation "+userId.getDesignation()+" Email Address "+userId.getEmailAddress()+" First Name "+userId.getFirstName()+" Land Line "+userId.getLandline()+" Last Name "+userId.getLastName()+" Min Value Of LC "+userId.getMinValueofLc()+" Mobile Number "+userId.getMobileNumber()+" Pin Code "+userId.getPincode()+" Province Name "+userId.getProvincename()+" Registration Type "+userId.getRegistrationType()+" Registred Country "+userId.getRegistredCountry()+" Subscriber Type "+userId.getSubscriberType()+" Swift Code "+userId.getSwiftCode()+" Telephone "+userId.getTelephone()+" User Id "+userId.getUserid());
		List<BlackListedGoods> blg = bgr.findByUserId(userId);	
		return blg;
	}
}
