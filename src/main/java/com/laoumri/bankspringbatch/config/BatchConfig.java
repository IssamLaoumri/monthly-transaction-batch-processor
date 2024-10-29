package com.laoumri.bankspringbatch.config;

import com.laoumri.bankspringbatch.dto.RTransaction;
import com.laoumri.bankspringbatch.entity.Transaction;
import com.laoumri.bankspringbatch.exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {
    private final ItemWriter<Transaction> transactionItemWriter;
    private final ItemProcessor<RTransaction,Transaction> transactionItemProcessor;
    private final FlatFileItemReader<RTransaction> transactionItemReader;


    @Bean
    public Step transactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("step-data-load", jobRepository)
                .<RTransaction, Transaction>chunk(10, transactionManager)
                .reader(transactionItemReader)
                .processor(transactionItemProcessor)
                .writer(transactionItemWriter)
                .faultTolerant()
                .retryLimit(5)
                .retry(ResourceNotFound.class)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job transactionJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("job-data-load", jobRepository)
                .start(transactionStep(jobRepository, transactionManager))
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
            @SneakyThrows
            @Override
            public void beforeJob(JobExecution jobExecution) {
                Resource resource = inputFileResource();
                if (!resource.exists()) {
                    jobExecution.setStatus(BatchStatus.STOPPED);
                    log.error("Input fichier est introuvable verifier le path suivant : {}", resource.getURL());
                    throw new ResourceNotFound("Input resource must exist");
                }
            }
        };
    }

    private Resource inputFileResource() {
        return new FileSystemResource("src\\main\\resources\\transactions.csv");
    }
}
