package com.vitalii.vorobii.runit.service;

import com.vitalii.vorobii.runit.dto.SubmissionExecuteDetails;
import com.vitalii.vorobii.runit.dto.SubmissionRequestDTO;

public interface SubmissionExecutor {
	SubmissionExecuteDetails execute(SubmissionRequestDTO submissionRequest);
}
