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
  private boolean terminado = false;
  private int casillasDespejadas;
  private ArrayList<Point> list;

  /**
   * Constructor
   * @param tamano tamaño del tablero
   * @param minas número de minas
   */
  public Juego(int tamano, int minas) {
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
    casillasFin = tamano * tamano - minas;
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
   * @param fila coordenada y
   * @param columna coordenada x
   * @return {@code Resultado.NADA} si no se ha hecho nada <br>
   * {@code Resultado.DERROTA} si se ha despejado una mina <br>
   * {@code Resultado.VICTORIA} si se ha ganado la partida <br>
   * {@code Resultado.SEGUIR} si solo se ha despejado una casilla <br>
   * {@code Resultado.HUECO} si se ha despejado varias casillas al pulsar un hueco
   */
  public Resultado clickDesp(int fila, int columna) {
    list.clear();
    Casilla casilla = getCasilla(fila, columna); 
    //Caso en el que se pulsa una casilla no valida para despejar
    if (casilla.esVisible() || casilla.getBanderilla() || terminado){
      return Resultado.NADA;
    }
    //Caso en el que se pulsa una mina
    if (derrota(casilla)){
      return Resultado.DERROTA;
    }
    //Se genera el tablero con el primer click para que no puedas pulsar una mina al inicio
    if (primerClick){ 
      primerClick = false; //Solo pasa la 1ª vez
      excluirInicio(fila, columna, true);
      ponerMinas();
      excluirInicio(fila, columna, false);
      ponerNumeros();
    }
    despejarCasilla(casilla);
    list.add(new Point(columna, fila));
    //Caso en el que se pulsa una casilla con un hueco
    if (casilla.esVacia()){
      expandirHuecos(fila, columna);
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
   * @param fila coordenada y
   * @param columna coordenada x
   * @return {@code true} si pone o quita la banderilla, {@code false} e.o.c
   */
  public boolean clickBanderilla(int fila, int columna) {
    Casilla casilla = getCasilla(fila, columna);
    //No se pone banderilla si no se ha despejado con el primer click o si la partida ha terminado
    if (primerClick || casilla.esVisible() || terminado){ 
      return false;
    }
    //Caso en el que la casilla ya tiene una banderilla
    if (casilla.getBanderilla()) {
      casilla.setBanderilla(false);
      banderillas++;
      return true;
    }
    //Caso en el que no quedan más banderillas
    if (banderillas == 0){
      return false;
    }
    //Caso en el que la casilla no tiene una banderilla 
    casilla.setBanderilla(true);
    banderillas--;
    return true;
  }

  /**
   * Click para despejar adyacentes si la casilla (x,y) ya tiene todas sus minas localizadas
   * @param fila coordenada y
   * @param columna coordenada x
   * @return {@code Resultado.NADA} si no se ha hecho nada <br>
   * {@code Resultado.DERROTA} si se ha despejado una mina <br>
   * {@code Resultado.VICTORIA} si se ha ganado la partida <br>
   * {@code Resultado.HUECO} si se ha despejado alguna casilla
   */
  public Resultado clickCentral(int fila, int columna){
    Casilla casilla = getCasilla(fila, columna);
    int num = casilla.getNum();
    list.clear();
    if (!casilla.esVisible() || num == 0 || num != contarBanderillas(fila, columna) || terminado){
      return Resultado.NADA;
    }
    for (Point puntoVecino : puntosVecinos(fila, columna)){
      Casilla vecina = tablero[puntoVecino.y][puntoVecino.x];
      if (!vecina.getBanderilla() && !vecina.esVisible()){
        if (derrota(vecina)){ //TODO
          return Resultado.DERROTA;
        }
        list.add(new Point(puntoVecino.x, puntoVecino.y));
        despejarCasilla(vecina);
        if (vecina.esVacia()){
          expandirHuecos(puntoVecino.y, puntoVecino.x);
        }
      }
    }
    return (victoria()) ? Resultado.VICTORIA : Resultado.HUECO;
  }

  /**
   * Cuenta el número de casillas vecinas a (x,y) que tengan banderilla
   * @param fila coordenada y
   * @param columna coordenada x
   * @return el número de banderillas vecinas
   */
  private int contarBanderillas(int fila, int columna){
    int res = 0;
    for (Casilla casilla : vecinos(fila, columna)){
      if (casilla.getBanderilla()){
        res++;
      }
    }
    return res;
  }

  /**
   * Devuelve la {@code Casilla} en la posicion (x,y)
   * @param fila coordenada y
   * @param columna coordenada x
   * @return la {@code Casilla} en la posicion (x,y)
   * @throws IndexOutOfBoundsException
   */
  protected Casilla getCasilla (int fila, int columna) {
    if (!esCasillaValida(fila, columna)) {
      throw new IndexOutOfBoundsException();
    }
    return tablero[fila][columna];
  }

  /**
   * Comprueba si se ha ganado
   * @return {@code true} si se ha ganado, {@code false} e.o.c
   */
  private boolean victoria (){
    if (casillasDespejadas == casillasFin){
      terminado = true;
      return true;
    }
    return false;
  }

  /**
   * Comprueba si se ha perdido
   * @return {@code true} si se ha perdido, {@code false} e.o.c
   */
  private boolean derrota (Casilla casilla){
    if (casilla.getHayMina()) {
      despejarTablero();
      terminado = true;
      return true;
    }
    return false;
  }

  /**
   * Verifica si la {@code Casilla} (x,y) esta dentro de los limites del tablero
   * @param fila coordenada y
   * @param columna coordenada x
   * @return {@code true} si es válida, {@code false} e.o.c
   */
  private boolean esCasillaValida(int fila, int columna) {
    return fila > -1 && fila < tablero.length && columna > -1 && columna < tablero[0].length;
  }

  /**
   * Calcula las coordenadas de los vecinos de la {@code Casilla} (x,y)
   * @param fila coordenada y
   * @param columna coordenada x
   * @return un {@code ArrayList<Point>} con las coordenadas de los vecinos de (x,y)
   */
  private ArrayList<Point> puntosVecinos (int fila, int columna){
    ArrayList<Point> res = new ArrayList<>(); 
    for (int i = -1; i <= 1; i++){
      for (int j = -1; j <= 1; j++){
        if (esCasillaValida(fila + i, columna + j)){
          res.add(new Point(columna + j, fila + i));
        }
      }
    }
    return res;
  }

  /**
   * Calcula los vecinos de la {@code Casilla} (x,y)
   * @param fila coordenada y
   * @param columna coordenada x
   * @return un {@code ArrayList<Casilla>} con las {@code Casilla} vecinas a (x,y)
   */
  private ArrayList<Casilla> vecinos (int fila, int columna){
    ArrayList<Casilla> res = new ArrayList<>(); 
    for (Point punto : puntosVecinos(fila, columna)){
      res.add(tablero[punto.y][punto.x]);
    }
    return res;
  }

  /**
   * Rodea de minas alrededor de la {@code Casilla} (x,y). Si había minas, entonces se quitan
   * @param fila coorednada y
   * @param columna coordenada x
   * @param caso
   */
  private void excluirInicio(int fila, int columna, boolean caso) {
    for (Casilla casilla : vecinos(fila, columna)){
      casilla.setMina(caso);
    }
  }

  /**
   * Pone las minas de forma aleatoria
   */
  private void ponerMinas() {
    for (int i = 0; i < minas;){
      int fila = (int)(Math.random() * tablero.length);
      int columna = (int)(Math.random() * tablero[0].length);
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
   * @param fila coordenada y
   * @param columna coordenada x
   * @return número de minas alrededor de la {@code Casilla} (x,y)
   */
  private int contarMinas(int fila, int columna) {
    int res = 0;
    for (Casilla casilla : vecinos(fila, columna)){
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
        if (!tablero[i][j].esVisible()){
          list.add(new Point(j, i));
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
    if (casilla.getBanderilla()){
      casilla.setBanderilla(false);
      banderillas++;
    }
    if (casilla.setVisible()){
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
    cola.add(new Point(columna, fila));
    while (!cola.isEmpty()){
      Point punto = cola.poll();
      for (int i = -1; i <= 1; i++){
        for (int j = -1; j <= 1; j++){
          int x = punto.x + i, y = punto.y + j;
          if (esCasillaValida(y, x)){
            Casilla casilla = tablero[y][x];
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

  /**
   * Getter de terminado
   * @return {@code true} si ha terminado la partida, {@code false} e.o.c
   */
  public boolean haTerminado(){
    return terminado;
  }

  @Override 
  public String toString(){
    String res = "";
    for (int i = 0; i < tablero.length; i++){
      for(int j = 0; j < tablero[i].length; j++){
        res += tablero[i][j].toString() + "\t";
      }
      res += "\n";
    }
    return res;
  }

  /**
   * Posibles estados de la partida tras hacer una jugada
   */
  public enum Resultado {
    DERROTA,
    VICTORIA,
    HUECO,
    SEGUIR,
    NADA
  }
}