package com.vitalii.vorobii.runit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.dockerjava.api.exception.DockerException;
import com.vitalii.vorobii.runit.dto.SubmissionExecuteDetails;
import com.vitalii.vorobii.runit.dto.SubmissionExecuteRequest;
import com.vitalii.vorobii.runit.dto.SubmissionExecutionResult;
import com.vitalii.vorobii.runit.service.SubmissionExecutionService;
import com.vitalii.vorobii.runit.service.SubmissionExecutorFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionExecutionServiceImpl implements SubmissionExecutionService {
	private final SubmissionExecutorFactory submissionExecutorFactory;

	@Override
	public SubmissionExecutionResult runSubmission(SubmissionExecuteRequest executionRequest) {
		var request = executionRequest.getRequest();
		var language = request.getLanguage();

		var submissionExecutor = submissionExecutorFactory.getExecutor(language);

		if (submissionExecutor == null) {
			throw new IllegalStateException("Cannot handle language: " + language);
		}
		try {
			SubmissionExecuteDetails details = submissionExecutor.execute(request);
			return SubmissionExecutionResult.builder()
					.success(true)
					.details(details)
					.build();
		}
		catch (DockerException dockerException) {
			log.warn("Error when running submittion ", dockerException);
			return SubmissionExecutionResult.builder()
					.success(false)
					.build();
		}
	}

}
