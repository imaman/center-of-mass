package com.blogspot.javadots.cexplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class Chater_Tests {

   @Test
   public void sholdProduceEmptyChart()
   {
      Model model = new Model();
      
      assertEquals("http://chart.apis.google.com/chart?cht=lxy&chs=500x500&chd=t:|", 
         new Charter().asUrlString(model));
   }

   @Test
   public void sholdHandleWidthHeight()
   {
      Model model = new Model();
      
      Charter charter = new Charter();
      charter.setWidth(923);
      charter.setHeight(107);
      
      
      assertEquals("http://chart.apis.google.com/chart?cht=lxy&chs=923x107&chd=t:|", 
         charter.asUrlString(model));
   }
   

   @Test
   public void sholdProduceSimpleChart()
   {
      Model model = new Model();
      
      model.set(10, 100);
      model.set(20, 20);
      
      
      assertEquals("http://chart.apis.google.com/chart?cht=lxy&chs=500x500&chd=t:10,20|100.0,20.0", 
         new Charter().asUrlString(model));
   }


   @Test
   public void sholdRespectChartType()
   {
      Model model = new Model();
      Charter ch = new Charter();
      ch.setCharType("__XXY__");
      
      assertEquals("http://chart.apis.google.com/chart?cht=__XXY__&chs=500x500&chd=t:|", 
         ch.asUrlString(model));
   }


   @Test
   public void sholdProduceAxisLabels()
   {
      Model model = new Model();
      model.set(0, 300);
      model.set(1, 20);
      

      Charter ch = new Charter();
      ch.showAxisLabels(true);
      ch.setCharType("bvs");
      
      assertEquals("http://chart.apis.google.com/chart?cht=bvs&chs=500x500&chd=t:0,1|300.0,20.0&chxt=x,y&chds=0,300.0&chxr=0,0,1|1,0,300.0", 
         ch.asUrlString(model));
   }

   @Test
   public void sholdRespectPercentage()
   {
      Model model = new Model();
      model.set(0, 300);
      model.set(1, 20);
      

      Charter ch = new Charter();
      ch.setPercentage(true);
      ch.setCharType("bvs");
      
      assertEquals("http://chart.apis.google.com/chart?cht=bvs&chs=500x500&chd=t:0,1|300.0,20.0&chxt=x,y&chds=0,100.0&chxr=0,0,1|1,0,100.0", 
         ch.asUrlString(model));
   }
}
