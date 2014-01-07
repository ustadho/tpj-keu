/*
 * DlgKoreksiPembayaran.java
 *
 * Created on June 13, 2008, 6:26 PM
 */

package tpjkeuangan;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  oestadho
 */
public class DlgKoreksiPembayaran extends javax.swing.JDialog {
    private DefaultTableModel myModel;
    Connection conn;
    String sUserName;
    boolean isKoreksi;
    
    /** Creates new form DlgKoreksiPembayaran */
    public DlgKoreksiPembayaran(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    void setKodeBayar(String toString) {
        txtNo.setText(toString);
    }

    void setKoreksi(boolean b) {
        isKoreksi=b;
    }
    
    void setUserName(String s){
         sUserName=s;
    }
    void setConn(Connection con){
        conn=con;
    }
    
//    private void udfLoadTable(){
//        int iRowCount = 0;
//        try {
//            String sQry = "";
//            if (optNoNota.isSelected()) {
//                sQry = "select d.no_bayar, no_nota, coalesce(jumlah,0) as jumlah, coalesce(klem,0) as klem, " + "coalesce(alat_pembayaran,'') as alat_pembayaran, coalesce(memo,'') as memo " + "from nota_pembayaran_detail d " + "inner join nota_pembayaran p on p.no_bayar=d.no_bayar " + "left join nota_alat_pembayaran ap on ap.kode=p.alat_bayar where no_nota='" + txtNo.getText() + "' order by d.no_bayar";
//            } else {
//                sQry = "";
//            }
//            ResultSet rs = conn.createStatement().executeQuery(sQry);
//            if(rs.next()){
//                myModel = new DefaultTableModel();
//                jTable1.setModel(myModel);
//
//                for(int i=1;i <= rs.getMetaData().getColumnCount();i++) {
//                    myModel.addColumn(rs.getMetaData().getColumnName(i));
//
//                }
//                while (rs.next()) {
//                    Object arObj[] = new Object[rs.getMetaData().getColumnCount()];
//                    for(int i=1;i <= rs.getMetaData().getColumnCount();i++) {
//                        if(rs.getObject(i) != null){
//                            arObj[i-1]=rs.getObject(i);
//                        } else {                        
//                            arObj[i-1]=new Object();
//                        }
//                    }
//                    myModel.addRow(arObj);
//                    iRowCount++;
//                }            
//                if (jTable1.getRowCount()>0) {
//                    jTable1.setRowSelectionInterval(0,0) ;
//                } else{
//                    //this.setVisible(false);
//                    String sMsg=optNoNota.isSelected()? "No Nota tidak ditemukan!":"Kode pembayaran tidak ditemukan!";
//                    JOptionPane.showMessageDialog(this, sMsg);
//                    //return;
//
//                }
//            }else{
//                String sMsg=optNoNota.isSelected()? "No Nota tidak ditemukan!":"Kode pembayaran tidak ditemukan!";
//                JOptionPane.showMessageDialog(this, sMsg);
//            }
//            rs.close();    
//            //jScrollPane1.setBounds(jScrollPane1.getX(),jScrollPane1.getY(),jScrollPane1.getWidth(),iRowCount*tblList.getRowHeight());
//            //setBounds(getX(),getY(),getWidth(),(iRowCount+2)*jTable1.getRowHeight());
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(DlgKoreksiPembayaran.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        
//    }
    
    void udfLoadBayar(){
        if(txtNo.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi No. Nota terlebih dulu!");
            txtNo.requestFocus();
            return;
        }
        myModel=(DefaultTableModel)jTable1.getModel();
        myModel.setNumRows(0);
        jTable1.setModel(myModel);
        
        String sQry = "select d.no_bayar, no_nota, coalesce(jumlah,0) as jumlah, coalesce(klem,0) as klem, (coalesce(jumlah,0)+coalesce(klem,0)) as total, " + 
                "coalesce(alat_pembayaran,'') as alat_pembayaran, coalesce(memo,'') as memo " + 
                "from nota_pembayaran_detail d " + "inner join nota_pembayaran p on p.no_bayar=d.no_bayar " + 
                "left join nota_alat_pembayaran ap on ap.kode=p.alat_bayar where no_nota='" + txtNo.getText() + "' " +
                "order by d.no_bayar";
        
        try{
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            while(rs.next()){
                myModel.addRow(new Object[]{
                    myModel.getRowCount()+1,
                    rs.getString("no_bayar"),
                    rs.getString("alat_pembayaran"),
                    rs.getString("memo"),
                    rs.getFloat("jumlah"),
                    rs.getFloat("klem"),
                    rs.getFloat("total")
                });

            }

            if(myModel.getRowCount()>0){
                jTable1.setRowSelectionInterval(0, 0);
            }
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Koreksi pembayaran");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Koreksi pembayaran by Nota"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("No. Nota");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 35, 90, -1));

        txtNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoKeyPressed(evt);
            }
        });
        jPanel1.add(txtNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 30, 230, 22));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Alat Bayar", "Keterangan", "Jumlah", "Klem", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(10);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 56, 560, 250));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 43, 580, 320));

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(474, 366, 80, 30));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 366, 80, 30));

        btnDel.setText("Delete");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 366, 80, 30));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-437)/2, 600, 437);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            udfLoadBayar();
    }//GEN-LAST:event_txtNoKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        int iRow=jTable1.getSelectedRow();
        if(iRow>=0){
            DlgBayarNota d1=new DlgBayarNota(JOptionPane.getFrameForComponent(this), true);
            d1.setConn(conn);
            d1.setKodeBayar(jTable1.getValueAt(iRow, 1).toString());
            d1.setNota(txtNo.getText());
            d1.setIsKoreksi(true);
            d1.setIsNew(false);
            d1.setSrcModel(myModel);
            d1.setRowSelected(iRow);
            d1.setTitle("Koreksi pembayaran Nota "+txtNo.getText() +" ~ Kode bayar: "+jTable1.getValueAt(iRow, 1).toString());
            d1.setVisible(true);
            if(d1.isSelected())
                udfLoadBayar();
//                }
//            }
//            else
//                d1.dispose();
            
        }
}//GEN-LAST:event_btnUpdateActionPerformed

    private boolean udfDelete(String sNoBayar){
        boolean b=false;
        try{
            conn.setAutoCommit(false);
            String sUpdate= "insert into nota_pembayaran_h(no_bayar, tanggal, kode_cust, pay_for, alat_bayar, rate, check_no, tgl_cek, " +
                            "memo, check_amount ,void_check , fiscal_payment ,is_lunas ,tgl_lunas ,user_tr ,date_trx , to_tujuan) " +
                            "select no_bayar, tanggal, kode_cust, pay_for, alat_bayar, rate, check_no, tgl_cek, " +
                            "memo, check_amount ,void_check , fiscal_payment ,is_lunas ,tgl_lunas ,user_tr ,date_trx , to_tujuan " +
                            "from nota_pembayaran where no_bayar='"+sNoBayar+"'; " ;
            sUpdate+=       "insert into nota_pembayaran_detail_h (no_bayar ,no_nota ,jumlah ,diskon ,pajak ,denda ,klem ,is_batal ," +
                            "user_ins ,date_ins ,user_batal ,date_batal ) " +
                            "select no_bayar ,no_nota ,jumlah ,diskon ,pajak ,denda ,klem ,is_batal ," +
                            "user_ins ,date_ins ,'"+sUserName+"' ,now() from nota_pembayaran_detail where no_bayar='"+sNoBayar+"'; ";
             
            sUpdate+=       "delete from nota_pembayaran where no_bayar='"+sNoBayar+"'; delete from nota_pembayaran_detail where no_bayar='"+sNoBayar+"';";
            
            int iUpd=conn.createStatement().executeUpdate(sUpdate);
            
            conn.setAutoCommit(true);
            b=true;
        }catch(SQLException se){
            try {
                b=false;
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Proses delete data gagal!\n"+se.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(DlgBayarNota.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return b;
    }
    
    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
        int iRow=jTable1.getSelectedRow();
        if(iRow>=0){
            if(JOptionPane.showConfirmDialog(this, "anda yakin untuk menghapus pembayaran tesebut?")==JOptionPane.YES_OPTION){
                if(udfDelete(jTable1.getValueAt(iRow, 1).toString())){
                    JOptionPane.showMessageDialog(this, "Hapus data sukses!", "Message", JOptionPane.INFORMATION_MESSAGE);
                    myModel.removeRow(iRow);
                }
                    
            }
        }
        
    }//GEN-LAST:event_btnDelActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgKoreksiPembayaran dialog = new DlgKoreksiPembayaran(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtNo;
    // End of variables declaration//GEN-END:variables
    
}
