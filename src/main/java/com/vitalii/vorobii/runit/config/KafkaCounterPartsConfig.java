package com.vitalii.vorobii.runit.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaCounterPartsConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	public ProducerFactory<String, Object> submissionsProducerFactory() {
		return new DefaultKafkaProducerFactory<>(kafkaProducerSubmissionsConfig());
	}

	@Bean
	public KafkaTemplate<String, Object> submissionsKafkaTemplate() {
		return new KafkaTemplate<>(submissionsProducerFactory());
	}

	@Bean
	public ConsumerFactory<String, Object> submissionsConsumerFactory() {
		final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(kafkaConsumerSubmissionsConfig(), new StringDeserializer(), jsonDeserializer);
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> submissionsKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(submissionsConsumerFactory());
		return factory;
	}

	private Map<String, Object> kafkaProducerSubmissionsConfig() {
		return Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
		);
	}

	private Map<String, Object> kafkaConsumerSubmissionsConfig() {
		return Map.of(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
				JsonDeserializer.USE_TYPE_INFO_HEADERS, false,
				JsonDeserializer.TRUSTED_PACKAGES, "*"
		);
	}

}
