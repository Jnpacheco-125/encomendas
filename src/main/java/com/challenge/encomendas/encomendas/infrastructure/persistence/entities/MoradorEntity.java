package com.challenge.encomendas.encomendas.infrastructure.persistence.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "moradores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoradorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private String apartamento;
    @Column(unique = true)
    private String email;
    private String senha;
}
