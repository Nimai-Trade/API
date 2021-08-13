package com.nimai.uam.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.nimai.uam.bean.ResetPasswordBean;

@Component
public class MailService {

	private JavaMailSender javaMailSender;
	
	@Autowired
	public MailService() {
		this.javaMailSender=javaMailSender;
	}
	
	public void sendEmail(String Link,ResetPasswordBean resetPasswordBean) throws MailException{
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(resetPasswordBean.getEmailId());
		mail.setSubject("Testing Mail API");
		mail.setText("Hurray ! You have done that dude..."+Link);
		javaMailSender.send(mail);
	}
	
	
}
