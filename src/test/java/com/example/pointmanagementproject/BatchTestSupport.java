package com.example.pointmanagementproject;

import com.example.pointmanagementproject.point.PointRepository;
import com.example.pointmanagementproject.point.message.MessageRepository;
import com.example.pointmanagementproject.point.reservation.PointReservationRepository;
import com.example.pointmanagementproject.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BatchTestSupport {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PointWalletRepository pointWalletRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PointReservationRepository pointReservationRepository;

    protected JobExecution launchJob(Job job, JobParameters jobParameters)  throws Exception {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(job);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);

        return jobLauncherTestUtils.launchJob(jobParameters);
    }

    @AfterEach
    protected void deleteAll() {
        pointWalletRepository.deleteAll();
        pointRepository.deleteAll();
        messageRepository.deleteAll();
        pointReservationRepository.deleteAll();
    }

}
