package com.ejemplo.musica.controller;

import com.ejemplo.musica.model.Cancion;
import com.ejemplo.musica.service.CancionService;
import com.ejemplo.musica.service.MusicaExternaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canciones")
@CrossOrigin(origins = "*")
public class CancionController {

    private final CancionService service;
    private final MusicaExternaService musicaExternaService;

    @Autowired
    public CancionController(CancionService service, MusicaExternaService musicaExternaService) {
        this.service = service;
        this.musicaExternaService = musicaExternaService;
    }

    // 201 CREATED - Crear (relacionado con Artista)
    @PostMapping
    public ResponseEntity<Cancion> crear(@RequestBody Cancion cancion) {
        Cancion nueva = service.guardar(cancion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // 200 OK - Listar todas
    @GetMapping
    public ResponseEntity<List<Cancion>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // 200 OK / 404 NOT FOUND - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cancion> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 200 OK / 404 NOT FOUND - Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Cancion> actualizar(@PathVariable Long id, @RequestBody Cancion cancion) {
        return service.actualizar(id, cancion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 204 NO CONTENT / 404 NOT FOUND - Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 200 OK / 204 - Consulta personalizada por genero
    @GetMapping("/buscar")
    public ResponseEntity<List<Cancion>> buscarPorGenero(@RequestParam String genero) {
        List<Cancion> resultado = service.buscarPorGenero(genero);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    // 200 OK / 204 - Consulta personalizada por titulo (parcial)
    @GetMapping("/titulo")
    public ResponseEntity<List<Cancion>> buscarPorTitulo(@RequestParam String q) {
        List<Cancion> resultado = service.buscarPorTitulo(q);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    // Consumo de API Externa (iTunes Search API)
    @GetMapping("/externa/buscar")
    public ResponseEntity<String> buscarExterna(@RequestParam String termino) {
        String resultado = musicaExternaService.buscarEnApiExterna(termino);
        return ResponseEntity.ok(resultado);
    }
}