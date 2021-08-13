package com.nimai.uam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;



@ComponentScan(value = "com.nimai.uam.*")
@EntityScan(basePackageClasses = { NimaiUamApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class NimaiUamApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimaiUamApplication.class, args);
		
		
		System.out.println(" =========== NIMAI UAM =========== ");
	}

}
