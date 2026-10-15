package Clases;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Juego {
  private final Casilla[][] tablero;
  private final int casillasFin;
  private final int minas;
  private int banderillas;
  private boolean primerClick = true;
  private int casillasDespejadas;
  private ArrayList<Point> list;

  /**
   * Constructor
   * @param tamano tamaño del tablero
   * @param minas número de minas
   */
  public Juego(int tamano, int minas) {
    casillasFin = tamano * tamano - minas;
    if (minas <= 0){
      throw new IllegalArgumentException("Parámetros de juego inválidos: " + minas + " <= 0");
    }
    if (tamano <= 3){
      throw new IllegalArgumentException("Parámetros de juego inválidos: " + tamano + " <= 3");
    }
    if (minas + 9 > tamano * tamano){
      throw new IllegalArgumentException("Parámetros de juego inválidos: No hay espacio para las minas (minas + 9 > tamano * tamano)");
    }
    this.minas = minas;
    banderillas = minas;
    casillasDespejadas = 0;
    list = new ArrayList<>();
    tablero = new Casilla[tamano][tamano]; //Inicializo el tablero
    for (int i = 0; i < tamano; i++) {
      for (int j = 0; j < tamano; j++) {
        tablero[i][j] = new Casilla();
      }
    }
  }

  /**
   * Despejar la {@code Casilla} (x,y)
   * @param x coordenada x
   * @param y coordenada y
   * @return {@code Resultado} dependiendo del resultado
   */
  public Resultado clickDesp(int x, int y) {
    Casilla casilla = getCasilla(x, y); 
    //Caso en el que se pulsa una casilla con banderilla
    if (casilla.getBanderilla()){
      return Resultado.BANDERILLA;
    }
    //Caso en el que se pulsa una mina
    if (derrota(casilla)){
      return Resultado.DERROTA;
    }
    //Se genera el tablero con el primer click para que no puedas pulsar una mina al inicio
    if (primerClick){ 
      primerClick = false; //Solo pasa la 1ª vez
      excluirInicio(x, y, true);
      ponerMinas();
      excluirInicio(x, y, false);
      ponerNumeros();
    }
    despejarCasilla(casilla);
    list.clear();
    list.add(new Point(x, y));
    //Caso en el que se pulsa una casilla con un hueco
    if (casilla.esVacia()){
      expandirHuecos(x, y);
      if (victoria()){
        return Resultado.VICTORIA;
      }
      return Resultado.HUECO;
    }
    //Caso en el que se gane la partida
    if (victoria()){
      return Resultado.VICTORIA;
    }
    //Caso en el que se pulsa una casilla normal
    return Resultado.SEGUIR;
  }

  /**
   * Click para poner o quitar una banderilla de una {@code Casilla}
   * @param x coordenada x
   * @param y coordenada y
   * @return
   */
  public boolean clickBanderilla(int x, int y) {
    //No se pone banderilla si no se ha despejado con el primer click
    if (primerClick){ 
      return false;
    }
    //Caso en el que la casilla ya tiene una banderilla
    if (tablero[x][y].getBanderilla()) {
      tablero[x][y].setBanderilla(false);
      banderillas++;
      return true;
    }
    //Caso en el que no quedan más banderillas
    if (banderillas == 0){
      return false;
    }
    //Caso en el que la casilla no tiene una banderilla 
    tablero[x][y].setBanderilla(true);
    banderillas--;
    return true;
  }

  /**
   * Devuelve la {@code Casilla} en la posicion (x,y)
   * @param x coordenada x
   * @param y coordenada y
   * @return la {@code Casilla} en la posicion (x,y) si es visible
   * @throws IndexOutOfBoundsException
   */
  protected Casilla getCasilla (int x, int y) {
    if (!esCasillaValida(x, y)) {
      throw new IndexOutOfBoundsException();
    }
    return tablero[x][y];
  }

  /**
   * Comprueba si se ha ganado
   * @return {@code true} si se ha ganado, {@code false} e.o.c
   */
  private boolean victoria (){
    return casillasDespejadas == casillasFin;
  }

  /**
   * Comprueba si se ha perdido
   * @return {@code true} si se ha perdido, {@code false} e.o.c
   */
  private boolean derrota (Casilla casilla){
    if (casilla == null){
      throw new NullPointerException("La casilla es null");
    }
    if (casilla.getHayMina()) {
      despejarTablero();
      return true;
    }
    return false;
  }

  /**
   * Verifica si la {@code Casilla} (x,y) esta dentro de los limites del tablero
   * @param x coordenada x
   * @param y coordenada y
   * @return {@code true} si es válida, {@code false} e.o.c
   */
  private boolean esCasillaValida(int x, int y) {
    return x > -1 && x < tablero.length && y > -1 && y < tablero.length;
  }

  /**
   * Calcula los vecines de la {@code Casilla} (x,y)
   * @param x coordenada x
   * @param y coordenada y
   * @return un {@code ArrayList<Casilla>} con las {@code Casilla} vecinas a (x,y)
   */
  private ArrayList<Casilla> vecinos (int x, int y){
    ArrayList<Casilla> res = new ArrayList<>(); 
    for (int i = -1; i <= 1; i++){
      for (int j = -1; j <= 1; j++){
        if (esCasillaValida(x + i, y + j)){
          res.add(tablero[x + i][y + j]);
        }
      }
    }
    return res;
  }

  /**
   * Rodea de minas alrededor de la {@code Casilla} (x,y). Si había minas, entonces se quitan
   * @param x coorednada x
   * @param y coordenada y
   * @param caso
   */
  private void excluirInicio(int x, int y, boolean caso) {
    for (Casilla casilla : vecinos(x,y)){
      casilla.setMina(caso);
    }
  }

  /**
   * Pone las minas de forma aleatoria
   */
  private void ponerMinas() {
    for (int i = 0; i < minas;){
      int fila = (int)(Math.random() * tablero.length);
      int columna = (int)(Math.random() * tablero.length);
      if (!tablero[fila][columna].getHayMina()) { //Casilla sin mina
        tablero[fila][columna].setMina(true);
        i++;
      }
    }
  }

  /**
   * Asigna numeros dependiendo de las minas de alrededor. 
   * No se cuentan las {@code Casilla} con minas
   */
  private void ponerNumeros() {
    for (int i = 0; i < tablero.length; i++){
      for (int j = 0; j < tablero[i].length; j++){
        if (!tablero[i][j].getHayMina()){ //No hay mina
          tablero[i][j].setNum(contarMinas(i, j));
        }
      }
    }
  }

  /**
   * Cuenta las minas alrededor de la {@code Casilla} (x,y)
   * @param x coordenada x
   * @param y coordenada y
   * @return número de minas alrededor de la {@code Casilla} (x,y)
   */
  private int contarMinas(int x, int y) {
    int res = 0;
    for (Casilla casilla : vecinos(x, y)){
      if (casilla.getHayMina()){
        res++;
      }
    }
    return res;
  }

  /**
   * Hace visible todo el tablero
   */
  private void despejarTablero() {
    list.clear();
    for (int i = 0; i < tablero.length; i++){
      for (int j = 0; j < tablero[i].length; j++){
        if (!tablero[i][j].setVisible()){
          list.add(new Point(i, j));
          tablero[i][j].setFallo(); //Caso en el que se haya puesto una banderilla incorrecta
        }
      }
    } 
  }

  /**
   * Hace visible la {@code Casilla} y aumenta casillasDespejadas
   * @param casilla {@code Casilla} a despejar
   * @return {@code true} si no era visible, {@code false} e.o.c.
   * @see Casilla#setVisible()
   */
  private boolean despejarCasilla(Casilla casilla){
    if (casilla == null){
      throw new NullPointerException("La casilla es null");
    }
    if (!casilla.setVisible()){
      if (casilla.getBanderilla()){
        casilla.setBanderilla(false);
        banderillas++;
      }
      casillasDespejadas++;
      return true;
    }
    return false;
  }

  /**
   * Expande los huecos vacios. Las casillas depejadas se guardan en un {@code ArrayList}
   * @param fila fila
   * @param columna columna
   * @see Juego#getList()
   */
  private void expandirHuecos(int fila, int columna) {
    Queue<Point> cola = new LinkedList<>();
    cola.add(new Point(fila, columna));
    while (!cola.isEmpty()){
      Point punto = cola.poll();
      for (int i = -1; i <= 1; i++){
        for (int j = -1; j <= 1; j++){
          int x = punto.x + i, y = punto.y + j;
          if (esCasillaValida(x, y)){
            Casilla casilla = tablero[x][y];
            if (despejarCasilla(casilla)) {
              list.add(new Point(x, y));
              if (casilla.esVacia()) { //Se expande el hueco
                cola.add(new Point(x, y));
              }             
            }
          }
        }
      }
    }
  }

  /**
   * Getter de list
   * @return {@code ArrayList<int[]>} lista de las coordenadas de las casillas expandidas
   */
  public ArrayList<Point> getList(){
    return this.list;
  }

  /**
   * Comprueba si se ha hecho algun click que despeje alguna casilla
   * @return primerClick
   */
  public boolean getPrimerClick(){
    return primerClick;
  }

  /**
   * Getter de las banderillas
   * @return nº de banderillas restantes
   */
  public int getBanderillasRestantes() {
    return banderillas;
  }

  public enum Resultado {
    DERROTA,
    VICTORIA,
    HUECO,
    SEGUIR,
    BANDERILLA
  }
}