package com.kessinger.kessinger.repository;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import com.kessinger.kessinger.model.enums.Area;
import com.kessinger.kessinger.model.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Integer>  {

    List<Publicacao> findByUser(Usuario usuario);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%')")
    List<Publicacao> findAllByNome(String nome);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%') and a.autor like CONCAT('%',?2,'%') and a.area like CONCAT('%', ?3, '%') and a.categoria like CONCAT('%', ?4, '%') and a.ano = ?5 and a.periodico = ?5")
    List<Publicacao> findAllByFiltro(String nome, String autor, String area, String categoria, Date ano, Periodico periodico);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%') and a.autor like CONCAT('%',?2,'%') and a.area like CONCAT('%', ?3, '%') and a.categoria like CONCAT('%', ?4, '%')")
    List<Publicacao> findAllUnlessDateAndPeriodico(String nome, String autor, String area, String categoria);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%') and a.autor like CONCAT('%',?2,'%') and a.area like CONCAT('%', ?3, '%') and a.categoria like CONCAT('%', ?4, '%') and a.periodico = ?5")
    List<Publicacao> findAllUnlessDate(String nome, String autor, String area, String categoria, Periodico periodico);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%') and a.autor like CONCAT('%',?2,'%') and a.area like CONCAT('%', ?3, '%') and a.categoria like CONCAT('%', ?4, '%') and a.ano = ?5")
    List<Publicacao> findAllUnlessPeriodico(String nome, String autor, String area, String categoria, Date ano);


}
