package com.my.railwayticketoffice;

public enum DayOfWeekLocaleUA {
    MONDAY("понеділок"),
    TUESDAY("вівторок"),
    WEDNESDAY("середа"),
    THURSDAY("четвер"),
    FRIDAY("п`ятниця"),
    SATURDAY("субота"),
    SUNDAY("неділя");

    private final String dayName;

    DayOfWeekLocaleUA(String dayName) {
        this.dayName = dayName;
    }

    private static final DayOfWeekLocaleUA[] ENUMS = DayOfWeekLocaleUA.values();

    public static String of(int dayOfWeekIndex) {
        return ENUMS[dayOfWeekIndex - 1].dayName;
    }
}
