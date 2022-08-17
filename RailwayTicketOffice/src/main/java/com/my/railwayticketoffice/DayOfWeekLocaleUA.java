package com.my.railwayticketoffice;

public enum DayOfWeekLocaleUA {
    понеділок,
    вівторок,
    середа,
    четвер,
    пятниця,
    субота,
    неділя;

    private static final DayOfWeekLocaleUA[] ENUMS = DayOfWeekLocaleUA.values();

    public static DayOfWeekLocaleUA of(int dayOfWeekIndex) {
        return ENUMS[dayOfWeekIndex - 1];
    }
}
