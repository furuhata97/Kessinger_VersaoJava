package com.kessinger.kessinger.repository;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Publicacao;
import com.kessinger.kessinger.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Integer>  {

    List<Publicacao> findByUser(Usuario usuario);

    @Query("select a from Publicacao a where a.nome like CONCAT('%',?1,'%')")
    List<Publicacao> findAllByNome(String nome);

}
