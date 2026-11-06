package com.asgtech.api_cadastro_clientes_imobiliaria.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "imoveis")
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean ativo = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImovel tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusImovel status = StatusImovel.DISPONIVEL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'VENDA'")
    private FinalidadeImovel finalidade = FinalidadeImovel.VENDA;

    @NotNull
    @Positive
    @Column(name = "area_util", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaUtil;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer quartos;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer banheiros;

    @NotNull
    @PositiveOrZero
    @Column(name = "vagas_garagem", nullable = false)
    private Integer vagasGaragem;

    @NotNull
    @PositiveOrZero
    @Column(name = "preco_venda", nullable = false, precision = 14, scale = 2)
    private BigDecimal precoVenda;

    @PositiveOrZero
    @Column(name = "preco_aluguel", precision = 14, scale = 2)
    private BigDecimal precoAluguel;

    @NotNull
    @PositiveOrZero
    @Column(name = "preco_condominio", nullable = false, precision = 14, scale = 2)
    private BigDecimal precoCondominio;

    @NotNull
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false, unique = true)
    private Endereco endereco;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Cliente proprietario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public TipoImovel getTipo() {
        return tipo;
    }

    public void setTipo(TipoImovel tipo) {
        this.tipo = tipo;
    }

    public StatusImovel getStatus() {
        return status;
    }

    public void setStatus(StatusImovel status) {
        this.status = status;
    }

    public FinalidadeImovel getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(FinalidadeImovel finalidade) {
        this.finalidade = finalidade;
    }

    public BigDecimal getAreaUtil() {
        return areaUtil;
    }

    public void setAreaUtil(BigDecimal areaUtil) {
        this.areaUtil = areaUtil;
    }

    public Integer getQuartos() {
        return quartos;
    }

    public void setQuartos(Integer quartos) {
        this.quartos = quartos;
    }

    public Integer getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(Integer banheiros) {
        this.banheiros = banheiros;
    }

    public Integer getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(Integer vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getPrecoAluguel() {
        return precoAluguel;
    }

    public void setPrecoAluguel(BigDecimal precoAluguel) {
        this.precoAluguel = precoAluguel;
    }

    public BigDecimal getPrecoCondominio() {
        return precoCondominio;
    }

    public void setPrecoCondominio(BigDecimal precoCondominio) {
        this.precoCondominio = precoCondominio;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        if (this.endereco != null && this.endereco.getImovel() == this) {
            this.endereco.setImovel(null);
        }
        this.endereco = endereco;
        if (endereco != null) {
            endereco.setImovel(this);
        }
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public void setProprietario(Cliente proprietario) {
        if (this.proprietario != null) {
            this.proprietario.getImoveis().remove(this);
        }
        this.proprietario = proprietario;
        if (proprietario != null && !proprietario.getImoveis().contains(this)) {
            proprietario.getImoveis().add(this);
        }
    }
}
