package com.blogspot.javadots.cexplorer;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.blogspot.javadots.cexplorer.FileInputProvider.Entry;


public class JarFileEntry implements Entry {

   private JarEntry je;
   private JarFile jarFile;

   public JarFileEntry(JarFile jarFile, JarEntry e) {
      this.jarFile = jarFile;
      je = e;
   }

   @Override
   public InputStream getContent() {
      try {
         return jarFile.getInputStream(je);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
