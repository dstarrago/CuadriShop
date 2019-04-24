/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.awt.*;                                   // For Graphics
import java.awt.geom.*;
import java.awt.Color;
import java.awt.geom.Rectangle2D.Float;

/**
 *
 * @author Danel
 */
public class Arc extends Feature {

  private final Color ARC_COLOR = Color.BLACK;
  private final int ARC_WIDTH = 6; //2;
  private final float neighborhood = 5;
  private FastVector trace;
  private GeneralPath  p;
  private float m_finalX;
  private float m_finalY;
  private boolean deployed;
  private Point2D.Float p0;   // punto inicial
  private Point2D.Float p1;   // punto final
  private Point2D.Float p2;   // punto de control
  private Point2D.Float p3;   // punto intermedio

  public Arc(Point2D.Float initPoint, Point2D.Float midPoint, Point2D.Float finalPoint,
          Point2D.Float controlPoint) {
    this.p0 = (Point2D.Float)initPoint.clone();
    this.p1 = (Point2D.Float)finalPoint.clone();
    this.p2 = (Point2D.Float)controlPoint.clone();
    this.p3 = (Point2D.Float)midPoint.clone();
    m_finalX = p1.x;
    m_finalY = p1.y;
    setColor(ARC_COLOR);
    setWidth(ARC_WIDTH);
    setShape(buildShape());
    setBoundingRect();
  }

  public Arc(Point2D.Float initPoint, Point2D.Float midPoint, Point2D.Float finalPoint) {
    this.p0 = initPoint;
    this.p1 = finalPoint;
    this.p3 = midPoint;
    m_finalX = p1.x;
    m_finalY = p1.y;
    setColor(ARC_COLOR);
    setWidth(ARC_WIDTH);
    calcControlPoint();
    setShape(buildShape());
    setBoundingRect();
  }

  public Arc(float x, float y) {
    trace = new FastVector();
    trace.addElement(new Point2D.Float(x, y));
    p = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    p.moveTo(x, y);
    m_finalX = x;
    m_finalY = y;
    setColor(ARC_COLOR);
    setWidth(ARC_WIDTH);
  }

  public void traceTo(float x, float y) {
    trace.addElement(new Point2D.Float(x, y));
    p.lineTo(x, y);
    m_finalX = x;
    m_finalY = y;
  }

  public void deployArc() {
    if (deployed) return;
    p0 = (Point2D.Float)trace.firstElement();               // punto inicial
    p1 = (Point2D.Float)trace.lastElement();                // punto final
    p3 = (Point2D.Float)trace.elementAt(trace.size() / 2);  // punto intermedio
    calcControlPoint();
    setShape(buildShape());
    p = null;
    trace = null;
    deployed = true;
    setBoundingRect();
  }

  private void calcControlPoint() {
    float mx = (p0.x + p1.x) / 2;
    float my = (p0.y + p1.y) / 2;
    float dx = mx - p3.x;
    float dy = my - p3.y;
    this.p2 = new Point2D.Float(mx - 2 * dx, my - 2 * dy);    // punto de control
  }

  private Shape buildShape() {
    GeneralPath  np = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    np.moveTo(p0.x, p0.y);
    np.quadTo(p2.x, p2.y, p1.x, p1.y);
    return np;
  }

  @Override
  public Shape shape() {
    if (p != null)
      return p;
    else
      return super.shape();
  }

  public float finalX() {
    return m_finalX;
  }

  public float finalY() {
    return m_finalY;
  }

  public boolean isNeighbor(float ox, float oy) {
    return (ox > m_finalX - neighborhood) && (ox < m_finalX + neighborhood) &&
            (oy > m_finalY - neighborhood) && (oy < m_finalY + neighborhood);
  }

  private void setBoundingRect() {
    float x0 = Math.min(Math.min(p0.x, p1.x), p3.x);
    float x1 = Math.max(Math.max(p0.x, p1.x), p3.x);
    float y0 = Math.min(Math.min(p0.y, p1.y), p3.y);
    float y1 = Math.max(Math.max(p0.y, p1.y), p3.y);
    setBoundingRect(new Rectangle2D.Float(x0, y0, x1 - x0, y1 - y0));
  }

  @Override
  public boolean intersect(double x, double y)   {
    //boolean insideBox = boundingRect.contains(x, y);
    float x0 = Math.min(Math.min(p0.x, p1.x), p3.x);
    float x1 = Math.max(Math.max(p0.x, p1.x), p3.x);
    float y0 = Math.min(Math.min(p0.y, p1.y), p3.y);
    float y1 = Math.max(Math.max(p0.y, p1.y), p3.y);
    boolean insideBox = (x >= x0 && x <= x1 && y >= y0 && y <= y1);
    if (!insideBox) {
      return false;
    } else {
      double diag = ((x0 - x1)*(x0 - x1)+(y0 - y1)*(y0 - y1))/100;
      double diagInv = 1/diag;
      for (double t = 0; t < 1; t += diagInv) {
        double Bx = (1-t)*(1-t)*p0.x + 2*t*(1-t)*p2.x + t*t*p1.x;
        double By = (1-t)*(1-t)*p0.y + 2*t*(1-t)*p2.y + t*t*p1.y;
        double epsilon = 2;
        double Bx0 = Bx - epsilon;
        double Bx1 = Bx + epsilon;
        double By0 = By - epsilon;
        double By1 = By + epsilon;
        if (x >= Bx0 && x <= Bx1 && y >= By0 && y <= By1)
          return true;
      }
      return false;
    }
  }

  @Override
  public void moveBy(double x, double y) {
    p0.setLocation(p0.x + x, p0.y + y);
    p1.setLocation(p1.x + x, p1.y + y);
    p2.setLocation(p2.x + x, p2.y + y);
    p3.setLocation(p3.x + x, p3.y + y);
    m_finalX += p1.x;
    m_finalY += p1.y;
    setShape(buildShape());
    setBoundingRect();
  }

  @Override
  public Feature clone()  {
    return new Arc(p0, p3, p1, p2);
  }

  @Override
  public void draw(Graphics2D g2D) {
    g2D.draw(shape());
  }

}
