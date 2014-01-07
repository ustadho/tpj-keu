/*
 * ColoredCell.java
 *
 * Created on December 23, 2006, 11:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tableRender;

/**
 *
 * @author root
 */
import java.awt.Color;


/**
 * @version 1.0 11/22/98
 */

public interface ColoredCell {
  
  public Color getForeground(int row, int column);
  public void setForeground(Color color, int row, int column);
  public void setForeground(Color color, int[] rows, int[] columns);

  public Color getBackground(int row, int column);
  public void setBackground(Color color, int row, int column);
  public void setBackground(Color color, int[] rows, int[] columns);


}
