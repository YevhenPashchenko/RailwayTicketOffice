package com.my.railwayticketoffice;

public enum DayOfWeekLocaleUA {
    MONDAY("Понеділок"),
    TUESDAY("Вівторок"),
    WEDNESDAY("Середа"),
    THURSDAY("Четвер"),
    FRIDAY("П`ятниця"),
    SATURDAY("Субота"),
    SUNDAY("Неділя");

    private final String dayName;

    DayOfWeekLocaleUA(String dayName) {
        this.dayName = dayName;
    }

    private static final DayOfWeekLocaleUA[] ENUMS = DayOfWeekLocaleUA.values();

    public static String of(int dayOfWeekIndex) {
        return ENUMS[dayOfWeekIndex - 1].dayName;
    }
}
