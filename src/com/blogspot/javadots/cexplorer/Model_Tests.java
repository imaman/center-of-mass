package com.blogspot.javadots.cexplorer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class Model_Tests {

   private static final double EPSILON = 0.001;
   Model m = new Model();

   @Test
   public void initiallyShouldBeEmpty() {
      assertEquals(0, m.sizes().size());
   }


   @Test
   public void shouldReturnNullWhenEmpty() {
      assertNull(m.ocurrencesOf(0));
   }


   @Test
   public void registerredSizeShouldAppearInKeys() {
      m.register(10);
      assertEquals(Arrays.asList(10), m.sizes());
   }

   @Test
   public void firstRegisterredSizeShouldSetCountToOne() {
      m.register(10);
      assertEquals("", new Double(1), m.ocurrencesOf(10), EPSILON);
   }

   @Test
   public void secodRegisterredSizeShouldSetCountToTwo() {
      m.register(20);
      m.register(20);
      assertEquals("", new Double(2), m.ocurrencesOf(20), EPSILON);
   }

   @Test
   public void shouldHanldeSeveralSizes() {
      m.register(20);
      m.register(30);
      m.register(5);
      m.register(20);
      m.register(5);
      m.register(5);
      
      assertEquals("", new Integer(1), m.ocurrencesOf(30), EPSILON);
      assertEquals("", new Integer(2), m.ocurrencesOf(20), EPSILON);
      assertEquals("", new Integer(3), m.ocurrencesOf(5), EPSILON);
   }
   
   @Test
   public void shouldBeClearable() {
      
      m.register(20);
      m.register(30);
      m.register(5);
      m.register(20);
      m.register(5);
      m.register(5);
      m.clear();
      
      assertNull(m.ocurrencesOf(30));
      assertNull(m.ocurrencesOf(20));
      assertNull(m.ocurrencesOf(5));
   }

   @Test
   public void shouldNormalize() {
      
      m.set(1, 128);  //128
      m.set(2, 64);   //192
      
      m.set(3, 32);   //224
      m.set(4, 16);   //240
      
      m.set(5, 8);    //
      m.set(6, 4);    //
      
      m.set(7, 2);
      m.set(10000, 2);
      
      m.normalize(16);
      
      assertEquals("", 1, m.ocurrencesOf(0), EPSILON);   //16  T=16 
      assertEquals("", 1, m.ocurrencesOf(1), EPSILON);   //32  T
      assertEquals("", 1, m.ocurrencesOf(2), EPSILON);   //48
      assertEquals("", 1, m.ocurrencesOf(3), EPSILON);   //64
      assertEquals("", 1, m.ocurrencesOf(4), EPSILON);   //80
      assertEquals("", 1, m.ocurrencesOf(5), EPSILON);   //96
      assertEquals("", 1, m.ocurrencesOf(6), EPSILON);   //112
      assertEquals("", 1, m.ocurrencesOf(7), EPSILON);   //128 T=128
      assertEquals("", 2, m.ocurrencesOf(8), EPSILON); //144  D=32 T=160
      assertEquals("", 2, m.ocurrencesOf(9), EPSILON);  //160    D=32 T=192
      assertEquals("", 2, m.ocurrencesOf(10), EPSILON); //176    D=32 T=224
      assertEquals("", 2, m.ocurrencesOf(11), EPSILON); //192    D=32 T=256
      assertEquals("", 3, m.ocurrencesOf(12), EPSILON); //208   D=48 T=256+16*3=256+48=304
      assertEquals("", 3, m.ocurrencesOf(13), EPSILON); //224   D=48    T=304+48=352
      assertEquals("", 4, m.ocurrencesOf(14), EPSILON); //240   D=64   T=352+64=416
      assertEquals("", 1254.875, m.ocurrencesOf(15), EPSILON); //256   D=20078     T=416+8*5+4*6+2*7+2*10000 = 20494
      
      assertEquals(256, m.getNumDataPoints());
      assertEquals("", 80.054, m.getAverage(), EPSILON);
   }
}
