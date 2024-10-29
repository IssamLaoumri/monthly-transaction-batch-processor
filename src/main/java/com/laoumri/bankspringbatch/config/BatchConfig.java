package com.laoumri.bankspringbatch.config;

import com.laoumri.bankspringbatch.dto.RTransaction;
import com.laoumri.bankspringbatch.entity.Transaction;
import com.laoumri.bankspringbatch.listener.BadRecordsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final ItemWriter<Transaction> transactionItemWriter;
    private final ItemProcessor<RTransaction,Transaction> transactionItemProcessor;
    private final FlatFileItemReader<RTransaction> transactionItemReader;


    @Bean
    public Step transactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws ItemStreamException {
        return new StepBuilder("step-data-load", jobRepository)
                .<RTransaction, Transaction>chunk(10, transactionManager)
                .reader(transactionItemReader)
                .processor(transactionItemProcessor)
                .writer(transactionItemWriter)
                .allowStartIfComplete(true)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(3)
                .listener(new BadRecordsListener())
                .build();
    }

    @Bean
    public Job transactionJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-data-load", jobRepository)
                .start(transactionStep(jobRepository, transactionManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }


}
