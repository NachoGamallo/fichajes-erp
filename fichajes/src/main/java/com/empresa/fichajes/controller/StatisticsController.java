package com.empresa.fichajes.controller;

import com.empresa.fichajes.model.Employee;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

    @GetMapping("/statistics")
    public String statistics(HttpSession session) {

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/";
        }

        return "statistics";
    }
}
