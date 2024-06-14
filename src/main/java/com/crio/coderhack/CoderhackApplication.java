package com.crio.coderhack;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoderhackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoderhackApplication.class, args);
        System.out.println("Server running at port: 8080");
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
