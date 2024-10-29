package com.laoumri.bankspringbatch.repository;

import com.laoumri.bankspringbatch.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Integer> {
}
