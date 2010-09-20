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
package org.jboss.bpm.monitor.emu;

import org.jboss.bpm.monitor.model.bpaf.Event;
import org.jboss.bpm.monitor.model.bpaf.State;

import java.util.Random;

public class ProcessRunnable implements Runnable
{
  private Process process;  
  private boolean active = true;
  EventHandler handler;

  public ProcessRunnable(Process process, EventHandler handler)
  {
    this.process = process;
    this.handler = handler;
  }

  public void run()
  {
    while(active)
    {
      try
      {
        // lifecycle start event
        if(process.getActivity().equalsIgnoreCase("none"))
        {
          Event startEvent = new Event(true);
          startEvent.setProcessName(process.getName());
          startEvent.setProcessDefinitionID(process.getName());
          startEvent.setProcessInstanceID(process.getInstanceId());
          startEvent.getEventDetails().setCurrentState(State.Open);

          handler.processEvent(startEvent);
        }

        // activity events

        long startedAt = System.currentTimeMillis();
        String actitivity = invokeActivity();
        long duration = System.currentTimeMillis()-startedAt;

        System.out.println(process.getName() + ": "+actitivity +" ("+duration+" ms)");

        handler.processEvent(createActivityEvent(actitivity, State.Open, startedAt));
        handler.processEvent(createActivityEvent(actitivity, State.Closed));

        active = process.isActive();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    System.out.println("Terminate " + process + "("+process.getExectime()+" ms)");

    // lifecycle end event
    Event endEvent = new Event(true);
    endEvent.setProcessDefinitionID(process.getName());
    endEvent.setProcessInstanceID(process.getInstanceId());
    endEvent.setProcessName(process.getName());
    endEvent.getEventDetails().setCurrentState(State.Closed);

    handler.processEvent(endEvent);
  }

  private Event createActivityEvent(String actitivity, State state)
  {
    Event event = new Event(true);

    event.setProcessDefinitionID(process.getName());
    event.setProcessInstanceID(process.getInstanceId());
    event.setProcessName(process.getName());

    event.setActivityDefinitionID(actitivity);
    event.setActivityInstanceID(actitivity+"_"+System.currentTimeMillis());

    event.getEventDetails().setCurrentState(state);
    return event;
  }

  private Event createActivityEvent(String actitivity, State state, long timestamp)
  {
    Event event = createActivityEvent(actitivity, state);
    event.setTimestamp(timestamp);       
    return event;
  }

  private String invokeActivity()
      throws InterruptedException
  {
    String activity = process.invoke();

    Random randomGenerator = new Random();
    int randomSleep = randomIntRange(500, 1200, randomGenerator);
    Thread.sleep(randomSleep);
    return activity;
  }

  private static int randomIntRange(int aStart, int aEnd, Random aRandom)
  {
    if ( aStart > aEnd )
      throw new IllegalArgumentException("Start cannot exceed End.");

    //get the range, casting to long to avoid overflow problems
    long range = (long)aEnd - (long)aStart + 1;

    // compute a fraction of the range, 0 <= frac < range
    long fraction = (long)(range * aRandom.nextDouble());
    int randomNumber =  (int)(fraction + aStart);

    return randomNumber;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }
}
