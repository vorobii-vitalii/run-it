package com.vitalii.vorobii.runit.service;

import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;
import com.vitalii.vorobii.runit.dto.SubmissionCreatedDTO;
import com.vitalii.vorobii.runit.dto.SubmissionRequestDTO;

public interface SubmissionService {
	SubmissionCreatedDTO createSubmission(SubmissionRequestDTO submissionRequestDTO);

	CurrentSubmissionStatus getCurrentSubmissionStatus(String submissionId);
}
