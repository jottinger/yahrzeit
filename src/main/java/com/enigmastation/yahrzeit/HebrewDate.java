package com.enigmastation.yahrzeit;

import java.util.Calendar;

public class HebrewDate {
    int year;
    int month;
    int day;

    final static int epoch = -1373429;    // Absolute date of start of Hebrew calendar

    public HebrewDate(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    private static int
    lastDayOfGregorianMonth(int month, int year) {
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


    public HebrewDate() {
        Calendar c = Calendar.getInstance();
        int gm = c.get(Calendar.MONTH)+1;
        int gd = c.get(Calendar.DAY_OF_MONTH);
        int gy = c.get(Calendar.YEAR);
        int N = gd;        // days this month
        for (int m = gm - 1; m > 0; m--)    // days in prior months this year
            N = N + lastDayOfGregorianMonth(m, year);
        int absoluteDay = (N            // days this year
                + 365 * (gy - 1)    // days in previous years ignoring leap days
                + (gy - 1) / 4    // Julian leap days before this year...
                - (gy - 1) / 100    // ...minus prior century years...
                + (gy - 1) / 400);    // ...plus prior years divisible by 400
        initFromAbsoluteDate(absoluteDay);
    }

    public int toAbsolute() {
        int dayInYear = day;    // Days so far this month.
        if (month < 7) {   // Before Tishri, so add days in prior months
            // this year before and after Nisan.
            int m = 7;
            while (m <= (lastMonth(year))) {
                dayInYear = dayInYear + lastDayOfMonth(m, year);
                m++;
            }
            ;
            m = 1;
            while (m < month) {
                dayInYear = dayInYear + lastDayOfMonth(m, year);
                m++;
            }
        } else {   // Add days in prior months this year
            int m = 7;
            while (m < month) {
                dayInYear = dayInYear + lastDayOfMonth(m, year);
                m++;
            }
        }
        return (dayInYear + (elapsedDays(year)    // Days in prior years.
                + epoch));    // Days elapsed before absolute date 1.
    }

    public HebrewDate(int d) {
        initFromAbsoluteDate(d);
    }

    private void initFromAbsoluteDate(int d) {
        year = (d + epoch) / 366;
        while (d >= new HebrewDate(7, 1, year + 1).toAbsolute())
            year++;
        // Search forward for month from either Tishri or Nisan.
        if (d < new HebrewDate(1, 1, year).toAbsolute())
            month = 7;        //  Start at Tishri
        else
            month = 1;        //  Start at Nisan
        while (d > new HebrewDate(month, (lastDayOfMonth(month, year)), year).toAbsolute())
            month++;
        // Calculate the day by subtraction.
        day = d - new HebrewDate(month, 1, year).toAbsolute() + 1;
    }

    static boolean isLeapYear(int year) {
        if ((((7 * year) + 1) % 19) < 7)
            return true;
        else
            return false;
    }

    static int lastMonth(int year) {
        return (isLeapYear(year)) ? 13 : 12;
    }

    static int elapsedDays(int year) {
        // Number of days elapsed from the Sunday prior to the start of the
// Hebrew calendar to the mean conjunction of Tishri of Hebrew year.

        int monthsElapsed = (235 * ((year - 1) / 19))    // Months in complete cycles so far.
                + (12 * ((year - 1) % 19))    // Regular months in this cycle.
                + (7 * ((year - 1) % 19) + 1) / 19;    // Leap months this cycle
        int partsElapsed = 204 + 793 * (monthsElapsed % 1080);
        int h = 5 + 12 * monthsElapsed + 793 * (monthsElapsed / 1080)
                + partsElapsed / 1080;
        int conjunctionDay = 1 + 29 * monthsElapsed + h / 24;
        int conjunctionParts = 1080 * (h % 24) + partsElapsed % 1080;
        int alternativeDay;
        if ((conjunctionParts >= 19440)    // If new moon is at or after midday,
                || (((conjunctionDay % 7) == 2)    // ...or is on a Tuesday...
                && (conjunctionParts >= 9924)    // at 9 hours, 204 parts or later...
                && !(isLeapYear(year)))    // ...of a common year,
                || (((conjunctionDay % 7) == 1)    // ...or is on a Monday at...
                && (conjunctionParts >= 16789)    // 15 hours, 589 parts or later...
                && (isLeapYear(year - 1))))    // at the end of a leap year
            // Then postpone Rosh HaShanah one day
            alternativeDay = conjunctionDay + 1;
        else
            alternativeDay = conjunctionDay;
        if (((alternativeDay % 7) == 0)    // If Rosh HaShanah would occur on Sunday,
                || ((alternativeDay % 7) == 3)    // or Wednesday,
                || ((alternativeDay % 7) == 5))    // or Friday
            // Then postpone it one (more) day
            return (1 + alternativeDay);
        else
            return alternativeDay;
    }

    static int daysInYear(int year) {
        return ((elapsedDays(year + 1))
                - (elapsedDays(year)));
    }

    static boolean longHeshvan(int year) {
        return ((daysInYear(year) % 10) == 5);
    }

    static boolean shortKislev(int year) {
        return ((daysInYear(year) % 10) == 3);
    }

    static int
    lastDayOfMonth(int month, int year) {
// Last day of month in Hebrew year.

        if ((month == 2) || (month == 4) || (month == 6)
                || ((month == 8) && !(longHeshvan(year)))
                || ((month == 9) && shortKislev(year)) || (month == 10)
                || ((month == 12) && !(isLeapYear(year))) || (month == 13))
            return 29;
        else
            return 30;
    }

    String getMonthName() {
        String monthNames[] = {"Nisan", "Iyyar", "Sivan", "Tammuz", "Av",
                "Elul", "Tishri", "Cheshvan", "Kislev", "Tevet", "Shvat",
                "Adar I", "Adar II"
        };
        return monthNames[month - 1];
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}

