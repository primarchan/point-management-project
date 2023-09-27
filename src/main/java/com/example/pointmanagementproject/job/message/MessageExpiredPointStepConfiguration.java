package com.example.pointmanagementproject.job.message;

import com.example.pointmanagementproject.job.listener.InputExpiredPointAlarmCriteriaDateStepListener;
import com.example.pointmanagementproject.point.ExpiredPointSummary;
import com.example.pointmanagementproject.point.PointRepository;
import com.example.pointmanagementproject.point.message.Message;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class MessageExpiredPointStepConfiguration {

    @Bean
    @JobScope
    public Step messageExpiredPointStep(
            StepBuilderFactory stepBuilderFactory,
            PlatformTransactionManager platformTransactionManager,
            InputExpiredPointAlarmCriteriaDateStepListener listener,
            RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader,
            ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor,
            JpaItemWriter<Message> messageExpiredPointItemWriter
    ) {
        return stepBuilderFactory
                .get("messageExpiredPointStep")
                .allowStartIfComplete(true)
                .transactionManager(platformTransactionManager)
                .listener(listener)
                .<ExpiredPointSummary, Message>chunk(1000)
                .reader(messageExpiredPointItemReader)
                .processor(messageExpiredPointItemProcessor)
                .writer(messageExpiredPointItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ExpiredPointSummary> messageExpiredPointItemReader(
            PointRepository pointRepository,
            @Value("#{T(java.time.LocalDate).parse(stepExecutionContext[alarmCriteriaDate])}")
            LocalDate alarmCriteriaDate
    ){
        return new RepositoryItemReaderBuilder<ExpiredPointSummary>()
                .name("messageExpiredPointItemReader")
                .repository(pointRepository)
                .methodName("sumByExpiredDate")
                .pageSize(1000)
                .arguments(alarmCriteriaDate)
                .sorts(Map.of("pointWallet", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ExpiredPointSummary, Message> messageExpiredPointItemProcessor(
            @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}")
            LocalDate today
    ) {
        return summary -> Message.expiredPointMessageInstance(
                summary.getUserId(), today, summary.getAmount()
        );
    }

    @Bean
    @StepScope
    public JpaItemWriter<Message> messageExpiredPointItemWriter(
        EntityManagerFactory entityManagerFactory
    ) {
        JpaItemWriter<Message> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

        return jpaItemWriter;
    }


}
