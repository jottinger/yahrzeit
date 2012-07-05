This is a simple command line application to output today's date.

It will attempt to read ~/.calendar as well. 

~/.calendar has a series of lines in it, with the format:

   date,description

If the date matches the current day in either of the Hebrew or Gregorian 
calendars, the description is output.

Note that the spelling of the Hebrew months is according to the Hebrew Academy;
thus, "Tammuz" is used (as opposed to "Tamuz"), although I used Cheshvan instead 
of Marhesvan as the Academy recommends.

(I was using Marhesvan, but then realized that when I thought of it, it was 
as "Cheshvan." A TODO is to make it so month names are customizable such that
you can use whichever spellings you prefer instead of relying on my inconsistent and
non-normalized transliterations.)

For Adar, the number is included; thus, "Adar 5" will not match, but "Adar I 5" 
will.

Note that for events in Adar II, matching will be inconsistent, because Adar II
is intercalatory; it's not present in every year. The matching algorithm is
brute-force, not an actual matching algorithm based on calendar dates.

It is taken nearly verbatim from
http://people.sc.fsu.edu/~jburkardt/cpp_src/calendar_rd/calendar_rd.cpp ;
I added the suport for ~/.calendar (because I wanted to track 
yahrzeit for something) but most of the actual calendar code is verbatim.
 