package com.nimai.ucm;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@ComponentScan(value = "com.nimai.ucm.*")
@EntityScan(basePackageClasses = { NimaiUcmApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class NimaiUcmApplication extends SpringBootServletInitializer {

	private static Logger logger = LogManager.getLogger(NimaiUcmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NimaiUcmApplication.class, args);
		System.out.println(" =========== NIMAI UCM  =========== ");
		logger.info(" =========== NIMAI UCM  =========== ");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(NimaiUcmApplication.class);

	}

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
