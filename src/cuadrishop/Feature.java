/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.awt.*;                                   // For Graphics
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Clase abstracta de un elemento gráfico. Los grafes están compuestos de varios
 * elementos gráficos. Por ejemplo: arcos, puntos o círculos rellenos, referencias
 * horizontales, verticales o angulares, etc.
 *
 * @author Danel
 */
public abstract class Feature implements Serializable {
  /**
   * Área rectangular del lienzo que contiene a este elemento gráfico.
   */
  private Rectangle2D.Float boundingRect;
  /**
   * Forma del elemento gráfico.
   */
  private Shape m_shape;
  /**
   * Color del elemento gráfico.
   */
  private Color color;
  /**
   * Ancho del trazo del elemento gráfico.
   */
  private int width;
  /**
   * Indica si el elemento gráfico está selecccionado.
   */
  private boolean selected;

  public Feature() {
  }

  public Feature(Shape s) {
    m_shape = s;
  }

  public boolean selected() { return selected; }
  public void setSelected(boolean v) { selected = v; }
  public void flipSelection() { selected = !selected; }

  public Color color() { return color; }
  public void setColor(Color color) { this.color = color; }

  public int width() { return width; }
  public void setWidth(int width) { this.width = width; }

  public Shape shape() { return m_shape; }
  public void setShape(Shape s) { m_shape = s; }

  public Rectangle2D.Float boundingRect() { return boundingRect; }
  public void setBoundingRect(Rectangle2D.Float rect) { boundingRect = rect; }

  public abstract boolean intersect(double x, double y);
  public abstract void moveBy(double x, double y);
  public abstract void draw(Graphics2D g2D);

  @Override
  public abstract Feature clone();
}
