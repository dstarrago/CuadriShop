/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cuadrishop;

import java.util.Observer;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;                                   // For Graphics
import java.awt.geom.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.util.ArrayList;

/**
 *
 * @author Danel
 */
public class Canvas extends JComponent implements Observer {

  public static int M_DRAW = 1;
  public static int M_ERASE = 2;
  public static int M_SELECT = 3;
  public static int LARGE_MOVE = 20;
  public static int SMALL_MOVE = 1;

  //private CuadriShopApp app;
  //private Model model;
  private CuadriShopView view;
  private int docWidth;
  private int docHeight;
  private final int NORMAL_WIDTH = 1;
  private boolean circle;
  private ArrayList<Feature> writing;
  private double scale = 1;
  private double zoomInc = 0.1;
  private boolean displace;
  private int currentScrollX;
  private int currentScrollY;
  private int currentPosX;
  private int currentPosY;
  private int formerPosX;
  private int formerPosY;
  private JScrollPane scrollPane;
  //private JScrollBar horSB;
  //private JScrollBar verSB;
  private boolean showGrids;
  private ArrayList<Line2D.Float> grids;
  private ArrayList<Line2D.Float> subGrids;
  private float gridSpace = 200;
  private int gridSubDiv = 5;
  private Color gridColor = Color.BLUE;
  private Color selectionColor = Color.MAGENTA;
  private Feature ongoingFeature;
  //private boolean shift;
  private int m_mode;
  private float initSelX;
  private float initSelY;
  private boolean selElastic;
  private Rectangle2D.Float selRect;
  private boolean flyingSel;
  private ArrayList<Feature> selectedFeatures = new ArrayList<Feature>();
  private boolean movingSel;
  private ArrayList<Feature> clipboard;
  private boolean addSelection;
  private BufferedImage theCanvas;
  private int docMargin = 4;
  public UndoManager undoManager = new UndoManager();

  private int cursorX;
  private int cursorY;

  private int grafeHorSep = 50;
  private int grafeVerSep = 50;
  private final int textMargin = 25;


  public Canvas(CuadriShopApp theApp) {
    //app = theApp;
    writing = new ArrayList<Feature>();
    //model = app.getModel();
    MouseHandler mHandler = new MouseHandler();    // Create the listener
    KeyHandler kHandler = new KeyHandler();
    HistoryHandler hHandler = new HistoryHandler();
    undoManager.setCommandlistener(hHandler);
    addKeyListener(kHandler);
    addMouseListener(mHandler);               // Monitor mouse button presses
    addMouseMotionListener(mHandler);         // as well as movement
    setDrawMode();
  }

  public void setCuadriShopView(CuadriShopView view) {
    this.view = view;
  }

  // Method called by Observable object when it changes
  public void update(Observable o, Object rectangle) {
    // Code to respond to changes in the model...
    repaint();
  }

  public boolean isWritten() {
    return writing.size() > 0;
  }

  public void setDrawMode()   {
    m_mode = M_DRAW;
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    clearSelection();
  }

  public void setEraseMode()   {
    m_mode = M_ERASE;
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    clearSelection();
  }

  public void setSelectMode()   {
    m_mode = M_SELECT;
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

  public void setDocSize(Dimension d) {
    setPreferredSize(d);
    docWidth = d.width;
    docHeight = d.height;
    theCanvas = new BufferedImage(docWidth, docHeight, BufferedImage.TYPE_INT_RGB);
  }
  
  public void checkDocPosition() {
    Point p = scrollPane.getViewport().getViewPosition();
    if (docWidth * scale < scrollPane.getViewport().getExtentSize().width) {
        p.x = (int)(docWidth * scale - scrollPane.getViewport().getExtentSize().width) / 2;
    }
    if (docHeight * scale < scrollPane.getViewport().getExtentSize().height) {
        p.y = (int)(docHeight * scale - scrollPane.getViewport().getExtentSize().height) / 2;
    }
    scrollPane.getViewport().setViewPosition(p);
  }

  private void setScale(double s) {
    scale = s;
    int newWidth = (int)(docWidth * scale);
    int newHeight = (int)(docHeight * scale);
    Dimension d = new Dimension(newWidth, newHeight);
    setPreferredSize(d);
    setSize(newWidth, newHeight);
    repaint();
  }

  public void zoomUp() {
    circle = false;
    setScale(scale + zoomInc);
  }

  public void zoomNormal() {
    setScale(1);
  }

  public void zoomDown() {
    circle = false;
    if (scale > zoomInc)
      setScale(scale - zoomInc);
  }

  @Override
  public void paint(Graphics G) {
    Graphics g = theCanvas.getGraphics();
    Graphics2D g2D = (Graphics2D)g;                // Get a Java 2D device context
    g2D.setStroke(new BasicStroke(docMargin, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
    g2D.setColor(Color.RED);
    g2D.draw(new Rectangle.Float(docMargin / 2, docMargin / 2, docWidth - docMargin, docHeight - docMargin));
    g2D.setColor(Color.WHITE);
    g2D.fill3DRect(docMargin, docMargin, docWidth - 2 * docMargin, docHeight - 2 * docMargin, true);
    g2D.setColor(Color.BLACK);
    if (showGrids) paintGrids(g2D);
    if (writing.size() > 0) paintWriting(g2D);
    g2D.setPaint(Color.BLACK);
    g2D.setStroke(new BasicStroke(NORMAL_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    if (circle) {
      Point t = getMousePosition();
      g2D.draw(new CentralRef((int)(t.x / scale), (int)(t.y / scale)).shape());
    }
    if (ongoingFeature != null)
      paintFeature(g2D, ongoingFeature);
    if (selElastic) {
      g2D.setColor(Color.GREEN);
      g2D.draw(selRect);
    }
    Graphics2D G2D = (Graphics2D)G;                // Get a Java 2D device context
    G2D.setBackground(new Color(102, 102, 255));
    G2D.clearRect(0, 0, getWidth(), getHeight());
    G2D.scale(scale, scale);
    G2D.drawImage(theCanvas, null, null);
    //if (showGrids) paintGrids(G2D);
  }

  private void paintWriting(Graphics2D g2D) {
    for (int i = 0; i < writing.size(); i++) {
      paintFeature(g2D, writing.get(i));
    }
  }

  private void paintFeature(Graphics2D g2D, Feature f) {
    Shape p = null;
    p = f.shape();
    if (p == null) return;
    if (f.selected())
      g2D.setPaint(selectionColor);
    else
      g2D.setPaint(f.color());
    g2D.setStroke(new BasicStroke(f.width(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    f.draw(g2D);
  }

  private void paintGrids(Graphics2D g2D) {
    g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2D.setPaint(gridColor);
    for (int i = 0; i < grids.size(); i++) {
      Shape s = grids.get(i);
      g2D.draw(s);
    }
    g2D.setPaint(Color.LIGHT_GRAY);
    for (int i = 0; i < subGrids.size(); i++) {
      Shape s = subGrids.get(i);
      g2D.draw(s);
    }
  }

  public void builGrids() {
    grids = new ArrayList<Line2D.Float>();
    subGrids = new ArrayList<Line2D.Float>();
    float subDivSpace = gridSpace / gridSubDiv;
    float h = 0, w = 0;
    for (int i = 1; h < docHeight; i++) {
      h =  i * subDivSpace;
      if (i % gridSubDiv == 0)
        grids.add(new Line2D.Float(0, h, docWidth, h));
      else
        subGrids.add(new Line2D.Float(0, h, docWidth, h));
    }
    for (int i = 1; w < docWidth; i++) {
      w =  i * subDivSpace;
      if (i % gridSubDiv == 0)
        grids.add(new Line2D.Float(w, 0, w, docHeight));
      else
        subGrids.add(new Line2D.Float(w, 0, w, docHeight));
    }
  }

  public void showGuides() {
    if (grids == null)
      builGrids();
    showGrids = true;
    repaint();
  }

  public void hideGuides() {
    showGrids = false;
    repaint();
  }

  public void setScrollPane(JScrollPane aScrollPane) {
    scrollPane = aScrollPane;
    scrollPane.setViewportView(this);
    //horSB = scrollPane.getHorizontalScrollBar();
    //verSB = scrollPane.getVerticalScrollBar();
  }

  private void setOngoingFeature(Feature f) {
    ongoingFeature = f;
    repaint();
  }

  private void writeOngoing() {
    if (ongoingFeature == null)
      return;
    writing.add(ongoingFeature);
    ArrayList<Feature> v = new ArrayList<Feature>();
    v.add(ongoingFeature);
    undoManager.addCommand(new DrawCmd(v));
    if (ongoingFeature instanceof  Arc) {
      Arc a = (Arc)ongoingFeature;
      a.deployArc();
      if (circle) {
        Feature c = new CentralRef(a.finalX(), a.finalY());
        writing.add(c);
        v = new ArrayList<Feature>();
        v.add(c);
        undoManager.addCommand(new DrawCmd(v));
        circle = false;
      }
    }
    ongoingFeature = null;
    repaint();
  }

  private void setWriting(Feature f, boolean clearOngoing) {
    writing.add(f);
    ArrayList<Feature> v = new ArrayList<Feature>();
    v.add(f);
    undoManager.addCommand(new DrawCmd(v));
    if (clearOngoing)
      ongoingFeature = null;
    repaint();
  }

  private Feature lastWrited() {
    if (writing.isEmpty())
      return null;
    else
      return writing.get(writing.size() - 1);
  }

  public void clear() {
    ongoingFeature = null;
    writing.removeAll(writing);
    undoManager.clear();
    repaint();
  }

  public void undo() {
    ongoingFeature = null;
    if (writing.size() > 0) {
      writing.remove(writing.size() - 1);
      repaint();
    }
  }

  public void export(File file) {
    try {
      FileOutputStream o = new FileOutputStream(file);
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(o);
      encoder.encode(theCanvas.getSubimage(docMargin, docMargin, docWidth - 2 * docMargin, docHeight - 2 * docMargin));
      o.close();
    } catch(Exception e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  private ArrayList<Feature> extractSelection() {
    ArrayList<Feature> extractedFeatures = new ArrayList<Feature>();
    for (int i = 0; i < selectedFeatures.size(); i++) {
      Feature f = selectedFeatures.get(i).clone();
      f.moveBy(-selRect.getX(), -selRect.getY());
      f.setSelected(false);
      extractedFeatures.add(f);
    }
    return extractedFeatures;
  }

  public void exportFragment(File file) {
    ObjectOutputStream objectOut = null;
    try {
       // Create the object output stream
       objectOut = new ObjectOutputStream(
                    new BufferedOutputStream(
                     new FileOutputStream(file)));

      // Write three objects to the file
      objectOut.writeObject(extractSelection());            // Write object

    } catch(IOException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }

    // Close the stream
    try {
       objectOut.close();                          // Close the output stream

    } catch(IOException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  public void save(File file) {
    ObjectOutputStream objectOut = null;
    try {
       // Create the object output stream
       objectOut = new ObjectOutputStream(
                    new BufferedOutputStream(
                     new FileOutputStream(file)));

      // Write three objects to the file
      objectOut.writeObject(writing);            // Write object

    } catch(IOException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }

    // Close the stream
    try {
       objectOut.close();                          // Close the output stream

    } catch(IOException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  public void importMultipleFragments(File[] files) {
    cursorX = textMargin;
    cursorY = textMargin;
    double maxHeight = 0;
    double sumHeight = 0;
    int numLines = 0;
    for (int k = 0; k < files.length; k++) {
      ObjectInputStream objectIn = null;  // Stores the stream reference
      int objectCount = 0;                // Number of objects read
      File file = files[k];
      try {
        objectIn = new ObjectInputStream(
                     new BufferedInputStream(
                       new FileInputStream(file)));

        // Read from the stream until we hit the end
        while(true) {
          ArrayList<Feature> fragments = (ArrayList<Feature>)objectIn.readObject();  // Read an object
          //clearSelection();
          //select(fragments);
          double width = 0;
          double height = 0;
          double xMin = Double.MAX_VALUE;
          double xMax = 0;
          double yMin = Double.MAX_VALUE;
          double yMax = 0;
          for (int i = 0; i < fragments.size(); i++) {
            Feature f = fragments.get(i);
            if (f.boundingRect().getMinX() < xMin)
              xMin = f.boundingRect().getMinX();
            if (f.boundingRect().getMaxX() > xMax)
              xMax = f.boundingRect().getMaxX();
            if (f.boundingRect().getMinY() < yMin)
              yMin = f.boundingRect().getMinY();
            if (f.boundingRect().getMaxY() > yMax)
              yMax = f.boundingRect().getMaxY();
          }
          width = xMax - xMin;
          height = yMax - yMin;
          if (height > maxHeight)
            maxHeight = height;
          if (cursorX + width > getWidth() - 2 * textMargin) {
            cursorX = textMargin;
            cursorY += maxHeight + grafeVerSep;
            sumHeight += maxHeight;
            numLines++;
            maxHeight = 0;
          }
          if (cursorY + height < getHeight() - 2 * textMargin) {
            for (int i = 0; i < fragments.size(); i++) {
              Feature f = fragments.get(i);
              f.moveBy(cursorX, cursorY);
            }
            writing.addAll(fragments);
            cursorX += width + grafeHorSep;
          } else {
            break;
          }
          objectCount++;                         // Increment the count
        }

      } catch(ClassNotFoundException e) {
        e.printStackTrace(System.err);
        System.exit(1);

      } catch(EOFException e) {         // This will execute when we reach EOF
        System.out.println("EOF reached. "+ objectCount + " objects read.");

      } catch(IOException e) {               // This is for other I/O errors
        e.printStackTrace(System.err);
        System.exit(1);
      }
      if (cursorY + sumHeight / numLines > getHeight() - 2 * textMargin) {
        break;
      }
    }
    repaint();                // Output the object
  }

  public void importFragment(File file) {
    ObjectInputStream objectIn = null;  // Stores the stream reference
    int objectCount = 0;                // Number of objects read
    try {
      objectIn = new ObjectInputStream(
                   new BufferedInputStream(
                     new FileInputStream(file)));

      // Read from the stream until we hit the end
      while(true) {
        ArrayList<Feature> fragments = (ArrayList<Feature>)objectIn.readObject();  // Read an object
        clearSelection();
        select(fragments);
        writing.addAll(fragments);
        objectCount++;                         // Increment the count
        repaint();                // Output the object
      }

    } catch(ClassNotFoundException e) {
      e.printStackTrace(System.err);
      System.exit(1);

    } catch(EOFException e) {         // This will execute when we reach EOF
      System.out.println("EOF reached. "+ objectCount + " objects read.");

    } catch(IOException e) {               // This is for other I/O errors
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  public void load(File file) {
    ObjectInputStream objectIn = null;  // Stores the stream reference
    int objectCount = 0;                // Number of objects read
    try {
      objectIn = new ObjectInputStream(
                   new BufferedInputStream(
                     new FileInputStream(file)));

       // Read from the stream until we hit the end
      while(true) {
        writing = (ArrayList<Feature>)objectIn.readObject();  // Read an object
        objectCount++;                         // Increment the count
        repaint();                // Output the object
      }

    } catch(ClassNotFoundException e) {
      e.printStackTrace(System.err);
      System.exit(1);

    } catch(EOFException e) {         // This will execute when we reach EOF
      System.out.println("EOF reached. "+ objectCount + " objects read.");

    } catch(IOException e) {               // This is for other I/O errors
      e.printStackTrace(System.err);
      System.exit(1);
    }

    // Close the stream
    try {
      objectIn.close();                          // Close the input stream

    } catch(IOException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  private void eraseLast() {
    writing.remove(writing.size() - 1);
  }

  private void deleteAction(double x, double y) {
    ArrayList<Feature> v = new ArrayList<Feature>();
    for (int i = 0; i < writing.size(); ) {
      Feature f = writing.get(i);
      boolean close = f.intersect(x, y);
      if (close) {
        v.add(f);
        writing.remove(i);
      }
      else
        i++;
    }
    if (v.size() > 0)
      undoManager.addCommand(new DeleteCmd(v));
  }

  public void deleteSelection() {
    ArrayList<Feature> v = new ArrayList<Feature>();
    for (int i = 0; i < writing.size(); ) {
      Feature f = writing.get(i);
      if (f.selected()) {
        v.add(f);
        writing.remove(i);
      }
      else
        i++;
    }
    if (v.size() > 0)
      undoManager.addCommand(new DeleteCmd(v));
    repaint();
  }

  private void deleteFeatures(ArrayList<Feature> list) {
    int p = writing.size() - list.size();
    for (int i = 0; i < list.size(); i++) {
      writing.remove(p);
    }
  }

  private void selectAction(double x, double y) {
    if (!addSelection)
      selectedFeatures = new ArrayList<Feature>();
    for (int i = 0; i < writing.size(); i++) {
      Feature f = writing.get(i);
      if (f.intersect(x, y)) {
        f.setSelected(true);
        selectedFeatures.add(f);
      } else {
        if (!addSelection)
          f.setSelected(false);
      }
    }
  }

  private void select() {
    if (!addSelection)
      selectedFeatures = new ArrayList<Feature>();
    for (int i = 0; i < writing.size(); i++) {
      Feature f = writing.get(i);
      if (selRect.contains(f.boundingRect())) {
        f.setSelected(true);
        selectedFeatures.add(f);
      } else {
        if (!addSelection)
          f.setSelected(false);
      }
    }
  }

  private boolean checkFlyingSel(double x, double y) {
    if (selectedFeatures != null) {
      for (int i = 0; i < selectedFeatures.size(); i++) {
        Feature f = selectedFeatures.get(i);
        if (f.intersect(x, y)) {
          setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
          flyingSel = true;
          return true;
        }
      }
    }
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    flyingSel = false;
    return false;
  }

  private void moveSel(double x, double y) {
    moveSel(selectedFeatures, x, y);
  }

  private void moveSel(ArrayList<Feature> sel, double x, double y) {
    for (int i = 0; i < sel.size(); i++) {
      Feature f = sel.get(i);
        f.moveBy(x, y);
    }
  }

  public void copy() {
    clipboard = selectedFeatures;
  }

  public void paste() {
    if (clipboard != null)
    {
      ArrayList<Feature> v = new ArrayList<Feature>();
      clearSelection();
      selectedFeatures = new ArrayList<Feature>();
      for (int i = 0; i < clipboard.size(); i++) {
        Feature f = (clipboard.get(i).clone());
        v.add(f);
        writing.add(f);
        f.setSelected(true);
        selectedFeatures.add(f);
        f.moveBy(50, 20);
      }
      undoManager.addCommand(new DrawCmd(v, "Pegar"));
      repaint();
    }
  }

  private void drawFeatures(ArrayList<Feature> list) {
    for (int i = 0; i < list.size(); i++) {
      writing.add(list.get(i));
    }
  }

  private void select(ArrayList<Feature> selection) {
    selectedFeatures = selection;
    for (int i = 0; i < selectedFeatures.size(); i++) {
      Feature f = selectedFeatures.get(i);
      f.setSelected(true);
    }
  }

  public void clearSelection() {
    if (selectedFeatures == null)
      return;
    for (int i = 0; i < selectedFeatures.size(); i++) {
      Feature f = selectedFeatures.get(i);
      f.setSelected(false);
    }
    selectedFeatures = null;
    repaint();
  }

  public void invertSelection() {
    selectedFeatures = new ArrayList<Feature>();
    for (int i = 0; i < writing.size(); i++) {
      Feature f = writing.get(i);
      f.flipSelection();
      if (f.selected())
        selectedFeatures.add(f);
    }
    repaint();
  }

  /**
   * Commands implementations
   */

  class HistoryHandler implements CommandListener {

    public void undoDraw(Command c) {
      ongoingFeature = null;
      deleteFeatures(c.getFeatureList());
      repaint();
    }

    public void redoDraw(Command c) {
      ongoingFeature = null;
      drawFeatures(c.getFeatureList());
      repaint();
    }

    public void undoDelete(Command c) {
      ongoingFeature = null;
      drawFeatures(c.getFeatureList());
      repaint();
    }

    public void redoDelete(Command c) {
      ongoingFeature = null;
      deleteFeatures(c.getFeatureList());
      repaint();
    }

    public void undoMove(Command c) {
      ongoingFeature = null;
      MoveCmd mc = (MoveCmd)c;
      moveSel(c.getFeatureList(), mc.xMovement(), mc.yMovement());
      repaint();
    }

    public void redoMove(Command c) {
      ongoingFeature = null;
      MoveCmd mc = (MoveCmd)c;
      moveSel(c.getFeatureList(), -mc.xMovement(), -mc.yMovement());
      repaint();
    }

  }

  class KeyHandler extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        if (e.isControlDown()) {
          moveSel(SMALL_MOVE, 0);
          repaint();
        } else if (e.isShiftDown()){
          moveSel(LARGE_MOVE, 0);
          repaint();
        }
      } else
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        if (e.isControlDown()) {
          moveSel(-SMALL_MOVE, 0);
          repaint();
        } else if (e.isShiftDown()){
          moveSel(-LARGE_MOVE, 0);
          repaint();
        }
      } else
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        if (e.isControlDown()) {
          moveSel(0, -SMALL_MOVE);
          repaint();
        } else if (e.isShiftDown()){
          moveSel(0, -LARGE_MOVE);
          repaint();
        }
      } else
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        if (e.isControlDown()) {
          moveSel(0, SMALL_MOVE);
          repaint();
        } else if (e.isShiftDown()){
          moveSel(0, LARGE_MOVE);
          repaint();
        }
      } else
      if (e.getKeyCode() == KeyEvent.VK_CONTROL && m_mode == M_DRAW) {
        if (ongoingFeature != null && ongoingFeature instanceof Reference) {
          Point p = getMousePosition();
          if (p != null) {
            float scaledX = p.x / (float)scale;
            float scaledY = p.y / (float)scale;
            Reference of = (Reference)ongoingFeature;
            of.moveFinalTo(scaledX, scaledY);
          }
        } else {
          circle = true;
        }
        repaint();
      } else
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        displace = true;
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        if (ongoingFeature != null && ongoingFeature instanceof Reference) {
          Point p = getMousePosition();
          if (p != null) {
            float scaledX = p.x / (float)scale;
            float scaledY = p.y / (float)scale;
            Reference of = (Reference)ongoingFeature;
            if (Math.abs(of.initX() - scaledX) > Math.abs(of.initY() - scaledY)) {
              of.moveFinalTo(scaledX, of.initY());
            } else {
              of.moveFinalTo(of.initX(), scaledY);
            }
          }
        } else {
          circle = false;
        }
        repaint();
      } 
      if (!e.isControlDown()) {
        float subDivSpace = gridSpace / gridSubDiv;
        //Rectangle rect = scrollPane.getVisibleRect();
        Point p = scrollPane.getViewport().getViewPosition();
        float initX = p.x / (float)scale + 300;
        float initY = p.y / (float)scale + 150;
        switch (e.getKeyCode()) {
          case KeyEvent.VK_SPACE:
            displace = false;
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            break;
          case KeyEvent.VK_H: {
            float refWidth = 2 * subDivSpace;
            Reference ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(initX + refWidth, ref.initY());
            writeOngoing();
            setSelectMode();
            //view.SelectMode();

            ArrayList<Feature> sel = new ArrayList<Feature>();
            sel.add(ref);
            clearSelection();
            select(sel);
            break;
          }
          case KeyEvent.VK_V: {
            float refHeight = 2 * subDivSpace;
            Reference ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(ref.initX(), initY + refHeight);
            writeOngoing();
            break;
          }
          case KeyEvent.VK_A: {
            // Parte Horizontal
            Reference ref = new Reference(initX, initY + subDivSpace);
            setOngoingFeature(ref);
            ref.moveFinalTo(initX + subDivSpace, ref.initY());
            writeOngoing();
            // Parte Vertical
            ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(ref.initX(), initY + subDivSpace);
            writeOngoing();
            break;
          }
          case KeyEvent.VK_E: {
            // Parte Horizontal
            Reference ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(initX + subDivSpace, ref.initY());
            writeOngoing();
            // Parte Vertical
            ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(ref.initX(), initY + subDivSpace);
            writeOngoing();
            break;
          }
          case KeyEvent.VK_I: {
            // Parte Horizontal
            Reference ref = new Reference(initX, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(initX + subDivSpace, ref.initY());
            writeOngoing();
            // Parte Vertical
            ref = new Reference(initX + subDivSpace, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(ref.initX(), initY + subDivSpace);
            writeOngoing();
            break;
          }
          case KeyEvent.VK_O: {
            // Parte Horizontal
            Reference ref = new Reference(initX, initY + subDivSpace);
            setOngoingFeature(ref);
            ref.moveFinalTo(initX + subDivSpace, ref.initY());
            writeOngoing();
            // Parte Vertical
            ref = new Reference(initX + subDivSpace, initY);
            setOngoingFeature(ref);
            ref.moveFinalTo(ref.initX(), initY + subDivSpace);
            writeOngoing();
            break;
          }
          case KeyEvent.VK_U: {
            CentralRef ref = new CentralRef(initX, initY);
            setOngoingFeature(ref);
            writeOngoing();
            break;
          }
        }
      }
    }
  }

  class MouseHandler extends MouseInputAdapter {

    @Override
    public void mouseMoved(MouseEvent e) {
      if (m_mode == M_SELECT) {
        float scaledX = e.getX() / (float)scale;
        float scaledY = e.getY() / (float)scale;
        checkFlyingSel(scaledX, scaledY);
      }
    }

    @Override
    public void mousePressed(MouseEvent e) {
      float scaledX = e.getX() / (float)scale;
      float scaledY = e.getY() / (float)scale;
      if (m_mode == M_ERASE) {
        deleteAction(scaledX, scaledY);
        repaint();
      } else {
        if (m_mode == M_SELECT) {
          if (flyingSel) {
            currentPosX = e.getXOnScreen(); // aqui
            currentPosY = e.getYOnScreen();
            formerPosX = currentPosX;
            formerPosY = currentPosY;
          } else {
            initSelX = scaledX;
            initSelY = scaledY;
          }
        } else
        if (m_mode == M_DRAW) {
          if (displace) {
            Point p = scrollPane.getViewport().getViewPosition();
            currentScrollX = p.x;
            currentScrollY = p.y;
            currentPosX = e.getXOnScreen();
            currentPosY = e.getYOnScreen();
          } else
          if (e.getButton() == MouseEvent.BUTTON1) {
            if (ongoingFeature == null) {
              if (circle) {
                setWriting(new CentralRef(scaledX, scaledY, e.isShiftDown()), false);
              }
              Feature last = lastWrited();
              float kx = scaledX;
              float ky = scaledY;
              if (last != null && last instanceof EndDot) {
                EndDot d = (EndDot)last;
                if (d.isNeighbor(scaledX, scaledY)) {
                  kx = d.getX();
                  ky = d.getY();
                  eraseLast();
                }
              }
              setOngoingFeature(new Arc(kx, ky));
            }
          } else {
            if (e.getButton() == MouseEvent.BUTTON3) 
              setOngoingFeature(new Reference(scaledX, scaledY));
            circle = false;
          }
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      float scaledX = e.getX() / (float)scale;
      float scaledY = e.getY() / (float)scale;
      if (m_mode == M_SELECT) {
        if (selElastic) {
          selRect = new Rectangle.Float(Math.min(initSelX, scaledX), Math.min(initSelY, scaledY),
              Math.abs(initSelX - scaledX), Math.abs(initSelY - scaledY));
          addSelection = e.isShiftDown();
          select();
          selElastic = false;
        } else {
          if (movingSel) {
            movingSel = false;    /// aca
            ArrayList<Feature> v = (ArrayList<Feature>)selectedFeatures.clone();
            undoManager.addCommand(new MoveCmd(v, formerPosX - e.getXOnScreen(), formerPosY - e.getYOnScreen()));
          }
          else {
            addSelection = e.isShiftDown();
            selectAction(scaledX, scaledY);
          }
        }
        repaint();
      } else {
        if (ongoingFeature != null) {
          if (ongoingFeature instanceof Reference) {
            Reference of = (Reference)ongoingFeature;
            if (of.initX() == scaledX && of.initY() == scaledY) {
              setWriting(new Decoration(of.initX(), of.initY()), true);
            } else {
              writeOngoing();
            }
          } else {
            writeOngoing();
            if (!(lastWrited() instanceof CentralRef))
              setWriting(new EndDot(scaledX, scaledY), false);
          }
        }
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      float scaledX = e.getX() / (float)scale;
      float scaledY = e.getY() / (float)scale;
      if (m_mode == M_ERASE) {
        deleteAction(scaledX, scaledY);
        repaint();
      } else 
      if (m_mode == M_SELECT) {
        if (flyingSel) {
          movingSel = true;
          float dx = (e.getXOnScreen() - currentPosX) / (float)scale;
          float dy = (e.getYOnScreen() - currentPosY) / (float)scale;
          moveSel(dx, dy);
          currentPosX = e.getXOnScreen();
          currentPosY = e.getYOnScreen();
        } else {
          selRect = new Rectangle.Float(Math.min(initSelX, scaledX), Math.min(initSelY, scaledY),
              Math.abs(initSelX - scaledX), Math.abs(initSelY - scaledY));
          selElastic = true;
        }
        repaint();
      } else
      if (displace) {
        int newX = currentScrollX + (currentPosX - e.getXOnScreen());
        int newY = currentScrollY + (currentPosY - e.getYOnScreen());
        double dWidth = docWidth * scale;
        double dHeight = docHeight * scale;
        int Dx = (dWidth < scrollPane.getViewport().getExtentSize().width)? 0 :
          (newX < 0)? 0: (newX + scrollPane.getViewport().getExtentSize().width > dWidth)?
          (int)(dWidth - scrollPane.getViewport().getExtentSize().width): newX;
        int Dy = (dHeight < scrollPane.getViewport().getExtentSize().height)? 0:
          (newY < 0)? 0: (newY + scrollPane.getViewport().getExtentSize().height > dHeight)?
          (int)(dHeight - scrollPane.getViewport().getExtentSize().height): newY;
        Point p = new Point(Dx, Dy);
        scrollPane.getViewport().setViewPosition(p);
      } else
      if (ongoingFeature != null && ongoingFeature instanceof Arc) {
        ((Arc)ongoingFeature).traceTo(scaledX, scaledY);
         repaint();
      } else
        if (ongoingFeature != null && ongoingFeature instanceof Reference) {
          Reference of = (Reference)ongoingFeature;
          of.setShift(e.isShiftDown());
          if (e.isControlDown()) {
            of.moveFinalTo(scaledX, scaledY);
            repaint();
          } else
          if (Math.abs(of.initX() - scaledX) > Math.abs(of.initY() - scaledY)) {
            of.moveFinalTo(scaledX, of.initY());
            repaint();
          } else {
            of.moveFinalTo(of.initX(), scaledY);
            repaint();
          }
        }
      
    }

  }

}
