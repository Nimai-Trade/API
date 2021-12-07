package com.nimai.ucm.service;

import java.util.List;

import com.nimai.ucm.bean.BeneficiaryInterestedCountryBean;
import com.nimai.ucm.bean.BlackListedGoodsBean;

import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.InterestedCountryBean;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.OwnerMasterBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.entity.BeneInterestedCountry;
import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiMLogin;
import com.nimai.ucm.entity.OwnerMaster;


public interface RegisterUserService {

	public PersonalDetailsBean savePersonalDetails(PersonalDetailsBean personDetailsBean);


	public PersonalDetailsBean updatePersonalDetails(PersonalDetailsBean personalDetails);

	public PersonalDetailsBean getPersonalDetails(String userId);

	public boolean saveBusinessDetails(String userId, BusinessDetailsBean businessDetailsBean);

	public BusinessDetailsBean updateBusinessDetails();

	public BusinessDetailsBean getBusinessDetails(String userId);

	public boolean checkEmailId(String emailId);

	public NimaiMLogin saveUserCredentials(NimaiMLogin loginEntity);

	NimaiMLogin saveUserCredentials();

	boolean checkUserId(String userId);

	public void saveInterestedCountry(InterestedCountry ic);
	
	public void saveBeneInterestedCountry(BeneInterestedCountry bic);

	public void saveBlackListedGoods(BlackListedGoods blg);

	public void updateInterestedCountry(InterestedCountryBean icb);
	
	public void updateBeneInterestedCountry(BeneficiaryInterestedCountryBean icb);

	public void updateBlackListedGoods(BlackListedGoodsBean blgb);

	public void saveOwnerMaster(OwnerMaster om);

	public void updateOwnerMaster(OwnerMasterBean om);

	BranchUserBean saveBranchUser(BranchUserBean branchUserbean);
	
	public List<NimaiCustomerBean> getRegisterUser(String userid);
	//public String getCountry(String userid);


	public int getActiveSubsidiaryCount(String account_source);
	
	public int getSubsidiaryUtilizedCount(String account_source);


	List<NimaiCustomerBean> getAdditionalRegisterUser(String userid);


	public void removeOwner(OwnerMasterBean ownerbean);
}
