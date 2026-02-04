package com.empresa.fichajes.service;

import com.empresa.fichajes.model.Employee;
import com.empresa.fichajes.model.TimeRecord;
import com.empresa.fichajes.repository.TimeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private TimeRecordRepository recordRepo;
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(15, 0);
    private static final int GRACE_PERIOD_MINS = 10;

    // Cálculo de estadísticas de los últimos 2 meses
    public Map<String, Object> getEmployeeStats(Long empId) {
        LocalDateTime twoMonthsAgo = LocalDateTime.now().minusMonths(2);
        List<TimeRecord> records = recordRepo.findByEmployee_IdAndTimestampAfter(empId, twoMonthsAgo);

        Map<LocalDate, List<TimeRecord>> dailyGroups = records.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().toLocalDate()));

        double totalWork = 0;
        double totalBreaks = 0;
        double totalExtra = 0;
        int lateness = 0;

        for (Map.Entry<LocalDate, List<TimeRecord>> entry : dailyGroups.entrySet()) {
            List<TimeRecord> dayRecords = entry.getValue();
            double work = calculateDayWork(dayRecords);
            double breaks = calculateDayBreaks(dayRecords);

            totalWork += work;
            totalBreaks += breaks;

            // Lógica de Horas Extra: si > 6h o si es Lunes/Viernes
            if (isOffDay(entry.getKey())) {
                totalExtra += work;
            } else if (work > 6.0) {
                totalExtra += (work - 6.0);
            }

            if (checkLateness(dayRecords)) lateness++;
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();

        //Obtener todos los registros de hoy para este empleado.
        List<TimeRecord> todayRecords = recordRepo.findByEmployee_IdAndTimestampAfter(empId, startOfDay);


        boolean sessionFinished = todayRecords.stream().anyMatch(r -> r.getActionType().equals("CLOCK_OUT"));
        boolean breakActive = isBreakOpen(todayRecords);

        int days = dailyGroups.size();
        return Map.of(
                "avgWork", days > 0 ? String.format("%.2f", totalWork / days) : "0",
                "avgBreak", days > 0 ? String.format("%.2f", totalBreaks / days) : "0",
                "avgExtra", days > 0 ? String.format("%.2f", totalExtra / days) : "0",
                "lateness", lateness,
                "sessionFinishedToday",sessionFinished,
                "isBreakOpen",breakActive,
                "daysWorked", days
        );
    }

    private double calculateDayWork(List<TimeRecord> records) {
        LocalDateTime in = null, out = null;
        for (TimeRecord r : records) {
            if (r.getActionType().equals("CLOCK_IN")) in = r.getTimestamp();
            if (r.getActionType().equals("CLOCK_OUT")) out = r.getTimestamp();
        }
        if (in != null && out != null) {
            long mins = Duration.between(in, out).toMinutes();
            return (mins / 60.0) - calculateDayBreaks(records);
        }
        return 0;
    }

    private double calculateDayBreaks(List<TimeRecord> records) {
        LocalDateTime bIn = null;
        long breakMins = 0;
        for (TimeRecord r : records) {
            if (r.getActionType().equals("BREAK_IN")) bIn = r.getTimestamp();
            if (r.getActionType().equals("BREAK_OUT") && bIn != null) {
                breakMins += Duration.between(bIn, r.getTimestamp()).toMinutes();
                bIn = null;
            }
        }
        return breakMins / 60.0;
    }

    private boolean isOffDay(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    private boolean checkLateness(List<TimeRecord> records) {
        return records.stream()
                .filter(r -> r.getActionType().equals("CLOCK_IN"))
                .anyMatch(r -> r.getTimestamp().toLocalTime().isAfter(LocalTime.of(9, 0)));
    }

    public String processPunch(Employee employee , String action){

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();

        //Obtener todos los registros de hoy para este empleado.
        List<TimeRecord> todayRecords = recordRepo.findByEmployee_IdAndTimestampAfter(employee.getId(), startOfDay);

        //Regla 1: solo 1 sesion al día.
        boolean alreadyFinished = todayRecords.stream().anyMatch(r -> r.getActionType().equals("CLOCK_OUT"));
        if (alreadyFinished) return "ERROR: Ya has cerrado tu sesion por hoy. No se permiten más acciones";

        //Regla 2: Si no hay CLOCK_IN , solo se permite CLOCK_IN
        boolean hasStarted = todayRecords.stream().anyMatch(r -> r.getActionType().equals("CLOCK_IN"));
        if (!hasStarted && !action.equals("CLOCK_IN")) return "ERROR: Debes iniciar jornada (Clock In) antes de realizar otras acciones.";

        //Regla 3: Si hay BREAK_IN abierto, la unica accion es BREAK_OUT.
        boolean breakOpen = isBreakOpen(todayRecords);
        if (breakOpen && !action.equals("BREAK_OUT")) return "ERROR: Tienes un descanso activo. Debes cerrarlo (Break Out) antes de realizar otra acción.";
        if (!breakOpen && action.equals("BREAK_OUT")) return "ERROR: No hay ningún descanso activo que cerrar.";

        // Alerta de fuera de horario (Lunes y Viernes)
        if (action.equals("CLOCK_IN") && (today.getDayOfWeek() == DayOfWeek.MONDAY || today.getDayOfWeek() == DayOfWeek.FRIDAY)) {
            return "Punch recorded. ALERT: Entrada fuera de horario (Horas Extra).";
        }

        //Si pasa validaciones , guardamos.

        TimeRecord record = new TimeRecord();
        record.setEmployee(employee);
        record.setActionType(action);
        record.setTimestamp(LocalDateTime.now());
        recordRepo.save(record);

        return "Registro completado: " + action;
    }

    private boolean isBreakOpen(List<TimeRecord> records){
        long ins = records.stream().filter(r -> r.getActionType().equals("BREAK_IN")).count();
        long outs = records.stream().filter(r -> r.getActionType().equals("BREAK_OUT")).count();
        return ins > outs; //Si hay más inicios que cierres. Esta abierto.
    }

    public Map<String, Object> getDynamicHistory(Long empId, String range) {
        LocalDateTime start;
        LocalDateTime now = LocalDateTime.now();

        switch (range) {
            case "DAY": start = now.toLocalDate().atStartOfDay(); break;
            case "WEEK": start = now.toLocalDate().minusWeeks(1).with(DayOfWeek.MONDAY).atStartOfDay(); break;
            case "MONTH": start = now.toLocalDate().withDayOfMonth(1).atStartOfDay(); break;
            case "YEAR": start = now.toLocalDate().withDayOfYear(1).atStartOfDay(); break;
            default: start = now.toLocalDate().minusMonths(2).atStartOfDay(); // Default 2 meses
        }

        List<TimeRecord> records = recordRepo.findByEmployee_IdAndTimestampAfter(empId, start);

        // Agrupamos y calculamos totales
        double totalWork = 0;
        double totalBreak = 0;
        double totalExtra = 0;

        Map<LocalDate, List<TimeRecord>> groups = records.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().toLocalDate()));

        for (Map.Entry<LocalDate, List<TimeRecord>> entry : groups.entrySet()) {
            double dayWork = calculateHours(entry.getValue(), "CLOCK");
            double dayBreak = calculateHours(entry.getValue(), "BREAK");

            totalWork += (dayWork - dayBreak);
            totalBreak += dayBreak;

            // Lógica de extras (sobre 6h o día libre)
            if (isOffDay(entry.getKey())) totalExtra += (dayWork - dayBreak);
            else if ((dayWork - dayBreak) > 6) totalExtra += ((dayWork - dayBreak) - 6);
        }

        return Map.of(
                "totalWork", String.format("%.2f", totalWork),
                "totalBreak", String.format("%.2f", totalBreak),
                "totalExtra", String.format("%.2f", totalExtra),
                "countDays", groups.size()
        );
    }

    // Metodo auxiliar para calcular horas entre IN y OUT
    private double calculateHours(List<TimeRecord> recs, String type) {
        String inTag = type.equals("CLOCK") ? "CLOCK_IN" : "BREAK_IN";
        String outTag = type.equals("CLOCK") ? "CLOCK_OUT" : "BREAK_OUT";

        Optional<TimeRecord> in = recs.stream().filter(r -> r.getActionType().equals(inTag)).findFirst();
        Optional<TimeRecord> out = recs.stream().filter(r -> r.getActionType().equals(outTag)).findFirst();

        if (in.isPresent() && out.isPresent()) {
            return Duration.between(in.get().getTimestamp(), out.get().getTimestamp()).toMinutes() / 60.0;
        }
        return 0;
    }

    // Metodo mejorado para el cálculo de estadísticas detalladas
    public Map<String, Object> calculateScheduleMetrics(Long empId, LocalDateTime startRange) {
        List<TimeRecord> records = recordRepo.findByEmployee_IdAndTimestampAfter(empId, startRange);

        // Agrupamos por día
        Map<LocalDate, List<TimeRecord>> dailyGroups = records.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().toLocalDate()));

        long inScheduleDays = 0;
        long offScheduleDays = 0;
        long absentDays = 0;
        long latenessCount = 0;

        LocalDate tempDate = startRange.toLocalDate();
        LocalDate endDate = LocalDate.now();

        while (tempDate.isBefore(endDate) || tempDate.equals(endDate)) {
            boolean worked = dailyGroups.containsKey(tempDate);
            boolean isRequiredDay = !isOffDay(tempDate); // Martes a Domingo

            if (worked) {
                if (isRequiredDay) inScheduleDays++;
                else offScheduleDays++; // Fichó un Lunes o Viernes

                // Verificar puntualidad (> 09:10)
                if (checkLateness(dailyGroups.get(tempDate))) latenessCount++;
            } else if (isRequiredDay) {
                absentDays++; // Era día laboral y no hay registro
            }
            tempDate = tempDate.plusDays(1);
        }

        return Map.of(
                "inSchedule", inScheduleDays,
                "offSchedule", offScheduleDays,
                "absences", absentDays,
                "lateness", latenessCount
        );
    }

    public LocalDateTime getStartDateTimeFromRange(String range) {
        LocalDateTime now = LocalDateTime.now();
        switch (range) {
            case "DAY": return now.toLocalDate().atStartOfDay();
            case "WEEK": return now.toLocalDate().minusWeeks(1).with(java.time.DayOfWeek.MONDAY).atStartOfDay();
            case "MONTH": return now.toLocalDate().withDayOfMonth(1).atStartOfDay();
            case "YEAR": return now.toLocalDate().withDayOfYear(1).atStartOfDay();
            default: return now.minusMonths(2);
        }
    }

}
