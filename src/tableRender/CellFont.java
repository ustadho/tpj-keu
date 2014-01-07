/*
 * CellFont.java
 *
 * Created on December 23, 2006, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tableRender;

/**
 *
 * @author root
 */
import java.awt.Font;


/**
 * @version 1.0 11/22/98
 */

public interface CellFont {
  
  public Font getFont(int row, int column);
  public void setFont(Font font, int row, int column);
  public void setFont(Font font, int[] rows, int[] columns);


}