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
public class CentralRef extends Feature {

  private final int WIDTH = 6; //4;
  private final float neighborhood = 5;
  static public final int outerRadius = 15;//10;
  static public final int radius = 10;//5;
  static public final int diam = radius * 2 + 1;
  static public final int outerDiam = outerRadius * 2 + 1;
  private float m_x;
  private float m_y;
  private boolean m_shift;

  public CentralRef(float x, float y, boolean s) {
    m_x = x;
    m_y = y;
    m_shift = s;
    setColor(Color.DARK_GRAY);
    setWidth(WIDTH);
    buildShape();
    setBoundingRect();
  }

  public CentralRef(float x, float y) {
    this(x, y, false);
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

  private void buildShape() {
    GeneralPath p = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    p.append(new Ellipse2D.Float(m_x - radius, m_y - radius, diam, diam), false);
    if (m_shift)
      p.append(new Ellipse2D.Float(m_x - outerRadius, m_y - outerRadius, outerDiam, outerDiam), false);
    setShape(p);
  }

  public void setShift(boolean s) {
    m_shift = s;
  }

  public boolean getShift() {
    return m_shift;
  }

  public boolean isNeighbor(float ox, float oy) {
    return (ox > m_x - neighborhood) && (ox < m_x + neighborhood) &&
            (oy > m_y - neighborhood) && (oy < m_y + neighborhood);
  }

  private void setBoundingRect() {
    setBoundingRect(new Rectangle2D.Float(m_x - radius, m_y - radius, 2 * radius, 2 * radius));
  }

  @Override
  public boolean intersect(double x, double y)   {
    return boundingRect().contains(x, y);
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
    return new CentralRef(m_x, m_y, m_shift);
  }

  @Override
  public void draw(Graphics2D g2D) {
    g2D.draw(shape());
  }

}
