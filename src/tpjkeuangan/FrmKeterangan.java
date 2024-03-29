/*
 * FrmKeterangan.java
 *
 * Created on April 30, 2008, 10:28 PM
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
public class FrmKeterangan extends javax.swing.JInternalFrame {
    Connection conn;
    boolean isNew=false, isEdit=false;
    DefaultTableModel myModel;
    private String oldKeterangan;
    
    
    /** Creates new form FrmKeterangan */
    public FrmKeterangan() {
        initComponents();
    }

    void setCon(Connection con) {
        conn=con;
    }
    
    private void udfStatusButton(){
        if(isNew){
            btnAdd.setEnabled(false);
            btnDelete.setText("Save");
            btnUpdate.setEnabled(false);
            btnClose.setText("Cancel");
        }else if(isEdit){
            btnAdd.setEnabled(false);
            btnDelete.setText("Save");
            btnUpdate.setEnabled(false);
            btnClose.setText("Cancel");
        }else{
            btnAdd.setEnabled(true);
            btnDelete.setText("Delete");
            btnUpdate.setEnabled(true);
            btnClose.setText("Close");
        }
        
    }
    
    void udfSetLookup(boolean bSt){
        btnAdd.setVisible(!bSt);
        btnUpdate.setVisible(!bSt);
        btnDelete.setText("Pilih");
        chkDefault.setEnabled(!bSt);
        txtKeterangan.setEditable(!bSt);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        btnUpdate = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        chkDefault = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Keterangan Nota");
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

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Keterangan", "Default"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(150);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(1).setMinWidth(30);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(30);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 12, 250, 260));

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane2.setViewportView(txtKeterangan);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 34, 450, 170));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 241, 90, 30));

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(623, 241, 90, 30));

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(526, 241, 90, 30));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 241, 90, 30));

        chkDefault.setText("Set Default");
        getContentPane().add(chkDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 212, 130, -1));

        jLabel1.setText("Keterangan Nota");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 14, 280, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        isNew=true; isEdit=false;
        udfStatusButton();
        oldKeterangan="";
        txtKeterangan.setText("");
        chkDefault.setSelected(true);
        txtKeterangan.requestFocus();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        isNew=false; isEdit=true;
        oldKeterangan=myModel.getValueAt(jTable1.getSelectedRow(), 0).toString();
        udfStatusButton();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(btnClose.getText().equalsIgnoreCase("Close")){
            this.dispose();
        }
        isNew=false; isEdit=false;
        udfStatusButton();
        
    }//GEN-LAST:event_btnCloseActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        SelectionListener listener=new SelectionListener(jTable1);
        jTable1.getSelectionModel().addListSelectionListener(listener);
        jTable1.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        
        myModel=(DefaultTableModel)jTable1.getModel();
        myModel.setNumRows(0);
        jTable1.setModel(myModel);
        
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select * from nota_keterangan where is_delete =false");
            
            while(rs.next()){
                myModel.addRow(new Object[]{
                    rs.getString(1),
                    rs.getBoolean(2)
                });
                
            }
            rs.close();
            st.close();
            
            if(myModel.getRowCount()>0){
                jTable1.setRowSelectionInterval(0, 0);
                jTable1.changeSelection(0, 0, false, false);
            }
        }catch(SQLException se){
            
        }
        
        
        
    }//GEN-LAST:event_formInternalFrameOpened

    private boolean udfUpdateDefault(String sKet){
        boolean b=true;
        try{
            Statement st=conn.createStatement();
            int iExe    =st.executeUpdate("Update nota_keterangan set is_default=false ;" +
                                        "update nota_keterangan set is_default=true where keterangan='"+sKet+"';");
                       
            for (int i=0; i<jTable1.getRowCount(); i++){
                myModel.setValueAt(false, i, 1);
            }
            myModel.setValueAt(true, jTable1.getSelectedRow(), 1);
            
            jTable1.setRowSelectionInterval(jTable1.getSelectedRow(), jTable1.getSelectedRow());
            if(iExe==0){
                JOptionPane.showMessageDialog(this, "Update default keterangan gagal!");
                b=false;
            }
            
            st.close();
        }catch(SQLException se){
        
        }
        return b;
    }
    
    private void udfCancelSave(String s){
        try {
            conn.setAutoCommit(true);
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Insert atau update data gagal!\n"+s);
        } catch (SQLException ex) {
            Logger.getLogger(FrmKeterangan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(btnDelete.getText().equalsIgnoreCase("Save")){
            if(txtKeterangan.getText().trim().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(this, "Silakan isi kterangan terlebih dulu!");
                txtKeterangan.requestFocus();
                return;
            }
            
            try{
                conn.setAutoCommit(false);
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery("select * from nota_keterangan where keterangan='"+oldKeterangan+"'");
                
                if(!rs.next())
                {
                    isNew=true;
                    rs.moveToInsertRow();
                }
                    
                rs.updateString("keterangan", txtKeterangan.getText());
                rs.updateBoolean("is_default", chkDefault.isSelected());
                
                if(isNew) {
                    rs.insertRow();
                    if(chkDefault.isSelected()){
                        if(!udfUpdateDefault(txtKeterangan.getText())){
                            udfCancelSave("Gagal update default");
                        }
                    }
                    myModel.addRow(new Object[]{
                        txtKeterangan.getText(),
                        chkDefault.isSelected()
                    });
                    
                    
                    udfUpdateDefault(txtKeterangan.getText());
//                    isNew=false; isEdit=false;
//                    udfStatusButton();
                }else{
                    rs.updateRow();
                    
                    myModel.setValueAt(txtKeterangan.getText(), jTable1.getSelectedRow(), 0);
                    myModel.setValueAt(chkDefault.isSelected(), jTable1.getSelectedRow(), 1);
                    
                }
                
                conn.setAutoCommit(true);
                isNew=false; isEdit=false;
                udfStatusButton();
            }catch(SQLException se){
                udfCancelSave(se.getMessage());
            }
            
        }else if(btnDelete.getText().equalsIgnoreCase("Delete")){
            if(jTable1.getSelectedRow()>=0){
                if(JOptionPane.showConfirmDialog(this, "Anda yankin untuk menghapus keterangan transaksi ini?", "Expedisi message", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                    try{
                        Statement st=conn.createStatement();
                        int i=st.executeUpdate("update nota_keterangan set is_Delete=true where keterangan='"+txtKeterangan.getText()+"'");
                        
                        if(i>0){
                            myModel.removeRow(jTable1.getSelectedRow());
                        }
                    }catch(SQLException se){
                        JOptionPane.showMessageDialog(this, "Hapus keterangan gagal!\n"+se.getMessage());
                    }
                }
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isKeteranganOn=false;
    }//GEN-LAST:event_formInternalFrameClosed
    
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
            if(table.getSelectedRow()>=0){
                txtKeterangan.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                chkDefault.setSelected((Boolean)table.getValueAt(table.getSelectedRow(), 1));
                oldKeterangan=txtKeterangan.getText();
                }
            }
        
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox chkDefault;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtKeterangan;
    // End of variables declaration//GEN-END:variables
    
}
