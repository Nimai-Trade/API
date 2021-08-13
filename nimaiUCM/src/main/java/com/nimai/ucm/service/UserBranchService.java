package com.nimai.ucm.service;

import java.util.List;

import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BranchUserListResponse;
import com.nimai.ucm.bean.SubsidiaryListResponse;
import com.nimai.ucm.bean.UserBranchBean;

public interface UserBranchService {

	String saveUserBranchDetails(UserBranchBean branchUserBean, String userid);
	List<SubsidiaryListResponse> getSubsidiaryList(String userId);
	List<BranchUserListResponse> getBranchUserList(String userId);
	List<SubsidiaryListResponse> getAddUserList(String userId);
}
