package com.nimai.uam.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.controller.passwordPolicyController;
import com.nimai.uam.entity.NimaiClient;
import com.nimai.uam.entity.NimaiEmployeeLoginAttempt;
import com.nimai.uam.entity.NimaiMLogin;
import com.nimai.uam.repository.LoginRepository;
import com.nimai.uam.repository.NimaiEmployeeLoginAttemptRepo;
import com.nimai.uam.repository.UserDetailRepository;
import com.nimai.uam.utility.MailService;
import com.nimai.uam.utility.Utils;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	EntityManager em;

	@Autowired
	LoginRepository loginRepository;

	@Autowired
	UserDetailRepository userDetailsRepository;

	@Autowired
	Utils utils;

	@Autowired
	MailService mail;

	@Autowired
	NimaiEmployeeLoginAttemptRepo attemptRepo;

	@Override
	public boolean checkUserId(String userId) {
		// TODO Auto-generated method stub
		return userDetailsRepository.existsById(userId);
	}

	@Override
	public boolean checkEmailId(String emailId) {
		// TODO Auto-generated method stub
		return userDetailsRepository.existsByEmailAddress(emailId);
	}

	@Override
	public boolean updateResetToken(ResetPasswordBean resetPasswordBean) {

		Optional<NimaiMLogin> login = loginRepository.findByUserId(resetPasswordBean.getUserId());

		if (login != null) {
			String token = utils.generatePasswordResetToken();
			NimaiMLogin nimaiLogin = login.get();
			Date tokenExpiry = utils.getLinkExpiryDate();
			nimaiLogin.setToken(token);
			nimaiLogin.setTokenExpiryDate(tokenExpiry);
			// nimaiLogin.setResetPasswordStatus('0');
			loginRepository.save(nimaiLogin);

			String link = "http://localhost:4200/forget?key=" + token;
			try {

				mail.sendEmail(link, resetPasswordBean);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return true;
			// }
			// return false;
		}
		return false;

	}

	@Override
	public NimaiMLogin getUserDetailsByTokenKey(String token) {

		NimaiMLogin nimaiLogin = loginRepository.findUserByToken(token);

		return nimaiLogin;

	}

	@Override
	public boolean checkUserTokenKey(String tokenKey) {

		String nimaiLogin = loginRepository.checkUserToken(tokenKey);

		if (nimaiLogin.equals(tokenKey)) {

			return true;
		} else {
			return false;
		}
	}

	@Override
	public NimaiMLogin saveResetPasswordDetails(ResetPasswordBean resetPasswordBean) {
		NimaiMLogin nimaiLoginTOken = loginRepository.findUserByToken(resetPasswordBean.getToken);
		if (nimaiLoginTOken == null) {
			NimaiMLogin resetPassLogin = loginRepository.findUserByActiveToken(resetPasswordBean.getToken);
			Optional<NimaiMLogin> login = loginRepository.findById(resetPassLogin.getLoginId());
			Optional<NimaiClient> custDetails = userDetailsRepository.findById(resetPassLogin.getUserid().getUserid());
			NimaiMLogin nimaiLogin = login.get();
			NimaiClient clientDetail = custDetails.get();
			clientDetail.setAccountStatus("ACTIVE");
			userDetailsRepository.save(clientDetail);
			nimaiLogin.setIsActPassed("ACTIVE");
			nimaiLogin.setPassword(resetPasswordBean.getRetypePaasword());
			loginRepository.save(nimaiLogin);
			return resetPassLogin;
		} else { 
			Optional<NimaiMLogin> login = loginRepository
					.findInactiveLoginByUserId(nimaiLoginTOken.getUserid().getUserid());
//			Optional<NimaiClient> client = userDetailsRepository.findById(nimaiLoginTOken.getUserid().getUserid());
			NimaiClient customer=userDetailsRepository.getOne(nimaiLoginTOken.getUserid().getUserid());
			if (customer.getAccountType().equalsIgnoreCase("REFER")
					|| customer.getAccountType().equalsIgnoreCase("SUBSIDIARY")
					|| customer.getAccountType().equalsIgnoreCase("BANKUSER")) {
				//NimaiClient customer=userDetailsRepository.getOne(client.get().getUserid());
				customer.settCInsertedDate(Calendar.getInstance().getTime());
				customer.setTcFlag(resetPasswordBean.getTcFlag());
				//userDetailsRepository.save(customer);
			}
			NimaiMLogin nimaiLogin = login.get();
			NimaiClient clientDetails = customer;
			clientDetails.setAccountStatus("ACTIVE");
			userDetailsRepository.save(clientDetails);
			nimaiLogin.setIsActPassed("ACTIVE");
			nimaiLogin.setPassword(resetPasswordBean.getRetypePaasword());
			loginRepository.save(nimaiLogin);

			return nimaiLogin;
		}

	}

	@Override
	public boolean checkSignIncrendentials(LoginRequest loginRequestBean) {
		Optional<NimaiMLogin> login = loginRepository.findByUserId(loginRequestBean.getUserId());
		NimaiMLogin loginCredentilas = login.get();
		NimaiEmployeeLoginAttempt attempt = new NimaiEmployeeLoginAttempt();
		int count;
		Date currentTime = new Date();
	//	Date currentDateTime = new Date();
		LOGGER.info("logincredentials", loginCredentilas);
		if ((!loginRequestBean.getUserId().equals(loginCredentilas.getUserid().getUserid())
				|| (!loginRequestBean.getPassword().equals(loginCredentilas.getPassword()))))

		{
			
			NimaiEmployeeLoginAttempt countDetails = attemptRepo.getcountDetailsByEmpCode(loginRequestBean.getUserId());
		
			if (countDetails == null) {
				 count = 1;
				attempt.setCount(count);
				attempt.setEmpCode(loginRequestBean.getUserId());
				attempt.setInsertedDate(currentTime);
				attemptRepo.save(attempt);
			}

			else {
				NimaiEmployeeLoginAttempt countDet = attemptRepo.getOne(countDetails.getId());
				if (countDet.getCount() == 0) {
					countDet.setCount(0 + 1);
				} else {
					countDet.setCount(countDetails.getCount() + 1);
				}

				countDet.setEmpCode(loginRequestBean.getUserId());
				countDet.setInsertedDate(currentTime);
				attemptRepo.save(countDet);

			}
			
			return false;
		}
		NimaiEmployeeLoginAttempt countDetails = attemptRepo.getcountDetailsByEmpCode(loginRequestBean.getUserId());
		if(countDetails==null) {
			 count = 1;
				attempt.setCount(count);
				attempt.setEmpCode(loginRequestBean.getUserId());
				attempt.setInsertedDate(currentTime);
				attemptRepo.save(attempt);
		}else {
			 count = 0;
			 countDetails.setCount(count);
			 countDetails.setEmpCode(loginRequestBean.getUserId());
			 countDetails.setInsertedDate(currentTime);
				attemptRepo.save(countDetails);
		}

		return true;
	}

	@Override
	public String checkUserStatus(String loginRequestBean) {
		try {
			Optional<NimaiMLogin> login = loginRepository.findByUserId(loginRequestBean);
			if (login != null) {
				try {
					NimaiMLogin loginCredentials = login.get();
					String is_Act_Passed_Status = loginCredentials.getIsActPassed();
					return is_Act_Passed_Status;
				} catch (Exception e) {
					e.printStackTrace();
					return "NO VALUE";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "NO VALUE";
		}

		return "NO VALUE";
	}

	@Override
	public String getKycStatus(String userId) {
		Optional<NimaiClient> kycStatus = userDetailsRepository.findById(userId);
		if (kycStatus != null && kycStatus.get().getKycStatus() != null && !kycStatus.get().getKycStatus().isEmpty()) {
			return kycStatus.get().getKycStatus();
		}
		return null;
	}

	@Override
	public void updateLastLoginTime(String userId) {
		// TODO Auto-generated method stub
		Date d = new Date();
		loginRepository.updateLastLogin(userId, d);

	}

	@Override
	public int getCountOfCustomerLoggedIn() {
		// TODO Auto-generated method stub
		return loginRepository.getCountOfLastLoginCustomer();
	}

	@Override
	public int getCountOfBankLoggedIn() {
		// TODO Auto-generated method stub
		return loginRepository.getCountOfLastLoginBank();
	}

	@Override
	public NimaiMLogin changePasswordDetails(ResetPasswordBean resetPasswordBean) {
		// TODO Auto-generated method stub
		// NimaiMLogin nimaiLoginTOken =
		// loginRepository.findUserByToken(resetPasswordBean.getToken);}
		try {
			Optional<NimaiMLogin> login = loginRepository.findActiveLoginByUserId(resetPasswordBean.getUserId(),
					resetPasswordBean.getOldPassword());

			NimaiMLogin nimaiLogin = login.get();
			// nimaiLogin.setIsActPassed("ACTIVE");
			nimaiLogin.setPassword(resetPasswordBean.getRetypePaasword());
			loginRepository.save(nimaiLogin);
			return nimaiLogin;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void removeToken(String userId) {
		// TODO Auto-generated method stubqww
		loginRepository.deleteToken(userId);
	}
	
	@Override
	public boolean checkSubsidiaryStatus(String userId) {
		// TODO Auto-generated method stub
		//NimaiClient data=null;
		try
		{
			NimaiClient subsidiaryData=userDetailsRepository.checkSubsidiary(userId);
			NimaiClient data=userDetailsRepository.checkSubsidiaryActive(userId);
			//System.out.println(data.getAccountStatus());
			if(subsidiaryData!=null && data==null)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
			return true;
		}
	
	}
}
