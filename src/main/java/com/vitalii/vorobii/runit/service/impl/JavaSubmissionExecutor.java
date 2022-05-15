package com.vitalii.vorobii.runit.service.impl;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.vitalii.vorobii.runit.dto.SubmissionExecuteDetails;
import com.vitalii.vorobii.runit.dto.SubmissionRequestDTO;
import com.vitalii.vorobii.runit.service.SubmissionExecutor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JavaSubmissionExecutor implements SubmissionExecutor {
	private static final Integer DEFAULT_MAX_EXECUTION_TIME_IN_SEC = 60;
	private static final Integer DEFAULT_MAX_MEMORY_CONSUME_IN_MB = 150;
	private static final Integer MIN_MEMORY_CONSUME = 5;
	private static final String OPEN_JDK_IMAGE = "openjdk:11";

	private final DockerClient dockerClient;

	@SneakyThrows
	@Override
	public SubmissionExecuteDetails execute(SubmissionRequestDTO submissionRequest) {
		var maxExecutionTimeInSec = submissionRequest.getMaxExecutionTimeInSeconds();
		var maxMemoryConsumeInMegabytes = submissionRequest.getMaxMemoryConsumeInMegabytes();

		if (maxExecutionTimeInSec == null) {
			maxExecutionTimeInSec = DEFAULT_MAX_EXECUTION_TIME_IN_SEC;
		}
		if (maxMemoryConsumeInMegabytes == null) {
			maxMemoryConsumeInMegabytes = DEFAULT_MAX_MEMORY_CONSUME_IN_MB;
		} else {
			maxMemoryConsumeInMegabytes = Math.max(MIN_MEMORY_CONSUME, maxMemoryConsumeInMegabytes);
		}

		String commandLineArguments = Optional.ofNullable(submissionRequest.getCommandLineArguments())
				.map(arguments -> arguments.stream()
						.filter(Objects::nonNull)
						.map(String::trim)
						.collect(Collectors.joining(" ")))
				.orElse("");

		String stdin = Optional.ofNullable(submissionRequest.getInput()).orElse("");

		var createContainerResponse = dockerClient
				.createContainerCmd(OPEN_JDK_IMAGE)
				.withHostConfig(HostConfig.newHostConfig().withAutoRemove(true))
				.withEntrypoint(
						"/bin/bash",
						"-c",
						"touch Solution.java && echo -e '"
								+ submissionRequest.getSourceCode()
								+ "' > Solution.java && "
								+ "javac Solution.java && echo '" + stdin +"' > stdin && "
								+ "timeout " + maxExecutionTimeInSec + "s "
								+ "java -Xmx" + maxMemoryConsumeInMegabytes + "m Solution " + commandLineArguments + " < stdin > res.out;"
								+ "echo $?; "
								+ "cat res.out")
				.exec();

		var startContainerCmd = dockerClient.startContainerCmd(createContainerResponse.getId());
		startContainerCmd.exec();

		var counter = new AtomicBoolean(false);
		var executeDetails =
				SubmissionExecuteDetails.builder()
					.output(new LinkedList<>())
					.build();

		var loggingCallback = new LogContainerResultCallback() {
			@Override
			public void onNext(Frame item) {
				String payloadAsString = new String(item.getPayload()).trim();
				if (!counter.get()) {
					try {
						int statusCode = Integer.parseInt(payloadAsString);
						executeDetails.setStatusCode(statusCode);
						executeDetails.setWasProgramExecuted(true);
						counter.set(true);
					}
					catch (NumberFormatException formatException) {
						executeDetails.setWasProgramExecuted(false);
						executeDetails.getOutput().add(payloadAsString);
						counter.set(true);
					}
				} else {
					executeDetails.getOutput().add(payloadAsString);
				}
			}
		};
		dockerClient.logContainerCmd(startContainerCmd.getContainerId())
				.withStdOut(true)
				.withStdErr(true)
				.withFollowStream(true)
				.withTailAll()
				.exec(loggingCallback)
				.awaitCompletion();

		return executeDetails;
	}

}
