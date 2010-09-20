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
import org.jboss.bpm.monitor.model.metric.Timespan;

import java.util.List;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 10, 2010
 */
public interface BPAFDataSource
{
  /**
   * Get a list of distinct process definition ID's that are known to the system.
   * @return a collection of process definition ID's
   */
  List<String> getProcessDefinitions();

  /**
   * Get a list of distinct process instance ID's that are known to the system.
   * @return a collection of process instance ID's
   */

  List<String> getProcessInstances(String processDefinition);

  /**
   * Get a list of distinct activity definition ID's that are known to the system.
   * @return a collection of activity definition ID's
   */

  List<String> getActivityDefinitions(String processInstance);

  /**
   * Get a list of process definition events for a specific timespan.
   * Process definition events are the ones that don't have and activityDefinition
   * assigned to them. These events are typically used to indicated start and end of an
   * process instance lifecycle.
   * <p/>
   * NOTE: Parity is important <code>(events.size()%2==0)</code>.
   * Only return result sets that include matching State.Close events.
   * Otherwise you include instances that are still running.
   *
   * @param processDefinition the process definition ID
   * @param timespan a timespan (inclusive)   
   * @return List of <tt>State.Open</tt> and <tt>State.Closed</tt> events for a particluar process definition
   * excluding the activity events (<tt>activityDefinitionID is null</tt>)
   */
  List<Event> getDefinitionEvents(String processDefinition, Timespan timespan);


  /**
   * Get a list of process instance events for specific timespan.
   * Process instance event are the one that have an activity assigned to it.
   *
   * <p/>
   * NOTE: Parity is important <code>(events.size()%2==0)</code>.
   * Only return result sets that include matching State.Close events.
   * Otherwise you include activities that are still running.
   *
   * @param processInstance the process instance ID      
   * @return a list of instance activity events.
   */
  List<Event> getInstanceEvents(String... processInstance);
}
