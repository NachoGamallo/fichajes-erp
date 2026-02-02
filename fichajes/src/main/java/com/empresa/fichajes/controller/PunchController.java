package com.empresa.fichajes.controller;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.PunchStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PunchController {

    @PostMapping("/punch/start")
    public String startWork(HttpSession session) {

        Employee employee = (Employee) session.getAttribute("employee");
        PunchStatus status = (PunchStatus) session.getAttribute("punchStatus");

        if (employee == null || status != PunchStatus.NOT_WORKING) {
            return "redirect:/dashboard?error=INVALID_ACTION";
        }

        session.setAttribute("punchStatus", PunchStatus.WORKING);
        System.out.println("START WORK: " + employee.getEmail());

        return "redirect:/dashboard";
    }

    @PostMapping("/punch/end")
    public String endWork(HttpSession session) {

        PunchStatus status = (PunchStatus) session.getAttribute("punchStatus");

        if (status != PunchStatus.WORKING) {
            return "redirect:/dashboard?error=INVALID_ACTION";
        }

        session.setAttribute("punchStatus", PunchStatus.NOT_WORKING);
        System.out.println("END WORK");

        return "redirect:/dashboard";
    }

    @PostMapping("/punch/break-in")
    public String breakIn(HttpSession session) {

        PunchStatus status = (PunchStatus) session.getAttribute("punchStatus");

        if (status != PunchStatus.WORKING) {
            return "redirect:/dashboard?error=INVALID_ACTION";
        }

        session.setAttribute("punchStatus", PunchStatus.ON_BREAK);
        System.out.println("BREAK IN");

        return "redirect:/dashboard";
    }

    @PostMapping("/punch/break-out")
    public String breakOut(HttpSession session) {

        PunchStatus status = (PunchStatus) session.getAttribute("punchStatus");

        if (status != PunchStatus.ON_BREAK) {
            return "redirect:/dashboard?error=INVALID_ACTION";
        }

        session.setAttribute("punchStatus", PunchStatus.WORKING);
        System.out.println("BREAK OUT");

        return "redirect:/dashboard";
    }
}
