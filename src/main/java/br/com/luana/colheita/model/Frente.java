package br.com.luana.colheita.model;

import jakarta.persistence.*;

/**
 * Entidade que representa uma frente de colheita (ex: F-11).
 * Cada atributo vira uma coluna na tabela "frentes".
 */
@Entity
@Table(name = "frentes")
public class Frente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private String fornecedor;

    @Column(name = "toneladas_dia", nullable = false)
    private Double toneladasDia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusFrente status = StatusFrente.ATIVA;

    public Frente() {
    }

    public Frente(String codigo, String fornecedor, Double toneladasDia) {
        this.codigo = codigo;
        this.fornecedor = fornecedor;
        this.toneladasDia = toneladasDia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

    public Double getToneladasDia() { return toneladasDia; }
    public void setToneladasDia(Double toneladasDia) { this.toneladasDia = toneladasDia; }

    public StatusFrente getStatus() { return status; }
    public void setStatus(StatusFrente status) { this.status = status; }
}
