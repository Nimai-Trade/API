package com.nimai.uam.utility;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiMLogin;
import com.nimai.uam.repository.LoginRepository;
import com.nimai.uam.service.UserService;

@Component
public class ResetUserValidation {

	@Autowired
	UserService userService;

	@Autowired
	LoginRepository loginRepo;

	public String Validation(ResetPasswordBean resetPasswordBean) {

		boolean isUserIdExist = userService.checkUserId(resetPasswordBean.getUserId());

		boolean isEmailExist = userService.checkEmailId(resetPasswordBean.getEmailId());

		String returnStr = "";
		try {
			if (!isUserIdExist) {
				return "UserId is Not Exist";
			}
			if (!isEmailExist) {
				return "EmailAddress is Not Exist";
			}
			if ((resetPasswordBean.getNewPassword() == null) && (resetPasswordBean.getRetypePaasword() == null)) {
				return "New password and Retype password should not be empty";
			}

			if (resetPasswordBean.getNewPassword() != resetPasswordBean.getRetypePaasword()) {
				return "New password & Retype Paswword Should Match With Each Other ";
			}
			returnStr = "success";
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failed : " + e.getMessage();
		}
		return returnStr;

	}

	public String passwordValidation(ResetPasswordBean resetPasswordBean) {

//		boolean isUserIdExist = userService.checkUserId(resetPasswordBean.getUserId());
//		Optional<NimaiMLogin> loginDetails = loginRepo.findByUserId(resetPasswordBean.getUserId());
//		NimaiMLogin login = loginDetails.get();
		String returnStr = "";
		try {
//			if (!isUserIdExist) {
//				return "UserId is Not Exist" + resetPasswordBean.getUserId();
//			}
//			if (login.getIsActPassed().equalsIgnoreCase("Inactive")) {
//				return "User Account is Inactive";
//			}
			if ((resetPasswordBean.getNewPassword() == null) && (resetPasswordBean.getRetypePaasword() == null)) {
				return "New password and Retype password should not be empty";
			}

			if (!resetPasswordBean.getNewPassword().equalsIgnoreCase(resetPasswordBean.getRetypePaasword())) {
				return "New password & Retype Paswword Should Match With Each Other ";
			}
			returnStr = "success";
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failed : " + e.getMessage();
		}
		return returnStr;

	}

	public String loginRequestValidation(LoginRequest loginRequestBean) {

		String is_Act_Passed_Status = userService.checkUserStatus(loginRequestBean.getUserId());
		String returnStr = "";
		try {

			if (is_Act_Passed_Status.equalsIgnoreCase("NO VALUE")) {
				return "Invalid User ID";
			}
			if (loginRequestBean != null) {
				if ((loginRequestBean.getUserId() == null) || (loginRequestBean.getUserId().trim().isEmpty())) {
					return "Please enter UserId";
				}

				if ((loginRequestBean.getPassword() == null) || (loginRequestBean.getPassword().trim().isEmpty())) {
					return "Please enter Password";
				}
				if (!is_Act_Passed_Status.equalsIgnoreCase("Active")) {
					return "Request you to kindly activate your account, using the set password link shared earlier.";
				}
			}
			returnStr = "success";
		} catch (Exception e) {
			e.printStackTrace();
			returnStr = "Failed : " + e.getMessage();
		}
		return returnStr;

	}

}
