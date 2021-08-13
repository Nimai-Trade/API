package com.nimai.uam.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.uam.bean.ForgotEmailResponseBean;
import com.nimai.uam.bean.GenericResponse;
import com.nimai.uam.bean.LoginRequest;
import com.nimai.uam.bean.ResetPasswordBean;
import com.nimai.uam.entity.NimaiClient;
import com.nimai.uam.entity.NimaiEmployeeLoginAttempt;
import com.nimai.uam.entity.NimaiMLogin;
import com.nimai.uam.repository.LoginRepository;
import com.nimai.uam.repository.NimaiEmployeeLoginAttemptRepo;
import com.nimai.uam.service.CaptchService;
import com.nimai.uam.service.UserService;
import com.nimai.uam.utility.ErrorDescription;
import com.nimai.uam.utility.ResetUserValidation;

@RestController
@RequestMapping("/passwordPolicy")
public class passwordPolicyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(passwordPolicyController.class);

	private HttpServletRequest request;

	@Autowired
	UserService resetUserService;

	@Autowired
	ResetUserValidation resetUserValidation;

	@Autowired
	CaptchService captchaService;

	@Autowired
	NimaiEmployeeLoginAttemptRepo attemptRepo;
	
	@Autowired
	LoginRepository loginRepository;

	@Autowired
	GenericResponse<Object> response;

	public passwordPolicyController() {
		System.out.println("password policy load");
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> userSignIn(@RequestBody LoginRequest loginRequestBean) {
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			System.out.println("=============================Current IP address========== : " + ip.getHostAddress());

		} catch (UnknownHostException e) {

			e.printStackTrace();
	
		}
		boolean captchaVerified = captchaService.verify(loginRequestBean.getRecaptchaResponse());
		LOGGER.debug("Personal Details recaptchResponse" + loginRequestBean.getRecaptchaResponse());

		if (captchaVerified) {
	
			String errorString = this.resetUserValidation.loginRequestValidation(loginRequestBean);

			if (errorString.equalsIgnoreCase("success")) {
				try {
					boolean subsidiaryStatus=resetUserService.checkSubsidiaryStatus(loginRequestBean.getUserId());
					if(subsidiaryStatus==true)
					{
						response.setMessage("Account Inactive");
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}
					Date currentTime = new Date();
					NimaiEmployeeLoginAttempt countDetails = attemptRepo
							.getcountDetailsByEmpCode(loginRequestBean.getUserId());
					if (countDetails == null) {
						boolean nimaiLogin = resetUserService.checkSignIncrendentials(loginRequestBean);
						LOGGER.debug("Object boolean", nimaiLogin);
						if (nimaiLogin == false) {
							response.setErrCode("ASA010");
							response.setMessage(ErrorDescription.getDescription("ASA010"));
							return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
						}
					}

					else if (countDetails.getCount() < 3) {
						boolean nimaiLogin = resetUserService.checkSignIncrendentials(loginRequestBean);
						LOGGER.debug("Object boolean", nimaiLogin);
						if (nimaiLogin == false) {

							response.setErrCode("ASA010");
							response.setMessage(ErrorDescription.getDescription("ASA010"));
							return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
						}
					} else if (countDetails.getCount() >= 3) {

						long diff = currentTime.getTime() - countDetails.getInsertedDate().getTime();
						long differenceMinutes = diff / (60 * 1000) % 60;

						LOGGER.info("=============currentTime" + differenceMinutes);

						LOGGER.info("=============currentTime" + countDetails.getInsertedDate().getTime());
						Calendar calendar = Calendar.getInstance();
						LOGGER.info("=============difference in minutes" + differenceMinutes);
						if (differenceMinutes <= 10) {
							response.setMessage(
									"You have excedded maximum attempts to login into the system.Your account is temporarily blocked.Please try again after 10mins.");
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else if (differenceMinutes > 10) {
							NimaiEmployeeLoginAttempt countDetail = attemptRepo.getOne(countDetails.getId());

							countDetail.setCount(0);
							countDetail.setEmpCode(loginRequestBean.getUserId());
							countDetail.setInsertedDate(currentTime);
							attemptRepo.save(countDetail);
							boolean nimaiLogin = resetUserService.checkSignIncrendentials(loginRequestBean);
							System.out.println(nimaiLogin);
							if (nimaiLogin == false) {
								response.setErrCode("ASA010");
								response.setMessage(ErrorDescription.getDescription("ASA010"));
								return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
							}
						}
					}

					String kycStatus = resetUserService.getKycStatus(loginRequestBean.getUserId());
					if (kycStatus != null) {
						response.setFlag(1);
						response.setMessage("KycStauts:" + kycStatus);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					} else {

						response.setFlag(1);
						response.setMessage("KycStauts:" + kycStatus);
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}

				} catch (Exception e) {
					e.printStackTrace();
					response.setErrCode("ASA009");
					response.setMessage(ErrorDescription.getDescription("ASA009"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {

				response.setMessage(errorString.toString());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.info("INSIDE ELSE CONDITION OF CAPTCH IN passwordPolicy CONTROLLER"
					+ loginRequestBean.getRecaptchaResponse());
			LOGGER.info("INSIDE ELSE CONDITION OF CAPTCH IN passwordPolicy CONTROLLER" + captchaVerified);
			response.setMessage("Invalid Captcha");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@CrossOrigin("*")
	@PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetpasswordFirstAttempt(@RequestBody ResetPasswordBean resetPasswordBean)

	{

		String errorString = this.resetUserValidation.passwordValidation(resetPasswordBean);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				NimaiMLogin nimaiLogin = resetUserService.saveResetPasswordDetails(resetPasswordBean);
				ForgotEmailResponseBean responseBean = new ForgotEmailResponseBean();
				responseBean.setUserId(nimaiLogin.getUserid().getUserid());
				responseBean.setEmailId(nimaiLogin.getUserid().getEmailAddress());
				response.setData(responseBean);
				response.setErrCode("ASA001");
				response.setMessage(ErrorDescription.getDescription("ASA001"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				response.setMessage(ErrorDescription.getDescription("Error while resetting password"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin("*")
	@PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> changePassword(@RequestBody ResetPasswordBean resetPasswordBean) {

		String errorString = this.resetUserValidation.passwordValidation(resetPasswordBean);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				// NimaiMLogin nimaiLogin =
				// resetUserService.saveResetPasswordDetails(resetPasswordBean);
				NimaiMLogin nimaiLogin = resetUserService.changePasswordDetails(resetPasswordBean);
				if (nimaiLogin == null) {
					response.setErrCode("ASA011");
					response.setMessage(ErrorDescription.getDescription("ASA011"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					response.setErrCode("ASA008");
					response.setMessage(ErrorDescription.getDescription("ASA008"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}

			} catch (Exception e) {
				response.setErrCode("EXE000");
				response.setMessage(ErrorDescription.getDescription("Oops! Something went wrong"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
		}
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin("*")
	@PostMapping(value = "/usertoken/{token}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> findUserByToken(@PathVariable("token") String token) {

		try {
			NimaiMLogin loginUser = this.resetUserService.getUserDetailsByTokenKey(token);
			if (loginUser != null) {
				response.setData(String.valueOf(loginUser.getUserid()));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setData("Invalid User");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin("*")
	@GetMapping("/getLiveUserStats")
	public ResponseEntity<Object> findUserByToken() {
	
		try {
			long totalCustomer = 0, totalBank = 0;
			totalCustomer =loginRepository.getCountOfLastLoginCustomer();
			totalBank = loginRepository.getCountOfLastLoginBank();
			LOGGER.debug("No Of Customer:" + totalCustomer);
			LOGGER.debug("No Of Bank:" + totalBank);
			response.setData("No. Of Customer: " + totalCustomer + ", No. Of Bank: " + totalBank);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(value = "*", allowedHeaders = "*")
	@RequestMapping(value = "logOut", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> userLogOut(@RequestBody LoginRequest loginRequestBean, HttpServletRequest request) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		String userId = loginRequestBean.getUserId();
		try {

			System.out.println("Logout User:" + userId);
			resetUserService.removeToken(userId);
			resetUserService.updateLastLoginTime(userId);
			// listOfUser.get("customers").remove(userId);

			response.setData("User Logout Successfully");
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
	}

}
