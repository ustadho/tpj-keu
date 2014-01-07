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

import com.ustasoft.Model.Pembayaran;
import com.ustasoft.Model.PembayaranDetail;
import com.ustasoft.Model.PembayaranDetailDetail;
import com.ustasoft.services.BayarNotaServices;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import main.GeneralFunction;

/**
 *
 * @author cak-ust
 */
public class DlgBayarSatuNota extends javax.swing.JDialog {
    private Connection conn;
    private String sNota="";
    List jenisBayar=new ArrayList();
    private boolean toTujuan=true;
    private Object srcForm;
    private String sNoBayarKoreksi="";
    private GeneralFunction fn=new GeneralFunction();
    BayarNotaServices service;

    /** Creates new form DlgNotaClaim */
    public DlgBayarSatuNota(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                double total=0;
                int col=jTable1.getColumnModel().getColumnIndex("Bayar");
                for(int i=0; i<jTable1.getRowCount(); i++){
                    total+=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, col));
                }
                lblTotalPembayaran.setText(GeneralFunction.intFmt.format(total));
            }
        });
        jTable1.setRowHeight(22);
    }

    public void setSrcForm(Object s){
        this.srcForm=s;
    }
    
    public void setConn(Connection con) {
        this.conn=con;
        service=new BayarNotaServices(conn);
    }

    public void tampilkanNota(String s){
        String sQry="select * from fn_nota_show_claim('"+s+"') as "
                + "(no_nota varchar, kepada varchar, customer varchar, "
                + "total double precision, klaim double precision, "
                + "lain2 double precision, pelunasan double precision, "
                + "lebih double precision, ket_klaim text, kode_cust varchar, to_tujuan boolean)";

        try{
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            if(rs.next()){
                lblNota.setText(rs.getString("no_nota"));
                lblKepada.setText(rs.getString("kepada"));
                lblPelanggan.setText(rs.getString("customer"));
                lblKodePelanggan.setText(rs.getString("kode_cust"));
                lblJumlah.setText(GeneralFunction.intFmt.format(rs.getDouble("total")));
                lblPelunasan.setText(GeneralFunction.intFmt.format(rs.getDouble("pelunasan")));
                lblKlaim.setText(GeneralFunction.intFmt.format(rs.getDouble("klaim")));
                lblLain2.setText(GeneralFunction.intFmt.format(rs.getDouble("lain2")));
                double piutang=rs.getDouble("total")-rs.getDouble("pelunasan")-rs.getDouble("klaim")-rs.getDouble("lain2")+rs.getDouble("lebih");
                lblPiutang.setText(GeneralFunction.intFmt.format(piutang));
                lblLebih.setText("");
                
                udfLoadNotaDetail();
            }
            rs.close();
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }

    private boolean udfCekBeforeSave(){
        if(GeneralFunction.udfGetInt(txtJumlah.getText()) == 0){
            JOptionPane.showMessageDialog(this, "Masukkan jumlah pembayaran terlebih dulu!");
            if(!txtJumlah.isFocusOwner())
                txtJumlah.requestFocus();
            return false;
        }

        return true;
    }

    private void udfSave(){
        if(!udfCekBeforeSave())
            return;
        try{
            conn.setAutoCommit(false);
            String notaKoreksi="";
            
            if(sNoBayarKoreksi.length()>0){
                notaKoreksi=service.koreksiPembayaran(sNoBayarKoreksi);
                System.out.println("Hasil korkesi :"+notaKoreksi);
            }
                        
            Pembayaran bayar=new Pembayaran();
            String sNoBayar=service.getNoBayar(jDateBayar.getDate());
            System.out.println("Dapet nomor :"+sNoBayar);
            bayar.setNo_bayar(sNoBayar);
            bayar.setKode_cust(lblKodePelanggan.getText());
            bayar.setAlat_bayar(jenisBayar.get(cmbAlatBayar.getSelectedIndex()).toString());
            bayar.setCheck_amount(GeneralFunction.udfGetDouble(txtJumlah.getText()));
            bayar.setCheck_no(txtNoBukti.getText());
            bayar.setDate_trx(jDateBayar.getDate());
            double nett=GeneralFunction.udfGetDouble(lblJumlah.getText())
                    -GeneralFunction.udfGetDouble(lblKlaim.getText())
                    -GeneralFunction.udfGetDouble(lblLain2.getText())
                    -GeneralFunction.udfGetDouble(lblPelunasan.getText());

            bayar.setIs_lunas(GeneralFunction.udfGetDouble(txtJumlah.getText())>=nett);
            bayar.setTanggal(jDateBayar.getDate());
            bayar.setTgl_lunas(GeneralFunction.udfGetDouble(txtJumlah.getText())>=nett ? jDateBayar.getDate(): null);
            bayar.setUser_tr(Utama.sUserName);
            bayar.setTo_tujuan(toTujuan);
            bayar.setMemo(txtKeterangan.getText());
            service.savePembayaran(bayar);

            List<PembayaranDetail> detail=new ArrayList<PembayaranDetail>();
            PembayaranDetail pembayaran=new PembayaranDetail();
            pembayaran.setNo_bayar(sNoBayar);
            pembayaran.setNo_nota(lblNota.getText());
            pembayaran.setJumlah(GeneralFunction.udfGetDouble(txtJumlah.getText()));
            detail.add(pembayaran);
            service.savePembayaranDetail(detail);

            double jmlBayar=0;
            List<PembayaranDetailDetail> detailNota=new ArrayList<PembayaranDetailDetail>();
            for(int i=0; i<jTable1.getRowCount(); i++){
                jmlBayar=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Bayar")));

                PembayaranDetailDetail dd=new PembayaranDetailDetail();
                dd.setNoBayar(sNoBayar);
                dd.setNoNota(lblNota.getText());
                dd.setNoNotaDet(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("No. Nota")).toString());
                dd.setBayar(jmlBayar);
                detailNota.add(dd);
            }
            
            service.savePembayaranDetailDetail(detailNota);
            service.simpanLebih(lblNota.getText(), sNoBayar,
                    GeneralFunction.udfGetDouble(lblLebih.getText()));
            
            
            conn.setAutoCommit(true);
            if(srcForm!=null && srcForm instanceof FrmListTagihanPerCust)
                    ((FrmListTagihanPerCust)srcForm).udfLoadTagihan(lblNota.getText());
            else if(srcForm!=null && srcForm instanceof FrmListPembayaranFilter)
                    ((FrmListPembayaranFilter)srcForm).udfFilter(sNoBayar);
            JOptionPane.showMessageDialog(this, "Simpan data sukses");
            this.dispose();

        } catch (SQLException ex) {
            try {
                conn.setAutoCommit(false);
                conn.rollback();
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(DlgBayarSatuNota.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    private void udfLoadNotaDetail(){
        String sQry="select * from fn_nota_pembayaran_detail('"+lblNota.getText()+"') as (no_gabung varchar, no_nota varchar, "
                + "kapal varchar, tgl_berangkat date, kota_tujuan varchar, total_tagihan double precision, "
                + "tot_bayar double precision)";
        ((DefaultTableModel)jTable1.getModel()).setNumRows(0);
        try{
            double piutang=0;
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            while(rs.next()){
                ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{
                    rs.getString("no_nota"),
                    rs.getString("kapal"),
                    rs.getDate("tgl_berangkat"),
                    rs.getDouble("total_tagihan"),
                    rs.getDouble("tot_bayar")
                });
                piutang+=(rs.getDouble("total_tagihan")-rs.getDouble("tot_bayar"));
            }
            rs.close();
            lblTotalPiutang.setText(GeneralFunction.intFmt.format(piutang));
            if(jTable1.getRowCount()>0){
                jTable1.setRowSelectionInterval(0, 0);
            }
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
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
        lblJumlah = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblKepada = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblPelunasan = new javax.swing.JLabel();
        lblKlaim = new javax.swing.JLabel();
        lblLain2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblKetKlaim = new javax.swing.JLabel();
        lblKodePelanggan = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbAlatBayar = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtNoBukti = new javax.swing.JTextField();
        jDateBayar = new org.jdesktop.swingx.JXDatePicker();
        jDateCek = new org.jdesktop.swingx.JXDatePicker();
        jLabel16 = new javax.swing.JLabel();
        lblPiutang = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblLebih = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        panel1 = new java.awt.Panel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        lblTotalPembayaran = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalPiutang = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Klaim Dll."); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("No. Nota :");
        jLabel1.setName("jLabel1"); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 20));

        lblNota.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblNota.setName("lblNota"); // NOI18N
        getContentPane().add(lblNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 310, 20));

        lblPelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPelanggan.setName("lblPelanggan"); // NOI18N
        getContentPane().add(lblPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 320, 20));

        jLabel4.setText("Pelanggan :");
        jLabel4.setName("jLabel4"); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));

        lblJumlah.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblJumlah.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblJumlah.setName("lblJumlah"); // NOI18N
        getContentPane().add(lblJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 100, 20));

        jLabel7.setText("Jumlah :");
        jLabel7.setName("jLabel7"); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 70, 20));

        lblKepada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblKepada.setName("lblKepada"); // NOI18N
        getContentPane().add(lblKepada, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 370, 20));

        jLabel6.setText("Kepada :");
        jLabel6.setName("jLabel6"); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 20));

        jLabel9.setText("Lain-lain");
        jLabel9.setName("jLabel9"); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 70, 20));

        jLabel10.setText("Pelunasan");
        jLabel10.setName("jLabel10"); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 70, 20));

        lblPelunasan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPelunasan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPelunasan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPelunasan.setName("lblPelunasan"); // NOI18N
        getContentPane().add(lblPelunasan, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 100, 20));

        lblKlaim.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblKlaim.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKlaim.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblKlaim.setName("lblKlaim"); // NOI18N
        getContentPane().add(lblKlaim, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 100, 20));

        lblLain2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLain2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLain2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblLain2.setName("lblLain2"); // NOI18N
        getContentPane().add(lblLain2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 100, 20));

        jLabel8.setText("Klaim:");
        jLabel8.setName("jLabel8"); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 70, 20));

        jLabel15.setText("Ket. Klaim :");
        jLabel15.setName("jLabel15"); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 70, 20));

        lblKetKlaim.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblKetKlaim.setName("lblKetKlaim"); // NOI18N
        getContentPane().add(lblKetKlaim, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 370, 20));

        lblKodePelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblKodePelanggan.setName("lblKodePelanggan"); // NOI18N
        getContentPane().add(lblKodePelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 50, 20));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Bayar :");
        jLabel5.setName("jLabel5"); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 70, 30));

        cmbAlatBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAlatBayar.setName("cmbAlatBayar"); // NOI18N
        jPanel1.add(cmbAlatBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 280, -1));

        jLabel12.setText("Alat Bayar :");
        jLabel12.setName("jLabel12"); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 20));

        jLabel13.setText("Tanggal");
        jLabel13.setName("jLabel13"); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 70, 20));

        txtJumlah.setBackground(new java.awt.Color(204, 255, 255));
        txtJumlah.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtJumlah.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtJumlah.setName("txtJumlah"); // NOI18N
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });
        jPanel1.add(txtJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 130, 30));

        jLabel14.setText("Piutang");
        jLabel14.setName("jLabel14"); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, 70, 20));

        txtNoBukti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNoBukti.setName("txtNoBukti"); // NOI18N
        jPanel1.add(txtNoBukti, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 240, 20));

        jDateBayar.setName("jDateBayar"); // NOI18N
        jPanel1.add(jDateBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 35, 130, -1));

        jDateCek.setName("jDateCek"); // NOI18N
        jPanel1.add(jDateCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 120, -1));

        jLabel16.setText("No. Bukti");
        jLabel16.setName("jLabel16"); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));

        lblPiutang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPiutang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPiutang.setName("lblPiutang"); // NOI18N
        jPanel1.add(lblPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 85, 110, 20));

        jLabel17.setText("Lebih");
        jLabel17.setName("jLabel17"); // NOI18N
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 70, 20));

        lblLebih.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLebih.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLebih.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblLebih.setName("lblLebih"); // NOI18N
        jPanel1.add(lblLebih, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 110, 20));

        jLabel18.setText("Keterangan:");
        jLabel18.setName("jLabel18"); // NOI18N
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 135, 70, 20));

        txtKeterangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKeterangan.setName("txtKeterangan"); // NOI18N
        jPanel1.add(txtKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 135, 360, 20));

        jTabbedPane1.addTab("Pembayaran", jPanel1);

        panel1.setName("panel1"); // NOI18N
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Nota", "Kapal", "Tgl Brkt", "Total", "Terbayar", "Bayar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);

        panel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 130));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Total Pembayaran");
        jLabel2.setName("jLabel2"); // NOI18N
        panel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 120, 20));

        lblTotalPembayaran.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPembayaran.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPembayaran.setText("0");
        lblTotalPembayaran.setName("lblTotalPembayaran"); // NOI18N
        panel1.add(lblTotalPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 110, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Piutang");
        jLabel3.setName("jLabel3"); // NOI18N
        panel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 70, 20));

        lblTotalPiutang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPiutang.setText("0");
        lblTotalPiutang.setName("lblTotalPiutang"); // NOI18N
        panel1.add(lblTotalPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 90, 20));

        jTabbedPane1.addTab("Detail Pembayaran", panel1);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 470, 190));

        jButton1.setText("Save");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 350, 70, -1));

        jButton2.setText("Cancel");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 350, 80, -1));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-505)/2, (screenSize.height-416)/2, 505, 416);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        udfSave();
}//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
}//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        udfWindowOpened();
    }//GEN-LAST:event_formWindowOpened
    double lebih=0;
    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        GeneralFunction.keyTyped(evt);
        
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void postingPembayaran(){
        double totalPiutang=GeneralFunction.udfGetDouble(lblTotalPiutang.getText());
        double sisa=GeneralFunction.udfGetDouble(txtJumlah.getText());
        double sisaPiutang=0;
        for(int i=0; i<jTable1.getRowCount(); i++){
            sisaPiutang=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Total")))-
                    GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Terbayar")));
            jTable1.setValueAt(sisaPiutang<sisa? sisaPiutang:sisa, i, jTable1.getColumnModel().getColumnIndex("Bayar"));
            sisa-=sisaPiutang;
        }
    }
    
    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        lebih=GeneralFunction.udfGetDouble(txtJumlah.getText())-
                GeneralFunction.udfGetDouble(lblPiutang.getText());
        if(lebih>0)
            lblLebih.setText(GeneralFunction.intFmt.format(lebih));
        else
            lblLebih.setText("0");
        
        postingPembayaran();
    }//GEN-LAST:event_txtJumlahKeyReleased

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgBayarSatuNota dialog = new DlgBayarSatuNota(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox cmbAlatBayar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private org.jdesktop.swingx.JXDatePicker jDateBayar;
    private org.jdesktop.swingx.JXDatePicker jDateCek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblKepada;
    private javax.swing.JLabel lblKetKlaim;
    private javax.swing.JLabel lblKlaim;
    private javax.swing.JLabel lblKodePelanggan;
    private javax.swing.JLabel lblLain2;
    private javax.swing.JLabel lblLebih;
    private javax.swing.JLabel lblNota;
    private javax.swing.JLabel lblPelanggan;
    private javax.swing.JLabel lblPelunasan;
    private javax.swing.JLabel lblPiutang;
    private javax.swing.JLabel lblTotalPembayaran;
    private javax.swing.JLabel lblTotalPiutang;
    private java.awt.Panel panel1;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtNoBukti;
    // End of variables declaration//GEN-END:variables

    private void udfWindowOpened() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("select * from nota_alat_pembayaran ");
            cmbAlatBayar.removeAllItems();
            while(rs.next()){
                jenisBayar.add(rs.getString(1));
                cmbAlatBayar.addItem(rs.getString(2));
            }
            rs.close();
            
            if(sNoBayarKoreksi.length()>0){
                udfLoadKoreksiBayar();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DlgBayarSatuNota.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void setNoBayar(String sNoBayar) {
        this.sNoBayarKoreksi = sNoBayar;
    }

    private void udfLoadKoreksiBayar() {
        try {
            String sQry="select d.no_nota, nota.kepada, nota.customer as kode_cust, coalesce(c.nama, coalesce(t.nama,'')) as pelanggan, "
                    + "sum(nd.sub_total) as total, coalesce(nota.pelunasan,0) as pelunasan, coalesce(nota.klaim,0) as klaim,  "
                    + "coalesce(nota.lain2,0) as lain2, coalesce(nota.ket_klaim,'') as ket_klaim "
                    + "from nota_pembayaran_detail d "
                    + "inner join nota on nota.no_nota=d.no_nota "
                    + "inner join nota_detail nd on nd.no_nota=nota.no_nota "
                    + "left join customer c on c.kode_cust=nota.customer and nota.tagihan_per='T' "
                    + "left join toko t on t.kode_toko=nota.customer and nota.tagihan_per='P' "
                    + "where d.no_bayar='"+sNoBayarKoreksi+"' "
                    + "group by d.no_nota, nota.kepada, nota.customer, coalesce(c.nama, coalesce(t.nama,'')), "
                    + "coalesce(nota.pelunasan,0), coalesce(nota.klaim,0),  "
                    + "coalesce(nota.lain2,0), coalesce(nota.ket_klaim,'')"; 
            double totPutang=0;        
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            if(rs.next()){
                lblNota.setText(rs.getString("no_nota"));
                lblKepada.setText(rs.getString("kepada"));
                lblKodePelanggan.setText(rs.getString("kode_cust"));
                lblPelanggan.setText(rs.getString("pelanggan"));
                lblJumlah.setText(fn.intFmt.format(rs.getDouble("total")));
                lblPelunasan.setText(fn.intFmt.format(rs.getDouble("pelunasan")));
                lblKlaim.setText(fn.intFmt.format(rs.getDouble("klaim")));
                lblLain2.setText(fn.intFmt.format(rs.getDouble("lain2")));
                lblKetKlaim.setText(rs.getString("ket_klaim"));
                totPutang=rs.getDouble("total")-rs.getDouble("klaim")-rs.getDouble("lain2");
                rs.close();
                
                sQry="select p.tanggal, coalesce(a.alat_pembayaran,'') as alat_pembayaran, "
                        + "coalesce(p.check_no,'') as no_bukti, p.tgl_cek,  "
                        + "coalesce(p.check_amount,0) as jml_bayar, coalesce(p.memo,'') as ket_bayar "
                        + "from nota_pembayaran p  "
                        + "left join nota_alat_pembayaran a on a.kode=p.alat_bayar "
                        + "where no_bayar='"+sNoBayarKoreksi+"'";
                rs=conn.createStatement().executeQuery(sQry);
                if(rs.next()){
                    cmbAlatBayar.setSelectedItem(rs.getString("alat_pembayaran"));
                    txtNoBukti.setText(rs.getString("no_bukti"));
                    jDateCek.setDate(rs.getDate("tgl_cek"));
                    txtJumlah.setText(fn.intFmt.format(rs.getDouble("jml_bayar")));
                    txtKeterangan.setText(rs.getString("ket_bayar"));
                }
                rs.close();
                
                ((DefaultTableModel)jTable1.getModel()).setNumRows(0);
                 sQry="select  nd.no_nota, dd.no_nota_det, coalesce(kp.nama_kapal,'') as nama_kapal, kt.tgl_berangkat, "
                        + "sum(nd.sub_total) as tot_nota, coalesce(dd.jumlah,0) as jml_bayar "
                        + "from nota_pembayaran_Detail_detail dd  "
                        + "inner join nota on nota.no_nota=dd.no_nota_Det "
                        + "inner join nota_detail nd on nd.no_nota=dd.no_nota_Det "
                        + "inner join nota_kapal_tujuan kt on kt.serial_kode=nota.seri_kapal "
                        + "inner join kapal kp on kp.kode_kapal=kt.kode_kapal "
                        + "where dd.no_bayar='"+sNoBayarKoreksi+"' "
                        + "group by nd.no_nota, dd.no_nota_det, coalesce(kp.nama_kapal,''), kt.tgl_berangkat, coalesce(dd.jumlah,0) "
                        + "order by nd.no_nota";
                rs=conn.createStatement().executeQuery(sQry);
                if(rs.next()){
                    ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{
                        rs.getString("no_nota_det"), 
                        rs.getString("nama_kapal"), 
                        rs.getDate("tgl_berangkat"), 
                        rs.getDouble("tot_nota"), 
                        0, 
                        rs.getDouble("jml_bayar")
                    });
                }
                rs.close();
                
                lblPiutang.setText(fn.intFmt.format(
                        totPutang-
                        service.telahBayarSelainKoreksi(lblNota.getText(), sNoBayarKoreksi)));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        
    }

}
