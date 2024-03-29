/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import main.GeneralFunction;

/**
 *
 * @author cak-ust
 */
public class DlgBiayaKapal extends javax.swing.JDialog {
    private Connection conn;
    ResultSet rs=null;
    GeneralFunction fn=new GeneralFunction();
    
    /**
     * Creates new form DlgBiayaKapal
     */
    public DlgBiayaKapal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        fn.addKeyListenerInContainer(jPanel1, new MyKeyListener(), txtFocusListener);
    }

    public void setConn(Connection con){
        this.conn=con;
        fn.setConn(conn);
    }
    
    public void udfLoadBiaya(int i){
        String sQry="select * from nota_kapal_tujuan where serial_kode="+i+"" ;
        try{
            rs=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                    .executeQuery(sQry);
            if(rs.next()){
                txtSpil.setText(GeneralFunction.intFmt.format(rs.getDouble("biaya_spil")));
                txtTanto.setText(GeneralFunction.intFmt.format(rs.getDouble("biaya_tanto")));
                txtExpedisi.setText(GeneralFunction.intFmt.format(rs.getDouble("biaya_expedisi")));
                txtPembongkaran.setText(GeneralFunction.intFmt.format(rs.getDouble("biaya_bongkar")));
            }
        }catch(SQLException se)    {
            JOptionPane.showMessageDialog(this, se.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPembongkaran = new javax.swing.JTextField();
        txtSpil = new javax.swing.JTextField();
        txtTanto = new javax.swing.JTextField();
        txtExpedisi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Biaya Kapal");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("PEMBONGKARAN : ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 120, 20));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("SPIL :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 120, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("TANTO :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 80, 20));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("EXPEDISI :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, 20));

        txtPembongkaran.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtPembongkaran.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPembongkaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtPembongkaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, 110, 22));

        txtSpil.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtSpil.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSpil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtSpil, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 100, 22));

        txtTanto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtTanto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTanto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtTanto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 110, 22));

        txtExpedisi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtExpedisi.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtExpedisi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtExpedisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 100, 22));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Keterangan :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 120, 20));

        txtKeterangan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtKeterangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(txtKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 340, 22));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 450, 110));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Save");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 70, 30));

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Close");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 70, 30));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-487)/2, (screenSize.height-206)/2, 487, 206);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            if(!rs.isClosed()){
                rs.updateDouble("biaya_spil", GeneralFunction.udfGetDouble(txtSpil.getText()));
                rs.updateDouble("biaya_tanto", GeneralFunction.udfGetDouble(txtTanto.getText()));
                rs.updateDouble("biaya_expedisi", GeneralFunction.udfGetDouble(txtExpedisi.getText()));
                rs.updateDouble("biaya_bongkar", GeneralFunction.udfGetDouble(txtPembongkaran.getText()));
                rs.updateString("keterangan_biaya", txtKeterangan.getText());
                rs.updateRow();
            }
            
            JOptionPane.showMessageDialog(this, "Saved record successfuly!");
            rs.close();
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(DlgBiayaKapal.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }//GEN-LAST:event_jButton1ActionPerformed

    private FocusListener txtFocusListener=new FocusListener() {
        public void focusGained(FocusEvent e) {
            if(e.getSource() instanceof JTextField || e.getSource() instanceof JFormattedTextField){
                ((JTextField)e.getSource()).setBackground(Color.YELLOW);
                if( e.getSource() instanceof JTextField){
                    ((JTextField)e.getSource()).setSelectionStart(0);
                    ((JTextField)e.getSource()).setSelectionEnd(((JTextField)e.getSource()).getText().length());

                }
            }
        }


        public void focusLost(FocusEvent e) {
            if(e.getSource().getClass().getSimpleName().equalsIgnoreCase("JTextField")||
                    e.getSource().getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")){
                ((JTextField)e.getSource()).setBackground(Color.WHITE);
                
                if(e.getSource().equals(txtKeterangan))
                    return;
                ((JTextField)e.getSource()).setText(
                    GeneralFunction.intFmt.format(
                        GeneralFunction.udfGetInt(((JTextField)e.getSource()).getText())
                    )
                );
                
           }
        }
    } ;
    
    public class MyKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(java.awt.event.KeyEvent evt) {
            if(!evt.getSource().equals(txtKeterangan))
                fn.keyTyped(evt);
        }

        public void keyPressed(KeyEvent evt) {
            Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
            int keyKode = evt.getKeyCode();
            switch(keyKode){
               case KeyEvent.VK_ENTER : {
                    if(!(ct instanceof JTable))                    {
                        if (!fn.isListVisible()){
                            Component c = findNextFocus();
                            if (c==null) return;
                            c.requestFocus();
                        }else{
                            fn.lstRequestFocus();
                        }
                    }
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
                        {
                            if (!fn.isListVisible()){
                                Component c = findNextFocus();
                                if (c==null) return;
                                c.requestFocus();
                            }else{
                                fn.lstRequestFocus();
                            }
                            break;
                    }
                }

                case KeyEvent.VK_UP: {
                    if(!(evt.getSource() instanceof JTable)){
                        Component c = findPrevFocus();
                        c.requestFocus();
                    }
                    break;
                }
                case KeyEvent.VK_ESCAPE:{
                    dispose();
                    break;
                }
                case KeyEvent.VK_F2:{
                    //udfSave();
                    break;
                }
                
            }
        }

//        @Override
//        public void keyReleased(KeyEvent evt){
//            if(evt.getSource().equals(txtDisc)||evt.getSource().equals(txtQty)||evt.getSource().equals(txtUnitPrice))
//                GeneralFunction.keyTyped(evt);
//        }

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
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DlgBiayaKapal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgBiayaKapal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgBiayaKapal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgBiayaKapal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgBiayaKapal dialog = new DlgBiayaKapal(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtExpedisi;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtPembongkaran;
    private javax.swing.JTextField txtSpil;
    private javax.swing.JTextField txtTanto;
    // End of variables declaration//GEN-END:variables
}
