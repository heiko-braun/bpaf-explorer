/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.*;
import org.gwt.mosaic.ui.client.layout.*;
import org.gwt.mosaic.ui.client.util.ResizableWidget;
import org.gwt.mosaic.ui.client.util.ResizableWidgetCollection;
import org.jboss.bpm.monitor.gui.client.comments.ChartComment;
import org.jboss.bpm.monitor.gui.client.comments.CommentEditCallback;
import org.jboss.bpm.monitor.gui.client.comments.CommentPanel;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.workspaces.client.api.ProvisioningCallback;
import org.jboss.errai.workspaces.client.api.WidgetProvider;
import org.jboss.errai.workspaces.client.api.annotations.LoadTool;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.Datasets;
import org.timepedia.chronoscope.client.Overlay;
import org.timepedia.chronoscope.client.XYPlot;
import org.timepedia.chronoscope.client.browser.ChartPanel;
import org.timepedia.chronoscope.client.browser.Chronoscope;
import org.timepedia.chronoscope.client.browser.json.GwtJsonDataset;
import org.timepedia.chronoscope.client.browser.json.JsonDatasetJSO;
import org.timepedia.chronoscope.client.canvas.View;
import org.timepedia.chronoscope.client.canvas.ViewReadyCallback;
import org.timepedia.chronoscope.client.data.tuple.Tuple2D;
import org.timepedia.chronoscope.client.event.PlotFocusEvent;
import org.timepedia.chronoscope.client.event.PlotFocusHandler;
import org.timepedia.chronoscope.client.overlays.DomainBarMarker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 11, 2010
 */
@LoadTool(name="Process Activity", group = "Processes")
public class DefinitionHistoryView implements WidgetProvider, CommentEditCallback
{

  private static final String TIMEPEDIA_FONTBOOK_SERVICE = "http://api.timepedia.org/fr";

  private static volatile double GOLDEN__RATIO = 1.618;

  private ChartPanel chartPanel;
  private ToolButton menuButton;
  private ToolButton timespanButton;
  private HTML title;
  private HTML timespan;
  private CaptionLayoutPanel chartArea;
  private LayoutPanel timespanPanel;
  private CommentPanel commentPanel;
  private Map<Long, Overlay> overlayMapping = new HashMap<Long, Overlay>();

  public void provideWidget(ProvisioningCallback callback)
  {
        
    LayoutPanel panel = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.VERTICAL));

    final ToolBar toolBar = new ToolBar();
    panel.add(toolBar, new BoxLayoutData(BoxLayoutData.FillStyle.HORIZONTAL));

    // -----

    menuButton = new ToolButton("Open");
    menuButton.setStyle(ToolButton.ToolButtonStyle.MENU);
    final Command selectProcessCmd = new Command() {
      public void execute()
      {
        // update if necessary
        selectDefinition();
      }
    };

    PopupMenu menuBtnMenu = new PopupMenu();
    menuBtnMenu.addItem("ProcessDefintion", selectProcessCmd);
    menuButton.setMenu(menuBtnMenu);
    toolBar.add(menuButton);

    // -----
    
    toolBar.addSeparator();
    toolBar.add(new ToolButton("Export"));

    // -----

    title = new HTML();
    title.getElement().setAttribute("style", "font-size:24px; font-weight:BOLD");

    // ------------

    BoxLayout boxLayout = new BoxLayout(BoxLayout.Orientation.HORIZONTAL);    
    timespanPanel = new LayoutPanel(boxLayout);
    
    timespan = new HTML();
    timespan.getElement().setAttribute("style", "padding-top:2px; color:#C8C8C8;font-size:16px;text-align:left;");
    timespanButton = new ToolButton();
    timespanButton.setVisible(false);    
    timespanButton.setStyle(ToolButton.ToolButtonStyle.MENU);

    final PopupMenu timeBtnMenu = new PopupMenu();

    for(final String ts : TimespanValues.ALL)
    {
      timeBtnMenu.addItem(ts, new Command()
      {
        public void execute()
        {
          loadGraphData(title.getText(), ts);  
        }
      });
    };

    timespanButton.setMenu(timeBtnMenu);

    timespanPanel.add(timespanButton);
    timespanPanel.add(timespan, new BoxLayoutData(BoxLayoutData.FillStyle.HORIZONTAL));

    // ------------
    
    final LayoutPanel contents = new LayoutPanel(new RowLayout());
        
    LayoutPanel headerPanel = new LayoutPanel(new ColumnLayout());
    headerPanel.add(title, new ColumnLayoutData("50%"));
    headerPanel.add(timespanPanel, new ColumnLayoutData("50%"));

    // ------------

    chartArea = new CaptionLayoutPanel();
    chartArea.setPadding(15);
    contents.add(headerPanel, new RowLayoutData("120"));
    contents.add(chartArea, new RowLayoutData(true));

    // ------------
    commentPanel = new CommentPanel(this);
    commentPanel.addCollapsedListener(new CollapsedListener() {
      public void onCollapsedChange(Widget sender)
      {
        commentPanel.toogleListView();
        contents.layout();
      }
    } );

    contents.add(commentPanel, new RowLayoutData("80"));

    // ------------
    panel.add(contents, new BoxLayoutData(BoxLayoutData.FillStyle.BOTH));
    callback.onSuccess(panel);
  }


  private void selectDefinition()
  {      
    HistoryRecords rpcService = MessageBuilder.createCall(
        new RemoteCallback<List<String>>()
        {

          public void callback(List<String> response)
          {
            final LayoutPopupPanel popup = new LayoutPopupPanel(false);
            popup.addStyleName("soa-PopupPanel");

            final ListBox listBox = new ListBox();
            listBox.addItem("");

            for(String s : response)
            {
              listBox.addItem(s);
            }

            // show dialogue
            LayoutPanel p = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.VERTICAL));
            p.add(new HTML("Which definition would like to inspect?"));
            p.add(listBox);

            // -----
            
            LayoutPanel p2 = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.HORIZONTAL));
            p2.add(new Button("Done", new ClickHandler() {
              public void onClick(ClickEvent clickEvent)
              {
                if(listBox.getSelectedIndex()>0)
                {
                  popup.hide();
                  String procDef = listBox.getItemText(listBox.getSelectedIndex());
                  title.setText(procDef);
                  loadGraphData(procDef, TimespanValues.LAST_7_DAYS);
                }
              }
            }));

            // -----

            HTML html = new HTML("Cancel");
            html.addClickHandler(new ClickHandler(){
              public void onClick(ClickEvent clickEvent)
              {
                popup.hide();
              }
            });
            p2.add(html, new BoxLayoutData(BoxLayoutData.FillStyle.HORIZONTAL));
            p.add(p2);

            // -----
            
            popup.setPopupPosition(menuButton.getAbsoluteLeft()-5, menuButton.getAbsoluteTop()+30);
            popup.setWidget(p);
            popup.pack();
            popup.show();

          }
        },
        HistoryRecords.class
    );

    rpcService.getProcessDefinitionKeys();

  }

  /**
   * Loads the chronoscope data for a particlar processdefinition
   * @param procDefID
   * @param timespan
   */
  private void loadGraphData(final String procDefID, final String timespan)
  {
    ChartData rpcService = MessageBuilder.createCall(
        new RemoteCallback<String>()
        {
          public void callback(String response)
          {
            timespanButton.setVisible(true);
            renderChart(response);
            timespanPanel.layout();
          }
        },
        ChartData.class
    );
    
    rpcService.getDefinitionActivity(procDefID, timespan);
  }

  public void onSaveComment(ChartComment comment)
  {
    final XYPlot plot = chartPanel.getChart().getPlot();
    DomainBarMarker marker = new DomainBarMarker(comment.getDomainValue(), 1.0, comment.getTitle());
    plot.addOverlay(marker);
    plot.redraw();

    // keep reference
    overlayMapping.put(comment.getId(), marker);    
  }

  public void onCancelComment(ChartComment comment)
  {
  }

  public void onDeleteComment(ChartComment comment)
  {
    Overlay marker = overlayMapping.get(comment.getId());
    final XYPlot plot = chartPanel.getChart().getPlot();
    plot.removeOverlay(marker);
    plot.redraw();

    resizeChartView(); // TODO: Why this?
  }

  public void onHighlightComment(ChartComment comment)
  {
    // TODO: implement highlight    
  }

  private void renderChart(String jsonData)
  {
    try
    {
      final Datasets<Tuple2D> datasets = new Datasets<Tuple2D>();
      datasets.add(MonitorUI.chronoscope.getDatasetReader().createDatasetFromJson(
          new GwtJsonDataset(JSOModel.fromJson(jsonData)))
      );
      Dataset[] dsArray = datasets.toArray();

      // if exists remove. I don't know how to update at this point
      if(chartPanel!=null)
      {
        chartArea.remove(chartPanel);
        commentPanel.reset();
        overlayMapping.clear();
      }

      initChartPanel(dsArray);

      chartArea.layout();
    }
    catch (Exception e)
    {
      GWT.log("Failed to create chart", e);
    }
  }

  private void initChartPanel(Dataset[] datasets)
  {
    int[] dim = calcChartDimension();

    // ------
    chartPanel = Chronoscope.createTimeseriesChart(datasets, dim[0], dim[1]);
    
    // marker
    final XYPlot plot = chartPanel.getChart().getPlot();
    plot.addPlotFocusHandler(new PlotFocusHandler(){
      public void onFocus(final PlotFocusEvent event)
      {

        if(event.getFocusDataset()>=0) // zooming
        {          
          ChartComment comment = new ChartComment();
          comment.setDomainValue(event.getDomain());          
          commentPanel.toogleEditView(comment);                   
        }
      }
    }); 

    // ------

    timespan.setText(datasets[0].getRangeLabel());

    chartPanel.setViewReadyCallback(
        new ViewReadyCallback() {
          public void onViewReady(View view)
          {
            resizeChartArea(view);
          }
        }
    );

    chartArea.add(chartPanel);

    // ------

    ResizableWidgetCollection.get().add(new ResizableWidget() {
      public Element getElement() {
        return chartPanel.getElement();
      }

      public boolean isAttached() {
        return chartPanel.isAttached();
      }

      public void onResize(int width, int height)
      {
        View view = resizeChartView();
      }
    });
  }

  private int[] calcChartDimension()
  {
    int w = chartArea.getOffsetWidth()/2;
    int h = (int) (w / GOLDEN__RATIO);

    return new int[] {w, h};
  }
  private View resizeChartView()
  {
    int[] dim = calcChartDimension();

    // Resizing the chart once displayed currently unsupported
    final View view = chartPanel.getChart().getView();
    if(view!=null)
      view.resize(dim[0], dim[1]);

    resizeChartArea(view);
    
    return view;
  }

  private void resizeChartArea(View view)
  {        
    int resizeTo= Integer.valueOf(view.getHeight()) + 75;
    chartArea.setHeight(resizeTo+"px");
    chartArea.layout();
  }

  private static native JsonDatasetJSO getJson(String varName) /*-{
       return $wnd[varName];
    }-*/;
}
