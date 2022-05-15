package com.vitalii.vorobii.runit.dao.impl;

import java.util.function.Function;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.vitalii.vorobii.runit.dao.SubmissionStatusDAO;
import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisSubmissionStatusDAO implements SubmissionStatusDAO {
	private static final String SUBMISSIONS_PREFIX = "submissions.";
	private static final Function<String, String> SUBMISSION_KEY_FORMATTER = submissionId -> SUBMISSIONS_PREFIX + submissionId;

	private final RedisTemplate<String, CurrentSubmissionStatus> redisTemplate;

	@Override
	public void saveSubmission(String submissionId, CurrentSubmissionStatus currentSubmissionStatus) {
		if (submissionId == null) {
			throw new IllegalArgumentException("submissionId == null");
		}
		var submissionRedisKey = SUBMISSION_KEY_FORMATTER.apply(submissionId);

		redisTemplate.opsForValue().set(submissionRedisKey, currentSubmissionStatus);
	}

	@Override
	public CurrentSubmissionStatus getBySubmissionId(String submissionId) {
		if (submissionId == null) {
			throw new IllegalArgumentException("submissionId == null");
		}
		var submissionRedisKey = SUBMISSION_KEY_FORMATTER.apply(submissionId);

		return redisTemplate.opsForValue().get(submissionRedisKey);
	}

}
