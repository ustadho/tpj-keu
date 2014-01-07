/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgNotaClaim.java
 *
 * Created on Jan 27, 2013, 10:55:10 AM
 */

package tpjkeuangan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.accessibility.AccessibleContext;
import javax.swing.JOptionPane;
import main.GeneralFunction;

/**
 *
 * @author cak-ust
 */
public class DlgNotaClaim extends javax.swing.JDialog {
    private Connection conn;
    private String sNota="";
    private Object srcForm;


    /** Creates new form DlgNotaClaim */
    public DlgNotaClaim(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setConn(Connection con) {
        this.conn=con;
    }

    public void tampilkaNota(String s){
        String sQry="select * from fn_nota_show_claim('"+s+"') as "
                + "(no_nota varchar, kepada varchar, customer varchar, "
                + "total double precision, klaim double precision, "
                + "lain2 double precision, pelunasan double precision, lebih double precision, ket_klaim text, "
                + "kode_cust varchar, to_tujuan boolean)";

        try{
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            if(rs.next()){
                lblNota.setText(rs.getString("no_nota"));
                lblKepada.setText(rs.getString("kepada"));
                lblPelanggan.setText(rs.getString("customer"));
                lblJumlah.setText(GeneralFunction.intFmt.format(rs.getDouble("total")));
                lblPelunasan.setText(GeneralFunction.intFmt.format(rs.getDouble("pelunasan")));
                txtKlaim.setText(GeneralFunction.intFmt.format(rs.getDouble("klaim")));
                txtLain2.setText(GeneralFunction.intFmt.format(rs.getDouble("lain2")));
                txtKetKlaim.setText(rs.getString("ket_klaim"));
            }
            rs.close();
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }
    private boolean udfCekBeforeSave(){
        if(GeneralFunction.udfGetInt(txtKlaim.getText()) > 0 && txtKetKlaim.getText().trim().length()==0){
            JOptionPane.showMessageDialog(this, "Masukkan keterangan Klaim terlebih dulu!");
            if(!txtKetKlaim.isFocusOwner())
                txtKetKlaim.requestFocus();
            return false;
        }

        return true;
    }

    private void udfSave(){
        if(!udfCekBeforeSave())
            return;
        try {
            String sUpd = "UPDATE nota SET " +
                    "lain2=?, klaim=?, ket_klaim=? " +
                    "WHERE no_nota=?;";
            PreparedStatement ps = conn.prepareStatement(sUpd);
            ps.setDouble(1, GeneralFunction.udfGetDouble(txtLain2.getText()));
            ps.setDouble(2, GeneralFunction.udfGetDouble(txtKlaim.getText()));
            ps.setString(3, txtKetKlaim.getText());
            ps.setString(4, lblNota.getText());

            int i=ps.executeUpdate();
            if(i>0){
                if(srcForm!=null && srcForm instanceof FrmListTagihanPerCust)
                    ((FrmListTagihanPerCust)srcForm).udfLoadTagihan(lblNota.getText());
                
                JOptionPane.showMessageDialog(this, "Update nota sukses!");
            }
            ps.close();
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblNota = new javax.swing.JLabel();
        lblPelanggan = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblJumlah = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtKlaim = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKetKlaim = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblKepada = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtLain2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        lblPelunasan = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Klaim Dll."); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("No. Nota :");
        jLabel1.setName("jLabel1"); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 20));

        lblNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblNota.setName("lblNota"); // NOI18N
        getContentPane().add(lblNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 310, 20));

        lblPelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPelanggan.setName("lblPelanggan"); // NOI18N
        getContentPane().add(lblPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 370, 20));

        jLabel4.setText("Pelanggan :");
        jLabel4.setName("jLabel4"); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));

        jLabel5.setText("Klaim:");
        jLabel5.setName("jLabel5"); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 70, 20));

        lblJumlah.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblJumlah.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblJumlah.setName("lblJumlah"); // NOI18N
        getContentPane().add(lblJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 100, 20));

        jLabel7.setText("Jumlah :");
        jLabel7.setName("jLabel7"); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 70, 20));

        txtKlaim.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtKlaim.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtKlaim.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKlaim.setName("txtKlaim"); // NOI18N
        txtKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKlaimKeyTyped(evt);
            }
        });
        getContentPane().add(txtKlaim, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 100, 20));

        jLabel8.setText("<html>\nKeterangan <br> Klaim\n</html>"); // NOI18N
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel8.setName("jLabel8"); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 70, 40));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtKetKlaim.setColumns(20);
        txtKetKlaim.setRows(5);
        txtKetKlaim.setName("txtKetKlaim"); // NOI18N
        jScrollPane1.setViewportView(txtKetKlaim);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 370, 60));

        jButton1.setText("Update");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 70, -1));

        jButton2.setText("Cancel");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 80, -1));

        lblKepada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblKepada.setName("lblKepada"); // NOI18N
        getContentPane().add(lblKepada, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 370, 20));

        jLabel6.setText("Kepada :");
        jLabel6.setName("jLabel6"); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 20));

        jLabel9.setText("Lain-lain");
        jLabel9.setName("jLabel9"); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 70, 20));

        txtLain2.setFont(new java.awt.Font("Tahoma", 1, 11));
        txtLain2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtLain2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtLain2.setName("txtLain2"); // NOI18N
        txtLain2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLain2KeyTyped(evt);
            }
        });
        getContentPane().add(txtLain2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 100, 20));

        jLabel10.setText("Pelunasan");
        jLabel10.setName("jLabel10"); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 70, 20));

        lblPelunasan.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblPelunasan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPelunasan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPelunasan.setName("lblPelunasan"); // NOI18N
        getContentPane().add(lblPelunasan, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 100, 20));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-471)/2, (screenSize.height-283)/2, 471, 283);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        udfSave();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtKlaimKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKlaimKeyTyped
        GeneralFunction.keyTyped(evt);
    }//GEN-LAST:event_txtKlaimKeyTyped

    private void txtLain2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLain2KeyTyped
        GeneralFunction.keyTyped(evt);
    }//GEN-LAST:event_txtLain2KeyTyped

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgNotaClaim dialog = new DlgNotaClaim(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblKepada;
    private javax.swing.JLabel lblNota;
    private javax.swing.JLabel lblPelanggan;
    private javax.swing.JLabel lblPelunasan;
    private javax.swing.JTextArea txtKetKlaim;
    private javax.swing.JTextField txtKlaim;
    private javax.swing.JTextField txtLain2;
    // End of variables declaration//GEN-END:variables

    void setSrcForm(Object aThis) {
        this.srcForm=aThis;
    }

}
