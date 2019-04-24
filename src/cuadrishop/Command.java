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
public abstract class Command {
  private String name;
  private ArrayList<Feature> featureList;
  private CommandListener cl;

  public Command(ArrayList<Feature> fl) {
    featureList = fl;
  }

  public void setCommandlistener(CommandListener cl) {
    this.cl = cl;
  }

  public CommandListener getCoomandListener() { return cl; }

  public abstract void undo();
  public abstract void redo();

  public String name() { return name; }

  public void setName(String n) {
    name = n;
  }

  public void setFeatureList(ArrayList<Feature> l) { featureList = l; }
  public ArrayList<Feature> getFeatureList() { return featureList; }
}
