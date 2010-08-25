/**
 * 
 */
package com.blogspot.javadots.cexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config 
{
   private File propsFile;
   private Properties props = new Properties();
   public final File home;
   
   public Config(String fileName) {
      home = new File(System.getProperty("user.home")).getAbsoluteFile();
      propsFile = new File(home, fileName);
      
      try {
         FileInputStream fis = new FileInputStream(propsFile);
         props.load(fis);
         fis.close();
      } catch (Exception e) {
         //
      }
   }
   
   public void store() {
      try {
         FileOutputStream fos = new FileOutputStream(propsFile);
         props.store(fos, "");
         fos.close();
      } catch (IOException e) {
         //
      }
   }
   
   public String get(String key, String defaultValue) {
      String result = props.getProperty(key);
      if(result == null)
         result = defaultValue;
      return result;
   }
   
   public void set(String key, String value) {
      props.setProperty(key, value);
   }
   
}