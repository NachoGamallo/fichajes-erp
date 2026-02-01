package com.empresa.fichajes.controller;

import com.empresa.fichajes.model.Employee;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PunchController {

    @GetMapping("/time-punch")
    public String punchPage(HttpSession session) {

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/";
        }

        return "/dashboard";
    }

    @PostMapping("/time-punch/in")
    public String punchIn(HttpSession session) {

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/";
        }

        System.out.println("Punch IN by " + employee.getEmail());
        return "redirect:/dashboard";
    }

    @PostMapping("/time-punch/out")
    public String punchOut(HttpSession session) {

        Employee employee = (Employee) session.getAttribute("employee");

        if (employee == null) {
            return "redirect:/";
        }

        System.out.println("Punch OUT by " + employee.getEmail());
        return "redirect:/dashboard";
    }
}
