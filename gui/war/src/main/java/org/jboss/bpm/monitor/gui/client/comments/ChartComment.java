/* jboss.org */
package org.jboss.bpm.monitor.gui.client.comments;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 31, 2010
 */
public class ChartComment
{
  long id = 0;

  double domainValue;
  String title;

  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  public double getDomainValue()
  {
    return domainValue;
  }

  public void setDomainValue(double domainValue)
  {
    this.domainValue = domainValue;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }
}
