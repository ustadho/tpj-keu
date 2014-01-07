/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tpjkeuangan;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author cak-ust
 */
public class FrmRptTagihan extends javax.swing.JInternalFrame {
    private Connection conn;
    private List lstKota=new ArrayList();
    private List lstKapal=new ArrayList();
    /**
     * Creates new form FrmRptTagihan
     */
    public FrmRptTagihan() {
        initComponents();
    }
    
    public void setConn(Connection con){
        this.conn=con;
    }

    private void udfLoadKapal(){
        if (cmbKota.getSelectedIndex() < 0 || conn == null) {
            return;
        }
        try {
            String sQry = "select distinct kt.kode_kapal, kapal.nama_kapal "
                    + "from nota_kapal_tujuan kt  "
                    + "inner join kota on kt.kota_tujuan=kota.kode_kota "
                    + "inner join kapal on kapal.kode_kapal=kt.kode_kapal "
                    + "where kt.kota_tujuan='" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "' "
                    + "order by 2";

            cmbKapal.removeAllItems();
            lstKapal.clear();
            ResultSet rs = conn.createStatement().executeQuery(sQry);
            while (rs.next()) {
                lstKapal.add(rs.getString("kode_kapal"));
                cmbKapal.addItem(rs.getString("nama_kapal"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void udfLoadTglBerangkat(){
        if (cmbKota.getSelectedIndex()<0 ||cmbKapal.getSelectedIndex()<0 || conn == null) {
            return;
        }
        try {
            String sQry = "select distinct to_char(tgl_berangkat, 'dd/MM/yyyy') as tanggal, "
                    + "tgl_berangkat::date "
                    + "from "
                    + "nota_kapal_tujuan kt "
                    + "where kt.kota_tujuan='" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "' "
                    + "and kode_kapal='" + lstKapal.get(cmbKapal.getSelectedIndex()).toString() + "'"
                    + "order by 2 desc";

            cmbTglBerangkat.removeAllItems();
            ResultSet rs = conn.createStatement().executeQuery(sQry);
            while (rs.next()) {
                cmbTglBerangkat.addItem(rs.getString("tanggal"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printPreview(){
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
//                reportParam.put("kodeCust", sKodeCust);
//                reportParam.put("namaCust", sNamaCust);
//                reportParam.put("tagihanPer", tagihanPer);
                reportParam.put("tahun", cmbTahun.getSelectedItem().toString());
                reportParam.put("jtTempo", new SimpleDateFormat("yyyy-MM-dd").format(jXDatePicker1.getDate()));
                reportParam.put("kodeKota", lstKota.get(cmbKota.getSelectedIndex()).toString());
                reportParam.put("namaKota", cmbKota.getSelectedItem().toString());
                String sRpt="";
                switch(jList1.getSelectedIndex()){
                    case 0:{
                        sRpt="PiutangOutstandingPerKota";
                        break;
                    }
                    case 1:{
                        sRpt="PiutangPerMerk";
                        break;
                    }
                    default:{
                        break;
                    }
                }
                
                jasperReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/tpjkeuangan/Reports/"+sRpt+".jasper"));
                JasperPrint print = JasperFillManager.fillReport(
                        jasperReport, reportParam, conn);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if(print.getPages().isEmpty()){
                    JOptionPane.showMessageDialog(this, "Report tidak ditemukan");
                    return;
                }
                print.setOrientation(jasperReport.getOrientation());
                JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            System.out.println("Error :"+ ex.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbKota = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbKapal = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbTglBerangkat = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel5 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox();

        setClosable(true);
        setTitle("Laporan Tagihan");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "1. Rekap Outstanding per Kota", "2. Rekap Outstanding per Merk", "3. Rekap Nota per Pelanggan" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Kota :");

        jLabel2.setText("Kapal:");

        jLabel3.setText("Tgl. Brgkt");

        jButton1.setText("Cancel");
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

        jLabel4.setText("Jth. Tempo");

        jLabel5.setText("Tahun :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKota, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbKapal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTglBerangkat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 52, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbTahun, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKapal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTglBerangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        requestFocusInWindow(true);
        try {
            
            ResultSet rs = null;
            cmbTahun.removeAllItems();
            rs = conn.createStatement().executeQuery("select distinct to_char(tgl_nota, 'yyyy') as tahun, to_Char(current_date, 'yyyy') as th_skg "
                    + "from nota order by to_char(tgl_nota, 'yyyy') desc ");
            String sTahunSkg = "";
            while (rs.next()) {
                cmbTahun.addItem(rs.getString("tahun"));
                sTahunSkg = rs.getString("th_skg");
            }
            rs.close();
            cmbKota.removeAllItems();
            rs = conn.createStatement().executeQuery("select * From kota order by nama_kota");
            while (rs.next()) {
                lstKota.add(rs.getString("kode_kota"));
                cmbKota.addItem(rs.getString("nama_kota"));
            }
            rs.close();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
        if (!Utama.sNamaKota.equalsIgnoreCase("")) {
            cmbKota.setSelectedItem(Utama.sNamaKota);
        }
//        udfLoadKapal();
        cmbKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                udfLoadKapal();
            }
        });
        cmbKapal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                udfLoadTglBerangkat();
            }
        });
        udfLoadKapal();
        udfLoadTglBerangkat();
        jList1.setSelectedIndex(0);
    }//GEN-LAST:event_formInternalFrameOpened

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        printPreview();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbKapal;
    private javax.swing.JComboBox cmbKota;
    private javax.swing.JComboBox cmbTahun;
    private javax.swing.JComboBox cmbTglBerangkat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    // End of variables declaration//GEN-END:variables
}
