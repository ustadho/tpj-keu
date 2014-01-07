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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
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
import tableRender.ColumnGroup;
import tableRender.GroupableTableHeader;

/**
 *
 * @author  oestadho
 */
public class FrmListBiayaKapal extends javax.swing.JInternalFrame {

    Connection conn;
    private String sUserName;
    JTextField tx = new JTextField();
    private JDesktopPane desktopPane;
    JCheckBox checkBox = new JCheckBox();
    private GeneralFunction fn = new GeneralFunction();
    AttributiveCellTableModel ml;
    String sCol[] = new String[]{"NO", "NO NOTA", "NAMA KAPAL", "TGL BRK", "JUMLAH", "LUNAS TGL", "PELUNASAN", "KLAIM", "LEBIH", "LAIN2", "PIUTANG", "KETERANGAN", "Gabungan", "Keterangan Klaim", "No. Pembayaran"};
    JScrollPane scroll = new JScrollPane();

    /** Creates new form FrmCustomer */
    public FrmListBiayaKapal() {
        initComponents();
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                setTotal();
            }
        });

//        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//
//            public void valueChanged(ListSelectionEvent e) {
//                int[] iRow = table.getSelectedRows();
//                double total = 0;
//                for (int i = 0; i < iRow.length; i++) {
//                    total += GeneralFunction.udfGetDouble(table.getValueAt(iRow[i],
//                            table.getColumnModel().getColumnIndex("PIUTANG")));
//                }
//                lblTotSeleksi.setText("Total terpilih: " + GeneralFunction.intFmt.format(total));
//            }
//        });
        UIManager.put(GroupableTableHeader.uiClassID, "tableRender.GroupableTableHeaderUI");
        GroupableTableHeader header=new GroupableTableHeader(table.getColumnModel());
        
        ColumnGroup colGroup = new ColumnGroup("PENGELUARAN");        
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("SPIL")));
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("TANTO")));
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("EXPEDISI")));
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("PEMBONGKARAN")));
        header.addGroup(colGroup);
        
        colGroup = new ColumnGroup("PEMASUKAN");        
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("TOT TAGIHAN")));
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("PELUNASAN")));
        colGroup.add(table.getColumnModel().getColumn(table.getColumnModel().getColumnIndex("TOT PIUTANG")));
        header.addGroup(colGroup);
        
        
        header.setBackground(new Color(51,0,245));
        header.setForeground(new Color(255,255,0));
        
        
        table.setTableHeader(header);
        table.getTableHeader().setSize(150,25);
        table.setRowHeight(22);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
    }

    void setUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    void udfSetDesktopPane(JDesktopPane jDesktopPane1) {
        this.desktopPane = jDesktopPane1;
    }

    private void setTotal() {
        double totSpil = 0, totTanto = 0, totExpedisi = 0, totPembongkaran = 0,
                totTagihan = 0, totPelunasan = 0, totPiutang = 0, totTpj=0;

        TableColumnModel col=table.getColumnModel();
        
        for (int i = 0; i < table.getRowCount(); i++) {
            totSpil += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("SPIL")));
            totTanto += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("TANTO")));
            totExpedisi += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("EXPEDISI")));
            totPembongkaran += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("PEMBONGKARAN")));
            
            totTagihan += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("TOT TAGIHAN")));
            totPelunasan += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("PELUNASAN")));
            totPiutang += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("TOT PIUTANG")));
            totTpj += GeneralFunction.udfGetDouble(table.getValueAt(i, col.getColumnIndex("TPJ")));
        }
        lblTotalSpil.setText(GeneralFunction.intFmt.format(totSpil));
        lblTotalTanto.setText(GeneralFunction.intFmt.format(totTanto));
        lblTotalExpedisi.setText(GeneralFunction.intFmt.format(totExpedisi));
        lblTotalTpj.setText(GeneralFunction.intFmt.format(totPembongkaran));
        
        lblTotalTagihan.setText(GeneralFunction.intFmt.format(totTagihan));
        lblTotalPelunasan.setText(GeneralFunction.intFmt.format(totPelunasan));
        lblTotalPiutang.setText(GeneralFunction.intFmt.format(totPiutang));
        lblTotalTpj.setText(GeneralFunction.intFmt.format(totTpj));
    }
    private CellSpan cellAtt;

    public void udfLoadTagihan(Integer seriKapal) {
        String sQry = "";
        sQry = "select * from fn_nota_rpt_piutang_per_kapal("
                + "'" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "', "
                + "'" + cmbTahun.getSelectedItem().toString() + "') "
                + "as (kota_kota varchar, nama_kota varchar, "
                + "serial_kode int, nama_kapal varchar, tgl_berangkat date, "
                + "tot_kontainer text, by_spil double precision, "
                + "by_tanto double precision, by_expedisi double precision, by_bongkar double precision, "
                + "tot_tagihan double precision, tot_pelunasan double precision, tot_piutang double precision, "
                + "by_tpj double precision, ket_biaya text) "
                + (cmbBulan.getSelectedIndex()==0? "": "where to_char(tgl_berangkat, 'MM')='"+new DecimalFormat("00").format(cmbBulan.getSelectedIndex())+"' order by tgl_berangkat") +" ";

        System.out.println(sQry);
        int i = 0;
        try {
            ((DefaultTableModel) table.getModel()).setNumRows(0);

            ResultSet rs = conn.createStatement().executeQuery(sQry);
            //System.out.println(sQry);

            int index=0;
            while (rs.next()) {
                    ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                        rs.getString("nama_kapal"),
                        rs.getDate("tgl_berangkat"),
                        rs.getString("tot_kontainer"),
                        rs.getDouble("by_spil"),
                        rs.getDouble("by_tanto"),
                        rs.getDouble("by_expedisi"),
                        rs.getDouble("by_bongkar"),
                        rs.getDouble("tot_tagihan"),
                        rs.getDouble("tot_pelunasan"),
                        rs.getDouble("tot_piutang"),
                        rs.getDouble("by_tpj"),
                        rs.getString("ket_biaya"),
                        rs.getInt("serial_kode")
                    });
                if(seriKapal.intValue()==rs.getInt("serial_kode")){
                    index=table.getRowCount()-1;
                }    
            }

            if (table.getRowCount() > 0) {
                table.requestFocusInWindow();
                table.setRowSelectionInterval(index, index);
//                table.setModel((DefaultTableModel) fn.autoResizeColWidth(table, (DefaultTableModel) table.getModel()).getModel());

//            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) tblNotaTagihan.getColumnModel();

            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }
    List<RekapTagihanCust> dataTagihan = new ArrayList<RekapTagihanCust>();

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

    private void bayarNotaSatu() {
        int iRow = table.getSelectedRow();
        if (iRow < 0) {
            return;
        }
        String sNota = table.getValueAt(iRow, table.getColumnModel().getColumnIndex("NO NOTA")).toString();
        DlgBayarSatuNota d2 = new DlgBayarSatuNota(JOptionPane.getFrameForComponent(this), false);
        d2.setConn(conn);
        d2.setSrcForm(this);
        d2.setTitle("No. Nota ~ " + sNota);
//        d2.setSisa( udfGetFloat(lblJmlNota.getText()) -
//                    udfGetFloat(lblTotalBayar.getText())+udfGetFloat(lblTotalKlem.getText()) );

        //d2.setUserName(sUserName);
        d2.tampilkanNota(sNota);
//        d2.setCustomer(sCustomer);
//        d2.setSrcModel(myModel);
        d2.setVisible(true);
    }

    private void udfPrint() {
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            String sBulan=cmbBulan.getSelectedIndex()==0? "": new DecimalFormat("00").format(cmbBulan.getSelectedIndex());
            reportParam.put("tahun", cmbTahun.getSelectedItem().toString());
            reportParam.put("bulan", sBulan);
            reportParam.put("namaBulan", cmbBulan.getSelectedItem().toString());
            reportParam.put("kodeKota", lstKota.get(cmbKota.getSelectedIndex()).toString());

            System.out.println("Bulan: " + sBulan);
            String sRpt = "BiayaKapalPerKota";
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/" + sRpt + ".jasper"));

            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);

            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            if(print.getPages().isEmpty()){
                JOptionPane.showMessageDialog(this, "Report tidak ditemukan!");
                return;
            }
            JasperViewer.viewReport(print, false);



        } catch (JRException je) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            JOptionPane.showMessageDialog(this,je.getMessage());
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
        cmbBulan = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lblTotalSpil = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTotalTanto = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTotalExpedisi = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblTotalTpj = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblTotalTagihan = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblTotalPelunasan = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblTotalPiutang = new javax.swing.JLabel();
        lblTotalPembongkaran1 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Biaya Kapal - Per Tahun");
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

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAMA KAPAL", "TGL BRK", "TOT CTNR", "SPIL", "TANTO", "EXPEDISI", "PEMBONGKARAN", "TOT TAGIHAN", "PELUNASAN", "TOT PIUTANG", "TPJ", "Keterangan", "Serial"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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
        panelCustomer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 50, 20));

        cmbTahun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 90, -1));

        btnRefresh.setText("Refresh");
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
        panelCustomer.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 90, -1));

        cmbBulan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbBulan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        panelCustomer.add(cmbBulan, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 100, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Bulan : ");
        panelCustomer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 50, 20));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("Biaya");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 90, 30));

        jButton3.setText("Print Preview");
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 90, 30));

        lblTotalSpil.setBackground(new java.awt.Color(255, 204, 255));
        lblTotalSpil.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalSpil.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalSpil.setText("0");
        lblTotalSpil.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalSpil.setOpaque(true);
        jPanel1.add(lblTotalSpil, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 90, 20));

        jLabel4.setBackground(new java.awt.Color(255, 204, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("SPIL");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.setOpaque(true);
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 20));

        jLabel16.setBackground(new java.awt.Color(255, 204, 255));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("TANTO");
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel16.setOpaque(true);
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 90, 20));

        lblTotalTanto.setBackground(new java.awt.Color(255, 204, 255));
        lblTotalTanto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalTanto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTanto.setText("0");
        lblTotalTanto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalTanto.setOpaque(true);
        jPanel1.add(lblTotalTanto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 90, 20));

        jLabel17.setBackground(new java.awt.Color(255, 204, 255));
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("EXPEDISI");
        jLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel17.setOpaque(true);
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 90, 20));

        lblTotalExpedisi.setBackground(new java.awt.Color(255, 204, 255));
        lblTotalExpedisi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalExpedisi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalExpedisi.setText("0");
        lblTotalExpedisi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalExpedisi.setOpaque(true);
        jPanel1.add(lblTotalExpedisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 90, 20));

        jLabel18.setBackground(new java.awt.Color(255, 204, 255));
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("PEMBONGKARAN");
        jLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel18.setOpaque(true);
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 110, 20));

        lblTotalTpj.setBackground(new java.awt.Color(204, 204, 255));
        lblTotalTpj.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotalTpj.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTpj.setText("0");
        lblTotalTpj.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalTpj.setOpaque(true);
        jPanel1.add(lblTotalTpj, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, 110, 20));

        jLabel19.setBackground(new java.awt.Color(204, 255, 204));
        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("TAGIHAN");
        jLabel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel19.setOpaque(true);
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 110, 20));

        lblTotalTagihan.setBackground(new java.awt.Color(204, 255, 204));
        lblTotalTagihan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalTagihan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTagihan.setText("0");
        lblTotalTagihan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalTagihan.setOpaque(true);
        jPanel1.add(lblTotalTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 110, 20));

        jLabel20.setBackground(new java.awt.Color(204, 255, 204));
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("PELUNASAN");
        jLabel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel20.setOpaque(true);
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 110, 20));

        lblTotalPelunasan.setBackground(new java.awt.Color(204, 255, 204));
        lblTotalPelunasan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalPelunasan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPelunasan.setText("0");
        lblTotalPelunasan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalPelunasan.setOpaque(true);
        jPanel1.add(lblTotalPelunasan, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 30, 110, 20));

        jLabel21.setBackground(new java.awt.Color(204, 204, 255));
        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("TOT TPJ");
        jLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel21.setOpaque(true);
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 220, 20));

        lblTotalPiutang.setBackground(new java.awt.Color(204, 255, 204));
        lblTotalPiutang.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalPiutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPiutang.setText("0");
        lblTotalPiutang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalPiutang.setOpaque(true);
        jPanel1.add(lblTotalPiutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 110, 20));

        lblTotalPembongkaran1.setBackground(new java.awt.Color(255, 204, 255));
        lblTotalPembongkaran1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalPembongkaran1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalPembongkaran1.setText("0");
        lblTotalPembongkaran1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTotalPembongkaran1.setOpaque(true);
        jPanel1.add(lblTotalPembongkaran1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 110, 20));

        jLabel22.setBackground(new java.awt.Color(204, 255, 204));
        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("TOT PIUTANG");
        jLabel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel22.setOpaque(true);
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 110, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(panelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-801)/2, (screenSize.height-516)/2, 801, 516);
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

        //TableLook();
        requestFocusInWindow(true);
        table.requestFocusInWindow();
        try {
            cmbTahun.removeAllItems();
            ResultSet rs = conn.createStatement().executeQuery("select distinct to_char(tgl_nota, 'yyyy') as tahun, to_Char(current_date, 'yyyy') as th_skg "
                    + "from nota order by to_char(tgl_nota, 'yyyy') DESC");
            String sTahunSkg = "";
            while (rs.next()) {
                cmbTahun.addItem(rs.getString("tahun"));
                sTahunSkg = rs.getString("th_skg");
            }
            rs.close();
            cmbTahun.setSelectedItem(sTahunSkg);

            cmbKota.removeAllItems();
            
            cmbKota.addItem("Semua Kota");
            lstKota.add("");
            
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

//        int jmlRow = 0;
//        final int fixJmlRow = 1;
//
//        ml = new AttributiveCellTableModel(sCol, jmlRow * fixJmlRow);
//
//        cellAtt = (CellSpan) ml.getCellAttribute();
//        table = new MultiSpanCellTable(ml);
//
//
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }//GEN-LAST:event_formInternalFrameOpened

    
private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
}//GEN-LAST:event_tableMouseClicked

private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
    udfLoadTagihan(0);
    //udfSetTableLook();
}//GEN-LAST:event_btnRefreshMouseClicked

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int iRow=table.getSelectedRow();
    if(iRow<0)
        return;
    Integer serial=GeneralFunction.udfGetInt(table.getValueAt(iRow, table.getColumnModel().getColumnIndex("Serial")));
    DlgBiayaKapal d1=new DlgBiayaKapal(JOptionPane.getFrameForComponent(this), true);
    d1.setConn(conn);
    d1.udfLoadBiaya(serial);
    d1.setVisible(true);
    udfLoadTagihan(serial);

}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    udfPrint();
}//GEN-LAST:event_jButton3ActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        udfLoadTagihan(0);
    }//GEN-LAST:event_btnRefreshActionPerformed

    void setCon(Connection conn) {
        this.conn = conn;
        fn.setConn(conn);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbBulan;
    private javax.swing.JComboBox cmbKota;
    private javax.swing.JComboBox cmbTahun;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalExpedisi;
    private javax.swing.JLabel lblTotalPelunasan;
    private javax.swing.JLabel lblTotalPembongkaran1;
    private javax.swing.JLabel lblTotalPiutang;
    private javax.swing.JLabel lblTotalSpil;
    private javax.swing.JLabel lblTotalTagihan;
    private javax.swing.JLabel lblTotalTanto;
    private javax.swing.JLabel lblTotalTpj;
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
