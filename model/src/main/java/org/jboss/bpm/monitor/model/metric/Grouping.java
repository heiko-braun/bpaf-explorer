/* jboss.org */
package org.jboss.bpm.monitor.model.metric;

import org.jboss.bpm.monitor.model.bpaf.Event;

import java.util.*;

/**
 * Grouping algorithms. Non-optimized.<br>
 * These may show bad execution times when being used on large scale data sets.
 *
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
public class Grouping
{
  private static SortedMap<Date,List<Event>> _doSort(List<Event> events, Timespan timespan, int calMetric)
  {
    long s0 = System.currentTimeMillis();
    SortedMap<Date, List<Event>> slotCount = new TreeMap<Date, List<Event>>();

    // create  slots first
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timespan.getStart());
    while(cal.getTimeInMillis()<timespan.getEnd())
    {
      slotCount.put(new Date(cal.getTimeInMillis()), new ArrayList<Event>()); // left inclusive
      cal.add(calMetric, 1);
    }

    // sort events according to slots
    final Set<Date> startingDates = slotCount.keySet();
    Iterator<Date> leftOffset = startingDates.iterator();
    while(leftOffset.hasNext())
    {
      Date from = leftOffset.next();

      for(Event e : events)
      {
        cal.setTime(from);
        cal.add(calMetric, 1);
        Date to = cal.getTime();

        Date actual = new Date(e.getTimestamp());

        if(actual.after(from) && actual.before(to))
        {
          slotCount.get(from).add(e);
        }

      }
    }

    //System.out.println("exectime: "+(System.currentTimeMillis()-s0)+ " ms");
    return slotCount;
  }

  public static SortedMap<Date, List<Event>> byHour(List<Event> events, Timespan timespan)
  {
    return _doSort(events, timespan, Calendar.HOUR_OF_DAY);
  }

  public static SortedMap<Date, List<Event>> byDay(List<Event> events, Timespan timespan)
  {
    return _doSort(events, timespan, Calendar.DAY_OF_YEAR);
  }

  public static SortedMap<Date, List<Event>> byWeek(List<Event> events, Timespan timespan)
  {
    return _doSort(events, timespan, Calendar.WEEK_OF_YEAR);
  }

  public static SortedMap<Date, List<Event>> byMonth(List<Event> events, Timespan timespan)
  {
    return _doSort(events, timespan, Calendar.MONTH);
  }

}
