/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import org.jboss.errai.bus.server.annotations.Remote;

import java.util.List;
import java.util.Set;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 11, 2010
 */
@Remote
public interface HistoryRecords
{
    List<String> getProcessDefinitionKeys();

    List<String> getProcessInstanceKeys(String definitionKey);

    List<String> getActivityKeys(String instanceKey);

    Set<String> getCompletedInstances(String definitionKey, long timestamp, String timespan);

    Set<String> getTerminatedInstances(String definitionKey, long timestamp, String timespan);

    Set<String> getFailedInstances(String definitionKey, long timestamp, String timespan);

}
