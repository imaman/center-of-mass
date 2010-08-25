/**
 * 
 */
package com.blogspot.javadots.cexplorer;

import java.awt.Component;

public interface Output
{
   public void setResult(String result);
   public String getResult();
   public Component getWidget();      
}