package com.challenge.encomendas.encomendas.domain.entities;

import java.time.LocalDateTime;

public class Encomenda {

    private final Long id;
    private final String nomeDestinatario;
    private final String apartamento;
    private final String descricao;
    private LocalDateTime dataRecebimento;
    private Boolean retirada;
    private LocalDateTime dataRetirada;
    private final Funcionario funcionarioRecebimento;
    private final Morador moradorDestinatario;

    public Encomenda(Long id, String nomeDestinatario, String apartamento, String descricao,
                     LocalDateTime dataRecebimento, Boolean retirada, LocalDateTime dataRetirada,
                     Funcionario funcionarioRecebimento, Morador moradorDestinatario) {
        this.id = id;
        this.nomeDestinatario = nomeDestinatario;
        this.apartamento = apartamento;
        this.descricao = descricao;
        this.dataRecebimento = dataRecebimento;
        this.retirada = retirada != null ? retirada : false;
        this.dataRetirada = dataRetirada;
        this.funcionarioRecebimento = funcionarioRecebimento;
        this.moradorDestinatario = moradorDestinatario;
    }

    // Getters
    public Long getId() { return id; }
    public String getNomeDestinatario() { return nomeDestinatario; }
    public String getApartamento() { return apartamento; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataRecebimento() { return dataRecebimento; }
    public Boolean getRetirada() { return retirada; }
    public LocalDateTime getDataRetirada() { return dataRetirada; }
    public Funcionario getFuncionarioRecebimento() { return funcionarioRecebimento; }
    public Morador getMoradorDestinatario() { return moradorDestinatario; }

    // Setters para atributos que podem mudar
    public void setRetirada(Boolean retirada) { this.retirada = retirada; }
    public void setDataRetirada(LocalDateTime dataRetirada) { this.dataRetirada = dataRetirada; }
    public void setDataRecebimento(LocalDateTime dataRecebimento) { this.dataRecebimento = dataRecebimento; }
}
