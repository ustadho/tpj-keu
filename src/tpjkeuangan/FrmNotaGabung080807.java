/*
 * FrmNota.java
 *
 * Created on April 15, 2008, 8:07 AM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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
public class FrmNotaGabung080807 extends javax.swing.JInternalFrame {
    private Connection conn;
    DefaultTableModel modelItem, modelNotaLalu, modelNotaBerikut;
    private NumberFormat fmt=NumberFormat.getInstance();
    private NumberFormat dFmt=new DecimalFormat("#,##0");
    private ListRsbm lst;
    private boolean isEditRow=false;
    ResultSet rsH, rsD;
    JLabel lblKepada ;
    String sUserName;
    SimpleDateFormat fmtYMD=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtDMY=new SimpleDateFormat("dd-MM-yyyy");
    String sOldKeterangan="";
    
    String sQry;
    private boolean isSave=false;
    private String srcKapal="";
    private boolean isKoreksi;
    private String sOldNota;
    private String sTglBerangkat="";
    
    
    /** Creates new form FrmNota */
    public FrmNotaGabung080807() {
        initComponents();
        
        txtToko.addFocusListener(txtFoculListener);
        txtNoNotaDetail.addFocusListener(txtFoculListener);
        txtNoNota.addFocusListener(txtFoculListener);
        txtKepada.addFocusListener(txtFoculListener);
        txtKapal.addFocusListener(txtFoculListener);
        
        txtMerk.addFocusListener(txtFoculListener);
        txtItemTrx.addFocusListener(txtFoculListener);
        txtQty.addFocusListener(txtFoculListener);
        txtSatuan.addFocusListener(txtFoculListener);
        txtKetSatuan.addFocusListener(txtFoculListener);
        txtTarif.addFocusListener(txtFoculListener);
        txtKapal.addFocusListener(txtFoculListener);
        txtSubTotal.addFocusListener(txtFoculListener);
        
        modelItem=(DefaultTableModel)tblItem.getModel();
        modelItem.setNumRows(0);
        tblItem.setModel(modelItem);
        
    }

    void setConn(Connection cn) {
        conn=cn;
    }

    void setIsKoreksi(boolean b) {
        this.isKoreksi=b;
        btnGenerateNota.setVisible(!b);
    }

    void setKapal(String text) {
        srcKapal=text;
    }
    
    void setUserName(String s){
        sUserName=s;
    }

    void udfClearAll() {
        udfClear();
        txtNoNotaDetail.setText(""); lblKapalBerangkat.setText("");
        lblJmlNota.setText("");
    }
    
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

    private void udfLoadDetailKoreksi() {
        String sH="select tgl_nota, coalesce(catatan,'') as catatan, " +
                "case when upper(kondisi)='PORT TO DOOR' then 0 when upper(kondisi)='PORT TO PORT' then 1 else 2 end as kondisi, " +
                "ktj.kode_kapal, coalesce(nama_kapal,'') as nama_kapal, coalesce(to_Char(ktj.tgl_berangkat,'dd-MM-yyyy'),'') as tgl_berangkat," +
                "coalesce(nama_kota,'') as kota_tujuan, seri_kapal, tagihan_per, customer, " +
                "case when tagihan_per='T' then coalesce(c.nama,'') else coalesce(toko.nama,'') end as toko, nota.kepada, " +
                "coalesce(nota_header,'') as nota_header, coalesce(keterangan_nota,'') as ket_nota " +
                "from nota " +
                "inner join nota_kapal_tujuan ktj on ktj.serial_kode=seri_kapal " +
                "inner join  kapal on kapal.kode_kapal=ktj.kode_kapal " +
                "left join kota on kota.kode_kota=ktj.kota_tujuan " +
                "left join customer c on c.kode_cust=nota.customer " +
                "left join toko on toko.kode_toko=nota.customer " +
                "where is_header=true and is_batal=false " +
                "and no_nota='"+txtNoNota.getText()+"'"; //and coalesce(nota_header,'')='' 
        
        System.out.println(sH);
        sOldNota=txtNoNota.getText();
        try {
            Statement st = conn.createStatement();
            ResultSet rsH = st.executeQuery(sH);
            
            if(rsH.next()){
                jDateNota.setDate(rsH.getDate("tgl_nota"));
                txtMemo.setText(rsH.getString("catatan"));
                cmbKondisi.setSelectedIndex(rsH.getInt("kondisi"));
                txtKapal.setText(rsH.getString("kode_kapal"));
                lblKapal.setText(rsH.getString("nama_kapal"));
                lblTglBerangkat.setText(rsH.getString("tgl_berangkat"));
                lblKotaTujuan.setText(rsH.getString("kota_tujuan"));
                lblSerialKapal.setText(rsH.getString("seri_kapal"));
                if(rsH.getString("tagihan_per").equalsIgnoreCase("T"))
                    radioToko.setSelected(true);
                else
                    radioPengirim.setSelected(true);
                txtToko.setText(rsH.getString("customer"));
                lblToko.setText(rsH.getString("toko"));
                txtKepada.setText(rsH.getString("kepada"));
                txtKeterangan.setText(rsH.getString("ket_nota"));
                
                Statement std=conn.createStatement();
                ResultSet rsd=std.executeQuery("select coalesce(merk,'') as merk, item_trx, ukuran, satuan, ket_satuan, tarif, sub_total " +
                        "from nota_detail where no_nota='"+txtNoNota.getText()+"'");
                
                modelItem.setNumRows(0);
                
                while(rsd.next()){
                    modelItem.addRow(new Object[]{
                        rsd.getString("merk"),
                        rsd.getString("item_trx"),
                        rsd.getFloat("ukuran") ,//(rsd.getInt("ukuran")<=0? null: fmt.format(rsd.getFloat("ukuran")) ),
                        rsd.getString("satuan"),
                        rsd.getString("ket_satuan"),
                        rsd.getFloat("tarif") ,//(rsd.getInt("tarif")<=0? null: fmt.format(rsd.getFloat("tarif")) ),
                        rsd.getFloat("sub_total")
                    });
                }
                
                rsd.close();
                std.close();
                
                //String sDet="select no_nota from nota where nota_header='"+txtNoNota.getText()+"'";
                
                 String sDet="select nota.no_nota, coalesce(nama_kapal||', Brkt Tgl '||fn_tanggal_ind(ktj.tgl_berangkat),'') as kapal_berangkat, " +
                            "sum(coalesce(sub_total,0)) as sub_total, to_char(ktj.tgl_berangkat, 'yyyy-MM-dd') as tgl_berangkat " +
                            "from nota " +
                            "inner join nota_detail d on nota.no_nota=d.no_nota " +
                            "left join nota_kapal_tujuan ktj on seri_kapal=serial_kode " +
                            "left join kapal on kapal.kode_kapal=ktj.kode_kapal " +
                            "where nota_header='"+sOldNota+"' " +
                            "group by nota.no_nota, coalesce(nama_kapal||', Brkt Tgl '||fn_tanggal_ind(ktj.tgl_berangkat),''), to_char(ktj.tgl_berangkat, 'yyyy-MM-dd') order by nota.no_nota";
                 
                modelNotaLalu.setNumRows(0);
                modelNotaBerikut.setNumRows(0);
                
                ResultSet rsDet=conn.createStatement().executeQuery(sDet);
                while(rsDet.next()){
                    txtNoNotaDetail.setText(rsDet.getString("no_nota"));
                    lblKapalBerangkat.setText(rsDet.getString("kapal_berangkat"));
                    lblJmlNota.setText(new DecimalFormat("#,##0").format(rsDet.getFloat("sub_total")));
                    sTglBerangkat=rsDet.getString("tgl_berangkat");
                    udfAddNotaKoreksi(rsDet.getString(1));
                }
                    
                rsDet.close();
                
            }else{
                JOptionPane.showMessageDialog(this, "Nota tersebut tidak ditemukan, mungkin karena :\n" +
                        "   1. Nota yang anda inputkan salah.\n" +
                        "   2. Nota tersebut bukan nota gabungan.\n" +
                        "   3. Nota telah dibatalkan.\n" +
                        "   4. Nota telah lunas.\n" +
                        "Silakan periksa lagi..! ", "Message", JOptionPane.OK_OPTION);

//                udfClear();
//                txtKapal.setText(""); lblKapal.setText("");
//                lblKotaTujuan.setText("");
//                lblTglBerangkat.setText("");
                
                txtNoNota.requestFocus();
            }
            
            st.close();
            rsH.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNota.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void udfLookupKeterangan() {
        DlgLookupKeteranganNota fLookup=new DlgLookupKeteranganNota(JOptionPane.getFrameForComponent(this), true);
        fLookup.setCon(conn);
        fLookup.setSrcText(txtKeterangan);
        fLookup.setVisible(true);
    }

    private void udfPrint() {
        try {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("no_nota", txtNoNota.getText());
            
            System.out.println("No. Nota: "+txtNoNota.getText());
            System.out.println(getClass().getResource(""));
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/nota_cetak_gabungan_v2.jasper"));
                           
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
            
            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            
            if(print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nota tidak ditemukan!");
                return;
            }
            JasperViewer.viewReport(print, false);
                            
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
    }

    private void udfProsesKoreksi(){
        if(txtKapal.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi kapal terlebih dulu!");
            txtKapal.requestFocus();
            return;
        }
        if(txtKepada.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi Kepada terlebih dulu!");
            txtKepada.requestFocus();
            return;
        }
        if(txtNoNota.getText().trim().equalsIgnoreCase("")){
            txtNoNota.setText(udfGetNewNota());
        }
        
        if(modelItem.getRowCount()==0){
            JOptionPane.showMessageDialog(this, "Item transaksi masih kosong!");
            txtMerk.requestFocus();
            return;
        }
        
        boolean isEdit=false;
        String sTag="";
        if(radioToko.isSelected()){
            sTag="T";
        }else if(radioPengirim.isSelected()){
            sTag="P";
        }
        Statement stDel, stH, stD;
        
        String sKoreksi="insert into nota_history(no_nota, tgl_nota, tagihan_per, customer, kepada, keterangan_nota, date_ins, user_ins, " +
                "date_upd, user_upd, is_batal, id, catatan, is_lunas, tgl_lunas, seri_kapal, kondisi, nota_header, is_header) " +
                "select no_nota, tgl_nota, tagihan_per, customer, kepada, keterangan_nota, date_ins, user_ins, " +
                "now(), '"+sUserName+"', is_batal, id, catatan, is_lunas, tgl_lunas, seri_kapal, kondisi, nota_header, is_header " +
                "from nota where no_nota='"+sOldNota+"' ;";
        
        sKoreksi+="insert into nota_history_detail(no_nota, merk, item_trx, ukuran, satuan, ket_satuan, tarif, sub_total) " +
                "select d.no_nota, merk, item_trx, ukuran, satuan, ket_satuan, tarif, sub_total from nota_detail d " +
                "inner join nota n using(no_nota) " +
                "where d.no_nota='"+sOldNota+"' or nota_header='"+sOldNota+"';";
        
        try {
            conn.setAutoCommit(false);
            
            stDel=conn.createStatement();
            int i=stDel.executeUpdate(  sKoreksi+
                                        "Delete from nota_detail where no_nota='"+sOldNota+"'; ");
            
            stH = conn.createStatement();
            rsH=stH.executeQuery("select * from nota where no_nota='"+sOldNota+"'");
            
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
            rsH.updateString("customer", txtToko.getText());
            rsH.updateString("kepada", txtKepada.getText());
            rsH.updateString("keterangan_nota", txtKeterangan.getText());
            rsH.updateString("catatan", txtMemo.getText());
            rsH.updateString("user_ins", sUserName);
            rsH.updateString("kondisi", cmbKondisi.getSelectedItem().toString());
            rsH.updateInt("seri_kapal", Integer.parseInt(lblSerialKapal.getText()));
            
            if(isEdit)
                rsH.updateRow();
            else
                rsH.insertRow();
            
            stD=conn.createStatement();
//            rsD=stD.executeQuery("select * from nota_detail limit 0");
            String sInsert="Delete from nota_detail where no_nota='"+txtNoNota.getText()+"'; ";
            for (int s=0; s<modelItem.getRowCount(); s++){
                
                sInsert+="Insert into nota_detail(no_nota, merk, item_trx, satuan, ket_satuan, ukuran, tarif, sub_total) values " +
                        "('"+txtNoNota.getText()+"', '"+ modelItem.getValueAt(s, 0).toString()+"', " +
                        "'"+modelItem.getValueAt(s, 1).toString()+"', '"+modelItem.getValueAt(s, 3).toString()+"', " +
                        "'"+modelItem.getValueAt(s, 4).toString()+"', " +
                        (udfGetFloat(modelItem.getValueAt(s, 2).toString())>0? udfGetFloat(modelItem.getValueAt(s, 2).toString()) :null)+", " +
                        (udfGetFloat(modelItem.getValueAt(s, 5).toString())>0? udfGetFloat(modelItem.getValueAt(s, 5).toString()) :null)+", " +
                        udfGetFloat(modelItem.getValueAt(s, 6).toString())+"); " ;
            }
            
            stD.executeUpdate(sInsert);
            
            String sUpdate="Update nota set nota_header=null where nota_header='"+txtNoNota.getText()+"'; ";
            for (int s=0; s<modelNotaLalu.getRowCount(); s++){
                if(!modelNotaLalu.getValueAt(s, 6).toString().equalsIgnoreCase(""))
                    sUpdate+="Update nota set nota_header='"+txtNoNota.getText()+"' where no_nota='"+modelNotaLalu.getValueAt(s, 6).toString()+"'; ";
            }
            for (int s=0; s<modelNotaBerikut.getRowCount(); s++){
                if(!modelNotaBerikut.getValueAt(s, 6).toString().equalsIgnoreCase(""))
                    sUpdate+="Update nota set nota_header='"+txtNoNota.getText()+"' where no_nota='"+modelNotaBerikut.getValueAt(s, 6).toString()+"'; ";
            }
            conn.createStatement().executeUpdate(sUpdate);
            
            
            conn.setAutoCommit(true);
            isSave=true;
            
            if(JOptionPane.showConfirmDialog(this, "Proses koreksi nota sukses! Selanjutnya apakah akan dicetak?", "Print confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                udfPrint();
            }
            
        } catch (SQLException ex) {
            try {
                JOptionPane.showMessageDialog(this, "Simpan data gagal!\n"+ex.getMessage());
                conn.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(FrmNota.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
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
            Logger.getLogger(FrmNotaGabung080807.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        rsH.updateString("customer", txtToko.getText());
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
                    (udfGetFloat(modelItem.getValueAt(s, 2).toString())>0? udfGetFloat(modelItem.getValueAt(s, 2).toString()) :null)+", " +
                    (udfGetFloat(modelItem.getValueAt(s, 5).toString())>0? udfGetFloat(modelItem.getValueAt(s, 5).toString()) :null)+", " +
                    udfGetFloat(modelItem.getValueAt(s, 6).toString())+"); " ;
        }

        conn.createStatement().executeUpdate(sInsert);
    }
    
    public class MyRowRendererItem extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color g;
            Color w;
            Color h;
            
            if(column%2==0){
                g = g1;
                w = w1;
                h = h1;
            }else{
                g = g2;
                w = w2;
                h = h2;
            }
            
            if(value instanceof Float){
                setHorizontalAlignment(txt.RIGHT);
                value=fmt.format(value);
            }else{
                setHorizontalAlignment(txt.LEFT);
            }
            
            setForeground(new Color(0,0,0));
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            if(isSelected){
                //setBackground(new Color(51,102,255));
                setBackground(new Color(248,255,167));
                //setForeground(new Color(255,255,255));
            }
            
            setValue(value);
            return this;
        }
    }
    
    public class MyRowRendererNota extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color g;
            Color w;
            Color h;
            
            if(column%2==0){
                g = g1;
                w = w1;
                h = h1;
            }else{
                g = g2;
                w = w2;
                h = h2;
            }
            
            if(value instanceof Float){
                setHorizontalAlignment(txt.RIGHT);
                value=fmt.format(value);
            }else{
                setHorizontalAlignment(txt.LEFT);
            }
            
            setForeground(new Color(0,0,0));
            
            
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            if(!table.getValueAt(row, 6).toString().equalsIgnoreCase("")){
                setFont(new Font("Arial", Font.BOLD, 11));
                setBackground(new Color(248,255,167));
                
            }else{
                setFont(new Font("Arial", Font.PLAIN, 11));
            }
            
            if(isSelected && !table.getValueAt(row, 6).toString().equalsIgnoreCase("")){
                //setBackground(new Color(51,102,255));
                setBackground(new Color(51,102,255));
            }
            setValue(value);
            return this;
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
        btnSave = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnPreview = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelItem = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        txtItemTrx = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        txtSatuan = new javax.swing.JTextField();
        addItem = new javax.swing.JButton();
        txtSubTotal = new javax.swing.JTextField();
        txtTarif = new javax.swing.JTextField();
        txtKetSatuan = new javax.swing.JTextField();
        txtMerk = new javax.swing.JTextField();
        panelNotaLalu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNotaLalu = new javax.swing.JTable();
        btnAddNota = new javax.swing.JButton();
        txtNoNotaDetail = new javax.swing.JTextField();
        lblKapalBerangkat = new javax.swing.JLabel();
        lblJmlNota = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        panelNotaBerikut = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblNotaBerikut = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblTotalNota = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        radioPengirim = new javax.swing.JRadioButton();
        radioToko = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNoNota = new javax.swing.JTextField();
        txtKepada = new javax.swing.JTextField();
        txtToko = new javax.swing.JTextField();
        lblToko = new javax.swing.JLabel();
        btnGenerateNota = new javax.swing.JButton();
        txtKapal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lblKapal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblKotaTujuan = new javax.swing.JLabel();
        lblSerialKapal = new javax.swing.JLabel();
        lblTglBerangkat = new javax.swing.JLabel();
        btnLookupPackingList = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.inactiveTitleForeground"));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Nota Gabungan");
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

        btnSave.setText("Simpan");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClose.setText("Batal");
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

        btnClear.setText("Clear");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        btnClear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnClearKeyPressed(evt);
            }
        });

        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane2.setViewportView(txtKeterangan);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("Ket: Lampiran Packing List");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jDateNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 5, 140, -1));

        jLabel1.setText("Tgl. Nota");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 9, 100, -1));

        txtMemo.setColumns(20);
        txtMemo.setRows(5);
        jScrollPane3.setViewportView(txtMemo);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 31, 300, 44));

        jLabel7.setText("Catatan");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 33, 81, 20));

        jLabel9.setText("Kondisi");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 80, 81, 20));

        cmbKondisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Port to Door", "Port to Port", "Door to Door" }));
        jPanel3.add(cmbKondisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 80, 220, -1));

        jButton2.setText("Lookup Keterangan");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        panelItem.setBackground(new java.awt.Color(204, 204, 204));
        panelItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelItem.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        panelItem.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 29, 780, 160));

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
        panelItem.add(txtItemTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 6, 247, 22));

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        panelItem.add(txtQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(349, 6, 60, 22));

        txtSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSatuanKeyReleased(evt);
            }
        });
        panelItem.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(409, 6, 80, 22));

        addItem.setText("+");
        addItem.setToolTipText("Tambahkan ke item transaksi");
        addItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addItemMouseClicked(evt);
            }
        });
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });
        addItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addItemKeyPressed(evt);
            }
        });
        panelItem.add(addItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(783, 4, -1, -1));

        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubTotalActionPerformed(evt);
            }
        });
        txtSubTotal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubTotalFocusLost(evt);
            }
        });
        txtSubTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubTotalKeyPressed(evt);
            }
        });
        panelItem.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(663, 6, 120, 22));

        txtTarif.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTarif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarifActionPerformed(evt);
            }
        });
        txtTarif.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTarifFocusLost(evt);
            }
        });
        panelItem.add(txtTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(569, 6, 94, 22));

        txtKetSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKetSatuanKeyReleased(evt);
            }
        });
        panelItem.add(txtKetSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(489, 6, 80, 22));

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
        panelItem.add(txtMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 6, 100, 22));

        jTabbedPane1.addTab("Item Transaksi", panelItem);

        panelNotaLalu.setBackground(new java.awt.Color(204, 204, 204));
        panelNotaLalu.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelNotaLalu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblNotaLalu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Merk", "Item Transaksi", "Ukuran", "Satuan", "Tarif", "JUMLAH", "No.Nota"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNotaLalu.getTableHeader().setReorderingAllowed(false);
        tblNotaLalu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblNotaLaluKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblNotaLalu);
        tblNotaLalu.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblNotaLalu.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblNotaLalu.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblNotaLalu.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblNotaLalu.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblNotaLalu.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblNotaLalu.getColumnModel().getColumn(6).setPreferredWidth(130);

        panelNotaLalu.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 51, 780, 140));

        btnAddNota.setText("+");
        btnAddNota.setToolTipText("Tambahkan ke nota gabungan");
        btnAddNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddNotaMouseClicked(evt);
            }
        });
        btnAddNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNotaActionPerformed(evt);
            }
        });
        btnAddNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddNotaKeyPressed(evt);
            }
        });
        panelNotaLalu.add(btnAddNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(783, 22, 40, -1));

        txtNoNotaDetail.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        txtNoNotaDetail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtNoNotaDetail.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtNoNotaDetail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoNotaDetailFocusLost(evt);
            }
        });
        txtNoNotaDetail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoNotaDetailKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoNotaDetailKeyReleased(evt);
            }
        });
        panelNotaLalu.add(txtNoNotaDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 23, 170, 24));

        lblKapalBerangkat.setBackground(new java.awt.Color(255, 255, 255));
        lblKapalBerangkat.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapalBerangkat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapalBerangkat.setOpaque(true);
        panelNotaLalu.add(lblKapalBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 23, 480, 24));

        lblJmlNota.setBackground(new java.awt.Color(255, 255, 255));
        lblJmlNota.setFont(new java.awt.Font("Dialog", 0, 12));
        lblJmlNota.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblJmlNota.setOpaque(true);
        panelNotaLalu.add(lblJmlNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(652, 23, 130, 24));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("JML. NOTA");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel8.setOpaque(true);
        panelNotaLalu.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(652, 8, 130, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setText("NO. NOTA");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel10.setOpaque(true);
        panelNotaLalu.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 8, 170, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setText("KAPAL ~ ");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel11.setOpaque(true);
        panelNotaLalu.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 8, 480, -1));

        jTabbedPane1.addTab("Nota Kapal Lalu", panelNotaLalu);

        panelNotaBerikut.setBackground(new java.awt.Color(204, 204, 204));
        panelNotaBerikut.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelNotaBerikut.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblNotaBerikut.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Merk", "Item Transaksi", "Ukuran", "Satuan", "Tarif", "JUMLAH", "No.Nota"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNotaBerikut.getTableHeader().setReorderingAllowed(false);
        tblNotaBerikut.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblNotaBerikutKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(tblNotaBerikut);
        tblNotaBerikut.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblNotaBerikut.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblNotaBerikut.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblNotaBerikut.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblNotaBerikut.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblNotaBerikut.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblNotaBerikut.getColumnModel().getColumn(6).setPreferredWidth(130);

        panelNotaBerikut.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 51, 780, 140));

        jButton3.setText("Nota Baru");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        panelNotaBerikut.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 30));

        jButton5.setText("Lookup nota");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        panelNotaBerikut.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 140, 30));

        jTabbedPane1.addTab("Nota Kapal Berikutnya", panelNotaBerikut);

        jTabbedPane1.setSelectedIndex(1);

        jLabel12.setBackground(new java.awt.Color(0, 0, 255));
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("TOTAL    ");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel12.setOpaque(true);

        lblTotalNota.setBackground(new java.awt.Color(0, 0, 255));
        lblTotalNota.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblTotalNota.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalNota.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalNota.setText("0");
        lblTotalNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalNota.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        radioPengirim.setForeground(new java.awt.Color(255, 255, 255));
        radioPengirim.setText("Toko Pengirim");
        radioPengirim.setOpaque(false);
        radioPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioPengirimActionPerformed(evt);
            }
        });
        jPanel1.add(radioPengirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 62, -1, -1));

        radioToko.setForeground(new java.awt.Color(255, 255, 255));
        radioToko.setSelected(true);
        radioToko.setText("Toko Tujuan");
        radioToko.setOpaque(false);
        radioToko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTokoActionPerformed(evt);
            }
        });
        jPanel1.add(radioToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(88, 62, 130, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("KEPADA");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 117, 80, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("NO. NOTA");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(528, 89, 80, -1));

        txtNoNota.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNoNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNoNota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoNotaKeyPressed(evt);
            }
        });
        jPanel1.add(txtNoNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 85, 220, 24));

        txtKepada.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtKepada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKepada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKepadaKeyReleased(evt);
            }
        });
        jPanel1.add(txtKepada, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 113, 734, 24));

        txtToko.setFont(new java.awt.Font("Dialog", 0, 12));
        txtToko.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtToko.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtToko.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokoFocusLost(evt);
            }
        });
        txtToko.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTokoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTokoKeyReleased(evt);
            }
        });
        jPanel1.add(txtToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 87, 64, 24));

        lblToko.setBackground(new java.awt.Color(255, 255, 255));
        lblToko.setFont(new java.awt.Font("Dialog", 0, 12));
        lblToko.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblToko.setOpaque(true);
        jPanel1.add(lblToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 87, 330, 24));

        btnGenerateNota.setText("Generate No. Nota");
        btnGenerateNota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGenerateNotaMouseClicked(evt);
            }
        });
        jPanel1.add(btnGenerateNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 62, 220, -1));

        txtKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKapal.setDisabledTextColor(new java.awt.Color(153, 153, 153));
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
        jPanel1.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(92, 7, 64, 24));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Tgl. Berangkat");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 9, 90, 20));

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);
        jPanel1.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 7, 320, 24));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("KAPAL");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 13, 80, -1));

        lblKotaTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaTujuan.setOpaque(true);
        jPanel1.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 35, 380, 24));

        lblSerialKapal.setText("0");
        jPanel1.add(lblSerialKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 110, 20));

        lblTglBerangkat.setBackground(new java.awt.Color(255, 255, 255));
        lblTglBerangkat.setFont(new java.awt.Font("Dialog", 0, 12));
        lblTglBerangkat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTglBerangkat.setOpaque(true);
        jPanel1.add(lblTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 7, 130, 24));

        btnLookupPackingList.setText("...");
        btnLookupPackingList.setToolTipText("Lihat Packing List");
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
        jPanel1.add(btnLookupPackingList, new org.netbeans.lib.awtextra.AbsoluteConstraints(491, 88, 22, 22));

        jButton4.setText("Urutan Nota");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(124, 124, 124)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTotalNota, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalNota, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-845)/2, (screenSize.height-618)/2, 845, 618);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        lst.setVisible(false);
        Utama.isNotaGabungOn=false;
        //dispose();
    }//GEN-LAST:event_formInternalFrameClosed

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        udfClear();
}//GEN-LAST:event_btnClearMouseClicked

    private void btnClearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnClearKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            udfClear();
        }
}//GEN-LAST:event_btnClearKeyPressed

    private void btnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCloseKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            this.dispose();
        }
}//GEN-LAST:event_btnCloseKeyPressed

    private void btnCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCloseMouseClicked
        this.dispose();
}//GEN-LAST:event_btnCloseMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        MyKeyListener kListener=new MyKeyListener();
        jDateNota.setFormats("dd-MM-yyyy");
        
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        lblKepada=new JLabel();
        
        MyRowRendererNota rndNota=new MyRowRendererNota();
        MyRowRendererItem rndItem=new MyRowRendererItem();
        
        modelNotaLalu=(DefaultTableModel)tblNotaLalu.getModel();
        modelNotaLalu.setNumRows(0);
        tblNotaLalu.setModel(modelNotaLalu);
        
        modelNotaBerikut=(DefaultTableModel)tblNotaBerikut.getModel();
        modelNotaBerikut.setNumRows(0);
        tblNotaBerikut.setModel(modelNotaBerikut);
        
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
        
        for(int i=0;i<panelNotaLalu.getComponentCount();i++){
            Component c = panelNotaLalu.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JScrollPane") || c.getClass().getSimpleName().equalsIgnoreCase("JTable")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<panelItem.getComponentCount();i++){
            Component c = panelItem.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JScrollPane") || c.getClass().getSimpleName().equalsIgnoreCase("JTable")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
                
        udfClear();
        
        
        txtNoNotaDetail.setText(srcKapal);
        requestFocusInWindow();
        txtKepada.requestFocus();
        
        tblNotaLalu.getModel().addTableModelListener(new MyTableModelListener(tblNotaLalu));
        tblItem.getModel().addTableModelListener(new MyTableModelListener(tblItem));
        tblNotaBerikut.getModel().addTableModelListener(new MyTableModelListener(tblNotaBerikut));
        
        udfSetKeterangan();
        
        jTabbedPane1.setSelectedIndex(0);
        
        for (int i=0;i<tblNotaLalu.getColumnCount();i++){
            tblNotaLalu.getColumnModel().getColumn(i).setCellRenderer(rndNota);
            tblNotaBerikut.getColumnModel().getColumn(i).setCellRenderer(rndNota);
        }
        
        for (int i=0;i<tblItem.getColumnCount();i++){
            tblItem.getColumnModel().getColumn(i).setCellRenderer(rndItem);
        }
        
    }//GEN-LAST:event_formInternalFrameOpened

    
    private void btnAddNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddNotaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER ||evt.getKeyCode()==KeyEvent.VK_SPACE ){
            udfAddNota(txtNoNotaDetail.getText());
        }
}//GEN-LAST:event_btnAddNotaKeyPressed

    private void btnAddNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddNotaMouseClicked
        if(!txtNoNotaDetail.getText().trim().equalsIgnoreCase(""))
            udfAddNota(txtNoNotaDetail.getText());
}//GEN-LAST:event_btnAddNotaMouseClicked

    private void tblNotaLaluKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNotaLaluKeyPressed
        int iRow=tblNotaLalu.getSelectedRow();
        if(evt.getKeyCode()==KeyEvent.VK_F3){
            
            isEditRow=true;
//            txtMerk.setText(itemModel.getValueAt(iRow, 0).toString());
//            txtItemTrx.setText(itemModel.getValueAt(iRow, 1).toString());
//            txtQty.setText(fmt.format((Float)itemModel.getValueAt(iRow, 2)));
//            txtSatuan.setText(itemModel.getValueAt(iRow, 3).toString());
//            txtKetSatuan.setText(itemModel.getValueAt(iRow, 4).toString());
//            txtTarif.setText(fmt.format((Float)itemModel.getValueAt(iRow, 5)));
//            txtSubTotal.setText(fmt.format((Float)itemModel.getValueAt(iRow, 6)));
            
        }if(evt.getKeyCode()==KeyEvent.VK_DELETE && !(modelNotaLalu.getValueAt(iRow, 6).toString().equalsIgnoreCase(""))){
            modelNotaLalu.removeRow(iRow);
            
            while(iRow<modelNotaLalu.getRowCount() && modelNotaLalu.getValueAt(iRow, 6).toString().equalsIgnoreCase("")){
                modelNotaLalu.removeRow(iRow);
            }
        }
        else{
            udfClearItem();
            isEditRow=true;
        }
}//GEN-LAST:event_tblNotaLaluKeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(isKoreksi)
            udfProsesKoreksi();
        else
            udfSave();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddNotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNotaActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_btnAddNotaActionPerformed

    private void txtNoNotaDetailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoNotaDetailFocusLost
        
}//GEN-LAST:event_txtNoNotaDetailFocusLost

    private void txtNoNotaDetailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoNotaDetailKeyPressed
        
}//GEN-LAST:event_txtNoNotaDetailKeyPressed

    private void txtNoNotaDetailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoNotaDetailKeyReleased
        String sQry="";
        try {
            sQry = "select nota.no_nota, " +
                    "coalesce(nama_kapal||', Berangkat Tgl '||fn_tanggal_ind(nota.tgl_nota),'') as kapal_berangkat, " + "sum(coalesce(sub_total,0)) as sub_total " + "from nota " + "inner join nota_detail d on nota.no_nota=d.no_nota " + "left join nota_kapal_tujuan ktj on seri_kapal=serial_kode " + "left join kapal on kapal.kode_kapal=ktj.kode_kapal " + "where coalesce(nota_header,'')='' and nota.no_nota<>'" + txtNoNota.getText() + "'  " + "and customer='" + txtToko.getText() + "' " + "and tagihan_per='" + (radioToko.isSelected() ? "T" : "P") + "' " + "and to_char(ktj.tgl_berangkat, 'yyyy-MM-dd')<='" + fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText())) + "'  " + "group by nota.no_nota, coalesce(nama_kapal||', Berangkat Tgl '||fn_tanggal_ind(nota.tgl_nota),'') order by nota.no_nota";
        } catch (ParseException ex) {
            Logger.getLogger(FrmNotaGabung080807.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtNoNotaDetail.setText(obj[0].toString());
                            lblKapalBerangkat.setText(obj[1].toString());
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
                    txtNoNotaDetail.setText("");
                    lblKapalBerangkat.setText("");
                    txtNoNotaDetail.requestFocus();
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
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jTabbedPane1.getX()+this.panelNotaLalu.getX()+ this.txtNoNotaDetail.getX()+19,
                                Utama.iTop + this.getY()+this.jTabbedPane1.getY() + this.panelNotaLalu.getY()+this.txtNoNotaDetail.getY() + txtNoNotaDetail.getHeight()+77,
                                txtNoNotaDetail.getWidth()+lblKapalBerangkat.getWidth()+lblJmlNota.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtNoNotaDetail);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapalBerangkat, lblJmlNota});
                        lst.setColWidth(0, txtNoNotaDetail.getWidth());
                        lst.setColWidth(1, lblKapalBerangkat.getWidth());
                        lst.setColWidth(2, lblJmlNota.getWidth());
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtNoNotaDetail.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtNoNotaDetail.setText("");
                            lblKapalBerangkat.setText("");
                            lblJmlNota.setText("0");
                            txtNoNotaDetail.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtNoNotaDetailKeyReleased

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        udfClear();
        txtNoNotaDetail.setText(""); lblKapalBerangkat.setText(""); lblSerialKapal.setText("");
        lblJmlNota.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        udfPrint();
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        udfLookupKeterangan();
    }//GEN-LAST:event_jButton2MouseClicked

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
        }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jTabbedPane1.getX()+panelNotaLalu.getX()+ this.txtItemTrx.getX()+19,
                                Utama.iTop+ this.getY()+this.jTabbedPane1.getY()+panelNotaLalu.getY()+this.txtItemTrx.getY() + txtItemTrx.getHeight()+77,
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
            txtSubTotal.setText(dFmt.format(udfGetFloat(txtQty.getText()) * udfGetFloat(txtTarif.getText()) ) );
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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jTabbedPane1.getX()+panelNotaLalu.getX()+ this.txtSatuan.getX()+19,
                                Utama.iTop+this.getY()+this.jTabbedPane1.getY()+panelNotaLalu.getY()+this.txtSatuan.getY() + txtSatuan.getHeight()+77,
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

    private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalActionPerformed

    private void txtSubTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubTotalFocusLost
        txtSubTotal.setText(fmt.format(udfGetFloat(txtSubTotal.getText())));
        
        if(udfGetFloat(txtSubTotal.getText())!= udfGetFloat(dFmt.format(udfGetFloat(txtQty.getText())*udfGetFloat(txtTarif.getText()))) &&
                !txtQty.getText().equalsIgnoreCase("")){
            
            txtQty.setText("");
            txtTarif.setText("");
        }
    }//GEN-LAST:event_txtSubTotalFocusLost

    private void txtSubTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubTotalKeyPressed
        //        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
        //            udfAddItem();
        //        }
    }//GEN-LAST:event_txtSubTotalKeyPressed

    private void addItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addItemMouseClicked
        udfAddItemTrx();
}//GEN-LAST:event_addItemMouseClicked

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_addItemActionPerformed

    private void addItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addItemKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER ||evt.getKeyCode()==KeyEvent.VK_SPACE ){
            udfAddItemTrx();
        }
}//GEN-LAST:event_addItemKeyPressed

    private void txtTarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarifActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTarifActionPerformed

    private void txtTarifFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarifFocusLost
        txtTarif.setText(fmt.format(udfGetFloat(txtTarif.getText())));
        if(udfGetFloat(txtTarif.getText()) > 0){
            if(!txtQty.getText().trim().equalsIgnoreCase(""))
                txtSubTotal.setText(dFmt.format(udfGetFloat(txtQty.getText()) * udfGetFloat(txtTarif.getText()) ) );
        }else
            txtTarif.setText("");
    }//GEN-LAST:event_txtTarifFocusLost

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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jTabbedPane1.getX()+panelNotaLalu.getX()+ this.txtKetSatuan.getX()+19,
                                Utama.iTop+this.getY()+this.jTabbedPane1.getY()+panelNotaLalu.getY()+this.txtKetSatuan.getY() + txtKetSatuan.getHeight()+77,
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
                        
                        sQry+=(radioToko.isSelected()? "and kode_cust='"+txtToko.getText()+"' ": "");
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jTabbedPane1.getX()+this.panelItem.getX()+ this.txtMerk.getX()+19,
                                Utama.iTop + this.getY() +this.jTabbedPane1.getY() + this.panelItem.getY()+this.txtMerk.getY() + txtMerk.getHeight()+77,
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
                            txtMerk.setText("");
                            txtMerk.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtMerkKeyReleased

    private void radioPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPengirimActionPerformed
        udfClear();
    }//GEN-LAST:event_radioPengirimActionPerformed

    private void radioTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTokoActionPerformed
        udfClear();
    }//GEN-LAST:event_radioTokoActionPerformed

    private void txtNoNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoNotaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER && isKoreksi){
            udfLoadDetailKoreksi();
        }
    }//GEN-LAST:event_txtNoNotaKeyPressed

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

    private void txtTokoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoFocusLost
        txtKepada.setText(lblKepada.getText());
    }//GEN-LAST:event_txtTokoFocusLost

    private void txtTokoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTokoKeyPressed

    private void txtTokoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoKeyReleased
        try {
            String sQry=getQuery();
            switch(evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtToko.setText(obj[0].toString());
                            lblToko.setText(obj[1].toString());
                            lblKepada.setText(obj[2].toString());
                            txtKepada.setText(lblKepada.getText());
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
                    txtToko.setText("");
                    lblToko.setText("");
                    lblKepada.setText("");
                    txtKepada.setText("");
                    
                    txtToko.requestFocus();
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
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jPanel1.getX()+ this.txtToko.getX()+19,
                                Utama.iTop + this.getY() + this.jPanel1.getY()+this.txtToko.getY() + txtToko.getHeight()+77,
                                txtToko.getWidth()+lblToko.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtToko);
                        lst.setLblDes(new javax.swing.JLabel[]{lblToko, lblKepada});
                        lst.setColWidth(0, txtToko.getWidth());
                        lst.setColWidth(1, lblToko.getWidth());
                        
                        lst.setRemoveCol(2);
                        
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtToko.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtToko.setText("");
                            lblToko.setText("");
                            lblKepada.setText("");
                            txtToko.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtTokoKeyReleased

    private void btnGenerateNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerateNotaMouseClicked
        if(txtToko.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi Toko terlebih dulu!", "Nota message", JOptionPane.OK_OPTION);
            txtToko.requestFocus();
            return;
        }else{
            txtNoNota.setText(udfGetNewNota());
            
        }
    }//GEN-LAST:event_btnGenerateNotaMouseClicked

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
                        String sQry="select tj.kode_kapal, nama_kapal , to_Char(tgl_berangkat, 'dd-MM-yyyy') as tgl_berangkat, " +
                                "coalesce(nama_kota,'') as kota_tujuan, serial_kode " +
                                "from nota_kapal_tujuan tj " +
                                "inner join kapal on kapal.kode_kapal=tj.kode_kapal " +
                                "left join kota on kode_kota=kota_tujuan " +
                                "where (tj.kode_kapal||nama_kapal) iLike '%"+txtKapal.getText()+"%' " +
                                "and coalesce(tj.kota_tujuan,'') iLike '%"+Utama.sKodeKota+"%' " +
                                "and active=true order by tj.kode_kapal limit 100";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jPanel1.getX()+ this.txtKapal.getX()+19,
                                Utama.iTop + this.getY() + this.jPanel1.getY()+this.txtKapal.getY() + txtKapal.getHeight()+77,
                                txtKapal.getWidth()+lblToko.getWidth()+jLabel12.getWidth()+lblTglBerangkat.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal, lblTglBerangkat, lblKotaTujuan, lblSerialKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblToko.getWidth());
                        lst.udfRemoveColumn(4);
                        
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

    private void btnLookupPackingListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLookupPackingListMouseClicked
        if(txtKapal.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan pilih kapal & tanggal berangkat terlebih dulu!");
            txtKapal.requestFocus();
            return;
        }
        if(txtToko.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan pilih toko pengirim atau tujuan terlebih dulu!");
            txtToko.requestFocus();
            return;
        }
        
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            
            String sTgl=lblTglBerangkat.getText().trim().equalsIgnoreCase("")? "": fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText()));
            reportParam.put("kapal", txtKapal.getText());
            reportParam.put("tgl_berangkat", sTgl);
            reportParam.put("customer", txtToko.getText());
            reportParam.put("is_tujuan", Boolean.valueOf(radioToko.isSelected() ? true: false));
            
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

private void tblNotaBerikutKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNotaBerikutKeyPressed
    int iRow=tblNotaBerikut.getSelectedRow();
        if(evt.getKeyCode()==KeyEvent.VK_DELETE && !(modelNotaBerikut.getValueAt(iRow, 6).toString().equalsIgnoreCase(""))){
            modelNotaBerikut.removeRow(iRow);
            
            while(iRow<modelNotaBerikut.getRowCount() && modelNotaBerikut.getValueAt(iRow, 6).toString().equalsIgnoreCase("")){
                modelNotaBerikut.removeRow(iRow);
            }
        }
}//GEN-LAST:event_tblNotaBerikutKeyPressed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    if(txtToko.getText().trim().equalsIgnoreCase("")){
        JOptionPane.showMessageDialog(this, "Silakan isi toko/ customer terlebih dulu!", "Message", JOptionPane.INFORMATION_MESSAGE);
        txtToko.requestFocus();
        return;
    }
    if(txtKepada.getText().trim().equalsIgnoreCase("")){
        JOptionPane.showMessageDialog(this, "Silakan isi kepada terlebih dulu!", "Message", JOptionPane.INFORMATION_MESSAGE);
        txtKepada.requestFocus();
        return;
    }
    
    FrmNotaGabungBerikut f1=new FrmNotaGabungBerikut(JOptionPane.getFrameForComponent(this), false);
    f1.setConn(conn);
    f1.setUserName(sUserName);
    f1.setKepada(txtKepada.getText());
    f1.setSFlag(radioToko.isSelected()?"T": "P");
    f1.setKodeCustomer(txtToko.getText());
    f1.setTglBerangkat(lblTglBerangkat.getText());
    f1.setSrcModel(modelNotaBerikut);
    f1.setVisible(true);
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    FrmNotaLookup f1=new FrmNotaLookup(JOptionPane.getFrameForComponent(this), true);
    f1.setConn(conn);
    f1.setNoNotaHeader(txtNoNota.getText());
    f1.setIsLookup(false);
    f1.setTitle("Urutan nota dalam laporan");
    
    f1.setVisible(true);
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    if(lblTglBerangkat.getText().trim().equalsIgnoreCase("")){
        JOptionPane.showMessageDialog(this, "Silakan pilih kapal berangkat terlebih dulu!");
        txtKapal.requestFocus();
        return;
    }
    
    if(lblToko.getText().trim().equalsIgnoreCase("")){
        JOptionPane.showMessageDialog(this, "Silakan isi toko terlebih dulu!");
        txtKapal.requestFocus();
        return;
    }
    FrmNotaLookup f1=new FrmNotaLookup(JOptionPane.getFrameForComponent(this), true);
    f1.setConn(conn);
    f1.setNoNotaHeader(txtNoNota.getText());
    f1.setIsLookup(true);
    f1.setTitle("Lookup nota untuk toko - "+ lblToko.getText());
    f1.setTo((radioToko.isSelected()? "T": "P"));
    f1.setCustomer(txtToko.getText());
    f1.setSrcModel(modelNotaBerikut);
    f1.setTglBerangkat(lblTglBerangkat.getText());
    f1.setVisible(true);
}//GEN-LAST:event_jButton5ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmNotaGabung080807().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItem;
    private javax.swing.JButton btnAddNota;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGenerateNota;
    private javax.swing.JButton btnLookupPackingList;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbKondisi;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private org.jdesktop.swingx.JXDatePicker jDateNota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblJmlNota;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JLabel lblKapalBerangkat;
    private javax.swing.JLabel lblKotaTujuan;
    private javax.swing.JLabel lblSerialKapal;
    private javax.swing.JLabel lblTglBerangkat;
    private javax.swing.JLabel lblToko;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JPanel panelItem;
    private javax.swing.JPanel panelNotaBerikut;
    private javax.swing.JPanel panelNotaLalu;
    private javax.swing.JRadioButton radioPengirim;
    private javax.swing.JRadioButton radioToko;
    private javax.swing.JTable tblItem;
    private javax.swing.JTable tblNotaBerikut;
    private javax.swing.JTable tblNotaLalu;
    private javax.swing.JTextField txtItemTrx;
    private javax.swing.JTextField txtKapal;
    private javax.swing.JTextField txtKepada;
    private javax.swing.JTextField txtKetSatuan;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextArea txtMemo;
    private javax.swing.JTextField txtMerk;
    private javax.swing.JTextField txtNoNota;
    private javax.swing.JTextField txtNoNotaDetail;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTarif;
    private javax.swing.JTextField txtToko;
    // End of variables declaration//GEN-END:variables

    Color g1 = new Color(239,234,240);//-->>(251,236,177);// Kuning         [251,251,235]
    Color g2 = new Color(239,234,240);//-->>(241,226,167);// Kuning         [247,247,218]


    Color w1 = new Color(255,255,255);// Putih
    Color w2 = new Color(250,250,250);// Putih Juga

    Color h1 = new Color(51,255,0);// Color(255,240,240);// Hijau menyala
    Color h2 = new Color(250,230,230);// Merah Muda
    JTextField txt;        
            
    private void udfClear() {
        if(modelItem.getRowCount()>0 && !isSave){
            if(JOptionPane.showConfirmDialog(this, "Data akan dihilangkan senelum disimpan?", "Konfirmasi", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            return;
            }
        }  
        udfClearItem();
        
        modelItem.setNumRows(0); modelNotaLalu.setNumRows(0);
//        tblNota.setModel(notaModel); 
//        tblItem.setModel(itemModel);

        txtKepada.setText("");
        txtKapal.setText(""); lblKapal.setText("");
        txtToko.setText(""); lblToko.setText("");
        txtNoNota.setText("");
        lblTotalNota.setText("0");
        lblKotaTujuan.setText("");
        lblTglBerangkat.setText("");
        lblKepada.setText("");
        
        isEditRow=false;
        isSave=false;
        
        txtToko.requestFocus();
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
   
//    private void udfLoadTglBerangkat(){
//        cmbTglBerangkat.removeAllItems();
//        
//        try {
//            Statement st1 = conn.createStatement();
//            ResultSet rs1=st1.executeQuery("select distinct coalesce(to_Char(tgl_berangkat,'dd-MM-yyyy'),'') as tanggal from kontainer " +
//                    "where active=true and kode_kapal='"+txtKapal.getText()+"' ");
//            
//            cmbTglBerangkat.removeAllItems();
//            
//            while(rs1.next()){
//                cmbTglBerangkat.addItem(rs1.getString(1));
//                
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(FrmNota.class.getName()).log(Level.SEVERE, null, ex);
//        }
//             
//    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            if(evt.getSource().equals(btnAddNota)) return;
            
            switch(keyKode){
                case KeyEvent.VK_ENTER : {
                    if(evt.getSource().getClass().getName().equalsIgnoreCase("JTable") ||
                            evt.getSource().equals(addItem)){
                        return;
                    }
                    
		    if (!lst.isVisible()){
			Component c = findNextFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_UP : {
                    if(evt.getSource().getClass().getName().equalsIgnoreCase("JTable")){
                        return;
                    }
                    
		    if (!lst.isVisible()){
			Component c = findPrevFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_DOWN : {
		    if(evt.getSource().getClass().getName().equalsIgnoreCase("JTable")){
                        return;
                    }
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
    
    private void udfClearItem(){
        txtMerk.setText("");
        txtItemTrx.setText("");
        txtQty.setText("");
        txtSatuan.setText("");
        txtKetSatuan.setText("");
        txtTarif.setText("");
        txtSubTotal.setText("");
        
        txtNoNotaDetail.setText("");
        lblKapalBerangkat.setText("");
        lblJmlNota.setText("");
    }
    
    private void udfAddNota(String sNota){
        if (txtNoNotaDetail.getText().trim().equalsIgnoreCase("") && !isKoreksi){
            JOptionPane.showMessageDialog(this, "Silakan pilih nota yang akan digabung terlebih dulu!");
            txtNoNotaDetail.requestFocus();
            return;
            
        }
//        if (udfGetFloat(txtSubTotal.getText())==0){
//            JOptionPane.showMessageDialog(this, "Silakan isi jumlah biaya terlebih dulu!");
//            txtSubTotal.requestFocus();
//            return;
//        }
//        
        for(int i=0; i<modelNotaLalu.getRowCount(); i++){
            if(!isKoreksi && modelNotaLalu.getValueAt(i, 6).toString().trim().equalsIgnoreCase(txtNoNotaDetail.getText())){
                JOptionPane.showMessageDialog(this, "Anda sudah memasukkan No. Nota tersebut!", "Message", JOptionPane.OK_OPTION);
                txtNoNotaDetail.requestFocus();
                return;
            }
        }
        try{
            Statement sD=conn.createStatement();
            ResultSet rD=sD.executeQuery("select coalesce(merk,'') as merk, item_trx, ukuran, satuan, tarif, sub_total " +
                    "from nota_detail where no_nota='"+sNota+"' ");
            
            if(rD.next()){
                //if(fmtYMD.parse(sTglBerangkat).before(fmtYMD.parse(fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText())))) ){
                //Tambahkan dulu Nota Headernya
                    modelNotaLalu.addRow(new Object[]{
                        "",
                        lblKapalBerangkat.getText(),
                        "",
                        "",
                        "",
                        "",
                        txtNoNotaDetail.getText()
                    });

                    //Tambahkan nota detailnya

                    modelNotaLalu.addRow(new Object[]{ 
                        rD.getString("merk"),
                        rD.getString("item_trx"),
                        rD.getFloat("ukuran"),
                        rD.getString("satuan"),
                        rD.getFloat("tarif"),
                        rD.getFloat("sub_total"),
                        ""
                    });

                    while(rD.next()){
                        modelNotaLalu.addRow(new Object[]{
                            rD.getString("merk"),
                            rD.getString("item_trx"),
                            rD.getFloat("ukuran"),
                            rD.getString("satuan"),
                            rD.getFloat("tarif"),
                            rD.getFloat("sub_total"),
                            ""
                        });
                    }
                //}
            }
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, "Ada kesalahan dalam menambahkan nota!\n"+se.getMessage());
            return;
        }
        
        udfClearItem();
        
        txtNoNotaDetail.requestFocus();
    }
    
//    
    private void udfAddNotaKoreksi(String sNota){
        if (txtNoNotaDetail.getText().trim().equalsIgnoreCase("") && !isKoreksi){
            JOptionPane.showMessageDialog(this, "Silakan pilih nota yang akan digabung terlebih dulu!");
            txtNoNotaDetail.requestFocus();
            return;
            
        }
//        if (udfGetFloat(txtSubTotal.getText())==0){
//            JOptionPane.showMessageDialog(this, "Silakan isi jumlah biaya terlebih dulu!");
//            txtSubTotal.requestFocus();
//            return;
//        }
//        
        for(int i=0; i<modelNotaLalu.getRowCount(); i++){
            if(!isKoreksi && modelNotaLalu.getValueAt(i, 6).toString().trim().equalsIgnoreCase(txtNoNotaDetail.getText())){
                JOptionPane.showMessageDialog(this, "Anda sudah memasukkan No. Nota tersebut!", "Message", JOptionPane.OK_OPTION);
                txtNoNotaDetail.requestFocus();
                return;
            }
        }
        try{
            Statement sD=conn.createStatement();
            ResultSet rD=sD.executeQuery("select coalesce(merk,'') as merk, item_trx, ukuran, satuan, tarif, sub_total " +
                    "from nota_detail where no_nota='"+sNota+"' ");
            
            if(rD.next()){
                if(fmtYMD.parse(sTglBerangkat).before(fmtYMD.parse(fmtYMD.format(fmtDMY.parse(lblTglBerangkat.getText())))) ){
                //Tambahkan dulu Nota Headernya
                    modelNotaLalu.addRow(new Object[]{
                        "",
                        lblKapalBerangkat.getText(),
                        "",
                        "",
                        "",
                        "",
                        txtNoNotaDetail.getText()
                    });

                    //Tambahkan nota detailnya

                    modelNotaLalu.addRow(new Object[]{ 
                        rD.getString("merk"),
                        rD.getString("item_trx"),
                        rD.getFloat("ukuran"),
                        rD.getString("satuan"),
                        rD.getFloat("tarif"),
                        rD.getFloat("sub_total"),
                        ""
                    });

                    while(rD.next()){
                        modelNotaLalu.addRow(new Object[]{
                            rD.getString("merk"),
                            rD.getString("item_trx"),
                            rD.getFloat("ukuran"),
                            rD.getString("satuan"),
                            rD.getFloat("tarif"),
                            rD.getFloat("sub_total"),
                            ""
                        });
                    }
                }else{
                    //Tambahkan dulu Nota Headernya
                    modelNotaBerikut.addRow(new Object[]{
                        "",
                        lblKapalBerangkat.getText(),
                        "",
                        "",
                        "",
                        "",
                        txtNoNotaDetail.getText()
                    });

                    //Tambahkan nota detailnya

                    modelNotaBerikut.addRow(new Object[]{rD.getString("merk"), rD.getString("item_trx"), rD.getFloat("ukuran"),
                        rD.getString("satuan"), rD.getFloat("tarif"),rD.getFloat("sub_total"), ""
                    });

                    while(rD.next()){
                        modelNotaBerikut.addRow(new Object[]{
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
            }
        } catch (ParseException ex) {
            Logger.getLogger(FrmNotaGabung080807.class.getName()).log(Level.SEVERE, null, ex);
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, "Ada kesalahan dalam menambahkan nota!\n"+se.getMessage());
            return;
        }
        
        udfClearItem();
        
        txtNoNotaDetail.requestFocus();
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
            modelItem.setValueAt((udfGetFloat(txtQty.getText())<=0? "": udfGetFloat(txtQty.getText())) , tblItem.getSelectedRow(), 2);
            modelItem.setValueAt(txtSatuan.getText(), tblItem.getSelectedRow(), 3);
            modelItem.setValueAt(txtKetSatuan.getText(), tblItem.getSelectedRow(), 4);
            modelItem.setValueAt((udfGetFloat(txtTarif.getText())<=0? "": udfGetFloat(txtTarif.getText()) ), tblItem.getSelectedRow(), 5);
            modelItem.setValueAt(udfGetFloat(txtSubTotal.getText()), tblItem.getSelectedRow(), 6);
            isEditRow=false;
        }
        
        udfClearItem();
        
        txtMerk.requestFocus();
    }
    
    private void udfSetTotal(){
        int i=0;
        double total=0;
        
        
        for (i=0; i<modelItem.getRowCount();i++){
            total = total+ udfGetFloat(modelItem.getValueAt(i, 6).toString());
        }
        for (i=0; i<modelNotaLalu.getRowCount();i++){
            total = total+ udfGetFloat(modelNotaLalu.getValueAt(i, 5).toString());
        }
        for (i=0; i<modelNotaBerikut.getRowCount();i++){
            total = total+ udfGetFloat(modelNotaBerikut.getValueAt(i, 5).toString());
        }
        
        System.out.println(total);
        String sTotal=dFmt.format(total);
        
        lblTotalNota.setText(sTotal);
        //lblTotalNota.setText(dFmt.format(total));
   }
    
    public class MyTableModelListener implements TableModelListener {
        JTable table;
        
        MyTableModelListener(JTable table){
            this.table=table;
        }
        
        public void tableChanged(TableModelEvent e) {
            System.out.println("ke myTableModelListener");
            udfSetTotal();
        }
        
    }
    
    private String getQuery(){
        //String sQry;
        if(radioToko.isSelected()){
            sQry="select c.kode_cust as kode,  coalesce(c.nama,'') as toko, " +
                 "coalesce(c.nama,'') ||' ('|| gabung(merk.merk)||'), ' ||coalesce(nama_kota,'') as kepada " +
                 "from customer c inner join merk on c.kode_cust=merk.kode_cust " +
                 "left join kota on kota.kode_kota=c.kota " +
                 "where (coalesce(c.nama,'')||coalesce(merk.merk,'')||coalesce(nama_kota,'')) iLike '%"+txtToko.getText()+"%' " +
                 "and c.kota ilike '%"+Utama.sKodeKota+"%'" +
                 "group by c.kode_cust, coalesce(c.nama,''), coalesce(nama_kota,'') " +
                 "order by coalesce(c.nama,'')";
                    
            return sQry;
        }
        
        if(radioPengirim.isSelected()){
            sQry=   "select kode_toko, coalesce(nama,'') as nama, coalesce(nama,'') as kepada " +
                    "from toko where coalesce(nama,'') iLike '%"+txtToko.getText()+"%' order by coalesce(nama,'') ";
            
            return sQry;
        }
        
        return sQry;
    }
    
    private String udfGetNewNota(){
        String sNew="";
        try{
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery("select fn_nota_get_new_kode_tagihan('"+txtToko.getText()+"', '"+fmtYMD.format(jDateNota.getDate())+"')");

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
    
    private void udfSave(){
        if(txtKapal.getText().trim().equalsIgnoreCase("")||udfGetFloat(lblSerialKapal.getText())==0){
            JOptionPane.showMessageDialog(this, "Silakan isi Kapal terlebih dulu!");
            txtKapal.requestFocus();
            return;
        }
        if(txtKepada.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi Kepada terlebih dulu!");
            txtKepada.requestFocus();
            return;
        }
        if(txtNoNota.getText().trim().equalsIgnoreCase("")){
            String sNota=udfGetNewNota();
            if(JOptionPane.showConfirmDialog(this, "No. Nota akan dibuatkan secara otomatis oleh sistem dengan No. Nota='"+sNota+"'. \n" +
                    "Anda yakin untuk melanjutkan?", "Konfimasi No. Nota", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                txtNoNota.setText(udfGetNewNota());
            }else{
                txtNoNota.requestFocus();
                return;
            }
        }
        if(modelItem.getRowCount()==0){
            JOptionPane.showMessageDialog(this, "Item transaksi masih kosong!");
            jTabbedPane1.setSelectedIndex(0);
            txtMerk.requestFocus();
            return;
        }
        if(modelNotaLalu.getRowCount()==0 && modelNotaBerikut.getRowCount()==0){
            if(JOptionPane.showConfirmDialog(this, "Nota yang akan digabungkan masih belum dipilih!\n" +
                    "Apakah simpan data akan dilajutkan?", "Nota lain", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
                    jTabbedPane1.setSelectedIndex(0);
                    txtMerk.requestFocus();
                    return;
            }
        }
        
        boolean isEdit=false;
        String sTag="";
        if(radioToko.isSelected()){
            sTag="T";
        }else if(radioPengirim.isSelected()){
            sTag="P";
        }
        
        try {
            conn.setAutoCommit(false);
            int i=conn.createStatement().executeUpdate("Delete from nota_detail where no_nota='"+txtNoNota.getText()+"' ");
            udfSimpanItemTrx(sTag);
            
            rsH=conn.createStatement().executeQuery("select * from nota where no_nota='"+txtNoNota.getText()+"'");
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
            rsH.updateString("customer", txtToko.getText());
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
            for (int s=0; s<modelNotaLalu.getRowCount(); s++){
                if(!tblNotaLalu.getValueAt(s, 6).toString().equalsIgnoreCase(""))
                    sUpdate+="Update nota set nota_header='"+txtNoNota.getText()+"' where no_nota='"+tblNotaLalu.getValueAt(s, 6).toString()+"'; ";
            }
            for (int s=0; s<modelNotaBerikut.getRowCount(); s++){
                if(!tblNotaBerikut.getValueAt(s, 6).toString().equalsIgnoreCase(""))
                    sUpdate+="Update nota set nota_header='"+txtNoNota.getText()+"' where no_nota='"+tblNotaBerikut.getValueAt(s, 6).toString()+"'; ";
            }
            conn.createStatement().executeUpdate(sUpdate);
            
            conn.setAutoCommit(true);
            isSave=true;
            
            if(JOptionPane.showConfirmDialog(this, "Simpan data sukses! Selanjutnya apakah akan dicetak?", "Print confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                udfPrint();
            }
            
        } catch (SQLException ex) {
            try {
                JOptionPane.showMessageDialog(this, "Simpan data gagal!\n"+ex.getMessage());
                conn.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(FrmNotaGabung080807.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
        
    }
}
