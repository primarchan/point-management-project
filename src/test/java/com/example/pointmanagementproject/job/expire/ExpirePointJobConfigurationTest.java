package com.example.pointmanagementproject.job.expire;

import com.example.pointmanagementproject.BatchTestSupport;
import com.example.pointmanagementproject.point.Point;
import com.example.pointmanagementproject.point.PointRepository;
import com.example.pointmanagementproject.point.wallet.PointWallet;
import com.example.pointmanagementproject.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class ExpirePointJobConfigurationTest extends BatchTestSupport {

    @Autowired
    Job expirePointJob;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    PointWalletRepository pointWalletRepository;

    @Test
    void expirePointJob() throws Exception {
        // given
        LocalDate earnDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 1, 3);
        PointWallet pointWallet = pointWalletRepository.save(
                new PointWallet("user123", BigInteger.valueOf(6000))
        );
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate));

        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-04")
                .toJobParameters();
        JobExecution execution = launchJob(expirePointJob, jobParameters);

        // then
        List<Point> points = pointRepository.findAll();
        then(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        then(points.stream().filter(Point::isExpired)).hasSize(3);
        PointWallet changedPointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
        then(changedPointWallet).isNotNull();
        then(changedPointWallet.getAmount()).isEqualByComparingTo(BigInteger.valueOf(3000));
    }

}