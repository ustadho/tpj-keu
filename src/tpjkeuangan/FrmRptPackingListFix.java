/*
 * frmRptKasir.java
 *
 * Created on November 30, 2005, 7:04 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author  root
 */
public class FrmRptPackingListFix extends javax.swing.JInternalFrame {
    
    private Connection conn;
    private String sKodeJenis="";
    private String sUser="";
    private DefaultListModel modelJenis, modelTrx;
    private ListRsbm lst;
    Color g1 = new Color(153,255,255);
    Color g2 = new Color(255,255,255); 
    
    public void setCon(Connection con){
        conn=con;
    }
    
    private FocusListener txtFoculListener=new FocusListener() {
        public void focusGained(FocusEvent e) {
           Component c=(Component) e.getSource();
           c.setBackground(g1);
           //c.setForeground(fPutih);
           //c.setCaretColor(new java.awt.Color(255, 255, 255));
        }
        public void focusLost(FocusEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(g2);
            //c.setForeground(fHitam);
        }
   };
   
    /** Creates new form frmRptKasir */
    public FrmRptPackingListFix() {
        initComponents();
        txtContainer.addFocusListener(txtFoculListener);
        txtKapal.addFocusListener(txtFoculListener);
        txtPetugas.addFocusListener(txtFoculListener);
        txtKotaTujuan.addFocusListener(txtFoculListener);
        
    }

    void setUserName(String sUserName) {
        sUser="";
    }
    
    private void onOpen(){
        try{
            Statement sDate=conn.createStatement();
            ResultSet rDate=sDate.executeQuery("select current_date,to_char(current_date,'dd-MM-yyyy') as tanggal");
            if(rDate.next()){
                jcTglAwal.setDate(rDate.getDate(1));
                jcTglAkhir.setDate(rDate.getDate(1));                
//                ComboBoxModel cModelAwal= new DefaultComboBoxModel();
//                cModelAwal.setSelectedItem(rDate.getString(2));
//                ComboBoxModel cModelAkhir= new DefaultComboBoxModel();
//                cModelAkhir.setSelectedItem(rDate.getString(2));
//                jcTglAwal.setModel(cModelAwal);
//                jcTglAkhir.setModel(cModelAkhir);
            }
            rDate.close();
            sDate.close();
            jcTglAwal.setFormats("dd-MM-yyyy");
            jcTglAkhir.setFormats("dd-MM-yyyy");
        }catch(SQLException sqE){System.out.println(sqE.getMessage());}

//            lst = new ListRsbm();
//            lst.setVisible(false);
//            lst.con = conn;
         
    }
    
    private void udfLoadJenis(){
        try {
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select kode_jenis, coalesce(jenis_transaksi,'') as jenis_transaksi " +
                    "from fisio_jenis_trx order by 1");
            
            modelJenis=new DefaultListModel();
            
            modelJenis.addElement("");
            
            while(rs.next()){
                //cmbJenis.addItem(rs.getString("jenis_transaksi"));
                modelJenis.addElement(rs.getString("kode_jenis"));
            }
            rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }
    
    private void udfLoadUser(){
//        try {
//            Statement st=conn.createStatement();
//            ResultSet rs=st.executeQuery("select distinct user_tr from fisio_transaksi_detail");
//            
//            cmbUser.addItem("All");
//            
//            while(rs.next()){
//                cmbUser.addItem(rs.getString(1));
//            }
//            
//            cmbUser.setSelectedIndex(0);
//            
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }
        
    }
    
    private void setFocusBlockTxt(JTextField txt){
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbLaporan = new javax.swing.JComboBox();
        txtContainer = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtPetugas = new javax.swing.JTextField();
        lblPegawai = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        lblKapal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtKotaTujuan = new javax.swing.JTextField();
        lblKotaTujuan = new javax.swing.JLabel();
        chkExpedisi = new javax.swing.JCheckBox();
        jcTglAwal = new org.jdesktop.swingx.JXDatePicker();
        jcTglAkhir = new org.jdesktop.swingx.JXDatePicker();
        btnPreview = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setForeground(new java.awt.Color(0, 0, 0));
        setTitle("Report");
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

        jLabel5.setBackground(new java.awt.Color(51, 51, 255));
        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 1, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Packing List");
        jLabel5.setOpaque(true);
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 12, 440, 23));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Laporan");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 13, 86, -1));

        jLabel4.setText("s/d");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 40, 30, 20));

        jLabel3.setText("No. Container");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 67, 86, 20));

        cmbLaporan.setFont(new java.awt.Font("Tahoma", 0, 12));
        cmbLaporan.setMaximumRowCount(17);
        cmbLaporan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1.  /Packing List by Container", "2.  /Packing List by Kapal", "3.  /Packing List by 1 MERK", "4.  /Packing List by Petugas Pengiriman", "5.  /Packing List by Kota Tujuan" }));
        cmbLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmbLaporanMouseClicked(evt);
            }
        });
        cmbLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLaporanActionPerformed(evt);
            }
        });
        jPanel1.add(cmbLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 12, 340, -1));

        txtContainer.setFont(new java.awt.Font("Dialog", 0, 12));
        txtContainer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtContainer.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtContainer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContainerActionPerformed(evt);
            }
        });
        txtContainer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContainerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContainerFocusLost(evt);
            }
        });
        txtContainer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContainerKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContainerKeyReleased(evt);
            }
        });
        jPanel1.add(txtContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 66, 230, 24));

        jLabel6.setText("Tanggal");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 37, 86, 20));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Kapal");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 130, 80, -1));

        txtPetugas.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtPetugas.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtPetugas.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtPetugas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPetugasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPetugasFocusLost(evt);
            }
        });
        txtPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPetugasKeyReleased(evt);
            }
        });
        jPanel1.add(txtPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 95, 70, 24));

        lblPegawai.setFont(new java.awt.Font("Dialog", 0, 12));
        lblPegawai.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblPegawai.setOpaque(true);
        jPanel1.add(lblPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 95, 276, 24));

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
        jPanel1.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 124, 70, 24));

        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 124, 276, 24));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Dikirim Oleh");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 97, 103, -1));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Kota Tujuan");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 159, 103, -1));

        txtKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKotaTujuan.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtKotaTujuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKotaTujuanActionPerformed(evt);
            }
        });
        txtKotaTujuan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKotaTujuanFocusLost(evt);
            }
        });
        txtKotaTujuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKotaTujuanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKotaTujuanKeyReleased(evt);
            }
        });
        jPanel1.add(txtKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 152, 70, 24));

        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 153, 276, 24));

        chkExpedisi.setText("Expedisi Report");
        jPanel1.add(chkExpedisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 67, 110, -1));
        jPanel1.add(jcTglAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 40, 140, -1));
        jPanel1.add(jcTglAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(257, 40, 140, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 34, 440, 184));

        btnPreview.setMnemonic('P');
        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        getContentPane().add(btnPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 221, -1, 27));

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(367, 221, 68, 27));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-463)/2, (screenSize.height-299)/2, 463, 299);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbLaporanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmbLaporanMouseClicked
        
        
        
    }//GEN-LAST:event_cmbLaporanMouseClicked

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isRptPackingList=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        onOpen();
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
            
        modelTrx=new DefaultListModel();
        jcTglAwal.setEnabled(false);
        jcTglAkhir.setEnabled(false);
    }//GEN-LAST:event_formInternalFrameOpened
        
    private void cmbLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLaporanActionPerformed
        chkExpedisi.setVisible(cmbLaporan.getSelectedIndex()==0? true: false);
        txtContainer.setText("");
        txtContainer.setEnabled(false);
        
        jcTglAwal.setEnabled(true);
        jcTglAkhir.setEnabled(true);
            
        txtPetugas.setText(""); txtPetugas.setEnabled(false);
        lblPegawai.setText("");
        
        txtKapal.setText(""); txtKapal.setEnabled(false);
        lblKapal.setText("");
        txtKotaTujuan.setText(""); txtKotaTujuan.setEnabled(false);
        lblKotaTujuan.setText("");
        
        if(cmbLaporan.getSelectedIndex()==0){  //Container
            txtContainer.setEnabled(true);
            jcTglAwal.setEnabled(false);
            jcTglAkhir.setEnabled(false);
            
        } else if(cmbLaporan.getSelectedIndex()==1){  //Kapal
            txtKapal.setEnabled(true); 
        
        } else if (cmbLaporan.getSelectedIndex()==2) {
            txtPetugas.setEnabled(true);
            jLabel12.setText("Merk");
        }else if (cmbLaporan.getSelectedIndex()==3) {
            txtPetugas.setEnabled(true);
            jLabel12.setText("Dikirim Oleh");
        }else if (cmbLaporan.getSelectedIndex()==4){
            txtKotaTujuan.setEnabled(true);
            
        }
    }//GEN-LAST:event_cmbLaporanActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
        
    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        udfPrint();
        
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void txtContainerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContainerFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtContainerFocusLost

    private void txtContainerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContainerKeyReleased
        try {
            String sCari = txtContainer.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtContainer.setText(obj[0].toString());
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
                    txtContainer.setText("");
                    
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
                      
                        String sQry="select no_spnu from kontainer where active=true and coalesce(kode_kota,'') iLike '"+Utama.sKodeKota+"%' " +
                                "and no_spnu ilike upper('%"+sCari+"%') order by 1";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+jPanel1.getX()+ this.txtContainer.getX()+4,
                                Utama.iTop+this.getY()+jPanel1.getY()+this.txtContainer.getY() + txtContainer.getHeight()+75,
                                txtContainer.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtContainer);
                        lst.setLblDes(new javax.swing.JLabel[]{});
                        lst.setColWidth(0, txtContainer.getWidth());
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtContainer.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtContainer.setText("");
                            txtContainer.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtContainerKeyReleased

    private void txtPetugasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPetugasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPetugasFocusGained

    private void txtPetugasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPetugasFocusLost
//        if(lst.isVisible())
//            lst.setVisible(false);
    }//GEN-LAST:event_txtPetugasFocusLost

    private void txtPetugasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPetugasKeyReleased
        try {
            String sCari = txtPetugas.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtPetugas.setText(obj[0].toString());
                            lblPegawai.setText(obj[1].toString());
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
                    txtPetugas.setText("");
                    lblPegawai.setText("");
                    
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
                        String sQry="";
                        if(jLabel12.getText().equalsIgnoreCase("Dikirim Oleh")){
                            sQry="select kode_pegawai as kode, coalesce(nama,'') as nama, coalesce(alamat_rumah,'') as alamat " +
                                "from pegawai where upper(kode_pegawai||coalesce(nama,'')||coalesce(alamat_rumah,'')) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        }else{
                            sQry="select merk.merk, nama from merk inner join customer using(kode_cust) " +
                                 "where upper(merk.merk||coalesce(nama,'')) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        }
                        
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ this.txtPetugas.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+this.txtPetugas.getY() + txtPetugas.getHeight()+70,
                                txtPetugas.getWidth()+lblPegawai.getWidth()+10,
                                120);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtPetugas);
                        lst.setLblDes(new javax.swing.JLabel[]{lblPegawai});
                        lst.setColWidth(0, txtPetugas.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtPetugas.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtPetugas.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtPetugasKeyReleased

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
        
}//GEN-LAST:event_txtKapalFocusLost

    private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            btnPreview.requestFocus();
        
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
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ this.txtKapal.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+this.txtKapal.getY() + txtKapal.getHeight()+75,
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

    private void txtKotaTujuanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKotaTujuanFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtKotaTujuanFocusLost

    private void txtKotaTujuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaTujuanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            btnPreview.requestFocus();
}//GEN-LAST:event_txtKotaTujuanKeyPressed

    private void txtKotaTujuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaTujuanKeyReleased
        try {
            String sCari = txtKotaTujuan.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKotaTujuan.setText(obj[0].toString());
                            lblKotaTujuan.setText(obj[1].toString());
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
                    txtKotaTujuan.setText("");
                    lblKotaTujuan.setText("");
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
                        String sQry="select kode_kota as kode, coalesce(nama_kota,'') as kota " +
                                "from kota where upper(kode_kota||coalesce(nama_kota,'')) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ this.txtKotaTujuan.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+this.txtKotaTujuan.getY() + txtKotaTujuan.getHeight()+75,
                                txtKotaTujuan.getWidth()+lblKotaTujuan.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKotaTujuan);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKotaTujuan});
                        lst.setColWidth(0, txtKotaTujuan.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKotaTujuan.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKotaTujuan.setText("");
                            lblKotaTujuan.setText("");
                            txtKotaTujuan.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKotaTujuanKeyReleased

    private void txtKotaTujuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKotaTujuanActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtKotaTujuanActionPerformed

    private void txtContainerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContainerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContainerActionPerformed

    private void txtContainerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContainerFocusGained
        if(!lst.isVisible())
            setFocusBlockTxt(txtContainer);
    }//GEN-LAST:event_txtContainerFocusGained

    private void txtContainerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContainerKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            btnPreview.requestFocus();
        
    }//GEN-LAST:event_txtContainerKeyPressed

    void setUser(String sUser) {
        this.sUser=sUser;
    }

    private boolean udfCekBeforePrint(){
        boolean bSt=true;
        if(cmbLaporan.getSelectedIndex()==0 && txtContainer.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi No. Container terebih dulu");
            txtContainer.requestFocus();
            bSt=false;
        } 
        if(cmbLaporan.getSelectedIndex()==1 && txtKapal.getText().trim().equalsIgnoreCase("")){ //By Kapal
            JOptionPane.showMessageDialog(this, "Silakan isi Nama Kapal terebih dulu");
            txtKapal.requestFocus();
            bSt=false;
        } 
        return bSt;
    }
    
    private void udfPrint() {
        if(udfCekBeforePrint())
            {
            ///Buku Register Pasien
            FileInputStream file = null;
            try {
                HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                int pilRpt = 0;
                pilRpt = cmbLaporan.getSelectedIndex();
                DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
                String tglAwal = dformat.format(jcTglAwal.getDate());
                String tglAkhir = dformat.format(jcTglAkhir.getDate());
                reportParam.put("tanggal1", tglAwal);
                reportParam.put("tanggal2", tglAkhir);
                switch (pilRpt) {
                    case 0:     //By Kontainer
                        {
                            String sRpt=chkExpedisi.isSelected()?"packing_list_per_kontainer_v1_ex": "packing_list_per_kontainer_v1";
                            //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("report/BukuRegisterUGD.jasper"));
                            reportParam.put("kontainer", txtContainer.getText());
                            
                            
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/"+sRpt+".jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 1:     //by Kapal
                        {
                            //3.  /Detail Transaksi Per Kasir per Fisioterapis
                            reportParam.put("kapal", txtKapal.getText());
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_kapal_per_tgl_v1.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 2:     //By per 1 Merk
                        {
                            reportParam.put("sHeader", txtPetugas.getText()+" ("+lblPegawai.getText()+")");
                            reportParam.put("merk", txtPetugas.getText());
                            //jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_merk22.jasper"));
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_satu_merk.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 3:
                        {
                            reportParam.put("petugas", txtPetugas.getText());
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_merk22.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 4:
                        {
                            reportParam.put("kota", txtKotaTujuan.getText());
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_kota_per_tgl_v1.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                }
            } catch (JRException je) {
                System.out.println(je.getMessage());
            }
            
            
        }
        //catch(NullPointerException ne){JOptionPane.showMessageDialog(null, ne.getMessage(), "SHS Open Source", JOptionPane.OK_OPTION);}
        
    }
    
    /**
     * @param args the command line arguments
     */
/*    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRptKasir().setVisible(true);
            }
        });
    }
 */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnPreview;
    public javax.swing.JCheckBox chkExpedisi;
    public javax.swing.JComboBox cmbLaporan;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel11;
    public javax.swing.JLabel jLabel12;
    public javax.swing.JLabel jLabel13;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JPanel jPanel1;
    public org.jdesktop.swingx.JXDatePicker jcTglAkhir;
    public org.jdesktop.swingx.JXDatePicker jcTglAwal;
    public javax.swing.JLabel lblKapal;
    public javax.swing.JLabel lblKotaTujuan;
    public javax.swing.JLabel lblPegawai;
    public javax.swing.JTextField txtContainer;
    public javax.swing.JTextField txtKapal;
    public javax.swing.JTextField txtKotaTujuan;
    public javax.swing.JTextField txtPetugas;
    // End of variables declaration//GEN-END:variables
    
}
