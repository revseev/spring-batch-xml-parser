package com.revseev.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootBatchXmlToDbApplicationTests {

	@Autowired
	JobLauncher jobLauncher;
	@Autowired
	Job job;

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldRunJob() throws
												  JobParametersInvalidException,
												  JobExecutionAlreadyRunningException,
												  JobRestartException,
												  JobInstanceAlreadyCompleteException {
		jobLauncher.run(job, new JobParameters());
	}
}
