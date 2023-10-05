package ppa.lab.securityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ppa.lab.securityservice.configuration.RsaKeyConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyConfiguration.class)
public class SecurityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityServiceApplication.class, args);
	}

}
