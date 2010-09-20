/* jboss.org */
package org.jboss.bpm.monitor.emu;

import java.util.UUID;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
public class RefillStockProcess implements Process
{
  private final String correlationId;

  public enum Activity {
    None,
    CheckStock,
    PlaceOrder,
    VerifyReceit,
    NotifyWarehouse,    
    Ended
  };

  private Activity current = Activity.None;

  private long start;
  private long end;

  public RefillStockProcess()
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
        next = Activity.CheckStock;
        break;
      case CheckStock:
        next = Activity.PlaceOrder;
        break;
      case PlaceOrder:
        next = Activity.VerifyReceit;
        break;
      case VerifyReceit:
        next = Activity.NotifyWarehouse;
        break;
      case NotifyWarehouse:
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
    return "RefillStockProcess{" +
        "activity=" + current +
        '}';
  }
}