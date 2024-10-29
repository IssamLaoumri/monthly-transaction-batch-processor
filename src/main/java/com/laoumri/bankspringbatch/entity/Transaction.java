package com.laoumri.bankspringbatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    private int id;
    private double montant;
    private String type;
    private LocalDateTime dateTransaction;
    private LocalDateTime dateDebit;

    @ManyToOne
    @JoinColumn(name = "idCompte")
    private Compte compte;
}
