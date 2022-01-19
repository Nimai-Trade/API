package com.nimai.ucm.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nimai.ucm.bean.FieoMember;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.NimaiCustomerReferrerBean;
import com.nimai.ucm.bean.NimaiSpecCustomerReferrerBean;
import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.bean.ReferIdBean;
import com.nimai.ucm.bean.ReferrerBean;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.Refer;

public interface ReferService {

	public ReferIdBean saveReferService(ReferBean referbean, String r1);

	public boolean checkEmailId(String emailId);

	public List<Refer> viewReferB(ReferBean referbean);

	public Refer updateRefer(ReferBean referbean);

	public ReferrerBean getReferByUserId(String userId);
	
	public List<NimaiCustomerReferrerBean> getRegisterUserByReferrerUser(String userid);

	public List<NimaiSpecCustomerReferrerBean> getSpecRegisterUserByUserId(String userid);

	public String checkKycApprovalStatus(String userId);

	public List<FieoMember> getReferrerFieoLeads(String referUserId);

	public String getEmailIdByFieoReferId(String userId);

	public Refer getReferDetails(String referEmailId);

}
