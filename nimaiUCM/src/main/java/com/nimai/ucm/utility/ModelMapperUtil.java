package com.nimai.ucm.utility;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.ucm.bean.BlackListedGoodsBean;
import com.nimai.ucm.bean.InterestedCountryBean;
import com.nimai.ucm.bean.NimaiCustomerReferrerBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.Refer;

@Component
public class ModelMapperUtil extends ModelMapper {

	public static PersonalDetailsBean mapPdResponse(NimaiCustomer nc) {
		PersonalDetailsBean pdb = new PersonalDetailsBean();

		pdb.setUserId(nc.getUserid());
		pdb.setSubscriberType(nc.getSubscriberType());
		pdb.setBankType(nc.getBankType());
		pdb.setFirstName(nc.getFirstName());
		pdb.setLastName(nc.getLastName());
		pdb.setEmailAddress(nc.getEmailAddress());
		pdb.setMobileNum(nc.getMobileNumber());
		pdb.setLandLinenumber(nc.getLandline());
		pdb.setCountryName(nc.getCountryName());
		pdb.setDesignation(nc.getDesignation());
		pdb.setCompanyName(nc.getCompanyName());
		pdb.setBusinessType(nc.getBusinessType());
		pdb.setMinLCValue(nc.getMinValueofLc());
		pdb.setRegCurrency(nc.getRegCurrency());
		pdb.setIsRegister(nc.getIsRegister());
		pdb.setIsBDetailsFilled(nc.getIsBDetailsFilled());
		pdb.setIsSPlanPurchased(nc.getIsSPlanPurchased());
		pdb.setModeOfPayment(nc.getModeOfPayment());
		pdb.setPaymentDate(nc.getPaymentDate());
		pdb.setKycStatus(nc.getKycStatus());
		pdb.setKycApprovalDate(nc.getKycApprovalDate());
		pdb.setIsRmAssigned(nc.getIsRmAssigned());
		pdb.setRmId(nc.getRmId());
		pdb.setEmailAddress1(nc.getEmailAddress1());
		pdb.setEmailAddress2(nc.getEmailAddress2());
		pdb.setEmailAddress3(nc.getEmailAddress3());
		if (nc.getSubscriberType().equalsIgnoreCase("REFERRER")) {
			pdb.setOtherType(nc.getOthers());
		} else if (nc.getSubscriberType().equalsIgnoreCase("BANK")
				&& nc.getBankType().equalsIgnoreCase("UNDERWRITER")) {
			pdb.setOtherTypeBank(nc.getOthers());
		}

		List<InterestedCountryBean> intrestedCountrList = nc.getIntrestedCountrList().stream().map(ict -> {
			InterestedCountryBean ib = new InterestedCountryBean();
			ib.setCountriesIntrested(ict.getCountryName());
			ib.setCcid(ict.getCountryCurrencyId());
			ib.setCountryID(ict.getCountryID());
			return ib;
		}).collect(Collectors.toList());
		List<BlackListedGoodsBean> bgList = nc.getBgList().stream().map(blg -> {
			BlackListedGoodsBean blb = new BlackListedGoodsBean();
			blb.setBlackListGoods(blg.getGoodsName());
			blb.setGoods_ID(blg.getGoods_ID());
			blb.setGoodsMId(blg.getGoodsMId());
			return blb;
		}).collect(Collectors.toList());

		pdb.setInterestedCountry((intrestedCountrList.isEmpty()) ? null : intrestedCountrList);
		pdb.setBlacklistedGoods((bgList.isEmpty()) ? null : bgList);

		return pdb;
	}

	public static NimaiCustomerReferrerBean mapNcbResponse(NimaiCustomer customerDetails, Refer nc) {

		NimaiCustomerReferrerBean ncb = new NimaiCustomerReferrerBean();
		ncb.setExpiredIn(null);
		ncb.setBeanchUserId(nc.getBranchUserId());
		ncb.setAccountStatus("Pending");
		ncb.setCompanyName(customerDetails.getCompanyName());
		ncb.setCountryName(customerDetails.getCountryName());
		ncb.setUserid(customerDetails.getUserid());
		ncb.setInsertedDate(nc.getInsertedDate());
		ncb.setEarning(null);
		ncb.setCurrency(null);
		return ncb;
	}
	
	public static NimaiCustomerReferrerBean mapfieoLeadResponse(NimaiCustomer customerDetails, NimaiCustomer nc) {

		NimaiCustomerReferrerBean ncb = new NimaiCustomerReferrerBean();
		ncb.setExpiredIn(null);
		//ncb.setBeanchUserId(nc.getBranchUserId());
		if(customerDetails.getKycStatus().equalsIgnoreCase("Rejected") && customerDetails.getPaymentStatus().equalsIgnoreCase("Rejected"))
			ncb.setAccountStatus("Rejected");
		else
			ncb.setAccountStatus("Pending");
		ncb.setCompanyName(customerDetails.getCompanyName());
		ncb.setCountryName(customerDetails.getCountryName());
		ncb.setUserid(customerDetails.getUserid());
		ncb.setInsertedDate(nc.getInsertedDate());
		ncb.setInsertedDate(nc.getAccountCreatedDate());
		ncb.setEarning(null);
		ncb.setCurrency(null);
		return ncb;
	}

	public NimaiCustomerReferrerBean mapNcbResults(NimaiCustomer customerDetails, Refer nc, List<Object[]> results, Float actualREarning) {

		NimaiCustomerReferrerBean ncb = new NimaiCustomerReferrerBean();
		if(customerDetails.getKycStatus().equalsIgnoreCase("Approved") &&
				customerDetails.getPaymentStatus().equalsIgnoreCase("Approved")) {
			for (Object[] result : results) {
				
				String currency = (String) result[0];
				if (currency == null) {
					ncb.setCurrency(null);
				} else {
					ncb.setCurrency(currency);
				}
				Date spLanEndDate = (java.util.Date) result[1];

				BigInteger biginteger = (BigInteger) result[5];
				int sigvalue = biginteger.signum();

				if ((BigInteger) result[5] == null)
					ncb.setExpiredIn(null);
				else if (sigvalue <= 0) {
					ncb.setExpiredIn("EXPIRED");
				} else {
					ncb.setExpiredIn((BigInteger) result[5] + " days");
				}
				Double grandAmount = (Double) result[2];
				if (grandAmount == 0) {
					ncb.setEarning(null);
				} else {
					Float earningValue = (float) (Double.valueOf(grandAmount) * (actualREarning));
					Float value =Float.parseFloat(new DecimalFormat("##.##").format(earningValue));
					ncb.setEarning(value);
					System.out.println("Earnng value"+value);
					//ncb.setEarning((int) ((int) Math.round(grandAmount * actualREarning)));

				}

				ncb.setCompanyName((String) result[3] != null ? (String) result[3] : "null");
				ncb.setCountryName((String) result[4] != null ? (String) result[4] : "null");
				ncb.setUserid(customerDetails.getUserid());
				ncb.setInsertedDate(nc.getInsertedDate());
				ncb.setBeanchUserId(nc.getBranchUserId());
				ncb.setAccountStatus(customerDetails.getKycStatus());

			}
		}else {
			for (Object[] result : results) {
				ncb.setCompanyName((String) result[3] != null ? (String) result[3] : "null");
				ncb.setCountryName((String) result[4] != null ? (String) result[4] : "null");
			}
			ncb.setUserid(customerDetails.getUserid());
			ncb.setInsertedDate(nc.getInsertedDate());
			ncb.setBeanchUserId(nc.getBranchUserId());
			ncb.setAccountStatus(customerDetails.getKycStatus());
		}
		
		return ncb;
	}
	
	public NimaiCustomerReferrerBean mapFieoReferResults(NimaiCustomer customerDetails, NimaiCustomer nc, List<Object[]> results, Float actualREarning) {

		NimaiCustomerReferrerBean ncb = new NimaiCustomerReferrerBean();
		if(customerDetails.getKycStatus().equalsIgnoreCase("Approved") &&
				customerDetails.getPaymentStatus().equalsIgnoreCase("Approved")) {
			for (Object[] result : results) {
				
				String currency = (String) result[0];
				if (currency == null) {
					ncb.setCurrency(null);
				} else {
					ncb.setCurrency(currency);
				}
				Date spLanEndDate = (java.util.Date) result[1];

				BigInteger biginteger = (BigInteger) result[5];
				int sigvalue = biginteger.signum();

				if ((BigInteger) result[5] == null)
					ncb.setExpiredIn(null);
				else if (sigvalue <= 0) {
					ncb.setExpiredIn("EXPIRED");
				} else {
					ncb.setExpiredIn((BigInteger) result[5] + " days");
				}
				Double grandAmount = (Double) result[2];
				if (grandAmount == 0) {
					ncb.setEarning(null);
				} else {
					Float earningValue = (float) (Double.valueOf(grandAmount) * (actualREarning));
					Float value =Float.parseFloat(new DecimalFormat("##.##").format(earningValue));
					ncb.setEarning(value);
					System.out.println("Earnng value"+value);
					//ncb.setEarning((int) ((int) Math.round(grandAmount * actualREarning)));

				}

				ncb.setCompanyName((String) result[3] != null ? (String) result[3] : "null");
				ncb.setCountryName((String) result[4] != null ? (String) result[4] : "null");
				ncb.setUserid(customerDetails.getUserid());
				ncb.setInsertedDate(nc.getAccountCreatedDate());
				//ncb.setBeanchUserId(nc.getBranchUserId());
				ncb.setAccountStatus(customerDetails.getKycStatus());

			}
		}else {
			for (Object[] result : results) {
				ncb.setCompanyName((String) result[3] != null ? (String) result[3] : "null");
				ncb.setCountryName((String) result[4] != null ? (String) result[4] : "null");
			}
			ncb.setUserid(customerDetails.getUserid());
			ncb.setInsertedDate(nc.getAccountCreatedDate());
			//ncb.setBeanchUserId(nc.getBranchUserId());
			ncb.setAccountStatus(customerDetails.getKycStatus());
		}
		
		return ncb;
	}
}