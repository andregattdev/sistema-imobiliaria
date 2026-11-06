package com.asgtech.api_cadastro_clientes_imobiliaria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Negociacao;

public interface NegociacaoRepository extends JpaRepository<Negociacao, Long> {

    Page<Negociacao> findAllByOrderByAtualizadaEmDesc(Pageable pageable);
}
