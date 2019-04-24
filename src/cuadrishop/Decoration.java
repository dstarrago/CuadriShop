/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.Color;

/**
 *
 * @author Danel
 */
public class Decoration extends Feature {

  static public final int radius = 6;
  static public final int diam = radius * 2 + 1;
  private float m_x;
  private float m_y;
  
  public Decoration(float x, float y) {
    m_x = x;
    m_y = y;
    setColor(Color.DARK_GRAY);
    setWidth(1);
    buildShape();
    setBoundingRect();
  }

  public float getX() {
    return m_x;
  }

  public float getY() {
    return m_y;
  }

  public void moveTo(float x, float y) {
    m_x = x;
    m_y = y;
    buildShape();
  }
  
  private void setBoundingRect() {
    setBoundingRect(new Rectangle2D.Float(m_x - radius, m_y - radius, 2 * radius, 2 * radius));
  }

  @Override
  public boolean intersect(double x, double y)   {
    return boundingRect().contains(x, y);
  }

  private void buildShape() {
    setShape(new RoundRectangle2D.Float(m_x - radius, m_y - radius, diam, diam, diam, diam));
  }

  @Override
  public void moveBy(double x, double y) {
    m_x += x;
    m_y += y;
    buildShape();
    setBoundingRect();
  }

  @Override
  public Feature clone()  {
    return new Decoration(m_x, m_y);
  }

  @Override
  public void draw(Graphics2D g2D) {
    g2D.fill(shape());
  }

}
