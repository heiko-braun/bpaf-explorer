/* jboss.org */
package org.jboss.bpm.monitor.model.metric;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a certain time period.
 * Includes factory methods to create common timespans.
 *
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 10, 2010
 */
public final class Timespan
{
  private long start;
  private long end;
  
  public static final String LAST_24_HOURS =  "Last 24 Hours";
  public static final String LAST_DAY =       "Last Day";
  public static final String LAST_7_DAYS =    "Last 7 Days";
  public static final String LAST_WEEK =      "Last Week";
  public static final String LAST_4_WEEKS =   "Last 4 Weeks";
  public static final String LAST_MONTH =     "Last Month";
  public static final String LAST_3_MONTH =   "Last 3 Month";
  public static final String LAST_QUARTER =   "Last Quarter";
  public static final String LAST_12_MONTH =  "Last 12 Month";
  public static final String LAST_YEAR =      "Last Year";

  public enum UNIT {HOUR, DAY, WEEK, MONTH, YEAR }

  private UNIT unit;
  
  private String title;

  public Timespan(long start, long end, UNIT unit, String title)
  {
    this.start = start;
    this.end = end;
    this.unit = unit;
    this.title = title;
  }

  public long getStart()
  {
    return start;
  }

  public long getEnd()
  {
    return end;
  }

  public UNIT getUnit()
  {
    return unit;
  }

  public String getTitle()
  {
    return title;
  }

  public String toString()
  {
    return "Timespan{ "+new Date(start)+" - "+new Date(end)+" }";
  }

  // ----------------------------------------

  public static Timespan last24Hours()
  {
    Calendar cal = Calendar.getInstance();
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.DAY_OF_YEAR, -1);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.HOUR, LAST_24_HOURS);
  }

  public static Timespan lastDay()
  {
    Calendar cal = Calendar.getInstance();

    cal.add(Calendar.DAY_OF_YEAR, -1);
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    long t1 = cal.getTimeInMillis();

    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.HOUR, LAST_DAY);
  }

  public static Timespan last7Days()
  {
    Calendar cal = Calendar.getInstance();
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.DAY_OF_YEAR, -7);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.DAY, LAST_7_DAYS);
  }

  public static Timespan lastWeek()
  {
    Calendar cal = Calendar.getInstance();

    cal.add(Calendar.WEEK_OF_YEAR, -1);

    cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    long t1 = cal.getTimeInMillis();

    cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.DAY, LAST_WEEK);
  }

  public static Timespan last4Weeks()
  {
    Calendar cal = Calendar.getInstance();
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.WEEK_OF_YEAR, -4);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.DAY, LAST_4_WEEKS);
  }

  public static Timespan lastMonth()
  {
    Calendar cal = Calendar.getInstance();

    cal.add(Calendar.MONTH, -1);

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    long t1 = cal.getTimeInMillis();

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.DAY, LAST_MONTH);
  }


  public static Timespan last3Month()
  {
    Calendar cal = Calendar.getInstance();
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.MONTH, -3);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.WEEK, LAST_3_MONTH);
  }

  public static Timespan lastQuarter()
  {
    Calendar cal = Calendar.getInstance();

    long currentMonth = cal.get(Calendar.MONTH);

    if(currentMonth>=1 && currentMonth<=3)
      cal.set(Calendar.MONTH, Calendar.MARCH);
    else if(currentMonth>=4 && currentMonth<=6)
      cal.set(Calendar.MONTH, Calendar.JUNE);
    else if(currentMonth>=7 && currentMonth<=9)
      cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
    else
      cal.set(Calendar.MONTH, Calendar.DECEMBER);

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.MONTH, -2);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.WEEK, LAST_QUARTER);
  }


  public static Timespan last12Month()
  {
    Calendar cal = Calendar.getInstance();
    long t1 = cal.getTimeInMillis();

    cal.add(Calendar.YEAR, -1);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.MONTH, LAST_12_MONTH);
  }

  public static Timespan lastYear()
  {
    Calendar cal = Calendar.getInstance();

    cal.add(Calendar.YEAR, -1);

    cal.set(Calendar.MONTH, Calendar.DECEMBER);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    long t1 = cal.getTimeInMillis();

    cal.set(Calendar.MONTH, Calendar.JANUARY);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    long t0 = cal.getTimeInMillis();

    return new Timespan(t0, t1, UNIT.MONTH, LAST_YEAR);
  }

  public static Timespan fromValue(String value)
  {   
    Timespan t = null;

    if(LAST_24_HOURS.equals(value))
      t = last24Hours();
    else if(LAST_DAY.equals(value))
      t =lastDay();
    else if(LAST_7_DAYS.equals(value))
      t =last7Days();
    else if(LAST_WEEK.equals(value))
      t =lastWeek();
    else if(LAST_4_WEEKS.equals(value))
      t =last4Weeks();
    else if(LAST_MONTH.equals(value))
      t =lastMonth();
    else if(LAST_3_MONTH.equals(value))
      t =last3Month();
    else if(LAST_QUARTER.equals(value))
      t =lastQuarter();
    else if(LAST_12_MONTH.equals(value))
      t =last12Month();
    else if(LAST_YEAR.equals(value))
      t =lastYear(); 

    if(null==t)
      throw new IllegalArgumentException("Unknown value: "+value);
    
    return t;
  }
}
