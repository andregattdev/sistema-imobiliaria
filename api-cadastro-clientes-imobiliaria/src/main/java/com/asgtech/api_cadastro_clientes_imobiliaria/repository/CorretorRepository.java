package com.asgtech.api_cadastro_clientes_imobiliaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.Corretor;

public interface CorretorRepository extends JpaRepository<Corretor, Long> {

    List<Corretor> findAllByAtivoTrueOrderByNomeAsc();

    Optional<Corretor> findByIdAndAtivoTrue(Long id);
}
