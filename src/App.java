import javax.swing.SwingUtilities;

import Clases.Tablero;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new Tablero();
    });
  }
}