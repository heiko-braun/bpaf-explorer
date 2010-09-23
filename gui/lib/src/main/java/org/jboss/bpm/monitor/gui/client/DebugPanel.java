/* jboss.org */
package org.jboss.bpm.monitor.gui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.event.RowSelectionEvent;
import org.gwt.mosaic.ui.client.event.RowSelectionHandler;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.ColumnLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.workspaces.client.api.ProvisioningCallback;
import org.jboss.errai.workspaces.client.api.WidgetProvider;
import org.jboss.errai.workspaces.client.api.annotations.LoadTool;

import java.util.List;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 11, 2010
 */
@LoadTool(name = "Key Sets", group = "Processes")
public class DebugPanel implements WidgetProvider
{

  private LayoutPanel panel;
  private ListBox<String> processDefinitions;
  private ListBox<String> processInstances;
  private ListBox<String> activities;

  public void provideWidget(ProvisioningCallback callback)
  {
    panel = new LayoutPanel();

    CaptionLayoutPanel wrapper = new CaptionLayoutPanel("BPAF Data Keys");
    wrapper.setLayout(new ColumnLayout());
        
    LayoutPanel leftPanel = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.VERTICAL));

    wrapper.getHeader().add(new Button("Reload",
        new ClickHandler()
        {
          public void onClick(ClickEvent clickEvent)
          {
            loadDefinitions();
          }
        })
    );

    processDefinitions = new ListBox<String>(new String[] {"processDefinitionID"});
    processDefinitions.setMinimumColumnWidth(0, 190);
    processDefinitions.setCellRenderer(
        new ListBox.CellRenderer<String>()
        {
          public void renderCell(ListBox<String> stringListBox, int row, int column, String item)
          {
            switch (column)
            {
              case 0:                
                processDefinitions.setText(row, column, item);
                break;
              default:
                throw new IllegalArgumentException("unknown column");
            }
          }
        }
    );
    processDefinitions.addRowSelectionHandler(
        new RowSelectionHandler()
        {
          public void onRowSelection(RowSelectionEvent rowSelectionEvent)
          {
            loadInstances();
          }
        }
    );

    leftPanel.add(processDefinitions, new BoxLayoutData(BoxLayoutData.FillStyle.BOTH));

    // -----
    LayoutPanel rightPanel = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.VERTICAL));

    processInstances = new ListBox<String>(new String[] {"processInstanceID"});
    processInstances.setMinimumColumnWidth(0, 190);
    processInstances.setCellRenderer(
        new ListBox.CellRenderer<String>()
        {
          public void renderCell(ListBox<String> stringListBox, int row, int column, String item)
          {
            switch (column)
            {
              case 0:                
                processInstances.setText(row, column, item);
                break;
              default:
                throw new IllegalArgumentException("unknown column");
            }
          }
        }
    );
    processInstances.addRowSelectionHandler(
        new RowSelectionHandler()
        {
          public void onRowSelection(RowSelectionEvent rowSelectionEvent)
          {
            loadActivities();
          }
        }
    );

    activities = new ListBox<String>(new String[] {"activityDefinitionID"});
       activities.setMinimumColumnWidth(0, 190);
       activities.setCellRenderer(
           new ListBox.CellRenderer<String>()
           {
             public void renderCell(ListBox<String> stringListBox, int row, int column, String item)
             {
               switch (column)
               {
                 case 0:
                   activities.setText(row, column, item);
                   break;
                 default:
                   throw new IllegalArgumentException("unknown column");
               }
             }
           }
       );


    rightPanel.add(processInstances, new BoxLayoutData(BoxLayoutData.FillStyle.BOTH));
    rightPanel.add(activities, new BoxLayoutData(BoxLayoutData.FillStyle.BOTH));
    
    // -----
    wrapper.add(leftPanel);
    wrapper.add(rightPanel);

    panel.add(wrapper);
    callback.onSuccess(panel);

  }

  private void loadDefinitions()
  {
    final DefaultListModel<String> model =
        (DefaultListModel<String>) processDefinitions.getModel();

    model.clear();

    HistoryRecords rpcService = MessageBuilder.createCall(
        new RemoteCallback<List<String>>()
        {

          public void callback(List<String> response)
          {
            for (String id : response)
              model.add(id);

            processDefinitions.layout();
          }
        },
        HistoryRecords.class
    );
        
    rpcService.getProcessDefinitionKeys();

  }

  private void loadInstances()
  {
    final DefaultListModel<String> model =
        (DefaultListModel<String>) processInstances.getModel();

    model.clear();

    HistoryRecords rpcService = MessageBuilder.createCall(
        new RemoteCallback<List<String>>()
        {

          public void callback(List<String> response)
          {
            for (String id : response)
              model.add(id);

            processInstances.layout();
          }
        },
        HistoryRecords.class
    );

    rpcService.getProcessInstanceKeys(
        processDefinitions.getItem(
            processDefinitions.getSelectedIndex()
        )
    );



  }

  private void loadActivities()
  {
    final DefaultListModel<String> model =
        (DefaultListModel<String>) activities.getModel();

    model.clear();

    HistoryRecords rpcService = MessageBuilder.createCall(
        new RemoteCallback<List<String>>()
        {

          public void callback(List<String> response)
          {
            for (String id : response)
              model.add(id);

            activities.layout();
          }
        },
        HistoryRecords.class
    );

    rpcService.getActivityKeys(
        processInstances.getItem(
            processInstances.getSelectedIndex()
        )
    );

  }

}
