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
public class Reference extends Feature {

  static public final int WIDTH = 12; //4;
  static public final int sep = 3;
  private float m_x0;
  private float m_y0;
  private float m_x1;
  private float m_y1;
  private boolean m_shift;

  public Reference(float x0, float y0, float x1, float y1, boolean s) {
    m_x0 = x0;
    m_y0 = y0;
    m_x1 = x1;
    m_y1 = y1;
    m_shift = s;
    setColor(Color.DARK_GRAY);
    setWidth(WIDTH);
    buildShape();
    setBoundingRect();
  }

  public Reference(float x0, float y0, float x1, float y1) {
    this(x0, y0, x1, y1, false);
  }

  private void buildShape() {
    GeneralPath p = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    if (m_shift) {
      if (m_x0 == m_x1) {
        p.moveTo(m_x0 - sep, m_y0);
        p.lineTo(m_x1 - sep, m_y1);
        p.moveTo(m_x0 + sep, m_y0);
        p.lineTo(m_x1 + sep, m_y1);
      } else {
        p.moveTo(m_x0, m_y0 - sep);
        p.lineTo(m_x1, m_y1 - sep);
        p.moveTo(m_x0, m_y0 + sep);
        p.lineTo(m_x1, m_y1 + sep);
      }
    } else {
      p.moveTo(m_x0, m_y0);
      p.lineTo(m_x1, m_y1);
    }
    setShape(p);
  }
  
  public Reference(float x0, float y0) {
    m_x0 = x0;
    m_y0 = y0;
    setColor(Color.DARK_GRAY);
    setWidth(WIDTH);
  }

  public float initX() {
    return m_x0;
  }

  public float initY() {
    return m_y0;
  }

  public float finalX() {
    return m_x1;
  }

  public float finalY() {
    return m_y1;
  }

  public void setShift(boolean s) {
    m_shift = s;
  }

  public boolean getShift() {
    return m_shift;
  }

  public void moveFinalTo(float x, float y) {
    m_x1 = x;
    m_y1 = y;
    buildShape();
    setBoundingRect();
  }

  private void setBoundingRect() {
    int dw = width() / 2;
    if (m_shift) dw += sep;
    if (m_x0 == m_x1)  {     // es una referencia vertical
      setBoundingRect(new Rectangle2D.Float(m_x0 - dw, Math.min(m_y0, m_y1) - dw,
              2 * dw, Math.abs(m_y0 - m_y1)));
    } else if (m_y0 == m_y1) {     // es una referencia horizontal
      setBoundingRect(new Rectangle2D.Float(Math.min(m_x0, m_x1), m_y0 - dw,
              Math.abs(m_x0 - m_x1), 2 * dw));
    } else {                    // es una linea oblicua
      float x0 = Math.min(m_x0, m_x1);
      float x1 = Math.max(m_x0, m_x1);
      float y0 = Math.min(m_y0, m_y1);
      float y1 = Math.max(m_y0, m_y1);
      setBoundingRect(new Rectangle2D.Float(x0, y0, x1 - x0, y1 - y0));
    }
  }

  @Override
  public boolean intersect(double x, double y)   {
    return boundingRect().contains(x, y);
  }

  @Override
  public void moveBy(double x, double y) {
    m_x0 += x;
    m_x1 += x;
    m_y0 += y;
    m_y1 += y;
    buildShape();
    setBoundingRect();
  }

  @Override
  public Feature clone()  {
    return new Reference(m_x0, m_y0, m_x1, m_y1, m_shift);
  }

  @Override
  public void draw(Graphics2D g2D) {
    g2D.draw(shape());
  }
}
