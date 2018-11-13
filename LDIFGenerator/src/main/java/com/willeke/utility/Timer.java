package com.willeke.utility;

/**
 * Title:        WILLEKE
 * Description:  Libraries that we create.
 * Copyright:    Copyright (c) 2001
 * Company:      WILLEKE.COM
 * @author Jim Willeke
 * @version 1.0
 */

public class Timer
{
  private static long t;
  public Timer()
  {
    reset();
  }

  // reset timer
  public void reset()
  {
    t = System.currentTimeMillis();
  }

  // return elapsed time
  public long elapsed()
  {
    return System.currentTimeMillis() - t;
  }

  public String getHMS( long msec )
  {
    String str = "";
    int timeInSeconds = ( int ) msec / 1000;
    int hours;
    int minutes;
    int seconds;
    hours = timeInSeconds / 3600;
    timeInSeconds = timeInSeconds - ( hours * 3600 );
    minutes = timeInSeconds / 60;
    timeInSeconds = timeInSeconds - ( minutes * 60 );
    seconds = timeInSeconds;
    str = seconds + " second(s)";
    if ( minutes > 0 || hours > 0 )
    {
      str = minutes + " minute(s) " + str;
    }
    if ( hours > 0 )
    {
      str = hours + " hour(s) " + str;
    }
    return str;
  }
}