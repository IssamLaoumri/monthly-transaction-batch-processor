package com.laoumri.bankspringbatch.batch;

import com.laoumri.bankspringbatch.dto.RTransaction;
import com.laoumri.bankspringbatch.utils.TransactionFieldSetMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TransactionItemReader extends FlatFileItemReader<RTransaction> {

    public TransactionItemReader() {
        this.setName("transactionItemReader");
        this.setResource(resource());
        this.setLineMapper(lineMapper());
        this.setLinesToSkip(1);
    }

    private DefaultLineMapper<RTransaction> lineMapper() {
        DefaultLineMapper<RTransaction> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("idTransaction", "idCompte", "montant", "type", "strDateTransaction");

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new TransactionFieldSetMapper());

        return lineMapper;
    }

    @PostConstruct
    public void validateConfiguration() {
        if (resource() == null) {
            throw new IllegalStateException("Resource file must be set!");
        }
        if (lineMapper() == null) {
            throw new IllegalStateException("LineMapper must be properly configured!");
        }
    }

//    public ItemReader<RTransaction> reader() {
//        FlatFileItemReader<RTransaction> reader = new FlatFileItemReader<>();
//        reader.setName("CSV-READER");
//        reader.setResource(inputFileResource());
//        reader.setLinesToSkip(1);
//        reader.setLineMapper(lineMapper());
//        return reader;
//    }

    public Resource resource() {
        return new FileSystemResource("src\\main\\resources\\transactions.csv");
    }

//    @Bean
//    public LineMapper<RTransaction> lineMapper() {
//        DefaultLineMapper<RTransaction> defaultLineMapper = new DefaultLineMapper<>();
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames("idTransaction","idCompte","montant","type","strDateTransaction");
//        defaultLineMapper.setLineTokenizer(lineTokenizer);
//        BeanWrapperFieldSetMapper<RTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(RTransaction.class);
//        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
//        return defaultLineMapper;
//    }
}
