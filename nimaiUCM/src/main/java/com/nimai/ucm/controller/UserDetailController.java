package com.nimai.ucm.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
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

import com.nimai.ucm.bean.BranchUserBean;
import com.nimai.ucm.bean.BusinessDetailsBean;
import com.nimai.ucm.bean.CountryResponse;
import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.OwnerMasterBean;
import com.nimai.ucm.bean.PersonalDetailsBean;
import com.nimai.ucm.bean.StateResponce;
import com.nimai.ucm.entity.NimaiCustomer;
import com.nimai.ucm.entity.NimaiLookupCountries;
import com.nimai.ucm.repository.NimaiLookupCountriesRepository;
import com.nimai.ucm.service.CaptchService;
import com.nimai.ucm.service.CountryService;
import com.nimai.ucm.service.RegisterUserService;
import com.nimai.ucm.service.TokenService;
import com.nimai.ucm.utility.ErrorDescription;
import com.nimai.ucm.utility.RegistrationId;
import com.nimai.ucm.utility.ValidateUserDetails;

@RestController
@RequestMapping("/UserDetails")
public class UserDetailController {

	static Logger logger = Logger.getLogger(UserDetailController.class.getName());

	@Autowired
	ValidateUserDetails vud;

	@Autowired
	RegisterUserService registerUser;

	@Autowired
	TokenService tokenService;

	@Autowired
	RegistrationId register;

	@Autowired
	CountryService countryService;

	@Autowired
	CaptchService captchaService;
	
	@Autowired
	GenericResponse<Object> response;
	

	@Autowired
	NimaiLookupCountriesRepository nimaiLookupCountriesRepository;

	/*----------------- Method For Personal Details ---------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/registerUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registerUser(@RequestBody PersonalDetailsBean personDetailsBean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		logger.info(" ================ UserDetail Controller ================");
		System.out.println(personDetailsBean.toString());
		String errorString = this.vud.validatePersonalDetails(personDetailsBean);
		if (errorString.equalsIgnoreCase("Success")) {
			try {
				if (personDetailsBean.getAccount_type().equalsIgnoreCase("SUBSIDIARY"))
						//|| personDetailsBean.getAccount_type().equalsIgnoreCase("BANKUSER")) 
				{
					int subsidiaryCount = registerUser.getActiveSubsidiaryCount(personDetailsBean.getAccount_source());
					int subsidiaryUtilizedCount = registerUser
							.getSubsidiaryUtilizedCount(personDetailsBean.getAccount_source());
					if (subsidiaryCount <= subsidiaryUtilizedCount) {
						response.setStatus("Failure");
						response.setErrMessage(
								"You had no / reached Maximum Subsidiary Count. Please Renew Your Subscription.");
						return new ResponseEntity<Object>(response, HttpStatus.OK);
					}
				}
				if (personDetailsBean.getAccount_type().equalsIgnoreCase("REFER")
						|| personDetailsBean.getAccount_type().equalsIgnoreCase("SUBSIDIARY")
						|| personDetailsBean.getAccount_type().equalsIgnoreCase("BANKUSER")) {
					try {
						personDetailsBean = registerUser.savePersonalDetails(personDetailsBean);
					} catch (Exception e) {
						e.printStackTrace();
						response.setStatus("Failure");
						response.setErrMessage("Error while saving details");
						;

						return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
					}
					response.setStatus(errorString);
					response.setData(personDetailsBean);
					response.setErrCode("ASA001");
					response.setErrMessage(ErrorDescription.getDescription("ASA001"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					boolean captchaVerified = captchaService.verify(personDetailsBean.getRecaptchaResponse());
					
		System.out.println("Peersonal Details recaptchResponse" + personDetailsBean.getRecaptchaResponse());
		if (captchaVerified) {
		System.out.println("Response from captcha service" + captchaVerified);
				personDetailsBean = registerUser.savePersonalDetails(personDetailsBean);
						response.setStatus(errorString);
						response.setData(personDetailsBean);
						response.setErrCode("ASA001");
						response.setErrMessage(ErrorDescription.getDescription("ASA001"));
						return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
	System.out.println("INSIDE ELSE CONDITION OF CAPTCH IN USERDETAILS CONTROLLER"
			+ personDetailsBean.getRecaptchaResponse());
		System.out
		.println("INSIDE ELSE CONDITION OF CAPTCH IN USERDETAILS CONTROLLER" + captchaVerified);
		response.setErrMessage("Invalid captcha");
		personDetailsBean.setStatus(400);
				response.setErrCode("ASA010");
		response.setErrMessage(ErrorDescription.getDescription("ASA010"));
		return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

				}

			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("ASA012");
				response.setErrMessage(ErrorDescription.getDescription("ASA012") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}

		} else {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(errorString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Method For View Personal Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewPersonalDetails/{userId}")
	public ResponseEntity<Object> viewPersonalDetails(@PathVariable String userId, HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
//		System.out.println("Authorization: " + token.substring(7));
		logger.info("Authorization: " + token.substring(7));
		try {
			boolean validToken = tokenService.validateToken(userId, token.substring(7));

			if (validToken) {
				try {
					PersonalDetailsBean personDetailsBean = registerUser.getPersonalDetails(userId);

					response.setData(personDetailsBean);
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} catch (Exception exception) {
					response.setStatus("Failure");
					response.setErrCode("ASA012");
					logger.error(exception.getMessage());

					if (exception.getMessage().contains("Unable to find"))
						response.setErrMessage("Records not Found");
					else
						response.setErrMessage(ErrorDescription.getDescription("ASA012"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setErrMessage("Unauthorized Access.");
				return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}
	/*---------------------- Method For View Personal Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewPersonalDetails/{userId}/{emailId}")
	public ResponseEntity<Object> viewPersonalDetails(@PathVariable String userId, @PathVariable String emailId, HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
//		System.out.println("Authorization: " + token.substring(7));
		System.out.println("Email Id: "+emailId);
		logger.info("Authorization: " + token.substring(7));
		String userid="";
		PersonalDetailsBean personDetailsBean;
		try {
			personDetailsBean = registerUser.getPersonalDetails(userId);
			if(userId.toString().substring(0, 2).equalsIgnoreCase("CU") || userId.toString().substring(0, 2).equalsIgnoreCase("BC") || userId.toString().substring(0, 2).equalsIgnoreCase("RE"))
			{
				if(emailId.equalsIgnoreCase("email"))
				{
					System.out.println("EmailID not present");
					userid=userId+"-"+personDetailsBean.getEmailAddress();
				}
				else
				{
					userid=userId+"-"+emailId;
				}
			}
			else
				userid=userId;
			boolean validToken = tokenService.validateToken(userid, token.substring(7));

			if (validToken) {
				try {
					//PersonalDetailsBean personDetailsBean = registerUser.getPersonalDetails(userId);

					response.setData(personDetailsBean);
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} catch (Exception exception) {
					response.setStatus("Failure");
					response.setErrCode("ASA012");
					logger.error(exception.getMessage());

					if (exception.getMessage().contains("Unable to find"))
						response.setErrMessage("Records not Found");
					else
						response.setErrMessage(ErrorDescription.getDescription("ASA012"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setErrMessage("Unauthorized Access.");
				return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}


	/*---------------------- Method For update Personal Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/updatePersonalDetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> updatePersonalDetails(@RequestBody PersonalDetailsBean pdb,
			HttpServletRequest request) {
		GenericResponse<Object> response = new GenericResponse<Object>();

		String token = request.getHeader("Authorization");
		String userid="";
		System.out.println("Authorization: " + token.substring(7));
		PersonalDetailsBean personDetailsBean;
		try {
			personDetailsBean = registerUser.getPersonalDetails(pdb.getUserId());
			if(pdb.getUserId().toString().substring(0, 2).equalsIgnoreCase("CU") || pdb.getUserId().toString().substring(0, 2).equalsIgnoreCase("BC") || pdb.getUserId().toString().substring(0, 2).equalsIgnoreCase("RE"))
					userid=pdb.getUserId()+"-"+personDetailsBean.getEmailAddress();
			else
				userid=pdb.getUserId();
			boolean validToken = tokenService.validateToken(userid, token.substring(7));

			if (validToken) {
				try {
					PersonalDetailsBean personDetailsBeanNew = registerUser.updatePersonalDetails(pdb);
					response.setData(personDetailsBeanNew);
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} catch (Exception exception) {
					response.setStatus("Failure");
					response.setErrCode("ASA013");
					response.setErrMessage(ErrorDescription.getDescription("ASA013"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setErrMessage("Unauthorized Access.");
				return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Method For Update Business Detail -----------------------*/
	@CrossOrigin("*")
	@PostMapping(value = "/updateBusinessDetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateBusinessDetail(@RequestBody BusinessDetailsBean businessDetailsBean,
			String userid) {
		System.out.println(businessDetailsBean);
		GenericResponse<Object> response = new GenericResponse<Object>();

		logger.info(" ============== UserDetail : Business Details ============== ");
		System.out.println(businessDetailsBean);

		if (businessDetailsBean.getUserId().toString().substring(0, 2).equalsIgnoreCase("RE")) {
			response.setErrCode("ASA005");
			response.setErrMessage(ErrorDescription.getDescription("ASA005"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		String errorString = this.vud.validateBusinessDetails(businessDetailsBean);

		logger.info("\n ======== UserDetail : Business Details Validation Status ========== " + errorString);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				// Change the Country name while updating business details
				/*
				 * String reqcountry=businessDetailsBean.getRegisteredCountry();
				 * System.out.println(reqcountry);
				 * 
				 * String originalCountry =
				 * registerUser.getCountry(businessDetailsBean.getUserId());
				 * System.out.println("originalcountry:"+originalCountry);
				 * //response.setData(country); // String
				 * dbcountry=country.get(0).getRegistredCountry();
				 * 
				 * //System.out.println(dbcountry);
				 * if(!originalCountry.equalsIgnoreCase(reqcountry)) { NimaiCustomer nm=new
				 * NimaiCustomer(); nm.setAccountStatus("Inactive");
				 * response.setErrCode("ASA008");
				 * response.setErrMessage(ErrorDescription.getDescription("ASA008")); return new
				 * ResponseEntity<Object>(response, HttpStatus.OK);
				 * 
				 * }
				 */
				try {
					boolean status = registerUser.saveBusinessDetails(businessDetailsBean.getUserId(),
							businessDetailsBean);
				} catch (Exception e) {
					e.printStackTrace();
					response.setErrCode("ASA011");
					response.setErrMessage(ErrorDescription.getDescription("ASA011"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}

				response.setErrCode("ASA009");
				response.setErrMessage(ErrorDescription.getDescription("ASA009"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setErrCode("ASA011");
				response.setErrMessage(ErrorDescription.getDescription("ASA011"));
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		} else {
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + errorString.toString());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Method For View Business Detail -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewBusinessDetails/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> viewBusinessDetails(@PathVariable String userId, HttpServletRequest request) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		String token = request.getHeader("Authorization");
		System.out.println("Authorization: " + token.substring(7));
		String userid="";
		
		try {
			PersonalDetailsBean personDetailsBean=registerUser.getPersonalDetails(userId);
			if(userId.toString().substring(0, 2).equalsIgnoreCase("CU") || userId.toString().substring(0, 2).equalsIgnoreCase("BC") || userId.toString().substring(0, 2).equalsIgnoreCase("RE"))
				userid=userId+"-"+personDetailsBean.getEmailAddress();
			else
				userid=userId;
			boolean validToken = tokenService.validateToken(userid, token.substring(7));

			if (validToken) {
				try {

					BusinessDetailsBean businessDetailsBean = registerUser.getBusinessDetails(userId);
					response.setData(businessDetailsBean);
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} catch (Exception e) {
					response.setStatus("Failure");
					response.setErrCode("ASA013");
					response.setErrMessage(ErrorDescription.getDescription("ASA013"));
					return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);

				}
			} else {
				response.setErrMessage("Unauthorized Access.");
				return new ResponseEntity<Object>(response, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception exception) {
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- check Email ID Exists -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/checkEmailID", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> checkEmailID(@RequestBody PersonalDetailsBean personalDetailsBean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			boolean status = registerUser.checkEmailId(personalDetailsBean.getEmailAddress());
			if (status != true) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	/*---------------------- Is Email ID Exists -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/isEmailExists/{emailAddress}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> isEmailExists(@PathVariable String emailAddress) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			boolean status = registerUser.checkEmailId(emailAddress);
			if (status == true) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("ASA012");
			response.setErrMessage(ErrorDescription.getDescription("ASA012") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/*---------------------- Branch User Details -----------------------*/
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/saveBranchUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> generatePassword(@RequestBody BranchUserBean branchUserbean) {
		GenericResponse<Object> response = new GenericResponse<Object>();

		registerUser.saveBranchUser(branchUserbean);

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/viewDetailedCountry")
	public ResponseEntity<Object> viewDetailedCountries() {
		GenericResponse<Object> response = new GenericResponse<Object>();
		logger.info(" ================ Drop Down : Countries with States================");

		try {
		//	List<?> country = countryService.countryData();
			List<NimaiLookupCountries> list = nimaiLookupCountriesRepository.findAll();
			List<CountryResponse> countryResponse = list.stream().map(request -> {
				CountryResponse countryData = new CountryResponse();
				countryData.setCountryId(request.getCountryId());
				countryData.setCountryName(request.getCountryName());
				countryData.setPhoneCode(request.getPhoneCode());
				countryData.setCurrency(request.getCurrency());
				List<StateResponce> stateList = request.getNimaiLookupStatesList().stream().map(stateReq -> {
					StateResponce stateResponse = new StateResponce();
					stateResponse.setStateId(stateReq.getStateId());
					stateResponse.setStateName(stateReq.getStateName());
					return stateResponse;
				}).collect(Collectors.toList());
				countryData.setStateResponce(stateList);
				return countryData;
			}).collect(Collectors.toList());			
			response.setData(countryResponse);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("ASA012");
			logger.error(exception.getMessage());

			if (exception.getMessage().contains("Unable to find"))
				response.setErrMessage("Records not Found");
			else
				response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getSubsidiaryList/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> getSubsidiaryData(@PathVariable String userid) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		// logger.info(" ================ Drop Down : Countries with
		// States================");

		try {
			List<NimaiCustomerBean> subsidary = registerUser.getRegisterUser(userid);
			response.setData(subsidary);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("ASA012");
			logger.error(exception.getMessage());

			if (exception.getMessage().contains("Unable to find"))
				response.setErrMessage("Records not Found");
			else
				response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/getAdditionalUserList/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Object> getAdditionalUserData(@PathVariable String userid) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		// logger.info(" ================ Drop Down : Countries with
		// States================");

		try {
			List<NimaiCustomerBean> additionalUser = registerUser.getAdditionalRegisterUser(userid);
			response.setData(additionalUser);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("ASA012");
			logger.error(exception.getMessage());

			if (exception.getMessage().contains("Unable to find"))
				response.setErrMessage("Records not Found");
			else
				response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/removeOwner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> removeOwnerDetails(@RequestBody OwnerMasterBean ownerbean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		try {
			registerUser.removeOwner(ownerbean);
			response.setStatus("Owner Removed Successfully");
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setErrMessage(ErrorDescription.getDescription("ASA012"));
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

}
