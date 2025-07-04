package com.field2fork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Field2Fork {

	public static void main(String[] args) {
		SpringApplication.run(Field2Fork.class, args);
	}
		@Bean // equivalent to <bean id ..../> in xml file
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) .setPropertyCondition(Conditions.isNotNull());														
		return modelMapper;
	}
}
