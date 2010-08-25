/**
 * 
 */
package com.blogspot.javadots.cexplorer;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class PngOutput implements Output
{
   private String result;      
   private JLabel label = new JLabel();
   
   public PngOutput() {
      label.setBackground(Color.WHITE);
      label.setOpaque(true);
      label.setBorder(BorderFactory.createEmptyBorder());
   }
   
   @Override
   public String getResult() {
      return result;
   }

   @Override
   public Component getWidget() {
      return label;
   }

   @Override
   public void setResult(String arg) {
      System.out.println("result=" + arg);
      result = arg;         
      try {
         ImageIcon ic = new ImageIcon(new URL(arg));
         label.setIcon(ic);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
}