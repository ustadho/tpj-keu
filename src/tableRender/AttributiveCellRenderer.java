/*
 * AttributiveCellRenderer.java
 *
 * Created on December 23, 2006, 12:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tableRender;

/**
 *
 * @author root
 */
/*
 * (swing1.1beta3)
 */
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;


/**
 * @version 1.0 11/22/98
 */
public class AttributiveCellRenderer extends JLabel
    implements TableCellRenderer {
  protected static Border noFocusBorder; 
 
  public AttributiveCellRenderer() {
    noFocusBorder = new EmptyBorder(1, 2, 1, 2);
    setOpaque(true);
    setBorder(noFocusBorder);  
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                 boolean isSelected, boolean hasFocus, int row, int column) {
    Color foreground = null;
    Color background = null;
    Font font = null;
    TableModel model = table.getModel();
    if (model instanceof AttributiveCellTableModel) {
      CellAttribute cellAtt = ((AttributiveCellTableModel)model).getCellAttribute();
      if (cellAtt instanceof ColoredCell) {
	foreground = ((ColoredCell)cellAtt).getForeground(row,column);
	background = ((ColoredCell)cellAtt).getBackground(row,column);
      }
      if (cellAtt instanceof CellFont) {
	font = ((CellFont)cellAtt).getFont(row,column);
      }
    }
    if (isSelected) {
      setForeground((foreground != null) ? foreground
                          : table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground((foreground != null) ? foreground 
			  : table.getForeground());
      setBackground((background != null) ? background 
			  : table.getBackground());
    }
    setFont((font != null) ? font : table.getFont());
    
    if (hasFocus) {
      setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
      if (table.isCellEditable(row, column)) {
	setForeground((foreground != null) ? foreground
	              : UIManager.getColor("Table.focusCellForeground") );
	setBackground( UIManager.getColor("Table.focusCellBackground") );
      }
    } else {
      setBorder(noFocusBorder);
    }
    setValue(value);        
    return this;
  }
    
  protected void setValue(Object value) {
    setText((value == null) ? "" : value.toString());
  }
}


