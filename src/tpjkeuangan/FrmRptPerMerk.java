/*
 * FrmRptPerMerk.java
 *
 * Created on February 8, 2008, 9:32 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
public class FrmRptPerMerk extends javax.swing.JInternalFrame {
    private Connection conn;
    DefaultTableModel lstModel;
    private String sToko, sHeader;
    private String sMerk;
    private ListRsbm lst;
    DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
    
    /** Creates new form FrmRptPerMerk */
    public FrmRptPerMerk() {
        initComponents();
    }
    
    public void setConn(Connection con)
    {
        conn=con;
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
            
        try{
            Statement sDate=conn.createStatement();
            ResultSet rDate=sDate.executeQuery("select current_date,to_char(current_date,'dd-MM-yyyy') as tanggal");
            if(rDate.next()){
                jcTglAwal.setDate(rDate.getDate(1));
                jcTglAkhir.setDate(rDate.getDate(1));                
            }
            rDate.close();
            sDate.close();
            jcTglAwal.setFormats("dd-MM-yyyy");
            jcTglAkhir.setFormats("dd-MM-yyyy");
        }catch(SQLException sqE){System.out.println(sqE.getMessage());}

         
    }
    
    
    private void udfLoadMerk(){
        try {
//            lstModel = (DefaultTableModel) tblMerk.getModel();
//            tblMerk.setModel(lstModel);
            lstModel.setNumRows(0);
            
            String tglAwal = dformat.format(jcTglAwal.getDate());
            String tglAkhir = dformat.format(jcTglAkhir.getDate());
            
//            String sQry="select distinct p.merk, coalesce(cust.nama,'') as nama_cust from penerimaan p " +
//                    "left join penerimaan_detail d using(no_penerimaan) " +
//                    "left join expedisi_barang ex on ex.serial=d.id " +
//                    "left join kontainer kn on kn.no_spnu=ex.no_spnu " +
//                    "left join merk on merk.merk=p.merk " +
//                    "left join customer cust on cust.kode_cust=merk.kode_cust " +
//                    "where to_char(tgl_berangkat, 'yyyy-MM-dd')>='"+tglAwal+"' " +
//                    "and to_char(tgl_berangkat, 'yyyy-MM-dd')<='"+tglAkhir+"' " +
//                    "and coalesce(kn.kode_kota,'') like '"+ MainForm.sKodeKota +"%' " +
//                    "and coalesce(kode_kapal,'') iLike '%"+txtKapal.getText()+"%'";
//            
            
            Statement st = conn.createStatement();
            ResultSet rs1 = st.executeQuery("select distinct p.merk, coalesce(cust.nama,'') as nama_cust " +
                    "from penerimaan p " +
                    "left join penerimaan_detail d using(no_penerimaan) " +
                    "left join expedisi_barang ex on ex.serial=d.id " +
                    "left join kontainer kn on kn.no_spnu=ex.no_spnu " +
                    "left join merk on merk.merk=p.merk " +
                    "left join customer cust on cust.kode_cust=merk.kode_cust " +
                    "where to_char(tgl_berangkat, 'yyyy-MM-dd')>='"+tglAwal+"' and to_char(tgl_berangkat, 'yyyy-MM-dd')<='"+tglAkhir+"' " +
                    "and coalesce(kn.kode_kota,'') like '%"+Utama.sNamaKota+"%' and coalesce(kode_kapal,'') iLike '%"+txtKapal.getText()+"%'");
            //System.out.println(sQry);
            
            while(rs1.next())
            {
                System.out.println(rs1.getString(1));
                
                lstModel.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    false
                });
                
            }
            rs1.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPerMerk.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void udfPrint(){
        
        if (!udfCekBeforePrint()) return;
        
        FileInputStream file = null;
            try {
                HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                int pilRpt = 0;
                
                
                String tglAwal = dformat.format(jcTglAwal.getDate());
                String tglAkhir = dformat.format(jcTglAkhir.getDate());
                
                reportParam.put("tanggal1", tglAwal);
                reportParam.put("tanggal2", tglAkhir);
                reportParam.put("merk", getMerk());
                reportParam.put("sHeader", sHeader);
                reportParam.put("kapal", txtKapal.getText());
                
                System.out.println("Awal: "+tglAwal);
                System.out.println("Akhir:"+tglAkhir);
                
                jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports_2/packing_list_per_merk_per_kapal_v1.jasper"));
                
                //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports_2/packing_list_per_kontainer.jasper"));
                JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                JasperViewer.viewReport(print, false);
                           
            } catch (JRException je) {
                System.out.println(je.getMessage());
            }
            
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
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblKapal = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jcTglAwal = new org.jdesktop.swingx.JXDatePicker();
        jcTglAkhir = new org.jdesktop.swingx.JXDatePicker();

        setBackground(new java.awt.Color(204, 204, 204));
        setClosable(true);
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
        jScrollPane1.setViewportView(tblMerk);
        tblMerk.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblMerk.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblMerk.getColumnModel().getColumn(2).setMaxWidth(50);

        jLabel6.setText("Tgl Berangkat");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("s/d");

        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

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

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Kapal");

        jcTglAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcTglAwalActionPerformed(evt);
            }
        });
        jcTglAwal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jcTglAwalFocusLost(evt);
            }
        });

        jcTglAkhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcTglAkhirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jcTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcTglAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcTglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcTglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(txtKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblKapal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(1, 1, 1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-532)/2, (screenSize.height-507)/2, 532, 507);
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
    }//GEN-LAST:event_formInternalFrameOpened

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isPckPerMerk=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        udfPrint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
        if (!lst.isVisible())
            udfLoadMerk();
        
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

    private void jcTglAwalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcTglAwalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jcTglAwalFocusLost

    private void jcTglAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcTglAwalActionPerformed
        udfLoadMerk();
    }//GEN-LAST:event_jcTglAwalActionPerformed

    private void jcTglAkhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcTglAkhirActionPerformed
        udfLoadMerk();
    }//GEN-LAST:event_jcTglAkhirActionPerformed
    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jcTglAkhir;
    private org.jdesktop.swingx.JXDatePicker jcTglAwal;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JTable tblMerk;
    private javax.swing.JTextField txtKapal;
    // End of variables declaration//GEN-END:variables
    
}
