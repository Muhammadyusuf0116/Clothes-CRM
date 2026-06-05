package com.clothing.crm.controller;

import com.clothing.crm.entity.Order;
import com.clothing.crm.entity.OrderItem;
import com.clothing.crm.entity.Product;
import com.clothing.crm.entity.User;
import com.clothing.crm.service.OrderService;
import com.clothing.crm.service.ProductService;
import com.clothing.crm.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public OrderController(OrderService orderService, ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/create")
    public String showCreateOrderForm(@RequestParam(value = "productId", required = false) Long productId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("selectedProductId", productId);

        return "order-create";
    }

    @PostMapping("/orders/create")
    public String placeOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("shippingAddress") String shippingAddress,
            Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Mahsulot topilmadi"));

            OrderItem item = new OrderItem(product, quantity, product.getPrice());
            List<OrderItem> items = new ArrayList<>();
            items.add(item);

            orderService.placeOrder(user, items, shippingAddress);
            return "redirect:/orders?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Buyurtma berishda xatolik: " + e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("currentUser", user);
            return "order-create";
        }
    }
}
