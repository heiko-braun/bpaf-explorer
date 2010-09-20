/* jboss.org */
package org.jboss.bpm.monitor.model.json;

import org.jboss.bpm.monitor.model.BPAFDataSource;
import org.jboss.bpm.monitor.model.DefaultBPAFDataSource;
import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.metric.Average;
import org.jboss.bpm.monitor.model.metric.Grouping;
import org.jboss.bpm.monitor.model.metric.Metrics;
import org.jboss.bpm.monitor.model.metric.Timespan;

import java.util.*;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 11, 2010
 */
public class JsonExport
{
  public static void main(String[] args)
  {
    BPAFDataSource ds = new DefaultBPAFDataSource();

    // definitions
    List<String> definitions = ds.getProcessDefinitions();
    System.out.println("Definitions: " +definitions);

    // instances
    String s0 = definitions.get(0);
    
    Timespan timespan = Timespan.last7Days();
    
    List<Event> events = ds.getDefinitionEvents(s0, timespan);

    System.out.println("Instances events for: "+timespan + ": "+events.size());
    
    Average avg = Metrics.getInstanceAverage(events);


    SortedMap<Date, List<Event>> grouped = groupEventsByUnit(events, timespan);

    /*System.out.println("--");
    for(Date d : grouped.keySet())
      System.out.println(d+": "+grouped.get(d).size());*/

    // ----------


    XYDataSetJSO dataSet = new XYDataSetJSO("Process Instances last 7 days", s0);

    List<Long> domainData = new ArrayList<Long>(grouped.size());
    List<Long> rangeData = new ArrayList<Long>(grouped.size());
    for(Date d : grouped.keySet())
    {
      domainData.add(d.getTime());
      rangeData.add(new Integer(grouped.get(d).size()).longValue());
    }

    dataSet.getDomain().add(domainData);
    dataSet.getRange().add(rangeData);
    dataSet.setAxis("time");
    dataSet.setRangeBottom(avg.getMin());
    dataSet.setRangeTop(avg.getMax());

    System.out.println(dataSet.toJSO());
  }

  private static SortedMap<Date, List<Event>> groupEventsByUnit(List<Event> events, Timespan timespan)
  {
    SortedMap<Date, List<Event>> result = null;
    switch (timespan.getUnit() )
    {
      case DAY:
          result = Grouping.byDay(events, timespan);
        break;
    }

    return result;
  }


}
