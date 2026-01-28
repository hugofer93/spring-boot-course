package com.example.demo.service;

import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Servicio de ejemplo para consumir JSONPlaceholder API.
 * 
 * JSONPlaceholder es una API REST de prueba gratuita:
 * https://jsonplaceholder.typicode.com
 * 
 * Este servicio muestra ejemplos usando tanto RestTemplate como WebClient.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JsonPlaceholderService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Value("${external.apis.jsonplaceholder.base-url}")
    private String baseUrl;

    // ========== EJEMPLOS CON RESTTEMPLATE (BLOQUEANTE) ==========

    /**
     * Obtiene un post por ID usando RestTemplate (bloqueante).
     * 
     * RestTemplate es simple y directo, pero bloquea el hilo durante la petición.
     */
    public PostDTO getPostByIdRestTemplate(Long id) {
        try {
            String url = baseUrl + "/posts/" + id;
            log.info("Obteniendo post {} con RestTemplate", id);
            
            ResponseEntity<PostDTO> response = restTemplate.getForEntity(url, PostDTO.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Post obtenido exitosamente: {}", response.getBody().getTitle());
                return response.getBody();
            }
            
            throw new ExternalApiException("JSONPlaceholder", 
                "No se pudo obtener el post", HttpStatus.NOT_FOUND.value());
                
        } catch (HttpClientErrorException e) {
            log.error("Error del cliente al obtener post {}: {}", id, e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Post no encontrado", e.getStatusCode().value());
        } catch (HttpServerErrorException e) {
            log.error("Error del servidor al obtener post {}: {}", id, e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error del servidor externo", e.getStatusCode().value());
        } catch (ResourceAccessException e) {
            log.error("Error de conexión al obtener post {}: {}", id, e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error de conexión o timeout", 0);
        }
    }

    /**
     * Obtiene todos los posts usando RestTemplate.
     */
    public List<PostDTO> getAllPostsRestTemplate() {
        try {
            String url = baseUrl + "/posts";
            log.info("Obteniendo todos los posts con RestTemplate");
            
            ResponseEntity<List<PostDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostDTO>>() {}
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Se obtuvieron {} posts", response.getBody().size());
                return response.getBody();
            }
            
            throw new ExternalApiException("JSONPlaceholder", 
                "No se pudieron obtener los posts", HttpStatus.INTERNAL_SERVER_ERROR.value());
                
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error HTTP al obtener posts: {}", e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error al obtener posts", e.getStatusCode().value());
        } catch (ResourceAccessException e) {
            log.error("Error de conexión al obtener posts: {}", e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error de conexión o timeout", 0);
        }
    }

    /**
     * Crea un nuevo post usando RestTemplate.
     */
    public PostDTO createPostRestTemplate(PostDTO post) {
        try {
            String url = baseUrl + "/posts";
            log.info("Creando post con RestTemplate: {}", post.getTitle());
            
            ResponseEntity<PostDTO> response = restTemplate.postForEntity(url, post, PostDTO.class);
            
            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                log.info("Post creado exitosamente con ID: {}", response.getBody().getId());
                return response.getBody();
            }
            
            throw new ExternalApiException("JSONPlaceholder", 
                "No se pudo crear el post", HttpStatus.INTERNAL_SERVER_ERROR.value());
                
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error HTTP al crear post: {}", e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error al crear post", e.getStatusCode().value());
        } catch (ResourceAccessException e) {
            log.error("Error de conexión al crear post: {}", e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error de conexión o timeout", 0);
        }
    }

    /**
     * Obtiene un usuario por ID usando RestTemplate.
     */
    public UserDTO getUserByIdRestTemplate(Long id) {
        try {
            String url = baseUrl + "/users/" + id;
            log.info("Obteniendo usuario {} con RestTemplate", id);
            
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Usuario obtenido exitosamente: {}", response.getBody().getName());
                return response.getBody();
            }
            
            throw new ExternalApiException("JSONPlaceholder", 
                "No se pudo obtener el usuario", HttpStatus.NOT_FOUND.value());
                
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error HTTP al obtener usuario {}: {}", id, e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Usuario no encontrado", e.getStatusCode().value());
        } catch (ResourceAccessException e) {
            log.error("Error de conexión al obtener usuario {}: {}", id, e.getMessage());
            throw new ExternalApiException("JSONPlaceholder", 
                "Error de conexión o timeout", 0);
        }
    }

    // ========== EJEMPLOS CON WEBCLIENT (REACTIVO) ==========

    /**
     * Obtiene un post por ID usando WebClient (reactivo, no bloqueante).
     * 
     * WebClient es más eficiente porque no bloquea hilos.
     * Retorna un Mono que puede ser combinado con otros operadores reactivos.
     */
    public Mono<PostDTO> getPostByIdWebClient(Long id) {
        log.info("Obteniendo post {} con WebClient", id);
        
        return webClient
                .get()
                .uri(baseUrl + "/posts/{id}", id)
                .retrieve()
                .bodyToMono(PostDTO.class)
                .doOnSuccess(post -> log.info("Post obtenido exitosamente: {}", post.getTitle()))
                .doOnError(error -> log.error("Error al obtener post {}: {}", id, error.getMessage()))
                .onErrorMap(WebClientResponseException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error al obtener post: " + ex.getMessage(), 
                        ex.getStatusCode().value()))
                .onErrorMap(WebClientException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error de conexión o timeout", 0));
    }

    /**
     * Obtiene todos los posts usando WebClient.
     * 
     * Retorna un Flux para manejar múltiples elementos de forma reactiva.
     */
    public Flux<PostDTO> getAllPostsWebClient() {
        log.info("Obteniendo todos los posts con WebClient");
        
        return webClient
                .get()
                .uri(baseUrl + "/posts")
                .retrieve()
                .bodyToFlux(PostDTO.class)
                .doOnNext(post -> log.debug("Post recibido: {}", post.getTitle()))
                .doOnComplete(() -> log.info("Todos los posts obtenidos exitosamente"))
                .doOnError(error -> log.error("Error al obtener posts: {}", error.getMessage()))
                .onErrorMap(WebClientResponseException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error al obtener posts: " + ex.getMessage(), 
                        ex.getStatusCode().value()))
                .onErrorMap(WebClientException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error de conexión o timeout", 0));
    }

    /**
     * Crea un nuevo post usando WebClient.
     */
    public Mono<PostDTO> createPostWebClient(PostDTO post) {
        log.info("Creando post con WebClient: {}", post.getTitle());
        
        return webClient
                .post()
                .uri(baseUrl + "/posts")
                .bodyValue(post)
                .retrieve()
                .bodyToMono(PostDTO.class)
                .doOnSuccess(createdPost -> 
                    log.info("Post creado exitosamente con ID: {}", createdPost.getId()))
                .doOnError(error -> log.error("Error al crear post: {}", error.getMessage()))
                .onErrorMap(WebClientResponseException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error al crear post: " + ex.getMessage(), 
                        ex.getStatusCode().value()))
                .onErrorMap(WebClientException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error de conexión o timeout", 0));
    }

    /**
     * Obtiene un usuario por ID usando WebClient.
     */
    public Mono<UserDTO> getUserByIdWebClient(Long id) {
        log.info("Obteniendo usuario {} con WebClient", id);
        
        return webClient
                .get()
                .uri(baseUrl + "/users/{id}", id)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .doOnSuccess(user -> log.info("Usuario obtenido exitosamente: {}", user.getName()))
                .doOnError(error -> log.error("Error al obtener usuario {}: {}", id, error.getMessage()))
                .onErrorMap(WebClientResponseException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error al obtener usuario: " + ex.getMessage(), 
                        ex.getStatusCode().value()))
                .onErrorMap(WebClientException.class, ex -> 
                    new ExternalApiException("JSONPlaceholder", 
                        "Error de conexión o timeout", 0));
    }

    /**
     * Ejemplo avanzado: Obtiene múltiples posts de forma paralela usando WebClient.
     * 
     * Esto muestra la ventaja de WebClient: puede hacer múltiples peticiones
     * en paralelo sin bloquear hilos.
     */
    public Flux<PostDTO> getMultiplePostsWebClient(List<Long> ids) {
        log.info("Obteniendo {} posts en paralelo con WebClient", ids.size());
        
        return Flux.fromIterable(ids)
                .flatMap(id -> 
                    webClient
                        .get()
                        .uri(baseUrl + "/posts/{id}", id)
                        .retrieve()
                        .bodyToMono(PostDTO.class)
                        .onErrorResume(error -> {
                            log.warn("No se pudo obtener post {}: {}", id, error.getMessage());
                            return Mono.empty(); // Continuar con otros posts aunque uno falle
                        })
                )
                .doOnComplete(() -> log.info("Proceso de obtención de posts completado"));
    }
}
