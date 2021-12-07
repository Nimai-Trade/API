package com.nimai.ucm.service;

import java.util.ArrayList;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.ucm.bean.BeneficiaryInterestedCountryBean;
import com.nimai.ucm.bean.BlackListedGoodsBean;
import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.CountryResponse;
import com.nimai.ucm.bean.InterestedCountryBean;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.OwnerMasterBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.bean.StateResponce;
import com.nimai.ucm.entity.BeneInterestedCountry;
import com.nimai.ucm.entity.BlackListedGoods;
import com.nimai.ucm.entity.BranchUser;
import com.nimai.ucm.entity.InterestedCountry;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiLookupCountries;
import com.nimai.ucm.entity.NimaiMLogin;
import com.nimai.ucm.entity.OwnerMaster;
import com.nimai.ucm.repository.BeneInterestedCountryRepository;
import com.nimai.ucm.repository.BlackListedGoodsRepository;
import com.nimai.ucm.repository.InterestedCountryRepository;
import com.nimai.ucm.repository.LoginRepository;
import com.nimai.ucm.repository.OwnerMasterRepository;
import com.nimai.ucm.repository.UserDetailRepository;
import com.nimai.ucm.utility.ModelMapperUtil;
import com.nimai.ucm.utility.PasswordGenerator;
import com.nimai.ucm.utility.RegistrationId;


@Service
@Transactional
public class RegisterServiceImpl implements RegisterUserService {

	// Chaanges from Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);

	@Autowired
	RegistrationId userid;

	@Autowired
	UserDetailRepository detailRepository;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	PasswordGenerator password;

	@Autowired
	InterestedCountryRepository icr;
	
	@Autowired
	BeneInterestedCountryRepository bicr;

	@Autowired
	ModelMapperUtil modelMapper;
	
	@Autowired
	BlackListedGoodsRepository blgr;

	@Autowired
	OwnerMasterRepository omr;

	@Value("${referrer.fieo}")
	private String fieoRefId;
	
	@Override
	public PersonalDetailsBean savePersonalDetails(PersonalDetailsBean personDetailsBean) {
		// Changes from Sravan
		LOGGER.info("savePersonalDetails methods is invoked in RegisterServiceImpl class");
		LOGGER.info(" Bank Type " + personDetailsBean.getBankType() + " Business Type "
				+ personDetailsBean.getBusinessType() + " Company Name " + personDetailsBean.getCompanyName()
				+ " Country Name " + personDetailsBean.getCountryName() + " Designation "
				+ personDetailsBean.getDesignation() + " Emai Address " + personDetailsBean.getEmailAddress()
				+ " First Name " + personDetailsBean.getFirstName() + " Land Line Number"
				+ personDetailsBean.getLandLinenumber() + " Last Name " + personDetailsBean.getLastName()
				+ " Min LCValue " + personDetailsBean.getMinLCValue() + " Mobile Num "
				+ personDetailsBean.getMobileNum() + " Subscriber Type " + personDetailsBean.getSubscriberType()
				+ " User Id " + personDetailsBean.getUserId());
		// End

		// NimaiEmailScheduler nem = new NimaiEmailScheduler();

		NimaiCustomer nc = new NimaiCustomer();
		String subscriberType = personDetailsBean.getSubscriberType();
		String bankType = "";
		String userID = "";
		String accountStatus = "PENDING";
		String kycStatus = "PENDING";
		if (personDetailsBean.getBankType() == null)
			bankType = "";
		else
			bankType = personDetailsBean.getBankType().toUpperCase();

		System.out.println(subscriberType + "   " + bankType);
		userID = userid.username(subscriberType, bankType);

		NimaiCustomer checkUserId = detailRepository.getOne(userID);
		if (checkUserId != null && checkUserId.getUserid().equals(userID)) {
			System.out.println(subscriberType + "   " + bankType);
			userID = userid.newUsername(subscriberType, bankType);
		}

		personDetailsBean.setUserId(userID);

		nc.setUserid(personDetailsBean.getUserId());
		nc.setSubscriberType(personDetailsBean.getSubscriberType().toUpperCase());

		nc.setFirstName(personDetailsBean.getFirstName());
		nc.setLastName(personDetailsBean.getLastName());
		nc.setEmailAddress(personDetailsBean.getEmailAddress());
		nc.setMobileNumber(personDetailsBean.getMobileNum());
		nc.setLandline(personDetailsBean.getLandLinenumber());
		nc.setCountryName(personDetailsBean.getCountryName());
		nc.setCompanyName(personDetailsBean.getCompanyName());
		// Changes By Shubham Patil
		nc.setIsRegister(true);
		nc.setAccountType(personDetailsBean.getAccount_type());
		nc.setAccountSource(personDetailsBean.getAccount_source());
		// nc.setAccountStatus(personDetailsBean.getAccount_status());
		nc.setAccountStatus(accountStatus);
		nc.setAccountCreatedDate(personDetailsBean.getAccount_created_date());
		nc.setRegCurrency(personDetailsBean.getRegCurrency());
		nc.setEmailAddress1(personDetailsBean.getEmailAddress1());
		nc.setEmailAddress2(personDetailsBean.getEmailAddress2());
		nc.setRegistredCountry(personDetailsBean.getCountryName());
		nc.setEmailAddress3(personDetailsBean.getEmailAddress3());
		nc.setKycStatus(kycStatus);
		if (personDetailsBean.getAccount_type().equalsIgnoreCase("MASTER")) {
			nc.settCInsertedDate(Calendar.getInstance().getTime());
			nc.setTcFlag(personDetailsBean.getTcFlag());
			nc.setLeadId(personDetailsBean.getLeadId());
		}
		
		// End

		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")) {
			nc.setBankType(personDetailsBean.getBankType().toUpperCase());
			nc.setBusinessType(personDetailsBean.getBusinessType());
			nc.setMinValueofLc(personDetailsBean.getMinLCValue());
			nc.setRegCurrency(personDetailsBean.getRegCurrency());
			if (personDetailsBean.getBankType().equalsIgnoreCase("underwriter")) {
				if (personDetailsBean.getOtherTypeBank() != null && !personDetailsBean.getOtherTypeBank().isEmpty()) {
					nc.setOthers(personDetailsBean.getOtherTypeBank());
				} else {
					nc.setOthers("");
				}

			}

		} else {
			nc.setBusinessType("");
			nc.setMinValueofLc("");
			nc.setBankType("");
		}
		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("REFERRER")) {
			nc.setCompanyName(personDetailsBean.getCompanyName());
			nc.setDesignation(personDetailsBean.getDesignation());
			nc.setBusinessType(personDetailsBean.getBusinessType());
			if (personDetailsBean.getOtherType() != null && !personDetailsBean.getOtherType().isEmpty()) {
				nc.setOthers(personDetailsBean.getOtherType());
			} else {
				nc.setOthers("");
			}

		} else {
			if (personDetailsBean.getAccount_type().equalsIgnoreCase("REFER")) {
				nc.setCompanyName(personDetailsBean.getCompanyName());
				nc.setLeadId(0);
			} else {
				nc.setCompanyName("");
			}
			nc.setDesignation("");
			nc.setBusinessType("");  
		}

		if(personDetailsBean.getLeadId()==null)
			nc.setLeadId(0);
		else
			nc.setLeadId(personDetailsBean.getLeadId());
		if(personDetailsBean.getAccount_source().equalsIgnoreCase("fieo"))
		{
			nc.setAccountSource(fieoRefId);
			nc.setAccountType("REFER");
			if(!nc.getEmailAddress().equalsIgnoreCase(nc.getEmailAddress1()))
			{
				detailRepository.updateReferEmailId(nc.getEmailAddress(), nc.getEmailAddress1());
			}
			nc.setEmailAddress1("");
		}
		nc.setInsertedDate(Calendar.getInstance().getTime());
		nc.setModifiedDate(Calendar.getInstance().getTime());
		nc.setPaymentStatus("Pending");
		// Setting value for email event Account_Activation and alert...
		// Changes by Shubham Patil
//		nem.setUserid(userID);
//		nem.setEmailId(personDetailsBean.getEmailAddress());
//		nem.setUserName(personDetailsBean.getFirstName());
//		nem.setEmailStatus("pending");
//		nem.setEvent("ACCOUNT_ACTIVATE");
//		nem.setCustomerType(personDetailsBean.getSubscriberType());
//		nem.setInsertedDate(Calendar.getInstance().getTime());
//
//		nem = emailAlertRepository.save(nem);

		NimaiCustomer customerRegister = detailRepository.save(nc);
		if (customerRegister.getAccountType().equalsIgnoreCase("SUBSIDIARY")
				|| customerRegister.getAccountType().equalsIgnoreCase("BANKUSER")) {
			detailRepository.updateSubsidiaryCount(customerRegister.getAccountSource());
		}
		if (personDetailsBean.getSubscriberType().equalsIgnoreCase("BANK")) {

			for (BlackListedGoodsBean blgBean : personDetailsBean.getBlacklistedGoods()) {

				if (blgBean.getGoods_ID() == null) {
					BlackListedGoods blg = new BlackListedGoods();
					blg.setGoodsName(blgBean.getBlackListGoods());
					blg.setInsertedDate(Calendar.getInstance().getTime());
					blg.setUserId(nc);
					blg.setGoodsMId(blgBean.getGoodsMId());
					saveBlackListedGoods(blg);
				} else {
					updateBlackListedGoods(blgBean);
				}

			}

			for (InterestedCountryBean intCon : personDetailsBean.getInterestedCountry()) {

				if (intCon.getCountryID() == null) {
					InterestedCountry ic = new InterestedCountry();
					ic.setCountryName(intCon.getCountriesIntrested());
					ic.setInsertedDate(Calendar.getInstance().getTime());
					ic.setUserId(nc);
					ic.setCountryCurrencyId(intCon.getCcid());
					saveInterestedCountry(ic);
				} else {
					updateInterestedCountry(intCon);
				}

			}
			
			for (BeneficiaryInterestedCountryBean beneintCon : personDetailsBean.getBeneInterestedCountry()) {

				if (beneintCon.getCountryID() == null) {
					BeneInterestedCountry ic = new BeneInterestedCountry();
					ic.setCountryName(beneintCon.getCountriesIntrested());
					ic.setInsertedDate(Calendar.getInstance().getTime());
					ic.setUserId(nc);
					ic.setCountryCurrencyId(beneintCon.getCcid());
					saveBeneInterestedCountry(ic);
				} else {
					updateBeneInterestedCountry(beneintCon);
				}

			}

		}

		if (customerRegister != null) { // && customerRegister.getNimaiMLoginList().size() == 0) {

			NimaiMLogin loginEntity = new NimaiMLogin();

			loginEntity.setUserid(customerRegister);
			loginEntity.setPassword(password.getInitialPassword(8));
			loginEntity.setUserType(personDetailsBean.getSubscriberType().toUpperCase());

			loginEntity.setIsActPassed("INACTIVE");
			loginEntity.setFlag("0");
			loginEntity.setStatus("INACTIVE");

			loginEntity.setInsertedDate(Calendar.getInstance().getTime());
			loginEntity.setModifiedDate(Calendar.getInstance().getTime());

			// loginEntity.setToken(customerRegister.getUserid());
			// loginEntity.setToken_exp_Date(Calendar.getInstance().getTime());

			loginEntity.setLastActivityTime(Calendar.getInstance().getTime());
			loginEntity.setLastLoginTime(Calendar.getInstance().getTime());

			loginRepository.save(loginEntity);
		}
		return personDetailsBean;

	}

	@Override
	public boolean saveBusinessDetails(String userId, BusinessDetailsBean businessDetailsBean) {
		// Changes from Sravan
		LOGGER.info("saveBusinessDetails method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Address1 " + businessDetailsBean.getAddress1() + " Address2 " + businessDetailsBean.getAddress2()
				+ " Address3 " + businessDetailsBean.getAddress3() + " Bank Name " + businessDetailsBean.getBankName()
				+ " Branch Name " + businessDetailsBean.getBranchName() + " City " + businessDetailsBean.getCity()
				+ " Company Name " + businessDetailsBean.getComapanyName() + " Designation "
				+ businessDetailsBean.getDesignation() + " Pin Code " + businessDetailsBean.getPincode()
				+ " Province Name " + businessDetailsBean.getProvinceName() + " Registered Country "
				+ businessDetailsBean.getRegisteredCountry() + " Registration Type "
				+ businessDetailsBean.getRegistrationType() + " Swift Code " + businessDetailsBean.getSwiftCode()
				+ " Telephone " + businessDetailsBean.getTelephone() + " User Id " + businessDetailsBean.getUserId());
		NimaiCustomer nc = detailRepository.getOne(businessDetailsBean.getUserId());

		System.out.println("bean:" + businessDetailsBean.getRegisteredCountry());
		if (nc != null) {
			nc.setBankNbfcName(businessDetailsBean.getBankName());
			nc.setAddress3(businessDetailsBean.getAddress3());
			nc.setAddress1(businessDetailsBean.getAddress1());
			nc.setAddress2(businessDetailsBean.getAddress2());
			nc.setBranchName(businessDetailsBean.getBranchName());
			nc.setCity(businessDetailsBean.getCity());
			nc.setProvincename(businessDetailsBean.getProvinceName());
			nc.setPincode(businessDetailsBean.getPincode());
			nc.setRegistrationType(businessDetailsBean.getRegistrationType());
			nc.setSwiftCode(businessDetailsBean.getSwiftCode());
			nc.setRegistredCountry(businessDetailsBean.getRegisteredCountry());
			System.out.println(businessDetailsBean.getRegisteredCountry());
			nc.setInsertedDate(Calendar.getInstance().getTime());
			nc.setModifiedDate(Calendar.getInstance().getTime());
			nc.setCompanyName(businessDetailsBean.getComapanyName());
			nc.setTelephone(businessDetailsBean.getTelephone());
			nc.setDesignation(businessDetailsBean.getDesignation());
			// Changes by shubham patil
			nc.setIsBDetailsFilled(true);
			// End

			for (OwnerMasterBean ombean : businessDetailsBean.getOwnerMasterBean()) {

				if (ombean.getOwnerID() == null) {
					OwnerMaster om = new OwnerMaster();
					om.setOwnerFirstName(ombean.getOwnerFirstName());
					om.setOwnerLastName(ombean.getOwnerLastName());
					om.setDesignation(ombean.getDesignation());
					om.setInsertedDate(Calendar.getInstance().getTime());
					om.setModifiedDate(Calendar.getInstance().getTime());

					om.setUserId(nc);
					saveOwnerMaster(om);
				} else {
					updateOwnerMaster(ombean);
				}

			}

			NimaiCustomer client = detailRepository.save(nc);
			return true;
		}
		return false;

	}

	@Override
	public PersonalDetailsBean getPersonalDetails(String userId) {

		NimaiCustomer nc = detailRepository.getOne(userId);
		
		PersonalDetailsBean pdb = new PersonalDetailsBean();
		
		pdb = ModelMapperUtil.mapPdResponse(nc);

		return pdb;
	}

	@Override
	public BusinessDetailsBean getBusinessDetails(String userId) {

		NimaiCustomer nc = detailRepository.getOne(userId);

		BusinessDetailsBean bdb = new BusinessDetailsBean();

		bdb.setUserId(nc.getUserid());

		bdb.setBankName(nc.getBankNbfcName());
		bdb.setBranchName(nc.getBranchName());
		bdb.setSwiftCode(nc.getSwiftCode());
		bdb.setDesignation(nc.getDesignation());

		bdb.setComapanyName(nc.getCompanyName());
		bdb.setRegistrationType(nc.getRegistrationType());

		bdb.setRegisteredCountry(nc.getRegistredCountry());
		bdb.setProvinceName(nc.getProvincename());

		bdb.setAddress1(nc.getAddress1());
		bdb.setAddress2(nc.getAddress2());
		bdb.setAddress3(nc.getAddress3());
		bdb.setTelephone(nc.getTelephone());

		bdb.setCity(nc.getCity());
		bdb.setPincode(nc.getPincode());
		bdb.setOwnerMasterBean(new OwnerMasterBean[] {});
		bdb.setIsBDetailsFilled(nc.getIsBDetailsFilled());

		List<OwnerMaster> owners = omr.findByUserId(nc);

		if (!owners.isEmpty()) {
			OwnerMasterBean[] ob = new OwnerMasterBean[owners.size()];
			for (int x = 0; x < ob.length; x++) {
				OwnerMaster om = owners.get(x);
				OwnerMasterBean obn = new OwnerMasterBean();
				obn.setDesignation(om.getDesignation());
				obn.setOwnerFirstName(om.getOwnerFirstName());
				obn.setOwnerID(om.getOwnerID());
				obn.setOwnerLastName(om.getOwnerLastName());
				ob[x] = obn;

			}
			bdb.setOwnerMasterBean(ob);
		}

		return bdb;
	}

	@Override
	public boolean checkEmailId(String emailId) {
		// Changes from Sravan
		LOGGER.info("checkEmailID method is invoked in RegisterServiceImpl class");
		LOGGER.info("Email Id" + emailId);
		return detailRepository.existsByEmailAddress(emailId);
	}

	@Override
	public boolean checkUserId(String userId) {
		// Changes From Sravan
		LOGGER.info("checkUserID method is invoked in RegisterServiceImpl class");
		LOGGER.info("User Id" + userId);
		return detailRepository.existsById(userId);
	}

	@Override
	public NimaiMLogin saveUserCredentials(NimaiMLogin loginEntity) {
		// Changes From Sravan
		LOGGER.info("SaveUserCredentials method is invoked in RegisterServiceImpl class");
		LOGGER.info("loginEntity " + loginEntity);
		return loginRepository.save(loginEntity);
	}

	@Override
	public NimaiMLogin saveUserCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonalDetailsBean updatePersonalDetails(PersonalDetailsBean pdb) {
		// Changes From Sravan
		LOGGER.info("updatePersonalDetails method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Bank Type " + pdb.getBankType() + " Business Type " + pdb.getBusinessType() + " Company Name "
				+ pdb.getCompanyName() + " Country Name " + pdb.getCountryName() + " Designation "
				+ pdb.getDesignation() + " Email Address " + pdb.getEmailAddress() + " First Name " + pdb.getFirstName()
				+ " Land Line Number " + pdb.getLandLinenumber() + " LastName " + pdb.getLastName() + " MinLCVlue "
				+ pdb.getMinLCValue() + " Mobile Num " + pdb.getMobileNum() + " Subscriber Type "
				+ pdb.getSubscriberType() + " User Id " + pdb.getUserId());
		NimaiCustomer nc = detailRepository.getOne(pdb.getUserId());

		nc.setFirstName(pdb.getFirstName());
		nc.setLastName(pdb.getLastName());
		nc.setEmailAddress(pdb.getEmailAddress());
		nc.setMobileNumber(pdb.getMobileNum());
		nc.setLandline(pdb.getLandLinenumber());
		nc.setCountryName(pdb.getCountryName());

		nc.setCompanyName(pdb.getCompanyName());
		nc.setBusinessType(pdb.getBusinessType());

		nc.setMinValueofLc(pdb.getMinLCValue());
		nc.setRegCurrency(pdb.getRegCurrency());
		nc.setEmailAddress1(pdb.getEmailAddress1());
		nc.setEmailAddress2(pdb.getEmailAddress2());
		nc.setEmailAddress3(pdb.getEmailAddress3());

		if (pdb.getSubscriberType().equalsIgnoreCase("REFERRER")) {
			nc.setDesignation(pdb.getDesignation());
			if (pdb.getOtherType() != null && !pdb.getOtherType().isEmpty()) {
				nc.setOthers(pdb.getOtherType());

			} else {
				nc.setOthers(" ");
			}

		}
		if (pdb.getSubscriberType().equalsIgnoreCase("BANK") && pdb.getBankType().equalsIgnoreCase("Underwriter")) {

			if (pdb.getOtherTypeBank() != null && !pdb.getOtherTypeBank().isEmpty()) {
				nc.setOthers(pdb.getOtherTypeBank());

			} else {
				nc.setOthers(" ");
			}

		}

		// Need to add Countries Interested and Blacklisted Goods

		nc = detailRepository.save(nc);

		pdb.setFirstName(nc.getFirstName());
		pdb.setLastName(nc.getLastName());
		pdb.setEmailAddress(nc.getEmailAddress());
		pdb.setMobileNum(nc.getMobileNumber());
		pdb.setLandLinenumber(nc.getLandline());
		pdb.setCountryName(nc.getCountryName());
		pdb.setCompanyName(nc.getCompanyName());
		pdb.setBusinessType(nc.getBusinessType());

		pdb.setEmailAddress1(nc.getEmailAddress1());
		pdb.setEmailAddress2(nc.getEmailAddress2());
		pdb.setEmailAddress3(nc.getEmailAddress3());

		// Need to add Countries Interested and Blacklisted Goods
		pdb.setSubscriberType(nc.getSubscriberType());

		if (nc.getSubscriberType().equalsIgnoreCase("BANK") && nc.getBankType().equalsIgnoreCase("Underwriter")) {

			blgr.deleteBlackListedGoodsUserId(nc.getUserid());
			for (BlackListedGoodsBean blgBean : pdb.getBlacklistedGoods()) {
				if (blgBean.getGoods_ID() == null) {

					BlackListedGoods blg = new BlackListedGoods();
					blg.setGoodsName(blgBean.getBlackListGoods());
					blg.setInsertedDate(Calendar.getInstance().getTime());
					blg.setUserId(nc);
					blg.setGoodsMId(blgBean.getGoodsMId());
					saveBlackListedGoods(blg);
				}

			}

			icr.deleteInterestedCountryUserId(nc.getUserid());
			for (InterestedCountryBean intCon : pdb.getInterestedCountry()) {
				if (intCon.getCountryID() == null) {
					InterestedCountry ic = new InterestedCountry();
					ic.setCountryName(intCon.getCountriesIntrested());
					ic.setInsertedDate(Calendar.getInstance().getTime());
					ic.setUserId(nc);
					ic.setCountryCurrencyId(intCon.getCcid());
					saveInterestedCountry(ic);
				}
			}
			
			bicr.deleteBeneInterestedCountryUserId(nc.getUserid());
			for (BeneficiaryInterestedCountryBean bintCon : pdb.getBeneInterestedCountry()) {
				if (bintCon.getCountryID() == null) {
					BeneInterestedCountry bic = new BeneInterestedCountry();
					bic.setCountryName(bintCon.getCountriesIntrested());
					bic.setInsertedDate(Calendar.getInstance().getTime());
					bic.setUserId(nc);
					bic.setCountryCurrencyId(bintCon.getCcid());
					saveBeneInterestedCountry(bic);
				}
			}
			pdb.setMinLCValue(nc.getMinValueofLc());

		}

		return pdb;
	}

	@Override
	public void saveInterestedCountry(InterestedCountry ic) {
		// Changes From Sravan
		LOGGER.info("Save Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Country Name " + ic.getCountryName() + " Country Currency Id " + ic.getCountryCurrencyId()
				+ " Country Id " + ic.getCountryID() + " Inserted Date " + ic.getInsertedDate() + " Modified Date "
				+ ic.getModifiedDate() + " User Id " + ic.getUserId());
		icr.save(ic);
	}
	
	@Override
	public void saveBeneInterestedCountry(BeneInterestedCountry bic) {
		// Changes From Sravan
		LOGGER.info("Save Bene Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Country Name " + bic.getCountryName() + " Country Currency Id " + bic.getCountryCurrencyId()
				+ " Country Id " + bic.getCountryID() + " Inserted Date " + bic.getInsertedDate() + " Modified Date "
				+ bic.getModifiedDate() + " User Id " + bic.getUserId());
		bicr.save(bic);
	}

	@Override
	public void saveBlackListedGoods(BlackListedGoods blg) {
		// Changes From Sravan
		LOGGER.info("Save Black Listed Goods method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Goods Name " + blg.getGoodsName() + " Goods ID " + blg.getGoods_ID() + " GoodsMId "
				+ blg.getGoodsMId() + " Inserted Date " + blg.getInsertedDate() + " Modified Date "
				+ blg.getModifiedDate() + " User Id " + blg.getUserId());
		blgr.save(blg);
	}

	@Override
	public void updateInterestedCountry(InterestedCountryBean icb) {
		// Changes From Sravan
		LOGGER.info("Update Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Ccid " + icb.getCcid() + " Country Id " + icb.getCountryID());
		InterestedCountry ic = icr.getOne(icb.getCountryID());
		if (ic != null) {
			ic.setCountryName(icb.getCountriesIntrested());
			ic.setInsertedDate(Calendar.getInstance().getTime());
			ic.setCountryCurrencyId(icb.getCcid());
			icr.save(ic);
		}

	}
	
	@Override
	public void updateBeneInterestedCountry(BeneficiaryInterestedCountryBean icb) {
		// Changes From Sravan
		LOGGER.info("Update Interested Country method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Ccid " + icb.getCcid() + " Country Id " + icb.getCountryID());
		InterestedCountry ic = icr.getOne(icb.getCountryID());
		if (ic != null) {
			ic.setCountryName(icb.getCountriesIntrested());
			ic.setInsertedDate(Calendar.getInstance().getTime());
			ic.setCountryCurrencyId(icb.getCcid());
			icr.save(ic);
		}

	}

	@Override
	public void updateBlackListedGoods(BlackListedGoodsBean blgb) {
		// Changes From Sravan
		LOGGER.info("Update Black Listed Goods method is invoked in RegisterServiceImpl class");
		LOGGER.info(" Black List Goods " + blgb.getBlackListGoods() + " Goods ID " + blgb.getGoods_ID() + " Goods MId "
				+ blgb.getGoodsMId());
		BlackListedGoods blg = blgr.getOne(blgb.getGoods_ID());
		if (blg != null) {
			blg.setGoodsName(blgb.getBlackListGoods());
			blg.setInsertedDate(Calendar.getInstance().getTime());
			blg.setGoodsMId(blgb.getGoodsMId());
			blgr.save(blg);
		}

	}

	@Override
	public void saveOwnerMaster(OwnerMaster om) {
		// Changes From Sravan
		LOGGER.info("saveOwnerMaster method is invoked in RegisterServiceimpl Class");
		LOGGER.info(" Designation " + om.getDesignation() + " Owner First Name " + om.getOwnerFirstName()
				+ " Owner Last Name " + om.getOwnerLastName() + " Owner ID " + om.getOwnerID() + " Inserted Date "
				+ om.getInsertedDate() + " Modified Date " + om.getModifiedDate() + " User Id " + om.getUserId());
		omr.save(om);
	}

	@Override
	public void updateOwnerMaster(OwnerMasterBean om) {
		// Changes From Sravan
		LOGGER.info("saveOwnerMaster method is invoked in RegisterServiceimpl Class");
		LOGGER.info(" Designation " + om.getDesignation() + " Owner First Name " + om.getOwnerFirstName()
				+ " Owner Last Name " + om.getOwnerLastName() + " Owner ID " + om.getOwnerID());
		OwnerMaster omm = omr.getOne(om.getOwnerID());
		if (omm != null) {
			omm.setDesignation(om.getDesignation());
			omm.setModifiedDate(new Date());
			omm.setOwnerFirstName(om.getOwnerFirstName());
			omm.setOwnerLastName(om.getOwnerLastName());
			omr.save(omm);
		}

	}

	@Override
	public BusinessDetailsBean updateBusinessDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BranchUserBean saveBranchUser(BranchUserBean branchUserbean) {
		// Changes From Sravan
		LOGGER.info("Save Branch User method is invoked");
		LOGGER.info(" Branch Id " + branchUserbean.getBranchId() + " Email Id " + branchUserbean.getEmailId()
				+ " Employee Id " + branchUserbean.getEmployeeId() + " Inserted By " + branchUserbean.getInsertedBy()
				+ " Inserted Date " + branchUserbean.getInsertedDate() + " Modified By "
				+ branchUserbean.getModifiedBy() + " Modified Date " + branchUserbean.getModifiedDate()
				+ " Passcode Value " + branchUserbean.getPasscodeValue() + " Token Id " + branchUserbean.getTokenId()
				+ " User Id " + branchUserbean.getUserID());
		BranchUser nimaipwd = new BranchUser();
		nimaipwd.setBranchId(branchUserbean.getBranchId());
		nimaipwd.setEmailId(branchUserbean.getEmailId());
		nimaipwd.setEmployeeId(branchUserbean.getEmployeeId());

		nimaipwd.setInsertedBy(branchUserbean.getInsertedBy());
		nimaipwd.setInsertedDate(branchUserbean.getInsertedDate());
		nimaipwd.setModifiedBy(branchUserbean.getModifiedBy());
		nimaipwd.setModifiedDate(branchUserbean.getModifiedDate());
//		
//		NimaiUniquePwd nimaipwd1=new NimaiUniquePwd();
//		nimaipwd.setTokenId(nimaipwd1.uniqueNumber());
//	
//		nimaipedrepo.save(nimaipwd);
		return branchUserbean;
	}

	@Override
	public List<NimaiCustomerBean> getRegisterUser(String userid) {
		List<NimaiCustomer> registerUser = detailRepository.findRegisterUser(userid);
		List<NimaiCustomerBean> qmList = new ArrayList<>();
		ModelMapperUtil modelMapper = new ModelMapperUtil();
		Integer noOfQuote = 0, noOfCredit = 0;
		for (NimaiCustomer subsidiary : registerUser) {
			NimaiCustomerBean qmb = modelMapper.map(subsidiary, NimaiCustomerBean.class);
			qmb.setUserid(subsidiary.getUserid());
			try {
				noOfCredit = detailRepository.getCreditUtilized(subsidiary.getUserid());
				if (noOfCredit.equals(null) || noOfCredit == 0) {
					noOfCredit = 0;
				} else
					noOfCredit = 1;
				noOfQuote = detailRepository.getQuoteReceived(subsidiary.getUserid());
			} catch (Exception e) {
				noOfCredit = 0;
				noOfQuote = 0;
			}
			qmb.setCreditUsed(noOfCredit);
			qmb.setQuoteReceived(noOfQuote);
			qmList.add(qmb);
		}

		return qmList;
	}
	/*
	 * @Override public String getCountry(String userid) { String
	 * registeredCountryName=detailRepository.findCountry(userid);
	 * System.out.println("Registered Country Name: "+registeredCountryName);
	 * 
	 * return registeredCountryName; }
	 */

	@Override
	public List<NimaiCustomerBean> getAdditionalRegisterUser(String userid) {
		List<NimaiCustomer> registerUser = detailRepository.findAdditionalRegisterUser(userid);
		List<NimaiCustomerBean> qmList = new ArrayList<>();
		ModelMapperUtil modelMapper = new ModelMapperUtil();
		Integer noOfQuote = 0, noOfCredit = 0;
		for (NimaiCustomer subsidiary : registerUser) {
			NimaiCustomerBean qmb = modelMapper.map(subsidiary, NimaiCustomerBean.class);
			qmb.setUserid(subsidiary.getUserid());
			try {
				noOfCredit = detailRepository.getCreditUtilizedOfAdditionalUser(subsidiary.getUserid());
				if (noOfCredit.equals(null) || noOfCredit == 0) {
					noOfCredit = 0;
				} else
					noOfCredit = 1;
				noOfQuote = detailRepository.getQuotePlaced(subsidiary.getUserid());
			} catch (Exception e) {
				noOfCredit = 0;
				noOfQuote = 0;
			}
			qmb.setCreditUsed(noOfCredit);
			qmb.setQuoteReceived(noOfQuote);
			qmList.add(qmb);
		}

		return qmList;
	}

	@Override
	public int getActiveSubsidiaryCount(String account_source) {
		// TODO Auto-generated method stub
		try {
			return Integer.valueOf(detailRepository.getSubsidiaryCount(account_source));
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int getSubsidiaryUtilizedCount(String account_source) {
		// TODO Auto-generated method stub
		try {
			return Integer.valueOf(detailRepository.getSubsidiaryUtilizedCount(account_source));
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void removeOwner(OwnerMasterBean ownerbean) {
		// TODO Auto-generated method stub
		LOGGER.info("Remove Owner Details");
		detailRepository.removeOwnerDet(ownerbean.getOwnerID());
	}

}
