This is a simple command line application to output today's date.

It will attempt to read ~/.calendar as well. 

~/.calendar has a series of lines in it, with the format:

   date,description

If the date matches the current day in either of the Hebrew or Gregorian 
calendars, the description is output.

Note that the spelling of the Hebrew months is according to the Hebrew Academy;
thus, "Tammuz" is used (as opposed to "Tamuz"), although Marhesvan is used
such that chet is rendered as "h" and pronounced as "kh".

For Adar, the number is included; thus, "Adar 5" will not match, but "Adar I 5" 
will.

It is taken nearly verbatim from
http://people.sc.fsu.edu/~jburkardt/cpp_src/calendar_rd/calendar_rd.cpp ;
all I've done is focused in on the Hebrew date.
 