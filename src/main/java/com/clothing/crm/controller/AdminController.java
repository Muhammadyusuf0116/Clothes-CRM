package com.clothing.crm.controller;

import com.clothing.crm.entity.Order;
import com.clothing.crm.entity.Product;
import com.clothing.crm.entity.User;
import com.clothing.crm.service.OrderService;
import com.clothing.crm.service.ProductService;
import com.clothing.crm.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        Map<String, Object> stats = orderService.getStatistics();
        model.addAttribute("stats", stats);

        List<Order> recentOrders = orderService.getAllOrders();
        // Limit to 5 recent orders for dashboard preview
        if (recentOrders.size() > 5) {
            model.addAttribute("recentOrders", recentOrders.subList(0, 5));
        } else {
            model.addAttribute("recentOrders", recentOrders);
        }

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String manageProducts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/products?success=true";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        model.addAttribute("product", product);
        return "admin/product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products?deleted=true";
    }

    @GetMapping("/orders")
    public String manageOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/orders/status")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders?updated=true";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("currentUser", user);

        Map<String, Object> stats = orderService.getStatistics();
        model.addAttribute("stats", stats);

        return "admin/reports";
    }
}
