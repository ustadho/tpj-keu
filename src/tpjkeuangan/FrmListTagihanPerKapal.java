/*
 * FrmCustomer.java
 *
 * Created on November 6, 2007, 9:54 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author  oestadho
 */
public class FrmListTagihanPerKapal extends javax.swing.JInternalFrame {
    Connection conn;
    private String sUser;
    JTextField tx=new JTextField();
    ListRsbm lst;
    
    
    /** Creates new form FrmCustomer */
    public FrmListTagihanPerKapal() {
        initComponents();
    }

    void setUserName(String sUserName) {
        sUser=sUserName;
    }
    
    private void onOpen(String sQry){
        sQry="select * from fn_nota_per_kapal_rekap('', '') as " +
             "(kode_cust varchar, customer varchar, jml_invoice float8, jml_terbayar float8, sisa float8)";

        modelPenerimaan = (DefaultTableModel) tblPenerimaan.getModel();
        int i=1;
        try {
            
            modelPenerimaan.setNumRows(0);
            
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sQry);
                
            System.out.println(sQry);
            
            
            while (rs.next()) {
                modelPenerimaan.addRow(new Object[]{
                    i,
                    rs.getString("kode_cust"),
                    rs.getString("customer"),
                    rs.getFloat("jml_invoice"),
                    rs.getFloat("jml_terbayar"),
                    rs.getFloat("sisa")
                });
                i++;
            }
            
            if(modelPenerimaan.getRowCount()>0)
                tblPenerimaan.setRowSelectionInterval(0, 0);
            
            rs.close();
            st.close();
        } catch (SQLException eswl){ System.out.println(eswl.getMessage());}
        if(tblPenerimaan.getRowCount()>0){
            tblPenerimaan.requestFocusInWindow();
            tblPenerimaan.setRowSelectionInterval(0,0);
        }
   }
    
    private void udfPrint() {
        {
            int iRow=tblPenerimaan.getSelectedRow();
            
            if(iRow<0){
                return;
            }
            
            HashMap reportParam = new HashMap();
            FileInputStream file = null;
            String tglAwal = dformat.format(jcTglAwal.getDate());
            //String tglAkhir = dformat.format(jcTglAkhir.getDate());

            reportParam.put("no_nota", tblPenerimaan.getValueAt(iRow, 2));

            try {
                //ashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                
                jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/nota_penerimaan.jasper"));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                JasperViewer.viewReport(print, false);
                
                    
                    
               
            } catch (JRException je) {
                System.out.println(je.getMessage());
            }
            
            
        }
        //catch(NullPointerException ne){JOptionPane.showMessageDialog(null, ne.getMessage(), "SHS Open Source", JOptionPane.OK_OPTION);}
        
    }
    
    private void udfPrintPackingList() {
        {
            int iRow=tblPenerimaan.getSelectedRow();
            
            if(iRow<0){
                return;
            }
            
            HashMap reportParam = new HashMap();
            FileInputStream file = null;
            
            reportParam.put("nota", tblPenerimaan.getValueAt(iRow, 2));

            try {
                //ashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                
                jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_nota.jasper"));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                JasperViewer.viewReport(print, false);
               
            } catch (JRException je) {
                System.out.println(je.getMessage());
            }
            
            
        }
        //catch(NullPointerException ne){JOptionPane.showMessageDialog(null, ne.getMessage(), "SHS Open Source", JOptionPane.OK_OPTION);}
        
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            switch(keyKode){
                
                case KeyEvent.VK_F2: {  //Save
//                    if (getBEdit())
//                        udfUpdateData();
//                    
//                    break;
                }
                
                case KeyEvent.VK_F3: {  //Search
//                    udfFilter();
                    
                    break;
                }
                
                case KeyEvent.VK_F4: {  //Edit
//                    udfEdit();
//                    break;
                }
                
                case KeyEvent.VK_F5: {  //New -- Add
//                    udfNew();
//                    break;
                }
                
                case KeyEvent.VK_F6: {  //Filter
//                    onOpen(cmbFilter.getSelectedItem().toString(),true);
                    break;
                }
                
                case KeyEvent.VK_F12: {  //Delete
//                    if (!getBEdit() && tblCustomer.getRowCount()>0)
//                        udfUpdateData();
//                    
//                    break;
                }
                case KeyEvent.VK_ENTER : {
//                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
//                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
//                        {
//                    if (!lst.isVisible()){
//                        Component c = findNextFocus();
//                        c.requestFocus();
//                    }else{
//                        lst.requestFocus();
//                    }
//                    }
//                    break;
                }
                case KeyEvent.VK_DOWN: {
//                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
//                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
//                        {                        
//                            if (!lst.isVisible()){
//			    Component c = findNextFocus();
//			    c.requestFocus();
//                            }else
//                                lst.requestFocus();
//                            
//                            break;
//                    }
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
                    dispose();
                    break;
                    
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
    
    private void udfEditCustomer(){
//        FrmEmklEdit fEdit=new FrmEmklEdit(JOptionPane.getFrameForComponent(this), true);
//        fEdit.setConn(conn);
//        fEdit.setTitle("Edit EMKL");
//        fEdit.setIsNew(false);
//        fEdit.setKodeCust(tblPenerimaan.getValueAt(tblPenerimaan.getSelectedRow(), 0).toString());
//        fEdit.setModel(modelPenerimaan);
//        fEdit.setRowPos(tblPenerimaan.getSelectedRow());
//        fEdit.setSrcTable(tblPenerimaan);
//        fEdit.setVisible(true);
    }
    
    private void TableLook(){
            int lbr=tblPenerimaan.getWidth();
            
            
        tblPenerimaan.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblPenerimaan.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblPenerimaan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblPenerimaan.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblPenerimaan.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblPenerimaan.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        tblPenerimaan.setRowHeight(18);
        for (int i=0;i<tblPenerimaan.getColumnCount();i++){
            tblPenerimaan.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
            
            
        if (modelPenerimaan.getRowCount() > 0) {
            tblPenerimaan.changeSelection(0, 0,false,false);                
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
            
            if(value instanceof Float){
                setHorizontalAlignment(tx.RIGHT);
                value=formatter.format(value);
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
    
    private void udfDelete(){
        if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus EMKL '"+tblPenerimaan.getValueAt(tblPenerimaan.getSelectedRow(), 1).toString()+"'", "Ustasoft Message", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try {
                Statement st=conn.createStatement();
                st.executeUpdate("Delete from emkl where kode_emkl='"+tblPenerimaan.getValueAt(tblPenerimaan.getSelectedRow(), 0).toString()+"'");
                
                modelPenerimaan.removeRow(tblPenerimaan.getSelectedRow());
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPenerimaan = new javax.swing.JTable();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        lblKapal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jcTglAwal = new org.jdesktop.swingx.JXDatePicker();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Toko per Kapal");
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

        jLabel2.setBackground(new java.awt.Color(255, 255, 153));
        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 18));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Daftar Toko Tujuan");
        jLabel2.setOpaque(true);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tblPenerimaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama Toko", "Jumlah", "Terbayar", "Sisa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPenerimaan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblPenerimaan.getTableHeader().setReorderingAllowed(false);
        tblPenerimaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenerimaanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPenerimaan);

        jXTaskPane1.setTitle("Filter");

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tanggal Berangkat");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Kapal");

        txtKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKapal.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtKapal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKapalFocusLost(evt);
            }
        });
        txtKapal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKapalKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKapalKeyReleased(evt);
            }
        });

        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);

        jButton1.setText("Tampilkan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKapal, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(txtKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jcTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jXTaskPane1.getContentPane().add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1086, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1088, Short.MAX_VALUE)
                .addGap(9, 9, 9))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jXTaskPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1089, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jXTaskPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isListTagihanOn=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void tblPenerimaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenerimaanMouseClicked
        if(evt.getClickCount()==2){
            udfEditCustomer();
        }
}//GEN-LAST:event_tblPenerimaanMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        modelPenerimaan=(DefaultTableModel)tblPenerimaan.getModel();
        modelPenerimaan.setNumRows(0)    ;
        tblPenerimaan.setModel(modelPenerimaan);
        
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        jScrollPane1.addKeyListener(new MyKeyListener());
        tblPenerimaan.addKeyListener(new MyKeyListener());
         
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
        TableLook();
        requestFocusInWindow(true);
        tblPenerimaan.requestFocusInWindow();
        
        try{
            Statement sDate=conn.createStatement();
            ResultSet rDate=sDate.executeQuery("select current_date,to_char(current_date,'dd-MM-yyyy') as tanggal");
            if(rDate.next()){
                jcTglAwal.setDate(rDate.getDate(1));
                
            }
            rDate.close();
            sDate.close();
            
            jcTglAwal.setFormats("dd-MM-yyyy");
            
            String sQry="select * from fn_list_penerimaan('"+dformat.format(jcTglAwal.getDate())+"' " +
                    "toko varchar, merk text, jumlah double precision)";
            System.out.println(sQry);
            onOpen(sQry);
       
        }catch(SQLException sqE){System.out.println(sqE.getMessage());}

        
        
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
//        if (!lst.isVisible())
//            udfLoadMerk();
    }//GEN-LAST:event_txtKapalFocusLost

    private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKapalKeyPressed

    private void txtKapalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyReleased
        try {
            String sCari = txtKapal.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKapal.setText(obj[0].toString());
                            lblKapal.setText(obj[1].toString());
                            lst.setVisible(false);
                        }
                    }
                    break;
                }
                case java.awt.event.KeyEvent.VK_DELETE: {
                    lst.setFocusable(true);
                    lst.requestFocus();
                    
                    break;
                }
                case java.awt.event.KeyEvent.VK_ESCAPE: {
                    lst.setVisible(false);
                    txtKapal.setText("");
                    lblKapal.setText("");
                    break;
                }
                case java.awt.event.KeyEvent.VK_DOWN: {
                    if (lst.isVisible()){
                        
                        lst.setFocusableWindowState(true);
                        lst.setVisible(true);
                        lst.requestFocus();
                    }
                    break;
                }
                default : {
                    if(!evt.getKeyText(evt.getKeyCode()).equalsIgnoreCase("Up") || !evt.getKeyText(evt.getKeyCode()).equalsIgnoreCase("F2")){
                        String sQry="select kode_kapal, coalesce(nama_kapal,'') as nama_kapal " +
                                "from kapal where upper(kode_kapal||coalesce(nama_kapal,'')) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+ this.txtKapal.getX()+15,
                                this.getY()+jPanel1.getY()+this.txtKapal.getY()+4 + txtKapal.getHeight()+75,
                                txtKapal.getWidth()+lblKapal.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, 250);
                        //                        lst.setColWidth(3, 150);
                        //                        lst.setColWidth(4, 75);
                        //                        lst.setColWidth(5, 75);
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKapal.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKapal.setText("");
                            lblKapal.setText("");
                            txtKapal.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtKapalKeyReleased

    void setCon(Connection conn) {
        this.conn=conn;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXDatePicker jcTglAwal;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JTable tblPenerimaan;
    private javax.swing.JTextField txtKapal;
    // End of variables declaration//GEN-END:variables

    private DefaultTableModel modelPenerimaan;
    DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat formatter = new DecimalFormat("#,###,###");
}
