package com.ejemplo.musica.repository;

import com.ejemplo.musica.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    List<Cancion> findByGeneroIgnoreCase(String genero);
    List<Cancion> findByTituloContainingIgnoreCase(String titulo);
}