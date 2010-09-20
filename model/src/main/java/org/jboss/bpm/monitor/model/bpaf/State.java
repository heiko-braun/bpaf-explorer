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
package org.jboss.bpm.monitor.model.bpaf;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * BPAF relies on a single state model that is based on both the Wf-XML state model
 * and the BPEL4People/WS-HumanTask state model.
 * 
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 5, 2010
 */
@XmlRootElement(namespace = "http://www.wfmc.org/2009/BPAF2.0", name="State")
@XmlJavaTypeAdapter(StateAdapter.class)
public enum State
{
  /**
   * An activity or process instance in the state OPEN can change state.
   */
  Open,

  /**
   * No active work is being performed on the process in this state.
   * Time spent in this state would be recorded as idle time or wait time.
   */
  Open_NotRunning,

  /**
   * The process/activity is ready to be processed
   * but has not been assigned to a particular participant.
   */
  Open_NotRunning_Ready,

  /**
   * The process/activity has been assigned to a role (potentially a group of participants),
   * but work has not started yet.
   */
  Open_NotRunning_Assigned,

  /**
   * The process/activity has been assigned to a named user (a single participant),
   * but work has not started yet.
   */
  Open_NotRunning_Reserved,

  /**
   * The process/activity has been moved into a hibernation state.
   * Execution has not yet begun at this point,
   * i.e. the process/activity cannot be executed from this state
   * but has to be reactivated before execution can begin.
   */
  Open_NotRunning_Suspended,

  /**
   * The process/activity has been moved
   * into a hibernation state after its execution has begun.
   */
  Open_NotRunning_Suspended_Assigned,
  Open_NotRunning_Suspended_Reserved,

  /**
   * The process is actively being worked on.
   * Time spent in this state would be recorded as processing time or work time.
   */
  Open_Running,
  Open_Running_InProgress,

  /**
   * The process/activity has been moved
   * into a hibernation state after its execution has begun.
   */
  Open_Running_Suspended,

  /**
   * An activity or process instance in the state CLOSED can no longer change state.
   */
  Closed,

  /**
   * The process has been fully executed.
   * Processes in this state may or may not have achieved their objective.
   */
  Closed_Completed,

  /**
   * The process/activity has completed as planned and has achieved its objectives.
   */
  Closed_Completed_Success,

  /**
   * The process/activity has completed as planned but has not achieved its objectives.
   */
  Closed_Completed_Failed,

  /**
   * The process has been forcefully terminated.
   * Processes in this state have not achieved their objective.
   */
  Closed_Cancelled,

  /**
   * The process/activity has ended because it was manually exited prior to its completion
   */
  Closed_Cancelled_Exited,

  /**
   * The process/activity has ended due to an error in the execution.
   */
  Closed_Cancelled_Error,

  /**
   * The process/activity has ended because it has been superseded by a more recent version.
   * This might occur if a process instance is migrated to a newer schema during the course of its execution and an activity that was part only
   * of the old schema has become ready for execution but has not been executed yet.
   */
  Closed_Cancelled_Obsolete,

  /**
   * The process/activity has been forcibly but gracefully ended, i.e. running activities and subprocesses were allowed
   * to complete as scheduled before the end of the process instance.
   */
  Closed_Cancelled_Aborted,

  /**
   * The process/activity has been forcibly ended, i.e. running activities
   * and subprocesses were terminated before their scheduled completion.
   */
  Closed_Cancelled_Terminated
}
