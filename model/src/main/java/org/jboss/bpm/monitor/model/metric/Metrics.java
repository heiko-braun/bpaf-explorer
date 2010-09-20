/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.bpm.monitor.model.metric;

import org.jboss.bpm.monitor.model.bpaf.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 10, 2010
 */
public class Metrics
{
  /**
   * Get the average instance execution time.
   * @param events a list of events that belong to the same process definition
   * (<tt>e0.processDefinitionID==e1.processDefinitionID, ..., eN</tt>)
   * @return average instance execution time
   */
  public static Average getInstanceAverage(List<Event> events)
  {
    assert events.size()>0 : "Empty event list";
    assert events.size()%2==0 : "Parity error: "+events.size() +" events. Every event should have closing counterpart!";

    Average stat = new Average(events.get(0).getProcessDefinitionID());

    Map<String, Long> duration = new HashMap<String,Long>(events.size()/2);
    for(Event e : events)
    {
      String processInstance = e.getProcessInstanceID();
      Long l0 = duration.get(processInstance);
      if(l0!=null)
      {
        long l1 = e.getTimestamp();
        if(l0<l1)
          duration.put(processInstance, (l1-l0));   // not necessarily in order
        else
          duration.put(processInstance, (l0-l1));
      }
      else
      {
        duration.put(processInstance, e.getTimestamp());
      }
    }

    long sum = 0;
    for(String processInstance : duration.keySet())
    {
      long l3 = duration.get(processInstance);
      if(stat.getMin()==-1) // first iteration
        stat.setMin(l3);
      else if(l3<stat.getMin())
        stat.setMin(l3);
      else if(l3>stat.getMax())
        stat.setMax(l3);

      sum+=l3;
    }

    stat.setAvg(sum/duration.entrySet().size());

    return stat;
  }

  /**
   * Get the average execution time for an activity.
   * @param events a list of event that belong to the same process definition
   * (<tt>e0.processDefinitionID==e1.processDefinitionID, ..., eN</tt>)
   * @param activityDefinition the activityDefinitionID
   * @return an average
   */
  public static Average getActivityAverage(List<Event> events, String activityDefinition)
  {
    assert events.size()>0 : "Empty event list";
    assert events.size()%2==0 : "Parity error: "+events.size() +" events. Every event should have closing counterpart!";

    Average stat = new Average(activityDefinition);

    Map<String, Long> in = new HashMap<String,Long>(events.size()/2);
    List<Long> out = new ArrayList<Long>();
    for(Event e : events)
    {
      String activity = e.getActivityDefinitionID();

      if(!activity.equals(activityDefinition)) // skip non related events
        continue;
      
      Long l0 = in.get(activity);
      if(l0!=null)
      {
        long l1 = e.getTimestamp();
        if(l0<l1)
          out.add(l1-l0);   // not necessarily in order
        else
          out.add(l0-l1);
      }
      else
      {
        in.put(activity, e.getTimestamp());
      }
    }

    long sum = 0;
    for(Long l3: out)
    {      
      if(stat.getMin()==-1) // first iteration
      {
        stat.setMin(l3);
        stat.setMax(l3);
      }
      else if(l3<stat.getMin())
        stat.setMin(l3);
      else if(l3>stat.getMax())
        stat.setMax(l3);

      sum+=l3;
    }

    stat.setAvg(sum/out.size());

    return stat;
  }
}
