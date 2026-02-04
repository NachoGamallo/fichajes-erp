package com.empresa.fichajes.controller;

import com.empresa.fichajes.model.Department;
import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.repository.DepartmentRepository;
import com.empresa.fichajes.repository.EmployeeRepository;
import com.empresa.fichajes.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private EmployeeRepository empRepo;
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private AttendanceService service;

    @GetMapping("/departments")
    public List<Department> getDepts() {
        return deptRepo.findAll(); // Verifica que no devuelva lista vacía
    }

    @GetMapping("/employees/{deptId}")
    public List<Employee> getEmps(@PathVariable Long deptId) {
        return empRepo.findByDepartmentId(deptId);
    }

    @PostMapping("/punch")
    public ResponseEntity<String> punch(@RequestParam Long empId, @RequestParam String pass, @RequestParam String action) {
        Employee emp = empRepo.findById(empId).orElseThrow();
        if (!emp.getPassword().equals(pass)) return ResponseEntity.status(401).body("Password incorrecta.");

        String response = service.processPunch(emp, action);
        if (response.startsWith("ERROR")) return ResponseEntity.badRequest().body(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/{empId}")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long empId) {
        // Obtenemos los promedios de los últimos 2 meses
        Map<String, Object> baseStats = service.getEmployeeStats(empId);

        // Calculamos ausencias y retrasos del mismo periodo
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        Map<String, Object> scheduleMetrics = service.calculateScheduleMetrics(empId, twoMonthsAgo);

        // Unimos todo en un solo mapa para el JSON
        java.util.HashMap<String, Object> combined = new java.util.HashMap<>(baseStats);
        combined.putAll(scheduleMetrics);

        return ResponseEntity.ok(combined);
    }

    @GetMapping("/history/{empId}")
    public ResponseEntity<Map<String, Object>> getHistory(
            @PathVariable Long empId,
            @RequestParam String range) {
        return ResponseEntity.ok(service.getDynamicHistory(empId, range));
    }

    @GetMapping("/stats/dynamic/{empId}")
    public ResponseEntity<Map<String, Object>> getDynamicStats(@PathVariable Long empId, @RequestParam String range) {
        // 1. Obtenemos los datos base (horas de trabajo, extra, etc.)
        Map<String, Object> history = service.getDynamicHistory(empId, range);

        // 2. Determinamos la fecha de inicio según el rango para calcular ausencias
        LocalDateTime start = service.getStartDateTimeFromRange(range);

        // 3. Calculamos ausencias e impuntualidades para ese periodo específico
        Map<String, Object> metrics = service.calculateScheduleMetrics(empId, start);

        // 4. Combinamos todo
        java.util.HashMap<String, Object> response = new java.util.HashMap<>(history);
        response.putAll(metrics);

        return ResponseEntity.ok(response);
    }
}
