/* jboss.org */
package org.jboss.bpm.monitor.model.metric;

import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.metric.Timespan;

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
    static TimeZone UTC = TimeZone.getTimeZone("Etc/UTC");

    private static SortedMap<Date,List<Event>> _doSort(List<Event> events, Timespan timespan, int calMetric)
    {
        long s0 = System.currentTimeMillis();
        SortedMap<Date, List<Event>> slotCount = new TreeMap<Date, List<Event>>();

        // create  slots first
        final Calendar cal = Calendar.getInstance(UTC);
        cal.setTimeInMillis(timespan.getEnd());
        while(cal.getTimeInMillis()>timespan.getStart()) // reverse
        {
            slotCount.put(new Date(cal.getTimeInMillis()), new ArrayList<Event>()); // left inclusive
            cal.add(calMetric, -1); // previous slot
        }

        // sort events according to slots
        final Set<Date> startingDates = slotCount.keySet();
        Iterator<Date> rightBounds = startingDates.iterator();
        while(rightBounds.hasNext())
        {
            final Date to = rightBounds.next();
            cal.setTime(to);
            cal.add(calMetric, -1);
            final Date from = cal.getTime();

            for(Event e : events)
            {
                Date actual = new Date(e.getTimestamp());

                if(actual.after(from) && actual.before(to))
                {
                    slotCount.get(to).add(e);
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
