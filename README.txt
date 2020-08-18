This is a program that reads in the 14 .txt files to create a schedule. This is specifically created for school, so it's split into fall and winter by day.

By default it will only read in values from 8:30am to 10pm, although if you change the constants in the Rects.java file you can alter the range of time.

The .txt files follow a very strict format. I haven't implemented error checking yet so make sure you follow the format well.

Line 1 must be an integer that represents how many events occur on that day.

All other lines follow the format
Event;Start time;End Time
The start and end times must be in 24 hour notation, and should not have excess whitespace.

An example of a valid file would be
----------
4
Statistics;10:00;17:30
Joint Probability;7:00;9:00
Risk Analysis;9:10;9:50
Some Other Task;17:30;20:00
----------

Notice that the events do not need to be in order, and the event names can have spaces in them.


DOWNLOAD INSTRUCTIONS
----------------------
To run the Schedule.jar and img folder must be in the same directory. The img folder must be called 'img' and none of the .txt files may be renamed.