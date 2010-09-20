/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 30, 2010
 */
public interface TimespanValues
{
  public static final String LAST_24_HOURS =  "Last 24 Hours";
  public static final String LAST_DAY =       "Last Day";
  public static final String LAST_7_DAYS =    "Last 7 Days";
  public static final String LAST_WEEK =      "Last Week";
  public static final String LAST_4_WEEKS =   "Last 4 Weeks";
  public static final String LAST_MONTH =     "Last Month";
  public static final String LAST_3_MONTH =   "Last 3 Month";
  public static final String LAST_QUARTER =   "Last Quarter";
  public static final String LAST_12_MONTH =  "Last 12 Month";
  public static final String LAST_YEAR =      "Last Year";

  public static String[] ALL = new String[] {
      LAST_24_HOURS, LAST_DAY,
      LAST_7_DAYS, LAST_WEEK,
      LAST_4_WEEKS, LAST_MONTH,
      LAST_3_MONTH, LAST_QUARTER,
      LAST_12_MONTH, LAST_YEAR
  };
}
