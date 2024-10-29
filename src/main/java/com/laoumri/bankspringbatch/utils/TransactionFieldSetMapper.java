package com.laoumri.bankspringbatch.utils;

import com.laoumri.bankspringbatch.dto.RTransaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class TransactionFieldSetMapper implements FieldSetMapper<RTransaction> {
    @Override
    public RTransaction mapFieldSet(FieldSet fs) {
        return RTransaction.builder()
                .idTransaction(fs.readInt("idTransaction"))
                .idCompte(fs.readInt("idCompte"))
                .montant(fs.readDouble("montant"))
                .type(fs.readString("type"))
                .strDateTransaction(fs.readString("strDateTransaction"))
                .build();
    }
}
