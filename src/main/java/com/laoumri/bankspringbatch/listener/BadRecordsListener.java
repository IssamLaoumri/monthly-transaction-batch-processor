package com.laoumri.bankspringbatch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.FlatFileParseException;

@Slf4j
public class BadRecordsListener implements ItemReadListener {
    @Override
    public void onReadError(Exception e){
        if (e instanceof FlatFileParseException parseException){
            String builder = "An error has occurred when reading "
                    + parseException.getLineNumber()
                    + " the line. Here are its details about the bad input\n "
                    + parseException.getInput()
                    + "\n";

            log.error(builder);
        } else {
            log.error("An error occur ", e);
        }
    }
}
