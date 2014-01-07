/*
 * CellSpan.java
 *
 * Created on December 23, 2006, 11:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tableRender;

import java.awt.Color;

/**
 *
 * @author root
 */
public interface CellSpan {
  public final int ROW    = 0;
  public final int COLUMN = 1;
  
  public int[] getSpan(int row, int column);
  public void setSpan(int[] span, int row, int column);
  
  public boolean isVisible(int row, int column);
  
  public void combine(int[] rows, int[] columns);
  public void split(int row, int column);
  

}
