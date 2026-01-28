package com.example.userservice.service;

import com.example.userservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestión de usuarios
 */
@Slf4j
@Service
public class UserService {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // Datos de ejemplo
        initializeUsers();
    }

    private void initializeUsers() {
        saveUser(new User(1L, "Juan Pérez", "juan.perez@example.com", "Calle Principal 123"));
        saveUser(new User(2L, "María García", "maria.garcia@example.com", "Avenida Central 456"));
        saveUser(new User(3L, "Carlos López", "carlos.lopez@example.com", "Plaza Mayor 789"));
    }

    public List<User> getAllUsers() {
        log.info("Obteniendo todos los usuarios");
        return new ArrayList<>(users.values());
    }

    public Optional<User> getUserById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return Optional.ofNullable(users.get(id));
    }

    public User createUser(User user) {
        log.info("Creando nuevo usuario: {}", user.getName());
        Long id = idGenerator.getAndIncrement();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    private void saveUser(User user) {
        users.put(user.getId(), user);
        if (user.getId() >= idGenerator.get()) {
            idGenerator.set(user.getId() + 1);
        }
    }
}
