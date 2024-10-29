package com.laoumri.bankspringbatch.repository;

import com.laoumri.bankspringbatch.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsById(int id);
}
