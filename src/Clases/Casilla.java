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
  protected boolean hayMina() {
    return hayMina;
  }

  /**
   * Comprueba si la casilla tiene una banderilla
   * @return {@code true} si hay banderilla, {@code false} e.o.c.
   */
  public boolean esBanderilla() {
    return estado == EstadoCasilla.BANDERILLA;
  }

  /**
   * Devuelve el número de minas vecinas que tiene la casilla
   * @return número de minas vecinas
   */
  public int getNum(){
    return minasVecinas;
  }

  /**
   * Devuelve el estado de la casilla
   * @return {@code EstadoCasilla} estado
   * @see EstadoCasilla
   */
  public EstadoCasilla getEstado(){
    return estado;
  }

  /**
   * Comprueba si la casilla es visible
   * @return {@code true} si es visible, {@code false} e.o.c.
   */
  public boolean esVisible(){
    return estado == EstadoCasilla.VISIBLE;
  }

  /**
   * Comprueba si la casilla está oculta
   * @return {@code true} si está oculta, {@code false} e.o.c.
   */
  public boolean esOculta(){
    return estado == EstadoCasilla.OCULTA;
  }

  /**
   * Comprueba si la casilla es un fallo
   * @return {@code true} si es un fallo, {@code false} e.o.c.
   */
  public boolean esFallo(){
    return estado == EstadoCasilla.FALLO;
  }

  /**
   * Comprueba si la casilla es un hueco
   * @return {@code true} si es vacia, {@code false} e.o.c
   */
  public boolean esVacia(){
    return minasVecinas == 0;
  }

  /**
   * Comprueba si una casilla es visible y no vacía
   * @return {@code true} si es un número, {@code false} e.o.c
   */
  public boolean esNumero(){
    return estado == EstadoCasilla.VISIBLE && !esVacia();
  }

  //Setters
  /**
   * Hace visible la casilla si su estado es {@code EstadoCasilla.OCULTA}
   * @return {@code true} si la casilla se ha hecho visible, {@code false} e.o.c
   */
  protected boolean setVisible() { 
    if (estado == EstadoCasilla.OCULTA){
      estado = EstadoCasilla.VISIBLE;
      return true;
    }
    return false;
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
    if (esOculta() || esBanderilla()){
      this.estado = hayBanderilla ? EstadoCasilla.BANDERILLA : EstadoCasilla.OCULTA;
    }
  }

  /**
   * Cambia el número asociado a la casilla
   * @param numero nuevo número de la casilla
   */
  protected void setNum (int numero) {
    this.minasVecinas = numero;
  }

  /**
   * Marca la casilla si tiene un fallo
   */
  protected void setFallo(){ 
    if (estado == EstadoCasilla.BANDERILLA){
      if (!hayMina){
        estado = EstadoCasilla.FALLO;
      }
    }
    else if (estado == EstadoCasilla.OCULTA){
      estado = EstadoCasilla.VISIBLE;
    }
  }

  @Override 
  public String toString(){
    switch(estado){
      case OCULTA: return "#";
      case FALLO: return "X";
      case BANDERILLA: return "F";
      default: return hayMina ? "*" : minasVecinas + "";
    }
  }

  /**
   * Estado actual de la casilla
   */
  public enum EstadoCasilla{
    OCULTA,
    VISIBLE,
    BANDERILLA,
    FALLO
  }
}