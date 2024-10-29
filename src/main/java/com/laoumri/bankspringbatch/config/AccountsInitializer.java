package com.laoumri.bankspringbatch.config;

import com.laoumri.bankspringbatch.entity.Compte;
import com.laoumri.bankspringbatch.repository.CompteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class AccountsInitializer {
    @Bean
    public CommandLineRunner initDatabase(CompteRepository compteRepository) {
        return args -> {
            Compte c1 = new Compte(1, 1000.0);
            Compte c2 = new Compte(2, 2000.0);
            Compte c3 = new Compte(3, 3000.0);
            Compte c4 = new Compte(4, 4000.0);

            compteRepository.saveAll(List.of(c1, c2, c3, c4));

            log.info("Four accounts have been initialized in the database.");
        };
    }
}
