package Clases;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Records {
  private final File archivo;
  private final Properties records;

  /**
   * Clase para gestionar el archivo con los records
   * @param ruta al txt con los records
   */
  public Records(String ruta){
    archivo = new File(ruta);
    records = new Properties();
    if (archivo.exists()) {
      try (FileReader fr = new FileReader(archivo)) {
        records.load(fr);
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Devuelve los segundos del record actual
   * @param dificultad dificultad a comprobar el record
   * @return los segundos del record
   */
  public int getRecord(Dificultad dificultad){
    return toInt(records.getProperty(dificultad.name()));
  }

  /**
   * Compara el nuevo tiempo con el record actual, y si es mejor, actualiza el record
   * @param dificultad dificultad a comprobar el record
   * @param nuevo segundos del nuevo tiempo
   * @return el formato del record
   */
  protected String comprobarRecord(Dificultad dificultad, int nuevo){
    int antiguo = getRecord(dificultad);
    if (nuevo < antiguo || antiguo < 0){
      String nuevoRecord = toStr(nuevo);
      records.setProperty(dificultad.name(), nuevoRecord);
      try(FileWriter fw = new FileWriter(archivo)){
        records.store(fw, null);
      }
      catch (IOException e){
        e.printStackTrace();
      }
      return nuevoRecord;
    }
    return toStr(antiguo);
  }

  /**
   * Parser del formato mm:ss a segundos
   * @param string string a parsear
   * @return -1 si no cumple el formato, e.o.c los segundos
   */
  public static int toInt(String string){
    if (string == null || !string.matches("\\d{2}:\\d{2}")){
      return -1;
    }
    String[] partes = string.split(":");
    int min = Integer.parseInt(partes[0]);
    int seg = Integer.parseInt(partes[1]);
    return (seg < 60) ? min * 60 + seg : -1;
  }

  /**
   * Parser de segundos al formato mm:ss
   * @param segundos int a parsear
   * @return los segundos en formato mm:ss
   */
  public static String toStr(int segundos){
    return (segundos < 0) ? "___" : String.format("%02d:%02d", segundos / 60, segundos % 60);
  }

  /**
   * Niveles de dificultad para la partida
   */
  public enum Dificultad {
    FACIL(9, 10, 67), 
    NORMAL(16, 40, 38), 
    DIFICIL(22, 99, 28);

    public final int tamano, minas, celda;
    
    Dificultad(int tamano, int minas, int celda) {
      this.tamano = tamano;
      this.minas = minas;
      this.celda = celda;
    }
  }
}