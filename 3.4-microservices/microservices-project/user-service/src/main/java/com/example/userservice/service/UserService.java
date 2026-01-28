package com.example.userservice.service;

import com.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestión de usuarios
 */
@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // Datos de ejemplo
        users.add(new User(idGenerator.getAndIncrement(), "Juan Pérez", "juan@example.com"));
        users.add(new User(idGenerator.getAndIncrement(), "María García", "maria@example.com"));
        users.add(new User(idGenerator.getAndIncrement(), "Carlos López", "carlos@example.com"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User createUser(User user) {
        user.setId(idGenerator.getAndIncrement());
        users.add(user);
        return user;
    }
}
