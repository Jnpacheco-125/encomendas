package com.challenge.encomendas.encomendas.infrastructure.persistence.entities;

import com.challenge.encomendas.encomendas.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "funcionario_roles", joinColumns = @JoinColumn(name = "funcionario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    // Getters e Setters
}
