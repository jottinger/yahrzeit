package com.enigmastation.yahrzeit;

import java.util.Calendar;

public class HebrewDate extends YahrzeitDate {

    final static int epoch = -1373429;    // Absolute date of start of Hebrew calendar

    public HebrewDate(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public HebrewDate(Calendar c) {
        initFromAbsoluteDate(toAbsolute(c));
    }

    public HebrewDate() {
        Calendar c = Calendar.getInstance();
        int absoluteDay = toAbsolute(c);
        initFromAbsoluteDate(absoluteDay);
    }

    @Override
    public String getMonthName() {
        String monthNames[] = {"Nisan", "Iyyar", "Sivan", "Tammuz", "Av",
                "Elul", "Tishri", "Cheshvan", "Kislev", "Tevet", "Shvat",
                "Adar I", "Adar II"
        };
        return monthNames[month - 1];
    }

    @Override
    public int toAbsolute() {
        int dayInYear = getDay();    // Days so far this month.
        if (getMonth() < 7) {   // Before Tishri, so add days in prior months
            // this year before and after Nisan.
            int m = 7;
            while (m <= (lastMonth(getYear()))) {
                dayInYear = dayInYear + lastDayOfMonth(m, getYear());
                m++;
            }
            m = 1;
            while (m < getMonth()) {
                dayInYear = dayInYear + lastDayOfMonth(m, getYear());
                m++;
            }
        } else {   // Add days in prior months this year
            int m = 7;
            while (m < getMonth()) {
                dayInYear = dayInYear + lastDayOfMonth(m, getYear());
                m++;
            }
        }
        return (dayInYear + (elapsedDays(getYear())    // Days in prior years.
                + epoch));    // Days elapsed before absolute date 1.
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
        while (d > new HebrewDate(getMonth(), (lastDayOfMonth(getMonth(), year)), year).toAbsolute())
            month++;
        // Calculate the day by subtraction.
        day = d - new HebrewDate(getMonth(), 1, year).toAbsolute() + 1;
    }

    static boolean isLeapYear(int year) {
        return (((7 * year) + 1) % 19) < 7;
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

}

