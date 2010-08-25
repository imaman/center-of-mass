package com.blogspot.javadots.cexplorer;

import java.util.List;

public interface InputProvider {

   void process(String path);
   
   List<String> paths();

   void setRoot(String root);
}
