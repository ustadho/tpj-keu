/*
 * FrmJenisBarang.java
 *
 * Created on December 5, 2006, 10:45 AM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;
/**
 *
 * @author  root
 */
public class FrmJenisBayar extends javax.swing.JInternalFrame {
    private String idUnit;
    private String nmUnit;
    private String singkatan;
    private String supervisor;
    DefaultTableModel modelJenis;
    private String sClose="close";
    private ListRsbm lst;
    
    static String sID="";
    static String sTgl="";
    private Connection conn;
    private boolean bAsc;
    private String sUserName;
    
    /** Creates new form FrmJenisBarang */
    public FrmJenisBayar() {
        initComponents();
       
    }
    
    public Connection getConn() {
        return conn;
    }
    
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public void initJDBC() {
        String sQry="select kode as kode_jenis, coalesce(alat_pembayaran,'') as nama_jenis," +
                    "coalesce(keterangan,'') as ket_jenis " +
                    "from nota_alat_pembayaran order by 1)";

        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(sQry);
            
        while (rs.next()) {
            modelJenis.addRow(new Object[]{rs.getString("kode_jenis"),
                                        rs.getString("nama_jenis"),
                                        rs.getString("ket_jenis"),
                                        });
                }
            
            SelectionListener listener = new SelectionListener(tblJenis);
            tblJenis.getSelectionModel().addListSelectionListener(listener);
            tblJenis.getColumnModel().getSelectionModel().addListSelectionListener(listener);
            tblJenis.setRequestFocusEnabled(true);
            
            if (modelJenis.getRowCount() > 0) {
                tblJenis.setRowSelectionInterval(0, 0);
            }
            rs.first();
            
            setBEdit(false);
            setBNew(false);
            
            TableLook();
            
        } catch(SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    void setUserName(String s) {
        sUserName=s;
    }
    
    private void TableLook(){
            tblJenis.getColumnModel().getColumn(0).setMaxWidth(70);     //kode
            tblJenis.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblJenis.getColumnModel().getColumn(1).setMaxWidth(150);     //kode
            tblJenis.getColumnModel().getColumn(1).setPreferredWidth(150);
            
            
            tblJenis.setRowHeight(20);
            for (int i=0;i<tblJenis.getColumnCount();i++){
                tblJenis.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
            }
            
            if (modelJenis.getRowCount() > 0) {
                tblJenis.changeSelection(0, 0,false,false);                
            }            
     }
    
    public boolean getAsc(){
        if (!bAsc)
            bAsc=true;
        else
            bAsc=false;
        return bAsc;               
    }
    
    public class myColHeaderList extends MouseAdapter{
            public void mouseClicked(MouseEvent evt) {
            JTable table = ((JTableHeader)evt.getSource()).getTable();
            TableColumnModel colModel = table.getColumnModel();
    
            // The index of the column whose header was clicked
            int vColIndex = colModel.getColumnIndexAtX(evt.getX());
            int mColIndex = table.convertColumnIndexToModel(vColIndex);
    
            // Return if not clicked on any column header
            if (vColIndex == -1) {
                return;
            }
            boolean bSt;
            bSt=getAsc();
            sortAllRowsBy(modelJenis, vColIndex, bSt);
            // Determine if mouse was clicked between column heads
            Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
            if (vColIndex == 0) {
                headerRect.width -= 3;    // Hard-coded constant
            } else {
                headerRect.grow(-3, 0);   // Hard-coded constant
            }
            if (!headerRect.contains(evt.getX(), evt.getY())) {
                // Mouse was clicked between column heads
                // vColIndex is the column head closest to the click
    
                // vLeftColIndex is the column head to the left of the click
                int vLeftColIndex = vColIndex;
                if (evt.getX() < headerRect.x) {
                    vLeftColIndex--;
                }                
            }
        }
    }
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            Color g1 = new Color(239,234,240);//-->>(251,236,177);// Kuning         [251,251,235]
            Color g2 = new Color(239,234,240);//-->>(241,226,167);// Kuning         [247,247,218]
            
             
            Color w1 = new Color(255,255,255);// Putih
            Color w2 = new Color(250,250,250);// Putih Juga
            
            Color h1 = new Color(255,240,240);// Merah muda
            Color h2 = new Color(250,230,230);// Merah Muda
            
            Color g;
            Color w;
            Color h;
            
            if(column%2==0){
                g = g1;
                w = w1;
                h = h1;
            }else{
                g = g2;
                w = w2;
                h = h2;
            }
            setForeground(new Color(0,0,0));
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            if(isSelected){
                //setBackground(new Color(51,102,255));
                setBackground(new Color(248,255,167));
                //setForeground(new Color(255,255,255));
            }
            
            setValue(value);
            return this;
        }
    }
    
    public class ColumnSorter implements Comparator {
        int colIndex;
        boolean ascending;
        ColumnSorter(int colIndex, boolean ascending) {
            this.colIndex = colIndex;
            this.ascending = ascending;
        }
        public int compare(Object a, Object b) {
            Vector v1 = (Vector)a;
            Vector v2 = (Vector)b;
            Object o1 = v1.get(colIndex);
            Object o2 = v2.get(colIndex);
    
            // Treat empty strains like nulls
            if (o1 instanceof String && ((String)o1).length() == 0) {
                o1 = null;
            }
            if (o2 instanceof String && ((String)o2).length() == 0) {
                o2 = null;
            }
    
            // Sort nulls so they appear last, regardless
            // of sort order
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if (o1 instanceof Comparable) {
                if (ascending) {
                    return ((Comparable)o1).compareTo(o2);
                } else {
                    return ((Comparable)o2).compareTo(o1);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareTo(o1.toString());
                }
            }
        }
    } 
    
    public void sortAllRowsBy(DefaultTableModel model, int colIndex, boolean ascending) {
        Vector data = model.getDataVector();
        Collections.sort(data, new ColumnSorter(colIndex, ascending));
        model.fireTableStructureChanged();
        TableLook();
    }
    
    private void pesanError(String Err){
        JOptionPane.showMessageDialog(this,Err,"Message",JOptionPane.ERROR_MESSAGE);
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            switch(keyKode){
                
                case KeyEvent.VK_F2: {  //Save
                    if (getBEdit())
                        udfUpdateData();
                    
                    break;
                }
                
                case KeyEvent.VK_F3: {  //Search
//                    udfFilter();
                    
                    break;
                }
                
                case KeyEvent.VK_F4: {  //Edit
                    udfEdit();
                    break;
                }
                
                case KeyEvent.VK_F5: {  //New -- Add
                    udfNew();
                    break;
                }
                
                case KeyEvent.VK_F6: {  //Filter
//                    onOpen(cmbFilter.getSelectedItem().toString(),true);
                    break;
                }
                
                case KeyEvent.VK_F12: {  //Delete
                    if (!getBEdit() && tblJenis.getRowCount()>0)
                        udfUpdateData();
                    
                    break;
                }
                case KeyEvent.VK_ENTER : {
                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
                        {
                    if (!lst.isVisible()){
                        Component c = findNextFocus();
                        c.requestFocus();
                    }else{
                        lst.requestFocus();
                    }
                    }
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
                        {                        
                            if (!lst.isVisible()){
			    Component c = findNextFocus();
			    c.requestFocus();
                            }else
                                lst.requestFocus();
                            
                            break;
                    }
                }
                case KeyEvent.VK_UP: {
                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
                    {    
                        Component c = findPrevFocus();
                        c.requestFocus();
                    }
                    break;
                }
                
                //lempar aja ke udfCancel
                case KeyEvent.VK_ESCAPE: {
                    //Jika status button adalah Close
                    if(sClose.equalsIgnoreCase("close")){
                        if(!getBEdit()){
                            if(JOptionPane.showConfirmDialog(null,"Anda Yakin Untuk Keluar?","Ustasoft open Source",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                                dispose();
                            }
                        }
                        else
                            if(JOptionPane.showConfirmDialog(null,"Apakah data disimpan sebelum anda keluar?","Ustasoft open Source",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                                udfUpdateData();
                            }
                            else
                                dispose();

                            break;
                    }   //Jika cancel
                    else
                        udfCancel();
                }
            }
        }
    }
    
    public Component findNextFocus() {
        // Find focus owner
        Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        Container root = c == null ? null : c.getFocusCycleRootAncestor();
        
        if (root != null) {
            FocusTraversalPolicy policy = root.getFocusTraversalPolicy();
            Component nextFocus = policy.getComponentAfter(root, c);
            if (nextFocus == null) {
                nextFocus = policy.getDefaultComponent(root);
            }
//            if (lst.isVisible())
//                lst.setVisible(false);
            
            return nextFocus;
                    }
        return null;
    }
    
    public Component findPrevFocus() {
        // Find focus owner
        Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        Container root = c == null ? null : c.getFocusCycleRootAncestor();
        
        if (root != null) {
            FocusTraversalPolicy policy = root.getFocusTraversalPolicy();
            Component prevFocus = policy.getComponentBefore(root, c);
            if (prevFocus == null) {
                prevFocus = policy.getDefaultComponent(root);
            }
//            if (lst.isVisible()) lst.setVisible(false);
            
            return prevFocus;
        }
        return null;
    }
    
    public void setUpBtn(){
        if (getBEdit()) {  //proses edit     
            
            String fileImageSave="/image/Icon/Save.png";
            ButtonIcon(fileImageSave,btnDelete);
            
            String fileImageCancel="/image/Icon/Cancel.png";
            ButtonIcon(fileImageCancel,btnClose);
            
            sClose="cancel";
            btnFirst.setEnabled(false);
            btnPrev.setEnabled(false);
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);

            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);

            btnDelete.setToolTipText("Save    (F5)");
            btnClose.setToolTipText("Cancel");
            System.out.println(getBEdit());
            
            tblJenis.setEnabled(false);
            txtNama.setEditable(true);
            
            tblJenis.requestFocus();

        } else {   //selain edit & NEW
            String fileImageSave="/image/Icon/Delete.png";
            ButtonIcon(fileImageSave,btnDelete);
            
            String fileImageCancel="/image/Icon/Exit.png";
            ButtonIcon(fileImageCancel,btnClose);
            
            sClose="close";
            
            btnFirst.setEnabled(true);
            btnPrev.setEnabled(true);        
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
            
            btnNew.setEnabled(true);
            btnEdit.setEnabled(true);
            
            btnDelete.setToolTipText("Delete     (F12)");
            btnClose.setToolTipText("Close");
            
            tblJenis.setEnabled(true);
            txtNama.setEditable(false);
            
        }        
    }
    
    public void setBNew(Boolean lNew) {
        bNew = lNew;
    }
    
    private void LabelIcon(String aFile,javax.swing.JLabel newlbl) {              
       javax.swing.ImageIcon myIcon = new javax.swing.ImageIcon(getClass().getResource(aFile));
       newlbl.setIcon(myIcon);
   }
    
    private void ButtonIcon(String aFile,javax.swing.JButton newBtn) {              
       javax.swing.ImageIcon myIcon = new javax.swing.ImageIcon(getClass().getResource(aFile));
       newBtn.setIcon(myIcon);
   }
    public Boolean getBNew() {
        return bNew;
    }

    public void setBEdit(Boolean lEdit) {
        bEdit = lEdit;
    }
    
    public Boolean getBEdit() {
        return bEdit;
    }
    
    public String getTanggal(){
        Calendar c = Calendar.getInstance();
        String sekarang="";
	try{
	    final Statement stTgl = conn.createStatement();
	    final ResultSet rtgl = stTgl.executeQuery("select now() as tanggal, current_time as jam");
	    if (rtgl.next()){		
		SimpleDateFormat fdateformat = new SimpleDateFormat("dd-MM-yyyy");
            	sekarang =fdateformat.format(rtgl.getDate(1));    
                c.setTimeInMillis(rtgl.getTime(2).getTime());
                //c.set()
	    }
	    rtgl.close();
	    stTgl.close();
	}catch(SQLException sqtgl){System.out.println(sqtgl.getMessage());}

	MaskFormatter fmt = null;
	try {
	    fmt = new MaskFormatter("##-##-####");
	} catch (java.text.ParseException e) {}
        return sekarang;
    }
    
    private void saveUnit(){
//        JenisBarangBean sBean=new JenisBarangBean();
//        sBean.setConn(conn);
//        sBean.setKode(txtKode.getText());
//        sBean.setNama(txtNama.getText());
        
    }
    
    //Modul untuk RowColChange-nya JTable
     public class SelectionListener implements ListSelectionListener {
         JTable table;
         int rowPos;
         int colPos;
    
        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        SelectionListener(JTable table) {
            this.table = table;
        }
        public void valueChanged(ListSelectionEvent e) {
            rowPos = table.getSelectedRow();           
            
            if  (rowPos >=0 && rowPos < table.getRowCount()) {
                txtKode.setText(table.getValueAt(rowPos,0).toString());
                txtNama.setText(table.getValueAt(rowPos,1).toString());
                txtKet.setText(table.getValueAt(rowPos,2).toString());
                
            }
            if (table.getRowCount()==0){
                txtKode.setText("");
                txtNama.setText("");
                txtKet.setText("");
            }
            
            try{
                
                if (rowPos == 0) { 
                    rs.first(); 
                } else 
                    if (rowPos == (table.getRowCount()-1)) {
                        rs.last();
                } else {
                    rs.absolute(rowPos+1);
                }
                
            System.out.println(rs.getRow());            
            
            } catch(SQLException SE) {}
        }
    }
     
    private void udfUpdateData(){
        //JenisBarangBean nB =new JenisBarangBean();
        int hsl=0;
        
        if (getBEdit()) {
            try {
                conn.setAutoCommit(false);
                if (getBNew()) {        //Add
                    Statement st=conn.createStatement();
                    ResultSet rs=st.executeQuery("select fn_save_jenis_bayar('', '"+txtNama.getText()+"', '"+txtKet.getText()+"')");
                    
                    if(rs.next()){
                        txtKode.setText(rs.getString(1));
                        modelJenis.addRow(new Object[]{txtKode.getText(),
                                                       txtNama.getText(),
                                                       txtKet.getText()
                                               });
                        setBEdit(false);
                        tblJenis.setRowSelectionInterval(modelJenis.getRowCount()-1, 
                                modelJenis.getRowCount()-1);
                        setBEdit(false);
                        setBNew(false);
                        setUpBtn();
                    }else{
                        pesanError("Insert jenis pembayaran gagal. Silakan coba sekali lagi!");
                        System.out.println(hsl);   
                    }
                    rs.close();
                    st.close();
                    
                    //System.out.println(hsl);
            } else {
                    Statement st=conn.createStatement();
                    ResultSet rs=st.executeQuery("select fn_save_jenis_bayar('"+txtKode.getText()+"', " +
                            "                                                   '"+txtNama.getText()+"'," +
                            "                                                   '"+txtKet.getText()+"')");
                    
                    if (!rs.next()){
                        pesanError("Gagal Update!");
                        System.out.println(hsl);   
                        
                    }else{
                        tblJenis.setValueAt(txtNama.getText(), tblJenis.getSelectedRow(), 1);
                        tblJenis.setValueAt(txtKet.getText(), tblJenis.getSelectedRow(), 2);
                        
                        System.out.println(hsl);
                        setBEdit(false);
                        setBNew(false);
                        setUpBtn();
                    }
           }

           conn.commit();
           conn.setAutoCommit(true);
           }catch (SQLException e) {
                try{
                    conn.rollback();
                    conn.setAutoCommit(true);
                }catch(SQLException s){}
                System.out.println(e.getMessage());
                pesanError(e.getMessage());  
            }
        
        } else {    //DELETE
            try {
                String  s=tblJenis.getValueAt(tblJenis.getSelectedRow(),1).toString();
                if(JOptionPane.showConfirmDialog(null,"Anda Yakin Untuk mengapus '" + s +"' ?","Ustasoft open Source",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                
                {    
                        int iPosDel = tblJenis.getSelectedRow();
                        
                        Statement st=conn.createStatement();
                        hsl=st.executeUpdate("delete from nota_alat_pembayaran where kode='"+txtKode.getText()+"'");
                        
                        conn.commit();
                        modelJenis.removeRow(iPosDel);
                        if ( iPosDel < modelJenis.getRowCount() && modelJenis.getRowCount()>0 ) {
                            tblJenis.setRowSelectionInterval(iPosDel, iPosDel);
                            tblJenis.requestFocus();
                    }
                }
            }catch(SQLException se) {
                    try{
                        System.out.println(se.getMessage());
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }catch(SQLException s){
                    pesanError(s.getMessage());
                }
            }
            tblJenis.requestFocus();
        }
    }
    private void udfEdit(){
        if (tblJenis.getRowCount()>0){
            setBEdit(true);
            setBNew(false);
            setUpBtn();
            txtNama.requestFocus();
        }
    }
    
    private void udfNew(){
        setBNew(true);
        setBEdit(true);
        setUpBtn();
        
        txtKode.setText("");
        txtNama.setText("");
        txtNama.requestFocus();
    }
    
    private void udfCancel(){
        if (getBEdit()) {
        setBEdit(false);
        setBNew(false);
        if (tblJenis.getRowCount()>0)
        {
            int rowPos = tblJenis.getSelectedRow();
            txtKode.setText(tblJenis.getValueAt(rowPos,0).toString());
            txtNama.setText(tblJenis.getValueAt(rowPos,1).toString());
        }
        setUpBtn();
        tblJenis.setRequestFocusEnabled(true);
            
        } else {
            this.dispose();
        }
    }
    
    private void udfLoadCmbFilter(){
        cmbFilter.addItem("All");
        cmbFilter.addItem("Kode");
        cmbFilter.addItem("Jenis pembayaran");
    }
    
   private void onOpen(String sQry){
        modelJenis = (DefaultTableModel) tblJenis.getModel();
        int i=0;
        try {
            
            while(modelJenis.getRowCount()>=1){
                modelJenis.removeRow(0);
            }
            
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sQry);
                
            System.out.println(sQry);
            while (rs.next()) {
            modelJenis.addRow(new Object[]{rs.getString("kode_jenis"),
                                        rs.getString("nama_jenis"),
                                        rs.getString("ket_jenis")
                                        });
            }
            
            if(modelJenis.getRowCount()>0)
                tblJenis.setRowSelectionInterval(0, 0);
            
            rs.close();
            st.close();
        } catch (SQLException eswl){ System.out.println(eswl.getMessage());}
        if(i>0){
            tblJenis.requestFocusInWindow();
            tblJenis.setRowSelectionInterval(0,0);
        }
   }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        cmbFilter = new javax.swing.JComboBox();
        cmbOperator = new javax.swing.JComboBox();
        txtFilter = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJenis = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtKode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKet = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Jenis pembayaran");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/New.png"))); // NOI18N
        btnNew.setToolTipText("New     (F5)");
        btnNew.setBorder(null);
        btnNew.setMaximumSize(new java.awt.Dimension(40, 40));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Edit.png"))); // NOI18N
        btnEdit.setToolTipText("Edit     (F4)");
        btnEdit.setBorder(null);
        btnEdit.setMaximumSize(new java.awt.Dimension(40, 40));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Delete.png"))); // NOI18N
        btnDelete.setToolTipText("New     (F12)");
        btnDelete.setBorder(null);
        btnDelete.setMaximumSize(new java.awt.Dimension(40, 40));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDelete);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Exit.png"))); // NOI18N
        btnClose.setToolTipText("New     (F12)");
        btnClose.setBorder(null);
        btnClose.setMaximumSize(new java.awt.Dimension(40, 40));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClose);

        jPanel1.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 50));

        cmbFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Kode", "Jenis Barang" }));
        cmbFilter.setMaximumSize(new java.awt.Dimension(150, 24));
        cmbFilter.setMinimumSize(new java.awt.Dimension(20, 24));
        cmbFilter.setPreferredSize(new java.awt.Dimension(20, 24));
        jToolBar2.add(cmbFilter);

        cmbOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">", ">=", "<", "<=", "Like" }));
        cmbOperator.setMaximumSize(new java.awt.Dimension(150, 24));
        cmbOperator.setMinimumSize(new java.awt.Dimension(20, 24));
        cmbOperator.setPreferredSize(new java.awt.Dimension(20, 24));
        jToolBar2.add(cmbOperator);

        txtFilter.setMaximumSize(new java.awt.Dimension(200, 24));
        txtFilter.setMinimumSize(new java.awt.Dimension(4, 4));
        jToolBar2.add(txtFilter);

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Filter.png"))); // NOI18N
        btnFilter.setToolTipText("Fiter      (F6)");
        btnFilter.setBorder(null);
        btnFilter.setMaximumSize(new java.awt.Dimension(40, 40));
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        jToolBar2.add(btnFilter);

        txtSearch.setMaximumSize(new java.awt.Dimension(200, 24));
        txtSearch.setMinimumSize(new java.awt.Dimension(4, 4));
        jToolBar2.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Search.png"))); // NOI18N
        btnSearch.setToolTipText("Search     (F3)");
        btnSearch.setBorder(null);
        btnSearch.setMaximumSize(new java.awt.Dimension(40, 40));
        btnSearch.setMinimumSize(new java.awt.Dimension(40, 40));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSearch);

        jPanel1.add(jToolBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 553, 50));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tblJenis.setAutoCreateRowSorter(true);
        tblJenis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode", "Jenis Pembayaran", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblJenis);

        jPanel2.setBackground(new java.awt.Color(249, 238, 180));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtKode.setFont(new java.awt.Font("Dialog", 1, 12));
        txtKode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKode.setEnabled(false);
        jPanel2.add(txtKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 9, 100, 24));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Kode");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 12, 90, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Jns Pembayaran");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 38, 90, -1));

        txtNama.setFont(new java.awt.Font("Dialog", 1, 12));
        txtNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 36, 470, 24));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Keterangan");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 65, 90, -1));

        txtKet.setFont(new java.awt.Font("Dialog", 1, 12));
        txtKet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(txtKet, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 63, 620, 24));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnFirst.setText("First");
        btnFirst.setPreferredSize(new java.awt.Dimension(65, 25));
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });
        jPanel3.add(btnFirst);

        btnPrev.setText("Prev");
        btnPrev.setPreferredSize(new java.awt.Dimension(65, 25));
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });
        jPanel3.add(btnPrev);

        btnNext.setText("Next");
        btnNext.setPreferredSize(new java.awt.Dimension(65, 25));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jPanel3.add(btnNext);

        btnLast.setText("Last");
        btnLast.setPreferredSize(new java.awt.Dimension(65, 25));
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });
        jPanel3.add(btnLast);

        jLabel5.setBackground(new java.awt.Color(0, 0, 255));
        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 1, 20));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Jenis Pembayaran");
        jLabel5.setOpaque(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(4, 4, 4)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(5, 5, 5)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(9, 9, 9)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-760)/2, (screenSize.height-501)/2, 760, 501);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isJenisBayarOn=false;
        //if (lst.isVisible()) lst.setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        modelJenis=(DefaultTableModel)tblJenis.getModel();
        modelJenis.setNumRows(0)    ;
        tblJenis.setModel(modelJenis);
        
        initJDBC(); 
        udfLoadCmbFilter();
        setBEdit(false);
        setBNew(false);
        
        for(int i=0;i<jToolBar2.getComponentCount();i++){
            Component c = jToolBar2.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
        for(int i=0;i<jToolBar1.getComponentCount();i++){
            Component c = jToolBar1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
         
         jScrollPane1.addKeyListener(new MyKeyListener());
         tblJenis.addKeyListener(new MyKeyListener());
         
        System.out.println(jPanel1.getComponentCount());
        for(int i=0;i<jPanel1.getComponentCount();i++){
            Component c = jPanel1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton")   || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
        
        System.out.println(jPanel2.getComponentCount());
        for(int i=0;i<jPanel2.getComponentCount();i++){
            Component c = jPanel2.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton")   || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
        
        setUpBtn();
        requestFocusInWindow(true);
        tblJenis.requestFocusInWindow();
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        tblJenis.setRowSelectionInterval(tblJenis.getRowCount()-1,tblJenis.getRowCount()-1);
        try {
            rs.last();
        } catch(SQLException e) {}
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if (tblJenis.getSelectedRow()<tblJenis.getRowCount()-1) {
            tblJenis.setRowSelectionInterval(tblJenis.getSelectedRow()+1,tblJenis.getSelectedRow()+1);
            try {
                rs.next();
            } catch(SQLException e) {}
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        if (tblJenis.getSelectedRow()>0) {
            tblJenis.setRowSelectionInterval(tblJenis.getSelectedRow()-1,tblJenis.getSelectedRow()-1);
            try {
                rs.previous();
            } catch(SQLException e) {}
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        tblJenis.setRowSelectionInterval(0,0);
        try {
            rs.first();
            System.out.println(rs.getRow());
        } catch(SQLException e) {}
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String sQry="select kode as kode_jenis, coalesce(alat_pembayaran,'') as nama_jenis," +
                    "coalesce(keterangan,'') as ket_jenis " +
                    "from nota_alat_pembayaran where (kode||coalesce(alat_pembayaran,'')) iLike '%"+txtSearch.getText().toUpperCase()+"%' " +
                    "order by 1";
        
        System.out.println(sQry);
        onOpen(sQry);
        txtSearch.requestFocus();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        String sField="";
        switch(cmbFilter.getSelectedIndex()){
            case 0:{
                sField="(kode||coalesce(alat_pembayaran,'') ) iLike ";
                break;
            }
            case 1:{
                sField="kode= ";
                break;
            }
            case 2:{
                sField="alat_pembayaran= ";
                break;
            }
            default:{
                sField="alat_pembayaran= ";
                break;
            }
                
        }
        String sQry="select kode as kode_jenis, coalesce(alat_pembayaran,'') as nama_jenis," +
                    "coalesce(keterangan,'') as ket_jenis " +
                    "from nota_alat_pembayaran where "+sField+" '"+txtFilter.getText().toUpperCase()+"' " +
                    "order by 1";
        
        System.out.println(sQry);
        onOpen(sQry);
        txtSearch.requestFocus();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        udfCancel();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (tblJenis.getRowCount()>0 || getBEdit())
            udfUpdateData();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        udfEdit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        udfNew();
    }//GEN-LAST:event_btnNewActionPerformed

    void setCon(Connection conn) {
        this.conn=conn;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox cmbFilter;
    private javax.swing.JComboBox cmbOperator;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTable tblJenis;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JTextField txtKet;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    private Statement st;
    private ResultSet rs;
    private Boolean bNew ;
    private Boolean bEdit;
}
