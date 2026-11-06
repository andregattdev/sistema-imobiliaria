package com.asgtech.api_cadastro_clientes_imobiliaria.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Visita;

public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findAllByDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc(
            LocalDateTime inicio,
            LocalDateTime fim);
}
