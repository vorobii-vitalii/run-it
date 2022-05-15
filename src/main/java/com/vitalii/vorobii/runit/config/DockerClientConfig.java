package com.vitalii.vorobii.runit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

@Configuration
public class DockerClientConfig {

	@Bean
	public DockerClient dockerClient() {
		return DockerClientBuilder.getInstance().build();
	}

}
