package com.vitalii.vorobii.runit.service.impl;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.vitalii.vorobii.runit.constant.Topics;
import com.vitalii.vorobii.runit.dao.SubmissionStatusDAO;
import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;
import com.vitalii.vorobii.runit.dto.SubmissionCreatedDTO;
import com.vitalii.vorobii.runit.dto.SubmissionExecuteRequest;
import com.vitalii.vorobii.runit.dto.SubmissionRequestDTO;
import com.vitalii.vorobii.runit.service.SubmissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
	private final SubmissionStatusDAO submissionStatusDAO;
	private final KafkaTemplate<String, Object> submissionsKafkaTemplate;

	@Override
	public SubmissionCreatedDTO createSubmission(SubmissionRequestDTO submissionRequestDTO) {
		var submissionId = UUID.randomUUID().toString();

		submissionStatusDAO.saveSubmission(submissionId, notYetStartedSubmissionStatus());
		submissionsKafkaTemplate.send(Topics.SUBMISSIONS, new SubmissionExecuteRequest(submissionRequestDTO, submissionId));

		return new SubmissionCreatedDTO(submissionId);
	}

	@Override
	public CurrentSubmissionStatus getCurrentSubmissionStatus(String submissionId) {
		return submissionStatusDAO.getBySubmissionId(submissionId);
	}

	private CurrentSubmissionStatus notYetStartedSubmissionStatus() {
		return CurrentSubmissionStatus.builder()
				.status(CurrentSubmissionStatus.StatusType.NOT_YET_STARTED)
				.build();
	}

}
