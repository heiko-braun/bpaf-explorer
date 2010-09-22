/* jboss.org */
package org.jboss.bpm.monitor.gui.client.comments;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 31, 2010
 */
public interface CommentEditCallback
{
  void onSaveComment(ChartComment comment);
  void onCancelComment(ChartComment comment);
  void onDeleteComment(ChartComment comment);
  void onHighlightComment(ChartComment comment);
}
