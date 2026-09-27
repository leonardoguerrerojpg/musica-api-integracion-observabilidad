package com.ejemplo.musica.service;

import com.ejemplo.musica.model.Artista;
import com.ejemplo.musica.model.Cancion;
import com.ejemplo.musica.repository.ArtistaRepository;
import com.ejemplo.musica.repository.CancionRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CancionService {

    private static final Logger logger = LoggerFactory.getLogger(CancionService.class);

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final Counter contadorCancionesCreadas;

    @Autowired
    public CancionService(CancionRepository cancionRepository, 
                          ArtistaRepository artistaRepository, 
                          MeterRegistry meterRegistry) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        // Metrica personalizada para Prometheus y Actuator
        this.contadorCancionesCreadas = Counter.builder("canciones_creadas_total")
                .description("Total de canciones registradas en el catalogo")
                .register(meterRegistry);
    }

    public List<Cancion> listarTodas() {
        logger.info("Consultando todas las canciones del catalogo");
        return cancionRepository.findAll();
    }

    public Optional<Cancion> buscarPorId(Long id) {
        logger.info("Buscando cancion con id: {}", id);
        return cancionRepository.findById(id);
    }

    public Cancion guardar(Cancion c) {
        // Si la cancion viene con un artista sin guardar, lo persistimos primero
        if (c.getArtista() != null && c.getArtista().getId() == null) {
            Artista artistaGuardado = artistaRepository.save(c.getArtista());
            c.setArtista(artistaGuardado);
            logger.info("Nuevo artista registrado: {}", artistaGuardado.getNombre());
        }

        Cancion guardada = cancionRepository.save(c);
        contadorCancionesCreadas.increment();
        logger.info("Cancion guardada exitosamente: '{}' (ID: {})", guardada.getTitulo(), guardada.getId());
        return guardada;
    }

    public Optional<Cancion> actualizar(Long id, Cancion datos) {
        return cancionRepository.findById(id).map(c -> {
            c.setTitulo(datos.getTitulo());
            c.setAlbum(datos.getAlbum());
            c.setGenero(datos.getGenero());
            c.setDuracion(datos.getDuracion());

            if (datos.getArtista() != null) {
                if (datos.getArtista().getId() == null) {
                    Artista artistaGuardado = artistaRepository.save(datos.getArtista());
                    c.setArtista(artistaGuardado);
                } else {
                    c.setArtista(datos.getArtista());
                }
            }

            Cancion actualizada = cancionRepository.save(c);
            logger.info("Cancion con ID {} actualizada correctamente", id);
            return actualizada;
        });
    }

    public boolean eliminar(Long id) {
        if (cancionRepository.existsById(id)) {
            cancionRepository.deleteById(id);
            logger.warn("Cancion con ID {} ha sido eliminada", id);
            return true;
        }
        logger.warn("Intento de eliminar cancion inexistente con ID {}", id);
        return false;
    }

    public List<Cancion> buscarPorGenero(String genero) {
        logger.info("Filtrando canciones por genero: {}", genero);
        return cancionRepository.findByGeneroIgnoreCase(genero);
    }

    public List<Cancion> buscarPorTitulo(String titulo) {
        logger.info("Filtrando canciones por titulo: {}", titulo);
        return cancionRepository.findByTituloContainingIgnoreCase(titulo);
    }
}