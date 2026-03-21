package Clases;

public class Casilla {
  private boolean hayMina;
  private boolean hayBanderilla;
  private boolean esVisible;
  private String num;
  
  //Constructor
  public Casilla() {
    hayMina = false;
    hayBanderilla = false;
    esVisible = false;
    num = "0";
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
    return hayBanderilla;
  }

  /**
   * Comprueba si la casilla es un hueco
   * @return {@code true} si es vacia, {@code false} e.o.c
   */
  public boolean esVacia(){
    return num.isEmpty();
  }

  //Setters
  /**
   * Hace visible la casilla
   * @return el valor anterior de esVisible
   */
  protected boolean setVisible() { //OBS: se podria ver si el toString es "", pero así se entiende mejor
    boolean aux = esVisible;
    this.esVisible = true;
    return aux;
  }

  /**
   * Pone una mina en la casilla
   */
  protected void setMina() { 
    this.hayMina = !this.hayMina;
  }

  /**
   * Pone una banderilla en la casilla
   */
  protected void setBanderilla() {
    this.hayBanderilla = !this.hayBanderilla;
  }

  /**
   * Cambia el número asociado a la casilla
   * @param numero nuevo número de la casilla.
   * Si {@code numero} es 0, se cambia al String vacío
   */
  protected void setNum(int numero) {
    this.num = (numero == 0) ? "" : numero + "";
  }

  /**
   * Marca la casilla como un fallo
   */
  protected void setFallo(){
    if (hayBanderilla && !hayMina){
      num = "X";
    }
  }

  //Presentar con formato
  @Override
  public String toString() {
    if (num.equals("X")){
      return "❌";
    }
    if (hayBanderilla){
      return "🚩";
    }
    if (!esVisible){
      return "";
    }
    return (hayMina) ? "💣" : num;
  }
}