package com.example.pointmanagementproject.job.expire;

import com.example.pointmanagementproject.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {

    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator todayJobParameterValidator,
            Step expirePointStep
    ) {
        return jobBuilderFactory.get("expirePointJob")
                .validator(todayJobParameterValidator)
                .incrementer(new RunIdIncrementer())  // run.id 가 계속해서 증가해서 job paramter 가 중복되지 않게 해줌.
                .start(expirePointStep)
                .build();
    }

}
