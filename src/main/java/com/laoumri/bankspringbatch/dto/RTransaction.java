package com.laoumri.bankspringbatch.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RTransaction implements Serializable {
    private int idTransaction;
    private int idCompte;
    private double montant;
    private String type;
    private String strDateTransaction;
}
