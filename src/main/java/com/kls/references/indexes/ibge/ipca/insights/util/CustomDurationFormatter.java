package com.kls.references.indexes.ibge.ipca.insights.util;

import java.time.Duration;

public class CustomDurationFormatter {

    private CustomDurationFormatter() {}

    public static String formatFrom(long durationInMillis) {
        Duration duration = Duration.ofMillis(durationInMillis);
        return formatFrom(duration);
    }

    public static String formatFrom(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return days > 0
            ? String.format("%d days, %02d:%02d:%02d (hh:mm:ss)", days, hours, minutes, seconds)
            : String.format("%02d:%02d:%02d (hh:mm:ss)", hours, minutes, seconds);
    }

}
