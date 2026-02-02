package com.empresa.fichajes.controller;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.PunchStatus;
import com.empresa.fichajes.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){this.authService = authService;}

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        Employee employee = authService.login(email,password);

        if (employee == null || !employee.getPassword().equals(password)) {
            return "redirect:/?error";
        }

        session.setAttribute("employee", employee);
        session.setAttribute("punchStatus", PunchStatus.NOT_WORKING);
        return "redirect:/dashboard";
    }

}
