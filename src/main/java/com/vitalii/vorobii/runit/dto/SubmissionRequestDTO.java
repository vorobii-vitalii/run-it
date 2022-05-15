package com.vitalii.vorobii.runit.dto;

import java.util.List;

import lombok.Data;

@Data
public class SubmissionRequestDTO {
	private String language;
	private String sourceCode;
	private Integer maxExecutionTimeInSeconds;
	private Integer maxMemoryConsumeInMegabytes;
	private List<String> commandLineArguments;
	private String input;
}
