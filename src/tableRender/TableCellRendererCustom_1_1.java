package tableRender;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class TableCellRendererCustom_1_1 extends DefaultTableCellRenderer 
{
    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) 
    {
        
        Component tcell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
           JLabel cell = (JLabel) tcell;
            if( (row % 2) == 0 )
            {
                cell.setBackground(new java.awt.Color(204,204,255));
                cell.setForeground(new java.awt.Color(0,0,0));
                
                // You can also customize the Font and Foreground this way
                // cell.setForeground();
                // cell.setFont();
            }
            else
            {
                cell.setBackground(new java.awt.Color(153,153,255));
                cell.setForeground(new java.awt.Color(153,0,204));
            }
            
            if (isSelected)
            {
                    cell.setBackground(new java.awt.Color(255,255,0));
                    //cell.setBackground( Color.LIGHT_GRAY);
                    cell.setForeground(Color.black);
            }
           if (value != null){
            if(value.getClass().getSimpleName().equalsIgnoreCase("Integer")){
                cell.setHorizontalAlignment(LEFT);
            }
           } 
        return cell;

    }
}