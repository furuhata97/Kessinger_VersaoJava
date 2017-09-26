package com.kessinger.kessinger;

import com.kessinger.kessinger.storage.StorageProperties;
import com.kessinger.kessinger.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class KessingerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KessingerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
    
}
