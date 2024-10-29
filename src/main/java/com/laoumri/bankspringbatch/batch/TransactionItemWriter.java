package com.laoumri.bankspringbatch.batch;

import com.laoumri.bankspringbatch.entity.Compte;
import com.laoumri.bankspringbatch.entity.Transaction;
import com.laoumri.bankspringbatch.repository.CompteRepository;
import com.laoumri.bankspringbatch.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionItemWriter implements ItemWriter<Transaction> {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;
    @Override
    public void write(Chunk<? extends Transaction> chunk) throws Exception {
        for(Transaction transaction : chunk) {
            Compte compte = transaction.getCompte();
            compteRepository.save(compte);
            transactionRepository.save(transaction);
        }
    }
}
