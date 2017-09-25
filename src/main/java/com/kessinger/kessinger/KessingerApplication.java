package com.kessinger.kessinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class KessingerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KessingerApplication.class, args);
	}
    
}
