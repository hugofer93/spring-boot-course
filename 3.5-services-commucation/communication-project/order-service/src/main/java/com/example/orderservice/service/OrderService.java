package com.example.orderservice.service;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Servicio para gestión de pedidos
 * 
 * Este servicio utiliza Feign Client para obtener información
 * de usuarios desde el user-service.
 */
@Slf4j
@Service
public class OrderService {

    private final UserClient userClient;
    private final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public OrderService(UserClient userClient) {
        this.userClient = userClient;
        // Inicializar datos de ejemplo
        initializeOrders();
    }

    /**
     * Inicializa pedidos de ejemplo
     */
    private void initializeOrders() {
        try {
            // Crear pedidos de ejemplo para usuarios existentes
            Order order1 = new Order();
            order1.setId(1L);
            order1.setUserId(1L);
            order1.setProductName("Laptop Dell XPS");
            order1.setAmount(1299.99);
            order1.setOrderDate(LocalDateTime.now().minusDays(2));
            order1.setStatus("DELIVERED");
            orders.put(1L, order1);

            Order order2 = new Order();
            order2.setId(2L);
            order2.setUserId(2L);
            order2.setProductName("iPhone 15 Pro");
            order2.setAmount(999.99);
            order2.setOrderDate(LocalDateTime.now().minusDays(1));
            order2.setStatus("SHIPPED");
            orders.put(2L, order2);

            Order order3 = new Order();
            order3.setId(3L);
            order3.setUserId(1L);
            order3.setProductName("Monitor LG 27 pulgadas");
            order3.setAmount(299.99);
            order3.setOrderDate(LocalDateTime.now());
            order3.setStatus("PENDING");
            orders.put(3L, order3);

            idGenerator.set(4L);
            log.info("Pedidos de ejemplo inicializados: {}", orders.size());
        } catch (Exception e) {
            log.warn("No se pudieron inicializar pedidos de ejemplo: {}", e.getMessage());
        }
    }

    /**
     * Obtiene todos los pedidos con información del usuario
     */
    public List<Order> getAllOrders() {
        log.info("Obteniendo todos los pedidos");
        List<Order> ordersList = new ArrayList<>(orders.values());
        
        // Enriquecer pedidos con información del usuario usando Feign Client
        ordersList.forEach(order -> {
            try {
                User user = userClient.getUserById(order.getUserId());
                if (user != null) {
                    order.setUserName(user.getName());
                    order.setUserEmail(user.getEmail());
                }
            } catch (Exception e) {
                log.error("Error al obtener información del usuario {}: {}", order.getUserId(), e.getMessage());
            }
        });
        
        return ordersList;
    }

    /**
     * Obtiene un pedido por ID con información del usuario
     */
    public Optional<Order> getOrderById(Long id) {
        log.info("Buscando pedido con ID: {}", id);
        Order order = orders.get(id);
        
        if (order != null) {
            // Obtener información del usuario usando Feign Client
            try {
                User user = userClient.getUserById(order.getUserId());
                if (user != null) {
                    order.setUserName(user.getName());
                    order.setUserEmail(user.getEmail());
                }
            } catch (Exception e) {
                log.error("Error al obtener información del usuario {}: {}", order.getUserId(), e.getMessage());
            }
        }
        
        return Optional.ofNullable(order);
    }

    /**
     * Crea un nuevo pedido
     * Valida que el usuario exista usando Feign Client
     */
    public Order createOrder(Order order) {
        log.info("Creando nuevo pedido para usuario: {}", order.getUserId());
        
        // Validar que el usuario exista usando Feign Client
        try {
            User user = userClient.getUserById(order.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("Usuario con ID " + order.getUserId() + " no encontrado");
            }
            
            // Enriquecer el pedido con información del usuario
            order.setUserName(user.getName());
            order.setUserEmail(user.getEmail());
        } catch (Exception e) {
            log.error("Error al validar usuario: {}", e.getMessage());
            throw new IllegalArgumentException("No se pudo validar el usuario: " + e.getMessage());
        }
        
        // Crear el pedido
        Long id = idGenerator.getAndIncrement();
        order.setId(id);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        orders.put(id, order);
        
        log.info("Pedido creado exitosamente con ID: {}", id);
        return order;
    }

    /**
     * Obtiene todos los usuarios usando Feign Client
     * Ejemplo de uso directo del cliente Feign
     */
    public List<User> getAllUsers() {
        log.info("Obteniendo todos los usuarios desde user-service usando Feign Client");
        return userClient.getAllUsers();
    }
}
