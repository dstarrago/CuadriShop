/*
 * CuadriShopView.java
 */

package cuadrishop;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.*;

/**
 * The application's main frame.
 */
public class CuadriShopView extends FrameView {

  private Canvas canvas;
  private CuadriShopApp cuadriApp;
  private File workingDir;
  private File doc;
  private boolean isInFile;
  public final String DEFAULT_EXTENSION = "cgd";
  public final String FRAGMENT_EXTENSION = "cgf";
  private String helpFile = "CuadriShop.txt";

    public CuadriShopView(SingleFrameApplication app) {
        super(app);

        cuadriApp = (CuadriShopApp)app;
        initComponents();
        initCanvas(cuadriApp);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

  public Canvas getCanvas() {
    return canvas;
  }

  private void initCanvas(CuadriShopApp app) {
    canvas = new Canvas(app);
    canvas.setMaximumSize(new java.awt.Dimension(10000, 32767));
    canvas.setMinimumSize(new java.awt.Dimension(10, 10));
    canvas.setName("paintBox"); // NOI18N
    canvas.setDocSize(new java.awt.Dimension(2000, 3000));
    canvas.setCuadriShopView(this);
    //canvas.setBackground(new Color(102, 102, 255));
    //MatteBorder border = BorderFactory.createMatteBorder(20, 20, 20, 20, Color.GRAY);
    //canvas.setBorder(border);
    /*
    javax.swing.GroupLayout paintBoxLayout = new javax.swing.GroupLayout(canvas);
    canvas.setLayout(paintBoxLayout);
    paintBoxLayout.setHorizontalGroup(
      paintBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 237, Short.MAX_VALUE)
    );
    paintBoxLayout.setVerticalGroup(
      paintBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 220, Short.MAX_VALUE)
    );
    */
    //jScrollPane1.setViewportView(canvas);
    canvas.setScrollPane(jScrollPane1);
    canvas.setFocusable(true);
    canvas.setFocusCycleRoot(true);
  }

  @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = CuadriShopApp.getApplication().getMainFrame();
            aboutBox = new CuadriShopAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CuadriShopApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    mainPanel = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    Herramientas = new javax.swing.JToolBar();
    Limpiar = new javax.swing.JButton();
    jSeparator2 = new javax.swing.JToolBar.Separator();
    Load = new javax.swing.JButton();
    Save = new javax.swing.JButton();
    jButton1 = new javax.swing.JButton();
    jSeparator4 = new javax.swing.JToolBar.Separator();
    UndoButton = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jSeparator1 = new javax.swing.JToolBar.Separator();
    jLabel1 = new javax.swing.JLabel();
    Disminuir = new javax.swing.JButton();
    Aumentar = new javax.swing.JButton();
    NormoScale = new javax.swing.JButton();
    jSeparator3 = new javax.swing.JToolBar.Separator();
    showGrids = new javax.swing.JCheckBox();
    jSeparator5 = new javax.swing.JToolBar.Separator();
    ModeDraw = new javax.swing.JToggleButton();
    ModeDelete = new javax.swing.JToggleButton();
    ModeSelect = new javax.swing.JToggleButton();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();
    jMenuItem3 = new javax.swing.JMenuItem();
    jMenuItem14 = new javax.swing.JMenuItem();
    jSeparator6 = new javax.swing.JSeparator();
    jMenuItem18 = new javax.swing.JMenuItem();
    jMenuItem17 = new javax.swing.JMenuItem();
    jMenuItem4 = new javax.swing.JMenuItem();
    jSeparator7 = new javax.swing.JSeparator();
    jMenuItem15 = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    jMenuItem9 = new javax.swing.JMenuItem();
    jMenuItem10 = new javax.swing.JMenuItem();
    jSeparator8 = new javax.swing.JSeparator();
    jMenuItem5 = new javax.swing.JMenuItem();
    jMenuItem6 = new javax.swing.JMenuItem();
    jMenuItem7 = new javax.swing.JMenuItem();
    jMenuItem8 = new javax.swing.JMenuItem();
    viewMenu = new javax.swing.JMenu();
    jMenuItem11 = new javax.swing.JMenuItem();
    jMenuItem12 = new javax.swing.JMenuItem();
    jMenuItem13 = new javax.swing.JMenuItem();
    jSeparator9 = new javax.swing.JSeparator();
    GridMenu = new javax.swing.JCheckBoxMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    jMenuItem16 = new javax.swing.JMenuItem();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
    statusMessageLabel = new javax.swing.JLabel();
    statusAnimationLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    actionGroup = new javax.swing.ButtonGroup();

    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cuadrishop.CuadriShopApp.class).getContext().getResourceMap(CuadriShopView.class);
    mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
    mainPanel.setName("mainPanel"); // NOI18N
    mainPanel.setLayout(new java.awt.BorderLayout());

    jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
    jScrollPane1.setName("jScrollPane1"); // NOI18N
    jScrollPane1.setPreferredSize(new java.awt.Dimension(2000, 2000));
    jScrollPane1.setWheelScrollingEnabled(false);
    mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    Herramientas.setRollover(true);
    Herramientas.setMaximumSize(new java.awt.Dimension(13, 30));
    Herramientas.setMinimumSize(new java.awt.Dimension(13, 30));
    Herramientas.setName("Herramientas"); // NOI18N
    Herramientas.setPreferredSize(new java.awt.Dimension(100, 30));

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(cuadrishop.CuadriShopApp.class).getContext().getActionMap(CuadriShopView.class, this);
    Limpiar.setAction(actionMap.get("newDoc")); // NOI18N
    Limpiar.setText(resourceMap.getString("Limpiar.text")); // NOI18N
    Limpiar.setFocusable(false);
    Limpiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    Limpiar.setName("Limpiar"); // NOI18N
    Limpiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(Limpiar);

    jSeparator2.setName("jSeparator2"); // NOI18N
    Herramientas.add(jSeparator2);

    Load.setAction(actionMap.get("open")); // NOI18N
    Load.setText(resourceMap.getString("Load.text")); // NOI18N
    Load.setFocusable(false);
    Load.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    Load.setName("Load"); // NOI18N
    Load.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(Load);

    Save.setAction(actionMap.get("save")); // NOI18N
    Save.setText(resourceMap.getString("Save.text")); // NOI18N
    Save.setFocusable(false);
    Save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    Save.setName("Save"); // NOI18N
    Save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(Save);

    jButton1.setAction(actionMap.get("export")); // NOI18N
    jButton1.setText(resourceMap.getString("btnExport.text")); // NOI18N
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton1.setName("btnExport"); // NOI18N
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(jButton1);

    jSeparator4.setName("jSeparator4"); // NOI18N
    Herramientas.add(jSeparator4);

    UndoButton.setAction(actionMap.get("undo")); // NOI18N
    UndoButton.setActionCommand(resourceMap.getString("UndoButton.actionCommand")); // NOI18N
    UndoButton.setFocusable(false);
    UndoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    UndoButton.setName("UndoButton"); // NOI18N
    UndoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(UndoButton);
    UndoButton.getAccessibleContext().setAccessibleName(resourceMap.getString("UndoButton.AccessibleContext.accessibleName")); // NOI18N

    jButton2.setAction(actionMap.get("redo")); // NOI18N
    jButton2.setText(resourceMap.getString("redoButton.text")); // NOI18N
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton2.setName("redoButton"); // NOI18N
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(jButton2);

    jSeparator1.setName("jSeparator1"); // NOI18N
    Herramientas.add(jSeparator1);

    jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
    jLabel1.setName("jLabel1"); // NOI18N
    Herramientas.add(jLabel1);

    Disminuir.setAction(actionMap.get("ZoomDown")); // NOI18N
    Disminuir.setText(resourceMap.getString("Disminuir.text")); // NOI18N
    Disminuir.setFocusable(false);
    Disminuir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    Disminuir.setMaximumSize(new java.awt.Dimension(30, 21));
    Disminuir.setName("Disminuir"); // NOI18N
    Disminuir.setPreferredSize(new java.awt.Dimension(30, 21));
    Disminuir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(Disminuir);

    Aumentar.setAction(actionMap.get("ZoomUp")); // NOI18N
    Aumentar.setText(resourceMap.getString("Aumentar.text")); // NOI18N
    Aumentar.setFocusable(false);
    Aumentar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    Aumentar.setMaximumSize(new java.awt.Dimension(30, 21));
    Aumentar.setName("Aumentar"); // NOI18N
    Aumentar.setPreferredSize(new java.awt.Dimension(30, 21));
    Aumentar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(Aumentar);

    NormoScale.setAction(actionMap.get("NormoScale")); // NOI18N
    NormoScale.setText(resourceMap.getString("NormoScale.text")); // NOI18N
    NormoScale.setFocusable(false);
    NormoScale.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    NormoScale.setName("NormoScale"); // NOI18N
    NormoScale.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(NormoScale);

    jSeparator3.setName("jSeparator3"); // NOI18N
    Herramientas.add(jSeparator3);

    showGrids.setAction(actionMap.get("checkGrids")); // NOI18N
    showGrids.setText(resourceMap.getString("showGrids.text")); // NOI18N
    showGrids.setFocusable(false);
    showGrids.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    showGrids.setName("showGrids"); // NOI18N
    Herramientas.add(showGrids);

    jSeparator5.setName("jSeparator5"); // NOI18N
    Herramientas.add(jSeparator5);

    ModeDraw.setAction(actionMap.get("DrawMode")); // NOI18N
    actionGroup.add(ModeDraw);
    ModeDraw.setSelected(true);
    ModeDraw.setText(resourceMap.getString("btnDraw.text")); // NOI18N
    ModeDraw.setFocusable(false);
    ModeDraw.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    ModeDraw.setName("btnDraw"); // NOI18N
    ModeDraw.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(ModeDraw);

    ModeDelete.setAction(actionMap.get("EraseMode")); // NOI18N
    actionGroup.add(ModeDelete);
    ModeDelete.setText(resourceMap.getString("btnErase.text")); // NOI18N
    ModeDelete.setFocusable(false);
    ModeDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    ModeDelete.setName("btnErase"); // NOI18N
    ModeDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(ModeDelete);

    ModeSelect.setAction(actionMap.get("SelectMode")); // NOI18N
    actionGroup.add(ModeSelect);
    ModeSelect.setText(resourceMap.getString("btnSelect.text")); // NOI18N
    ModeSelect.setFocusable(false);
    ModeSelect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    ModeSelect.setName("btnSelect"); // NOI18N
    ModeSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    Herramientas.add(ModeSelect);

    mainPanel.add(Herramientas, java.awt.BorderLayout.PAGE_START);

    menuBar.setName("menuBar"); // NOI18N

    fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
    fileMenu.setName("fileMenu"); // NOI18N

    jMenuItem1.setAction(actionMap.get("newDoc")); // NOI18N
    jMenuItem1.setName("jMenuItem1"); // NOI18N
    fileMenu.add(jMenuItem1);

    jMenuItem2.setAction(actionMap.get("open")); // NOI18N
    jMenuItem2.setName("jMenuItem2"); // NOI18N
    fileMenu.add(jMenuItem2);

    jMenuItem3.setAction(actionMap.get("save")); // NOI18N
    jMenuItem3.setName("jMenuItem3"); // NOI18N
    fileMenu.add(jMenuItem3);

    jMenuItem14.setAction(actionMap.get("saveAs")); // NOI18N
    jMenuItem14.setName("jMenuItem14"); // NOI18N
    fileMenu.add(jMenuItem14);

    jSeparator6.setName("jSeparator6"); // NOI18N
    fileMenu.add(jSeparator6);

    jMenuItem18.setAction(actionMap.get("ImportFragment")); // NOI18N
    jMenuItem18.setText(resourceMap.getString("jMenuItem18.text")); // NOI18N
    jMenuItem18.setName("jMenuItem18"); // NOI18N
    fileMenu.add(jMenuItem18);

    jMenuItem17.setAction(actionMap.get("ExportFragment")); // NOI18N
    jMenuItem17.setText(resourceMap.getString("jMenuItem17.text")); // NOI18N
    jMenuItem17.setName("jMenuItem17"); // NOI18N
    fileMenu.add(jMenuItem17);

    jMenuItem4.setAction(actionMap.get("export")); // NOI18N
    jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
    jMenuItem4.setActionCommand(resourceMap.getString("jMenuItem4.actionCommand")); // NOI18N
    jMenuItem4.setName("jMenuItem4"); // NOI18N
    fileMenu.add(jMenuItem4);

    jSeparator7.setName("jSeparator7"); // NOI18N
    fileMenu.add(jSeparator7);

    jMenuItem15.setAction(actionMap.get("Cerrar")); // NOI18N
    jMenuItem15.setName("jMenuItem15"); // NOI18N
    fileMenu.add(jMenuItem15);

    menuBar.add(fileMenu);

    editMenu.setAction(actionMap.get("copy")); // NOI18N
    editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
    editMenu.setName("editMenu"); // NOI18N

    jMenuItem9.setAction(actionMap.get("undo")); // NOI18N
    jMenuItem9.setName("jMenuItem9"); // NOI18N
    editMenu.add(jMenuItem9);

    jMenuItem10.setAction(actionMap.get("redo")); // NOI18N
    jMenuItem10.setName("jMenuItem10"); // NOI18N
    editMenu.add(jMenuItem10);

    jSeparator8.setName("jSeparator8"); // NOI18N
    editMenu.add(jSeparator8);

    jMenuItem5.setAction(actionMap.get("copy")); // NOI18N
    jMenuItem5.setName("jMenuItem5"); // NOI18N
    editMenu.add(jMenuItem5);

    jMenuItem6.setAction(actionMap.get("paste")); // NOI18N
    jMenuItem6.setName("jMenuItem6"); // NOI18N
    editMenu.add(jMenuItem6);

    jMenuItem7.setAction(actionMap.get("delete")); // NOI18N
    jMenuItem7.setName("jMenuItem7"); // NOI18N
    editMenu.add(jMenuItem7);

    jMenuItem8.setAction(actionMap.get("InvertSel")); // NOI18N
    jMenuItem8.setName("jMenuItem8"); // NOI18N
    editMenu.add(jMenuItem8);

    menuBar.add(editMenu);

    viewMenu.setText(resourceMap.getString("viewMenu.text")); // NOI18N
    viewMenu.setName("viewMenu"); // NOI18N

    jMenuItem11.setAction(actionMap.get("ZoomUp")); // NOI18N
    jMenuItem11.setName("jMenuItem11"); // NOI18N
    viewMenu.add(jMenuItem11);

    jMenuItem12.setAction(actionMap.get("ZoomDown")); // NOI18N
    jMenuItem12.setName("jMenuItem12"); // NOI18N
    viewMenu.add(jMenuItem12);

    jMenuItem13.setAction(actionMap.get("NormoScale")); // NOI18N
    jMenuItem13.setName("jMenuItem13"); // NOI18N
    viewMenu.add(jMenuItem13);

    jSeparator9.setName("jSeparator9"); // NOI18N
    viewMenu.add(jSeparator9);

    GridMenu.setAction(actionMap.get("setGrid")); // NOI18N
    GridMenu.setName("GridMenu"); // NOI18N
    viewMenu.add(GridMenu);

    menuBar.add(viewMenu);

    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
    helpMenu.setName("helpMenu"); // NOI18N

    jMenuItem16.setAction(actionMap.get("Help")); // NOI18N
    jMenuItem16.setName("Help"); // NOI18N
    helpMenu.add(jMenuItem16);

    aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
    aboutMenuItem.setName("aboutMenuItem"); // NOI18N
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    statusPanel.setName("statusPanel"); // NOI18N

    statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

    statusMessageLabel.setName("statusMessageLabel"); // NOI18N

    statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

    progressBar.setName("progressBar"); // NOI18N

    javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
    statusPanel.setLayout(statusPanelLayout);
    statusPanelLayout.setHorizontalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(statusMessageLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 543, Short.MAX_VALUE)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statusAnimationLabel)
        .addContainerGap())
    );
    statusPanelLayout.setVerticalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(statusPanelLayout.createSequentialGroup()
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(statusMessageLabel)
          .addComponent(statusAnimationLabel)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(3, 3, 3))
    );

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

  /*
   * File Menu
   */

  @Action
  public void newDoc() {
    checkSaveChange();
    canvas.clear();
    isInFile = false;
    cuadriApp.getMainFrame().setTitle("Nuevo documento");
  }

  @Action
  public void Clear() {
    checkSaveChange();
    canvas.clear();
    isInFile = false;
  }

  @Action
  public void open() {
    checkSaveChange();
    JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
    jFileChooser1.setCurrentDirectory(workingDir);
    FileNameExtensionFilter ff = new FileNameExtensionFilter("Documento de Cuadrigrafía", DEFAULT_EXTENSION);
    jFileChooser1.setFileFilter(ff);
    if (jFileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      doc = jFileChooser1.getSelectedFile();
      canvas.load(doc);
      cuadriApp.getMainFrame().setTitle(doc.getPath());
      workingDir = jFileChooser1.getCurrentDirectory();
      isInFile = true;
    }
  }

  @Action
  public void save() {
    if (isInFile) {
      canvas.save(doc);
    } else {
      saveAs();
    }
  }

  @Action
  public void saveAs() {
    JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
    jFileChooser1.setCurrentDirectory(workingDir);
    FileNameExtensionFilter ff = new FileNameExtensionFilter("Documento de Cuadrigrafía", DEFAULT_EXTENSION);
    jFileChooser1.setFileFilter(ff);
    if (jFileChooser1.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      doc = jFileChooser1.getSelectedFile();
      String fileName = doc.getName();
      if (jFileChooser1.getFileFilter().equals(ff)) {
        String ext = "." + DEFAULT_EXTENSION;
        if (!fileName.endsWith(ext)) {
          doc = new File(jFileChooser1.getCurrentDirectory(), fileName + ext);
        }
      }
      boolean save = true;
      if (doc.exists()) {
        int ans = JOptionPane.showConfirmDialog(mainPanel, "¿Desea sobrescribir el archivo " + fileName + " ?",
                "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (ans != JOptionPane.YES_OPTION) {
          save = false;
        }
      }
      if (save) {
        canvas.save(doc);
        cuadriApp.getMainFrame().setTitle(doc.getPath());
        workingDir = jFileChooser1.getCurrentDirectory();
        isInFile = true;
      }
   }
  }

  @Action
  public void export() {
    JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
    jFileChooser1.setCurrentDirectory(workingDir);
    jFileChooser1.setDialogTitle("Exportar");
    FileNameExtensionFilter ff = new FileNameExtensionFilter("JPEG", "jpg");
    jFileChooser1.setFileFilter(ff);
    jFileChooser1.setAcceptAllFileFilterUsed(false);
    if (jFileChooser1.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      File f = jFileChooser1.getSelectedFile();
      String fileName = f.getName();
      String ext = ".jpg";
      if (!fileName.endsWith(ext)) {
        f = new File(jFileChooser1.getCurrentDirectory(), fileName + ext);
      }
      boolean save = true;
      if (f.exists()) {
        int ans = JOptionPane.showConfirmDialog(mainPanel, "¿Desea sobrescribir el archivo " + fileName + " ?",
                "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (ans != JOptionPane.YES_OPTION) {
          save = false;
        }
      }
      if (save) {
        canvas.export(f);
        workingDir = jFileChooser1.getCurrentDirectory();
      }
    }
  }

  @Action
  public void Cerrar() {
    checkSaveChange();
    cuadriApp.exit();
  }

  /*
   * Edit Menu
   */

  @Action
  public void undo() {
    canvas.undoManager.undo();
  }

  @Action
  public void redo() {
    canvas.undoManager.redo();
  }

  /*
   * View Menu
   */

  @Action
  public void ZoomUp() {
    canvas.zoomUp();
  }

  @Action
  public void ZoomDown() {
    canvas.zoomDown();
  }

  @Action
  public void NormoScale() {
    canvas.zoomNormal();
  }

  /*
   * Help Menu
   */

  public javax.swing.JScrollPane getScrollPane() {
    return jScrollPane1;
  }

  @Action
  public void checkDocPos() {
    canvas.checkDocPosition();
  }

  public void checkSaveChange() {
    if (canvas.isWritten() && canvas.undoManager.canUndo()) {   // Hay acciones que salvar
      int ans = JOptionPane.showConfirmDialog(mainPanel, "¿Desea guardar los cambios?",
              "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
      if (ans == JOptionPane.YES_OPTION) {
        save();
      }
    }
  }

  public void checkOverride(File f) {
    if (f.exists()) {   // Ya existe el archivo
      String fn = f.getName();
      int ans = JOptionPane.showConfirmDialog(mainPanel, "¿Desea sobrescribir el archivo " + fn + " ?",
              "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
      if (ans == JOptionPane.YES_OPTION) {
        save();
      }
    }
  }

  @Action
  public void DrawMode() {
    canvas.setDrawMode();
  }

  @Action
  public void EraseMode() {
    canvas.setEraseMode();
  }

  @Action
  public void SelectMode() {
    canvas.setSelectMode();
  }

  @Action
  public void copy() {
    canvas.copy();
  }

  @Action
  public void paste() {
    canvas.paste();
  }

  @Action
  public void delete() {
    canvas.deleteSelection();
  }

  @Action
  public void InvertSel() {
    canvas.invertSelection();
  }

  @Action
  public void setGrid() {
    if (GridMenu.isSelected()) {
      canvas.showGuides();
      showGrids.setSelected(true);
    }
    else {
      canvas.hideGuides();
      showGrids.setSelected(false);
    }
  }

  @Action
  public void checkGrids() {
    if (showGrids.isSelected()) {
      canvas.showGuides();
      GridMenu.setSelected(true);
    }
    else {
      canvas.hideGuides();
      GridMenu.setSelected(false);
    }
  }

  @Action
  public void Help() {

  }

  @Action
  public void ImportFragment() {
    //checkSaveChange();
    JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
    jFileChooser1.setMultiSelectionEnabled(true);
    jFileChooser1.setCurrentDirectory(workingDir);
    FileNameExtensionFilter ff = new FileNameExtensionFilter("Fragmento de Cuadrigrafía", FRAGMENT_EXTENSION);
    jFileChooser1.setFileFilter(ff);
    if (jFileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      File[] files = jFileChooser1.getSelectedFiles();
      if (files.length == 1) {
        doc = jFileChooser1.getSelectedFile();
        canvas.importFragment(doc);
      } else {
        canvas.importMultipleFragments(files);
      }
      //cuadriApp.getMainFrame().setTitle(doc.getPath());
      workingDir = jFileChooser1.getCurrentDirectory();
      //isInFile = true;
    }
  }

  @Action
  public void ExportFragment() {
    JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
    jFileChooser1.setCurrentDirectory(workingDir);
    FileNameExtensionFilter ff = new FileNameExtensionFilter("Fragmento de Cuadrigrafía", FRAGMENT_EXTENSION);
    jFileChooser1.setFileFilter(ff);
    if (jFileChooser1.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      doc = jFileChooser1.getSelectedFile();
      String fileName = doc.getName();
      if (jFileChooser1.getFileFilter().equals(ff)) {
        String ext = "." + FRAGMENT_EXTENSION;
        if (!fileName.endsWith(ext)) {
          doc = new File(jFileChooser1.getCurrentDirectory(), fileName + ext);
        }
      }
      boolean save = true;
      if (doc.exists()) {
        int ans = JOptionPane.showConfirmDialog(mainPanel, "¿Desea sobrescribir el archivo " + fileName + " ?",
                "Confirme", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (ans != JOptionPane.YES_OPTION) {
          save = false;
        }
      }
      if (save) {
        canvas.exportFragment(doc);
        //cuadriApp.getMainFrame().setTitle(doc.getPath());
        workingDir = jFileChooser1.getCurrentDirectory();
        //isInFile = true;
      }
   }
 }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton Aumentar;
  private javax.swing.JButton Disminuir;
  private javax.swing.JCheckBoxMenuItem GridMenu;
  private javax.swing.JToolBar Herramientas;
  private javax.swing.JButton Limpiar;
  private javax.swing.JButton Load;
  private javax.swing.JToggleButton ModeDelete;
  private javax.swing.JToggleButton ModeDraw;
  private javax.swing.JToggleButton ModeSelect;
  private javax.swing.JButton NormoScale;
  private javax.swing.JButton Save;
  private javax.swing.JButton UndoButton;
  private javax.swing.ButtonGroup actionGroup;
  private javax.swing.JMenu editMenu;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuItem jMenuItem10;
  private javax.swing.JMenuItem jMenuItem11;
  private javax.swing.JMenuItem jMenuItem12;
  private javax.swing.JMenuItem jMenuItem13;
  private javax.swing.JMenuItem jMenuItem14;
  private javax.swing.JMenuItem jMenuItem15;
  private javax.swing.JMenuItem jMenuItem16;
  private javax.swing.JMenuItem jMenuItem17;
  private javax.swing.JMenuItem jMenuItem18;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JMenuItem jMenuItem3;
  private javax.swing.JMenuItem jMenuItem4;
  private javax.swing.JMenuItem jMenuItem5;
  private javax.swing.JMenuItem jMenuItem6;
  private javax.swing.JMenuItem jMenuItem7;
  private javax.swing.JMenuItem jMenuItem8;
  private javax.swing.JMenuItem jMenuItem9;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JToolBar.Separator jSeparator1;
  private javax.swing.JToolBar.Separator jSeparator2;
  private javax.swing.JToolBar.Separator jSeparator3;
  private javax.swing.JToolBar.Separator jSeparator4;
  private javax.swing.JToolBar.Separator jSeparator5;
  private javax.swing.JSeparator jSeparator6;
  private javax.swing.JSeparator jSeparator7;
  private javax.swing.JSeparator jSeparator8;
  private javax.swing.JSeparator jSeparator9;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JCheckBox showGrids;
  private javax.swing.JLabel statusAnimationLabel;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  private javax.swing.JMenu viewMenu;
  // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
