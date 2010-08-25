/**
 * 
 */
package com.blogspot.javadots.cexplorer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileInputProvider implements InputProvider
{
   private final Collector collector;
   
   private final List<Entry> entries = new ArrayList<Entry>();

   public FileInputProvider(Collector c) {
      collector = c;
   }
   
   public interface Entry {
      public InputStream getContent();
   }
   
   @Override
   public List<String> paths() {
      List<String> res = new ArrayList<String>();
      for(int i = 0; i < entries.size(); ++i)
         res.add("" + i);
      return res;
   }

   @Override
   public void setRoot(String root) {
      entries.clear();
      scan(new File(root).getAbsoluteFile());
   }      
   
   private void scan(File f) {
      if(f.isDirectory()) {
         for(File curr : f.listFiles())
            scan(curr);
         return;
      }
      
      if(f.getName().endsWith(".class"))
         entries.add(new FileEntry(f));
      else if(f.getName().endsWith(".jar"))
         scanJar(f);
   }

   private void scanJar(File f) {
      
      JarFile jarFile;
      try {
         jarFile = new JarFile(f);
      } catch (IOException e) {
         return;
         //throw new RuntimeException(e);
      }
      
      for(Enumeration<JarEntry> all = jarFile.entries(); all.hasMoreElements(); ) {
         JarEntry curr = all.nextElement();
         if(curr.getName().endsWith(".class"))
            entries.add(new JarFileEntry(jarFile, curr));
      }
   }

   @Override
   public void process(String path) {
      Integer n = Integer.parseInt(path);
      Entry e = entries.get(n);
      
      if(e == null)
         throw new AssertionError("path=" + path + " files.size()=" + entries.size());
      
      MethodInspector mi = new MethodInspector(collector);
      mi.inspect(e.getContent());
   }
}