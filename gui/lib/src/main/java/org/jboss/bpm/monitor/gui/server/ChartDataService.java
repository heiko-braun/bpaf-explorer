/* jboss.org */
package org.jboss.bpm.monitor.gui.server;

import org.jboss.bpm.monitor.gui.client.ChartData;
import org.jboss.bpm.monitor.model.BPAFDataSource;
import org.jboss.bpm.monitor.model.DataSourceFactory;
import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.json.XYDataSetJSO;
import org.jboss.bpm.monitor.model.metric.Grouping;
import org.jboss.bpm.monitor.model.metric.Timespan;
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
     * Delegates to {@link org.jboss.bpm.monitor.model.BPAFDataSource#getDefinitionEvents(String, org.jboss.bpm.monitor.model.metric.Timespan)}
     * @param processDefiniton a processDefinitionID
     * @param timespanValue a timespan string identifier
     * @return JSON data
     */
    public String getDefinitionActivity(String processDefiniton, String timespanValue)
    {
        assertDataSource();

        final Timespan timespan = Timespan.fromValue(timespanValue);

        List<Event> events = dataSource.getDefinitionEvents(processDefiniton, timespan);

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

        XYDataSetJSO dataSet = new XYDataSetJSO(
                "Process Instances "+timespan.getTitle(),
                UUID.randomUUID().toString()
        );

        List<Long> domainData = new ArrayList<Long>(grouped.size());
        List<Long> rangeData = new ArrayList<Long>(grouped.size());
        for(Date d : grouped.keySet())
        {
            domainData.add(d.getTime());
            rangeData.add(new Integer(grouped.get(d).size()).longValue());
        }

        dataSet.getDomain().add(domainData);
        dataSet.getRange().add(rangeData);
        dataSet.setAxis("date");

        return dataSet.toJSO();
    }

    private void assertDataSource() {
        if(null==this.dataSource)
            throw new IllegalStateException("BPAFDataSource not initialized");

    }
}
