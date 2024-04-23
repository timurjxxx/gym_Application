package com.gypApp_eurekaService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GypAppEurekaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GypAppEurekaServiceApplication.class, args);
	}

}
