/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 12, 2010
 */
public class JSOModel extends JavaScriptObject
{
    protected JSOModel()
    {
    }

    public static native JSOModel fromJson(String jsonString) /*-{
          return eval('(' + jsonString + ')');
      }-*/;


    public final native int length() /*-{ return this.length; }-*/;
    public final native JavaScriptObject get(int i) /*-{ return this[i];     }-*/;
}
