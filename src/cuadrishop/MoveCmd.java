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
public class MoveCmd extends Command {
  private int xMovement;
  private int yMovement;

  public MoveCmd(ArrayList<Feature> fl, int  xd, int yd, String n) {
    super(fl);
    xMovement = xd;
    yMovement = yd;
    setName(n);
  }
  
  public MoveCmd(ArrayList<Feature> fl, int  xd, int yd) {
    this (fl, xd, yd, "Mover");
  }

  public void undo() {
    getCoomandListener().undoMove(this);
  }

  public void redo() {
    getCoomandListener().redoMove(this);
  }

  public int xMovement() {
    return xMovement;
  }

  public int yMovement() {
    return yMovement;
  }

}
