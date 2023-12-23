import model.HealthNCare;
import view.SwingUI;
import view.TextUI;
import view.UserInterface;

import javax.swing.UIManager;

import com.formdev.flatlaf.*;

/** This class is responsible for starting the program. */
public class Program {
  public static void main(String[] args) {
    HealthNCare hnc = new HealthNCare();
    try {
        UIManager.setLookAndFeel( new FlatLightLaf() );
    } catch( Exception ex ) {
        System.err.println( "Failed to initialize LaF" );
    }
    UserInterface gui = new SwingUI(hnc);
    TextUI tui = new TextUI(hnc);
  }
}
