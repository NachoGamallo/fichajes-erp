package com.empresa.fichajes.util;

import java.time.Duration;
import java.time.LocalTime;

public class TimeCalculationUtil {

    public static Duration calculateEffectiveTime( LocalTime checkIn, LocalTime checkOut, Duration breakTime ){

        return Duration.between(checkIn,checkOut).minus(breakTime);

    }

    public static Duration calculateOverTime( Duration effective ){

        return effective.toHours() > 6 ? effective.minusHours(6) : Duration.ZERO;

    }

}
