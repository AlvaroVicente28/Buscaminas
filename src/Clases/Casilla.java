package Clases;

public class Casilla {
  private boolean hayMina;
  private int minasVecinas;
  private EstadoCasilla estado;
  
  //Constructor
  public Casilla() {
    hayMina = false;
    minasVecinas = 0;
    estado = EstadoCasilla.OCULTA;
  }

  //Getters
  /**
   * Comprueba si la casilla tiene una mina
   * @return {@code true} si hay casilla, {@code false} e.o.c.
   */
  protected boolean getHayMina() {
    return hayMina;
  }

  /**
   * Comprueba si la casilla tiene una banderilla
   * @return {@code true} si hay banderilla, {@code false} e.o.c.
   */
  public boolean getBanderilla() {
    return estado == EstadoCasilla.BANDERILLA;
  }

  /**
   * 
   * @return
   */
  public int getNum(){
    return minasVecinas;
  }

  /**
   * 
   * @return
   */
  public EstadoCasilla getEstado(){
    return estado;
  }

  /**
   * Comprueba si la casilla es un hueco
   * @return {@code true} si es vacia, {@code false} e.o.c
   */
  public boolean esVacia(){
    return minasVecinas == 0;
  }

  //Setters
  /**
   * Hace visible la casilla
   * @return el valor anterior de esVisible
   */
  protected boolean setVisible() {
    boolean res = estado == EstadoCasilla.VISIBLE;
    this.estado = EstadoCasilla.VISIBLE;
    return res;
  }

  /**
   * Pone o quita una mina en la casilla
   * @param hayMina {@code true} para poner mina, {@code false} para quitarla
   */
  protected void setMina(boolean hayMina) { 
    this.hayMina = hayMina;
  }

  /**
   * Pone o quita una banderilla en la casilla
   * @param hayBanderilla {@code true} para poner banderilla, {@code false} para quitarla
   */
  protected void setBanderilla(boolean hayBanderilla) {
    this.estado  = hayBanderilla ? EstadoCasilla.BANDERILLA : EstadoCasilla.OCULTA;
  }

  /**
   * Cambia el número asociado a la casilla
   * @param numero nuevo número de la casilla
   * Si {@code numero} es 0, se cambia al String vacío
   */
  protected void setNum(int numero) {
    this.minasVecinas = numero;
  }

  /**
   * Marca la casilla como un fallo
   */
  protected void setFallo(){
    if (estado == EstadoCasilla.BANDERILLA && !hayMina){
      estado = EstadoCasilla.FALLO;
    }
  }

  public enum EstadoCasilla{
    OCULTA,
    VISIBLE,
    BANDERILLA,
    FALLO
  }
}