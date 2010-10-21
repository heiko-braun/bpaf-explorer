/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 30, 2010
 */
public enum TimespanValues
{
    LAST_24_HOURS   (UNIT.HOUR, "Last 24 Hours"),
    LAST_DAY        (UNIT.HOUR, "Last Day"),
    LAST_7_DAYS     (UNIT.DAY, "Last 7 Days"),
    LAST_WEEK       (UNIT.DAY, "Last Week"),
    LAST_4_WEEKS    (UNIT.DAY, "Last 4 Weeks"),
    LAST_MONTH      (UNIT.DAY, "Last Month"),
    LAST_3_MONTH    (UNIT.WEEK, "Last 3 Month"),
    LAST_QUARTER    (UNIT.WEEK, "Last Quarter"),
    LAST_12_MONTH   (UNIT.MONTH, "Last 12 Month"),
    LAST_YEAR       (UNIT.MONTH, "Last Year");

    public enum UNIT {HOUR, DAY, WEEK, MONTH, YEAR }

    private UNIT unit;
    private String cname;

    TimespanValues(UNIT unit, String name) {
        this.unit = unit;
        this.cname = name;
    }

    public String getCanonicalName() {
        return cname;
    }

    public UNIT getUnit() {
        return unit;
    }
}
