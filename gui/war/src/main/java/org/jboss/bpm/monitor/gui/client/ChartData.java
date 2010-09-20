/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import org.jboss.bpm.monitor.model.metric.Timespan;
import org.jboss.errai.bus.server.annotations.Remote;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
@Remote
public interface ChartData
{
  String getDefinitionActivity(String processDefiniton, String timespanValue);  
}
