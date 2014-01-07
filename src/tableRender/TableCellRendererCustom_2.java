package tableRender;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;


public class TableCellRendererCustom_2 extends DefaultTableCellRenderer 
{
    public Component getTableCellRendererComponent
       (JTable table, Object value, boolean isSelected,
       boolean hasFocus, int row, int column) 
    {
        
        Component tcell = super.getTableCellRendererComponent
           (table, value, isSelected, hasFocus, row, column);
        JLabel cell=(JLabel) tcell;   
            if( (row % 2) == 0 )
            {
                cell.setBackground(new java.awt.Color(204,204,255));
                cell.setForeground(new java.awt.Color(0, 0, 0));
                
                if (column==6){
                    cell.setForeground(Color.MAGENTA);
                    cell.setFont(new java.awt.Font("Tahoma", 1, 11));
                }
                if (column==7){
                    cell.setForeground(Color.RED);
                    cell.setFont(new java.awt.Font("Tahoma", 1, 11));
                } 
                // You can also customize the Font and Foreground this way
                // cell.setForeground();
                // cell.setFont();
            }
            else
            {
                cell.setBackground(new java.awt.Color(204,255,204));
                cell.setForeground(new java.awt.Color(0, 0, 0));
                if (column==6){
                    cell.setForeground(Color.MAGENTA);
                    cell.setFont(new java.awt.Font("Tahoma", 1, 11));
                }
                if (column==7){
                    cell.setForeground(Color.RED);
                    cell.setFont(new java.awt.Font("Tahoma", 1, 11));
                }
            }
            
            if (isSelected)
            {
                    cell.setForeground(Color.black);
                    if (column>=3 && column<=5)
                    {cell.setBackground(new java.awt.Color(255,255,0));}
                    else if (column==7){cell.setBackground(Color.BLACK);
                            cell.setForeground(Color.WHITE);
                         }
                        else{cell.setBackground(Color.WHITE);}
                    
            }
        if (value !=null && value.getClass().getSimpleName().equalsIgnoreCase("Integer"))
        {cell.setHorizontalAlignment(RIGHT);}
        else if (value !=null && value.getClass().getSimpleName().equalsIgnoreCase("Float"))
        {cell.setHorizontalAlignment(CENTER);}else{cell.setHorizontalAlignment(LEFT);}
        return cell;

    }
}