package com.blogspot.javadots.cexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.blogspot.javadots.cexplorer.FileInputProvider.Entry;


public class FileEntry implements Entry {

   private File file;

   public FileEntry(File f) {
      file = f;
   }

   @Override
   public InputStream getContent() {
      try {
         return new FileInputStream(file);
      } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

}
