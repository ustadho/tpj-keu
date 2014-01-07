/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgBayarBanyakNota.java
 *
 * Created on Jan 31, 2013, 5:40:39 AM
 */

package tpjkeuangan;

import com.ustasoft.Model.Pembayaran;
import com.ustasoft.Model.PembayaranDetail;
import com.ustasoft.Model.PembayaranDetailDetail;
import com.ustasoft.services.BayarNotaServices;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import main.GeneralFunction;
import main.TextAreaRenderer;

/**
 *
 * @author cak-ust
 */
public class DlgBayarBanyakNota extends javax.swing.JDialog {
    private Connection conn;
    GeneralFunction fn=new GeneralFunction();
    List jenisBayar=new ArrayList();
    private String sCustID="";
    boolean toTujuan=true;
    private Object srcForm;
    private String sNoBayarKoreksi="";
    BayarNotaServices service;
    private String sTahun;

    /** Creates new form DlgBayarBanyakNota */
    public DlgBayarBanyakNota(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        tblNotaTagihan.setRowHeight(30);
        for (int i=0;i<tblNotaTagihan.getColumnCount();i++){
            if(i==1 || i==2)
                tblNotaTagihan.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
            else
                tblNotaTagihan.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
        tblNotaTagihan.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.INSERT){ // && e.getColumn()==tblNotaTagihan.getColumnModel().getColumnIndex("PIUTANG")
                    double totalPiutang=0;
                    int colPiutang=tblNotaTagihan.getColumnModel().getColumnIndex("PIUTANG");
                    for(int i=0; i<tblNotaTagihan.getRowCount(); i++){
                        totalPiutang+=GeneralFunction.udfGetDouble(tblNotaTagihan.getValueAt(i, colPiutang));
                    }
                    lblTotPiutang.setText(GeneralFunction.intFmt.format(totalPiutang));
                }
                if(e.getType()==TableModelEvent.UPDATE && e.getColumn()==tblNotaTagihan.getColumnModel().getColumnIndex("BAYAR")){
//                    int iRow=e.getFirstRow();
//                    double totalPiutang=GeneralFunction.udfGetDouble(tblNotaTagihan.getValueAt(iRow, 
//                            tblNotaTagihan.getColumnModel().getColumnIndex("PIUTANG")));
                    for(int a=0; a<tblNotaTagihan.getRowCount(); a++){
                        double sisa=GeneralFunction.udfGetDouble(tblNotaTagihan.getValueAt(a, 
                                tblNotaTagihan.getColumnModel().getColumnIndex("BAYAR")));
                        double sisaPiutang=0;
                        String sNotaGabung=tblNotaTagihan.getValueAt(a, tblNotaTagihan.getColumnModel().getColumnIndex("NO NOTA")).toString();

                        for(int i=0; i<jTable1.getRowCount(); i++){
                            if(!jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Nota Gabung")).toString().equalsIgnoreCase(sNotaGabung))
                                continue;

                            sisaPiutang=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Total")))-
                                    GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Terbayar")));
                            jTable1.setValueAt(sisaPiutang<=sisa? sisaPiutang:sisa, i, jTable1.getColumnModel().getColumnIndex("Bayar"));
                            sisa-=sisaPiutang;
                        }
                    }
                }
            }
        });
        
        jTable1.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                double total=0, piutang=0;
                int colBayar=jTable1.getColumnModel().getColumnIndex("Bayar");
                int colJumlah=jTable1.getColumnModel().getColumnIndex("Total");
                int colTerbayar=jTable1.getColumnModel().getColumnIndex("Terbayar");
                
                for(int i=0; i<jTable1.getRowCount(); i++){
                    total+=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, colBayar));
                    piutang+=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, colJumlah))-
                            GeneralFunction.udfGetDouble(jTable1.getValueAt(i, colTerbayar));
                }
                lblTotalPiutang.setText(GeneralFunction.intFmt.format(piutang));
                lblTotalPembayaran.setText(GeneralFunction.intFmt.format(total));
            }
        });
        jTable1.setRowHeight(22);
    }

    public void setSrcForm(Object s){
        srcForm=s;
    }
    
    public void setCustID(String s, boolean b){
        sCustID=s;
        toTujuan=b;
    }

    public void setConn(Connection con){
        this.conn=con;
        fn.setConn(conn);
        service=new BayarNotaServices(conn);
    }

    private void postingPembayaran(String sNotaHeader, double jmlBayar){
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
    
    public void tampilkanNota(List nota, String sCust, String tagihanPer, String tahun, String kota){
        String sQry="";
        String sNota="";
        for(int i=0; i<nota.size(); i++){
            sNota+=sNota.length()>2?",'": "";
            sNota+=nota.get(i).toString()+"'";
        }
        sNota=sNota.substring(0, sNota.length()-1);
        
        System.out.println("Nota :"+sNota);
        sQry=   "select * from fn_nota_rekap_per_cust_per_tahun3('"+sCust+"', "
                + "'"+tagihanPer+"', '"+tahun+"', '"+kota+"') "
                + "as (kepada varchar, merk text, no_nota varchar, nama_kapal text, tgl_berangkat text, "
                + "total double precision, is_header boolean, tgl_lunas text, pelunasan double precision, "
                + "klaim double precision, lebih double precision, lain2 double precision, piutang double precision, keterangan  text, "
                + "alat_pembayaran text, no_bayar text) "
                + "where no_nota in('"+sNota+"')";

        System.out.println(sQry);
        int i=1;
        try {
            ((DefaultTableModel)tblNotaTagihan.getModel()).setNumRows(0);

            ResultSet rs = conn.createStatement().executeQuery(sQry);
            double piutang=0;
            System.out.println(sQry);
            while (rs.next()) {
                piutang=rs.getDouble("total")-rs.getDouble("pelunasan")-rs.getDouble("klaim")-rs.getDouble("lain2")+rs.getDouble("lebih");
                ((DefaultTableModel)tblNotaTagihan.getModel()).addRow(new Object[]{
                    rs.getString("no_nota"),
                    rs.getString("nama_kapal"),
                    rs.getString("tgl_berangkat"),
                    rs.getDouble("total"),
                    rs.getDouble("pelunasan"),
                    rs.getDouble("klaim"),
                    rs.getDouble("lebih"),
                    rs.getDouble("lain2"),
                    piutang,
                    0
                });
                i++;
            }
            if(tblNotaTagihan.getRowCount()>0){
                tblNotaTagihan.setRowSelectionInterval(0, 0);
                tblNotaTagihan.setModel((DefaultTableModel)fn.autoResizeColWidth(tblNotaTagihan,
                        (DefaultTableModel)tblNotaTagihan .getModel()).getModel());
                
                rs.close();
                ((DefaultTableModel)jTable1.getModel()).setNumRows(0);
                sQry="select * from fn_nota_pembayaran_detail(?) as (nota_gabung varchar, no_nota varchar, "
                        + "nama_kapal varchar, tgl_berangkat date, kota_tujuan varchar, total_tagihan double precision, "
                        + "tot_bayar double precision)";
                System.out.println(sQry);
                PreparedStatement ps=conn.prepareStatement(sQry);
                ps.setString(1, sNota);
                rs=ps.executeQuery();
                while(rs.next()){
                    ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{
                        rs.getString("nota_gabung"), 
                        rs.getString("no_nota"), 
                        rs.getString("nama_kapal"), 
                        rs.getDate("tgl_berangkat"),
                        rs.getDouble("total_tagihan"),
                        rs.getDouble("tot_bayar")
                    });
                }
                if(jTable1.getRowCount()>0){
                    jTable1.setModel((DefaultTableModel)fn.autoResizeColWidth(jTable1,
                        (DefaultTableModel)jTable1 .getModel()).getModel());
                }
            }else
                JOptionPane.showMessageDialog(this, "Daftar nota tidak ditemukan");
//            if(!txtCustomer.isFocusOwner())
//                txtCustomer.requestFocusInWindow();
            rs.close();

        } catch (SQLException eswl){
            System.err.println(eswl.getMessage());
        }
        if(tblNotaTagihan.getRowCount()>0){
            tblNotaTagihan.requestFocusInWindow();
            tblNotaTagihan.setRowSelectionInterval(0,0);
            tblNotaTagihan.setModel((DefaultTableModel)fn.autoResizeColWidth(tblNotaTagihan, (DefaultTableModel)tblNotaTagihan.getModel()).getModel());

            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) tblNotaTagihan.getColumnModel();

        }
    }

    private void clearPembayaranKoreksi(){
        int col=tblNotaTagihan.getColumnModel().getColumnIndex("PIUTANG");
        int colBayar=tblNotaTagihan.getColumnModel().getColumnIndex("BAYAR");
        int colLebih=tblNotaTagihan.getColumnModel().getColumnIndex("LEBIH");
        for(int i=0; i<tblNotaTagihan.getRowCount(); i++){
            tblNotaTagihan.setValueAt(0, i, colBayar);
            tblNotaTagihan.setValueAt(0, i, colLebih);
        }
        colBayar=jTable1.getColumnModel().getColumnIndex("Bayar");
        for(int i=0; i<jTable1.getRowCount(); i++){
            jTable1.setValueAt(0, i, colBayar);
        }
    }
    
    private void udfPostBayar(){
        double bayar=GeneralFunction.udfGetDouble(txtJumlah.getText());
        double tagihan=0, sisa=bayar;
        if(sNoBayarKoreksi.length()>0){
            clearPembayaranKoreksi();
        }
        if(bayar<=0){
            JOptionPane.showMessageDialog(this, "Jumlah pembayaran masig nol!");
            txtJumlah.requestFocusInWindow();
            return;
        }
        int col=tblNotaTagihan.getColumnModel().getColumnIndex("PIUTANG");
        int colBayar=tblNotaTagihan.getColumnModel().getColumnIndex("BAYAR");
        int colLebih=tblNotaTagihan.getColumnModel().getColumnIndex("LEBIH");

        for(int i=0; i<tblNotaTagihan.getRowCount(); i++){
            tagihan=GeneralFunction.udfGetDouble(tblNotaTagihan.getValueAt(i, col));
            if(sisa>=tagihan){
                ((DefaultTableModel)tblNotaTagihan.getModel())
                        .setValueAt(tagihan, i, colBayar);
                sisa-=tagihan;
            }else{
                ((DefaultTableModel)tblNotaTagihan.getModel())
                        .setValueAt(sisa, i, colBayar);
                sisa=0;
            }
            ((DefaultTableModel)tblNotaTagihan.getModel()).setValueAt(0, i, colLebih);
            
            if(sisa<=0)
                break;
        }
        if(sisa>0 && tblNotaTagihan.getRowCount()>0){
            ((DefaultTableModel)tblNotaTagihan.getModel()).setValueAt(sisa, tblNotaTagihan.getRowCount()-1, 
                    tblNotaTagihan.getColumnModel().getColumnIndex("LEBIH"));
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

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cmbAlatBayar = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtNoBukti = new javax.swing.JTextField();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jButton3 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtKeterangan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblTotPiutang = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jDateCek = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNotaTagihan = new javax.swing.JTable();
        panel1 = new java.awt.Panel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        lblTotalPembayaran = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalPiutang = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pembayaran", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Total Piutang :");
        jLabel5.setName("jLabel5"); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, 100, 20));

        btnSave.setText("Save");
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 150, 70, 30));

        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 150, 80, 30));

        cmbAlatBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAlatBayar.setName("cmbAlatBayar"); // NOI18N
        jPanel1.add(cmbAlatBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 280, -1));

        jLabel12.setText("Alat Bayar :");
        jLabel12.setName("jLabel12"); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 20));

        jLabel13.setText("Tgl. Cek");
        jLabel13.setName("jLabel13"); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, 70, 20));

        txtJumlah.setBackground(new java.awt.Color(204, 255, 255));
        txtJumlah.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtJumlah.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtJumlah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtJumlah.setName("txtJumlah"); // NOI18N
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });
        jPanel1.add(txtJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 140, 30));

        jLabel14.setText("No. Bukti :");
        jLabel14.setName("jLabel14"); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 70, 20));

        txtNoBukti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNoBukti.setName("txtNoBukti"); // NOI18N
        jPanel1.add(txtNoBukti, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 240, 20));

        jXDatePicker1.setName("jXDatePicker1"); // NOI18N
        jPanel1.add(jXDatePicker1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 65, 130, -1));

        jButton3.setText("Post");
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 70, 70, 30));

        jLabel15.setText("Keterangan :");
        jLabel15.setName("jLabel15"); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 70, 20));

        txtKeterangan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtKeterangan.setName("txtKeterangan"); // NOI18N
        jPanel1.add(txtKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 470, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Jumlah Bayar:");
        jLabel6.setName("jLabel6"); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 70, 120, 30));

        lblTotPiutang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotPiutang.setText("0");
        lblTotPiutang.setName("lblTotPiutang"); // NOI18N
        jPanel1.add(lblTotPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 140, 20));

        jLabel16.setText("Tanggal");
        jLabel16.setName("jLabel16"); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 65, 70, 20));

        jDateCek.setName("jDateCek"); // NOI18N
        jPanel1.add(jDateCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 115, 140, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 730, 190));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane1KeyPressed(evt);
            }
        });

        tblNotaTagihan.setAutoCreateRowSorter(true);
        tblNotaTagihan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO NOTA", "NAMA KAPAL", "TGL BRK", "JUMLAH", "PELUNASAN", "KLAIM", "LEBIH", "LAIN2", "PIUTANG", "BAYAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNotaTagihan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblNotaTagihan.setName("tblNotaTagihan"); // NOI18N
        tblNotaTagihan.getTableHeader().setReorderingAllowed(false);
        tblNotaTagihan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNotaTagihanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNotaTagihan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 730, 160));

        panel1.setName("panel1"); // NOI18N
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nota Gabung", "No. Nota", "Kapal", "Tgl Brkt", "Total", "Terbayar", "Bayar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
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
        jScrollPane2.setViewportView(jTable1);

        panel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 140));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Total Pembayaran");
        jLabel2.setName("jLabel2"); // NOI18N
        panel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 150, 120, 20));

        lblTotalPembayaran.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPembayaran.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPembayaran.setText("0");
        lblTotalPembayaran.setName("lblTotalPembayaran"); // NOI18N
        panel1.add(lblTotalPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, 110, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Piutang");
        jLabel3.setName("jLabel3"); // NOI18N
        panel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 70, 20));

        lblTotalPiutang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPiutang.setText("0");
        lblTotalPiutang.setName("lblTotalPiutang"); // NOI18N
        panel1.add(lblTotalPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 90, 20));

        getContentPane().add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 730, 180));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-765)/2, (screenSize.height-597)/2, 765, 597);
    }// </editor-fold>//GEN-END:initComponents

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
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }


    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        udfSave();
}//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
}//GEN-LAST:event_btnCancelActionPerformed

    private void tblNotaTagihanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNotaTagihanMouseClicked

}//GEN-LAST:event_tblNotaTagihanMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        udfWindowOpened();
    }//GEN-LAST:event_formWindowOpened

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        GeneralFunction.keyTyped(evt);
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        udfPostBayar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jScrollPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            ((DefaultTableModel)tblNotaTagihan.getModel()).removeRow(tblNotaTagihan.getSelectedRow());
        }
    }//GEN-LAST:event_jScrollPane1KeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgBayarBanyakNota dialog = new DlgBayarBanyakNota(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cmbAlatBayar;
    private javax.swing.JButton jButton3;
    private org.jdesktop.swingx.JXDatePicker jDateCek;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JLabel lblTotPiutang;
    private javax.swing.JLabel lblTotalPembayaran;
    private javax.swing.JLabel lblTotalPiutang;
    private java.awt.Panel panel1;
    private javax.swing.JTable tblNotaTagihan;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKeterangan;
    private javax.swing.JTextField txtNoBukti;
    // End of variables declaration//GEN-END:variables
    
    JTextArea textPane=new JTextArea();
    Color g1 = new Color(239,234,240);//-->>(251,236,177);// Kuning         [251,251,235]
    Color g2 = new Color(239,234,240);//-->>(241,226,167);// Kuning         [247,247,218]
    Color w1 = new Color(255,255,255);// Putih
    Color w2 = new Color(250,250,250);// Putih Juga
    Color h1 = new Color(255,240,240);// Merah muda
    Color h2 = new Color(250,230,230);// Merah Muda
    Color g;
    Color w;
    Color h;
    JCheckBox checkBox = new JCheckBox();

    private boolean udfCekBeforeSave(){
        btnSave.requestFocusInWindow();
        if(GeneralFunction.udfGetDouble(txtJumlah.getText())==0){
            JOptionPane.showMessageDialog(this, "Jumlah yang dibayarkan masih Nol!");
            txtJumlah.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private void udfSave() {
        if(!udfCekBeforeSave())
            return;
        try{
            conn.setAutoCommit(false);
            
            if(sNoBayarKoreksi.length()>0){
                String notaKoreksi=service.koreksiPembayaran(sNoBayarKoreksi);
                System.out.println("Hasil korkesi :"+notaKoreksi);
            }
            
            BayarNotaServices service=new BayarNotaServices(conn);
            Pembayaran bayar=new Pembayaran();
            String sNoBayar=service.getNoBayar(jXDatePicker1.getDate());
            bayar.setNo_bayar(sNoBayar);
            bayar.setKode_cust(sCustID);
            bayar.setAlat_bayar(jenisBayar.get(cmbAlatBayar.getSelectedIndex()).toString());
            bayar.setCheck_amount(GeneralFunction.udfGetDouble(txtJumlah.getText()));
            bayar.setCheck_no(txtNoBukti.getText());
            bayar.setDate_trx(new java.sql.Date(new Date().getTime()));
            double nett=GeneralFunction.udfGetDouble(lblTotPiutang.getText());

//            bayar.setIs_lunas(GeneralFunction.udfGetDouble(txtJumlah.getText())>=nett);
            bayar.setTanggal(jXDatePicker1.getDate());
            bayar.setTgl_lunas(GeneralFunction.udfGetDouble(txtJumlah.getText())>=nett ? jXDatePicker1.getDate(): null);
            bayar.setUser_tr(Utama.sUserName);
            bayar.setTo_tujuan(toTujuan);
            bayar.setKeteranganNota(txtKeterangan.getText());
            bayar.setMemo(txtKeterangan.getText());
            service.savePembayaran(bayar);
            String sNota="";
            double jmlBayar=0;
            List<PembayaranDetail> detail=new ArrayList<PembayaranDetail>();
                
            for(int i=0; i<tblNotaTagihan.getRowCount(); i++){
                sNota=tblNotaTagihan.getValueAt(i, tblNotaTagihan.getColumnModel().getColumnIndex("NO NOTA")).toString();
                jmlBayar=GeneralFunction.udfGetDouble(tblNotaTagihan.getValueAt(i, tblNotaTagihan.getColumnModel().getColumnIndex("BAYAR")));

                PembayaranDetail pembayaran=new PembayaranDetail();
                pembayaran.setNo_bayar(sNoBayar);
                pembayaran.setNo_nota(sNota);
                pembayaran.setJumlah(jmlBayar);
                detail.add(pembayaran);
                
            }
            service.savePembayaranDetail(detail);
            
            jmlBayar=0;
            List<PembayaranDetailDetail> detailNota=new ArrayList<PembayaranDetailDetail>();
            for(int i=0; i<jTable1.getRowCount(); i++){
                jmlBayar=GeneralFunction.udfGetDouble(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Bayar")));
                sNota=jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("Nota Gabung")).toString();
                
                PembayaranDetailDetail dd=new PembayaranDetailDetail();
                dd.setNoBayar(sNoBayar);
                dd.setNoNota(sNota);
                dd.setNoNotaDet(jTable1.getValueAt(i, jTable1.getColumnModel().getColumnIndex("No. Nota")).toString());
                dd.setBayar(jmlBayar);
                detailNota.add(dd);
            }
            
            service.savePembayaranDetailDetail(detailNota);
            
            service.simpanLebih(sNota, sNoBayar,
                    GeneralFunction.udfGetDouble(txtJumlah.getText()) - nett);
            conn.setAutoCommit(true);
            if(srcForm!=null && srcForm instanceof FrmListTagihanPerCust){
                ((FrmListTagihanPerCust)srcForm).udfLoadTagihan(sNota);
            }
            JOptionPane.showMessageDialog(this, "Simpan data sukses");
            this.dispose();

        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(false);
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(DlgBayarSatuNota.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    void setNoBayar(String sNoBayar) {
        this.sNoBayarKoreksi=sNoBayar;
    }

    private void udfLoadKoreksiBayar() {
        try {
            ResultSet rs=null;
            String sKota="";
            String sQry="select distinct p.kode_cust, to_tujuan, p.tanggal, coalesce(a.alat_pembayaran,'') as alat_pembayaran, "
                        + "coalesce(p.check_no,'') as no_bukti, p.tgl_cek,  "
                        + "coalesce(p.check_amount,0) as jml_bayar, coalesce(p.memo,'') as ket_bayar, "
                        + "to_char(nota.tgl_nota, 'yyyy') as th_nota, coalesce(kt.kota_tujuan,'') as kode_kota "
                        + "from nota_pembayaran p  "
                        + "inner join nota_pembayaran_detail d on d.no_bayar=p.no_bayar "
                        + "inner join nota on nota.no_nota=d.no_nota "
                        + "left join nota_kapal_tujuan kt on kt.serial_kode=nota.seri_kapal "
                        + "left join nota_alat_pembayaran a on a.kode=p.alat_bayar "
                        + "where p.no_bayar='"+sNoBayarKoreksi+"'";
            rs=conn.createStatement().executeQuery(sQry);
            if(rs.next()){
                cmbAlatBayar.setSelectedItem(rs.getString("alat_pembayaran"));
                txtNoBukti.setText(rs.getString("no_bukti"));
                jDateCek.setDate(rs.getDate("tgl_cek"));
                txtJumlah.setText(fn.intFmt.format(rs.getDouble("jml_bayar")));
                txtKeterangan.setText(rs.getString("ket_bayar"));
                sCustID=rs.getString("kode_Cust");
                sTahun=rs.getString("th_nota");
                toTujuan=rs.getBoolean("to_tujuan");
                sKota=rs.getString("kode_kota");
            }
            rs.close();
                
            sQry="select d.no_nota, x.nama_kapal, x.tgl_berangkat, x.total, x.pelunasan, "
                    + "x.klaim, x.lebih, x.lain2, x.piutang, d.jumlah "
                    + "from nota_pembayaran_detail d "
                    + "inner join nota on nota.no_nota=d.no_nota "
                    + "inner join nota_detail nd on nd.no_nota=nota.no_nota "
                    + "left join ("
                    + " select * from fn_nota_rekap_per_cust_per_tahun3("
                    + "'" + sCustID + "', "
                    + "'" + (toTujuan ? "T" : "P") + "', '"+sTahun+"', '"+sKota+"') "
                    + "as (kepada varchar, merk text, no_nota varchar, nama_kapal text, tgl_berangkat text, "
                    + "total double precision, is_header boolean, tgl_lunas text, pelunasan double precision, "
                    + "klaim double precision, lebih double precision, lain2 double precision, piutang double precision, keterangan  text, "
                    + "alat_pembayaran text, no_bayar text) "
                    + ")x on x.no_nota=d.no_nota "
                    + "where d.no_bayar='"+sNoBayarKoreksi+"' "
                    + "order by d.no_nota";
            System.out.println(sQry);
            
            double totPutang=0;        
            ((DefaultTableModel)tblNotaTagihan.getModel()).setNumRows(0);
            rs=conn.createStatement().executeQuery(sQry);
            while(rs.next()){
                totPutang=rs.getDouble("total")-rs.getDouble("klaim")-rs.getDouble("lain2");
                ((DefaultTableModel)tblNotaTagihan.getModel()).addRow(new Object[]{
                    rs.getString("no_nota"),
                    rs.getString("nama_kapal"),
                    rs.getString("tgl_berangkat"),
                    rs.getDouble("total"),
                    rs.getDouble("pelunasan"),
                    rs.getDouble("klaim"),
                    rs.getDouble("lebih"),
                    rs.getDouble("lain2"),
                    totPutang-service.telahBayarSelainKoreksi(rs.getString("no_nota"), sNoBayarKoreksi),
                    rs.getDouble("jumlah"),
                });
                
            }
                rs.close();
                
                if(tblNotaTagihan.getRowCount()>0){
                    tblNotaTagihan.setRowSelectionInterval(0, 0);
                    tblNotaTagihan.setModel((DefaultTableModel)fn.autoResizeColWidth(tblNotaTagihan,
                            (DefaultTableModel)tblNotaTagihan .getModel()).getModel());
                }
                
                ((DefaultTableModel)jTable1.getModel()).setNumRows(0);
                sQry="select  dd.no_nota, dd.no_nota_det, coalesce(kp.nama_kapal,'') as nama_kapal, kt.tgl_berangkat, "
                        + "sum(nd.sub_total) as tot_nota, coalesce(dd.jumlah,0) as jml_bayar "
                        + "from nota_pembayaran_Detail_detail dd  "
                        + "inner join nota on nota.no_nota=dd.no_nota_Det "
                        + "inner join nota_detail nd on nd.no_nota=dd.no_nota_Det "
                        + "inner join nota_kapal_tujuan kt on kt.serial_kode=nota.seri_kapal "
                        + "inner join kapal kp on kp.kode_kapal=kt.kode_kapal "
                        + "where dd.no_bayar='"+sNoBayarKoreksi+"' "
                        + "group by dd.no_nota, dd.no_nota_det, coalesce(kp.nama_kapal,''), kt.tgl_berangkat, coalesce(dd.jumlah,0) "
                        + "order by dd.no_nota";
                rs=conn.createStatement().executeQuery(sQry);
                while(rs.next()){
                    ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{
                        rs.getString("no_nota"), 
                        rs.getString("no_nota_det"), 
                        rs.getString("nama_kapal"), 
                        rs.getDate("tgl_berangkat"), 
                        rs.getDouble("tot_nota"), 
                        0, //service.telahBayarSelainKoreksi(lblNota.getText(), sNoBayarKoreksi), 
                        rs.getDouble("jml_bayar")
                    });
                }
                rs.close();
                if(jTable1.getRowCount()>0){
                    jTable1.setRowSelectionInterval(0, 0);
                    jTable1.setModel((DefaultTableModel)fn.autoResizeColWidth(jTable1,
                            (DefaultTableModel)jTable1 .getModel()).getModel());
                }
                
//                lblPiutang.setText(fn.intFmt.format(
//                        totPutang-
//                        service.telahBayarSelainKoreksi(lblNota.getText(), sNoBayarKoreksi)));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        
    }

    public void setTahun(String toString) {
        sTahun=toString;
    }
    

    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {


            if(column%2==0){
                g = g1;
                w = w1;
                h = h1;
            }else{
                g = g2;
                w = w2;
                h = h2;
            }

            //setVerticalAlignment(tx.TOP);
            if(value instanceof Float){
                setHorizontalAlignment(SwingConstants.RIGHT);
                value=GeneralFunction.intFmt.format(value);
            }

            if (value instanceof Boolean) { // Boolean
                checkBox.setSelected(((Boolean) value).booleanValue());
                checkBox.setHorizontalAlignment(JLabel.CENTER);
                if (row%2==0){
                    checkBox.setBackground(w);
                }else{
                    checkBox.setBackground(g);
                }

                if (isSelected){
                    checkBox.setBackground(tblNotaTagihan.getSelectionBackground());//51,102,255));
                }
                return checkBox;
            }else if(value instanceof String && value!=null && value.toString().indexOf("\n") >0){
                textPane.setText(value.toString());
                if (row%2==0){
                    textPane.setBackground(w);
                }else{
                    textPane.setBackground(g);
                }
                if (isSelected){
                    checkBox.setBackground(tblNotaTagihan.getSelectionBackground());//51,102,255));
                }
                return textPane;
            }else if(value instanceof Double || value instanceof Integer || value instanceof Float){
                value=fn.intFmt.format(value);
                setHorizontalAlignment(SwingConstants.RIGHT);
            }else if(value instanceof Date){
                value=GeneralFunction.ddMMyy_format.format(value);
            }
            setForeground(new Color(0,0,0));
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            if(isSelected){
                //setBackground(new Color(51,102,255));
                setBackground(tblNotaTagihan.getSelectionBackground());
                //setForeground(new Color(255,255,255));
            }

            setValue(value);
            return this;
        }
    }

}
