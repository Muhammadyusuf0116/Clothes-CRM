package com.clothing.crm.service;

import com.clothing.crm.entity.Order;
import com.clothing.crm.entity.OrderItem;
import com.clothing.crm.entity.Product;
import com.clothing.crm.entity.User;
import com.clothing.crm.repository.OrderRepository;
import com.clothing.crm.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order placeOrder(User user, List<OrderItem> items, String shippingAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setShippingAddress(shippingAddress);

        double total = 0.0;
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName() 
                        + " (Available: " + product.getStockQuantity() + ")");
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            item.setProduct(product);
            item.setPrice(product.getPrice());
            order.addItem(item);

            total += product.getPrice() * item.getQuantity();
        }

        order.setTotalPrice(total);
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Dashboard & Report stats
    public Map<String, Object> getStatistics() {
        List<Order> orders = orderRepository.findAll();
        double totalSales = 0.0;
        long pendingOrders = 0;
        long completedOrders = 0;
        long processingOrders = 0;

        for (Order o : orders) {
            if ("COMPLETED".equals(o.getStatus()) || "PROCESSING".equals(o.getStatus()) || "PENDING".equals(o.getStatus())) {
                totalSales += o.getTotalPrice();
            }
            if ("PENDING".equals(o.getStatus())) pendingOrders++;
            else if ("COMPLETED".equals(o.getStatus())) completedOrders++;
            else if ("PROCESSING".equals(o.getStatus())) processingOrders++;
        }

        long totalProducts = productRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSales", totalSales);
        stats.put("totalOrders", (long) orders.size());
        stats.put("pendingOrders", pendingOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("processingOrders", processingOrders);
        stats.put("totalProducts", totalProducts);

        return stats;
    }
}
