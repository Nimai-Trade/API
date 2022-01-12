package com.nimai.ucm.service;

import java.util.ArrayList;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.ucm.bean.BranchUserListResponse;
import com.nimai.ucm.bean.SubsidiaryListResponse;
import com.nimai.ucm.bean.TermsAndPolicyBean;
import com.nimai.ucm.bean.UserBranchBean;
import com.nimai.ucm.entity.CustomerTnC;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.TermsAndPolicy;
import com.nimai.ucm.entity.UserBranchEntity;
import com.nimai.ucm.repository.CustomerRepository;
import com.nimai.ucm.repository.CustomerTnCRepo;
import com.nimai.ucm.repository.TermsAndPolicyRepo;
import com.nimai.ucm.repository.UserBranchRepository;
import com.nimai.ucm.repository.UserDetailRepository;

@Service
public class UserBranchServiceImpl implements UserBranchService {

	// Changes from Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(UserBranchServiceImpl.class);
	// End

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private UserBranchRepository userBranchRepository;
	
	@Autowired
	private CustomerTnCRepo customerTnCRepo;
	
	@Autowired
	UserDetailRepository detailRepository;
	
	@Autowired
	private TermsAndPolicyRepo termPolicyRepo;

	@Override
	public String saveUserBranchDetails(UserBranchBean branchUserBean, String userid) {
		NimaiCustomer customer = customerRepository.findByUSERID(userid);
		UserBranchEntity brDetails = userBranchRepository.getBranchUser(branchUserBean.getEmail_id());
		ModelMapper modelMapper = new ModelMapper();
		Date currentDateTime = new Date();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		if (brDetails != null) {
			LOGGER.info("Update User Branch Details method is invoked in UserBranchServiceIMpl class");
			UserBranchEntity brUserToupdate = userBranchRepository.getOne(brDetails.getId());
			brUserToupdate.setInsert_time(currentDateTime);
			brDetails.setUser_id(userid);
			brDetails.setEmployee_id(("EM_").concat(branchUserBean.getEmployee_id()));
			brUserToupdate.setId(brDetails.getId());
			brDetails.setEmployee_name(branchUserBean.getEmployee_name());
			userBranchRepository.save(brUserToupdate);
			String updatedResponseID = Integer.toString(brDetails.getId());
			return updatedResponseID;
		}else {
			LOGGER.info("Save User Branch Details method is invoked in UserBranchServiceIMpl class");
			String email = customer.getEmailAddress();
			String customerdomain = email.substring(email.indexOf("@") + 1);
			String credentialemail = branchUserBean.getEmail_id();
			UserBranchEntity userBranchEntity = modelMapper.map(branchUserBean, UserBranchEntity.class);
			String validateemail = credentialemail.substring(credentialemail.indexOf("@") + 1);
			if (customerdomain.equals(validateemail)) {
				userBranchEntity.setInsert_time(currentDateTime);
				userBranchEntity.setUser_id(userid);
				userBranchEntity.setEmployee_id(("EM_").concat(branchUserBean.getEmployee_id()));
				userBranchEntity.setEmployee_name(branchUserBean.getEmployee_name());
				userBranchRepository.save(userBranchEntity);
			} else {
				return "Registered Email Domain not matched";
			}
			String newResponseID = Integer.toString(userBranchEntity.getId());
			return newResponseID;
		}
		
		
		
	}

	@Override
	public List<SubsidiaryListResponse> getSubsidiaryList(String userId) {
		// TODO Auto-generated method stub
		String kycStatus = "Approved";
		List<NimaiCustomer> subList = customerRepository.getSubUserList(userId, kycStatus);
		//List<NimaiCustomer> listWithoutDuplicates = subList.stream().distinct().collect(Collectors.toList());
		NimaiCustomer cust = customerRepository.findByUSERID(userId);
		List<SubsidiaryListResponse> subSidiaryBean = new ArrayList<SubsidiaryListResponse>();
		for (NimaiCustomer cu : subList) {
			if (!cu.getAccountType().equalsIgnoreCase("REFER")) {
				SubsidiaryListResponse response = new SubsidiaryListResponse();
				response.setSubCompany(cu.getCompanyName());
				response.setSubName(cu.getFirstName());
				response.setSubUserId(cu.getUserid());
				response.setBankName(cu.getBankNbfcName());
				response.setEmailId(cu.getEmailAddress());
				subSidiaryBean.add(response);

			}

		}
if(subList.size()==0) {
	SubsidiaryListResponse addResponse = new SubsidiaryListResponse();
	addResponse.setSubUserId("All".concat(userId));
	// addResponse.setSubUserId(userId);
	addResponse.setSubCompany("All");
	addResponse.setSubName("All");
	addResponse.setEmailId("All");

	SubsidiaryListResponse newAddResponse = new SubsidiaryListResponse();
	newAddResponse.setSubUserId(userId);
	newAddResponse.setSubCompany(cust.getCompanyName());
	newAddResponse.setSubName(cust.getFirstName());
	newAddResponse.setBankName(cust.getBankNbfcName());
	newAddResponse.setEmailId(cust.getEmailAddress());
	subSidiaryBean.add(0, addResponse);
	subSidiaryBean.add(1, newAddResponse);

	return subSidiaryBean;
}
		SubsidiaryListResponse addResponse = new SubsidiaryListResponse();
		addResponse.setSubUserId("All".concat(userId));
		// addResponse.setSubUserId(userId);
		addResponse.setSubCompany("All");
		addResponse.setSubName("All");
		addResponse.setEmailId("All");

		SubsidiaryListResponse newAddResponse = new SubsidiaryListResponse();
		newAddResponse.setSubUserId(userId);
		newAddResponse.setSubCompany(cust.getCompanyName());
		newAddResponse.setSubName(cust.getFirstName());
		newAddResponse.setBankName(cust.getBankNbfcName());
		newAddResponse.setEmailId(cust.getEmailAddress());
		subSidiaryBean.add(1, newAddResponse);
		subSidiaryBean.add(0, addResponse);

		return subSidiaryBean;
	}

	@Override
	public List<BranchUserListResponse> getBranchUserList(String userId) {
		// TODO Auto-generated method stub
		List<UserBranchEntity> branchList = userBranchRepository.getBranchUserList(userId);
		List<UserBranchEntity> listWithoutDuplicates = branchList.stream().distinct().collect(Collectors.toList());

		List<BranchUserListResponse> branchUserListResponse = listWithoutDuplicates.stream().map(obj -> {
			BranchUserListResponse response = new BranchUserListResponse();
			response.setUserNAme(obj.getEmployee_name());
			response.setEmployeeId(obj.getEmployee_id());
			response.setEmployeeEmail(obj.getEmail_id());
			return response;
		}).collect(Collectors.toList());

		NimaiCustomer customer = customerRepository.findByUSERID(userId);

		BranchUserListResponse response = new BranchUserListResponse();
		response.setUserId("All".concat(userId));
		response.setUserNAme("All");
		response.setEmployeeEmail("All");

		BranchUserListResponse addResponse = new BranchUserListResponse();
		addResponse.setUserId(userId);
		addResponse.setUserNAme("All");
		addResponse.setEmployeeEmail(customer.getEmailAddress());

		branchUserListResponse.add(0, response);
		branchUserListResponse.add(1, addResponse);

		return branchUserListResponse;
	}

	@Override
	public List<SubsidiaryListResponse> getAddUserList(String userId) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String kycStatus = "Approved";
		List<NimaiCustomer> subList = customerRepository.getSubUserList(userId, kycStatus);
		NimaiCustomer cust = customerRepository.findByUSERID(userId);
		List<SubsidiaryListResponse> subSidiaryBean = new ArrayList<SubsidiaryListResponse>();
		for (NimaiCustomer cu : subList) {
			if (!cu.getAccountType().equalsIgnoreCase("REFER")) {
				SubsidiaryListResponse response = new SubsidiaryListResponse();
				response.setSubCompany(cu.getCompanyName());
				response.setSubName(cu.getFirstName());
				response.setSubUserId(cu.getUserid());
				response.setBankName(cu.getBankNbfcName());
				response.setEmailId(cu.getEmailAddress());
				subSidiaryBean.add(response);

			}

		}

		SubsidiaryListResponse addResponse = new SubsidiaryListResponse();
		// addResponse.setSubUserId("All".concat(userId));
		addResponse.setEmailId("All");
		addResponse.setSubUserId(userId);
		addResponse.setSubCompany("All");
		addResponse.setSubName("All");

		SubsidiaryListResponse newAddResponse = new SubsidiaryListResponse();
		newAddResponse.setSubUserId(userId);
		newAddResponse.setSubCompany(cust.getCompanyName());
		newAddResponse.setSubName(cust.getFirstName());
		newAddResponse.setBankName(cust.getBankNbfcName());
		newAddResponse.setEmailId(cust.getEmailAddress());
		subSidiaryBean.add(1, newAddResponse);
		subSidiaryBean.add(0, addResponse);

		return subSidiaryBean;
	}
	
	@Override
	public TermsAndPolicyBean getTermsAndPolicy()
	{
		TermsAndPolicy tp=termPolicyRepo.getTermsAndPolicyDetails();
		TermsAndPolicyBean tpb=new TermsAndPolicyBean();
		tpb.setId(tp.getId());
		tpb.setVersion(tp.getVersion());
		tpb.setTerms(tp.getTerms());
		tpb.setPolicy(tp.getPolicy());
		tpb.setCreatedDate(tp.getCreatedDate());
		tpb.setStatus(tp.getStatus());
		
		return tpb;
	}

	@Override
	public void updateTermsPolicy(String userId) {
		// TODO Auto-generated method stub
		TermsAndPolicy tp=termPolicyRepo.getLastTermsAndPolicyDetails();
		NimaiCustomer nc = detailRepository.getOne(userId);
		
		LOGGER.info("====== Logging user previous TnC acceptance date =====");
		System.out.println("====== Logging user previous TnC acceptance date =====");
		CustomerTnC ct=new CustomerTnC();
		ct.setUserId(userId);
		ct.setVersion(tp.getVersion());
		ct.setAcceptedDate(nc.gettCInsertedDate());
		ct.setInsertedDate(new Date());
		customerTnCRepo.save(ct);
		
		LOGGER.info("====== Updating user TnC acceptance date =====");
		System.out.println("====== Updating user TnC acceptance date =====");
		nc.settCInsertedDate(new Date());
		customerRepository.save(nc);
	}
	
	
}
