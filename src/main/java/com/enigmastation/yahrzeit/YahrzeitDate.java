package com.enigmastation.yahrzeit;

import java.util.Calendar;

abstract public class YahrzeitDate {
    int year;
    int month;
    int day;

    abstract public String getMonthName();

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public abstract int toAbsolute();

    int toAbsolute(Calendar c) {
        int gm = c.get(Calendar.MONTH) + 1;
        int gd = c.get(Calendar.DAY_OF_MONTH);
        int gy = c.get(Calendar.YEAR);
        int N = gd;        // days this month
        for (int m = gm - 1; m > 0; m--)    // days in prior months this year
            N = N + lastDayOfGregorianMonth(m, getYear());
        return (N            // days this year
                + 365 * (gy - 1)    // days in previous years ignoring leap days
                + (gy - 1) / 4    // Julian leap days before this year...
                - (gy - 1) / 100    // ...minus prior century years...
                + (gy - 1) / 400);
    }

    int lastDayOfGregorianMonth(int month, int year) {
        // Compute the last date of the month for the Gregorian calendar.

        switch (month) {
            case 2:
                if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0))
                    return 29;
                else
                    return 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

}
