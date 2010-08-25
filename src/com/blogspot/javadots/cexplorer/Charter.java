package com.blogspot.javadots.cexplorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Charter implements ModelProcessor {

   private int width = 500;
   private int height = 500;
   private String chartType = "lxy";
   private boolean showAxisLabels = false;
   private boolean percentage;
   private boolean showKeys = true;
   
   public void setCharType(String arg) {
      chartType = arg;
   }
   
   public void showAxisLabels(boolean b) { 
      showAxisLabels = b;
   }

   public String asUrlString(Model model) {
      String s = "http://chart.apis.google.com/chart?cht=" + chartType + "&chs=" + width + "x" + height 
         + "&chd=t:";
      
      List<Integer> keys = new ArrayList<Integer>();
      keys.addAll(model.sizes());
      Collections.sort(keys);
      
      if(showKeys )
         s += toStr(keys) + "|";
      
      List<Double> counts = new ArrayList<Double>();
      for(int n : keys) 
         counts.add(round(model.ocurrencesOf(n)));
      
      s += toStr(counts);
      
      String axis = "&chxt=x,y";
      if(percentage)
         s += axis + "&chds=0,100.0&chxr=0,0," + max(keys) + "|1,0,100.0";
      else if(showAxisLabels)
         s += axis + "&chds=0," + max(counts) + "&chxr=0," + min(keys) + "," + max(keys) + "|1,0," + max(counts);
      
      for(Map.Entry<String,String> e : atts.entrySet())
         s += "&" + e.getKey() + "=" + e.getValue();
      
      return s;
   }
   

   public static double round(double d) {
      d = Math.round(d * 1000.0);
      return d / 1000.0;
      
   }

   private<T extends Comparable<T>> T min(List<T> ts) {
      T result = null;
      for(T t : ts) {
         if(result == null)
            result = t;
         else if(t.compareTo(result) < 0)
            result = t;
      }
      return result;
   }

   private<T extends Comparable<T>> T max(List<T> ts) {
      T result = null;
      for(T t : ts) {
         if(result == null)
            result = t;
         else if(t.compareTo(result) > 0)
            result = t;
      }
      return result;
   }

   private String toStr(List<? extends Number> ns) {
      String res = "";
      for(Number n : ns) 
         res += res.length() == 0 ? n : "," + n;
      return res;
   }

   public void setWidth(int w) {
      width = w;
   }

   public void setHeight(int h) {
      height = h;
   }

   public void setPercentage(boolean b) {
      percentage = b;      
   }
   
   public void setShowKeys(boolean b) {
      showKeys = b;
   }

   private final Map<String,String> atts = new HashMap<String,String>();
   
   public void set(String key, String value) {
      atts.put(key, value);
   }
   
   
}
