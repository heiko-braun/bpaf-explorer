/* jboss.org */
package org.jboss.bpm.monitor.gui.server;

import org.jboss.bpm.monitor.gui.client.ChartData;
import org.jboss.bpm.monitor.model.BPAFDataSource;
import org.jboss.bpm.monitor.model.DataSourceFactory;
import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.bpaf.State;
import org.jboss.bpm.monitor.model.json.XYDataSetJSO;
import org.jboss.bpm.monitor.model.metric.Grouping;
import org.jboss.bpm.monitor.model.metric.Timespan;
import org.jboss.bpm.monitor.model.metric.TimespanFactory;
import org.jboss.errai.bus.server.annotations.Service;

import java.util.*;

/**
 * Turn history records into JSON data that can be consumed by the chart renderer.
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
@Service
public class ChartDataService implements ChartData
{
    private BPAFDataSource dataSource;

    public ChartDataService()
    {
        this.dataSource = DataSourceFactory.createDataSource();
    }

    /**
     * @param processDefiniton a processDefinitionID
     * @param timespanValue a timespan string identifier
     * @return JSON data
     */
    public String getCompletedInstances(String processDefiniton, String timespanValue)
    {
        assertDataSource();

        final Timespan timespan = TimespanFactory.fromValue(timespanValue);

        List<Event> events = dataSource.getInstanceEvents(processDefiniton, timespan, State.Closed_Completed);

        return createDatasetJSO(
                new String[]{"Completed"}, timespan, true, events);
    }

    /**
     * @param processDefiniton a processDefinitionID
     * @param timespanValue a timespan string identifier
     * @return JSON data
     */
    public String getFailedInstances(String processDefiniton, String timespanValue)
    {
        assertDataSource();

        final Timespan timespan = TimespanFactory.fromValue(timespanValue);

        List<Event> completed = dataSource.getInstanceEvents(processDefiniton, timespan, State.Closed_Completed);
        List<Event> failed  = dataSource.getInstanceEvents(processDefiniton, timespan, State.Closed_Completed_Failed);
        List<Event> terminated = dataSource.getInstanceEvents(processDefiniton, timespan, State.Closed_Cancelled_Terminated);

        return createDatasetJSO(
                new String[]{"Completed", "Failed", "Terminated"}, timespan, true, completed, failed, terminated);
    }
  
    private static String createDatasetJSO(String[] title, Timespan timespan, boolean matchParity, List<Event>... events) {

        XYDataSetJSO dataSet = new XYDataSetJSO(
                title,
                UUID.randomUUID().toString()
        );

        for(List<Event> subset : events)
        {

            SortedMap<Date, List<Event>> grouped = group(timespan, subset);

            List<Long> domainData = new ArrayList<Long>(grouped.size());
            List<Long> rangeData = new ArrayList<Long>(grouped.size());
            for(Date d : grouped.keySet())
            {
                domainData.add(d.getTime());

                // if parity matched datasets, then we get Open and Closed events.
                int actualSize = matchParity ? grouped.get(d).size()/2 : grouped.get(d).size();
                rangeData.add(new Integer(actualSize).longValue());
            }

            dataSet.getDomain().add(domainData);
            dataSet.getRange().add(rangeData);
        }

        dataSet.setAxis("date");

        return dataSet.toJSO();
    }

    private static SortedMap<Date, List<Event>> group(Timespan timespan, List<Event> events) {
        SortedMap<Date, List<Event>> grouped;

        switch (timespan.getUnit())
        {
            case HOUR:
                grouped = Grouping.byHour(events, timespan);
                break;
            case DAY:
                grouped = Grouping.byDay(events, timespan);
                break;
            case WEEK:
                grouped = Grouping.byWeek(events, timespan);
                break;
            case MONTH:
                grouped = Grouping.byMonth(events, timespan);
                break;
            default:
                throw new IllegalArgumentException("UNIT not supported: "+timespan.getUnit());

        }
        return grouped;
    }

    private void assertDataSource() {
        if(null==this.dataSource)
            throw new IllegalStateException("BPAFDataSource not initialized");

    }
}
