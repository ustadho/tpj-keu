/*
 * FrmCustomer.java
 *
 * Created on November 6, 2007, 9:54 PM
 */
package tpjkeuangan;

import com.ustasoft.Model.RekapTagihanCust;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import main.GeneralFunction;
import main.TextAreaRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import tableRender.AttributiveCellTableModel;
import tableRender.CellSpan;
import tableRender.MultiSpanCellTable;

/**
 *
 * @author  oestadho
 */
public class FrmListTagihanPerKapalOutstanding extends javax.swing.JInternalFrame {

    Connection conn;
    private String sUserName;
    JTextField tx = new JTextField();
    private JDesktopPane desktopPane;
    JCheckBox checkBox = new JCheckBox();
    private GeneralFunction fn = new GeneralFunction();
    AttributiveCellTableModel ml;
    String sCol[] = new String[]{"NO", "NO NOTA", "NAMA KAPAL", "TGL BRK", "JUMLAH", "LUNAS TGL", "PELUNASAN", "KLAIM", "LEBIH", "LAIN2", "PIUTANG", "KETERANGAN", "Gabungan", "Keterangan Klaim", "No. Pembayaran"};
    JScrollPane scroll = new JScrollPane();
    List lstSerial=new ArrayList();

    /** Creates new form FrmCustomer */
    public FrmListTagihanPerKapalOutstanding() {
        initComponents();
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                setTotal();
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int[] iRow = table.getSelectedRows();
                double total = 0;
                for (int i = 0; i < iRow.length; i++) {
                    total += GeneralFunction.udfGetDouble(table.getValueAt(iRow[i],
                            table.getColumnModel().getColumnIndex("PIUTANG")));
                }
                lblTotSeleksi.setText("Total terpilih: " + GeneralFunction.intFmt.format(total));
            }
        });
    }

    void setUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    void udfSetDesktopPane(JDesktopPane jDesktopPane1) {
        this.desktopPane = jDesktopPane1;
    }

    private void setTotal() {
        double totTagihan = 0, totPelunasan = 0, totKlaim = 0,
                totLebih = 0, totLain = 0, totPiutang = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            totTagihan += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("JUMLAH")));
            totPelunasan += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("PELUNASAN")));
            totKlaim += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("KLAIM")));
            totLebih += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("LEBIH")));
            totLain += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("LAIN2")));
            totPiutang += GeneralFunction.udfGetDouble(table.getValueAt(i, table.getColumnModel().getColumnIndex("PIUTANG")));
        }
        lblTotalTagihan.setText(GeneralFunction.intFmt.format(totTagihan));
        lblTotalPelunasan.setText(GeneralFunction.intFmt.format(totPelunasan));
        lblTotalKlaim.setText(GeneralFunction.intFmt.format(totKlaim));
        lblTotalLebih.setText(GeneralFunction.intFmt.format(totLebih));
        lblTotalLain.setText(GeneralFunction.intFmt.format(totLain));
        lblTotalPiutang.setText(GeneralFunction.intFmt.format(totPiutang));
    }
    private CellSpan cellAtt;

    public void udfLoadTagihan(String sNota) {
        String sQry = "";
        sQry = "select * from fn_nota_rekap_outstanding_per_kapal("+
                (Integer)lstSerial.get(cmbTglBerangkat.getSelectedIndex()) +") as "+
                "(kepada varchar, no_nota varchar, total double precision, pelunasan double precision, "
                + "klaim double precision, lebih double precision, lain2 double precision, "
                + "piutang double precision, keterangan  text, alat_pembayaran text, no_bayar text)";

        System.out.println(sQry);
        int i = 0;
        try {
            ((DefaultTableModel) table.getModel()).setNumRows(0);

            ResultSet rs = conn.createStatement().executeQuery(sQry);
            double piutang = 0;
            //System.out.println(sQry);

            int index=0;
            while (rs.next()) {
               piutang = rs.getDouble("total") - rs.getDouble("pelunasan") - rs.getDouble("klaim") - rs.getDouble("lain2") + rs.getDouble("lebih");
                    ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                        table.getRowCount() + 1,
                        rs.getString("no_nota"),
                        rs.getString("kepada"),
                        rs.getDouble("total"),
                        rs.getDouble("pelunasan"),
                        rs.getDouble("klaim"),
                        rs.getDouble("lebih"),
                        rs.getDouble("lain2"),
                        rs.getDouble("piutang"),
                        rs.getString("alat_pembayaran"),
                        rs.getString("keterangan"),
                        rs.getString("no_bayar")
                    });
                if(sNota.equalsIgnoreCase(rs.getString("no_nota"))){
                    index=table.getRowCount()-1;
                }    
            }


            if(table.getRowCount()>0){
                table.setRowSelectionInterval(index, index);
                table.setModel((DefaultTableModel)fn.autoResizeColWidth(table,
                        (DefaultTableModel)table .getModel()).getModel());
            }

            int col = 0, iRow = 0;
            int colNota = table.getColumnModel().getColumnIndex("No. Pembayaran");
            int colCaraBayar = table.getColumnModel().getColumnIndex("KETERANGAN");

            if (table.getRowCount() > 0) {
                table.requestFocusInWindow();
                table.setRowSelectionInterval(0, 0);
                table.setModel((DefaultTableModel) fn.autoResizeColWidth(table, (DefaultTableModel) table.getModel()).getModel());

//            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) tblNotaTagihan.getColumnModel();

            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }
    List<RekapTagihanCust> dataTagihan = new ArrayList<RekapTagihanCust>();
    
    List lstKapal=new ArrayList();
    private void udfLoadKapal(){
        if (cmbKota.getSelectedIndex() < 0 || conn == null) {
            return;
        }
        try {
            String sQry = "select distinct kt.kode_kapal, kapal.nama_kapal "
                    + "from nota_kapal_tujuan kt  "
                    + "inner join kota on kt.kota_tujuan=kota.kode_kota "
                    + "inner join kapal on kapal.kode_kapal=kt.kode_kapal "
                    + "where kt.kota_tujuan='" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "' "
                    + "order by 2";
            System.out.println(sQry);
            lstKapal.clear();
            cmbKapal.removeAllItems();
            ResultSet rs = conn.createStatement().executeQuery(sQry);
            while (rs.next()) {
                lstKapal.add(rs.getString("kode_kapal"));
                cmbKapal.addItem(rs.getString("nama_kapal"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void udfLoadTglBerangkat(){
        if (cmbKota.getSelectedIndex()<0 ||cmbKapal.getSelectedIndex()<0 || conn == null) {
            return;
        }
        try {
            String sQry = "select distinct to_char(tgl_berangkat, 'dd/MM/yyyy') as tanggal, "
                    + "tgl_berangkat::date, serial_kode "
                    + "from "
                    + "nota_kapal_tujuan kt "
                    + "where kt.kota_tujuan='" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "' "
                    + "and kode_kapal='" + lstKapal.get(cmbKapal.getSelectedIndex()).toString() + "' "
                    + "order by 2 desc";
            
            System.out.println(sQry);
            
            cmbTglBerangkat.removeAllItems();
            lstSerial.clear();
            ResultSet rs = conn.createStatement().executeQuery(sQry);
            while (rs.next()) {
                cmbTglBerangkat.addItem(rs.getString("tanggal"));
                lstSerial.add(rs.getInt("serial_kode"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class MyKeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            //if(evt.getSource().equals(btnAdd)) return;

            switch (keyKode) {
                case KeyEvent.VK_ENTER: {

                    if (!fn.isListVisible()) {
                        Component c = findNextFocus();
                        if (c != null) {
                            c.requestFocus();
                        }
                    } else {
                        fn.lstRequestFocus();
                    }
                    break;
                }
                case KeyEvent.VK_UP: {
                    if (!fn.isListVisible() && !evt.getSource().equals(table)) {
                        Component c = findPrevFocus();
                        if (c != null) {
                            c.requestFocus();
                        }
                    } else {
                        fn.lstRequestFocus();
                    }
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    if (!fn.isListVisible() && !evt.getSource().equals(table)) {
                        Component c = findNextFocus();
                        if (c != null) {
                            c.requestFocus();
                        }
                    } else {
                        fn.lstRequestFocus();
                    }
                    break;
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
    }

    private void TableLook() {
        int lbr = table.getWidth();


//        tblNotaTagihan.getColumnModel().getColumn(0).setPreferredWidth(25);
//        tblNotaTagihan.getColumnModel().getColumn(1).setPreferredWidth(100);
//        tblNotaTagihan.getColumnModel().getColumn(2).setPreferredWidth(200);
//        tblNotaTagihan.getColumnModel().getColumn(3).setPreferredWidth(200);

        table.setRowHeight(30);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 2 || i == 3||i==table.getColumnModel().getColumnIndex("KETERANGAN")) {
                table.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
            }
        }


        if (table.getRowCount() > 0) {
            table.changeSelection(0, 0, false, false);
        }
        //tblPenerimaan.removeColumn(tblPenerimaan.getColumnModel().getColumn(4));
    }
    JTextArea textPane = new JTextArea();
    Color g1 = new Color(239, 234, 240);//-->>(251,236,177);// Kuning         [251,251,235]
    Color g2 = new Color(239, 234, 240);//-->>(241,226,167);// Kuning         [247,247,218]
    Color w1 = new Color(255, 255, 255);// Putih
    Color w2 = new Color(250, 250, 250);// Putih Juga
    Color h1 = new Color(255, 240, 240);// Merah muda
    Color h2 = new Color(250, 230, 230);// Merah Muda
    Color g;
    Color w;
    Color h;

    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column % 2 == 0) {
                g = g1;
                w = w1;
                h = h1;
            } else {
                g = g2;
                w = w2;
                h = h2;
            }

            //setVerticalAlignment(tx.TOP);
            if (value instanceof Float) {
                setHorizontalAlignment(tx.RIGHT);
                value = formatter.format(value);
            }

            if (value instanceof Boolean) { // Boolean
                checkBox.setSelected(((Boolean) value).booleanValue());
                checkBox.setHorizontalAlignment(JLabel.CENTER);
                if (row % 2 == 0) {
                    checkBox.setBackground(w);
                } else {
                    checkBox.setBackground(g);
                }

                if (isSelected) {
                    checkBox.setBackground(table.getSelectionBackground());//51,102,255));
                }
                return checkBox;
            } else if (value instanceof String && value != null && value.toString().indexOf("\n") > 0) {
                textPane.setText(value.toString());
                if (row % 2 == 0) {
                    textPane.setBackground(w);
                } else {
                    textPane.setBackground(g);
                }
                if (isSelected) {
                    checkBox.setBackground(table.getSelectionBackground());//51,102,255));
                }
                return textPane;
            } else if (value instanceof Double || value instanceof Integer || value instanceof Float) {
                value = fn.intFmt.format(value);
                setHorizontalAlignment(tx.RIGHT);
            } else if (value instanceof Date) {
                value = GeneralFunction.ddMMyy_format.format(value);
            }
            setForeground(new Color(0, 0, 0));
            if (row % 2 == 0) {
                setBackground(w);
            } else {
                setBackground(g);
            }
            if (isSelected) {
                //setBackground(new Color(51,102,255));
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            setValue(value);
            return this;
        }
    }

    private String getNoNota(){
        String sNota="";
        int iRow=table.getSelectedRow();
        if(iRow<0){return sNota;}
        try{
            String sQry="select case when coalesce(nota_header,'')='' then no_nota else nota_header end as no_nota "
                + "from nota where no_nota='"+table.getValueAt(iRow, table.getColumnModel().getColumnIndex("NO NOTA")).toString() +"' ";
        
            ResultSet rs=conn.createStatement().executeQuery(sQry);
            if(rs.next()){
                sNota=rs.getString(1);
            }
            rs.close();
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
        
        return sNota;
    }
    
    private void bayarNotaSatu() {
        int iRow = table.getSelectedRow();
        if (iRow < 0) {
            return;
        }
        String sNota = getNoNota();
        DlgBayarSatuNota d2 = new DlgBayarSatuNota(JOptionPane.getFrameForComponent(this), false);
        d2.setConn(conn);
        d2.setSrcForm(this);
        d2.setTitle("No. Nota ~ " + getNoNota());
        d2.tampilkanNota(sNota);
        d2.setVisible(true);
    }

    private void udfPrint() {
        String sNota = table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("NO NOTA")).toString();
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("no_nota", sNota);
            reportParam.put("stempel", getClass().getResource("/image/tpj.gif").toString());
            reportParam.put("showCompany", chkTPJHeader.isSelected());

            System.out.println("No. Nota: " + sNota);
            System.out.println(getClass().getResource(""));
            //String sRpt=(Boolean)modelPenerimaan.getValueAt(tblNotaTagihan.getSelectedRow(), 4)==false? "nota_cetak_v4":"nota_cetak_gabungan_v4" ;
            String sRpt = (Boolean) table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("Gabungan")) == false ? "nota_cetak_v53" : "nota_cetak_gabungan_v53";
            sRpt += (chkStempel.isSelected() ? "_ttd" : "");
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/" + sRpt + ".jasper"));

            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);

            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            JasperViewer.viewReport(print, false);



        } catch (JRException je) {
            System.out.println("Error report:" + je.getMessage());
        }
    }
    private FocusListener txtFoculListener = new FocusListener() {

        public void focusGained(FocusEvent e) {
            Component c = (Component) e.getSource();
            c.setBackground(g1);
            //c.setSelectionStart(0);
            //c.setSelectionEnd(c.getText().length());

            //c.setForeground(fPutih);
            //c.setCaretColor(new java.awt.Color(255, 255, 255));
        }

        public void focusLost(FocusEvent e) {
            Component c = (Component) e.getSource();
            c.setBackground(g2);
            //c.setForeground(fHitam);
        }
    };

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        panelCustomer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbKota = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox();
        btnRefresh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbKapal = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        cmbTglBerangkat = new javax.swing.JComboBox();
        lblTotalTagihan = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTotalPelunasan = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTotalKlaim = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblTotalLebih = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalLain = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblTotalPiutang = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        chkTPJHeader = new javax.swing.JCheckBox();
        chkStempel = new javax.swing.JCheckBox();
        lblTotSeleksi = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Outstanding per Kapal");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "NO NOTA", "KEPADA", "JUMLAH", "PELUNASAN", "KLAIM", "LEBIH", "LAIN2", "PIUTANG", "KETERANGAN", "Keterangan Klaim", "No. Pembayaran"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(3).setMaxWidth(400);

        panelCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Kota :");
        panelCustomer.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 20));

        cmbKota.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbKota.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 260, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tahun : ");
        panelCustomer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 50, 20));

        cmbTahun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 90, -1));

        btnRefresh.setText("Load");
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        panelCustomer.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, 70, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Kapal :");
        panelCustomer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 60, 20));

        cmbKapal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbKapal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 260, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Tgl Brkt");
        panelCustomer.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 60, 20));

        cmbTglBerangkat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTglBerangkat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 130, -1));

        lblTotalTagihan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalTagihan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTagihan.setText("0");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("TOTAL TAGIHAN");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("= Rp. ");

        lblTotalPelunasan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPelunasan.setForeground(new java.awt.Color(0, 0, 153));
        lblTotalPelunasan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPelunasan.setText("0");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 153));
        jLabel6.setText("= Rp. ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 153));
        jLabel7.setText("TOTAL PELUNASAN");

        lblTotalKlaim.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalKlaim.setForeground(new java.awt.Color(0, 153, 51));
        lblTotalKlaim.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalKlaim.setText("0");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 51));
        jLabel8.setText("= Rp. ");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 153, 51));
        jLabel9.setText("TOTAL KLAIM");

        lblTotalLebih.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalLebih.setForeground(new java.awt.Color(204, 102, 0));
        lblTotalLebih.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalLebih.setText("0");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 102, 0));
        jLabel10.setText("= Rp. ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 102, 0));
        jLabel11.setText("TOTAL LEBIH BAYAR");

        lblTotalLain.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalLain.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalLain.setText("0");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("= Rp. ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("TOTAL LAIN-LAIN");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("TOTAL PIUTANG");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("= Rp. ");

        lblTotalPiutang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPiutang.setText("0");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("Bayar");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 30));

        jButton2.setText("Klaim");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 70, 30));

        jButton3.setText("Preview Nota");
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 100, 30));

        chkTPJHeader.setText("Header");
        jPanel1.add(chkTPJHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 60, -1));

        chkStempel.setText("Stempel");
        jPanel1.add(chkStempel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 70, -1));

        lblTotSeleksi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel1.add(lblTotSeleksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 260, 20));

        jButton4.setText("Preview Outstanding");
        jButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblTotalPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblTotalKlaim, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblTotalLebih, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblTotalLain, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(180, 180, 180)
                                .addComponent(lblTotalPiutang, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)))
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(panelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalKlaim, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalLebih, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalLain, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalPiutang, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-654)/2, (screenSize.height-529)/2, 654, 529);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isTagihanOn = false;
        fn.setVisibleList(false);
    }//GEN-LAST:event_formInternalFrameClosed
    private ArrayList lstKota = new ArrayList();
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        MyKeyListener kListener = new MyKeyListener();
        jScrollPane1.addKeyListener(kListener);
        table.addKeyListener(kListener);

        TableLook();
        requestFocusInWindow(true);
        table.requestFocusInWindow();
        try {
            cmbTahun.removeAllItems();
            ResultSet rs = conn.createStatement().executeQuery("select distinct to_char(tgl_nota, 'yyyy') as tahun, to_Char(current_date, 'yyyy') as th_skg "
                    + "from nota order by to_char(tgl_nota, 'yyyy')");
            String sTahunSkg = "";
            while (rs.next()) {
                cmbTahun.addItem(rs.getString("tahun"));
                sTahunSkg = rs.getString("th_skg");
            }
            rs.close();
            cmbTahun.setSelectedItem(sTahunSkg);

            cmbKota.removeAllItems();
            rs = conn.createStatement().executeQuery("select * From kota order by nama_kota");
            while (rs.next()) {
                lstKota.add(rs.getString("kode_kota"));
                cmbKota.addItem(rs.getString("nama_kota"));
            }
            rs.close();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
        if (!Utama.sNamaKota.equalsIgnoreCase("")) {
            cmbKota.setSelectedItem(Utama.sNamaKota);
        }

        cmbKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                udfLoadKapal();
            }
        });
        cmbKapal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                udfLoadTglBerangkat();
            }
        });
        udfLoadKapal();
        udfLoadTglBerangkat();
    }//GEN-LAST:event_formInternalFrameOpened

    
private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
}//GEN-LAST:event_tableMouseClicked

private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
    udfLoadTagihan("");
    //udfSetTableLook();
}//GEN-LAST:event_btnRefreshMouseClicked

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int iRow = table.getSelectedRow();
    if (iRow < 0) {
        return;
    }
    String sNota = table.getValueAt(iRow, table.getColumnModel().getColumnIndex("NO NOTA")).toString();

    DlgNotaClaim f1 = new DlgNotaClaim(JOptionPane.getFrameForComponent(this), true);
    f1.setConn(conn);
    f1.setSrcForm(this);
    f1.tampilkaNota(sNota);
    f1.setVisible(true);

}//GEN-LAST:event_jButton2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (table.getSelectedRow() < 0) {
        return;
    }
    if (table.getSelectedRows().length == 1) {
        bayarNotaSatu();
    } 
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    udfPrint();
}//GEN-LAST:event_jButton3ActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        udfLoadTagihan("");
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String sNota = table.getValueAt(table.getSelectedRow(), table.getColumnModel().getColumnIndex("NO NOTA")).toString();
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("serial", (Integer)lstSerial.get(cmbTglBerangkat.getSelectedIndex()));
            reportParam.put("kapal", cmbKapal.getSelectedItem().toString()+" TUJUAN "+cmbKota.getSelectedItem().toString()+" TGL BERANGKAT : "+cmbTglBerangkat.getSelectedItem().toString());
            
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/PiutangOutstandingPerKapal.jasper"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);

            if(print.getPages().isEmpty()){
                JOptionPane.showMessageDialog(this, "report tidak ditemukan!");
                return;
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            print.setOrientation(jasperReport.ORIENTATION_LANDSCAPE);
            JasperViewer.viewReport(print, false);



        } catch (JRException je) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(this, je.getMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    void setCon(Connection conn) {
        this.conn = conn;
        fn.setConn(conn);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkStempel;
    private javax.swing.JCheckBox chkTPJHeader;
    private javax.swing.JComboBox cmbKapal;
    private javax.swing.JComboBox cmbKota;
    private javax.swing.JComboBox cmbTahun;
    private javax.swing.JComboBox cmbTglBerangkat;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblTotSeleksi;
    private javax.swing.JLabel lblTotalKlaim;
    private javax.swing.JLabel lblTotalLain;
    private javax.swing.JLabel lblTotalLebih;
    private javax.swing.JLabel lblTotalPelunasan;
    private javax.swing.JLabel lblTotalPiutang;
    private javax.swing.JLabel lblTotalTagihan;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
//    private DefaultTableModel modelPenerimaan;
    DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat formatter = new DecimalFormat("#,###,###");
//    Color g1 = new Color(153,255,255);
//    Color g2 = new Color(255,255,255);
    Color fHitam = new Color(0, 0, 0);
    Color fPutih = new Color(255, 255, 255);
    Color crtHitam = new java.awt.Color(0, 0, 0);
    Color crtPutih = new java.awt.Color(255, 255, 255);
}
