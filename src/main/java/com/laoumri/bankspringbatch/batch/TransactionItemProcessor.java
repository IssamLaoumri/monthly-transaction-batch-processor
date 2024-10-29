package com.laoumri.bankspringbatch.batch;

import com.laoumri.bankspringbatch.dto.RTransaction;
import com.laoumri.bankspringbatch.entity.Compte;
import com.laoumri.bankspringbatch.entity.Transaction;
import com.laoumri.bankspringbatch.repository.CompteRepository;
import com.laoumri.bankspringbatch.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionItemProcessor implements ItemProcessor<RTransaction, Transaction> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final CompteRepository compteRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction process(RTransaction item) {
        if (transactionRepository.existsById(item.getIdTransaction())) {
            log.warn("Transaction with ID {} already exists. Skipping...", item.getIdTransaction());
            return null;
        }

        Compte compte = compteRepository.findById(item.getIdCompte())
                .orElseThrow(() -> new IllegalArgumentException("Le compte avec l'ID : "+item.getIdCompte()+" est introuvable."));

        compte.setSolde(compte.getSolde()-item.getMontant());

        Transaction transaction = Transaction.builder()
                .id(item.getIdTransaction())
                .dateTransaction(LocalDateTime.parse(item.getStrDateTransaction(), formatter))
                .montant(item.getMontant())
                .dateDebit(LocalDateTime.now())
                .type(item.getType())
                .compte(compte)
                .build();

        return transaction;
    }
}
