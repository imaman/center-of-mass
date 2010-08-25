package com.blogspot.javadots.cexplorer;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.junit.testcase.FestSwingJUnitTestCase;
import org.junit.Test;

public class UI_Tests extends FestSwingJUnitTestCase  
{

   private FrameFixture window;
   private UI frame;
   protected Collector collector = new Collector();
   
   InputProvider ip = new InputProvider() {
      
      @Override
      public void setRoot(String root) {
      }
      
      @Override
      public void process(String path) {
         collector.collect("a", 111);
         collector.collect("b", 222);
         collector.collect("aa", 111);
      }
      
      @Override
      public List<String> paths() {
         return Arrays.asList("p1", "p2", "p3");
      }
   };
   
   
   
   @Override
   protected void onSetUp() {
      frame = GuiActionRunner.execute(new GuiQuery<UI>() {
         @Override
         protected UI executeInEDT() {
            return new UI(collector);
         }
      });
      window = new FrameFixture(robot(), frame);
      window.show(); 
   }

   @Override
   public void onTearDown() {
      window.cleanUp();
   }

   @Test
   public void shouldOpenFileAndShowItsContent() {
      
      String PATH = "something.txt";
      
      frame.setProvider(ip);
      collector.decorate = false;
      
      
      frame.setModelProcessor(new ModelProcessor() {
         
         @Override
         public String asUrlString(Model model) {
            return "" + model.ocurrencesOf(111) + ";" + model.ocurrencesOf(222);
         }
      });
      
      window.menuItemWithPath("File", "Open").click().robot.waitForIdle();
      robot().waitForIdle();
      
      
      JFileChooserFixture fc = window.fileChooser("open");
      fc.selectFile(new File(PATH));
      fc.approve();
      robot().waitForIdle();
      
      assertEquals("2.0;1.0", frame.output.getResult());
   }
}
