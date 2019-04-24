/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.util.EventListener;

/**
 *
 * @author Danel
 */
public interface CommandListener extends EventListener {

  public void undoDraw(Command c);
  public void redoDraw(Command c);

  public void undoDelete(Command c);
  public void redoDelete(Command c);

  public void undoMove(Command c);
  public void redoMove(Command c);
}
