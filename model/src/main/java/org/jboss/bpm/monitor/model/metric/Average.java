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

/**
 * Min, Max, Average for a particular key.
 *
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 10, 2010
 */
public class Average
{
  String key;
  long min = -1;
  long max = -1;
  long avg = -1;

  public Average(String key)
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }

  public long getMin()
  {
    return min;
  }

  public void setMin(long min)
  {
    this.min = min;
  }

  public long getMax()
  {
    return max;
  }

  public void setMax(long max)
  {
    this.max = max;
  }

  public long getAvg()
  {
    return avg;
  }

  public void setAvg(long avg)
  {
    this.avg = avg;
  }

  @Override
  public String toString()
  {
    return "Average{" +
        "min=" + min +
        ", max=" + max +
        ", avg=" + avg +
        '}';
  }
}
