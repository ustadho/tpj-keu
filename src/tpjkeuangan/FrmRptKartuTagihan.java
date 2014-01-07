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
public class FrmRptKartuTagihan extends javax.swing.JInternalFrame {
    private Connection conn;
    DefaultTableModel lstModel;
    private String sToko, sHeader;
    private String sMerk;
    private ListRsbm lst;
    DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
    
    /** Creates new form FrmRptPerMerk */
    public FrmRptKartuTagihan() {
        initComponents();
        jList1.setSelectedIndex(0);
        
    }
    
    public void setConn(Connection con)
    {
        conn=con;
    }

    
    private void udfClear() {
        txtToko.setText(""); lblToko.setText("");
        
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
            
        try{
            Statement sDate=conn.createStatement();
            ResultSet rDate=sDate.executeQuery("select current_date,to_char(current_date,'dd-MM-yyyy') as tanggal");
            if(rDate.next()){
                jcTglAwal.setDate(rDate.getDate(1));
                jcTglAkhir.setDate(rDate.getDate(1));                
                ComboBoxModel cModelAwal= new DefaultComboBoxModel();
                cModelAwal.setSelectedItem(rDate.getString(2));
                ComboBoxModel cModelAkhir= new DefaultComboBoxModel();
                cModelAkhir.setSelectedItem(rDate.getString(2));
                jcTglAwal.setModel(cModelAwal);
                jcTglAkhir.setModel(cModelAkhir);
            }
            rDate.close();
            sDate.close();
            jcTglAwal.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
            jcTglAkhir.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        }catch(SQLException sqE){System.out.println(sqE.getMessage());}

//            lst = new ListRsbm();
//            lst.setVisible(false);
//            lst.con = conn;
         
    }
    
    
    private void udfPrint(){
        if(txtToko.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi nama toko terlebih dulu!", "Trans Papua", JOptionPane.OK_OPTION);
            txtToko.requestFocus();
            return;
        }
        
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            String tglAwal = dformat.format(jcTglAwal.getDate());
            String tglAkhir = dformat.format(jcTglAkhir.getDate());

            switch(jList1.getSelectedIndex()){
                case 0:{
                    reportParam.put("tgl_berangkat1", tglAwal);
                    reportParam.put("tgl_berangkat2", tglAkhir);
                    reportParam.put("kode_cust", txtToko.getText());
                    reportParam.put("customer", lblToko.getText());
                    reportParam.put("kota", lblKota.getText());

                    reportParam.put("flag_nota", (radioToko.isSelected()? "T": "P"));
                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_merk_L.jasper"));
                    jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/kartu_tagihan_per_tgl_berangkat_v2.jasper"));

                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_kontainer.jasper"));
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                    print.setOrientation(jasperReport.getOrientation());
                    JasperViewer.viewReport(print, false);
                    break;
                }
                case 1:{
                    
                    reportParam.put("tgl_nota1", tglAwal);
                    reportParam.put("tgl_nota2", tglAkhir);
                    reportParam.put("kode_cust", txtToko.getText());
                    reportParam.put("customer", lblToko.getText());
                    reportParam.put("kota", lblKota.getText());

                    reportParam.put("flag_nota", (radioToko.isSelected()? "T": "P"));
                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_merk_L.jasper"));
                    jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/kartu_tagihan_per_tgl_nota.jasper"));

                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_kontainer.jasper"));
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                    print.setOrientation(jasperReport.getOrientation());
                    JasperViewer.viewReport(print, false);
                    break;
                }
                case 2:{
                    reportParam.put("tanggal1", tglAwal);
                    reportParam.put("tanggal2", tglAkhir);
                    reportParam.put("customer", txtToko.getText());
                    
                    reportParam.put("to_tujuan", radioToko.isSelected());
                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_merk_L.jasper"));
                    jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/rekap_penerimaan_per_customer.jasper"));

                    //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("Reports/packing_list_per_kontainer.jasper"));
                    JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                    print.setOrientation(jasperReport.getOrientation());
                    JasperViewer.viewReport(print, false);
                    break;
                }
                default:{
                    
                }
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        radioToko = new javax.swing.JRadioButton();
        radioPengirim = new javax.swing.JRadioButton();
        lblToko = new javax.swing.JLabel();
        txtToko = new javax.swing.JTextField();
        lblKota = new javax.swing.JLabel();
        jcTglAwal = new org.freixas.jcalendar.JCalendarCombo();
        jcTglAkhir = new org.freixas.jcalendar.JCalendarCombo();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnPreview = new javax.swing.JButton();
        btnTambahItem1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setTitle("Kartu Tagihan/ Laporan per Customer");
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        buttonGroup1.add(radioToko);
        radioToko.setForeground(new java.awt.Color(255, 255, 255));
        radioToko.setSelected(true);
        radioToko.setText("Toko Tujuan");
        radioToko.setOpaque(false);
        radioToko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTokoActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioPengirim);
        radioPengirim.setForeground(new java.awt.Color(255, 255, 255));
        radioPengirim.setText("Toko Pengirim");
        radioPengirim.setOpaque(false);
        radioPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioPengirimActionPerformed(evt);
            }
        });

        lblToko.setBackground(new java.awt.Color(255, 255, 255));
        lblToko.setFont(new java.awt.Font("Dialog", 0, 12));
        lblToko.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblToko.setOpaque(true);

        txtToko.setFont(new java.awt.Font("Dialog", 0, 12));
        txtToko.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtToko.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtToko.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokoFocusLost(evt);
            }
        });
        txtToko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTokoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTokoKeyReleased(evt);
            }
        });

        lblKota.setBackground(new java.awt.Color(255, 255, 255));
        lblKota.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKota.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKota.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(radioPengirim))
                    .addComponent(radioToko, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblKota, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtToko, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(lblToko, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioPengirim)
                    .addComponent(radioToko))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtToko, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblToko, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblKota, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 62, 676, 90));

        jcTglAwal.setEditable(true);
        getContentPane().add(jcTglAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(214, 246, 120, -1));

        jcTglAkhir.setEditable(true);
        getContentPane().add(jcTglAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 246, 110, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Tanggal");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 247, 140, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/report_header.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 5, 680, -1));

        btnPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/preview.png"))); // NOI18N
        btnPreview.setMnemonic('P');
        btnPreview.setText("Preview");
        btnPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        getContentPane().add(btnPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 166, 90, 100));

        btnTambahItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Exit_1.png"))); // NOI18N
        btnTambahItem1.setText("Tutup");
        btnTambahItem1.setToolTipText("Tutup aplikasi pembayaran");
        btnTambahItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTambahItem1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTambahItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahItem1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnTambahItem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 166, 90, 100));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Kartu tagihan per Tgl. Nota", "Kartu tagihan per Tgl. Berangkat", "Rekap Penerimaan per Customer", "Detail Penerimaan per Customer" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 168, 490, 70));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Pilih Jenis Laporan");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 154, 280, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("s/d");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 249, 40, -1));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-697)/2, (screenSize.height-315)/2, 697, 315);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        onOpen();
    }//GEN-LAST:event_formInternalFrameOpened

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        lst.setVisible(false);
        Utama.isRptKartuTagihan=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void radioTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTokoActionPerformed
        udfClear();
    }//GEN-LAST:event_radioTokoActionPerformed

    private void radioPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPengirimActionPerformed
        udfClear();
    }//GEN-LAST:event_radioPengirimActionPerformed

    private void txtTokoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoFocusLost
        
    }//GEN-LAST:event_txtTokoFocusLost

    private void txtTokoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTokoKeyPressed

    private String getQuery(){
        String sQry="";
        if(radioToko.isSelected()){
            sQry="select c.kode_cust as kode,  coalesce(c.nama,'') as toko, coalesce(nama_kota,'') as nama_kota " +
                 "from customer c inner join merk on c.kode_cust=merk.kode_cust " +
                 "left join kota on kota.kode_kota=c.kota " +
                 "where (c.kode_cust||coalesce(c.nama,'')||coalesce(merk.merk,'')||coalesce(nama_kota,'')) iLike '%"+txtToko.getText()+"%' " +
                 "group by c.kode_cust, coalesce(c.nama,''), coalesce(nama_kota,'') " +
                 "order by coalesce(c.nama,'')";
            return sQry;
        }
        
        if(radioPengirim.isSelected()){
            sQry=   "select kode_toko, coalesce(nama,'') as nama, case when nama_kota is null then toko.kota else nama_kota end as nama_kota " +
                    "from toko " +
                    "left join kota on kota.kode_kota=kota where kode_toko||coalesce(nama,'') iLike '%"+txtToko.getText()+"%' order by coalesce(nama,'') ";
            
            return sQry;
        }
        
        return sQry;
    }
    
    private void txtTokoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoKeyReleased
        try {
            String sQry=getQuery();
            switch(evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtToko.setText(obj[0].toString());
                            lblToko.setText(obj[1].toString());
                            lblKota.setText(obj[2].toString());
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
                    txtToko.setText("");
                    lblToko.setText("");
                    lblKota.setText("");
                    txtToko.requestFocus();
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
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.txtToko.getLocationOnScreen().x,
                                this.txtToko.getLocationOnScreen().y + txtToko.getHeight(),
                                txtToko.getWidth()+lblToko.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtToko);
                        lst.setLblDes(new javax.swing.JLabel[]{lblToko, lblKota});
                        lst.setColWidth(0, txtToko.getWidth());
                        lst.setColWidth(1, lblToko.getWidth());
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtToko.requestFocus();
                        } else{
                            lst.setVisible(false);
                            ///txtToko.setText("");
                            lblToko.setText("");
                            lblKota.setText("");
                            txtToko.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtTokoKeyReleased

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        udfPrint();
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnTambahItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnTambahItem1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        switch(jList1.getSelectedIndex()){
            case 0:{
                jLabel6.setText("Tgl. Nota");
                break;
            }
            case 1:{
                jLabel6.setText("Tgl. Berangkat");
                break;
            }
            case 2:{
                jLabel6.setText("Tgl. Pembayaran");
                break;
            }
            default:{
                jLabel6.setText("Tanggal");
                break;
            }
        }
            
           
    }//GEN-LAST:event_jList1ValueChanged
    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnTambahItem1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.freixas.jcalendar.JCalendarCombo jcTglAkhir;
    private org.freixas.jcalendar.JCalendarCombo jcTglAwal;
    private javax.swing.JLabel lblKota;
    private javax.swing.JLabel lblToko;
    private javax.swing.JRadioButton radioPengirim;
    private javax.swing.JRadioButton radioToko;
    private javax.swing.JTextField txtToko;
    // End of variables declaration//GEN-END:variables
    
}
