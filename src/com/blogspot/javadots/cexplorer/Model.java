package com.blogspot.javadots.cexplorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A distribution chart: tracks how many times a certain 'size' value was encountered.
 */
public class Model {
   
   private Map<Integer, Integer> countFromSize = new HashMap<Integer, Integer>();
   private Map<Integer, Double> normal;
   private int numDataPoints = -1;
   private double average;

   public int getNumDataPoints() {
      return numDataPoints ;
   }
   public void set(int size, int count) {
      countFromSize.put(size, count);
   }

   public Double ocurrencesOf(int key) {
      if(normal != null) 
         return normal.get(key);
      
      Integer n = countFromSize.get(key);
      if(n == null)
         return null;
      
      return (double) n.intValue();
   }

   public Collection<Integer> sizes() {
      if(normal != null)
         return new ArrayList<Integer>(normal.keySet());
      return new ArrayList<Integer>(countFromSize.keySet());
   }

   public void register(int size) {
      Integer count = countFromSize.get(size);
      if(count == null)
         count = 0;
      ++count;
      countFromSize.put(size, count);
   }

   public void clear() {
      countFromSize.clear();
      if(normal != null)
         normal.clear();
   }

   public static class Data implements Comparable<Data> {
      public final int size;
      public int count;
      
      public Data(int size, int count) {
         this.size = size;
         this.count = count;
      }

      @Override
      public int compareTo(Data that) {
         return this.size - that.size;
      }
      
      @Override
      public String toString() {
         return "value=" + size + " x" + count;
      }
      
      
   }
   public void normalize(int parts) {
      List<Data> ds = new ArrayList<Data>();
      double totalMass = 0.0;
      numDataPoints = 0;
      for(int s : countFromSize.keySet()) {
         int c = countFromSize.get(s);
         Data d = new Data(s, c);
         if(d.count == 0)
            continue;
         
         ds.add(d);
         numDataPoints += d.count;
         totalMass += d.size * d.count;
      }
      
      average = totalMass / numDataPoints;
      
      Collections.sort(ds);
      
      
      
      normal = new HashMap<Integer,Double>();
      
      int window = numDataPoints / parts;
      int left = ds.size() - window * parts;
      int index = 0;
      int i = 0;
      double chunkMass = 0.0;
      int itemsSoFar = 0;
      int currentChunk = window + (left > 0 ? 1 : 0);
      left -= (left > 0) ? 1 : 0;
      while(i < parts && index < ds.size()) {
         
         Data d = ds.get(index);
         if(d.count == 0) {
            index++;
            continue;
         }
         
         d.count -= 1;
         itemsSoFar += 1;
         chunkMass += d.size;
         currentChunk -= 1;
         
         if(currentChunk > 0) 
            continue;
         
         normal.put(i,100.0 * chunkMass / totalMass);
         
         ++i;
         currentChunk = window + (left > 0 ? 1 : 0);
         left -= (left > 0) ? 1 : 0;  
         
         chunkMass = 0.0;
         itemsSoFar = 0;
      }
   }
   public double getAverage() {
      return average;
   }
}
