/*
 * FrmCustomerEdit.java
 *
 * Created on November 6, 2007, 10:23 PM
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
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  oestadho
 */
public class FrmCustomerEdit extends javax.swing.JDialog {
    private ListRsbm lst;
    private Connection conn;

    private boolean isNew;
    private MyKeyListener kListener=new MyKeyListener();
    
    private ResultSet rs;
    private Statement stmt;
    private DefaultTableModel srcModel;
    private int rowPos;

    private JTable srcTable;
    
    /** Creates new form FrmCustomerEdit */
    public FrmCustomerEdit(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        txtKode.addKeyListener(kListener);
        txtNama.addKeyListener(kListener);
        //txtMerk.addKeyListener(kListener);
        txtAlamat.addKeyListener(kListener);
        txtKota.addKeyListener(kListener);
        txtTelepon.addKeyListener(kListener);
        txtKontak.addKeyListener(kListener);
        txtHP.addKeyListener(kListener);
        txtTOP.addKeyListener(kListener);
        cmdSimpan.addKeyListener(kListener);
        cmdBatal.addKeyListener(kListener);
    }
  
    public void setModel(DefaultTableModel model){
        srcModel=model;
    }
    
    public void setRowPos(int iPos)
    {
        rowPos=iPos;
    }
    
    private int udfGetInt(String sNum){
        int hsl=0;
        if(!sNum.trim().equalsIgnoreCase("")){
            try{
                hsl=Integer.valueOf(sNum.replace(",", ""));
            }catch(NumberFormatException ne){
                hsl=0;
            }catch(IllegalArgumentException i){
                hsl=0;
            }
        }
        return hsl;
    }
    
    private void udfSave(){
        if(udfCekbeforeSave()){
            try {
                if(isNew){
                    Statement stm=conn.createStatement();
                    ResultSet rsm=stm.executeQuery("select fn_get_kode_customer()");
                    
                    if(rsm.next())
                        txtKode.setText(rsm.getString(1));
                    
                    rsm.close();
                    stm.close();
                    
                    rs.moveToInsertRow();
                    
                }
                // Set values for the new row.
                rs.updateString("kode_cust", txtKode.getText());
                
                rs.updateString("nama", txtNama.getText());
                rs.updateString("merk", "");
                rs.updateString("alamat_1", txtAlamat.getText());
                rs.updateString("kota", txtKota.getText());
                rs.updateString("telp", txtTelepon.getText());
                rs.updateString("contact_person", txtKontak.getText());
                rs.updateString("hp", txtHP.getText());
                rs.updateInt("termin_pembayaran", Integer.parseInt(txtTOP.getText()));
                rs.updateInt("nota_start", Integer.parseInt(txtNotaStart.getText()));
                
                // Insert the new row
                if(isNew)
                    rs.insertRow();
                else
                    rs.updateRow();
            
                if(isNew){
                    srcModel.addRow(new Object[]{
                        txtKode.getText(),
                        txtNama.getText(),
                        txtAlamat.getText(),
                        lblKota.getText(),
                        txtTelepon.getText(),
                        txtKontak.getText(),
                        txtHP.getText(),
                        udfGetInt(txtTOP.getText())
                    });
                    srcTable.setRowSelectionInterval(srcModel.getRowCount()-1, srcModel.getRowCount()-1);
                    
                }else{
                    srcModel.setValueAt(txtKode.getText(), rowPos, 0);
                    srcModel.setValueAt(txtNama.getText(), rowPos, 1);
                    srcModel.setValueAt(txtAlamat.getText(), rowPos, 2);
                    srcModel.setValueAt(lblKota.getText(), rowPos, 3);
                    srcModel.setValueAt(txtTelepon.getText(), rowPos, 4);
                    srcModel.setValueAt(txtKontak.getText(), rowPos, 5);
                    srcModel.setValueAt(txtHP.getText(), rowPos, 6);
                    srcModel.setValueAt(udfGetInt(txtTOP.getText()), rowPos, 7);
                }
                
                dispose();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ne){
                JOptionPane.showMessageDialog(this, "Silakan isikan termin pembayaran dengan angka", "Message", JOptionPane.OK_OPTION);
                txtTOP.requestFocus();
                return;
            }
            
        }
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
                
                 
                case KeyEvent.VK_F3: {  //Search
                 //   udfFilter();
                    break;
                }
                
                case KeyEvent.VK_F5: {  //New -- Add
                    udfSave();
                    break;
                }
                
                case KeyEvent.VK_ESCAPE: {
                    dispose();
                    break;
                }
                //default ;
                
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtAlamat = new javax.swing.JTextField();
        txtKota = new javax.swing.JTextField();
        txtTelepon = new javax.swing.JTextField();
        txtKontak = new javax.swing.JTextField();
        txtHP = new javax.swing.JTextField();
        txtTOP = new javax.swing.JTextField();
        cmdBatal = new javax.swing.JButton();
        cmdSimpan = new javax.swing.JButton();
        lblKota = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNotaStart = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(204, 255, 255));
        jLabel1.setText("Kode");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 61, 55, -1));

        jLabel2.setForeground(new java.awt.Color(204, 255, 255));
        jLabel2.setText("Nama/ TOKO");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 87, 82, -1));

        jLabel4.setForeground(new java.awt.Color(204, 255, 255));
        jLabel4.setText("Alamat");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 117, 64, -1));

        jLabel5.setForeground(new java.awt.Color(204, 255, 255));
        jLabel5.setText("Kota");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 145, 53, -1));

        jLabel6.setForeground(new java.awt.Color(204, 255, 255));
        jLabel6.setText("Telepon");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 175, 69, -1));

        jLabel7.setForeground(new java.awt.Color(204, 255, 255));
        jLabel7.setText("Kontak");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 199, 64, -1));

        jLabel8.setForeground(new java.awt.Color(204, 255, 255));
        jLabel8.setText("HP");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 227, 44, -1));

        jLabel9.setForeground(new java.awt.Color(204, 255, 255));
        jLabel9.setText("Termin (Hr)");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 254, 102, -1));

        txtKode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKode.setEnabled(false);
        getContentPane().add(txtKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 60, 178, 21));

        txtNama.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 87, 472, 21));

        txtAlamat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 114, 471, 24));

        txtKota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKotaKeyReleased(evt);
            }
        });
        getContentPane().add(txtKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 143, 76, 21));

        txtTelepon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 169, 246, 21));

        txtKontak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtKontak, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 195, 354, 21));

        txtHP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(txtHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 223, 180, 21));

        txtTOP.setText("1");
        txtTOP.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtTOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTOPActionPerformed(evt);
            }
        });
        txtTOP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTOPFocusLost(evt);
            }
        });
        getContentPane().add(txtTOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 250, 42, 21));

        cmdBatal.setText("Batal");
        cmdBatal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmdBatalMouseClicked(evt);
            }
        });
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });
        getContentPane().add(cmdBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(507, 259, 77, 30));

        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(cmdSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(426, 258, 77, 30));

        lblKota.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblKota.setForeground(new java.awt.Color(255, 255, 255));
        lblKota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        getContentPane().add(lblKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 144, 399, 19));

        jLabel11.setForeground(new java.awt.Color(204, 255, 255));
        jLabel11.setText("Mulai Nota");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 102, 20));

        txtNotaStart.setText("0");
        txtNotaStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNotaStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotaStartActionPerformed(evt);
            }
        });
        txtNotaStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNotaStartFocusLost(evt);
            }
        });
        getContentPane().add(txtNotaStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 42, 21));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, -1, 590, 310));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-599)/2, (screenSize.height-339)/2, 599, 339);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTOPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOPFocusLost
        txtTOP.setText(String.valueOf(udfGetInt(txtTOP.getText())));
    }//GEN-LAST:event_txtTOPFocusLost

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        udfSave();
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //lst = new DlgList(JOptionPane.getFrameForComponent(this), true);
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        try {
            stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs=stmt.executeQuery("select * from customer c left join kota on kode_kota=kota where  kode_cust='"+txtKode.getText()+"' ");
            
            if(rs.next()){
                txtKode.setText(rs.getString("kode_cust"));
                txtNama.setText(rs.getString("nama"));
                //txtMerk.setText(rs.getString("merk"));
                txtAlamat.setText(rs.getString("alamat_1"));
                txtKota.setText(rs.getString("kota"));
                lblKota.setText(rs.getString("nama_kota"));
                txtTelepon.setText(rs.getString("telp"));
                txtKontak.setText(rs.getString("contact_person"));
                txtHP.setText(rs.getString("hp"));
                txtTOP.setText(String.valueOf(rs.getInt("termin_pembayaran")));
                txtNotaStart.setText(String.valueOf(rs.getInt("nota_start")));
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        setFocus();
    }//GEN-LAST:event_formWindowOpened

    private void cmdBatalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdBatalMouseClicked
        dispose();
    }//GEN-LAST:event_cmdBatalMouseClicked

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        dispose();
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void txtKotaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaKeyReleased
        try {
            String sCari = txtKota.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKota.setText(obj[0].toString());
                            lblKota.setText(obj[1].toString());
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
                    txtKota.setText("");
                    lblKota.setText("");
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
                        String sQry="select kode_kota, coalesce(nama_kota,'') as nama_kota from kota " +
                                "where (kode_kota||coalesce(nama_kota,'') ) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+ this.txtKota.getX()+4,
                                this.getY()+this.txtKota.getY() + txtKota.getHeight()+30,
                                txtKota.getWidth()+lblKota.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKota);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKota});
                        lst.setColWidth(0, txtKota.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKota.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKota.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtKotaKeyReleased

    private void txtTOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTOPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTOPActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        lst.setVisible(false);
    }//GEN-LAST:event_formWindowClosed

private void txtNotaStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotaStartActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtNotaStartActionPerformed

private void txtNotaStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNotaStartFocusLost
    txtNotaStart.setText(String.valueOf(udfGetInt(txtNotaStart.getText())));
}//GEN-LAST:event_txtNotaStartFocusLost
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmCustomerEdit(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblKota;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtHP;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtKontak;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNotaStart;
    private javax.swing.JTextField txtTOP;
    private javax.swing.JTextField txtTelepon;
    // End of variables declaration//GEN-END:variables

    private void setFocus(){
        txtAlamat.addFocusListener(txtFoculListener);
        txtHP.addFocusListener(txtFoculListener);
        txtKode.addFocusListener(txtFoculListener);
        txtKontak.addFocusListener(txtFoculListener);
        txtKota.addFocusListener(txtFoculListener);
        //txtMerk.addFocusListener(txtFoculListener);
        txtNama.addFocusListener(txtFoculListener);
        txtTOP.addFocusListener(txtFoculListener);
        txtTelepon.addFocusListener(txtFoculListener);
        
   }
    
    
    
    private FocusListener txtFoculListener=new FocusListener() {
        public void focusGained(FocusEvent e) {
           Component c=(Component) e.getSource();
           c.setBackground(g1);
           //c.setSelectionStart(0);
           //c.setSelectionEnd(c.getText().length());
           
           //c.setForeground(fPutih);
           //c.setCaretColor(new java.awt.Color(255, 255, 255));
        }
        public void focusLost(FocusEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(g2);
            //c.setForeground(fHitam);
        }
   };
   
    void setConn(Connection conn) {
        this.conn=conn;
    }

    void setIsNew(boolean b) {
        isNew=b;
    }

    private boolean udfCekbeforeSave() {
        boolean b=true;
        
//        if(txtKode.getText().equalsIgnoreCase("")){
//            JOptionPane.showMessageDialog(this, "Silakan isi kode terlebih dulu!");
//            txtKode.requestFocus();
//            return false;
//        }
        if(txtNama.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi nama customer/ toko terlebih dulu!");
            txtNama.requestFocus();
            return false;
        }
        if(txtKota.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi kota terlebih dulu!");
            txtKota.requestFocus();
            return false;
        }
        return b;
    }

    void setKodeCust(String string) {
        txtKode.setText(string);
    }

    void setSrcTable(JTable tblCustomer) {
        srcTable=tblCustomer;
    }
    
    Color g1 = new Color(153,255,255);
    Color g2 = new Color(255,255,255); 
    
    Color fHitam = new Color(0,0,0);
    Color fPutih = new Color(255,255,255); 
    
    Color crtHitam =new java.awt.Color(0, 0, 0);
    Color crtPutih = new java.awt.Color(255, 255, 255); 
}
