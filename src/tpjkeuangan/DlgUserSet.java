/*
 * DlgUserSet.java
 *
 * Created on January 26, 2008, 5:16 PM
 */

package tpjkeuangan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  oestadho
 */
public class DlgUserSet extends javax.swing.JDialog {
    private boolean isEdit=false;
    DefaultTableModel myModel;
    private Statement st;
    private ResultSet rs;
    private Connection conn;
    private boolean isNew=false;
    /** Creates new form DlgUserSet */
    public DlgUserSet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public void setConn(Connection con){
        conn=con;
    }
    
    private void setButtonText(){
        if(isEdit){
            btnEdit.setText("Save");
            btnClose.setText("Cancel");
            
        }else{
            btnEdit.setText("Edit");
            btnClose.setText("Close");
        }
        btnNew.setEnabled(!isEdit);
    }
    
    private void udfBlank(){
        txtUserName.setText("");
        txtPass.setText("");
        cmbAuth.setSelectedIndex(2);
        txtKet.setText("");
        txtUserName.requestFocus();
    }

    private void udfEdit() {
        try {
            String pass = "";
            char[] chrPass = txtPass.getPassword();
            for (int i = 0; i < chrPass.length; i++) {
                pass = pass + chrPass[i];
                chrPass[i] = '0';
            }
            
            setButtonText();
            
            if (isNew) {
                try {
                    rs.moveToInsertRow();
                } catch (SQLException ex) {
                    Logger.getLogger(DlgUserSet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                
            }

            rs.updateString("user_name", txtUserName.getText());
            rs.updateString("pwd", pass);
            rs.updateString("keterangan", txtKet.getText());
            rs.updateInt("authority", cmbAuth.getSelectedIndex());
            
            if(isNew){
                rs.insertRow();
                myModel.addRow(new Object[]{
                    txtUserName.getText(),
                    pass,
                    cmbAuth.getSelectedIndex(),
                    txtKet.getText()
                });
                
                
            }
            else{
                int iRow=tblUser.getSelectedRow();
                rs.updateRow();
                myModel.setValueAt(txtUserName.getText(), iRow, 0);
                myModel.setValueAt(pass, iRow, 1);
                myModel.setValueAt(cmbAuth.getSelectedIndex(), iRow, 2);
                myModel.setValueAt(txtKet.getText(), iRow, 3);
            }
            
            isEdit=false;
            isNew=false;
            setButtonText();
            
        } catch (SQLException ex) {
            Logger.getLogger(DlgUserSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
            btnEdit.setEnabled(false);
            btnDel.setEnabled(false);
            if(table.getSelectedRow()>=0){
                //tblItem.getModel().addTableModelListener(new MyTableModelListener(tblItem));
                rowPos = table.getSelectedRow();           
               
                if (rowPos >=0 && rowPos < table.getRowCount() && table.getValueAt(rowPos,0)!=null) { 
                    try {
                        btnEdit.setEnabled(true);

                        btnDel.setEnabled(true);

                        txtUserName.setText(myModel.getValueAt(rowPos, 0).toString());
                        txtPass.setText(myModel.getValueAt(rowPos, 1).toString());
                        cmbAuth.setSelectedIndex(Integer.valueOf(myModel.getValueAt(rowPos, 2).toString()));
                        txtKet.setText(myModel.getValueAt(rowPos, 3).toString());

                        rs = st.executeQuery("select * from user_set where user_name='" + txtUserName.getText() + "'");
                        if (rs.next()) {
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(DlgUserSet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                   
                    
                }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        cmbAuth = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtKet = new javax.swing.JTextArea();
        btnNew = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUser = new javax.swing.JTable();
        txtPass = new javax.swing.JPasswordField();
        btnDel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("User List");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Keterangan");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 104, 100, -1));

        jLabel2.setText("Username");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 24, 100, -1));

        jLabel3.setText("Password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 50, 100, -1));

        jLabel4.setText("Authority");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(191, 76, 100, -1));

        txtUserName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserNameActionPerformed(evt);
            }
        });
        getContentPane().add(txtUserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 22, 230, -1));

        cmbAuth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admnistrator", "Supervisor", "User" }));
        getContentPane().add(cmbAuth, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 73, 230, -1));

        txtKet.setColumns(20);
        txtKet.setRows(5);
        jScrollPane2.setViewportView(txtKet);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(284, 99, 230, -1));

        btnNew.setText("New");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 262, 80, 30));

        btnClose.setText("jButton1");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 261, 80, 30));

        btnEdit.setText("jButton1");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 261, 80, 30));

        tblUser.setAutoCreateRowSorter(true);
        tblUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Username", "Password", "Aouth", "Ket"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUser);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 7, 180, 250));

        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });
        getContentPane().add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 48, 230, -1));

        btnDel.setText("Delete");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 262, 80, 30));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-535)/2, (screenSize.height-336)/2, 535, 336);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        isEdit=true;
        isNew =true;
        setButtonText();
        udfBlank();
}//GEN-LAST:event_btnNewActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        SelectionListener listener = new SelectionListener(tblUser);
        tblUser.getSelectionModel().addListSelectionListener(listener);
        tblUser.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        tblUser.setRequestFocusEnabled(true);
        try {
            setButtonText();
            myModel = (DefaultTableModel) tblUser.getModel();
            myModel.setNumRows(0);
            tblUser.setModel(myModel);

            tblUser.getColumnModel().removeColumn(tblUser.getColumnModel().getColumn(1));
            tblUser.getColumnModel().removeColumn(tblUser.getColumnModel().getColumn(1));
            tblUser.getColumnModel().removeColumn(tblUser.getColumnModel().getColumn(1));

            st = conn.createStatement();
            rs=st.executeQuery("select * from user_set");
            
            while(rs.next()){
                myModel.addRow(new Object[]{
                    rs.getString("user_name"),
                    rs.getString("pwd")==null? "":rs.getString("pwd"),
                    rs.getInt("authority") ,
                    rs.getString("keterangan")==null? "": rs.getString("keterangan"),
                });
                
            }
            
            if(myModel.getRowCount()>0){
                tblUser.setRowSelectionInterval(0, 0);
                tblUser.changeSelection(0, 0, false, false);
            }
             
        } catch (SQLException ex) {
            Logger.getLogger(DlgUserSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_formWindowOpened

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(isEdit){
            isEdit=false;
            isNew =false;
            setButtonText();
        }else{
            this.dispose();
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtUserNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserNameActionPerformed

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (btnEdit.getText().equalsIgnoreCase("Save"))
            udfEdit();
        else{
            isEdit=true;
            setButtonText();
        }
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus user '"+txtUserName.getText()+"' ?")==JOptionPane.YES_OPTION){
            try {
                rs.deleteRow();
                myModel.removeRow(tblUser.getSelectedRow());
            } catch (SQLException ex) {
                Logger.getLogger(DlgUserSet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
}//GEN-LAST:event_btnDelActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgUserSet dialog = new DlgUserSet(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox cmbAuth;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblUser;
    private javax.swing.JTextArea txtKet;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    
}
