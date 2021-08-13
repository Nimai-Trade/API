package com.nimai.ucm.service;

import java.math.BigInteger;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.NimaiCustomerReferrerBean;
import com.nimai.ucm.bean.NimaiSpecCustomerReferrerBean;
import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.bean.ReferIdBean;
import com.nimai.ucm.bean.ReferrerBean;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiMSubscription;
import com.nimai.ucm.entity.NimaiSubscriptionDetails;
import com.nimai.ucm.entity.Refer;
import com.nimai.ucm.repository.CustomerRepository;
import com.nimai.ucm.repository.NimaiMSubscriptionRepo;
import com.nimai.ucm.repository.NimaiSubscriptionDetailsRepo;
import com.nimai.ucm.repository.ReferRepository;
import com.nimai.ucm.repository.UserDetailRepository;
import com.nimai.ucm.repository.getRegisterUserRepo;
import com.nimai.ucm.repository.nimaiSystemConfigRepository;
import com.nimai.ucm.utility.AppConstants;
import com.nimai.ucm.utility.ModelMapperUtil;
import com.nimai.ucm.utility.ReferenceIdUniqueNumber;
import com.nimai.ucm.utility.ValidateUserDetails;

@Service
@Transactional
public class ReferServiceImpl implements ReferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReferServiceImpl.class);

	@Autowired
	ReferRepository referRepo;

	@Autowired
	getRegisterUserRepo getRegiUserRepo;

	@Autowired
	UserDetailRepository detailRepository;

	@Autowired
	GenericResponse<Object> response;

	@Autowired
	NimaiSubscriptionDetailsRepo subRepo;

	@Autowired
	NimaiMSubscriptionRepo subMRepo;

	@Autowired
	ValidateUserDetails util;

	@Autowired
	ModelMapperUtil modelMapperUtil;

	@Autowired
	CustomerRepository cuRepo;

	@Autowired
	nimaiSystemConfigRepository systemConfig;

	@Override
	public ReferIdBean saveReferService(ReferBean referbean, String r1) {
		GenericResponse response = new GenericResponse();
		LOGGER.info("SaveReferService method is invoked in ReferServiceImpl class");
		LOGGER.info(" Branch User Id " + referbean.getBranchUserId() + " Company Name " + referbean.getCompanyName()
				+ " Country Name " + referbean.getCountryName() + " Email Address " + referbean.getEmailAddress()
				+ " First Name " + referbean.getFirstName() + " Inserted By " + referbean.getInsertedBy()
				+ " Last Name " + referbean.getLastName() + " Mobile No " + referbean.getMobileNo() + " Modified By "
				+ referbean.getModifiedBy() + " Reference Id " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " User Id " + referbean.getUserId() + "referrer_Email_Id"
				+ referbean.getReferrer_Email_Id());
		Refer refer = new Refer();
		Date dNow = new Date();
//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		NimaiCustomer cusdetails = cuRepo.getOne(referbean.getUserId());
		refer.setUserid(cusdetails);
		refer.setBranchUserId(referbean.getBranchUserId());
		refer.setCompanyName(referbean.getCompanyName());
		refer.setCountryName(referbean.getCountryName());
		refer.setEmailAddress(referbean.getEmailAddress());
		if (referbean.getUserId().substring(0, 2).equalsIgnoreCase("BC")
				|| referbean.getUserId().substring(0, 2).equalsIgnoreCase("CU")
				|| referbean.getUserId().substring(0, 2).equalsIgnoreCase("RE")) {
			refer.setReferrer_Email_Id(cusdetails.getEmailAddress());
		} else {
			refer.setReferrer_Email_Id(referbean.getReferrer_Email_Id());
		}

		refer.setFirstName(referbean.getFirstName());
		refer.setInsertedBy(referbean.getInsertedBy());
		refer.setInsertedDate(dNow);
		refer.setLastName(referbean.getLastName());
		refer.setMobileNo(referbean.getMobileNo());
		refer.setStatus(referbean.getStatus());
		refer.setModifiedBy(referbean.getModifiedBy());
		refer.setReferenceId(r1);

		// Refer refer = modelMapper.map(referbean, Refer.class);

		ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
		refer.setReferenceId(r1);
		int responseId = refer.getId();

		Refer r2 = referRepo.save(refer);

		System.out.println(r2.getId());
		ReferIdBean responseBean = new ReferIdBean();
		responseBean.setReId(r2.getId());
		responseBean.setReferenceId(r2.getReferenceId());

		return responseBean;

	}

	@Override
	public ReferrerBean getReferByUserId(String userId) {
		// Changes from Sravan
		LOGGER.info("getReferByUserId method is invoked in ReferServiceImpl class");
		LOGGER.info("User Id " + userId);
		String referUserId;
		List<Refer> refer = referRepo.findReferByUserId(userId);
		ReferrerBean referBean = new ReferrerBean();
		List<ReferBean> rfb = new ArrayList<>();
		Float totalEarning = (float) 0.0;
		ModelMapperUtil modelMapper = new ModelMapperUtil();
		for (Refer rf : refer) {

			ReferBean rb = modelMapper.map(rf, ReferBean.class);
			NimaiCustomer customer = new NimaiCustomer();
			try {
				customer = detailRepository.getUserIdByEmailId(rf.getEmailAddress());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Customer id which is not present :" + rf.getEmailAddress());
				continue;
			}
			List<NimaiSubscriptionDetails> details = new ArrayList<>();
			try {
				details = subRepo.finSplanByReferId(customer.getUserid());

				Float referEarning = Float.valueOf(systemConfig.earningPercentage());
				Float actualREarning = (Float) (referEarning / 100);
				/* totalearning */
				Float userTotalEarning = (float) 0.0;
				if (details.size() == 1) {
					if ((customer.getPaymentStatus().equalsIgnoreCase("Approved")
							|| customer.getPaymentStatus().equalsIgnoreCase("Success"))
							&& customer.getKycStatus().equalsIgnoreCase("Approved")) {
						Integer totalEarn = getRegiUserRepo.findTotalEarning(customer.getUserid());
						if (totalEarn == null) {
							userTotalEarning = (float) 0.0;
						} else {
							Float value = Float
									.parseFloat(new DecimalFormat("##.##").format(totalEarn * actualREarning));
							// ncb.setTotalEarning(String.valueOf(value));
							userTotalEarning = value;
							// ncb.setTotalEarning(currency + " " + (int) ((int) Math.round(totalEarn *
							// 0.07)));
						}
					} else {
						userTotalEarning = (float) 0.0;
					}
				} else {
					Integer totalEarn = getRegiUserRepo.findTotalEarning(customer.getUserid());
					if (totalEarn == null) {
						userTotalEarning = (float) 0.0;
					} else {
						Float value = Float.parseFloat(new DecimalFormat("##.##").format(totalEarn * actualREarning));
						// ncb.setTotalEarning(String.valueOf(value));
						userTotalEarning = value;
						// ncb.setTotalEarning(currency + " " + (int) ((int) Math.round(totalEarn *
						// 0.07)));
					}
				}
				rb.setUserWiseTotalEarning(userTotalEarning);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Customer id which is not present :" + rf.getEmailAddress());
				continue;
			}

			String kysStatus = customer.getKycStatus();
			System.out.println(kysStatus);
			if (kysStatus == null || kysStatus.isEmpty() || details == null) {
				rb.setEarnings(null);
				rb.setPurchaseType(AppConstants.NA_PURCHASE);
				rb.setBillingAmount("0");
				rb.setPlanPurchased(AppConstants.NA_PURCHASE);
				rb.setStatus(AppConstants.KYCSTATUS);
			} else {

				if (details != null && kysStatus.equalsIgnoreCase(AppConstants.STATUS) && kysStatus != null
						&& !kysStatus.isEmpty() && customer.getPaymentStatus().equalsIgnoreCase(AppConstants.STATUS)) {
					Predicate<NimaiSubscriptionDetails> activePredicate = p -> p.getStatus()
							.equalsIgnoreCase(AppConstants.SPLAN_STATUS);
					List<NimaiSubscriptionDetails> activeList = details.stream().filter(activePredicate)
							.collect(Collectors.<NimaiSubscriptionDetails>toList());
					Predicate<NimaiSubscriptionDetails> InactivePredicate = p -> p.getStatus()
							.equalsIgnoreCase(AppConstants.SPLAN_IN_STATUS);
					List<NimaiSubscriptionDetails> inactiveList = details.stream().filter(InactivePredicate)
							.collect(Collectors.<NimaiSubscriptionDetails>toList());
					DecimalFormat df = new DecimalFormat("####0.00");
					for (NimaiSubscriptionDetails dl : activeList) {
						Double totalAmount;
						try {
							totalAmount = ((double) dl.getSubscriptionAmount() + dl.getVasAmount()) - dl.getDiscount();
						} catch (Exception e) {
							totalAmount = 0.0;
						}

						rb.setBillingAmount(String.valueOf(totalAmount));
						rb.setPlanPurchased(dl.getSubscriptionName());
					}

					if (activeList.size() == 1 && activeList.size() == 0) {
						rb.setPurchaseType(AppConstants.PURCHASE_TYPE);
					}
					if (activeList.size() == 1 && inactiveList.size() > 0) {
						rb.setPurchaseType(AppConstants.PURCHASE_R_TYPE);
					}
					try {
						String billingAmount = rb.getBillingAmount();
						String referrerEarnValue = referRepo.getReferEarningsPercent();
						Integer referrerEarn = Integer.valueOf(referrerEarnValue);
						Float referrerEarnCalc = (float) (referrerEarn) / 100;
						Float finalEarning = Float.valueOf(billingAmount) * (float) (referrerEarnCalc);
						String earning = new DecimalFormat(AppConstants.DECIMAL_FORMAT).format(finalEarning);

						rb.setEarnings("" + earning);
					} catch (Exception e) {
						rb.setEarnings("0");
					}
					rb.setStatus(customer.getKycStatus());
				} else {
					rb.setEarnings(null);
					rb.setPurchaseType(AppConstants.NA_PURCHASE);
					rb.setBillingAmount("0");
					rb.setPlanPurchased(AppConstants.NA_PURCHASE);
					rb.setStatus(AppConstants.KYCSTATUS);
				}
			}
			rfb.add(rb);
			
			
		}
		referBean.setRfb(rfb);

		for (ReferBean rEarn : rfb) {
			totalEarning += rEarn.getUserWiseTotalEarning();
		}
		referBean.setTotalEarning(Float.parseFloat(new DecimalFormat("##.##").format(totalEarning)));
		return referBean;

	}

	@Override
	public boolean checkEmailId(String emailAddress) {
		// Changes from Sravan
		LOGGER.info("checkEmailId method is invoked in ReferServiceImpl class");
		LOGGER.info("Email Address " + emailAddress);
		referRepo.existsByEmailAddress(emailAddress);
		return false;
	}

	@Override
	public Refer updateRefer(ReferBean referbean) {
		// Changes from Sravan
		LOGGER.info("updateRefer method is invoked in ReferServiceImpl class");
		LOGGER.info(" User Id " + referbean.getBranchUserId() + " Company Name " + referbean.getCompanyName()
				+ " Country Name " + referbean.getCountryName() + " Email Address " + referbean.getEmailAddress()
				+ " First Name " + referbean.getFirstName() + " Inserted By " + referbean.getInsertedBy() + " LastName "
				+ referbean.getLastName() + " Mobile No " + referbean.getMobileNo() + " Modified By "
				+ referbean.getModifiedBy() + " Reference Id " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " User Id " + referbean.getUserId());
		referRepo.getOne(referbean.getUserId());
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Refer refer = modelMapper.map(referbean, Refer.class);

		return referRepo.save(refer);

	}

	@Override
	public List<Refer> viewReferB(ReferBean referbean) {
		// Changes from Sravan
		LOGGER.info("viewReferB method is invoked in ReferServiceImpl Class");
		LOGGER.info(" User Id " + referbean.getBranchUserId() + " Company Name " + referbean.getCompanyName()
				+ " Country Name " + referbean.getCountryName() + " Email Address " + referbean.getEmailAddress()
				+ " First Name " + referbean.getFirstName() + " Inserted By " + referbean.getInsertedBy() + " LastName "
				+ referbean.getLastName() + " Mobile No " + referbean.getMobileNo() + " Modified By "
				+ referbean.getModifiedBy() + " Reference Id " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " User Id " + referbean.getUserId());

		return referRepo.findAll();
	}

	@Override
	public List<NimaiCustomerReferrerBean> getRegisterUserByReferrerUser(String emailId) {
		// Changes from Dhiraj
		try {
			List<Refer> registerUsers = referRepo.findRegisterUserByReferrerEmail(emailId);
			LOGGER.info(registerUsers.toString());
			List<NimaiCustomerReferrerBean> custList = new ArrayList<>();

			for (Refer nc : registerUsers) {
				NimaiCustomer customerDetails = new NimaiCustomer();
				LOGGER.info("============parentApprovalStatus:" + nc.getUserid().getKycStatus());
				String parentkycStatus = nc.getUserid().getKycStatus();
				NimaiCustomerReferrerBean ncb = new NimaiCustomerReferrerBean();
				LOGGER.info("============cuUserIdDetails:" + nc.getEmailAddress());
				try {
					customerDetails = getRegiUserRepo.findRegisterUserByReferrerEmail(nc.getEmailAddress());
					LOGGER.info("============cuUserIdDetails:" + customerDetails.getUserid());
					String cuKycStatus = customerDetails.getKycStatus();
					if (parentkycStatus == null || parentkycStatus.isEmpty()
							|| parentkycStatus.equalsIgnoreCase(AppConstants.KYCSTATUS)) {
						ncb = modelMapperUtil.mapNcbResponse(customerDetails, nc);
					} else if ((cuKycStatus == null || cuKycStatus.isEmpty()
							|| cuKycStatus.equalsIgnoreCase(AppConstants.KYCSTATUS))) {
						ncb = modelMapperUtil.mapNcbResponse(customerDetails, nc);
					} else {
						String Status = AppConstants.SPLAN_STATUS;
						List<Object[]> results = getRegiUserRepo.findRegisterUser(customerDetails.getUserid(), Status);

						if (results != null
								&& (customerDetails.getPaymentStatus().equalsIgnoreCase("Approved")
										|| customerDetails.getPaymentStatus().equalsIgnoreCase("Success"))
								&& customerDetails.getKycStatus().equalsIgnoreCase("Approved")) {
							LOGGER.info("============cuUserIdDetails:" + results.toString());
							Float referEarning = Float.valueOf(systemConfig.earningPercentage());
							Float actualREarning = (float) (referEarning / 100);
							System.out.println("actual earning " + actualREarning);
							ncb = modelMapperUtil.mapNcbResults(customerDetails, nc, results, actualREarning);
						} else {
							ncb = modelMapperUtil.mapNcbResponse(customerDetails, nc);

						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("catch");
					continue;
				}

				custList.add(ncb);

			}
			return custList;

		} catch (Exception e) {
			e.printStackTrace();
			response.setErrMessage("Referrer list not available");
			return null;
		}

	}

	@Override
	public List<NimaiSpecCustomerReferrerBean> getSpecRegisterUserByUserId(String userid) {
		// TODO Auto-generated method stub
		List<NimaiCustomer> registerUsers = getRegiUserRepo.findRegisterUserById(userid);
		System.out.println(registerUsers);
		List<NimaiSpecCustomerReferrerBean> custList = new ArrayList<>();
		ModelMapperUtil modelMapper = new ModelMapperUtil();
		for (NimaiCustomer nc : registerUsers) {

			NimaiSpecCustomerReferrerBean ncb = modelMapper.map(nc, NimaiSpecCustomerReferrerBean.class);
			String rmfname = getRegiUserRepo.findRmFirstName(nc.getRmId());

			String rmlname = getRegiUserRepo.findRmLastName(nc.getRmId());
			if (rmfname == null) {
				ncb.setRmFirstName(null);
			} else {
				ncb.setRmFirstName(rmfname);
			}
			if (rmlname == null) {
				ncb.setRmLastName(null);
			} else {
				ncb.setRmLastName(rmlname);
			}
			Float referEarning = Float.valueOf(systemConfig.earningPercentage());
			Float actualREarning = (Float) (referEarning / 100);
			if ((nc.getPaymentStatus().equalsIgnoreCase("Approved")
					|| nc.getPaymentStatus().equalsIgnoreCase("Success"))
					&& nc.getKycStatus().equalsIgnoreCase("Approved")) {
				ncb.setAccountStatus("Active");
				String subscriptionId = getRegiUserRepo.findSubscriptionId(nc.getUserid());

				Integer earn = getRegiUserRepo.findEarning(nc.getUserid());// *(7/100);
				String subscriptionName = getRegiUserRepo.findSubscriptionName(nc.getUserid());
				String status = "ACTIVE";
				NimaiMSubscription currency = subMRepo.findSubscriptionCurrency(subscriptionId, status);
				String currencyNew = "";
				if (currency == null) {
					currencyNew = "";
				} else {
					currencyNew = currency.getCurrency();

				}

				Integer subscriptionFee = getRegiUserRepo.findSubscriptionFee(nc.getUserid());
				if (subscriptionFee == null) {
					ncb.setSubsPlanFee("0");
				} else {
					ncb.setSubsPlanFee(currencyNew + " " + String.valueOf(subscriptionFee));
				}

				if (earn == null) {
					ncb.setEarning(null);
				} else {
					Float value = Float.parseFloat(new DecimalFormat("##.##").format(earn * actualREarning));
					ncb.setEarning(String.valueOf(value));
					// ncb.setEarning(currencyNew + " " + (int) ((int) Math.round(earn *
					// actualREarning)));

				}

				ncb.setSubsPlanName(subscriptionName);
//				if (currency == null || subscriptionFee == null)
//					ncb.setSubsPlanFee(null);
//				else
//					ncb.setSubsPlanFee(currency + " " + String.valueOf(subscriptionFee));

			} else {
				ncb.setAccountStatus("Pending");
			}
			List<NimaiSubscriptionDetails> sPlanList = subRepo.finSplanByReferId(nc.getUserid());
			if (sPlanList.size() == 1) {
				if ((nc.getPaymentStatus().equalsIgnoreCase("Approved")
						|| nc.getPaymentStatus().equalsIgnoreCase("Approved"))
						&& nc.getKycStatus().equalsIgnoreCase("Approved")) {
					Integer totalEarn = getRegiUserRepo.findTotalEarning(nc.getUserid());
					if (totalEarn == null) {
						ncb.setTotalEarning(null);
					} else {
						Float value = Float.parseFloat(new DecimalFormat("##.##").format(totalEarn * actualREarning));
						ncb.setTotalEarning(String.valueOf(value));
						// ncb.setTotalEarning(currency + " " + (int) ((int) Math.round(totalEarn *
						// 0.07)));
					}
				} else {
					ncb.setTotalEarning(null);
				}
			} else {
				Integer totalEarn = getRegiUserRepo.findTotalEarning(nc.getUserid());
				if (totalEarn == null) {
					ncb.setTotalEarning(null);
				} else {
					Float value = Float.parseFloat(new DecimalFormat("##.##").format(totalEarn * actualREarning));
					ncb.setTotalEarning(String.valueOf(value));
					// ncb.setTotalEarning(currency + " " + (int) ((int) Math.round(totalEarn *
					// 0.07)));
				}
			}

			custList.add(ncb);
		}
		return custList;
	}

	@Override
	public String checkKycApprovalStatus(String userId) {
		// TODO Auto-generated method stub
		try {
			NimaiCustomer approvalStatus = getRegiUserRepo.findRegisterUser(userId);
			if (!approvalStatus.getKycStatus().equalsIgnoreCase(AppConstants.STATUS)) {
				return AppConstants.KYCSTATUS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return AppConstants.ERROR;
		}

		return AppConstants.STATUS;

	}

	public static void main(String[] args) {
		float x = 10;
		float y = 100;
		float z = x / y;
		System.out.println(z);

	}
}
