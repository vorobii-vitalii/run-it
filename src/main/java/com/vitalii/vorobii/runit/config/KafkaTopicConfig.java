package com.vitalii.vorobii.runit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.vitalii.vorobii.runit.constant.Topics;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic submissionsTopic() {
		return TopicBuilder.name(Topics.SUBMISSIONS).build();
	}

}
