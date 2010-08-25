/**
 * 
 */
package com.blogspot.javadots.cexplorer;

import java.util.HashMap;
import java.util.Map;

public class Collector {
   public final Map<String,Integer> map = new HashMap<String,Integer>();
   
   public boolean decorate = true;

//   private static final Random random = new Random();
   
   public void collect(String methodName, int size) {
//      size = random.nextInt(20);
      map.put((decorate ? (map.size() + "#") : "") + methodName, size);
   }      
   
   public void fill(Model m) {
      for(String methodName : map.keySet()) {
         Integer size = map.get(methodName);
         m.register(size);
      }
   }

   public void clear() {
      map.clear();
   }
}