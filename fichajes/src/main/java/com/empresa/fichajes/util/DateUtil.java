package com.empresa.fichajes.util;

import com.empresa.fichajes.model.WorkSchedule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class DateUtil {

    public static List<DayOfWeek> parseWorkingDays(String days){

        return List.of(days.split(",")).stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .toList();

    }

    public static boolean countAbsentDays(WorkSchedule schedule, LocalDate date){
        return parseWorkingDays(schedule.getWorkingDays()).contains(date.getDayOfWeek());
    }

    public static long countAbsentDays(WorkSchedule schedule, LocalDate start, LocalDate end, List<?> punches){

        long count = 0;
        LocalDate d = start;
        while (!d.isAfter(end)){

            boolean workingDay = parseWorkingDays(schedule.getWorkingDays()).contains(d.getDayOfWeek());
            boolean hasPunch = punches.stream().anyMatch(p -> {

                try {

                    Method m = p.getClass().getMethod("getPunchDate");
                    LocalDate pd = (LocalDate) m.invoke(p);
                    return pd.equals(d);

                } catch (Exception e) {

                    return false;

                }

            });

        }

        return count;

    }

}
