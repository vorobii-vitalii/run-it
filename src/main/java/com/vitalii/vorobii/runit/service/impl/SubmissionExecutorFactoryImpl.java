package com.vitalii.vorobii.runit.service.impl;

import org.springframework.stereotype.Component;

import com.github.dockerjava.api.DockerClient;
import com.vitalii.vorobii.runit.constant.Languages;
import com.vitalii.vorobii.runit.service.SubmissionExecutor;
import com.vitalii.vorobii.runit.service.SubmissionExecutorFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionExecutorFactoryImpl implements SubmissionExecutorFactory {
	private final DockerClient dockerClient;

	@Override
	public SubmissionExecutor getExecutor(String language) {
		if (language == null) {
			throw new IllegalArgumentException("language == null");
		}
		switch (language) {
			case Languages.JAVA:
				return new JavaSubmissionExecutor(dockerClient);
			case Languages.C:
				return new CSubmissionExecutor(dockerClient);
			default:
				return null;
		}
	}

}
