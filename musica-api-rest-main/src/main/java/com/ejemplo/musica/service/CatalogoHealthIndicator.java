package com.ejemplo.musica.service;

import com.ejemplo.musica.repository.CancionRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CatalogoHealthIndicator implements HealthIndicator {

    private final CancionRepository cancionRepository;

    public CatalogoHealthIndicator(CancionRepository cancionRepository) {
        this.cancionRepository = cancionRepository;
    }

    @Override
    public Health health() {
        try {
            long totalCanciones = cancionRepository.count();
            return Health.up()
                    .withDetail("servicio", "Catalogo Musical Operativo")
                    .withDetail("total_canciones", totalCanciones)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", "No se puede acceder a la base de datos de catalogo")
                    .withException(e)
                    .build();
        }
    }
}