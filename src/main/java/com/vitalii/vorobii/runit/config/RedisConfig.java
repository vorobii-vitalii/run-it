package com.vitalii.vorobii.runit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;

@Configuration
public class RedisConfig {

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private Integer redisPort;

	@Value("${redis.password}")
	private String redisPassword;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
		configuration.setPassword(redisPassword);
		return new LettuceConnectionFactory(configuration);
	}

	@Bean
	public RedisTemplate<String, CurrentSubmissionStatus> submissionsRedisTemplate() {
		RedisTemplate<String, CurrentSubmissionStatus> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(CurrentSubmissionStatus.class));
		return redisTemplate;
	}

}
