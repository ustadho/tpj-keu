/*
 * FrmCustomer.java
 *
 * Created on November 6, 2007, 9:54 PM
 */

package tpjkeuangan;


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
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
public class FrmListTagihan extends javax.swing.JInternalFrame {
    Connection conn;
    private String sUserName;
    JTextField tx=new JTextField();
    private ListRsbm lst;
    private JDesktopPane desktopPane;
    JCheckBox checkBox = new JCheckBox();
    
    /** Creates new form FrmCustomer */
    public FrmListTagihan() {
        initComponents();
        
        txtKapal.addFocusListener(txtFoculListener);
        txtTgl.addFocusListener(txtFoculListener);
        panelTanggal.setVisible(false);
        
    } 

    void setUserName(String sUserName) {
        this.sUserName=sUserName;
    }

    void udfSetDesktopPane(JDesktopPane jDesktopPane1) {
        this.desktopPane=jDesktopPane1;
    }

    private void onOpen(String sQry){
        
        int i=1;
        try {
            modelPenerimaan=(DefaultTableModel)tblNotaTagihan.getModel();
            modelPenerimaan.setNumRows(0);
            
            ResultSet rs = conn.createStatement().executeQuery(sQry);
                
            System.out.println(sQry);
            while (rs.next()) {
                modelPenerimaan.addRow(new Object[]{
                    i,
                    rs.getString("kepada"),
                    rs.getString("no_nota"),
                    rs.getFloat("total"),
                    rs.getBoolean("is_header")
                });
                i++;
            }
            tblNotaTagihan.setModel(modelPenerimaan);
            
            if(tblNotaTagihan.getRowCount()>0)
                tblNotaTagihan.setRowSelectionInterval(0, 0);
            else
                JOptionPane.showMessageDialog(this, "Daftar nota tidak ditemukan");
            
            txtKapal.requestFocus();
            rs.close();
            
        } catch (SQLException eswl){ System.out.println(eswl.getMessage());}
        if(tblNotaTagihan.getRowCount()>0){
            tblNotaTagihan.requestFocusInWindow();
            tblNotaTagihan.setRowSelectionInterval(0,0);
        }
   }

    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            //if(evt.getSource().equals(btnAdd)) return;
            
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
                    //udfSave();
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
    
    
    private void TableLook(){
            int lbr=tblNotaTagihan.getWidth();
            
            
//        tblNotaTagihan.getColumnModel().getColumn(0).setPreferredWidth(25);
//        tblNotaTagihan.getColumnModel().getColumn(1).setPreferredWidth(100);
//        tblNotaTagihan.getColumnModel().getColumn(2).setPreferredWidth(200);
//        tblNotaTagihan.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        tblNotaTagihan.setRowHeight(18);
        for (int i=0;i<tblNotaTagihan.getColumnCount();i++){
            tblNotaTagihan.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
            
            
        if (modelPenerimaan.getRowCount() > 0) {
            tblNotaTagihan.changeSelection(0, 0,false,false);                
        } 
        //tblPenerimaan.removeColumn(tblPenerimaan.getColumnModel().getColumn(4));
     }
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            Color g1 = new Color(239,234,240);//-->>(251,236,177);// Kuning         [251,251,235]
            Color g2 = new Color(239,234,240);//-->>(241,226,167);// Kuning         [247,247,218]
            
             
            Color w1 = new Color(255,255,255);// Putih
            Color w2 = new Color(250,250,250);// Putih Juga
            
            Color h1 = new Color(255,240,240);// Merah muda
            Color h2 = new Color(250,230,230);// Merah Muda
            
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
                setHorizontalAlignment(tx.RIGHT);
                value=formatter.format(value);
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
                    checkBox.setBackground(new Color(248,255,167));//51,102,255));
                }
                return checkBox;
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
    
    private void udfDelete(){
        if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus EMKL '"+tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 1).toString()+"'", "Ustasoft Message", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try {
                Statement st=conn.createStatement();
                st.executeUpdate("Delete from emkl where kode_emkl='"+tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 0).toString()+"'");
                
                modelPenerimaan.removeRow(tblNotaTagihan.getSelectedRow());
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    private void udfPrint() {
        String sNota=tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 2).toString();
        try {
            HashMap reportParam = new HashMap();
            JasperReport jasperReport = null;
            reportParam.put("no_nota", sNota);
            reportParam.put("stempel", getClass().getResource("/image/tpj.gif").toString());
            reportParam.put("showCompany", chkTPJHeader.isSelected());
            
            System.out.println("No. Nota: "+ sNota);
            System.out.println(getClass().getResource(""));
            //String sRpt=(Boolean)modelPenerimaan.getValueAt(tblNotaTagihan.getSelectedRow(), 4)==false? "nota_cetak_v4":"nota_cetak_gabungan_v4" ;
            String sRpt=(Boolean)modelPenerimaan.getValueAt(tblNotaTagihan.getSelectedRow(), 4)==false? "nota_cetak_v53":"nota_cetak_gabungan_v53" ;
            sRpt+=(chkStempel.isSelected()? "_ttd": "");
            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/"+sRpt+".jasper"));
                           
            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
            
            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
            JasperViewer.viewReport(print, false);
            
            
                            
        } catch (JRException je) {
            System.out.println("Error report:"+je.getMessage());
        }
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
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNotaTagihan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnBayar = new javax.swing.JButton();
        btnPrintPreview = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnClear1 = new javax.swing.JButton();
        chkStempel = new javax.swing.JCheckBox();
        chkTPJHeader = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        panelKapal = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        lblKotaTujuan = new javax.swing.JLabel();
        lblKapal = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTgl = new javax.swing.JTextField();
        lblSerialKapal = new javax.swing.JLabel();
        panelCustomer = new javax.swing.JPanel();
        txtCustomer = new javax.swing.JTextField();
        lblCustomerTujuan = new javax.swing.JLabel();
        optToko = new javax.swing.JRadioButton();
        optCustomer = new javax.swing.JRadioButton();
        btnPreview = new javax.swing.JButton();
        panelTanggal = new javax.swing.JPanel();
        lblTglAwal = new javax.swing.JLabel();
        jDateAwal = new org.jdesktop.swingx.JXDatePicker();
        lblTglAkhir = new javax.swing.JLabel();
        jDateAkhir = new org.jdesktop.swingx.JXDatePicker();
        jLabel10 = new javax.swing.JLabel();
        cmbTahun = new javax.swing.JComboBox();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Daftar Nota Tagihan");
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

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tblNotaTagihan.setAutoCreateRowSorter(true);
        tblNotaTagihan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "No", "Kepada", "No. Nota", "Jumlah", "IsHead"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNotaTagihan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblNotaTagihan.getTableHeader().setReorderingAllowed(false);
        tblNotaTagihan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNotaTagihanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNotaTagihan);
        tblNotaTagihan.getColumnModel().getColumn(0).setResizable(false);
        tblNotaTagihan.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblNotaTagihan.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblNotaTagihan.getColumnModel().getColumn(2).setPreferredWidth(170);
        tblNotaTagihan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblNotaTagihan.getColumnModel().getColumn(3).setMaxWidth(400);
        tblNotaTagihan.getColumnModel().getColumn(4).setPreferredWidth(40);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBayar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/edit_32.png"))); // NOI18N
        btnBayar.setText("Bayar Tagihan");
        btnBayar.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnBayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBayarMouseClicked(evt);
            }
        });
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        jPanel3.add(btnBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 9, 140, 34));

        btnPrintPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/print_32.png"))); // NOI18N
        btnPrintPreview.setText("Print Nota");
        btnPrintPreview.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnPrintPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPreviewActionPerformed(evt);
            }
        });
        jPanel3.add(btnPrintPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 9, 100, 34));

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/new_32.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClear.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel3.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 90, 34));

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/exit_32.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClose.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel3.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 80, 34));

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/edit_32.png"))); // NOI18N
        btnClear1.setText("Detail");
        btnClear1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnClear1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnClear1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 100, 34));

        chkStempel.setText("Stempel & TTD");
        jPanel3.add(chkStempel, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 20, 100, -1));

        chkTPJHeader.setText("Tampilkan Header TPJ");
        jPanel3.add(chkTPJHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 150, -1));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Per kapal berangkat", "Per customer/ toko" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectedIndex(0);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new java.awt.CardLayout());

        panelKapal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("KAPAL");
        panelKapal.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 50, -1));

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
        panelKapal.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 60, 24));

        lblKotaTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaTujuan.setOpaque(true);
        panelKapal.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 37, 240, 24));

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);
        panelKapal.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 400, 24));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Tgl. Berangkat");
        panelKapal.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 37, 100, 20));

        txtTgl.setFont(new java.awt.Font("Dialog", 0, 12));
        txtTgl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtTgl.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtTgl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTglFocusLost(evt);
            }
        });
        txtTgl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTglKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTglKeyReleased(evt);
            }
        });
        panelKapal.add(txtTgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 37, 90, 24));

        lblSerialKapal.setText("1");
        panelKapal.add(lblSerialKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 40, -1));

        jPanel6.add(panelKapal, "card2");

        panelCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtCustomer.setFont(new java.awt.Font("Dialog", 0, 12));
        txtCustomer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtCustomer.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtCustomer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerFocusLost(evt);
            }
        });
        txtCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCustomerKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCustomerKeyReleased(evt);
            }
        });
        panelCustomer.add(txtCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 60, 24));

        lblCustomerTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblCustomerTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblCustomerTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblCustomerTujuan.setOpaque(true);
        panelCustomer.add(lblCustomerTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 400, 24));

        buttonGroup1.add(optToko);
        optToko.setText("Toko pengirim");
        panelCustomer.add(optToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 3, 110, -1));

        buttonGroup1.add(optCustomer);
        optCustomer.setSelected(true);
        optCustomer.setText("Customer tujuan");
        panelCustomer.add(optCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 3, 110, -1));

        jPanel6.add(panelCustomer, "card4");

        btnPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/refresh_32.png"))); // NOI18N
        btnPreview.setMnemonic('P');
        btnPreview.setText("Refresh");
        btnPreview.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPreview.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        panelTanggal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTglAwal.setText("Dari");
        panelTanggal.add(lblTglAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 20));
        panelTanggal.add(jDateAwal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        lblTglAkhir.setText("S/D");
        panelTanggal.add(lblTglAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 30, 20));
        panelTanggal.add(jDateAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tahun");
        panelTanggal.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 50, 20));

        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2020" }));
        panelTanggal.add(cmbTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, -1));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(panelTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPreview))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(panelTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isTagihanOn=false;
        lst.setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosed

    private void tblNotaTagihanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNotaTagihanMouseClicked
        
}//GEN-LAST:event_tblNotaTagihanMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
        
        modelPenerimaan=(DefaultTableModel)tblNotaTagihan.getModel();
        modelPenerimaan.setNumRows(0)    ;
        tblNotaTagihan.setModel(modelPenerimaan);
        MyKeyListener kListener=new MyKeyListener();
        jScrollPane1.addKeyListener(kListener);
        tblNotaTagihan.addKeyListener(kListener);
         
        System.out.println(panelKapal.getComponentCount());
        for(int i=0;i<panelKapal.getComponentCount();i++){
            Component c = panelKapal.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton")   || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        TableLook();
        requestFocusInWindow(true);
        tblNotaTagihan.requestFocusInWindow();
        
        cmbTahun.setSelectedItem(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        
    }//GEN-LAST:event_formInternalFrameOpened

    private String getCriteria(){
        String sRtn="";
        switch(jList1.getSelectedIndex()){
            case 0:{
                sRtn="and seri_kapal="+Integer.parseInt(lblSerialKapal.getText()) ;
                break;
            }
            case 1:{
                sRtn=   "and tagihan_per='"+(optCustomer.isSelected()? "T": "P")+ "' " +
                        "and customer iLike '"+txtCustomer.getText()+"%' " +
                        "and to_Char(tgl_nota, 'yyyy-MM-dd')>='"+new SimpleDateFormat("yyyy-MM-dd").format(jDateAwal.getDate())+"' " +
                        "and to_Char(tgl_nota, 'yyyy-MM-dd')<='"+new SimpleDateFormat("yyyy-MM-dd").format(jDateAkhir.getDate())+"' " +
                        "and substring(nota.no_nota from '..$')='"+cmbTahun.getSelectedItem().toString().substring(2, 4)+"' ";
                break;
            }
        }
        
        return sRtn;
    }
    
    private String getCriteria2(){
        String sRtn="";
        switch(jList1.getSelectedIndex()){
            case 0:{
                sRtn="and nh.seri_kapal="+Integer.parseInt(lblSerialKapal.getText()) ;
                break;
            }
            case 1:{
                sRtn=   "and nh.tagihan_per='"+(optCustomer.isSelected()? "T": "P")+ "' " +
                        "and nh.customer iLike '"+txtCustomer.getText()+"%' " +
                        "and to_Char(nh.tgl_nota, 'yyyy-MM-dd')>='"+new SimpleDateFormat("yyyy-MM-dd").format(jDateAwal.getDate())+"' " +
                        "and to_Char(nh.tgl_nota, 'yyyy-MM-dd')<='"+new SimpleDateFormat("yyyy-MM-dd").format(jDateAkhir.getDate())+"' " +
                        "and substring(nh.no_nota from '..$')='"+cmbTahun.getSelectedItem().toString().substring(2, 4)+"' ";
                break;
                //and nh.seri_kapal="+Integer.parseInt(lblSerialKapal.getText())+"
            }
        }
        
        return sRtn;
    }
    
    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        String sQry="";
        
        if(txtKapal.getText().equalsIgnoreCase("") && jList1.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(this, "Silakan pilih kapal dengan tanggal berangkat yang spesifik!");
            return;
        }
//            sQry="select kepada, nota.no_nota, sum(case when is_header=true then coalesce(d2.sub_total,0) else coalesce(d.sub_total,0) end) as total ," +
//                "coalesce(is_header, false) as is_header " +
//                "from nota " +
//                "left join nota_detail d on d.no_nota=nota.no_nota " +
//                "left join nota_detail d2 on d2.no_nota=nota.nota_header " +
//                "where coalesce(seri_kapal,0)=0 " +
//                "group by kepada, nota.no_nota, coalesce(is_header, false)   order by nota.no_nota";
            
                sQry=   "select kepada, no_nota, sum(total) as total, is_header from" +
                        "(select kepada, d.no_nota, sum(sub_total) as total, coalesce(is_header, false) as is_header " +
                        "from nota " +
                        "inner join nota_detail d on d.no_nota=nota.no_nota " +
                        "where is_batal=false and trim(coalesce(nota_header,''))='' " +
                        ""+getCriteria()+" " +
                        "group by kepada, d.no_nota, coalesce(is_header, false) " +
                        "union all " +
                        "select nh.kepada, nh.no_nota, sum(sub_total) as total, coalesce(nh.is_header, false) as is_header " +
                        "from nota " +
                        "inner join nota nh on nh.no_nota=nota.nota_header " +
                        "inner join nota_detail d on d.no_nota=nota.no_nota " +
                        "where nh.is_batal=false and trim(coalesce(nota.nota_header,''))<>'' " +
                        ""+getCriteria2()+" " +
                        "group by nh.kepada, nh.no_nota, coalesce(nh.is_header, false)) a group by kepada, no_nota,is_header";
//        else
//            sQry="select kepada, nota.no_nota, sum(case when is_header=true then coalesce(d2.sub_total,0) else coalesce(d.sub_total,0) end) as total ," +
//                "coalesce(is_header, false) as is_header " +
//                "from nota " +
//                "left join nota_detail d on d.no_nota=nota.no_nota " +
//                "left join nota_detail d2 on d2.no_nota=nota.nota_header " +
//                "where seri_kapal ="+Integer.parseInt(lblSerialKapal.getText())+" " +
//                "and coalesce(nota.nota_hseri_kapal ="+Integer.parseInt(lblSerialKapal.getText())eader,'')='' " +
//                "group by kepada, nota.no_nota, coalesce(is_header, false)   order by nota.no_nota";
        System.out.println(sQry);
        onOpen(sQry);
    }//GEN-LAST:event_btnPreviewActionPerformed

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
//            Logger.getLogger(FrmListTagihan.class.getName()).log(Level.SEVERE, null, ex);
//        }
//             
//    }
    
    private void btnPrintPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPreviewActionPerformed
        if(tblNotaTagihan.getSelectedRow()>=0){
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            udfPrint();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btnPrintPreviewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtKapal.setText("");
        lblKapal.setText("");
        lblKotaTujuan.setText("");
        txtTgl.setText("");
        
        lblSerialKapal.setText("");
        modelPenerimaan.setNumRows(0);
        txtKapal.requestFocus();
        
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnBayarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBayarMouseClicked
        // TODO add your handling code here:
        if(tblNotaTagihan.getSelectedRow()>=0){
            int iRow=tblNotaTagihan.getSelectedRow();
            FrmNotaBayarDetail dTrx=new FrmNotaBayarDetail();
            dTrx.setConn(conn);
            dTrx.setNoNota(tblNotaTagihan.getValueAt(iRow, 2).toString());
            dTrx.setTitle("No. Nota ~ "+tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 2).toString());
            dTrx.setUserName(sUserName);
            dTrx.setVisible(true);
        }
        
//        int iRow=tblPenerimaan.getSelectedRow();
//        if (!Utama.isDetailBayarNota && iRow>=0){
//            FrmNotaBayarDetail dTrx=new FrmNotaBayarDetail();
//            dTrx.setConn(conn);
//            dTrx.setNoNota(tblPenerimaan.getValueAt(iRow, 2).toString());
//            dTrx.setTitle("No. Nota ~ "+tblPenerimaan.getValueAt(tblPenerimaan.getSelectedRow(), 2).toString());
//            dTrx.setVisible(true);
//            dTrx.setUserName(sUserName);
//            desktopPane.add(dTrx, javax.swing.JLayeredPane.MODAL_LAYER);
//            try{
//                    dTrx.setBounds(Utama.iLeft+ this.getX(), Utama.iTop + this.getY(), dTrx.getWidth(), dTrx.getHeight());
//                    dTrx.setMaximum(true);
//                    this.setMaximum(true);
//                    dTrx.setSelected(true);
//                } catch(PropertyVetoException PO){
//                }
//            Utama.isDetailBayarNota=true;
//        }
//        else{
//            JInternalFrame ji[] = desktopPane.getAllFrames();
//            for(int i=0;i<ji.length;i++){
//                System.out.println(ji[i].getTitle());
//                
//                if(ji[i].getTitle().equalsIgnoreCase("No. Nota ~ "+tblPenerimaan.getValueAt(tblPenerimaan.getSelectedRow(), 2).toString())){
//                    try{
//                        ji[i].setSelected(true);
//                    } catch(PropertyVetoException PO){
//                    }
//                    break;
//                }
//            }
//            
//        }
        
    }//GEN-LAST:event_btnBayarMouseClicked

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBayarActionPerformed

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
                            lblKapal.setText(obj[1].toString());
                            //lblTglBerangkat.setText(obj[2].toString());
//                            lblKotaTujuan.setText(obj[3].toString());
//                            lblSerialKapal.setText(obj[4].toString());
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
//                    lblKotaTujuan.setText("");
//                    lblTglBerangkat.setText("");
//                    lblSerialKapal.setText("");
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
//                        String sQry="select tj.kode_kapal, nama_kapal , to_Char(tgl_berangkat, 'dd-MM-yyyy') as tgl_berangkat, " +
//                                "coalesce(nama_kota,'') as kota_tujuan, serial_kode " +
//                                "from nota_kapal_tujuan tj " +
//                                "inner join kapal on kapal.kode_kapal=tj.kode_kapal " +
//                                "left join kota on kode_kota=kota_tujuan " +
//                                "where (tj.kode_kapal||nama_kapal||coalesce(nama_kota,'') ) iLike '%"+txtKapal.getText()+"%' and active=true limit 100";
                        
                        String sQry="select distinct tj.kode_kapal, nama_kapal " +
                                "from nota_kapal_tujuan tj " +
                                "inner join kapal on kapal.kode_kapal=tj.kode_kapal " +
                                "where (tj.kode_kapal||nama_kapal) iLike '%"+txtKapal.getText()+"%' and active=true limit 100";
                        
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.txtKapal.getLocationOnScreen().x,
                                txtKapal.getLocationOnScreen().y + txtKapal.getHeight(),
                                txtKapal.getWidth()+lblKapal.getWidth()+jLabel12.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblKapal.getWidth());
//                        lst.udfRemoveColumn(4);
                        
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKapal.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKapal.setText("");
                            lblKapal.setText("");
//                            lblKotaTujuan.setText("");
//                            lblSerialKapal.setText("0");
                            txtKapal.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtKapalKeyReleased

    private void txtTglFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTglFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtTglFocusLost

    private void txtTglKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTglKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTglKeyPressed

    private void txtTglKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTglKeyReleased
        try {
            switch(evt.getKeyCode()) {
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtTgl.setText(obj[0].toString());
                            lblKotaTujuan.setText(obj[1].toString());
                            lblSerialKapal.setText(obj[2].toString());
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
                    txtTgl.setText("");
                    lblKotaTujuan.setText("");
                    lblSerialKapal.setText("");
                    
                    txtTgl.requestFocus();
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
                        String sQry="select distinct coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'), '') as tgl_berangkat,  " +
                                "coalesce(nama_kota,'') as kota_tujuan, serial_kode " +
                                "from nota_kapal_tujuan tj " +
                                "left join kota on kota.kode_kota=tj.kota_tujuan " +
                                "where kode_kapal='"+txtKapal.getText()+"' and " +
                                "(coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'), '')||coalesce(nama_kota,'')||serial_kode::varchar) " +
                                "iLike '%"+txtTgl.getText()+"%' and active=true limit 100";
                        
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+ this.jPanel5.getX()+this.jPanel6.getX()+this.panelKapal.getX()+ this.txtTgl.getX()+17,
                                Utama.iTop + this.getY() + this.jPanel5.getY()+this.jPanel6.getY()+this.panelKapal.getX()+this.txtTgl.getY() + txtTgl.getHeight()+77,
                                350,
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtTgl);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKotaTujuan, lblSerialKapal});
                        lst.setColWidth(0, txtTgl.getWidth());
//                        lst.udfRemoveColumn(1);
//                        lst.udfRemoveColumn(1);
                        
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtTgl.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtTgl.setText("");
                            lblKotaTujuan.setText("");
                            lblSerialKapal.setText("");
//                            lblSerialKapal.setText("0");
                            txtTgl.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtTglKeyReleased

private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
    int iRow=tblNotaTagihan.getSelectedRow();
    if(iRow >=0){
        if((Boolean) tblNotaTagihan.getValueAt(iRow, 4)==true){ //NOta gabungan
            FrmNotaGabung d1=new FrmNotaGabung();
            d1.setUserName(sUserName);
            d1.setConn(conn);
            d1.setIsKoreksi(true);
            d1.setNoNota(tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 2).toString());
            d1.setTitle("Koreksi pembayaran nota : "+tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 0).toString());
            d1.udfLoadDetailKoreksi();
            desktopPane.add(d1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            d1.setVisible(true);
            try{
                d1.setBounds(this.getX(), this.getY(), d1.getWidth(), d1.getHeight());
                d1.setSelected(true);
            } catch(PropertyVetoException PO){
            }
        }else{
            FrmNota d1=new FrmNota();
            d1.setUserName(sUserName);
            d1.setConn(conn);
            d1.setKoreksi(true);
            d1.setNoNota(tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 2).toString());
            d1.setTitle("Koreksi pembayaran nota : "+tblNotaTagihan.getValueAt(tblNotaTagihan.getSelectedRow(), 0).toString());
            d1.udfLoadDetailKoreksi();
            desktopPane.add(d1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            d1.setVisible(true);
            try{
                d1.setBounds(this.getX(), this.getY(), d1.getWidth(), d1.getHeight());
                d1.setSelected(true);
            } catch(PropertyVetoException PO){
            }
        
        }
    }
}//GEN-LAST:event_btnClear1ActionPerformed

private void txtCustomerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtCustomerFocusLost

private void txtCustomerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerKeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_txtCustomerKeyPressed

private void txtCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerKeyReleased
try {
            String sCari = txtCustomer.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtCustomer.setText(obj[0].toString());
                            lblCustomerTujuan.setText(obj[1].toString());
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
                    txtCustomer.setText("");
                    lblCustomerTujuan.setText("");
                    txtCustomer.requestFocus();
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
                        //sQry=getQuery();
                        
                        ///System.out.println(sQry);
                        lst.setSQuery(getQuery());
                        
                        lst.setBounds(txtCustomer.getLocationOnScreen().x,
                                txtCustomer.getLocationOnScreen().y+txtCustomer.getHeight(),
                                txtCustomer.getWidth()+lblCustomerTujuan.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtCustomer);
                        lst.setLblDes(new javax.swing.JLabel[]{lblCustomerTujuan});
                        lst.setColWidth(0, txtCustomer.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtCustomer.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtCustomer.setText("");
                            lblCustomerTujuan.setText("");
                            
                            txtCustomer.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtCustomerKeyReleased

private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
    panelTanggal.setVisible(false);
    panelCustomer.setVisible(false);
    panelKapal.setVisible(false);
    
    switch(jList1.getSelectedIndex()){
        case 0:{
            panelKapal.setVisible(true);
            break;
        }
        case 1:{
            panelTanggal.setVisible(true);
            panelCustomer.setVisible(true);
            break;
        }
        
    }
}//GEN-LAST:event_jList1ValueChanged

    void setCon(Connection conn) {
        this.conn=conn;
    }
    
    private String getQuery(){
        String sQry="";
        if(optCustomer.isSelected()){
            sQry="select c.kode_cust as kode,  coalesce(c.nama,'') as toko, " +
                 "coalesce(c.nama,'') ||' ('|| gabung(merk.merk)||'), ' ||coalesce(nama_kota,'') as kepada " +
                 "from customer c inner join merk on c.kode_cust=merk.kode_cust " +
                 "left join kota on kota.kode_kota=c.kota " +
                 "where (coalesce(c.kode_cust,'')||coalesce(c.nama,'')||coalesce(merk.merk,'')||coalesce(nama_kota,'')) iLike '%"+txtCustomer.getText()+"%' " +
                 "and c.kota like '%"+Utama.sKodeKota+"%' " +
                 "group by c.kode_cust, coalesce(c.nama,''), coalesce(nama_kota,'') " +
                 "order by coalesce(c.nama,'')";
                    
            return sQry;
        }
        
        if(optToko.isSelected()){
            sQry=   "select kode_toko, coalesce(nama,'') as nama, coalesce(nama,'') as kepada " +
                    "from toko where kode_toko||coalesce(nama,'') iLike '%"+txtCustomer.getText()+"%' order by coalesce(nama,'') ";
            
            return sQry;
        }
        
        return sQry;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnPrintPreview;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkStempel;
    private javax.swing.JCheckBox chkTPJHeader;
    private javax.swing.JComboBox cmbTahun;
    private org.jdesktop.swingx.JXDatePicker jDateAkhir;
    private org.jdesktop.swingx.JXDatePicker jDateAwal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCustomerTujuan;
    private javax.swing.JLabel lblKapal;
    private javax.swing.JLabel lblKotaTujuan;
    private javax.swing.JLabel lblSerialKapal;
    private javax.swing.JLabel lblTglAkhir;
    private javax.swing.JLabel lblTglAwal;
    private javax.swing.JRadioButton optCustomer;
    private javax.swing.JRadioButton optToko;
    private javax.swing.JPanel panelCustomer;
    private javax.swing.JPanel panelKapal;
    private javax.swing.JPanel panelTanggal;
    private javax.swing.JTable tblNotaTagihan;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtKapal;
    private javax.swing.JTextField txtTgl;
    // End of variables declaration//GEN-END:variables

    private DefaultTableModel modelPenerimaan;
    DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat formatter = new DecimalFormat("#,###,###");
    
    Color g1 = new Color(153,255,255);
    Color g2 = new Color(255,255,255); 
    
    Color fHitam = new Color(0,0,0);
    Color fPutih = new Color(255,255,255); 
    
    Color crtHitam =new java.awt.Color(0, 0, 0);
    Color crtPutih = new java.awt.Color(255, 255, 255); 
    
}
