/*
 * DlgBayarNota.java
 *
 * Created on May 7, 2008, 7:36 PM
 */

package tpjkeuangan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  oestadho
 */
public class DlgBayarNota extends javax.swing.JDialog {
    private DlgList lst;
    Connection conn;
    float fSisa=0;
    private String sCustomer;
    private String sNota;
    private DefaultTableModel srcModel;
    private String sUserName;
    private boolean bSelected=false;
    private boolean isNew;
    private boolean isKoreksi;
    private String sNoBayar;
    private boolean isExistBayarNota;
    private int iRowSelected;
    
    /** Creates new form DlgBayarNota */
    public DlgBayarNota(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    boolean isExistNota() {
        return isExistBayarNota;
    }

    void setIsKoreksi(boolean b) {
        isKoreksi=b;
    }

    void setIsNew(boolean b) {
        isNew=b;
    }

    void setKodeBayar(String toString) {
        sNoBayar=toString;
    }

    void setRowSelected(int iRow) {
        iRowSelected=iRow;
    }
    
    void setSisa(float f){
        fSisa=f;
    }
    
    public boolean isSelected() {
        return bSelected;
    }
    
    void setCustomer(String s){
        sCustomer=s;
    }
    
    void setNota(String s){
        sNota=s;
    }
    
    public void setConn(Connection con){
        conn=con;
    }

    void setSrcModel(DefaultTableModel myModel) {
        srcModel=myModel;
    }

    void setUserName(String sUser) {
        sUserName=sUser;
    }

    private boolean udfCekBeforeSave() {
        boolean bSt=true;
        if(!isKoreksi && fSisa< udfGetFloat(lblTotalBayar.getText())){
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran melebihi Sisa Tagihan pada Nota tersebut!");
            txtBayar.requestFocus();
            bSt=false;
            return bSt;
        }
        if(txtJenisBayar.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi alat/ jenis pembayaran terlebih dulu!");
            txtJenisBayar.requestFocus();
            bSt=false;
            return bSt;
        }
        if(udfGetFloat(lblTotalBayar.getText())==0){
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran masih nol!");
            txtBayar.requestFocus();
            bSt=false;
            return bSt;
        }
        return bSt;
    }
    
    private float udfGetFloat(String sNum){
    float hsl=0;
    if(!sNum.trim().equalsIgnoreCase("")){
        try{
            hsl=Float.valueOf(sNum.replace(",", ""));
        }catch(NumberFormatException ne){
            hsl=0;
        }catch(IllegalArgumentException i){
            hsl=0;
        }
    }
    return hsl;
  }
    
    private void udfProsesKoreksi(){
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
            udfSimpanTrx();
            
            conn.setAutoCommit(true);
            bSelected=true;
        }catch(SQLException se){
            try {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Proses simpan simpan data gagal!\n"+se.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(DlgBayarNota.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void udfLoadJenisPembayaran() {
        try{
            ResultSet rs=conn.createStatement().executeQuery("select * from nota_alat_pembayaran order by 2");
            while(rs.next()){
                
            }
        }catch(SQLException se){
            System.out.println(se.getMessage());
        }
    }
    
    private void udfSimpanTrx() {
        String sInput="select fn_nota_save_bayar(" +
                    "'"+new SimpleDateFormat("yyyy-MM-dd").format(jDateBayar.getDate()) +"', " +
                    "'"+sCustomer+"', '', '"+txtJenisBayar.getText()+"', " +
                    "'"+txtNoCek.getText()+"', '"+new SimpleDateFormat("yyyy-MM-dd").format(jDateCek.getDate()) +"'," +
                    "'"+txtMemo.getText()+"', "+udfGetFloat(lblTotalBayar.getText())+", " +
                    "'"+sNota+"', "+udfGetFloat(txtBayar.getText())+", " +
                    ""+udfGetFloat(txtKlem.getText())+", '"+sUserName+"')";

        System.out.println(sInput);
        try{
            Statement st1=conn.createStatement();
            ResultSet rs=st1.executeQuery(sInput);

            if(!isKoreksi){
                if(rs.next()){
                    srcModel.addRow(new Object[]{
                        srcModel.getRowCount()+1,
                        rs.getString(1),
                        new SimpleDateFormat("dd-MM-yyyy").format(jDateBayar.getDate()),
                        lblJenisBayar.getText(),
                        txtMemo.getText(),
                        udfGetFloat(txtBayar.getText()),
                        udfGetFloat(txtKlem.getText())                        
                    });
                }
                
            }else{
                srcModel.setValueAt(lblJenisBayar.getText(), iRowSelected, 2);
                srcModel.setValueAt(txtMemo.getText(), iRowSelected, 3);
                srcModel.setValueAt(txtBayar.getText(), iRowSelected, 4);
                srcModel.setValueAt(txtKlem.getText(), iRowSelected, 5);
                srcModel.setValueAt(lblTotalBayar.getText(), iRowSelected, 6);
            }
            JOptionPane.showMessageDialog(this, "Simpan data sukses");
            bSelected=true;
            this.dispose();
            
        }catch(SQLException se){
            System.err.println("Simpan data :"+ se.getMessage());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtJenisBayar = new javax.swing.JTextField();
        lblJenisBayar = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateCek = new org.jdesktop.swingx.JXDatePicker();
        jLabel16 = new javax.swing.JLabel();
        txtNoCek = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMemo = new javax.swing.JTextArea();
        btnSimpan = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtBayar = new javax.swing.JTextField();
        txtKlem = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDateBayar = new org.jdesktop.swingx.JXDatePicker();
        lblTotalBayar = new javax.swing.JLabel();
        lblNoNota = new javax.swing.JLabel();
        lblNoBayar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText("Memo");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 69, 110, -1));

        jLabel24.setText("Jenis Pembayaran");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, 100, -1));

        txtJenisBayar.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtJenisBayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtJenisBayar.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtJenisBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJenisBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJenisBayarKeyReleased(evt);
            }
        });
        jPanel1.add(txtJenisBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 73, 24));

        lblJenisBayar.setFont(new java.awt.Font("Dialog", 0, 12));
        lblJenisBayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(lblJenisBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 10, 380, 24));

        jLabel7.setText("Tgl. Cek");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(341, 42, 70, -1));
        jPanel1.add(jDateCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 39, 160, -1));

        jLabel16.setText("No. Cek");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 41, 100, -1));

        txtNoCek.setFont(new java.awt.Font("Dialog", 0, 12));
        txtNoCek.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtNoCek.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtNoCek.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoCekFocusLost(evt);
            }
        });
        txtNoCek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoCekKeyReleased(evt);
            }
        });
        jPanel1.add(txtNoCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 38, 180, 24));

        txtMemo.setColumns(20);
        txtMemo.setRows(5);
        jScrollPane2.setViewportView(txtMemo);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 64, 460, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 57, 600, 120));

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 278, 90, 30));

        btnCancel.setText("Batal");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(516, 278, 90, 30));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBayar.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtBayar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBayarFocusLost(evt);
            }
        });
        jPanel2.add(txtBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 10, 180, 24));

        txtKlem.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtKlem.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtKlem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKlemActionPerformed(evt);
            }
        });
        txtKlem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKlemFocusLost(evt);
            }
        });
        jPanel2.add(txtKlem, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 39, 180, 24));

        jLabel4.setText("Klem");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 41, 70, 20));

        jLabel5.setText("Bayar");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 70, 20));

        jLabel8.setText("Tgl. Bayar");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(359, 47, 70, -1));
        jPanel2.add(jDateBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(431, 44, 160, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 182, 600, 80));

        lblTotalBayar.setBackground(new java.awt.Color(0, 0, 153));
        lblTotalBayar.setFont(new java.awt.Font("Tahoma", 1, 36));
        lblTotalBayar.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalBayar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalBayar.setText("0");
        lblTotalBayar.setOpaque(true);
        getContentPane().add(lblTotalBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 263, 280, 40));

        lblNoNota.setText("No Nota tagihan");
        getContentPane().add(lblNoNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 39, 260, 20));

        lblNoBayar.setText("No. Bayar");
        getContentPane().add(lblNoBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 37, 140, 20));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-622)/2, (screenSize.height-354)/2, 622, 354);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNoCekFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoCekFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoCekFocusLost

    private void txtNoCekKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoCekKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoCekKeyReleased

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtKlemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKlemActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtKlemActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(udfCekBeforeSave()){
            if(isKoreksi)
                udfProsesKoreksi();
            else{
                udfSimpanTrx();
            }
        }
}//GEN-LAST:event_btnSimpanActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        lst = new DlgList(JOptionPane.getFrameForComponent(this), true);
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        lblTotalBayar.setText("0");
        
        jDateCek.setFormats("dd-MM-yyyy");
        jDateBayar.setFormats("dd-MM-yyyy");
        
        udfLoadJenisPembayaran();
        
        if(isKoreksi){
            try{
                String sQry="select d.no_bayar, no_nota, coalesce(p.alat_bayar,'') as alat_bayar, coalesce(alat_pembayaran,'') as alat_pembayaran, " +
                        "coalesce(check_no,'') as check_no, tgl_cek, coalesce(memo,'') as memo," +
                        "coalesce(jumlah,0) as jumlah, coalesce(klem,0) as klem," +
                        "p.tanggal " +
                        "from nota_pembayaran_detail d " +
                        "inner join nota_pembayaran p on p.no_bayar=d.no_bayar " +
                        "left join nota_alat_pembayaran ap on ap.kode=p.alat_bayar where d.no_bayar='"+sNoBayar+"' and " +
                        "no_nota='"+sNota+"'";
                
                System.out.println(sQry);
                ResultSet rs=conn.createStatement().executeQuery(sQry);
                if(rs.next()){
                    lblNoBayar.setText("Kode Bayar : "+rs.getString("no_bayar"));
                    lblNoNota.setText("Nota tagihan : "+rs.getString("no_nota"));
                    txtJenisBayar.setText(rs.getString("alat_bayar"));
                    lblJenisBayar.setText(rs.getString("alat_pembayaran"));
                    txtNoCek.setText(rs.getString("check_no"));
                    jDateCek.setDate(rs.getDate("tgl_cek"));
                    txtMemo.setText(rs.getString("memo"));
                    txtBayar.setText(new DecimalFormat("#,##0").format(rs.getFloat("jumlah")));
                    txtKlem.setText(new DecimalFormat("#,##0").format(rs.getFloat("klem")));
                    jDateBayar.setDate(rs.getDate("tanggal"));
                    isExistBayarNota=true;
                    
                    lblTotalBayar.setText(NumberFormat.getInstance().format(
                        udfGetFloat(txtBayar.getText())+udfGetFloat(txtKlem.getText())
                        ));
                    
                }else{
                    JOptionPane.showMessageDialog(this, "Tidak ditemukan nota & Kode pembayaran tersebut");
                    lblNoBayar.setText("");
                    lblNoNota.setText("");
                    txtJenisBayar.setText("");
                    lblJenisBayar.setText("");
                    txtNoCek.setText("");
                    txtMemo.setText("");
                    txtBayar.setText("0");
                    txtKlem.setText("0");
                    this.dispose();
                }
                
            }catch(SQLException se){
                JOptionPane.showMessageDialog(this, se.getMessage());
            }
        }else{
            //tampilkaNota();
        }
    }//GEN-LAST:event_formWindowOpened

    

    private void txtBayarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBayarFocusLost
        txtBayar.setText(NumberFormat.getInstance().format(udfGetFloat(txtBayar.getText())));
        
        lblTotalBayar.setText(NumberFormat.getInstance().format(
                udfGetFloat(txtBayar.getText())+udfGetFloat(txtKlem.getText())
                ));
        
    }//GEN-LAST:event_txtBayarFocusLost

    private void txtKlemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKlemFocusLost
        txtKlem.setText(NumberFormat.getInstance().format(udfGetFloat(txtKlem.getText())));
        
        lblTotalBayar.setText(NumberFormat.getInstance().format(
                udfGetFloat(txtBayar.getText())+udfGetFloat(txtKlem.getText())
                ));
        
    }//GEN-LAST:event_txtKlemFocusLost

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtJenisBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisBayarKeyReleased
        try {
            String sCari = txtJenisBayar.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtJenisBayar.setText(obj[0].toString());
                            lblJenisBayar.setText(obj[1].toString());
                            //lblAlamatToko.setText(obj[2].toString());
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
                        String sQry="select coalesce(kode, '') as kode, coalesce(alat_pembayaran,'') as Jenis_pembayaran " +
                                "from nota_alat_pembayaran where (coalesce(kode, '')||coalesce(alat_pembayaran,'')) iLike '%"+sCari+"%' order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+this.jPanel1.getX()+ this.txtJenisBayar.getX()+5,
                                this.getY()+this.jPanel1.getY()+this.txtJenisBayar.getY() + txtJenisBayar.getHeight()+30,
                                txtJenisBayar.getWidth()+lblJenisBayar.getWidth()+5,
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtJenisBayar);
                        lst.setLblDes(new javax.swing.JLabel[]{lblJenisBayar});
                        lst.setColWidth(0, txtJenisBayar.getWidth());
                        lst.setColWidth(1, lblJenisBayar.getWidth());
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtJenisBayar.requestFocus();
                        } else{
                            txtJenisBayar.setText("");
                            lblJenisBayar.setText("");
                            lst.setVisible(false);
                            txtJenisBayar.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtJenisBayarKeyReleased

    private void txtJenisBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisBayarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJenisBayarKeyPressed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgBayarNota dialog = new DlgBayarNota(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSimpan;
    private org.jdesktop.swingx.JXDatePicker jDateBayar;
    private org.jdesktop.swingx.JXDatePicker jDateCek;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblJenisBayar;
    private javax.swing.JLabel lblNoBayar;
    private javax.swing.JLabel lblNoNota;
    private javax.swing.JLabel lblTotalBayar;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtJenisBayar;
    private javax.swing.JTextField txtKlem;
    private javax.swing.JTextArea txtMemo;
    private javax.swing.JTextField txtNoCek;
    // End of variables declaration//GEN-END:variables

    

    
    
}
