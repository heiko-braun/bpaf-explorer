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
package org.jboss.bpm.monitor.model;

import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.metric.Metrics;
import org.jboss.bpm.monitor.model.metric.Timespan;

import java.util.List;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 10, 2010
 */
public class Runner
{
  public static void main(String[] args)
  {
    BPAFDataSource ds = new DefaultBPAFDataSource();

    // definitions
    List<String> definitions = ds.getProcessDefinitions();
    System.out.println("Definitions: " +definitions);

    // instances
    String s0 = definitions.get(0);
    List<String> instances = ds.getProcessInstances(s0);
    System.out.println("Instances for "+s0 +": "+instances.size());

    // activities
    String s1 = instances.get(0);
    List<String> activities = ds.getActivityDefinitions(s1);
    System.out.println("Distinct activities: ");
    for(String s : activities) System.out.println("- "+s);


    // definition events
    Timespan timespan = Timespan.lastQuarter();
    System.out.println(timespan);
    List<Event> instanceHistory = ds.getDefinitionEvents(definitions.get(0), timespan);
    System.out.println(Metrics.getInstanceAverage(instanceHistory));

    // instance events
    List<Event> activityHistory = ds.getInstanceEvents(instances.toArray(new String[] {}));
    System.out.println("Instance events: "+activityHistory.size());

    for(String s : activities)
    {
      System.out.println("- "+s+": " + Metrics.getActivityAverage(activityHistory, s));
    }
  }
}
