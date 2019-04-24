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
public class DrawCmd extends Command {

  public DrawCmd(ArrayList<Feature> fl, String n) {
    super(fl);
    setName(n);
  }

  public DrawCmd(ArrayList<Feature> fl) {
    this (fl, "Dibujar");
  }

  public void undo() {
    getCoomandListener().undoDraw(this);
  }

  public void redo() {
    getCoomandListener().redoDraw(this);
  }

}
