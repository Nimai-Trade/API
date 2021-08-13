package com.nimai.uam.service;

import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiClient;
import com.nimai.uam.entity.NimaiMLogin;

public interface UserService {

	boolean checkUserId(String userId);

	boolean checkEmailId(String emailId);

	boolean updateResetToken(ResetPasswordBean resetPasswordBean);

	NimaiMLogin getUserDetailsByTokenKey(String token);

	boolean checkUserTokenKey(String tokenKey);

	NimaiMLogin saveResetPasswordDetails(ResetPasswordBean resetPasswordBean);

	boolean checkSignIncrendentials(LoginRequest loginRequestBean);

	String checkUserStatus(String string);

	String getKycStatus(String userId);

	void updateLastLoginTime(String userId);

	int getCountOfCustomerLoggedIn();

	int getCountOfBankLoggedIn();

	NimaiMLogin changePasswordDetails(ResetPasswordBean resetPasswordBean);

	void removeToken(String userId);

	boolean checkSubsidiaryStatus(String userId);

}
