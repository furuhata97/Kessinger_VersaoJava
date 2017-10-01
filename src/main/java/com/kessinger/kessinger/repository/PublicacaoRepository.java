package com.kessinger.kessinger.repository;

import com.kessinger.kessinger.model.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicacaoRepository extends JpaRepository<Publicacao, Integer>  {

}
