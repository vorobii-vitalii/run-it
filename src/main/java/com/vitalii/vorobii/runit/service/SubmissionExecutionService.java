package com.vitalii.vorobii.runit.service;

import com.vitalii.vorobii.runit.dto.SubmissionExecuteRequest;
import com.vitalii.vorobii.runit.dto.SubmissionExecutionResult;

public interface SubmissionExecutionService {
	SubmissionExecutionResult runSubmission(SubmissionExecuteRequest executionRequest);
}
