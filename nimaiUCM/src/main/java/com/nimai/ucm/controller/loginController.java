package com.nimai.ucm.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimai.ucm.bean.GenericResponse;
import com.nimai.ucm.bean.ResetPasswordBean;

@RestController
public class loginController {

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping(value = "/resetpassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> resetPassword(@RequestBody ResetPasswordBean resetBean) {

		try {

		} catch (Exception exception) {
			return null;
		}
		return null;

	}

}
