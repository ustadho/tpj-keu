/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import main.GeneralFunction;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

/**
 *
 * @author cak-ust
 */
public class FrmNotaJatuhTempo extends javax.swing.JInternalFrame {

    private Connection conn;
    private List lstKota = new ArrayList();
    private List lstKapal = new ArrayList();

    /**
     * Creates new form FrmNotaJatuhTempo
     */
    public FrmNotaJatuhTempo() {
        initComponents();
        table.getColumn("Jatuh Tempo").setCellEditor(new MyTableCellEditor());
        for(int i=0; i<table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                int i=table.getSelectedRow();
                if(i<0)
                    return;
                
                if(e.getColumn()==table.getColumnModel().getColumnIndex("Check")){
                    if((Boolean)table.getValueAt(i, table.getColumnModel().getColumnIndex("Check")))
                        table.setValueAt(jXDatePicker1.getDate(), i, table.getColumnModel().getColumnIndex("Jatuh Tempo"));
                    else
                        table.setValueAt(null, i, table.getColumnModel().getColumnIndex("Jatuh Tempo"));
                }
            }
        });
    }

    public void setConn(Connection con) {
        this.conn = con;
    }

    JXDatePicker tanggal = new JXDatePicker() {
        protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
            if (hasFocus()) {
                return super.processKeyBinding(ks, e, condition, pressed);
            } else {
                this.requestFocus();
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        processKeyBinding(ks, e, condition, pressed);
                    }
                });
                return true;
            }
        }
    };

    public class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        private Toolkit toolkit;
        

        ;
        int col, row;

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at (rowIndex, vColIndex)

            //text.addKeyListener(kListener);
            //text.setEditable(canEdit);
            col = vColIndex;
            row = rowIndex;
            tanggal.setBackground(new Color(0, 255, 204));
            tanggal.setFont(table.getFont());
            tanggal.setName("textEditor");
            
            
            if (isSelected) {
            }

            if (value instanceof java.util.Date) {
                tanggal.setDate((java.util.Date)value);
            } 
            return tanggal;
        }

        public Object getCellEditorValue() {
            Object retVal = 0;
            try {
                retVal = tanggal.getDate();
            } catch (Exception e) {
                toolkit.beep();
                retVal = 0;
            }
            return retVal;
        }
    }
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //Component comp = getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Color g1 = new Color(239, 234, 240);//-->>(251,236,177);// Kuning         [251,251,235]
            Color g2 = new Color(239, 234, 240);//-->>(241,226,167);// Kuning         [247,247,218]

            Color w1 = new Color(255, 255, 255);// Putih
            Color w2 = new Color(250, 250, 250);// Putih Juga

            Color h1 = new Color(255, 240, 240);// Merah muda
            Color h2 = new Color(250, 230, 230);// Merah Muda

            Color g;
            Color w;
            Color h;

            if (column % 2 == 0) {
                g = g1;
                w = w1;
                h = h1;
            } else {
                g = g2;
                w = w2;
                h = h2;
            }


            JCheckBox checkBox = new JCheckBox();
            if (value instanceof Timestamp) {
                value = dmyFmt_hhmm.format(value);
            }else if (value instanceof Date) {
                value = dmyFmt.format(value);
            } else if (value instanceof Double || value instanceof Integer || value instanceof Float) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                value = fn.intFmt.format(value);
            } else if (value instanceof Boolean) { // Boolean
                checkBox.setSelected(((Boolean) value).booleanValue());
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                if(isSelected){
                    checkBox.setBackground(new Color(0, 102, 255));
                    checkBox.setForeground(new Color(255, 255, 255));
                }else{
                    checkBox.setBackground(getBackground());
                    checkBox.setForeground(getForeground());
                }
                return checkBox;
            }

            setForeground(new Color(0, 0, 0));
            if (row % 2 == 0) {
                setBackground(w);
            } else {
                setBackground(g);
            }

            if (column == 6) {
                setFont(new Font("Tahoma", 1, 11));
            } else {
                setFont(new Font("Tahoma", 0, 11));
            }

            if (isSelected) {
                setBackground(new Color(0, 102, 255));
                setForeground(new Color(255, 255, 255));
            }

            setValue(value);
            return this;
        }
    }
    SimpleDateFormat dmyFmt = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dmyFmt_hhmm = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCustomer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbKota = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox();
        btnRefresh = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbKapal = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbTglBerangkat = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        panelCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Kota :");
        panelCustomer.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 20));

        cmbKota.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbKota.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbKotaItemStateChanged(evt);
            }
        });
        panelCustomer.add(cmbKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 260, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tahun : ");
        panelCustomer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 50, 20));

        cmbTahun.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 90, -1));

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
        panelCustomer.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 90, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Kapal:");
        panelCustomer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 60, 20));

        cmbKapal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbKapal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKapal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbKapalItemStateChanged(evt);
            }
        });
        panelCustomer.add(cmbKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 35, 260, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Tgl. Brkt :");
        panelCustomer.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 35, 80, 20));

        cmbTglBerangkat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cmbTglBerangkat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelCustomer.add(cmbTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 35, 120, -1));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "NO NOTA", "KEPADA", "MERK", "JUMLAH", "Jatuh Tempo", "Check"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
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

        jCheckBox1.setText("Pilih Semua");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Tgl. Jth Tempo :");

        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });

        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(273, 273, 273)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton1))
                .addGap(14, 14, 14))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-731)/2, (screenSize.height-416)/2, 731, 416);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        //udfLoadTagihan("");
        //udfSetTableLook();
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        udfLoadTagihan("");
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
    }//GEN-LAST:event_tableMouseClicked

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
                    + "tgl_berangkat::date "
                    + "from "
                    + "nota_kapal_tujuan kt "
                    + "where kt.kota_tujuan='" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "' "
                    + "and kode_kapal='" + lstKapal.get(cmbKapal.getSelectedIndex()).toString() + "' "
                    + "order by 2 desc";
            
            System.out.println(sQry);
            
            cmbTglBerangkat.removeAllItems();
            ResultSet rs = conn.createStatement().executeQuery(sQry);
            while (rs.next()) {
                cmbTglBerangkat.addItem(rs.getString("tanggal"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
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
//        udfLoadKapal();
//        cmbKota.addItemListener(new java.awt.event.ItemListener() {
//            public void itemStateChanged(java.awt.event.ItemEvent evt) {
//                udfLoadKapal();
//            }
//        });
//        cmbKapal.addItemListener(new java.awt.event.ItemListener() {
//            public void itemStateChanged(java.awt.event.ItemEvent evt) {
//                udfLoadTglBerangkat();
//            }
//        });
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmbKotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbKotaItemStateChanged
        udfLoadKapal();
    }//GEN-LAST:event_cmbKotaItemStateChanged

    private void cmbKapalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbKapalItemStateChanged
        udfLoadTglBerangkat();
    }//GEN-LAST:event_cmbKapalItemStateChanged

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed
        tanggal.setDate(jXDatePicker1.getDate());
    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        for(int i=0; i<table.getRowCount(); i++){
            table.setValueAt(jCheckBox1.isSelected(), i, table.getColumnModel().getColumnIndex("Check"));
            table.setValueAt(jCheckBox1.isSelected()? jXDatePicker1.getDate(): null, i, table.getColumnModel().getColumnIndex("Jatuh Tempo"));
            
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try{
            PreparedStatement ps=conn.prepareStatement("update nota set jt_tempo=? where no_nota=?");
            Date tanggal;
            for(int i=0; i<table.getRowCount(); i++){
                tanggal=table.getValueAt(i, table.getColumnModel().getColumnIndex("Jatuh Tempo"))==null? null: (Date)table.getValueAt(i, table.getColumnModel().getColumnIndex("Jatuh Tempo"));
                ps.setDate(1, tanggal==null? null: new java.sql.Date(tanggal.getTime()));
                ps.setString(2, table.getValueAt(i, table.getColumnModel().getColumnIndex("NO NOTA")).toString()) ;
                ps.addBatch();
            }
            ps.executeBatch();
            JOptionPane.showMessageDialog(this, "Record(s) saved successfuly!");
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    GeneralFunction fn = new GeneralFunction();

    public void udfLoadTagihan(String sNota) {
        try {
            String sQry = "";
            sQry = "select * from fn_nota_list_jatuh_tempo("
                    + "'" + lstKapal.get(cmbKapal.getSelectedIndex()) + "', "
                    + "'" + new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd/MM/yy").parse(cmbTglBerangkat.getSelectedItem().toString())) + "', "
                    + "'" + lstKota.get(cmbKota.getSelectedIndex()).toString() + "') as "
                    + "(no_nota varchar, kepada varchar, merk text, total double precision, jt_tempo date)";

            System.out.println(sQry);
            int i = 0;
            ((DefaultTableModel) table.getModel()).setNumRows(0);

            ResultSet rs = conn.createStatement().executeQuery(sQry);

            int index = 0;
            while (rs.next()) {
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                            table.getRowCount() + 1,
                            rs.getString("no_nota"),
                            rs.getString("kepada"),
                            rs.getString("merk"),
                            rs.getDouble("total"),
                            rs.getDate("jt_tempo"),
                            rs.getDate("jt_tempo")!=null
                        });
                if (sNota.equalsIgnoreCase(rs.getString("no_nota"))) {
                    index = table.getRowCount() - 1;
                }
            }


            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(index, index);
                table.setModel((DefaultTableModel) fn.autoResizeColWidth(table,
                        (DefaultTableModel) table.getModel()).getModel());
            }


            if (table.getRowCount() > 0) {
                table.requestFocusInWindow();
                table.setRowSelectionInterval(0, 0);
                table.setModel((DefaultTableModel) fn.autoResizeColWidth(table, (DefaultTableModel) table.getModel()).getModel());
            }

        } catch (ParseException ex) {
            Logger.getLogger(FrmNotaJatuhTempo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(this, se.getMessage());
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox cmbKapal;
    private javax.swing.JComboBox cmbKota;
    private javax.swing.JComboBox cmbTahun;
    private javax.swing.JComboBox cmbTglBerangkat;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
