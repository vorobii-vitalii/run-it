package com.vitalii.vorobii.runit.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitalii.vorobii.runit.dto.CurrentSubmissionStatus;
import com.vitalii.vorobii.runit.dto.SubmissionCreatedDTO;
import com.vitalii.vorobii.runit.dto.SubmissionRequestDTO;
import com.vitalii.vorobii.runit.service.SubmissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {
	private final SubmissionService submissionService;

	@PostMapping
	public SubmissionCreatedDTO createSubmission(@Valid @RequestBody SubmissionRequestDTO submissionRequest) {
		return submissionService.createSubmission(submissionRequest);
	}

	@GetMapping("/{submissionId}")
	public CurrentSubmissionStatus getCurrentStatus(@PathVariable("submissionId") String submissionId) {
		return submissionService.getCurrentSubmissionStatus(submissionId);
	}

}
