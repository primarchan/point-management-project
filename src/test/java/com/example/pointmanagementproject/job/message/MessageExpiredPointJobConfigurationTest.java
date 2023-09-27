package com.example.pointmanagementproject.job.message;

import com.example.pointmanagementproject.BatchTestSupport;
import com.example.pointmanagementproject.point.Point;
import com.example.pointmanagementproject.point.message.Message;
import com.example.pointmanagementproject.point.message.MessageRepository;
import com.example.pointmanagementproject.point.wallet.PointWallet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class MessageExpiredPointJobConfigurationTest extends BatchTestSupport {

    @Autowired
    Job messageExpiredPointJob;
    @Autowired
    MessageRepository messageRepository;

    @Test
    void messageExpiredPointJob() throws Exception {
        // given
        // 포인트 지갑 생성
        LocalDate earnDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 9, 5);
        LocalDate notExpireDate = LocalDate.of(2025, 12, 31);
        PointWallet pointWallet1 = pointWalletRepository.save(
                new PointWallet("user1", BigInteger.valueOf(3000))
        );
        PointWallet pointWallet2 = pointWalletRepository.save(
                new PointWallet("user2", BigInteger.ZERO)
        );

        // 오늘 (09-06) 만료 시킬 포인트 적립 내역을 생성 (expiredDate = 어제 09-05)
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));

        // when
        // messageExpiredPointJob 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-09-06")
                .toJobParameters();
        JobExecution jobExecution = launchJob(messageExpiredPointJob, jobParameters);

        // then
        // 아래와 같은 메시지 생성 여부 확인
        // 3000 포인트 만료
        // 2021-09-06 기준 3000 포인트가 만료되었습니다.
        // user1 : 3000 포인트 만료 메시지
        // user2 : 2000 포인트 만료 메시
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Message> messages = messageRepository.findAll();
        then(messages).hasSize(2);
        Message message1 = messages.stream().filter(item -> item.getUserId().equals("user1")).findFirst().orElseGet(null);
        then(message1).isNotNull();
        then(message1.getTitle()).isEqualTo("3000 포인트 만료");  // user1 에게 전달되는 메시지
        then(message1.getContent()).isEqualTo("2021-09-06 기준 3000 포인트가 만료되었습니다.");
        Message message2 = messages.stream().filter(item -> item.getUserId().equals("user2")).findFirst().orElseGet(null);
        then(message2).isNotNull();
        then(message2.getTitle()).isEqualTo("2000 포인트 만료");  // user2 에게 전달되는 메시지
        then(message2.getContent()).isEqualTo("2021-09-06 기준 2000 포인트가 만료되었습니다.");
    }

}