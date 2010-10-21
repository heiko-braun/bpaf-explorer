/* jboss.org */
package org.jboss.bpm.monitor.model.metric;

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
}
