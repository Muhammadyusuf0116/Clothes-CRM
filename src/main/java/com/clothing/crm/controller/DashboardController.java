package com.clothing.crm.controller;

import com.clothing.crm.entity.Order;
import com.clothing.crm.entity.User;
import com.clothing.crm.service.OrderService;
import com.clothing.crm.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final UserService userService;
    private final OrderService orderService;

    public DashboardController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("currentUser", user);

        if ("ROLE_ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        // Customer Dashboard details
        List<Order> userOrders = orderService.getOrdersByUser(user);
        double totalSpent = userOrders.stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalPrice)
                .sum();

        model.addAttribute("orders", userOrders);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("orderCount", userOrders.size());

        return "dashboard";
    }
}
