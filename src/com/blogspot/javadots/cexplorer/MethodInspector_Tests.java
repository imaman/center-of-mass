package com.blogspot.javadots.cexplorer;

import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class MethodInspector_Tests {

   @Test
   public void shouldCountMethods()
   {
      Collector c = new Collector();
      c.decorate = false;
      MethodInspector mi = new MethodInspector(c);
      
      mi.inspect(getClass().getClassLoader().getResourceAsStream(Sample.class.getName().replace('.', '/') + ".class"));
      
      System.out.println(c.map);
      int sizeM1 = c.map.get("m1");
      int sizeM2 = c.map.get("m2");
      int sizeCtor = c.map.get("<init>");
      
      assertTrue("sizeM1="  + sizeM1 + " sizeCtor=" + sizeCtor, sizeM1 < sizeCtor);
      assertTrue(sizeCtor < sizeM2);
   }
   
   public static class Sample {
      public void m1() { }
      public void m2() { 
         System.out.println("blah-blah");
         for(int i = 0; i < 10; ++i)
            System.out.println("blah-blah");
      }
      public Sample() { }      
   }
}
