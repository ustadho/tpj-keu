/*
 * FrmRptPerMerk.java
 *
 * Created on February 8, 2008, 9:32 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
public class FrmRptPerMerk_v2 extends javax.swing.JInternalFrame {
    private Connection conn;
    DefaultTableModel lstModel;
    private String sToko, sHeader;
    private String sMerk, sTgl;
    private ListRsbm lst;
    DateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat fmtDMY = new SimpleDateFormat("dd-MM-yyyy");
                
                
    /** Creates new form FrmRptPerMerk */
    public FrmRptPerMerk_v2() {
        initComponents();
    }
    
    private void udfLoadTglBerangkat(){
        cmbTglBerangkat.removeAllItems();
        
        try {
            Statement st1 = conn.createStatement();
            ResultSet rs1=st1.executeQuery("select distinct coalesce(to_Char(tgl_berangkat,'dd-MM-yyyy'),'') as tanggal from kontainer " +
                    "where active=true and kode_kapal='"+txtKapal.getText()+"' ");
            
            cmbTglBerangkat.removeAllItems();
            
            while(rs1.next()){
                cmbTglBerangkat.addItem(rs1.getString(1));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPerMerk_v2.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
    
    public void setConn(Connection con)
    {
        conn=con;
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            switch(keyKode){
                case KeyEvent.VK_ENTER : {
		    if (!lst.isVisible()){
			Component c = findNextFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_UP : {
		    if (!lst.isVisible()){
			Component c = findPrevFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_DOWN : {
		    if (!lst.isVisible()){
                Component c = findNextFocus();
                if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                
                 
                case KeyEvent.VK_ESCAPE: {
                    if (!lst.isVisible())
                        dispose();
                    else
                        lst.setVisible(false);
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
	    return prevFocus;
	}
	return null;
    }
    
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        JCheckBox checkBox = new JCheckBox();
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
//            if((column==0)||(column==1)||(column==2)||(column==3)||(column==6)||(column==7)||(column==9)){
//                JTextField jt= new JTextField();
//                setHorizontalAlignment(jt.LEFT);
//            }
//            
            
            
            setValue(value);
            
            Color g1 = new Color(230,243,255);//[251,251,235]
            Color g2 = new Color(219,238,255);//[247,247,218]
            
            Color w1 = new Color(255,255,255);
            Color w2 = new Color(250,250,250);
            
            Color h1 = new Color(255,240,240);
            Color h2 = new Color(250,230,230);
            
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
            
             if (value instanceof Boolean) { // Boolean
                  checkBox.setSelected(((Boolean) value).booleanValue());
                  checkBox.setHorizontalAlignment(JLabel.CENTER);
                  if (row%2==0){
                     checkBox.setBackground(w);
                  }else{
                     checkBox.setBackground(g);
                  }
 
                  if (isSelected){
                        checkBox.setBackground(new Color(248,255,167));//51,102,255));
                    }
                  
                  return checkBox;
            }
            
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            
            if(isSelected){
                setBackground(new Color(248,255,167));//[174,212,254]
            }
            
            setFont(new Font("Tahoma", 0,10));
             if (value instanceof Boolean) { // Boolean
                  checkBox.setSelected(((Boolean) value).booleanValue());
                  checkBox.setHorizontalAlignment(JLabel.CENTER);

                  return checkBox;
            }
            return this;
        }
    }
    
    private void onOpen(){
        lstModel = (DefaultTableModel) tblMerk.getModel();
        tblMerk.setModel(lstModel);
        lstModel.setNumRows(0);
            
//        try{
//            Statement sDate=conn.createStatement();
//            ResultSet rDate=sDate.executeQuery("select current_date,to_char(current_date,'dd-MM-yyyy') as tanggal");
//            if(rDate.next()){
//                jcTglAwal.setDate(rDate.getDate(1));
//                jcTglAkhir.setDate(rDate.getDate(1));                
//            }
//            rDate.close();
//            sDate.close();
//            jcTglAwal.setFormats("dd-MM-yyyy");
//            jcTglAkhir.setFormats("dd-MM-yyyy");
//        }catch(SQLException sqE){System.out.println(sqE.getMessage());}

         
    }
    
    private void udfLoadMerk(){
        try {
            lstModel.setNumRows(0);
            
            
            Statement st = conn.createStatement();
            String sQry="select distinct p.merk, coalesce(cust.nama,'') as nama_cust " +
                    "from penerimaan p " +
                    "left join penerimaan_detail d using(no_penerimaan) " +
                    "left join expedisi_barang ex on ex.serial=d.id " +
                    "left join kontainer kn on kn.serial_kon=ex.serial_kon " +
                    "left join merk on merk.merk=p.merk " +
                    "left join customer cust on cust.kode_cust=merk.kode_cust " +
                    "where coalesce(to_char(tgl_berangkat, 'dd-MM-yyyy'),'') ='"+ cmbTglBerangkat.getSelectedItem().toString()+"' " +
                    "and coalesce(kode_kapal,'') = '"+txtKapal.getText()+"' and coalesce(kn.kode_kota,'') Like '%"+Utama.sKodeKota+"%' ";
            
            System.out.println(sQry);
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            ResultSet rs1 = st.executeQuery(sQry);
            
            while(rs1.next())
            {
                //System.out.println(rs1.getString(1));
                
                lstModel.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    false
                });
                
            }
            rs1.close();
            st.close();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPerMerk_v2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private boolean udfCekBeforePrint(){
        boolean b=false;
        
        sToko=""; 
        sHeader="";
        sMerk="";
        String sKoma="";
        boolean bSama=false;
        
        for(int i=0; i<lstModel.getRowCount(); i++){
            if((Boolean)tblMerk.getValueAt(i, 2)==true){
                sMerk=tblMerk.getValueAt(i, 0).toString();
                b=true;
                //break;
                
                if(sToko.equalsIgnoreCase(tblMerk.getValueAt(i, 1).toString())){
                    sKoma=sToko.equalsIgnoreCase(tblMerk.getValueAt(i, 1).toString())? ",":"";
                    sHeader=sHeader.replace(" ("+sToko+") ","") + sKoma+ sMerk ;
                    bSama=true;
                }else
                {
                    String s_and="";
                    s_and=sToko.length()>0? " & ":"";
                    
                    sToko=tblMerk.getValueAt(i, 1).toString();
                    sHeader=sHeader + s_and + sMerk+"("+sToko+")";
                    bSama=false;
                }
                
                
            }
            
        }
        sHeader=sHeader+(bSama==true? "("+sToko+")":"");
//            JOptionPane.showMessageDialog(this, sHeader);
        if(b==false){
            JOptionPane.showMessageDialog(this, "Silakan pilih merk terlebih dulu!", "Trans Papua", JOptionPane.OK_OPTION);
            tblMerk.setRowSelectionInterval(0, 0);
            return false;
        }
        
        return b;
    }
    
    private String getMerk(){
        String sRtn="";
        int s=0;
        
        for(int i=0; i<lstModel.getRowCount(); i++){
            if((Boolean)tblMerk.getValueAt(i, 2)==true){
                if(s==0){
                    sRtn=""+tblMerk.getValueAt(i, 0).toString()+"";
                    s=1; 
                }
                else
                    sRtn=sRtn+"','"+tblMerk.getValueAt(i, 0).toString()+"";
            }
        }
        
        
        System.out.println(sRtn);
        return sRtn;
    }
    
    private String udfGetHeader(){
        String sRtn = "";
        int j = 0;
        try {
            
            String s=""; int iDel;
            String sDel="Delete from tmp_merk_toko; ";
            String sIns="";
            Statement stm=conn.createStatement();
            iDel=stm.executeUpdate(sDel);
            for (int ji=0; ji< tblMerk.getRowCount(); ji++){
                if ((Boolean) tblMerk.getValueAt(ji, 2)==true){
                    sToko = tblMerk.getValueAt(ji, 1).toString();
                    sMerk = tblMerk.getValueAt(ji, 0).toString();

                    iDel=stm.executeUpdate("insert into tmp_merk_toko values('"+sMerk+"', '"+sToko+"' )");
                }
            } 
        
            
            Statement st2 = conn.createStatement();
            ResultSet rs2 = st2.executeQuery("select gabung(merk), toko from tmp_merk_toko group by toko");
            
            while(rs2.next()){
                if(j==0){
                    sRtn=sRtn+rs2.getString(1)+ " ("+ rs2.getString(2) +")";
                    j++;
                }else{
                    sRtn= sRtn+" & "+ rs2.getString(1)+ " ("+ rs2.getString(2) +")";
                }
            }
            
            stm.execute(sDel);
            System.out.println(sRtn);
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPerMerk_v2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sRtn;
        
    }
    private void udfPrint(){
        if (!udfCekBeforePrint()) return;
        
        setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        FileInputStream file = null;
            try {
                HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                sTgl=cmbTglBerangkat.getSelectedItem().toString().trim().equalsIgnoreCase("")? "": fmtYMD.format(fmtDMY.parse(cmbTglBerangkat.getSelectedItem().toString()));
                
                reportParam.put("merk", getMerk());
                reportParam.put("sHeader", udfGetHeader());
                reportParam.put("kapal", txtKapal.getText());
                reportParam.put("tgl_berangkat", sTgl);
                
                //System.out.println(getClass().getResource("Reports/packing_list_per_merk_per_kapal_per_tgl_berangkat_v2.jasper"));
                //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_merk_per_kapal_per_tgl_berangkat_v2.jasper"));
                jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/per_merk_v3.jasper"));
                
                JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.getOrientation());
                JasperViewer.viewReport(print, false);
                setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                
            } catch (ParseException ex) {
                setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                Logger.getLogger(FrmRptPerMerk_v2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException je) {
                setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                System.out.println(je.getMessage());
            }
            
    }
    
    private String udfGetMerk(){
        String s=""; int iDel;
        String sDel="Delete frm tmp_merk_toko; ";
        String sIns="";
        try{
            Statement stm=conn.createStatement();
            iDel=stm.executeUpdate(sDel);
            for (int j=0; j< tblMerk.getRowCount(); j++){
                if ((Boolean) tblMerk.getValueAt(j, 2)==true){
                    sToko = tblMerk.getValueAt(j, 1).toString();
                    sMerk = tblMerk.getValueAt(j, 0).toString();

                    iDel=stm.executeUpdate("insert into tmp_merk_toko values('"+sMerk+"', '"+sToko+"' )");
                }
            } 
        }catch(SQLException se){
            System.err.println(se.getMessage());
        }
        
        return s;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMerk = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblKapal = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbTglBerangkat = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Laporan per Merk");
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

        jLabel1.setBackground(new java.awt.Color(51, 51, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Daftar merk");
        jLabel1.setOpaque(true);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Preview");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tblMerk.setAutoCreateRowSorter(true);
        tblMerk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Merk", "Nama Toko", "Pilih"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMerk.getTableHeader().setReorderingAllowed(false);
        tblMerk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMerkMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMerk);
        tblMerk.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblMerk.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblMerk.getColumnModel().getColumn(2).setMaxWidth(50);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);
        jPanel1.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 6, 390, 24));

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
        jPanel1.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 6, 60, 24));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Kapal");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 80, 20));

        cmbTglBerangkat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTglBerangkatItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 33, 160, 24));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Tgl. Berangkat");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 36, -1, 20));

        jCheckBox1.setText("Check All");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1))
                .addGap(30, 30, 30))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-597)/2, (screenSize.height-514)/2, 597, 514);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        
        tblMerk.setRowHeight(16);
        for (int i=0;i<tblMerk.getColumnCount();i++){
            tblMerk.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
        
            
        onOpen();
        MyKeyListener kListener=new MyKeyListener();
        
        for(int i=0;i<jPanel1.getComponentCount();i++){
            Component c = jPanel1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        jScrollPane1.addKeyListener(kListener);
        //tblMerk.addKeyListener(kListener);
        jButton1.addKeyListener(kListener);
        jButton2.addKeyListener(kListener);
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        lst.setVisible(false);
        Utama.isPckPerMerk=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        udfPrint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
        if (!lst.isVisible())
            //udfLoadMerk();
            udfLoadTglBerangkat();
    }//GEN-LAST:event_txtKapalFocusLost

    private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER && !txtKapal.getText().trim().equalsIgnoreCase("")){
            udfLoadTglBerangkat();
        }
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
                            udfLoadTglBerangkat();
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
//                        String sQry="select kode_kapal, coalesce(nama_kapal,'') as nama_kapal " +
//                                "from kapal where upper(kode_kapal||coalesce(nama_kapal,'')) " +
//                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        String sQry="select distinct kon.kode_kapal, nama_kapal " +
                                "from kontainer kon " +
                                "inner join kapal on kapal.kode_kapal=kon.kode_kapal " +
                                "where (kon.kode_kapal||nama_kapal) iLike '%"+txtKapal.getText()+"%' ";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+ this.txtKapal.getX()+15,
                                Utama.iTop+this.getY()+jPanel1.getY()+this.txtKapal.getY()+4 + txtKapal.getHeight()+75,
                                txtKapal.getWidth()+lblKapal.getWidth()+15,
                                120);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblKapal.getWidth());
                        
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

    private void cmbTglBerangkatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTglBerangkatItemStateChanged
        //JOptionPane.showMessageDialog(this, cmbTglBerangkat.getSelectedItem().toString());
        if(cmbTglBerangkat.getSelectedItem()!=null){
            udfLoadMerk();
            return;
        }
    }//GEN-LAST:event_cmbTglBerangkatItemStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
           boolean b=jCheckBox1.isSelected();
            for(int i=0 ;i<tblMerk.getRowCount(); i++){
                tblMerk.setValueAt(b, i, 2);
            }
        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void tblMerkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMerkMouseClicked
//        if(tblMerk.getSelectedColumn()==2){
//            try {
//                int i = tblMerk.getSelectedRow();
//                String sToko="" ;
//                String sMerk="";
//
//                Statement stm = conn.createStatement();
//                int iDel=stm.executeUpdate("delete from tmp_merk_toko");
//                
//                for (int s=0; s< tblMerk.getRowCount(); s++){
//                    if ((Boolean) tblMerk.getValueAt(s, 2)==true){
//                        sToko = tblMerk.getValueAt(s, 1).toString();
//                        sMerk = tblMerk.getValueAt(s, 0).toString();
//                        
//                        iDel=stm.executeUpdate("insert into tmp_merk_toko values('"+sMerk+"', '"+sToko+"' )");
//                    }
//                }
//                //boolean bSt = (Boolean) tblMerk.getValueAt(i, 2);
//                //JOptionPane.showMessageDialog(this, bSt);
//            } catch (SQLException ex) {
//                Logger.getLogger(FrmRptPerMerk_v2.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_tblMerkMouseClicked
    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbTglBerangkat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JTable tblMerk;
    private javax.swing.JTextField txtKapal;
    // End of variables declaration//GEN-END:variables
    
}
