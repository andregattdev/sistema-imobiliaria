package com.asgtech.api_cadastro_clientes_imobiliaria.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "corretor_id", nullable = false)
    private Corretor corretor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "data_hora_lembrete")
    private LocalDateTime dataHoraLembrete;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_confirmacao", nullable = false, length = 20)
    private StatusConfirmacaoVisita statusConfirmacao = StatusConfirmacaoVisita.PENDENTE;

    @Column(nullable = false, length = 1000)
    private String observacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Corretor getCorretor() {
        return corretor;
    }

    public void setCorretor(Corretor corretor) {
        this.corretor = corretor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public LocalDateTime getDataHoraLembrete() {
        return dataHoraLembrete;
    }

    public void setDataHoraLembrete(LocalDateTime dataHoraLembrete) {
        this.dataHoraLembrete = dataHoraLembrete;
    }

    public StatusConfirmacaoVisita getStatusConfirmacao() {
        return statusConfirmacao;
    }

    public void setStatusConfirmacao(StatusConfirmacaoVisita statusConfirmacao) {
        this.statusConfirmacao = statusConfirmacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
