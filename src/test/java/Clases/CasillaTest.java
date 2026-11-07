package Clases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CasillaTest {
  
  @Test 
  void testConstructor() {
    Casilla casilla = new Casilla();
    assertTrue(casilla.esOculta());
    assertFalse(casilla.hayMina());
    assertFalse(casilla.esFallo());
    assertEquals(0, casilla.getNum());
  }

  @Test
  void testPonerNumero() {
    Casilla casilla = new Casilla();
    casilla.setNum(1);
    assertEquals(1, casilla.getNum());
  }

  @Test
  void testPoner0(){
    Casilla casilla = new Casilla();
    casilla.setNum(1);
    casilla.setNum(0);
    assertEquals(0, casilla.getNum());
  }

  @Test 
  void testPonerNumeroNegativo() {
    Casilla casilla = new Casilla();
    casilla.setNum(-1);
    assertEquals(0, casilla.getNum());
  }

  @Test 
  void testPonerNumeroGrande() {
    Casilla casilla = new Casilla();
    casilla.setNum(9);
    assertEquals(0, casilla.getNum());
  }

  @Test 
  void testPonerNumeroEnRevelada() {
    Casilla casilla = new Casilla();
    casilla.setVisible();
    casilla.setNum(5);
    assertTrue(casilla.esVisible());
    assertEquals(0, casilla.getNum());
  }

  @Test 
  void testBanderillas() {
    Casilla casilla = new Casilla();
    casilla.setBanderilla(true);
    assertTrue(casilla.esBanderilla());
    assertFalse(casilla.esOculta());
    casilla.setBanderilla(false);
    assertFalse(casilla.esBanderilla());
    assertTrue(casilla.esOculta());
  }

  @Test 
  void testBanderillaEnRevelada() {
    Casilla casilla = new Casilla();
    casilla.setVisible();
    casilla.setBanderilla(true);
    assertFalse(casilla.esBanderilla());
    assertTrue(casilla.esVisible());
  }

  @Test 
  void testRevelar() {
    Casilla casilla = new Casilla();
    assertTrue(casilla.setVisible());
    assertFalse(casilla.esOculta());
    assertTrue(casilla.esVisible());
    assertFalse(casilla.setVisible());
  }

  @Test
  void testRevelarEnBanderilla() {
    Casilla casilla = new Casilla();
    casilla.setBanderilla(true);
    assertFalse(casilla.setVisible());
    assertTrue(casilla.esBanderilla());
  }

  @Test 
  void testEsNumero() {
    Casilla casilla = new Casilla();
    assertFalse(casilla.esNumero());
    casilla.setNum(1);
    assertFalse(casilla.esNumero());
    assertTrue(casilla.setVisible());
    assertTrue(casilla.esNumero());
    casilla.setNum(0);
    assertFalse(casilla.esNumero());
  }

  @Test 
  void testPonerMina() {
    Casilla casilla = new Casilla();
    casilla.setMina(true);
    assertTrue(casilla.hayMina());
    assertTrue(casilla.esOculta());
    casilla.setMina(false);
    assertFalse(casilla.hayMina());
    assertTrue(casilla.esOculta());
  }

  @Test 
  void testPonerMinaEnRevelada() {
    Casilla casilla = new Casilla();
    casilla.setVisible();
    casilla.setMina(true);
    assertFalse(casilla.hayMina());
    assertTrue(casilla.esVisible());
  }

  @Test 
  void testPonerMinaEnBanderilla() {
    Casilla casilla = new Casilla();
    casilla.setBanderilla(true);
    casilla.setMina(true);
    assertTrue(casilla.hayMina());
    assertTrue(casilla.esBanderilla());
  }

  @Test 
  void testFallo() {
    Casilla casilla = new Casilla();
    casilla.setBanderilla(true);
    casilla.setFallo();
    assertTrue(casilla.esFallo());
  }

  @Test
  void testSinFallo() {
    Casilla casilla = new Casilla();
    casilla.setMina(true);
    casilla.setBanderilla(true);
    casilla.setFallo();
    assertTrue(casilla.esBanderilla());
    assertFalse(casilla.esFallo());
  }

  @Test 
  void testFalloRevela() {
    Casilla casilla = new Casilla();
    casilla.setFallo();
    assertTrue(casilla.esVisible());
    assertFalse(casilla.esFallo());
  }

  @Test 
  void testEsVacia() {
    Casilla casilla = new Casilla();
    assertTrue(casilla.esVacia());
    casilla.setMina(true);
    assertTrue(casilla.esVacia());
    casilla.setBanderilla(true);
    assertTrue(casilla.esVacia());
    casilla.setNum(5);
    assertFalse(casilla.esVacia());
    casilla.setVisible();
    assertFalse(casilla.esVacia());
  }

  @Test 
  void testToString() {
    Casilla casilla = new Casilla();
    assertEquals("#", casilla.toString());
    casilla.setNum(3);
    assertEquals("#", casilla.toString());
    casilla.setBanderilla(true);
    assertEquals("F", casilla.toString());
    casilla.setBanderilla(false);
    casilla.setVisible();
    assertEquals("3", casilla.toString());
  }

  @Test 
  void testMinaToString() {
    Casilla casilla = new Casilla();
    casilla.setMina(true);
    assertEquals("#", casilla.toString());
    casilla.setNum(3);
    casilla.setVisible();
    assertEquals("*", casilla.toString());
  }

  @Test
  void testFalloToString() {
    Casilla casilla = new Casilla();
    casilla.setBanderilla(true);
    assertEquals("F", casilla.toString());
    casilla.setFallo();
    assertEquals("X", casilla.toString());
  }
}