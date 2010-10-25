/* jboss.org */
package org.jboss.bpm.monitor.model.json;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 11, 2010
 */
public class XYDataSetJSO
{
    /*
  dataset = {
     Id: "unique id for this dataset",
     domain: [ UTCTimeInMilliseconds_1, UTCtimeInMilliseconds_2, ... ],
     range: [ Value_1, Value_2, ... ],
     label: "default label for this dataset",
     axis: "unit"
 }
    */

    String id;
    boolean mipped = true;

    List<List<Long>> domain = new ArrayList<List<Long>>();
    List<List<Long>> range = new ArrayList<List<Long>>();

    double rangeBottom, rangeTop;

    String[] label;
    String axis;

    public XYDataSetJSO()
    {
    }

    public XYDataSetJSO(String[] label, String id)
    {
        this.label = label;
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isMipped()
    {
        return mipped;
    }

    public void setMipped(boolean mipped)
    {
        this.mipped = mipped;
    }

    public List<List<Long>> getDomain()
    {
        return domain;
    }

    public List<List<Long>> getRange()
    {
        return range;
    }

    public double getRangeBottom()
    {
        return rangeBottom;
    }

    public void setRangeBottom(double rangeBottom)
    {
        this.rangeBottom = rangeBottom;
    }

    public double getRangeTop()
    {
        return rangeTop;
    }

    public void setRangeTop(double rangeTop)
    {
        this.rangeTop = rangeTop;
    }

    public String[] getLabel()
    {
        return label;
    }

    public void setLabel(String[] label)
    {
        this.label = label;
    }

    public String getAxis()
    {
        return axis;
    }

    public void setAxis(String axis)
    {
        this.axis = axis;
    }

    public String toJSO()
    {
        StringBuffer sb = new StringBuffer();
        if(domain.size()>1)
            sb.append("[");

        for(int current = 0; current<domain.size(); current++ )
        {

            sb.append("{");
            sb.append("\"id\":\"").append(id).append("-").append(current).append("\",");
            sb.append("\"preferredRenderer\": \"line\",");
            //sb.append("mipped:").append(Boolean.valueOf(mipped).toString()).append(",");

            sb.append("\"domain\": [");
            int idx = 1;
            List<Long> list = domain.get(current);
            for(Long d : list)
            {
                sb.append(d);
                if(idx<list.size()) sb.append(",");
                idx++;
            }

            sb.append("],");

            sb.append("\"range\" :[");
            idx = 1;

            list = range.get(current);
            for(Long d : list)
            {
                sb.append(d);
                if(idx<list.size()) sb.append(",");
                idx++;
            }

            sb.append("],");

            //sb.append("\"rangeBottom\" :").append(rangeBottom).append(",");
            //sb.append("\"rangeTop:\"").append(rangeTop).append(",");
            sb.append("\"label\":\"").append(label[current]).append("\",");
            sb.append("\"axis\":\"").append("Unit").append("\"");
            sb.append("}");

            if(current<(domain.size()-1))
                sb.append(",");

        }

        if(domain.size()>1)
            sb.append("]");
        return sb.toString();
    }
}
