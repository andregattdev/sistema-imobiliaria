package com.asgtech.api_cadastro_clientes_imobiliaria.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.asgtech.api_cadastro_clientes_imobiliaria.model.FinalidadeImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.Imovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.StatusImovel;
import com.asgtech.api_cadastro_clientes_imobiliaria.model.TipoImovel;

public interface ImovelRepository extends JpaRepository<Imovel, Long> {

    Page<Imovel> findAllByAtivoTrueAndProprietarioAtivoTrue(Pageable pageable);

    Optional<Imovel> findByIdAndAtivoTrueAndProprietarioAtivoTrue(Long id);

    Optional<Imovel> findByIdAndProprietarioAtivoTrue(Long id);

    @Query("""
            select i from Imovel i
            where i.ativo = true and i.proprietario.ativo = true
              and (:tipo is null or i.tipo = :tipo)
              and (:finalidade is null or i.finalidade = :finalidade)
              and (:precoMinimo is null or
                   (case when i.finalidade = :aluguel then i.precoAluguel else i.precoVenda end) >= :precoMinimo)
              and (:precoMaximo is null or
                   (case when i.finalidade = :aluguel then i.precoAluguel else i.precoVenda end) <= :precoMaximo)
              and (:quartos is null or i.quartos >= :quartos)
              and (:disponibilidade is null or i.status = :disponibilidade)
              and (:localizacao is null or
                   lower(coalesce(i.endereco.localidade, '')) like concat('%', lower(:localizacao), '%') or
                   lower(coalesce(i.endereco.bairro, '')) like concat('%', lower(:localizacao), '%') or
                   lower(coalesce(i.endereco.logradouro, '')) like concat('%', lower(:localizacao), '%'))
            """)
    Page<Imovel> buscarComFiltros(
            @Param("tipo") TipoImovel tipo,
            @Param("finalidade") FinalidadeImovel finalidade,
            @Param("precoMinimo") BigDecimal precoMinimo,
            @Param("precoMaximo") BigDecimal precoMaximo,
            @Param("quartos") Integer quartos,
            @Param("localizacao") String localizacao,
            @Param("disponibilidade") StatusImovel disponibilidade,
            @Param("aluguel") FinalidadeImovel aluguel,
            Pageable pageable);
}
