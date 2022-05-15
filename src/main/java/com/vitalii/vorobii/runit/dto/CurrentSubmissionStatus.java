package com.vitalii.vorobii.runit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentSubmissionStatus {
	private StatusType status;
	private SubmissionExecuteDetails details;

	public enum StatusType {
		NOT_YET_STARTED,
		IN_PROCESS,
		FAILED_INTERNAL_ERROR,
		SUCCESS
	}

}
