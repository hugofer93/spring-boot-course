package com.example.demo.controller;

import com.example.demo.dto.PostDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.exception.ExternalApiException;
import com.example.demo.service.JsonPlaceholderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Controlador para demostrar consumo de JSONPlaceholder API.
 * 
 * Este controlador muestra ejemplos usando tanto RestTemplate como WebClient.
 */
@RestController
@RequestMapping("/api/jsonplaceholder")
@RequiredArgsConstructor
public class JsonPlaceholderController {

    private final JsonPlaceholderService jsonPlaceholderService;

    // ========== ENDPOINTS CON RESTTEMPLATE ==========

    /**
     * GET /api/jsonplaceholder/resttemplate/posts/{id}
     * 
     * Obtiene un post por ID usando RestTemplate (bloqueante).
     */
    @GetMapping("/resttemplate/posts/{id}")
    public ResponseEntity<PostDTO> getPostByIdRestTemplate(@PathVariable Long id) {
        PostDTO post = jsonPlaceholderService.getPostByIdRestTemplate(id);
        return ResponseEntity.ok(post);
    }

    /**
     * GET /api/jsonplaceholder/resttemplate/posts
     * 
     * Obtiene todos los posts usando RestTemplate.
     */
    @GetMapping("/resttemplate/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsRestTemplate() {
        List<PostDTO> posts = jsonPlaceholderService.getAllPostsRestTemplate();
        return ResponseEntity.ok(posts);
    }

    /**
     * POST /api/jsonplaceholder/resttemplate/posts
     * 
     * Crea un nuevo post usando RestTemplate.
     */
    @PostMapping("/resttemplate/posts")
    public ResponseEntity<PostDTO> createPostRestTemplate(@RequestBody PostDTO post) {
        PostDTO createdPost = jsonPlaceholderService.createPostRestTemplate(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * GET /api/jsonplaceholder/resttemplate/users/{id}
     * 
     * Obtiene un usuario por ID usando RestTemplate.
     */
    @GetMapping("/resttemplate/users/{id}")
    public ResponseEntity<UserDTO> getUserByIdRestTemplate(@PathVariable Long id) {
        UserDTO user = jsonPlaceholderService.getUserByIdRestTemplate(id);
        return ResponseEntity.ok(user);
    }

    // ========== ENDPOINTS CON WEBCLIENT ==========

    /**
     * GET /api/jsonplaceholder/webclient/posts/{id}
     * 
     * Obtiene un post por ID usando WebClient (reactivo).
     * 
     * Nota: Retorna un Mono, que es un tipo reactivo de Spring WebFlux.
     */
    @GetMapping("/webclient/posts/{id}")
    public Mono<ResponseEntity<PostDTO>> getPostByIdWebClient(@PathVariable Long id) {
        return jsonPlaceholderService
                .getPostByIdWebClient(id)
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)) // Propagar la excepción para que GlobalExceptionHandler la maneje
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/jsonplaceholder/webclient/posts
     * 
     * Obtiene todos los posts usando WebClient.
     * 
     * Nota: Retorna un Flux para manejar múltiples elementos de forma reactiva.
     */
    @GetMapping("/webclient/posts")
    public Flux<PostDTO> getAllPostsWebClient() {
        return jsonPlaceholderService
                .getAllPostsWebClient()
                .onErrorResume(ExternalApiException.class, ex -> 
                    Flux.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }

    /**
     * POST /api/jsonplaceholder/webclient/posts
     * 
     * Crea un nuevo post usando WebClient.
     */
    @PostMapping("/webclient/posts")
    public Mono<ResponseEntity<PostDTO>> createPostWebClient(@RequestBody PostDTO post) {
        return jsonPlaceholderService
                .createPostWebClient(post)
                .map(createdPost -> ResponseEntity.status(HttpStatus.CREATED).body(createdPost))
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)); // Propagar la excepción para que GlobalExceptionHandler la maneje
    }

    /**
     * GET /api/jsonplaceholder/webclient/users/{id}
     * 
     * Obtiene un usuario por ID usando WebClient.
     */
    @GetMapping("/webclient/users/{id}")
    public Mono<ResponseEntity<UserDTO>> getUserByIdWebClient(@PathVariable Long id) {
        return jsonPlaceholderService
                .getUserByIdWebClient(id)
                .map(ResponseEntity::ok)
                .onErrorResume(ExternalApiException.class, ex -> 
                    Mono.error(ex)) // Propagar la excepción para que GlobalExceptionHandler la maneje
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/jsonplaceholder/webclient/posts/batch?ids=1,2,3
     * 
     * Ejemplo avanzado: Obtiene múltiples posts en paralelo usando WebClient.
     * 
     * Esto demuestra la ventaja de WebClient: puede hacer múltiples peticiones
     * en paralelo sin bloquear hilos.
     */
    @GetMapping("/webclient/posts/batch")
    public Flux<PostDTO> getMultiplePostsWebClient(@RequestParam List<Long> ids) {
        return jsonPlaceholderService.getMultiplePostsWebClient(ids);
    }
}
