package com.vitalii.vorobii.runit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionExecutionResult {
	private boolean success;
	private SubmissionExecuteDetails details;
}
