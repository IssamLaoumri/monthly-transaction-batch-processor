package com.laoumri.bankspringbatch.batch;

import com.laoumri.bankspringbatch.dto.RTransaction;
import com.laoumri.bankspringbatch.utils.TransactionFieldSetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.boot.autoconfigure.batch.JobExecutionExitCodeGenerator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionItemReader extends FlatFileItemReader<RTransaction> {

    private final JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator;

    public TransactionItemReader(JobExecutionExitCodeGenerator jobExecutionExitCodeGenerator) {
        this.setName("transactionItemReader");
        this.setResource(resource());
        this.setLineMapper(lineMapper());
        this.setLinesToSkip(1);
        this.setStrict(false);
        this.jobExecutionExitCodeGenerator = jobExecutionExitCodeGenerator;
    }

    private DefaultLineMapper<RTransaction> lineMapper() {
        DefaultLineMapper<RTransaction> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("idTransaction", "idCompte", "montant", "type", "strDateTransaction");

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new TransactionFieldSetMapper());

        return lineMapper;
    }

    public Resource resource() {
        return new FileSystemResource("src\\main\\resources\\transactions.csv");
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        Resource resource = resource();
        if (!resource.exists()) {
            log.error("File not found: {}", resource.getFilename());
            stepExecution.setTerminateOnly();
        }
    }
}
