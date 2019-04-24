/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.util.ArrayList;

/**
 *
 * @author Danel
 */
public class DeleteCmd extends Command {

  public DeleteCmd(ArrayList<Feature> fl, String n) {
    super(fl);
    setName(n);
  }

  public DeleteCmd(ArrayList<Feature> fl) {
    this (fl, "Borrar");
  }

  public void undo() {
    getCoomandListener().undoDelete(this);
  }

  public void redo() {
    getCoomandListener().redoDelete(this);
  }

}
