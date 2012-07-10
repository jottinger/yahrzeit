package com.enigmastation.yahrzeit;

import com.beust.jcommander.Parameter;
import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Yahrzeit {
    final DateTime now = new DateTime();

    @Parameter(names = {"-s", "--since"})
    private boolean since = false;

    @Parameter(names = {"-o", "--output"})
    private boolean outputToday = true;

    @Parameter(names = {"-h", "--hebrew"})
    private boolean showHebrew = true;

    @Parameter(names = {"-i", "--islamic"})
    private boolean showIslamic = false;

    @Parameter(names = {"-g", "--gregorian"})
    private boolean showGregorian = false;

    public void run() {
        // we need to calculate today's date in Hebrew...
        HebrewDate hebrew = new HebrewDate();
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        DateTime islamic = new DateTime(islamicChronology);
        DateTime gregorian = new DateTime();
        List<String> dateMatches = new ArrayList<>();

        dateMatches.add(hebrew.getMonthName() + " " + hebrew.getDay());

        DateTimeFormatter monthDayFormatter = DateTimeFormat.forPattern("MMMM d");
        DateTimeFormatter monthDayYearFormatter = DateTimeFormat.forPattern("MMMM d, yyyy");
        String[] islamicMonthNames = {"Muharram", "Safar", "Rabi I", "Rabi II," +
                "Jumada I", "Jumada II", "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhu'l Qa'dah", "Dhu'l-Hijja"};
        dateMatches.add(islamicMonthNames[islamic.getMonthOfYear()] + " " + islamic.getDayOfMonth());
        dateMatches.add(monthDayFormatter.print(gregorian));
        if (outputToday) {
            if (showHebrew) {
                System.out.printf("%s %d, %d%n", hebrew.getMonthName(), hebrew.getDay(), hebrew.getYear());
            }
            if (showIslamic) {
                System.out.printf("%s %d, %d%n", islamicMonthNames[islamic.getMonthOfYear()],
                        islamic.getDayOfMonth(), islamic.getYear());
            }
            if (showGregorian) {
                System.out.println(monthDayYearFormatter.print(gregorian));
            }
        }
        File calendarFile=new File(System.getProperty("user.home")+System.getProperty("file.separator")+".calendar");
        try {
            FileReader fr=new FileReader(calendarFile);
            BufferedReader br=new BufferedReader(fr);
            Scanner scanner=new Scanner(br);
            String input;
            while(scanner.hasNextLine()) {
                input=scanner.nextLine();
                for(String date:dateMatches) {
                    if(input.trim().startsWith(date)) {
                        System.out.println(input.substring(date.length()+1).trim());
                    }
                }
            }
        } catch (FileNotFoundException ignored) {
            // this is okay, we just don't output anything if the file doesn't exist
        }

    }

    public static void main(String[] args) {
        Yahrzeit y = new Yahrzeit();
        y.run();
    }
}
