/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import org.jboss.errai.bus.server.annotations.Remote;

import java.util.List;

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
}
