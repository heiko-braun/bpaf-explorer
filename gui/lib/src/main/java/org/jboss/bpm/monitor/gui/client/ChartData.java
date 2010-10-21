/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import org.jboss.errai.bus.server.annotations.Remote;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
@Remote
public interface ChartData
{
    String getCompletedInstances(String processDefiniton, String timespanValue);

    String getFailedInstances(String processDefiniton, String timespanValue);

    String getTerminatedInstances(String processDefiniton, String timespanValue);
}
