package com.vitalii.vorobii.runit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalii.vorobii.runit.constant.Topics;
import com.vitalii.vorobii.runit.dao.SubmissionStatusDAO;
import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;
import com.vitalii.vorobii.runit.dto.SubmissionExecuteRequest;
import com.vitalii.vorobii.runit.service.SubmissionExecutionService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaListeners {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SubmissionExecutionService service;

	@Autowired
	private SubmissionStatusDAO submissionStatusDAO;

	@SneakyThrows
	@KafkaListener(topics = Topics.SUBMISSIONS, groupId = "999")
	public void listenForSubmissions(@Payload String payload) {
		var submissionExecuteRequest = objectMapper.readValue(payload, SubmissionExecuteRequest.class);

		log.info("Submission request to process: {}", submissionExecuteRequest);

		var submissionId = submissionExecuteRequest.getSubmissionId();

		submissionStatusDAO.saveSubmission(submissionId,
				CurrentSubmissionStatus.builder()
						.status(CurrentSubmissionStatus.StatusType.IN_PROCESS)
						.build());

		var executionResult = service.runSubmission(submissionExecuteRequest);

		if (!executionResult.isSuccess()) {
			log.warn("Execution of submission {} has completed with failure", submissionId);
			submissionStatusDAO.saveSubmission(submissionId,
					CurrentSubmissionStatus.builder()
							.status(CurrentSubmissionStatus.StatusType.FAILED_INTERNAL_ERROR)
							.build());
		} else {
			log.info("Execution of submission {} has completed with success, details: {}",
					submissionId, executionResult.getDetails());
			submissionStatusDAO.saveSubmission(submissionId,
					CurrentSubmissionStatus.builder()
							.status(CurrentSubmissionStatus.StatusType.SUCCESS)
							.details(executionResult.getDetails())
							.build());
		}
	}


}
