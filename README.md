This is a simple Java line application to output today's date. "Yahrzeit" is
"time of year" in Yiddish, used to remember important dates; I added support
for a feature to output events on a given day largely for the purposes of
the actual yahrzeit.

To use the remembrance feature, create a readable file called ~/.calendar.

~/.calendar has a series of lines in it, with the format:

   date,description

An example .calendar might be:

   Tammuz 20,Birthday for Sam Hill
   July 17,Anniversary
   Ramadan 4,Birthday, Ali

If the date matches the current day in the Hebrew, Gregorian, or Islamic
calendars, the description is output.

Note that the spelling of the Hebrew months is according to the Hebrew
Academy; thus, "Tammuz" is used (as opposed to "Tamuz"), although I used
Cheshvan instead of Marhesvan as the Academy recommends.

(I was using Marhesvan, but then realized that when I thought of it, it was
as "Cheshvan." A TODO is to make it so month names are customizable such
that you can use whichever spellings you prefer instead of relying on my
inconsistent and non-normalized transliterations.)

For Adar, the number is included; thus, "Adar 5" will not match, but "Adar I
5" will.

Note that for events in Adar II, matching will be inconsistent, because Adar
II is intercalatory; it's not present in every year.  The matching algorithm
is brute-force, not an actual matching algorithm based on calendar dates.

The algorithms are taken from
http://people.sc.fsu.edu/~jburkardt/cpp_src/calendar_rd/calendar_rd.cpp. 
The date support was originally taken from Joda-time, but this created a
large executable jar file, and using my own date classes was shorter (and
more clear).  (I also didn't want to write a Hebrew chronology for Joda-time
or Threeten, because I don't think I understand what the chronologies need
to support.) 