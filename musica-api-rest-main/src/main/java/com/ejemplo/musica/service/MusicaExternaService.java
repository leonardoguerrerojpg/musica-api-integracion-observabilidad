package com.ejemplo.musica.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class MusicaExternaService {

    private static final Logger logger = LoggerFactory.getLogger(MusicaExternaService.class);
    private final RestClient restClient;

    public MusicaExternaService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://itunes.apple.com")
                .build();
    }

    public String buscarEnApiExterna(String termino) {
        logger.info("Consultando API externa de musica para el termino: {}", termino);
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("term", termino)
                            .queryParam("media", "music")
                            .queryParam("limit", 3)
                            .build())
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            logger.error("Error al comunicarse con la API externa de musica: {}", e.getMessage());
            return "{\"error\": \"No fue posible consultar el servicio externo de musica. Intente mas tarde.\"}";
        }
    }
}