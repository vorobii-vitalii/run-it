package com.vitalii.vorobii.runit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubmissionCreatedDTO {
	private String submissionId;
}
