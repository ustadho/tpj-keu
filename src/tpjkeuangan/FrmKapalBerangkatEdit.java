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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author  oestadho
 */
public class FrmKapalBerangkatEdit extends javax.swing.JDialog {
    private ListRsbm lst;
    private Connection conn;

    private boolean isNew;
    private String sOldContainer="";
    
    private ResultSet rs;
    private Statement stmt;
    private DefaultTableModel srcModel;
    private int rowPos;

    private JTable srcTable;
    private String sSerialKapal;
    private int iSerial;
    
    /** Creates new form FrmCustomerEdit */
    public FrmKapalBerangkatEdit(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        MyKeyListener kListener=new MyKeyListener();
        txtKapal.addKeyListener(kListener);
        jDateBerangkat.addKeyListener(kListener);
        txtKota.addKeyListener(kListener);
        txtKeterangan.addKeyListener(kListener);
        jDateBerangkat.setFormats("dd-MM-yyyy");
    }

    void setKapal(String string) {
        txtKapal.setText(string);
    }

    void setSerialKapal() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void setSerialKapal(int i) {
        iSerial=i;
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
        String sTgl=null;
        
        if(udfCekbeforeSave()){
            try {
                rs.close();
                rs=stmt.executeQuery("select * from nota_kapal_tujuan where serial_kode="+iSerial+"");
                
                if(rs.next()) {}
               
                if(isNew){
                    rs.moveToInsertRow();
                    
                }
                // Set values for the new row.
                
                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                
                System.out.println(sTgl);
                rs.updateString("kode_kapal", txtKapal.getText());
                rs.updateDate("tgl_berangkat", java.sql.Date.valueOf(df.format(jDateBerangkat.getDate())));
                rs.updateString("kota_tujuan", txtKota.getText());
                rs.updateString("keterangan", txtKeterangan.getText());
                rs.updateBoolean("active", chkActive.isSelected());
                
                // Insert the new row
                if(isNew)
                    rs.insertRow();
                else
                    rs.updateRow();
            
                if(isNew){
                    srcModel.addRow(new Object[]{
                        txtKapal.getText(),
                        lblKapal.getText(),
                        new SimpleDateFormat("dd/MM/yyyy").format(jDateBerangkat.getDate()),
                        txtKota.getText(),
                        lblKota.getText(),
                        txtKeterangan.getText(),
                        udfGetSerial()
                    });
                    srcTable.setRowSelectionInterval(srcModel.getRowCount()-1, srcModel.getRowCount()-1);
                    
                }else{
                    srcModel.setValueAt(txtKapal.getText(), rowPos, 0);
                    srcModel.setValueAt(lblKapal.getText(), rowPos, 1);
                    srcModel.setValueAt(new SimpleDateFormat("dd/MM/yyyy").format(jDateBerangkat.getDate()), rowPos, 2);
                    srcModel.setValueAt(txtKota.getText(), rowPos, 3);
                    srcModel.setValueAt(lblKota.getText(), rowPos, 4);
                    srcModel.setValueAt(txtKeterangan.getText(), rowPos, 5);
                    
                }
                
                dispose();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } 
            
        }
    }
    
    private int udfGetSerial(){
        int i=0;
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select serial_kode from nota_kapal_tujuan where kode_kapal='"+txtKapal.getText()+"' " +
                    "and kode_kota='"+txtKota.getText()+"' and " +
                    "to_char(tgl_berangkat, 'yyyy-MM-dd')='"+new SimpleDateFormat("yyyy-MM-dd").format(jDateBerangkat.getDate())+"' ");
            
            if (rs.next()){
                i=rs.getInt(1);
            }
            
            rs.close();
            st.close();
            
        }catch(SQLException se){
            System.err.println("Get Serial "+se.getMessage());
        }
        
        return i;
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
                
                 
                case KeyEvent.VK_ESCAPE: {
                    if (!lst.isVisible())
                        dispose();
                    else
                        lst.setVisible(false);
                    break;
                }
                
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

        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        jDateBerangkat = new org.jdesktop.swingx.JXDatePicker();
        cmdBatal = new javax.swing.JButton();
        cmdSimpan = new javax.swing.JButton();
        lblKapal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chkActive = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        lblKota = new javax.swing.JLabel();
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

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setForeground(new java.awt.Color(255, 255, 153));
        jLabel5.setText("Kapal");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 105, 53, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setForeground(new java.awt.Color(255, 255, 153));
        jLabel7.setText("Tgl. Berangkat");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 134, 90, -1));

        txtKapal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKapal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKapalKeyReleased(evt);
            }
        });
        getContentPane().add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 103, 76, 22));
        getContentPane().add(jDateBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 129, 140, -1));

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
        getContentPane().add(cmdBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 251, 77, 30));

        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(cmdSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 250, 77, 30));

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblKapal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        lblKapal.setOpaque(true);
        getContentPane().add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(176, 103, 399, 22));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setForeground(new java.awt.Color(255, 255, 153));
        jLabel8.setText("Keterangan");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 187, 90, -1));

        jLabel2.setBackground(new java.awt.Color(153, 204, 255));
        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 1, 24));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Kapal ~ Berangkat");
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 19, 590, -1));

        chkActive.setFont(new java.awt.Font("Tahoma", 1, 11));
        chkActive.setForeground(new java.awt.Color(255, 255, 255));
        chkActive.setSelected(true);
        chkActive.setText("Aktif");
        chkActive.setContentAreaFilled(false);
        getContentPane().add(chkActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 253, 90, -1));

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane1.setViewportView(txtKeterangan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 480, 50));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setForeground(new java.awt.Color(255, 255, 153));
        jLabel6.setText("Kota");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 161, 53, -1));

        txtKota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKotaKeyReleased(evt);
            }
        });
        getContentPane().add(txtKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 155, 76, 22));

        lblKota.setBackground(new java.awt.Color(255, 255, 255));
        lblKota.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblKota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        lblKota.setOpaque(true);
        getContentPane().add(lblKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(176, 155, 400, 22));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/background.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, -1, 594, 310));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-599)/2, (screenSize.height-344)/2, 599, 344);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        udfSave();
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        try {
            stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs=stmt.executeQuery("select n.kode_kapal, coalesce(nama_kapal,'') as nama_kapal, tgl_berangkat, " +
                    "coalesce(kota_tujuan,'') as kode_kota, coalesce(kota.nama_kota,'') as nama_kota, coalesce(keterangan,'') as keterangan, " +
                    "active " +
                    "from nota_kapal_tujuan n " +
                    "inner join kapal on kapal.kode_kapal=n.kode_kapal " +
                    "left join kota on kota.kode_kota=kota_tujuan " +
                    "where serial_kode="+iSerial+" ");
            
            if(rs.next()){
                txtKapal.setText(rs.getString("kode_kapal"));
                lblKapal.setText(rs.getString("nama_kapal"));
                jDateBerangkat.setDate(rs.getDate("tgl_berangkat"));
                txtKota.setText(rs.getString("kode_kota"));
                lblKota.setText(rs.getString("nama_kota"));
                txtKeterangan.setText(rs.getString("keterangan"));
                chkActive.setSelected(rs.getBoolean("active"));
            }else{
                
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
                        String sQry="select kode_kapal, coalesce(nama_kapal,'') as nama_kapal from kapal " +
                                "where (kode_kapal||coalesce(nama_kapal,'') ) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+ this.txtKapal.getX()+4,
                                this.getY()+this.txtKapal.getY() + txtKapal.getHeight()+30,
                                txtKapal.getWidth()+lblKapal.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKapal.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKapal.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKapalKeyReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        lst.setVisible(false);
        
    }//GEN-LAST:event_formWindowClosed

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
    
    private void setFocusBlockTxt(JTextField txt){
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
        
    }
    
    private void setFocusBlockTxtFmt(JFormattedTextField txt){
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
        
    }
    
    public static boolean validateDate( String dateStr, boolean allowPast, String formatStr){
         if (formatStr == null) return false; // or throw some kinda exception, possibly a InvalidArgumentException
            SimpleDateFormat df = new SimpleDateFormat(formatStr);
            Date testDate = null;
            try
            {
                    testDate = df.parse(dateStr);
            }
            catch (ParseException e)
            {
                    // invalid date format
                    return false;
            }
            if (!allowPast)
            {
                    // initialise the calendar to midnight to prevent 
                    // the current day from being rejected
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    if (cal.getTime().after(testDate)) return false;
            }
            // now test for legal values of parameters
            if (!df.format(testDate).equals(dateStr)) return false;
            return true;
    }

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
    private javax.swing.JCheckBox chkActive;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdSimpan;
    private org.jdesktop.swingx.JXDatePicker jDateBerangkat;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JLabel lblKota;
    private javax.swing.JTextField txtKapal;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtKota;
    // End of variables declaration//GEN-END:variables

    private void setFocus(){
        txtKapal.addFocusListener(txtFoculListener);
        txtKota.addFocusListener(txtFoculListener);
        txtKeterangan.addFocusListener(txtFoculListener);
        
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
        if(txtKapal.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi no container terlebih dulu!");
            txtKapal.requestFocus();
            return false;
        }
        if(txtKota.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan kota tujuan terlebih dulu!");
            txtKota.requestFocus();
            return false;
        }
        
        if(isNew){
        String sQry="select * from nota_kapal_tujuan where kode_kapal ='"+txtKapal.getText()+"' and " +
                    "coalesce(to_Char(tgl_berangkat, 'dd/MM/yyyy'), '')='"+new SimpleDateFormat("dd/MM/yyyy").format(jDateBerangkat.getDate())+"' " +
                    "and kota_tujuan='"+txtKota.getText()+"'";
        
        try{
            Statement sCek=conn.createStatement();
            ResultSet rCek=sCek.executeQuery(sQry);

            System.out.println(sQry);
            
            if (rCek.next()){
                JOptionPane.showMessageDialog(this, "Kapal '"+lblKapal.getText()+ "' tujuan '"+lblKota.getText()+"' dengan " +
                        "tanggal keberangkatan '"+ (new SimpleDateFormat("dd/MM/yyyy").format(jDateBerangkat.getDate()))+"' sudah dimasukkan");
                txtKapal.requestFocus();
                return false;
            }
            
            rCek.close();
            sCek.close();
            
            }catch(SQLException se){
                System.out.println(se.getMessage());
            }
        }
        return b;
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
