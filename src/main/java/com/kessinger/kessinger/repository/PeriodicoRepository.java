package com.kessinger.kessinger.repository;

import com.kessinger.kessinger.model.Periodico;
import com.kessinger.kessinger.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeriodicoRepository extends JpaRepository<Periodico, Integer> {

    List<Periodico> findByUser(Usuario usuario);

}
