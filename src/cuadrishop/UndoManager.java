/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

/**
 *
 * @author Danel
 */
public class UndoManager {
  private FastVector history = new FastVector();
  private FastVector reHistory = new FastVector();
  private CommandListener cl;

  public void setCommandlistener(CommandListener cl) {
    this.cl = cl;
  }

  public void addCommand(Command c) {
    c.setCommandlistener(cl);
    history.addElement(c);
    reHistory.removeAllElements();
  }

  public void undo() {
    if (canUndo()) {
      Command c = (Command)history.lastElement();
      reHistory.addElement(c);
      history.removeElementAt(history.size() - 1);
      c.undo();
    }
  }

  public void redo() {
    if (canRedo()) {
      Command c = (Command)reHistory.lastElement();
      history.addElement(c);
      reHistory.removeElementAt(reHistory.size() - 1);
      c.redo();
    }
  }

  public boolean canUndo() {
    return history.size() > 0;
  }

  public boolean canRedo() {
    return reHistory.size() > 0;
  }

  public void clear() {
    history.removeAllElements();
    reHistory.removeAllElements();
  }

  public String undoName() {
    if (canUndo()) {
      Command c = (Command)history.lastElement();
      return c.name();
    }
    return "";
  }

  public String redoName() {
    if (canRedo()) {
      Command c = (Command)reHistory.lastElement();
      return c.name();
    }
    return "";
  }
  
}
