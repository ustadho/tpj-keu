/*
 * FrmNotaGabungBerikut.java
 *
 * Created on July 9, 2008, 9:04 PM
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author  oestadho
 */
public class FrmNotaGabungBerikut extends javax.swing.JDialog {
    Connection conn;
    private String sKodeCustomer;
    private String sTo;
    private ListRsbm lst;
    private String sQry="";
    private NumberFormat fmt=NumberFormat.getInstance();
    private NumberFormat dcFmt=new DecimalFormat("#,##0.00;(#,##0.00)");
    SimpleDateFormat fmtYMD=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtDMY=new SimpleDateFormat("dd-MM-yyyy");
    String sUserName;
    private DefaultListModel lstKodeKota=new DefaultListModel();
    private DefaultListModel lstKotaSingkatan=new DefaultListModel();

    private boolean isEditRow=false;
    private DefaultTableModel modelItem;
    private ResultSet rsH;
    private boolean isSave;
    private String sTglBerangkat;
    private DefaultTableModel srcModel;
    private String sOldKeterangan;
    Color g1 = new Color(239,234,240);//-->>(251,236,177);// Kuning         [251,251,235]
    Color g2 = new Color(239,234,240);//-->>(241,226,167);// Kuning         [247,247,218]


    Color w1 = new Color(255,255,255);// Putih
    Color w2 = new Color(250,250,250);// Putih Juga

    Color h1 = new Color(51,255,0);// Color(255,240,240);// Hijau menyala
    Color h2 = new Color(250,230,230);// Merah Muda
    
    /** Creates new form FrmNotaGabungBerikut */
    public FrmNotaGabungBerikut(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        txtKapal.addFocusListener(txtFoculListener);
        txtNoNota.addFocusListener(txtFoculListener);
        txtMerk.addFocusListener(txtFoculListener);
        txtItemTrx.addFocusListener(txtFoculListener);
        txtQty.addFocusListener(txtFoculListener);
        txtSatuan.addFocusListener(txtFoculListener);
        txtKetSatuan.addFocusListener(txtFoculListener);
        txtTarif.addFocusListener(txtFoculListener);
        txtSubTotal.addFocusListener(txtFoculListener);
    }

    
    void setConn(Connection con){
        conn=con;
    }

    private void udfLoadKota(){
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(   "select kode_kota, coalesce(nama_kota, '') as nama_kota, " +
                                            "coalesce(singkatan,'') as singkatan, to_char(current_date,'yyyy') as th " +
                                            "from kota order by nama_kota");

            lstKodeKota.removeAllElements();

            int idx=0;
            lstKodeKota.add(idx, "");
            lstKotaSingkatan.add(idx, "");

            cmbKota.addItem("<Semua Kota>");
            idx++;

            while(rs.next()){
                cmbKota.addItem(rs.getString("nama_kota"));
                lstKodeKota.add(idx, rs.getString("kode_kota"));
                lstKotaSingkatan.add(idx, rs.getString("singkatan"));
                idx++;

                cmbTahun.setSelectedItem(rs.getString("th"));
            }
            cmbKota.setSelectedItem(Utama.sNamaKota);

        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }


    private Double udfGetDouble(String sNum){
        Double hsl = 0.0;
        try {
            hsl = (fmt.parse(sNum)).doubleValue();
              
        } catch (ParseException ex) {
            hsl=0.0;
            System.out.println(ex.getMessage());
        }
        return hsl;
    }
    
    void setKepada(String s){
        txtKepada.setText(s);
    }

    void setKodeCustomer(String text) {
        sKodeCustomer=text;
    }

    void setSFlag(String string) {
        sTo=string;
    }

    void setSrcModel(DefaultTableModel model) {
        srcModel=model;
    }

    void setTglBerangkat(String text) {
        sTglBerangkat=text;
    }

    void setUserName(String s) {
        sUserName=s;
    }
    private FocusListener txtFoculListener=new FocusListener() {
        public void focusGained(FocusEvent e) {
           Component c=(Component) e.getSource();
           c.setBackground(h1);
           //c.setSelectionStart(0);
           //c.setSelectionEnd(c.getText().length());
           
           //c.setForeground(fPutih);
           //c.setCaretColor(new java.awt.Color(255, 255, 255));
        }
        public void focusLost(FocusEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(w1);
            //c.setForeground(fHitam);
        }
   };
    private float udfGetFloat(String sNum){
        float hsl = 0;
        try {
            hsl = (fmt.parse(sNum)).floatValue();
              
        } catch (ParseException ex) {
            hsl=0;
            System.out.println(ex.getMessage());
        }
        return hsl;
    }
    
    private void udfClearItem(){
        txtMerk.setText("");
        txtItemTrx.setText("");
        txtQty.setText("");
        txtSatuan.setText("");
        txtKetSatuan.setText("");
        txtTarif.setText("");
        txtSubTotal.setText("");
        
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            if( evt.getSource().getClass().getName().equalsIgnoreCase("JTable") ||
                evt.getSource().equals(btnAddItem))
                return;
            
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
                
                case KeyEvent.VK_F5: {  //New -- Add
                    udfSave();
                    break;
                }
                
                case KeyEvent.VK_ESCAPE: {
                    //dispose();
                    break;
                }
                //default ;
                
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
    }
    
    private void udfAddItemTrx(){
        if (txtItemTrx.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi item transaksi terlebih dulu!");
            txtItemTrx.requestFocus();
            return;
            
        }
        if (udfGetFloat(txtSubTotal.getText())==0){
            JOptionPane.showMessageDialog(this, "Silakan isi jumlah biaya terlebih dulu!");
            txtSubTotal.requestFocus();
            return;
        }
        
        if(!isEditRow){
            modelItem.addRow(new Object[]{
                txtMerk.getText(),
                txtItemTrx.getText(),
                udfGetFloat(txtQty.getText()),
                txtSatuan.getText(),
                txtKetSatuan.getText(),
                udfGetFloat(txtTarif.getText()),
                udfGetFloat(txtSubTotal.getText())
            });
            
        }else{
            modelItem.setValueAt(txtMerk.getText(), tblItem.getSelectedRow(), 0);
            modelItem.setValueAt(txtItemTrx.getText(), tblItem.getSelectedRow(), 1);
            modelItem.setValueAt((udfGetFloat(txtQty.getText())==0? "": udfGetFloat(txtQty.getText())) , tblItem.getSelectedRow(), 2);
            modelItem.setValueAt(txtSatuan.getText(), tblItem.getSelectedRow(), 3);
            modelItem.setValueAt(txtKetSatuan.getText(), tblItem.getSelectedRow(), 4);
            modelItem.setValueAt((udfGetFloat(txtTarif.getText())==0? "": udfGetFloat(txtTarif.getText()) ), tblItem.getSelectedRow(), 5);
            modelItem.setValueAt(udfGetFloat(txtSubTotal.getText()), tblItem.getSelectedRow(), 6);
            isEditRow=false;
        }
        
        udfClearItem();
        
        txtMerk.requestFocus();
    }
    
    private void udfLookupKeterangan() {
        DlgLookupKeteranganNota fLookup=new DlgLookupKeteranganNota(JOptionPane.getFrameForComponent(this), true);
        fLookup.setCon(conn);
        fLookup.setSrcText(txtKeterangan);
        fLookup.setVisible(true);
    }
    
    private String udfGetNewNota(){
        String sNew="";
        try{
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery("select fn_nota_get_new_kode_tagihan('"+sKodeCustomer+"', " +
                    "'"+fmtYMD.format(jDateNota.getDate())+"', '"+sTo+"', " +
                    "'"+lstKotaSingkatan.get(cmbKota.getSelectedIndex()).toString()+"', " +
                    "'"+cmbTahun.getSelectedItem().toString().substring(2, 4)+"')");

            if(rs.next()){
                sNew=rs.getString(1);
            }

            rs.close();
            stm.close();
        
        }catch(SQLException se){
            System.out.println(se.getMessage());
        }
        
        return sNew;
    }
    
    private void udfSimpanItemTrx(String sTag) throws SQLException {
        ResultSet rsH=null;
        int i=conn.createStatement().executeUpdate("Delete from nota_detail where no_nota='"+txtNoNota.getText()+"' ");
        
        rsH=conn.createStatement().executeQuery("select * from nota where no_nota='"+txtNoNota.getText()+"'");

//        if(!rsH.next()){
//            isEdit=false;
          rsH.moveToInsertRow();
//        }else{
//            isEdit=true;
//        }

        rsH.updateString("no_nota", txtNoNota.getText());
        rsH.updateDate("tgl_nota",java.sql.Date.valueOf(fmtYMD.format(jDateNota.getDate())));
        rsH.updateString("tagihan_per", sTag);
        rsH.updateString("customer", txtKepada.getText());
        //rsH.updateString("kode_kapal", txtKapal.getText());
        //rsH.updateDate("tgl_berangkat",  java.sql.Date.valueOf(fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText()))) );
        rsH.updateString("customer", sKodeCustomer);
        rsH.updateString("kepada", txtKepada.getText());
        rsH.updateString("keterangan_nota", txtKeterangan.getText());
        rsH.updateString("catatan", txtMemo.getText());
        rsH.updateString("user_ins", sUserName);
        rsH.updateString("kondisi", cmbKondisi.getSelectedItem().toString());
        rsH.updateInt("seri_kapal", Integer.parseInt(lblSerialKapal.getText()));

//        if(isEdit)
//            rsH.updateRow();
//        else
        rsH.insertRow();

        //stD=conn.createStatement();
//            rsD=stD.executeQuery("select * from nota_detail limit 0");
        String sInsert="Delete from nota_detail where no_nota='"+txtNoNota.getText()+"'; ";
        for (int s=0; s<modelItem.getRowCount(); s++){

            sInsert+="Insert into nota_detail(no_nota, merk, item_trx, satuan, ket_satuan, ukuran, tarif, sub_total) values " +
                    "('"+txtNoNota.getText()+"', '"+ modelItem.getValueAt(s, 0).toString()+"', " +
                    "'"+modelItem.getValueAt(s, 1).toString()+"', '"+modelItem.getValueAt(s, 3).toString()+"', " +
                    "'"+modelItem.getValueAt(s, 4).toString()+"', " +
                    (udfGetFloat(modelItem.getValueAt(s, 2).toString())!=0? udfGetFloat(modelItem.getValueAt(s, 2).toString()) :null)+", " +
                    (udfGetFloat(modelItem.getValueAt(s, 5).toString())!=0? udfGetFloat(modelItem.getValueAt(s, 5).toString()) :null)+", " +
                    udfGetFloat(modelItem.getValueAt(s, 6).toString())+"); " ;
        }

        conn.createStatement().executeUpdate(sInsert);
    }
    
    private void udfPrint() {
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("no_nota", txtNoNota.getText());
            
            System.out.println("No. Nota: "+txtNoNota.getText());
            System.out.println(getClass().getResource(""));
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/nota_cetak_gabungan_v4.jasper"));
                           
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
            
            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            JasperViewer.viewReport(print, false);
            
            
                            
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
    }
    
    private void udfSetKeterangan(){
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select keterangan from nota_keterangan where is_delete =false and is_default=true");

            if (rs.next()) {
                txtKeterangan.setText(rs.getString(1));
                sOldKeterangan=rs.getString(1);
            }else{
                txtKeterangan.setText("");
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNota.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void udfSave(){
        if(txtKepada.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi Kepada terlebih dulu!");
            txtKepada.requestFocus();
            return;
        }
        if(txtKapal.getText().trim().equalsIgnoreCase("")||udfGetFloat(lblSerialKapal.getText())==0){
            JOptionPane.showMessageDialog(this, "Silakan isi Kapal terlebih dulu!");
            txtKapal.requestFocus();
            return;
        }
        if(tblItem.getRowCount()==0){
            JOptionPane.showMessageDialog(this, "Item transaksi masih kosong.\n Silakan isi terlebih dulu!");
            txtMerk.requestFocus();
            return;
        }
        if(txtNoNota.getText().trim().equalsIgnoreCase("")){
            String sNota=udfGetNewNota();
            if(JOptionPane.showConfirmDialog(this, "Nomor nota masih kosong...!\n" +
                    "Apakah akan dibuatkan secara otomatis oleh sistem dengan nomor :'"+udfGetNewNota()+"' ?\n\n" +
                    "Pilih No jika anda ingin membuat secara manual.", "Konfimasi No. Nota", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                txtNoNota.setText(udfGetNewNota());
            }else{
                txtNoNota.requestFocus();
                return;
            }
        }
        
        boolean isEdit=false;
        String sTag=sTo;
//        if(radioToko.isSelected()){
//            sTag="T";
//        }else if(radioPengirim.isSelected()){
//            sTag="P";
//        }
        
        try {
            conn.setAutoCommit(false);
            int i=conn.createStatement().executeUpdate("Delete from nota_detail where no_nota='"+txtNoNota.getText()+"' ");
            udfSimpanItemTrx(sTag);
            
            rsH=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                    .executeQuery("select * from nota where no_nota='"+txtNoNota.getText()+"'");
            if(!rsH.next()){
                isEdit=false;
                rsH.moveToInsertRow();
            }else{
                isEdit=true;
            }
            
            rsH.updateString("no_nota", txtNoNota.getText());
            rsH.updateDate("tgl_nota",java.sql.Date.valueOf(fmtYMD.format(jDateNota.getDate())));
            rsH.updateString("tagihan_per", sTag);
            rsH.updateString("customer", txtKepada.getText());
            //rsH.updateString("kode_kapal", txtKapal.getText());
            //rsH.updateDate("tgl_berangkat",  java.sql.Date.valueOf(fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText()))) );
            rsH.updateString("customer", sKodeCustomer);
            rsH.updateString("kepada", txtKepada.getText());
            rsH.updateString("keterangan_nota", txtKeterangan.getText());
            rsH.updateString("catatan", txtMemo.getText());
            rsH.updateString("user_ins", sUserName);
            rsH.updateString("kondisi", cmbKondisi.getSelectedItem().toString());
            rsH.updateBoolean("is_header", true);
            
            if(isEdit)
                rsH.updateRow();
            else
                rsH.insertRow();
            
            String sUpdate="Update nota set nota_header=null where nota_header='"+txtNoNota.getText()+"'; ";
            
            conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
                    .executeUpdate(sUpdate);
            
            conn.setAutoCommit(true);
            udfAddNota(txtNoNota.getText());
            isSave=true;
            dispose();
//            if(JOptionPane.showConfirmDialog(this, "Simpan data sukses! Selanjutnya apakah akan dicetak?", "Print confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
//                udfPrint();
//            }
            
        } catch (SQLException ex) {
            try {
                JOptionPane.showMessageDialog(this, "Simpan data gagal!\n"+ex.getMessage());
                conn.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(FrmNotaGabungBerikut.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
        
    }
    
    private void udfAddNota(String sNota){
//        for(int i=0; i<srcModel.getRowCount(); i++){
//            if(!isKoreksi && modelNotaLalu.getValueAt(i, 6).toString().trim().equalsIgnoreCase(txtNoNotaDetail.getText())){
//                JOptionPane.showMessageDialog(this, "Anda sudah memasukkan No. Nota tersebut!", "Message", JOptionPane.OK_OPTION);
//                txtNoNotaDetail.requestFocus();
//                return;
//            }
//        }
        try{
            Statement sD=conn.createStatement();
            ResultSet rD=sD.executeQuery("select coalesce(merk,'') as merk, item_trx, ukuran, satuan, tarif, sub_total " +
                    "from nota_detail where no_nota='"+sNota+"' ");
            
            if(rD.next()){
                //Tambahkan dulu Nota Headernya
                srcModel.addRow(new Object[]{
                    "",
                    lblKapal.getText()+" , Berangkat tgl. "+lblTglBerangkat.getText(),
                    "",
                    "",
                    "",
                    "",
                    txtNoNota.getText()
                });
                
                //Tambahkan nota detailnya
                
                srcModel.addRow(new Object[]{
                    rD.getString("merk"),
                    rD.getString("item_trx"),
                    rD.getFloat("ukuran"),
                    rD.getString("satuan"),
                    rD.getFloat("tarif"),
                    rD.getFloat("sub_total"),
                    ""
                });
                
                while(rD.next()){
                    srcModel.addRow(new Object[]{
                        rD.getString("merk"),
                        rD.getString("item_trx"),
                        rD.getFloat("ukuran"),
                        rD.getString("satuan"),
                        rD.getFloat("tarif"),
                        rD.getFloat("sub_total"),
                        ""
                    });
                }
            }
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, "Ada kesalahan dalam menambahkan nota!\n"+se.getMessage());
            return;
        }
        
        //udfClearItem();
        
        //txtNoNota.requestFocus();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        lblKotaTujuan = new javax.swing.JLabel();
        lblKapal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblTglBerangkat = new javax.swing.JLabel();
        lblSerialKapal = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtKepada = new javax.swing.JTextField();
        btnGenerateNota = new javax.swing.JButton();
        txtNoNota = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLookupPackingList = new javax.swing.JButton();
        panelItem = new javax.swing.JPanel();
        txtMerk = new javax.swing.JTextField();
        txtItemTrx = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        txtSatuan = new javax.swing.JTextField();
        txtKetSatuan = new javax.swing.JTextField();
        txtTarif = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        btnAddItem = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblTotalNota = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jDateNota = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMemo = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbKondisi = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbKota = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota kapal berikutnya");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("KAPAL");
        jLabel14.setName("jLabel14"); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 60, -1));

        txtKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKapal.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtKapal.setName("txtKapal"); // NOI18N
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
        jPanel1.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 64, 24));

        lblKotaTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaTujuan.setName("lblKotaTujuan"); // NOI18N
        lblKotaTujuan.setOpaque(true);
        jPanel1.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 390, 24));

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setName("lblKapal"); // NOI18N
        lblKapal.setOpaque(true);
        jPanel1.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 320, 24));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Tgl. Berangkat");
        jLabel13.setName("jLabel13"); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 90, 20));

        lblTglBerangkat.setBackground(new java.awt.Color(255, 255, 255));
        lblTglBerangkat.setFont(new java.awt.Font("Dialog", 0, 12));
        lblTglBerangkat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTglBerangkat.setName("lblTglBerangkat"); // NOI18N
        lblTglBerangkat.setOpaque(true);
        jPanel1.add(lblTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 130, 24));

        lblSerialKapal.setText("jLabel3");
        lblSerialKapal.setName("lblSerialKapal"); // NOI18N
        jPanel1.add(lblSerialKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 110, 20));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("KEPADA");
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 60, 20));

        txtKepada.setEditable(false);
        txtKepada.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtKepada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKepada.setName("txtKepada"); // NOI18N
        txtKepada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKepadaKeyReleased(evt);
            }
        });
        jPanel1.add(txtKepada, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 750, 24));

        btnGenerateNota.setText("Generate No. Nota");
        btnGenerateNota.setName("btnGenerateNota"); // NOI18N
        btnGenerateNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGenerateNotaMouseClicked(evt);
            }
        });
        btnGenerateNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateNotaActionPerformed(evt);
            }
        });
        jPanel1.add(btnGenerateNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 70, 220, -1));

        txtNoNota.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNoNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNoNota.setName("txtNoNota"); // NOI18N
        txtNoNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoNotaKeyPressed(evt);
            }
        });
        jPanel1.add(txtNoNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 220, 24));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("NO. NOTA");
        jLabel4.setName("jLabel4"); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 90, 90, 20));

        btnLookupPackingList.setText("Packing list");
        btnLookupPackingList.setToolTipText("Lihat Packing List");
        btnLookupPackingList.setName("btnLookupPackingList"); // NOI18N
        btnLookupPackingList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLookupPackingListMouseClicked(evt);
            }
        });
        btnLookupPackingList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLookupPackingListActionPerformed(evt);
            }
        });
        jPanel1.add(btnLookupPackingList, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 120, 26));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 840, 130));

        panelItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelItem.setName("panelItem"); // NOI18N
        panelItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtMerk.setName("txtMerk"); // NOI18N
        txtMerk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMerkFocusLost(evt);
            }
        });
        txtMerk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMerkKeyReleased(evt);
            }
        });
        panelItem.add(txtMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 22));

        txtItemTrx.setName("txtItemTrx"); // NOI18N
        txtItemTrx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtItemTrxActionPerformed(evt);
            }
        });
        txtItemTrx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtItemTrxKeyReleased(evt);
            }
        });
        panelItem.add(txtItemTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 247, 22));

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.setName("txtQty"); // NOI18N
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        panelItem.add(txtQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 10, 53, 22));

        txtSatuan.setName("txtSatuan"); // NOI18N
        txtSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSatuanKeyReleased(evt);
            }
        });
        panelItem.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 80, 22));

        txtKetSatuan.setName("txtKetSatuan"); // NOI18N
        txtKetSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKetSatuanKeyReleased(evt);
            }
        });
        panelItem.add(txtKetSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 80, 22));

        txtTarif.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTarif.setName("txtTarif"); // NOI18N
        txtTarif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarifActionPerformed(evt);
            }
        });
        txtTarif.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTarifFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTarifFocusLost(evt);
            }
        });
        panelItem.add(txtTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 100, 22));

        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.setName("txtSubTotal"); // NOI18N
        txtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubTotalActionPerformed(evt);
            }
        });
        txtSubTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSubTotalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubTotalFocusLost(evt);
            }
        });
        txtSubTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubTotalKeyPressed(evt);
            }
        });
        panelItem.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 120, 22));

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        tblItem.setAutoCreateRowSorter(true);
        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Merk", "Item Transaksi", "Ukuran", "Satuan", "Ket. Satuan", "Tarif", "JUMLAH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItem.setName("tblItem"); // NOI18N
        tblItem.getTableHeader().setReorderingAllowed(false);
        tblItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblItemKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(tblItem);
        tblItem.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblItem.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblItem.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblItem.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblItem.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblItem.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblItem.getColumnModel().getColumn(6).setPreferredWidth(120);

        panelItem.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 780, 135));

        btnAddItem.setText("+");
        btnAddItem.setToolTipText("Tambahkan ke item transaksi");
        btnAddItem.setName("btnAddItem"); // NOI18N
        btnAddItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddItemMouseClicked(evt);
            }
        });
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });
        btnAddItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddItemKeyPressed(evt);
            }
        });
        panelItem.add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, -1, -1));

        jButton1.setText("Add Other Item");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelItem.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 130, -1));

        getContentPane().add(panelItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 840, 210));

        jLabel12.setBackground(new java.awt.Color(0, 0, 255));
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("TOTAL    ");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setOpaque(true);
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 350, -1, 20));

        lblTotalNota.setBackground(new java.awt.Color(0, 0, 255));
        lblTotalNota.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblTotalNota.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalNota.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalNota.setText("0");
        lblTotalNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalNota.setName("lblTotalNota"); // NOI18N
        lblTotalNota.setOpaque(true);
        getContentPane().add(lblTotalNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 350, 120, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("Ket: Lampiran Packing List");
        jLabel6.setName("jLabel6"); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, -1, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDateNota.setName("jDateNota"); // NOI18N
        jPanel3.add(jDateNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 5, 140, -1));

        jLabel1.setText("Tgl. Nota");
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 9, 100, -1));

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        txtMemo.setColumns(20);
        txtMemo.setRows(5);
        txtMemo.setName("txtMemo"); // NOI18N
        jScrollPane3.setViewportView(txtMemo);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 31, 300, 44));

        jLabel7.setText("Catatan");
        jLabel7.setName("jLabel7"); // NOI18N
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 33, 81, 20));

        jLabel9.setText("Kondisi");
        jLabel9.setName("jLabel9"); // NOI18N
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 80, 81, 20));

        cmbKondisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Port to Door", "Port to Port", "Door to Door" }));
        cmbKondisi.setName("cmbKondisi"); // NOI18N
        jPanel3.add(cmbKondisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 80, 220, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 380, -1, 110));

        jButton2.setText("Lookup Keterangan");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 180, -1));

        btnSave.setText("Simpan & Tambahkan");
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 510, 180, 30));

        btnClose.setText("Batal");
        btnClose.setName("btnClose"); // NOI18N
        btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCloseMouseClicked(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        btnClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCloseKeyPressed(evt);
            }
        });
        getContentPane().add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(757, 510, 90, 30));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        txtKeterangan.setName("txtKeterangan"); // NOI18N
        jScrollPane1.setViewportView(txtKeterangan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 420, 120));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tahun");
        jLabel10.setName("jLabel10"); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 90, 20));

        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2020" }));
        cmbTahun.setName("cmbTahun"); // NOI18N
        getContentPane().add(cmbTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 510, 70, 20));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Kota");
        jLabel3.setName("jLabel3"); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 510, 90, 20));

        cmbKota.setName("cmbKota"); // NOI18N
        cmbKota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbKotaKeyPressed(evt);
            }
        });
        getContentPane().add(cmbKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 510, 180, 20));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-866)/2, (screenSize.height-580)/2, 866, 580);
    }// </editor-fold>//GEN-END:initComponents

private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost

}//GEN-LAST:event_txtKapalFocusLost

private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed

}//GEN-LAST:event_txtKapalKeyPressed

private void txtKapalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyReleased
    try {
            switch(evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKapal.setText(obj[0].toString());
                            
                            lst.udfSetSelectedValue();
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
                    txtKapal.requestFocus();
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
                    try {
                        sQry = "select tj.kode_kapal, nama_kapal , to_Char(tgl_berangkat, 'dd-MM-yyyy') as tgl_berangkat, " + 
                                "coalesce(nama_kota,'') as kota_tujuan, serial_kode " + 
                                "from nota_kapal_tujuan tj " + 
                                "inner join kapal on kapal.kode_kapal=tj.kode_kapal " + 
                                "left join kota on kode_kota=kota_tujuan " + 
                                "where (tj.kode_kapal||nama_kapal) iLike '%" + txtKapal.getText() + "%' " + 
                                "and coalesce(tj.kota_tujuan,'') iLike '%" + Utama.sKodeKota + "%' " + 
                                (sTglBerangkat.equalsIgnoreCase("")?"": "and to_char(tj.tgl_berangkat, 'yyyy-MM-dd') >='" +  fmtYMD.format(fmtDMY.parse(sTglBerangkat)) + "' ") + 
                                "and active=true order by tj.kode_kapal limit 100";
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(FrmNotaGabungBerikut.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+this.jPanel1.getX()+ this.txtKapal.getX()+3,
                                this.getY() + this.jPanel1.getY()+this.txtKapal.getY() + txtKapal.getHeight()+30,
                                500, //txtKapal.getWidth()+jLabel13.getWidth()+lblTglBerangkat.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal, lblTglBerangkat, lblKotaTujuan, lblSerialKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblKapal.getWidth());
                        //lst.udfRemoveColumn(4);
                        
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

private void txtKepadaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKepadaKeyReleased
//        try {
        //            //String sCari = txtKepada.getText();
        //            switch(evt.getKeyCode()) {
        //
        //                case java.awt.event.KeyEvent.VK_ENTER : {
        //                    if (lst.isVisible()){
        //                        Object[] obj = lst.getOResult();
        //                        if (obj.length > 0) {
        //                            txtKepada.setText(obj[0].toString());
        //                            lst.setVisible(false);
        //                        }
        //                    }
        //                    break;
        //                }
        //                case java.awt.event.KeyEvent.VK_DELETE: {
        //                    lst.setFocusable(true);
        //                    lst.requestFocus();
        //
        //                    break;
        //                }
        //                case java.awt.event.KeyEvent.VK_ESCAPE: {
        //                    lst.setVisible(false);
        //                    txtKepada.setText("");
        //
        //                    break;
        //                }
        //
        //                case java.awt.event.KeyEvent.VK_DOWN: {
        //                    if (lst.isVisible()){
        //
        //                        lst.setFocusableWindowState(true);
        //                        lst.setVisible(true);
        //                        lst.requestFocus();
        //                    }
        //                    break;
        //                }
        //                default : {
        //                    if(!evt.getKeyText(evt.getKeyCode()).equalsIgnoreCase("Up") || !evt.getKeyText(evt.getKeyCode()).equalsIgnoreCase("F2")){
        //
        ////                        String sQry="select no_spnu from kontainer where active=true and coalesce(kode_kota,'') iLike '"+MainForm.sKodeKota+"%' " +
        ////                                "and no_spnu ilike upper('%"+sCari+"%') order by 1";
        ////
        //                        System.out.println(sQry);
        //                        lst.setSQuery(getQuery());
        //
        //                        lst.setBounds(this.getX()+jPanel1.getX()+ this.txtKepada.getX()+225,
        //                                this.getY()+jPanel1.getY()+this.txtKepada.getY() + txtKepada.getHeight()+145,
        //                                txtKepada.getWidth(),
        //                                150);
        //                        lst.setFocusableWindowState(false);
        //                        lst.setTxtCari(txtKepada);
        //                        lst.setLblDes(new javax.swing.JLabel[]{});
        //                        lst.setColWidth(0, txtKepada.getWidth());
        //
        //                        if(lst.getIRowCount()>0){
        //                            lst.setVisible(true);
        //                            requestFocusInWindow();
        //                            txtKepada.requestFocus();
        //                        } else{
        //                            lst.setVisible(false);
        //                            //txtKepada.setText("");
        //                            txtKepada.requestFocus();
        //                        }
        //                    }
        //                    break;
        //                }
        //            }
        //        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKepadaKeyReleased

private void txtMerkFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMerkFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtMerkFocusLost

private void txtMerkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMerkKeyReleased
    try {
            switch(evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtMerk.setText(obj[0].toString());
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
                    txtMerk.setText("");
                    txtMerk.requestFocus();
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
                        String sQry="select merk " +
                                "from merk kon " +
                                "where (merk) iLike '%"+txtMerk.getText()+"%' ";
                        
                        sQry+=(sTo.equalsIgnoreCase("T")? "and kode_cust='"+sKodeCustomer+"' ": "");
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+this.panelItem.getX()+ this.txtMerk.getX()+3,
                                this.getY() + this.panelItem.getY()+this.txtMerk.getY() + txtMerk.getHeight()+30,
                                txtMerk.getWidth()+20,
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtMerk);
                        lst.setLblDes(new javax.swing.JLabel[]{});
                        lst.setColWidth(0, txtMerk.getWidth());
                        
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtMerk.requestFocus();
                        } else{
                            lst.setVisible(false);
                            //txtMerk.setText("");
                            txtMerk.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtMerkKeyReleased

private void txtItemTrxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemTrxActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtItemTrxActionPerformed

private void txtItemTrxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemTrxKeyReleased
    if(txtItemTrx.getText().trim().equalsIgnoreCase("")){
            return;
        }
        
        try {
            //String sCari = txtKepada.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtItemTrx.setText(obj[0].toString());
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
                    txtItemTrx.setText("");
                    
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
                        
                        sQry="select distinct item_trx from nota_item_trx where item_trx iLike '%"+txtItemTrx.getText()+"%' order by 1 ";
                        
                        //
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+ panelItem.getX()+ this.txtItemTrx.getX()+3,
                                this.getY()+panelItem.getY()+this.txtItemTrx.getY() + txtItemTrx.getHeight()+30,
                                txtItemTrx.getWidth(),
                                100);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtItemTrx);
                        lst.setLblDes(new javax.swing.JLabel[]{});
                        lst.setColWidth(0, txtItemTrx.getWidth());
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtItemTrx.requestFocus();
                        } else{
                            lst.setVisible(false);
                            //txtKepada.setText("");
                            txtItemTrx.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtItemTrxKeyReleased

private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
if(lst.isVisible()){return;}
        txtQty.setText(fmt.format(udfGetFloat(txtQty.getText())));
        if(!txtQty.getText().equalsIgnoreCase("0")){
            txtSubTotal.setText(fmt.format(udfGetDouble(txtQty.getText()) * udfGetDouble(txtTarif.getText()) ) );
        }else{
            txtQty.setText("");
        }
}//GEN-LAST:event_txtQtyFocusLost

private void txtSatuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSatuanKeyReleased
    try {
            //String sCari = txtKepada.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtSatuan.setText(obj[0].toString());
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
                    txtSatuan.setText("");
                    
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
                        
                        String sQry="select distinct coalesce(satuan,'') as satuan " +
                                "from nota_satuan where coalesce(satuan,'') iLike '%"+txtSatuan.getText()+"%' order by 1 ";
                        
                        //
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+ panelItem.getX()+ this.txtSatuan.getX()+3,
                                this.getY()+panelItem.getY()+this.txtSatuan.getY() + txtSatuan.getHeight()+30,
                                txtSatuan.getWidth(),
                                100);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtSatuan);
                        lst.setLblDes(new javax.swing.JLabel[]{});
                        lst.setColWidth(0, txtSatuan.getWidth());
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtSatuan.requestFocus();
                        } else{
                            lst.setVisible(false);
                            //txtKepada.setText("");
                            txtSatuan.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtSatuanKeyReleased

private void txtKetSatuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKetSatuanKeyReleased
    try {
        switch(evt.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_ENTER : {
                if (lst.isVisible()){
                    Object[] obj = lst.getOResult();
                    if (obj.length > 0) {
                        txtKetSatuan.setText(obj[0].toString());
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
                txtKetSatuan.setText("");

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

                    String sQry="select coalesce(ket_satuan,'') as ket_satuan " +
                            "from nota_satuan where coalesce(ket_satuan,'') iLike '%"+txtKetSatuan.getText()+"%' " +
                            "and satuan ilike '"+txtSatuan.getText()+"%' order by 1 ";

                    //
                    System.out.println(sQry);
                    lst.setSQuery(sQry);

                    lst.setBounds(this.getX()+panelItem.getX()+ this.txtKetSatuan.getX()+3,
                            this.getY()+panelItem.getY()+this.txtKetSatuan.getY() + txtKetSatuan.getHeight()+30,
                            txtKetSatuan.getWidth(),
                            100);
                    lst.setFocusableWindowState(false);
                    lst.setTxtCari(txtKetSatuan);
                    lst.setLblDes(new javax.swing.JLabel[]{});
                    lst.setColWidth(0, txtKetSatuan.getWidth());

                    if(lst.getIRowCount()>0){
                        lst.setVisible(true);
                        requestFocusInWindow();
                        txtKetSatuan.requestFocus();
                    } else{
                        lst.setVisible(false);
                        //txtKepada.setText("");
                        txtKetSatuan.requestFocus();
                    }
                }
                break;
            }
        }
    } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKetSatuanKeyReleased

private void txtTarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarifActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtTarifActionPerformed

private void txtTarifFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarifFocusLost
txtTarif.setText(fmt.format(udfGetFloat(txtTarif.getText())));
        if(udfGetFloat(txtTarif.getText()) > 0){
            if(!txtQty.getText().trim().equalsIgnoreCase(""))
                txtSubTotal.setText(fmt.format(udfGetDouble(txtQty.getText()) * udfGetDouble(txtTarif.getText()) ) );
        }else
            txtTarif.setText("");
}//GEN-LAST:event_txtTarifFocusLost

private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtSubTotalActionPerformed

private void txtSubTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubTotalFocusLost
//    txtSubTotal.setText(fmt.format(udfGetFloat(txtSubTotal.getText())));
//        
//        if(udfGetDouble(txtSubTotal.getText())!=udfGetDouble(dcFmt.format(udfGetDouble(txtQty.getText())*udfGetDouble(txtTarif.getText()))) && 
//                !txtQty.getText().equalsIgnoreCase("")){
//            
//            txtQty.setText("");
//            txtTarif.setText("");
//        } 
    
    txtSubTotal.setText(fmt.format(udfGetDouble(txtSubTotal.getText())));
        if(udfGetDouble(txtSubTotal.getText())==udfGetDouble(fmt.format(udfGetDouble(txtQty.getText())*udfGetDouble(txtTarif.getText()))) ){
            txtQty.setText("");
            txtTarif.setText("");
        } else{
            return;
        }
}//GEN-LAST:event_txtSubTotalFocusLost

private void txtSubTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubTotalKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
        //            udfAddItem();
        //        }
}//GEN-LAST:event_txtSubTotalKeyPressed

private void tblItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemKeyPressed
    if(evt.getKeyCode()==KeyEvent.VK_F3){
            int iRow=tblItem.getSelectedRow();
            isEditRow=true;
            txtMerk.setText(modelItem.getValueAt(iRow, 0).toString());
            txtItemTrx.setText(modelItem.getValueAt(iRow, 1).toString());
            txtQty.setText(fmt.format((Float)modelItem.getValueAt(iRow, 2)));
            txtSatuan.setText(modelItem.getValueAt(iRow, 3).toString());
            txtKetSatuan.setText(modelItem.getValueAt(iRow, 4).toString());
            txtTarif.setText(fmt.format((Float)modelItem.getValueAt(iRow, 5)));
            txtSubTotal.setText(fmt.format((Float)modelItem.getValueAt(iRow, 6)));
            txtMerk.requestFocus();
            return;
        }if(evt.getKeyCode()==KeyEvent.VK_DELETE){
            if(tblItem.getSelectedRow()>=0){
                modelItem.removeRow(tblItem.getSelectedRow());
                return;
            }
        } 
        else{
            udfClearItem();
            isEditRow=false;
            
        }
}//GEN-LAST:event_tblItemKeyPressed

private void btnAddItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddItemMouseClicked
    udfAddItemTrx();
}//GEN-LAST:event_btnAddItemMouseClicked

private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnAddItemActionPerformed

private void btnAddItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddItemKeyPressed
    if(evt.getKeyCode()==KeyEvent.VK_ENTER ||evt.getKeyCode()==KeyEvent.VK_SPACE ){
        udfAddItemTrx();
    }
}//GEN-LAST:event_btnAddItemKeyPressed

private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    lst = new ListRsbm();
    lst.setVisible(false);
    lst.setSize(500, 200);
    lst.con = conn;
    
    modelItem=(DefaultTableModel)tblItem.getModel();
    modelItem.setNumRows(0);
    tblItem.setModel(modelItem);
    
    MyKeyListener kListener=new MyKeyListener();
    
    for(int i=0;i<jPanel1.getComponentCount();i++){
        Component c = jPanel1.getComponent(i);
        if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
        || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
        || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
        || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
            //System.out.println(c.getClass().getSimpleName());
            c.addKeyListener(kListener);
        }
    }
    for(int i=0;i<panelItem.getComponentCount();i++){
        Component c = panelItem.getComponent(i);
        if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
        || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
        || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
        || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
            //System.out.println(c.getClass().getSimpleName());
            c.addKeyListener(kListener);
        }
    }
    udfSetKeterangan();
    udfLoadKota();
    txtKapal.requestFocus();
}//GEN-LAST:event_formWindowOpened

private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
    udfLookupKeterangan();
}//GEN-LAST:event_jButton2MouseClicked

private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
this.dispose();
}//GEN-LAST:event_btnCloseMouseClicked

private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
this.dispose();
}//GEN-LAST:event_btnCloseActionPerformed

private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed
if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            this.dispose();
        }
}//GEN-LAST:event_btnCloseKeyPressed

private void btnGenerateNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerateNotaMouseClicked
    if(lstKotaSingkatan.get(cmbKota.getSelectedIndex()).toString().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan pilih Kota terlebih dulu!");
//            DlgGantiKota d1=new DlgGantiKota(JOptionPane.getFrameForComponent(this), true);
//            d1.setConn(conn);
//            d1.setVisible(true);
            cmbKota.requestFocus();
            return;
        }

    if(sKodeCustomer.trim().equalsIgnoreCase("")){
        JOptionPane.showMessageDialog(this, "Silakan isi Toko terlebih dulu!", "Nota message", JOptionPane.OK_OPTION);
        
        return;
    }else{
        txtNoNota.setText(udfGetNewNota());

    }
}//GEN-LAST:event_btnGenerateNotaMouseClicked

private void txtNoNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoNotaKeyPressed
//    if(evt.getKeyCode()==KeyEvent.VK_ENTER && isKoreksi){
//        udfLoadDetailKoreksi();
//    }
}//GEN-LAST:event_txtNoNotaKeyPressed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
//    if(isKoreksi)
//        udfProsesKoreksi();
//    else
        udfSave();
}//GEN-LAST:event_btnSaveActionPerformed

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    lst.setVisible(false);
}//GEN-LAST:event_formWindowClosed

private void btnLookupPackingListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLookupPackingListMouseClicked
    if(txtKapal.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan pilih kapal & tanggal berangkat terlebih dulu!");
            txtKapal.requestFocus();
            return;
        }
        if(sKodeCustomer.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Customer masih kosong!");
            return;
        }
        
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            
            String sTgl=lblTglBerangkat.getText().trim().equalsIgnoreCase("")? "": fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText()));
            reportParam.put("kapal", txtKapal.getText());
            reportParam.put("tgl_berangkat", sTgl);
            reportParam.put("customer", sKodeCustomer);
            reportParam.put("is_tujuan", (sTo.equalsIgnoreCase("T")? true: false) );
            
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_kapal_per_tgl_berangkat_per_cust_v2.jasper"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            if(print.getPages().isEmpty()){
                JOptionPane.showMessageDialog(this, "Dokumen tidak ditemukan untuk customer dan kapal yang dimaksud!");
                return;
            }
            JasperViewer.viewReport(print, false);
            
        } catch (ParseException ex) {
            Logger.getLogger(FrmNota.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
}//GEN-LAST:event_btnLookupPackingListMouseClicked

private void btnLookupPackingListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookupPackingListActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnLookupPackingListActionPerformed

private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
    lst.setVisible(false);
}//GEN-LAST:event_txtQtyFocusGained

private void txtTarifFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarifFocusGained
// TODO add your handling code here:
    lst.setVisible(false);
}//GEN-LAST:event_txtTarifFocusGained

private void txtSubTotalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubTotalFocusGained
    lst.setVisible(false);
}//GEN-LAST:event_txtSubTotalFocusGained

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    udfAddItemOther();
}//GEN-LAST:event_jButton1ActionPerformed

private void btnGenerateNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateNotaActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_btnGenerateNotaActionPerformed

private void cmbKotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbKotaKeyPressed

}//GEN-LAST:event_cmbKotaKeyPressed

private void udfAddItemOther() {
        FrmNotaAddItem f1=new FrmNotaAddItem(JOptionPane.getFrameForComponent(this), false);
        f1.setConn(conn);
        f1.setTitle("Add Other Item ["+sTo+"]");
        f1.setCustomer(sKodeCustomer);
        f1.setIsToko(sTo.equalsIgnoreCase("T")? true:false);
        f1.setSrcModel(modelItem);
        f1.setIsEditRow(false);
        f1.setRowPos(0);
        f1.setVisible(true);
        
    }


    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmNotaGabungBerikut dialog = new FrmNotaGabungBerikut(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGenerateNota;
    private javax.swing.JButton btnLookupPackingList;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbKondisi;
    private javax.swing.JComboBox cmbKota;
    private javax.swing.JComboBox cmbTahun;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private org.jdesktop.swingx.JXDatePicker jDateNota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JLabel lblKotaTujuan;
    private javax.swing.JLabel lblSerialKapal;
    private javax.swing.JLabel lblTglBerangkat;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JPanel panelItem;
    private javax.swing.JTable tblItem;
    private javax.swing.JTextField txtItemTrx;
    private javax.swing.JTextField txtKapal;
    private javax.swing.JTextField txtKepada;
    private javax.swing.JTextField txtKetSatuan;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextArea txtMemo;
    private javax.swing.JTextField txtMerk;
    private javax.swing.JTextField txtNoNota;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTarif;
    // End of variables declaration//GEN-END:variables

}
