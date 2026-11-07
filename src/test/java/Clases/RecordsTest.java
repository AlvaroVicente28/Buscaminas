package Clases;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import Clases.Records.Dificultad;

public class RecordsTest {

  @TempDir
  Path carpeta; // carpeta temporal: los tests no tocan tu records.txt
 
  private String ruta() {
    return carpeta.resolve("records.txt").toString();
  }

  @Test 
  void testToInt() {
    assertEquals(0, Records.toInt("00:00"));
    assertEquals(65, Records.toInt("01:05"));
    assertEquals(599, Records.toInt("09:59"));
  }
  
  @Test 
  void testToIntInvalido() {
    assertEquals(-1, Records.toInt(null));
    assertEquals(-1, Records.toInt("test"));
    assertEquals(-1, Records.toInt("1:5"));
    assertEquals(-1, Records.toInt("00:75"));
  }

  @Test 
  void testToStr() {
    assertEquals("00:00", Records.toStr(0));
    assertEquals("01:05", Records.toStr(65));
    assertEquals("10:00", Records.toStr(600));
  }

  @Test 
  void testToStrInvalido() {
    assertEquals("00:00", Records.toStr(0));
    assertEquals("___", Records.toStr(-10));
  }

  @Test
  void testNoFichero() {
    Records records = new Records(ruta());
    assertEquals(-1, records.getRecord(Dificultad.FACIL));
    assertEquals(-1, records.getRecord(Dificultad.NORMAL));
    assertEquals(-1, records.getRecord(Dificultad.DIFICIL));
  }

  @Test
  void testPrimeroEsRecord() {
    Records records = new Records(ruta());
    assertEquals("00:30", records.comprobarRecord(Dificultad.FACIL, 30));
    assertEquals(30, records.getRecord(Dificultad.FACIL));
  }
 
  @Test
  void testNoMejora() {
    Records records = new Records(ruta());
    records.comprobarRecord(Dificultad.FACIL, 30);
    assertEquals("00:30", records.comprobarRecord(Dificultad.FACIL, 45));
    assertEquals(30, records.getRecord(Dificultad.FACIL));
  }
 
  @Test
  void testMejora() {
    Records records = new Records(ruta());
    records.comprobarRecord(Dificultad.FACIL, 30);
    assertEquals("00:20", records.comprobarRecord(Dificultad.FACIL, 20));
    assertEquals(20, records.getRecord(Dificultad.FACIL));
  }
 
  @Test
  void testDificultadesSeparadas() {
    Records records = new Records(ruta());
    records.comprobarRecord(Dificultad.FACIL, 30);
    assertEquals(-1, records.getRecord(Dificultad.NORMAL));
    assertEquals(-1, records.getRecord(Dificultad.DIFICIL));
    assertEquals(30, records.getRecord(Dificultad.FACIL));
  }
 
  @Test
  void testMismoFichero() {
    Records primero = new Records(ruta());
    primero.comprobarRecord(Dificultad.NORMAL, 95);
    Records segundo = new Records(ruta()); 
    assertEquals(95, segundo.getRecord(Dificultad.NORMAL));
  }
}