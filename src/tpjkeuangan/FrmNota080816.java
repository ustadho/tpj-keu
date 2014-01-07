/*
 * FrmNota.java
 *
 * Created on April 15, 2008, 8:07 AM
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
public class FrmNota080816 extends javax.swing.JInternalFrame {
    private Connection conn;
    DefaultTableModel myModel;
    private NumberFormat fmt=NumberFormat.getInstance();
    private NumberFormat dFmt=new DecimalFormat("#,##0");
    private ListRsbm lst;
    private boolean isEditRow=false;
    Statement stH, stD;
    ResultSet rsH, rsD;
    JLabel lblKepada ;
    String sUserName;
    SimpleDateFormat fmtYMD=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtDMY=new SimpleDateFormat("dd-MM-yyyy");
    String sOldKeterangan="";
    private boolean isKoreksi=false;
    
    String sQry;
    private boolean isSave=false;
    private String srcKapal="";
    private String sOldNota="";
    JLabel lblKotaNota=new JLabel();
    private String sSerialNota="";
        
    
    /** Creates new form FrmNota */
    public FrmNota080816() {
        initComponents();
        
        txtToko.addFocusListener(txtFoculListener);
        txtKapal.addFocusListener(txtFoculListener);
        txtNoNota.addFocusListener(txtFoculListener);
        txtKepada.addFocusListener(txtFoculListener);
        
        txtMerk.addFocusListener(txtFoculListener);    
        txtItemTrx.addFocusListener(txtFoculListener);
        txtQty.addFocusListener(txtFoculListener);
        txtSatuan.addFocusListener(txtFoculListener);
        txtKetSatuan.addFocusListener(txtFoculListener);
        txtTarif.addFocusListener(txtFoculListener);
        txtSubTotal.addFocusListener(txtFoculListener);
        
        myModel=(DefaultTableModel)jTable1.getModel();
        myModel.setNumRows(0);
        jTable1.setModel(myModel);
        //udfClear();
        
        

    }

    void setKoreksi(boolean b){
        isKoreksi=b;
    }

    void setNoNota(String toString) {
        txtNoNota.setText(toString);
    }

    private void setStKoreksi() {
        //btnGenerateNota.setVisible(!isKoreksi);
        if(isKoreksi)
            btnGenerateNota.setText("Delete Nota");
        
//        txtKapal.setEnabled(!isKoreksi);
//        radioPengirim.setEnabled(!isKoreksi);
//        radioToko.setEnabled(!isKoreksi);
        
    }

    private void udfDeleteNota() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("select * from nota_pembayaran_detail where no_nota='" + txtNoNota.getText() + "' ");
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Nota pembayaran tidak bisa dihapus karena telah dilakukan pembayaran!\n" +
                        "Untuk menghapus nota, anda harus membatalkan pembayaran yang telah dilakukan!", "Message", JOptionPane.INFORMATION_MESSAGE);
                
                txtNoNota.requestFocus();
                return;
            }
            rs.close();
            
            if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus nota ini?", "Confirm", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                    
            }else{
                txtNoNota.requestFocus();
                return;
            }
            String sDelete="" +
                    "insert into nota_history select * from nota where no_nota='"+txtNoNota.getText()+"'; " +
                    "insert into nota_history_detail select * from nota_detail where no_nota='"+txtNoNota.getText()+"'; " +
                    "Delete from nota_detail where no_nota='"+txtNoNota.getText()+"'; " +
                    "Delete from nota where no_nota='"+txtNoNota.getText()+"'; " +
                    "Update nota set nota_header=null where nota_header='"+txtNoNota.getText()+"';";
                    
            int iDel=conn.createStatement().executeUpdate(sDelete);
                    
            
            if(iDel>0){
                JOptionPane.showMessageDialog(this, "Penghapusan nota sukses!", "Message", JOptionPane.INFORMATION_MESSAGE);
                udfClear();
                txtKapal.setText(""); lblKapal.setText("");
                lblKotaTujuan.setText("");
                lblTglBerangkat.setText("");

                if(isKoreksi)
                    txtNoNota.requestFocus();
                else
                    txtToko.requestFocus();
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaGabung.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String udfGetSerialNota() {
        String sSerial="";
        try{
            ResultSet rs=conn.createStatement().executeQuery("select fn_nota_get_serial_nota()");
            if(rs.next())sSerial=rs.getString(1);
            
        }catch(SQLException se){
            System.out.println("Error pada function getSerialNota");
        }
        return sSerial;
    }
    
    private void udfLookTable(){
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(txtMerk.getWidth());    //Merk
        jTable1.getColumnModel().getColumn(0).setMinWidth(txtMerk.getWidth());    //Merk
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(txtItemTrx.getWidth());    //Item transaksi
        jTable1.getColumnModel().getColumn(1).setMinWidth(txtItemTrx.getWidth());    //Item transaksi
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(txtQty.getWidth());    //Qty
        jTable1.getColumnModel().getColumn(2).setMinWidth(txtQty.getWidth());    //Qty
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(txtSatuan.getWidth());    //Satuan
        jTable1.getColumnModel().getColumn(3).setMinWidth(txtSatuan.getWidth());    //Satuan
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(txtKetSatuan.getWidth());    //Ket. Satuan
        jTable1.getColumnModel().getColumn(4).setMinWidth(txtKetSatuan.getWidth());    //Ket. Satuan
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(txtTarif.getWidth());    //Biaya Sat
        jTable1.getColumnModel().getColumn(5).setMinWidth(txtTarif.getWidth());    //Biaya Sat
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(txtSubTotal.getWidth());    //Sub Totals
        jTable1.getColumnModel().getColumn(6).setMinWidth(txtSubTotal.getWidth());    //Sub Totals
    }
    
    void setConn(Connection cn) {
        conn=cn;
    }

    void setKapal(String text) {
        srcKapal=text;
    }
    
    void setUserName(String s){
        sUserName=s;
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

    private void udfLookupKeterangan() {
        DlgLookupKeteranganNota fLookup=new DlgLookupKeteranganNota(JOptionPane.getFrameForComponent(this), true);
        fLookup.setCon(conn);
        fLookup.setSrcText(txtKeterangan);
        fLookup.setVisible(true);
    }

    private void udfPrint() {
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("no_nota", txtNoNota.getText());
            
            System.out.println("No. Nota: "+txtNoNota.getText());
            System.out.println(getClass().getResource(""));
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/nota_cetak.jasper"));
            
            
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            if(print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nota tidak ditemukan!");
                return;
            }
            JasperViewer.viewReport(print, false);
                           
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
    }
    
    private String udfGetSerialNotaKoreksi(){
        String sSerial="";
        String sQry="select no_nota, coalesce(nama_kota,'')  as kota, kode_nota " +
                "from nota inner join kota on kota.kode_kota=nota.kode_kota where no_nota='"+txtNoNota.getText()+"'";
        
        try{
            ResultSet rs=conn.createStatement().executeQuery("select count(*) from nota where no_nota='"+txtNoNota.getText()+"'");
            rs.next();
            if(rs.getInt(1)>1){
                FrmLookupKonfirmasi f1=new FrmLookupKonfirmasi(JOptionPane.getFrameForComponent(this), true);
                f1.setConn(conn);
                f1.setQuery(sQry);
                f1.setVisible(true);
                
                if(f1.udfIsSelected()){
                    Object[] obj = f1.getSelectedRs();
                        if (obj.length > 0) {
                            sSerial= obj[2].toString();
                            
                        }
                }
                
            }else{
                rs.close();
                rs=conn.createStatement().executeQuery("select kode_nota from nota where no_nota='"+txtNoNota.getText()+"'");
                
                if(rs.next()) sSerial=rs.getString(1);
                
                rs.close();
            }
            
        }catch(SQLException se){
            System.err.println("Error pada udfGetSerialNotaKoreksi()");
        }
        return sSerial;
        
    }
    
    void udfLoadDetailKoreksi() {
        sSerialNota=udfGetSerialNotaKoreksi();
        
        if(sSerialNota.trim().equalsIgnoreCase("")){
            //JOptionPane.showMessageDialog(this, "No. nota tidak ditemukan. Silakan pilih no nota lainnya!");
            txtNoNota.requestFocus();
            return;
        }
        if(txtNoNota.getText().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "No. nota tidak ditemukan. Silakan pilih no nota lainnya!");
            txtNoNota.requestFocus();
            return;
        }
        
        String sH="select tgl_nota, coalesce(catatan,'') as catatan, case when upper(kondisi)='PORT TO DOOR' then 0 else 1 end as kondisi, " +
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
                "where is_batal=false " + //is_header=false and 
                "and kode_nota='"+sSerialNota+"' "; //and no_nota='"+txtNoNota.getText()+"'"; //and coalesce(nota_header,'')='' 
        
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
                
                myModel.setNumRows(0);
                
                while(rsd.next()){
                    myModel.addRow(new Object[]{
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
            }else{
                JOptionPane.showMessageDialog(this, "Nota tersebut tidak ditemukan, coba periksa lagi!", "Message", JOptionPane.OK_OPTION);
//                udfClear();
//                txtKapal.setText(""); lblKapal.setText("");
//                lblKotaTujuan.setText("");
//                lblTglBerangkat.setText("");
                
                txtNoNota.requestFocus();
            }
            
            st.close();
            rsH.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNota080816.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FrmNota080816.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel12 = new javax.swing.JLabel();
        lblKapal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblKotaTujuan = new javax.swing.JLabel();
        lblSerialKapal = new javax.swing.JLabel();
        lblTglBerangkat = new javax.swing.JLabel();
        btnLookupPackingList = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtItemTrx = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        txtSatuan = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        lblTotalNota = new javax.swing.JLabel();
        txtTarif = new javax.swing.JTextField();
        txtKetSatuan = new javax.swing.JTextField();
        txtMerk = new javax.swing.JTextField();
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

        setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.inactiveTitleForeground"));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Nota tagihan");
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

        jPanel1.setBackground(new java.awt.Color(51, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroup1.add(radioPengirim);
        radioPengirim.setForeground(new java.awt.Color(255, 255, 255));
        radioPengirim.setText("Toko Pengirim");
        radioPengirim.setOpaque(false);
        radioPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioPengirimActionPerformed(evt);
            }
        });
        jPanel1.add(radioPengirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 62, -1, -1));

        buttonGroup1.add(radioToko);
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
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 89, 80, -1));

        txtNoNota.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
        jPanel1.add(txtKepada, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 113, 735, 24));

        txtToko.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
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

        txtKapal.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
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

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Tgl. Berangkat");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 9, 90, 20));

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);
        jPanel1.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 7, 320, 24));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("KAPAL");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 13, 80, -1));

        lblKotaTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaTujuan.setOpaque(true);
        jPanel1.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 35, 380, 24));

        lblSerialKapal.setText("jLabel3");
        jPanel1.add(lblSerialKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 35, 110, -1));

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

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(120);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 29, 780, 130));

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
        jPanel2.add(txtItemTrx, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 6, 247, 22));

        txtQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtQtyFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQtyFocusLost(evt);
            }
        });
        jPanel2.add(txtQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(349, 6, 60, 22));

        txtSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSatuanKeyReleased(evt);
            }
        });
        jPanel2.add(txtSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(409, 6, 80, 22));

        txtSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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
        jPanel2.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(663, 6, 120, 22));

        btnAdd.setText("+");
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
            }
        });
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(783, 4, -1, -1));

        jLabel5.setBackground(new java.awt.Color(0, 0, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("TOTAL    ");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel5.setOpaque(true);
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(566, 159, 100, 20));

        lblTotalNota.setBackground(new java.awt.Color(0, 0, 255));
        lblTotalNota.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblTotalNota.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalNota.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalNota.setText("0");
        lblTotalNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalNota.setOpaque(true);
        jPanel2.add(lblTotalNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(666, 159, 120, 20));

        txtTarif.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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
        jPanel2.add(txtTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(569, 6, 94, 22));

        txtKetSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKetSatuanKeyReleased(evt);
            }
        });
        jPanel2.add(txtKetSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(489, 6, 80, 22));

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
        jPanel2.add(txtMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 6, 100, 22));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 262, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-840)/2, (screenSize.height-561)/2, 840, 561);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtSubTotalActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isNotaOn=false;
        if(lst.isVisible())
            lst.setVisible(false);
        //dispose();
    }//GEN-LAST:event_formInternalFrameClosed

    private void radioTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTokoActionPerformed
        udfClear();
        
    }//GEN-LAST:event_radioTokoActionPerformed

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

    private void txtQtyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusLost
        txtQty.setText(fmt.format(udfGetFloat(txtQty.getText())));
        if(!txtQty.getText().equalsIgnoreCase("0")){
            txtSubTotal.setText(dFmt.format(udfGetFloat(txtQty.getText()) * udfGetFloat(txtTarif.getText()) ) );
        }else{
            txtQty.setText("");
        }
    }//GEN-LAST:event_txtQtyFocusLost

    private void txtSubTotalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubTotalFocusLost
        txtSubTotal.setText(fmt.format(udfGetFloat(txtSubTotal.getText())));
        
        if(udfGetFloat(txtSubTotal.getText())!=udfGetFloat(dFmt.format(udfGetFloat(txtQty.getText())*udfGetFloat(txtTarif.getText()))) && 
                !txtQty.getText().equalsIgnoreCase("")){
            
            txtQty.setText("");
            txtTarif.setText("");
        } 
}//GEN-LAST:event_txtSubTotalFocusLost

    private void txtTarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarifActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTarifActionPerformed

    private void txtTarifFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarifFocusLost
        txtTarif.setText(fmt.format(udfGetFloat(txtTarif.getText())));
        if(udfGetFloat(txtTarif.getText()) > 0){
            if(!txtQty.getText().trim().equalsIgnoreCase("")){
                System.out.println("Qty   : "+udfGetFloat(txtQty.getText()));
                System.out.println("Tarif : "+udfGetFloat(txtTarif.getText()));
                txtSubTotal.setText(dFmt.format(udfGetFloat(txtQty.getText()) * udfGetFloat(txtTarif.getText()) ) );
                
                
            }
        }else
            txtTarif.setText("");
        
}//GEN-LAST:event_txtTarifFocusLost

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

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        MyKeyListener kListener=new MyKeyListener();
        jDateNota.setFormats("dd-MM-yyyy");
        
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        lblKepada=new JLabel();
        
        MyRowRenderer rnd=new MyRowRenderer();
        
        for (int i=0;i<jTable1.getColumnCount();i++){
            jTable1.getColumnModel().getColumn(i).setCellRenderer(rnd);
        }
        
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
        
        for(int i=0;i<jPanel2.getComponentCount();i++){
            Component c = jPanel2.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JScrollPane") || c.getClass().getSimpleName().equalsIgnoreCase("JTable")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        
        txtKapal.setText(srcKapal);
        requestFocusInWindow();
        txtKepada.requestFocus();
        jTable1.getModel().addTableModelListener(new MyTableModelListener(jTable1));
        //udfLookTable();
        
        udfSetKeterangan();
        
        setStKoreksi();
        if(isKoreksi) txtNoNota.requestFocus();
        
    }//GEN-LAST:event_formInternalFrameOpened

    
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
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
    
    private void radioPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPengirimActionPerformed
        udfClear();
    }//GEN-LAST:event_radioPengirimActionPerformed

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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+jPanel2.getX()+ this.txtItemTrx.getX()+19, 
                                Utama.iTop+ this.getY()+jPanel2.getY()+this.txtItemTrx.getY() + txtItemTrx.getHeight()+77, 
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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+jPanel2.getX()+ this.txtSatuan.getX()+19, 
                                Utama.iTop+this.getY()+jPanel2.getY()+this.txtSatuan.getY() + txtSatuan.getHeight()+77, 
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

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER ||evt.getKeyCode()==KeyEvent.VK_SPACE ){
            udfAddItem();
        }
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        udfAddItem();
    }//GEN-LAST:event_btnAddMouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_F3){
            int iRow=jTable1.getSelectedRow();
            isEditRow=true;
            txtMerk.setText(myModel.getValueAt(iRow, 0).toString());
            txtItemTrx.setText(myModel.getValueAt(iRow, 1).toString());
            txtQty.setText(fmt.format((Float)myModel.getValueAt(iRow, 2)));
            txtSatuan.setText(myModel.getValueAt(iRow, 3).toString());
            txtKetSatuan.setText(myModel.getValueAt(iRow, 4).toString());
            txtTarif.setText(fmt.format((Float)myModel.getValueAt(iRow, 5)));
            txtSubTotal.setText(fmt.format((Float)myModel.getValueAt(iRow, 6)));
            txtMerk.requestFocus();
            return;
        }else if(evt.getKeyCode()==KeyEvent.VK_DELETE && jTable1.getSelectedRow()>=0){
            if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus baris ini?", "Expedisi", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                myModel.removeRow(jTable1.getSelectedRow());
            }
            return;
                
        }else{
            udfClearItem();
            isEditRow=true;
            return;
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if(isKoreksi)
            udfProsesKoreksi();
        else
            udfSave();
    }//GEN-LAST:event_btnSaveActionPerformed

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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+jPanel2.getX()+ this.txtKetSatuan.getX()+19, 
                                Utama.iTop+this.getY()+jPanel2.getY()+this.txtKetSatuan.getY() + txtKetSatuan.getHeight()+77, 
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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

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

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
        
}//GEN-LAST:event_txtKapalFocusLost

    private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed
        
}//GEN-LAST:event_txtKapalKeyPressed

    
    private void txtKapalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyReleased
//        String sQry="select distinct kon.kode_kapal, nama_kapal " +
//                                "from kontainer kon " +
//                                "inner join kapal on kapal.kode_kapal=kon.kode_kapal " +
//                                "where (kon.kode_kapal||nama_kapal) iLike '%"+txtKapal.getText()+"%' limit 100";
        try {
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKapal.setText(obj[0].toString());
                            lblKapal.setText(obj[1].toString());
                            lblTglBerangkat.setText(obj[2].toString());
                            lblKotaTujuan.setText(obj[3].toString());
                            lblSerialKapal.setText(obj[4].toString());
                            lblKotaNota.setText(obj[5].toString());
                            
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
                    lblTglBerangkat.setText("");
                    lblKotaTujuan.setText("");
                    lblSerialKapal.setText("");
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
                        String sQry="select tj.kode_kapal, nama_kapal , coalesce(to_Char(tgl_berangkat, 'dd-MM-yyyy'),'') as tgl_berangkat, " +
                                "coalesce(nama_kota,'') as kota_tujuan, serial_kode, kota_tujuan " +
                                "from nota_kapal_tujuan tj " +
                                "inner join kapal on kapal.kode_kapal=tj.kode_kapal " +
                                "left join kota on kode_kota=kota_tujuan " +
                                "where (tj.kode_kapal||nama_kapal ) iLike '%"+txtKapal.getText()+"%' and " +
                                "kota_tujuan like '%"+Utama.sKodeKota+"%' and " +
                                "active=true limit 100";
                        
                        //System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jPanel1.getX()+ this.txtKapal.getX()+19,
                                Utama.iTop + this.getY() + this.jPanel1.getY()+this.txtKapal.getY() + txtKapal.getHeight()+77,
                                txtKapal.getWidth()+lblToko.getWidth()+jLabel12.getWidth()+lblTglBerangkat.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal, lblTglBerangkat, lblKotaTujuan, lblSerialKapal, lblKotaNota});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblToko.getWidth());
                        lst.setColWidth(5, 0);
                        
                        lst.udfRemoveColumn(4);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKapal.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKapal.setText("");
                            lblKapal.setText("");
                            lblTglBerangkat.setText("");
                            lblKotaTujuan.setText("");
                            lblSerialKapal.setText("");
                            txtKapal.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKapalKeyReleased

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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jPanel2.getX()+ this.txtMerk.getX()+19,
                                Utama.iTop + this.getY() + this.jPanel2.getY()+this.txtMerk.getY() + txtMerk.getHeight()+77,
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

    private void txtSubTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubTotalKeyPressed
//        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
//            udfAddItem();
//        }
        
    }//GEN-LAST:event_txtSubTotalKeyPressed

    private void txtTokoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoFocusLost
        txtKepada.setText(lblKepada.getText());
    }//GEN-LAST:event_txtTokoFocusLost

    private void txtItemTrxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtItemTrxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtItemTrxActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
//        udfClear();
//        txtKapal.setText(""); lblKapal.setText("");
//        lblKotaTujuan.setText("");
//        lblTglBerangkat.setText("");
//        
//        if(isKoreksi)
//            txtNoNota.requestFocus();
//        else
//            txtToko.requestFocus();
        
        FrmLookupKonfirmasi f1=new FrmLookupKonfirmasi(JOptionPane.getFrameForComponent(this), true);
        f1.setConn(conn);
        f1.setQuery("select * from kapal");
        f1.setVisible(true);
                
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnGenerateNotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGenerateNotaMouseClicked
        if(!btnGenerateNota.getText().equalsIgnoreCase("Delete Nota")){
            if(txtToko.getText().trim().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(this, "Silakan isi Toko terlebih dulu!", "Nota message", JOptionPane.OK_OPTION);
                txtToko.requestFocus();
                return;
            }else if(lblKotaNota.getText().trim().equalsIgnoreCase("")){
                JOptionPane.showMessageDialog(this, "Kota tujuan nota masih belum dipilih\n" +
                        "Silakan pilih kembali kapal berangkat!", "Nota message", JOptionPane.OK_OPTION);
                txtKapal.requestFocus();
                return;
            }else{
                txtNoNota.setText(udfGetNewNota());

            }
        }else{
            if(myModel.getRowCount()>0)
                udfDeleteNota();
        }
        
}//GEN-LAST:event_btnGenerateNotaMouseClicked

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        if(!txtNoNota.getText().equalsIgnoreCase(""))
            udfPrint();
        
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        udfLookupKeterangan();
    }//GEN-LAST:event_jButton2MouseClicked

    private void btnLookupPackingListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookupPackingListActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_btnLookupPackingListActionPerformed

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
            Logger.getLogger(FrmNota080816.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
        
}//GEN-LAST:event_btnLookupPackingListMouseClicked

    private void txtNoNotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoNotaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER && isKoreksi){
            udfLoadDetailKoreksi();
        }
    }//GEN-LAST:event_txtNoNotaKeyPressed

    private void txtQtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQtyFocusGained
        lst.setVisible(false);
    }//GEN-LAST:event_txtQtyFocusGained

    private void txtTarifFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTarifFocusGained
        lst.setVisible(false);
    }//GEN-LAST:event_txtTarifFocusGained

    private void txtSubTotalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubTotalFocusGained
        lst.setVisible(false);
    }//GEN-LAST:event_txtSubTotalFocusGained
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmNota080816().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGenerateNota;
    private javax.swing.JButton btnLookupPackingList;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbKondisi;
    private javax.swing.JButton jButton2;
    private org.jdesktop.swingx.JXDatePicker jDateNota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JLabel lblKotaTujuan;
    private javax.swing.JLabel lblSerialKapal;
    private javax.swing.JLabel lblTglBerangkat;
    private javax.swing.JLabel lblToko;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JRadioButton radioPengirim;
    private javax.swing.JRadioButton radioToko;
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
        if(myModel.getRowCount()>0 && !isSave && !isKoreksi){
            if(JOptionPane.showConfirmDialog(this, "Data akan dihilangkan senelum disimpan?", "Konfirmasi", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            return;
            }
        }  
        udfClearItem();
        
        myModel.setNumRows(0);
        jTable1.setModel(myModel);

        txtKepada.setText("");
        //txtKapal.setText(""); lblKapal.setText("");
        txtToko.setText(""); lblToko.setText("");
        txtNoNota.setText("");
        lblTotalNota.setText("0");
        //lblKotaTujuan.setText("");
        //lblTglBerangkat.setText("");
        lblSerialKapal.setText("");
        lblKepada.setText("");
        
        isEditRow=false;
        isSave=false;
        
        
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
            if(evt.getSource().equals(btnAdd)) return;
            
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
        txtItemTrx.setText("");
        txtQty.setText("");
        txtSatuan.setText("");
        txtSubTotal.setText("");
        txtTarif.setText("");
        txtMerk.setText("");
        txtKetSatuan.setText("");
        
    }
    
    private void udfAddItem(){
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
            myModel.addRow(new Object[]{
                txtMerk.getText(),
                txtItemTrx.getText(),
                udfGetFloat(txtQty.getText()),
                txtSatuan.getText(),
                txtKetSatuan.getText(),
                udfGetFloat(txtTarif.getText()),
                udfGetFloat(txtSubTotal.getText())
            });
        }else{
            myModel.setValueAt(txtMerk.getText(), jTable1.getSelectedRow(), 0);
            myModel.setValueAt(txtItemTrx.getText(), jTable1.getSelectedRow(), 1);
            myModel.setValueAt((udfGetFloat(txtQty.getText())<=0? "": udfGetFloat(txtQty.getText())) , jTable1.getSelectedRow(), 2);
            myModel.setValueAt(txtSatuan.getText(), jTable1.getSelectedRow(), 3);
            myModel.setValueAt(txtKetSatuan.getText(), jTable1.getSelectedRow(), 4);
            myModel.setValueAt((udfGetFloat(txtTarif.getText())<=0? "": udfGetFloat(txtTarif.getText()) ), jTable1.getSelectedRow(), 5);
            myModel.setValueAt(udfGetFloat(txtSubTotal.getText()), jTable1.getSelectedRow(), 6);
        }
        
        udfClearItem();
        
        txtMerk.requestFocus();
    }
    
//    
    
    private void udfSetTotal(){
        int i=0;
        double total=0;
        
        
        for (i=0; i<myModel.getRowCount();i++){
            total = total+ udfGetFloat(myModel.getValueAt(i, 6).toString());
            
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
                 "and c.kota like '%"+Utama.sKodeKota+"%' " +
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
            String sTag="";
            if(radioToko.isSelected()){
                sTag="T";
            }else if(radioPengirim.isSelected()){
                sTag="P";
            }
            Statement stm=conn.createStatement();
            String sQry ="select fn_nota_get_new_kode_tagihan('"+txtToko.getText()+"', " +
                    "'"+fmtYMD.format(jDateNota.getDate())+"','"+sTag+"', '"+lblKotaNota.getText()+"')";
            
            ResultSet rs=stm.executeQuery(sQry);
            System.out.println(sQry);
            
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
        
        if(myModel.getRowCount()==0){
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
        Statement stDel;
        
        String sKoreksi="insert into nota_history(no_nota, tgl_nota, tagihan_per, customer, kepada, keterangan_nota, date_ins, user_ins, " +
                "date_upd, user_upd, is_batal, id, catatan, is_lunas, tgl_lunas, seri_kapal, kondisi, nota_header, is_header) " +
                "select no_nota, tgl_nota, tagihan_per, customer, kepada, keterangan_nota, date_ins, user_ins, " +
                "now(), '"+sUserName+"', is_batal, id, catatan, is_lunas, tgl_lunas, seri_kapal, kondisi, nota_header, is_header " +
                "from nota where no_nota='"+sOldNota+"' ;";
        
        sKoreksi+="insert into nota_history_detail(no_nota, merk, item_trx, ukuran, satuan, ket_satuan, tarif, sub_total) " +
                "select no_nota, merk, item_trx, ukuran, satuan, ket_satuan, tarif, sub_total from nota_detail " +
                "where no_nota='"+sOldNota+"';";
        
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
            for (int s=0; s<myModel.getRowCount(); s++){
                
                sInsert+="Insert into nota_detail(no_nota, merk, item_trx, satuan, ket_satuan, ukuran, tarif, sub_total) values " +
                        "('"+txtNoNota.getText()+"', '"+ myModel.getValueAt(s, 0).toString()+"', " +
                        "'"+myModel.getValueAt(s, 1).toString()+"', '"+myModel.getValueAt(s, 3).toString()+"', " +
                        "'"+myModel.getValueAt(s, 4).toString()+"', " +
                        (udfGetFloat(myModel.getValueAt(s, 2).toString())>0? udfGetFloat(myModel.getValueAt(s, 2).toString()) :null)+", " +
                        (udfGetFloat(myModel.getValueAt(s, 5).toString())>0? udfGetFloat(myModel.getValueAt(s, 5).toString()) :null)+", " +
                        udfGetFloat(myModel.getValueAt(s, 6).toString())+"); " ;
            }
            
            stD.executeUpdate(sInsert);
            
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
                Logger.getLogger(FrmNota080816.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
    }
    
    private void udfSave(){
        if(txtKapal.getText().trim().equalsIgnoreCase("") || lblKotaNota.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi/ pilih kapal terlebih dulu!");
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
        
        if(myModel.getRowCount()==0){
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
        Statement stDel;
        
        try {
            conn.setAutoCommit(false);
            
            stDel=conn.createStatement();
            //int i=stDel.executeUpdate("Delete from nota_detail where no_nota='"+txtNoNota.getText()+"' ");
            int i=stDel.executeUpdate("Delete from nota_detail where kode_nota='"+sSerialNota+"' ");
            
            stH = conn.createStatement();
            //rsH=stH.executeQuery("select * from nota where no_nota='"+txtNoNota.getText()+"'");
            rsH=stH.executeQuery("select * from nota where kode_nota='"+sSerialNota+"'");
            
            if(!rsH.next()){
                isEdit=false;
                rsH.moveToInsertRow();
            }else{
                isEdit=true;
            }
            
            String sSerialNota=udfGetSerialNota();
            rsH.updateString("kode_nota", sSerialNota);
            rsH.updateString("kode_kota", lblKotaNota.getText());
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
            
            if(isEdit)
                rsH.updateRow();
            else
                rsH.insertRow();
            
            stD=conn.createStatement();
            String sInsert="Delete from nota_detail where kode_nota='"+sSerialNota+"'; ";
            for (int s=0; s<myModel.getRowCount(); s++){
               
                sInsert+="Insert into nota_detail(kode_nota, no_nota, merk, item_trx, satuan, ket_satuan, ukuran, tarif, sub_total) values " +
                        "('"+sSerialNota+"','"+txtNoNota.getText()+"', '"+ myModel.getValueAt(s, 0).toString()+"', " +
                        "'"+myModel.getValueAt(s, 1).toString()+"', '"+myModel.getValueAt(s, 3).toString()+"', " +
                        "'"+myModel.getValueAt(s, 4).toString()+"', " +
                        (udfGetFloat(myModel.getValueAt(s, 2).toString())>0? udfGetFloat(myModel.getValueAt(s, 2).toString()) :null)+", " +
                        (udfGetFloat(myModel.getValueAt(s, 5).toString())>0? udfGetFloat(myModel.getValueAt(s, 5).toString()) :null)+", " +
                        udfGetFloat(myModel.getValueAt(s, 6).toString())+"); " ;
            }
            
            stD.executeUpdate(sInsert);
            
            conn.setAutoCommit(true);
            isSave=true;
            
            if(JOptionPane.showConfirmDialog(this, "Simpan data sukses! Selanjutnya apakah akan dicetak?", "Print confirmation", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                udfPrint();
            }
            
            //udfClear();
            
            
        } catch (SQLException ex) {
            try {
                JOptionPane.showMessageDialog(this, "Simpan data gagal!\n"+ex.getMessage());
                conn.rollback();
                
            } catch (SQLException ex1) {
                Logger.getLogger(FrmNota080816.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
        
    }
}
