package com.empresa.fichajes.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        if (session.getAttribute("employee") == null) {
            return "redirect:/";
        }

        return "dashboard";
    }


}
