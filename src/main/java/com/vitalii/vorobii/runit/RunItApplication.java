package com.vitalii.vorobii.runit;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import com.vitalii.vorobii.runit.config.KafkaTopicConfig;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RunItApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunItApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
//		return args -> {
//			IntStream.range(0, 100)
//					.forEach(i -> kafkaTemplate.send(KafkaTopicConfig.EXAMPLE_TOPIC, "hey to Kafka " + i));
//		};
//	}

}
