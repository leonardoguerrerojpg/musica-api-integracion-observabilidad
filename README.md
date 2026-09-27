# API REST de Catálogo Musical - Integración y Observabilidad

Proyecto desarrollado con Java y Spring Boot para la gestión de un catálogo de canciones y artistas, con persistencia en MySQL, relaciones entre entidades, consumo de una API externa y observabilidad con Actuator y Prometheus.

## Integrantes
- Leonardo de Jesús Guerrero Montoya
- Jhon Wilman García Granja

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.2.5
- Spring Data JPA / Hibernate
- MySQL (XAMPP)
- Spring Boot Actuator & Micrometer Prometheus
- RestClient

## Configuración de MySQL
1. Iniciar el servicio de MySQL desde el panel de XAMPP en el puerto 3306.
2. La conexión está definida en `src/main/resources/application.properties`:
   - URL: `jdbc:mysql://localhost:3306/musica_db?createDatabaseIfNotExist=true`
   - Usuario: `root`
   - Contraseña: (vacía)

## Relación entre Entidades
Relación N a 1 implementada con anotaciones JPA:
- **Artista** (1): `id`, `nombre`, `paisOrigen`.
- **Cancion** (N): `id`, `titulo`, `album`, `genero`, `duracion` y la relación `@ManyToOne` hacia `Artista`.

## Endpoints Principales
- `GET /canciones` - Listar todas las canciones.
- `GET /canciones/{id}` - Consultar canción por ID.
- `POST /canciones` - Crear nueva canción y registrar artista.
- `PUT /canciones/{id}` - Actualizar datos de una canción.
- `DELETE /canciones/{id}` - Eliminar una canción.
- `GET /canciones/buscar?genero={genero}` - Búsqueda personalizada por género.
- `GET /canciones/titulo?q={texto}` - Búsqueda personalizada por título.
- `GET /canciones/externa/buscar?termino={texto}` - Consumo de iTunes Search API pública mediante RestClient.

## Observabilidad y Monitoreo
- `GET /actuator/health` - Estado general del sistema, base de datos MySQL e indicador personalizado `catalogo`.
- `GET /actuator/metrics` - Listado de métricas de la aplicación.
- `GET /actuator/metrics/canciones_creadas_total` - Métrica personalizada (contador de canciones registradas).
- `GET /actuator/prometheus` - Exposición de métricas en formato compatible con Prometheus.
- **Logs:** Registros con niveles `INFO`, `WARN` y `ERROR` en operaciones clave del servicio.