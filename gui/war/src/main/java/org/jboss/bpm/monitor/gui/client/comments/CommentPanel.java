/* jboss.org */
package org.jboss.bpm.monitor.gui.client.comments;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import org.gwt.mosaic.ui.client.DeckLayoutPanel;
import org.gwt.mosaic.ui.client.DisclosureLayoutPanel;
import org.gwt.mosaic.ui.client.ListBox;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.list.DefaultListModel;

/**
 * @author: Heiko Braun <hbraun@redhat.com>
 * @date: Mar 31, 2010
 */
public class CommentPanel extends DisclosureLayoutPanel
{
  private DeckLayoutPanel commentDeck;
  private LayoutPanel editPanel;
  private LayoutPanel listPanel;
  private ListBox<ChartComment> comments;
  private CommentEditCallback callbackComment;

  private ChartComment suggestion = null;
  private TextArea textBox;

  public CommentPanel(final CommentEditCallback callbackComment)
  {
    super("Comments");

    this.callbackComment = callbackComment;
    
    commentDeck = new DeckLayoutPanel();    

    // ----
    editPanel = new LayoutPanel(new BoxLayout(BoxLayout.Orientation.HORIZONTAL));    
    textBox = new TextArea();
    textBox.setCharacterWidth(50);
    textBox.setVisibleLines(2);
    editPanel.add(textBox);
    
    editPanel.add(new Button("Save", new ClickHandler()
    {
      public void onClick(ClickEvent clickEvent)
      {
        // chart callback
        suggestion.setTitle(textBox.getText());
        addComment(suggestion); // TODO: save entity
        callbackComment.onSaveComment(suggestion);
        resetEditState();
      }
    }));
    
    HTML cancel = new HTML("cancel");
    cancel.addClickHandler(new ClickHandler()
    {
      public void onClick(ClickEvent clickEvent)
      {
        callbackComment.onCancelComment(suggestion);
        resetEditState();
      }
    });
    editPanel.add(cancel);

    // ----
    listPanel = new LayoutPanel();
    comments = new ListBox<ChartComment>(new String[]{"Title", ""});
    comments.setCellRenderer(new ListBox.CellRenderer<ChartComment>() {
      public void renderCell(ListBox listBox, int row, int col, final ChartComment item)
      {
        switch (col)
        {
          case 0:
            listBox.setText(row, col, item.getTitle());
            break;
          case 1:
            HTML html = new HTML("delete");
            html.addClickHandler(new ClickHandler()
            {
              public void onClick(ClickEvent clickEvent)
              {
                callbackComment.onDeleteComment(item);
                DefaultListModel<ChartComment> model = (DefaultListModel)comments.getModel();
                model.remove(item);
              }
            });
            listBox.setWidget(row, col, html);
            break;
          default:
            throw new IllegalArgumentException("illegal column");
        }
      }
    });
    listPanel.add(comments);


    commentDeck.add(listPanel);
    commentDeck.add(editPanel);

    this.add(commentDeck);

  }

  public void toogleListView()
  {
    if(this.isCollapsed())
      setCollapsed(false);
    commentDeck.showWidget(0);
    this.layout();
  }

  public void toogleEditView(ChartComment comment)
  {
    if(this.isCollapsed())
      setCollapsed(false);
    commentDeck.showWidget(1);

    suggestion = comment;
    if(comment.getId()!=0)
      textBox.setText(comment.getTitle());
    this.layout();
  }

  private void addComment(ChartComment comment)
  {
    DefaultListModel<ChartComment> model = (DefaultListModel)comments.getModel();
    model.add(comment);
    comment.setId(model.getSize());
  }

  private void resetEditState()
  {
    //suggestion = null;
    textBox.setText("");
    DeferredCommand.addCommand(new Command()
    {
      public void execute()
      {
        setCollapsed(true);
        layout();
      }
    });


  }

  public void reset()
  {
    suggestion = null;
    DefaultListModel<ChartComment> model = (DefaultListModel)comments.getModel();
    model.clear();
    textBox.setText("");
  }
}
