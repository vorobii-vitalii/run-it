package com.vitalii.vorobii.runit.service;

import org.springframework.lang.Nullable;

public interface SubmissionExecutorFactory {
	@Nullable
	SubmissionExecutor getExecutor(String language);
}
