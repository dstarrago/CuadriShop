/*
 * CuadriShopApp.java
 */

package cuadrishop;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class CuadriShopApp extends SingleFrameApplication {

  private CuadriShopView view;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
      view = new CuadriShopView(this);
      show(view);
    }

    public CuadriShopView getView() {
      return view;
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of CuadriShopApp
     */
    public static CuadriShopApp getApplication() {
        return Application.getInstance(CuadriShopApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(CuadriShopApp.class, args);
    }
}
