package com.vitalii.vorobii.runit.dao;

import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;

public interface SubmissionStatusDAO {
	void saveSubmission(String submissionId, CurrentSubmissionStatus currentSubmissionStatus);
	CurrentSubmissionStatus getBySubmissionId(String submissionId);
}
