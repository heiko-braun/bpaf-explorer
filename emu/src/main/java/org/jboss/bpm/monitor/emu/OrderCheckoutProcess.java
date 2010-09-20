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

import java.util.UUID;

public class OrderCheckoutProcess implements Process
{
  private final String correlationId;

  public enum Activity {
    None,
    VerifyAvailability,
    RequestBillingDetails,
    ProcessBilling,
    ShipProduct,
    Delivered,
    Ended
  };

  private Activity current = Activity.None;

  private long start;
  private long end;

  public OrderCheckoutProcess()
  {
    this.correlationId = UUID.randomUUID().toString(); 
  }

  public String getName()
  {
    return getClass().getSimpleName(); 
  }

  public long getStarted()
  {
    return start;
  }

  public long getEnded()
  {
    return end;
  }

  public String getInstanceId()
  {
    return this.correlationId;
  }

  public String getActivity()
  {
    return current.toString(); 
  }

  public String invoke()
  {
    Activity next = null;

    switch (current)
    {
      case None:
        start = System.currentTimeMillis();
        next = Activity.VerifyAvailability;
        break;
      case VerifyAvailability:
        next = Activity.RequestBillingDetails;
        break;
      case RequestBillingDetails:
        next = Activity.ProcessBilling;
        break;
      case ProcessBilling:
        next = Activity.ShipProduct;
        break;
      case ShipProduct:
        next = Activity.Delivered;
        break;
      case Delivered:
        next = Activity.Ended;
        end = System.currentTimeMillis();
        break;
      default:
        throw new IllegalStateException("Unknown state "+ current);
    }

    current = next;
    return current.toString();
  }

  public boolean isActive()
  {
    return (current!=Activity.Ended && current!=Activity.None);
  }

  public long getExectime()
  {
    if(0==start)
      throw new IllegalArgumentException("No start time recorded");
    
    return end -  start;
  }

  @Override
  public String toString()
  {
    return "OrderCheckoutProcess{" +
        "activity=" + current +
        '}';
  }
}
