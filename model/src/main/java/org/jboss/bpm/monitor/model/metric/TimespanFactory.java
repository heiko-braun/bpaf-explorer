/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.bpm.monitor.model.metric;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Oct 19, 2010
 */
public class TimespanFactory {

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

    public static Timespan last24Hours()
    {
        Calendar cal = Calendar.getInstance();
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.DAY_OF_YEAR, -1);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.HOUR, LAST_24_HOURS);
    }

    public static Timespan lastDay()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        
        DayBounds dayBounds = new DayBounds(cal).invoke();
        long t0 = dayBounds.getT0();
        long t1 = dayBounds.getT1();

        return new Timespan(t0, t1, Timespan.UNIT.HOUR, LAST_DAY);
    }

    public static Timespan last7Days()
    {
        Calendar cal = Calendar.getInstance();
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.DAY_OF_YEAR, -7);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.DAY, LAST_7_DAYS);
    }

    public static Timespan lastWeek()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        WeekBounds weekBounds = new WeekBounds(cal).invoke();
        long t0 = weekBounds.getT0();
        long t1 = weekBounds.getT1();

        return new Timespan(t0, t1, Timespan.UNIT.DAY, LAST_WEEK);
    }

    public static Timespan last4Weeks()
    {
        Calendar cal = Calendar.getInstance();
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.WEEK_OF_YEAR, -4);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.DAY, LAST_4_WEEKS);
    }

    public static Timespan lastMonth()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        MonthBounds monthBounds = new MonthBounds(cal).invoke();
        long t0 = monthBounds.getT0();
        long t1 = monthBounds.getT1();

        return new Timespan(t0, t1, Timespan.UNIT.DAY, LAST_MONTH);
    }


    public static Timespan last3Month()
    {
        Calendar cal = Calendar.getInstance();
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, -3);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.WEEK, LAST_3_MONTH);
    }

    public static Timespan lastQuarter()
    {
        Calendar cal = Calendar.getInstance();

        long currentMonth = cal.get(Calendar.MONTH);

        if(currentMonth>=1 && currentMonth<=3)
            cal.set(Calendar.MONTH, Calendar.MARCH);
        else if(currentMonth>=4 && currentMonth<=6)
            cal.set(Calendar.MONTH, Calendar.JUNE);
        else if(currentMonth>=7 && currentMonth<=9)
            cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        else
            cal.set(Calendar.MONTH, Calendar.DECEMBER);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, -2);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.WEEK, LAST_QUARTER);
    }


    public static Timespan last12Month()
    {
        Calendar cal = Calendar.getInstance();
        long t1 = cal.getTimeInMillis();

        cal.add(Calendar.YEAR, -1);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.MONTH, LAST_12_MONTH);
    }

    public static Timespan lastYear()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        long t1 = cal.getTimeInMillis();

        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long t0 = cal.getTimeInMillis();

        return new Timespan(t0, t1, Timespan.UNIT.MONTH, LAST_YEAR);
    }

    public static Timespan fromValue(String value)
    {
        Timespan t = null;

        if(LAST_24_HOURS.equals(value))
            t = last24Hours();
        else if(LAST_DAY.equals(value))
            t =lastDay();
        else if(LAST_7_DAYS.equals(value))
            t =last7Days();
        else if(LAST_WEEK.equals(value))
            t =lastWeek();
        else if(LAST_4_WEEKS.equals(value))
            t =last4Weeks();
        else if(LAST_MONTH.equals(value))
            t =lastMonth();
        else if(LAST_3_MONTH.equals(value))
            t =last3Month();
        else if(LAST_QUARTER.equals(value))
            t =lastQuarter();
        else if(LAST_12_MONTH.equals(value))
            t =last12Month();
        else if(LAST_YEAR.equals(value))
            t =lastYear();

        if(null==t)
            throw new IllegalArgumentException("Unknown value: "+value);

        return t;
    }

    /**
     * Works with right bounds across all calculations.
     * Hence bounds are calculated from right to left (reverse)
     *  
     * @see org.jboss.bpm.monitor.model.metric.Grouping
     * @param timespan
     * @param date
     * @return
     */
    public static long[] getLeftBounds(Timespan timespan, Date date) {
        
        long[] bounds = new long[2];
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        switch(timespan.getUnit())
        {
            case HOUR:
                bounds[1] = cal.getTimeInMillis();
                cal.add(Calendar.HOUR_OF_DAY, -1);
                bounds[0] = cal.getTimeInMillis();
                break;
            case DAY:
                bounds[1] = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, -1);
                bounds[0] = cal.getTimeInMillis();
                break;
            case WEEK:
                bounds[1] = cal.getTimeInMillis();
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                bounds[0] = cal.getTimeInMillis();
                break;
            case MONTH:
                bounds[1] = cal.getTimeInMillis();
                cal.add(Calendar.MONTH, -1);
                bounds[0] = cal.getTimeInMillis();
                break;
            default:
                throw new UnsupportedOperationException("Unknown unit " + timespan.getUnit());
        }
        
        return bounds;
    }

    private static class HourBounds {
        private Calendar cal;
        private long t1;
        private long t0;

        public HourBounds(Calendar cal) {
            this.cal = cal;
        }

        public long getT1() {
            return t1;
        }

        public long getT0() {
            return t0;
        }

        public HourBounds invoke() {            
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            t0 = cal.getTimeInMillis();

            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            t1 = cal.getTimeInMillis();

            return this;
        }
    }

    private static class DayBounds {
        private Calendar cal;
        private long t1;
        private long t0;

        public DayBounds(Calendar cal) {
            this.cal = cal;
        }

        public long getT1() {
            return t1;
        }

        public long getT0() {
            return t0;
        }

        public DayBounds invoke() {
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            t0 = cal.getTimeInMillis();

            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            t1 = cal.getTimeInMillis();
            return this;
        }
    }

    private static class WeekBounds {
        private long t1;
        private long t0;
        private Calendar cal;

        private WeekBounds(Calendar cal) {
            this.cal = cal;
        }

        public long getT1() {
            return t1;
        }

        public long getT0() {
            return t0;
        }

        public WeekBounds invoke() {
           
            cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            t0 = cal.getTimeInMillis();

            cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            t1 = cal.getTimeInMillis();
            return this;
        }
    }

    private static class MonthBounds {
        private Calendar cal;
        private long t1;
        private long t0;

        public MonthBounds(Calendar cal) {
            this.cal = cal;
        }

        public long getT1() {
            return t1;
        }

        public long getT0() {
            return t0;
        }

        public MonthBounds invoke() {
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            t0 = cal.getTimeInMillis();

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            t1 = cal.getTimeInMillis();
            return this;
        }
    }
}
