package tableRender;
/*
 * GroupHeader.java
 *
 * Created on March 11, 2007, 10:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author dwikk
 */
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

//import menufakultas.ColumnGroup;
//import menufakultas.GroupableTableHeader;

/**
 * Example demonstating implementing groupable JTable headers.
 * 
 * @author mick http://www.objects.com.au
 *
 */
public class GroupableHeaderExample extends JApplet
{
	public void init()
	{
		UIManager.put(GroupableTableHeader.uiClassID, "menufakultas.GroupableTableHeaderUI");
		DefaultTableModel model = new DefaultTableModel(new Object[][] {
				{ "Bob", "Jones", "Builder", "Sydney", "NSW", "9876543" },
				{ "Jane", "Smith", "Plumber", "Tilba Tilba", "NSW", "123456" },
				{ "Jim", "Brown", "Nurse", "Darwin", "NT", "6666666" }},
				new Object[] { "First", "Last", "Occupation", "City", "State", "Phone" });
		JTable table = new JTable(model);
		GroupableTableHeader header = new GroupableTableHeader(table.getColumnModel());
		TableColumnModel columns = table.getColumnModel();
		ColumnGroup name = new ColumnGroup("Name");
		name.add(columns.getColumn(0));
		name.add(columns.getColumn(1));
		ColumnGroup contact = new ColumnGroup("Contact");
		ColumnGroup address = new ColumnGroup("Address");
		address.add(columns.getColumn(3));
		address.add(columns.getColumn(4));
		contact.add(address);
		contact.add(columns.getColumn(5));
		
		header.addGroup(name);
		header.addGroup(contact);
		table.setTableHeader(header);
		
		getContentPane().add(new JScrollPane(table));
	}
	
	public static void main(String[] args)
	{
		UIManager.put(GroupableTableHeader.uiClassID, "menufakultas.GroupableTableHeaderUI");
		JFrame main = new JFrame("groupable header");
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GroupableHeaderExample content = new GroupableHeaderExample();
		content.init();
		main.setContentPane(content);
		main.pack();
		main.setVisible(true);
	}
}
