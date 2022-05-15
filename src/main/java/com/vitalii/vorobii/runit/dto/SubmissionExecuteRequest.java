package com.vitalii.vorobii.runit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionExecuteRequest {
	private final SubmissionRequestDTO request;
	private final String submissionId;
}
