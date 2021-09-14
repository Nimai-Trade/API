package com.nimai.ucm.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.nimai.ucm.bean.FieoMember;
import com.nimai.ucm.bean.GenericResBean;
import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.NimaiCustomerBean;
import com.nimai.ucm.bean.NimaiCustomerReferrerBean;
import com.nimai.ucm.bean.NimaiSpecCustomerReferrerBean;
import com.nimai.ucm.bean.ReferBean;
import com.nimai.ucm.bean.ReferIdBean;
import com.nimai.ucm.bean.ReferrerBean;
import com.nimai.ucm.bean.SubsidiaryListResponse;
import com.nimai.ucm.entity.Refer;
import com.nimai.ucm.repository.ReferRepository;
import com.nimai.ucm.service.ReferService;
import com.nimai.ucm.utility.AppConstants;
import com.nimai.ucm.utility.ErrorDescription;
import com.nimai.ucm.utility.ReferenceIdUniqueNumber;

@RestController
public class ReferController {

	// Changes From Sravan
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferController.class);

	@Autowired
	ReferRepository referRepo;

	@Autowired
	ReferService referservice;

	@Value("${referrer.fieo}")
	private String fieoRefId;
	
	@Autowired
	GenericResponse<Object> response;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/saverefer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveRefer(@RequestBody ReferBean referbean) {
		// Changes From Sravan
		LOGGER.info("Save Refer Method is  invoked");
		LOGGER.info(" BranchUserId " + referbean.getBranchUserId() + " CompanyName " + referbean.getCompanyName()
				+ " CountryName " + referbean.getCountryName() + " EmailAddress " + referbean.getEmailAddress()
				+ " FirstName " + referbean.getFirstName() + " InsertedBy " + referbean.getInsertedBy() + " LastName "
				+ referbean.getLastName() + " MobileNo " + referbean.getMobileNo() + " ModifiedBy "
				+ referbean.getModifiedBy() + " ReferbeanId " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " UserId " + referbean.getUserId());
		GenericResponse<Object> response = new GenericResponse<Object>();
		Refer refer = new Refer();

		String errorString = "Success";// this.rv.validateReferDetail(referbean);

		if (errorString.equalsIgnoreCase("success")) {
			try {
				ReferenceIdUniqueNumber refernceId = new ReferenceIdUniqueNumber();
				String rid = refernceId.uniqueNumberReferenceId();
				refer.setReferenceId(rid);
				ReferIdBean responseId = referservice.saveReferService(referbean, rid);

				response.setData(responseId);

				response.setStatus("Success");

				response.setErrCode("ASA006");
				response.setErrMessage(ErrorDescription.getDescription("ASA006"));
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatus("Failure");
				response.setErrCode("EXE000");
				response.setErrMessage(ErrorDescription.getDescription("EXE000") + e.getMessage());
				return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
			}
		}

		else {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(errorString);
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/getReferListByUserId/{userId}")
	public ResponseEntity<Object> getReferByUserId(@PathVariable String userId) {
		// Changes From Sravan
		String navin;
		GenericResBean<Object> response = new GenericResBean<Object>();
		LOGGER.info("ReferByUserId method is invoked");
		LOGGER.info("User Id " + userId);
		String checkUserId = referservice.checkKycApprovalStatus(userId);
		if (checkUserId.equalsIgnoreCase(AppConstants.STATUS)) {
			try {
				ReferrerBean refer = referservice.getReferByUserId(userId);
				if (refer == null) {
					response.setErrMessage("Refer list is not available");
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				} else {
					response.setTotalEarning(refer.getTotalEarning());
					response.setList(refer.getRfb());
					response.setStatus("Success");
					response.setErrCode("ASA006");
					response.setErrMessage(ErrorDescription.getDescription("ASA006"));
					return new ResponseEntity<Object>(response, HttpStatus.OK);
				}
			} catch (Exception e) {
				response.setErrMessage("Error while fetching referList");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}

		}
		response.setErrMessage("Error while fetching referList");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/viewRefer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Refer> viewRefer(@RequestBody ReferBean referbean) {
		// Changes From Sravan
		LOGGER.info("viewRefer method is invoked");
		LOGGER.info(" BranchUserId " + referbean.getBranchUserId() + " CompanyName " + referbean.getCompanyName()
				+ " CountryName " + referbean.getCountryName() + " EmailAddress " + referbean.getEmailAddress()
				+ " FirstName " + referbean.getFirstName() + " InsertedBy " + referbean.getInsertedBy() + " LastName "
				+ referbean.getLastName() + " MobileNo " + referbean.getMobileNo() + " ModifiedBy "
				+ referbean.getModifiedBy() + " ReferbeanId " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " UserId " + referbean.getUserId());
		return referservice.viewReferB(referbean);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/checkEmailID", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Object> checkEmailID(@RequestBody ReferBean referbean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		// Changes From Sravan
		LOGGER.info("CheckEmailID method is invoked");
		LOGGER.info(" BranchUserId " + referbean.getBranchUserId() + " CompanyName " + referbean.getCompanyName()
				+ " CountryName " + referbean.getCountryName() + " EmailAddress " + referbean.getEmailAddress()
				+ " FirstName " + referbean.getFirstName() + " InsertedBy " + referbean.getInsertedBy() + " LastName "
				+ referbean.getLastName() + " MobileNo " + referbean.getMobileNo() + " ModifiedBy "
				+ referbean.getModifiedBy() + " ReferbeanId " + referbean.getReferenceId() + " Status "
				+ referbean.getStatus() + " UserId " + referbean.getUserId());
		try {
			boolean status = referservice.checkEmailId(referbean.getEmailAddress());
			if (status != true) {
				response.setStatus("Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				response.setStatus("Failure");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception exception) {
			response.setStatus("Failure");
			response.setErrCode("EXE000");
			response.setErrMessage(ErrorDescription.getDescription("EXE000") + exception.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@RequestMapping(value = "/updateRefer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Object> updateRefer(@RequestBody ReferBean referbean) {
		GenericResponse<Object> response = new GenericResponse<Object>();
		referservice.updateRefer(referbean);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/getRegisterUsers/{userId}/{emailId}")
	public ResponseEntity<Object> getRegisterUser(@PathVariable String userId,@PathVariable String emailId) {

		List<NimaiCustomerReferrerBean> registeruser;
		// Changes From Dhiraj
		if(userId.equalsIgnoreCase(fieoRefId))
		{
			String obtainedEmailId=referservice.getEmailIdByFieoReferId(userId);
			registeruser = referservice.getRegisterUserByReferrerUser(obtainedEmailId);
		}
		else
		{
			registeruser = referservice.getRegisterUserByReferrerUser(emailId);
		}
		if (registeruser.size() == 0) {
			response.setErrMessage("Referrer list not available");
		}
		response.setList(registeruser);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/getRegisterUserByUserId/{userid}")
	public List<NimaiSpecCustomerReferrerBean> getRegisterUserByUserId(@PathVariable String userid) {
		// Changes From Sravan
		LOGGER.info("ReferByUserId method is invoked");
		LOGGER.info("User Id " + userid);
		List<NimaiSpecCustomerReferrerBean> registeruser = referservice.getSpecRegisterUserByUserId(userid);

		return registeruser;
	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/referrerChannel/{userId}")
	public ResponseEntity<Object> getReferrerChannel(@PathVariable String userId) {

		//String fieoReferrerId = referservice.getReferrerUser(userId);
		if (userId.equalsIgnoreCase(fieoRefId)) 
			response.setData("FIEO");
		else
			response.setData("");
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping(value = "/getReferrerLeads")
	public ResponseEntity<Object> getReferrerLeads() {

		List<FieoMember> fieoReferrer = referservice.getReferrerFieoLeads();
		response.setData(fieoReferrer);
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
}
