/*
 * frmRptKasir.java
 *
 * Created on November 30, 2005, 7:04 PM
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author  root
 */
public class FrmRptPackingListBaru extends javax.swing.JInternalFrame {
    
    private Connection conn;
    private String sKodeJenis="";
    private String sUser="";
    private DefaultListModel modelJenis, modelTrx;
    private ListRsbm lst;
    Color g1 = new Color(153,255,255);
    Color g2 = new Color(255,255,255); 
    DateFormat fmtYMD = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat fmtDMY = new SimpleDateFormat("dd-MM-yyyy");
     
    public void setCon(Connection con){
        conn=con;
    }
    
    private FocusListener txtFoculListener=new FocusListener() {
        public void focusGained(FocusEvent e) {
           Component c=(Component) e.getSource();
           c.setBackground(g1);
           //c.setForeground(fPutih);
           //c.setCaretColor(new java.awt.Color(255, 255, 255));
        }
        public void focusLost(FocusEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(g2);
            //c.setForeground(fHitam);
        }
   };
   
    /** Creates new form frmRptKasir */
    public FrmRptPackingListBaru() {
        initComponents();
        txtSatu.addFocusListener(txtFoculListener);
        txtKapal.addFocusListener(txtFoculListener);
        
    }

    void setUserName(String sUserName) {
        sUser="";
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
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
                
                 
                case KeyEvent.VK_ESCAPE: {
                    if (!lst.isVisible())
                        dispose();
                    else
                        lst.setVisible(false);
                    break;
                }
                
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
    
    private void udfLoadJenis(){
        try {
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery("select kode_jenis, coalesce(jenis_transaksi,'') as jenis_transaksi " +
                    "from fisio_jenis_trx order by 1");
            
            modelJenis=new DefaultListModel();
            
            modelJenis.addElement("");
            
            while(rs.next()){
                //cmbJenis.addItem(rs.getString("jenis_transaksi"));
                modelJenis.addElement(rs.getString("kode_jenis"));
            }
            rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
    }
    
    private void setFocusBlockTxt(JTextField txt){
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupKotaTujuan = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        btnPreview = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        panelKontainer = new javax.swing.JPanel();
        txtSatu = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        chkExpedisi = new javax.swing.JCheckBox();
        lblTglBerangkat = new javax.swing.JLabel();
        lblSerialKon = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        panelKapal = new javax.swing.JPanel();
        lblKapal = new javax.swing.JLabel();
        txtKapal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbTglBerangkat = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        panelKotaTujuan = new javax.swing.JPanel();
        lblKotaTujuan = new javax.swing.JLabel();
        txtKotaTujuan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbTglBerangkatKota = new javax.swing.JComboBox();
        radioKota1 = new javax.swing.JRadioButton();
        radioKota2 = new javax.swing.JRadioButton();
        jcDateKota2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jcDateKota1 = new org.jdesktop.swingx.JXDatePicker();
        panelEMKL = new javax.swing.JPanel();
        lblEmkl = new javax.swing.JLabel();
        txtEmkl = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cmbTglBerangkatEMKL = new javax.swing.JComboBox();
        radioEmkl1 = new javax.swing.JRadioButton();
        radioEmkl2 = new javax.swing.JRadioButton();
        jcDateEmkl2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel3 = new javax.swing.JLabel();
        jcDateEmkl1 = new org.jdesktop.swingx.JXDatePicker();
        panelPenerimaan = new javax.swing.JPanel();
        txtTokoPengirim = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        lblTokoPengirim = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtKotaPenerimaan = new javax.swing.JTextField();
        lblKotaPenerimaan = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtTokoTujuan = new javax.swing.JTextField();
        lblTokoTujuan = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMerk = new javax.swing.JTextField();
        jcDateTerima1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        jcDateTerima2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel18 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setForeground(new java.awt.Color(0, 0, 0));
        setTitle("Report");
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setBackground(new java.awt.Color(51, 51, 255));
        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 1, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Packing List");
        jLabel5.setOpaque(true);
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 5, 590, 23));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Packing List per Kontainer", "Packing List per Kapal", "Packing List per Kota Tujuan", "Packing List per EMKL", "Packing List per Penerimaan" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 33, 210, 250));

        jLabel2.setText("Pilih jenis report");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 15, 230, -1));

        btnPreview.setMnemonic('P');
        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });
        jPanel2.add(btnPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 294, 90, 27));

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jPanel2.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(506, 294, 80, 27));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.CardLayout());

        panelKontainer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSatu.setFont(new java.awt.Font("Dialog", 0, 12));
        txtSatu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtSatu.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtSatu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSatuActionPerformed(evt);
            }
        });
        txtSatu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSatuFocusGained(evt);
            }
        });
        txtSatu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSatuKeyReleased(evt);
            }
        });
        panelKontainer.add(txtSatu, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 31, 180, 24));

        jLabel8.setText("Tgl. Berangkat");
        panelKontainer.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 14, 86, 20));

        chkExpedisi.setText("Expedisi Report");
        panelKontainer.add(chkExpedisi, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 214, 110, -1));

        lblTglBerangkat.setBackground(new java.awt.Color(255, 255, 255));
        lblTglBerangkat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblTglBerangkat.setOpaque(true);
        panelKontainer.add(lblTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 31, 90, 24));

        lblSerialKon.setForeground(new java.awt.Color(0, 0, 255));
        lblSerialKon.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSerialKon.setText("lblSerialKon");
        panelKontainer.add(lblSerialKon, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 54, 90, 20));

        jLabel9.setText("No. Container");
        panelKontainer.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 14, 86, 20));

        jPanel1.add(panelKontainer, "card2");

        panelKapal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblKapal.setBackground(new java.awt.Color(255, 255, 255));
        lblKapal.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKapal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKapal.setOpaque(true);
        panelKapal.add(lblKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 26, 290, 24));

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
        panelKapal.add(txtKapal, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 26, 60, 24));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Kapal");
        panelKapal.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 80, 20));

        cmbTglBerangkat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTglBerangkatItemStateChanged(evt);
            }
        });
        panelKapal.add(cmbTglBerangkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 92, 130, 24));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setText("Tgl. Berangkat");
        panelKapal.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 74, -1, 20));

        jPanel1.add(panelKapal, "card4");

        panelKotaTujuan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblKotaTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaTujuan.setOpaque(true);
        panelKotaTujuan.add(lblKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 26, 290, 24));

        txtKotaTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKotaTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKotaTujuan.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtKotaTujuan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKotaTujuanFocusLost(evt);
            }
        });
        txtKotaTujuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKotaTujuanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKotaTujuanKeyReleased(evt);
            }
        });
        panelKotaTujuan.add(txtKotaTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 26, 60, 24));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setText("Kota tujuan");
        panelKotaTujuan.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 80, 20));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tanggal berangkat"));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbTglBerangkatKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTglBerangkatKotaItemStateChanged(evt);
            }
        });
        jPanel3.add(cmbTglBerangkatKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 29, 100, 24));

        buttonGroupKotaTujuan.add(radioKota1);
        radioKota1.setSelected(true);
        radioKota1.setText("Per tanggal");
        radioKota1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioKota1ActionPerformed(evt);
            }
        });
        jPanel3.add(radioKota1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 30, 90, -1));

        buttonGroupKotaTujuan.add(radioKota2);
        radioKota2.setText("Antara");
        radioKota2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioKota2ActionPerformed(evt);
            }
        });
        jPanel3.add(radioKota2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 65, 70, -1));

        jcDateKota2.setEnabled(false);
        jPanel3.add(jcDateKota2, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 66, -1, -1));

        jLabel1.setText("S/d");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 69, 30, 20));

        jcDateKota1.setEnabled(false);
        jPanel3.add(jcDateKota1, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 66, -1, -1));

        panelKotaTujuan.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 63, 350, 170));

        jPanel1.add(panelKotaTujuan, "card4");

        panelEMKL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblEmkl.setBackground(new java.awt.Color(255, 255, 255));
        lblEmkl.setFont(new java.awt.Font("Dialog", 0, 12));
        lblEmkl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblEmkl.setOpaque(true);
        panelEMKL.add(lblEmkl, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 26, 290, 24));

        txtEmkl.setFont(new java.awt.Font("Dialog", 0, 12));
        txtEmkl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtEmkl.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtEmkl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmklFocusLost(evt);
            }
        });
        txtEmkl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmklKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmklKeyReleased(evt);
            }
        });
        panelEMKL.add(txtEmkl, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 26, 60, 24));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("EMKL");
        panelEMKL.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 80, 20));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Tanggal berangkat"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbTglBerangkatEMKL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTglBerangkatEMKLItemStateChanged(evt);
            }
        });
        jPanel4.add(cmbTglBerangkatEMKL, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 29, 100, 24));

        buttonGroupKotaTujuan.add(radioEmkl1);
        radioEmkl1.setSelected(true);
        radioEmkl1.setText("Per tanggal");
        radioEmkl1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEmkl1ActionPerformed(evt);
            }
        });
        jPanel4.add(radioEmkl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 30, 90, -1));

        buttonGroupKotaTujuan.add(radioEmkl2);
        radioEmkl2.setText("Antara");
        radioEmkl2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioEmkl2ActionPerformed(evt);
            }
        });
        jPanel4.add(radioEmkl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 65, 70, -1));

        jcDateEmkl2.setEnabled(false);
        jPanel4.add(jcDateEmkl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 66, -1, -1));

        jLabel3.setText("S/d");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 69, 30, 20));

        jcDateEmkl1.setEnabled(false);
        jPanel4.add(jcDateEmkl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 66, -1, -1));

        panelEMKL.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 63, 350, 170));

        jPanel1.add(panelEMKL, "card4");

        panelPenerimaan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTokoPengirim.setFont(new java.awt.Font("Dialog", 0, 12));
        txtTokoPengirim.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtTokoPengirim.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtTokoPengirim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokoPengirimFocusLost(evt);
            }
        });
        txtTokoPengirim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTokoPengirimKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTokoPengirimKeyReleased(evt);
            }
        });
        panelPenerimaan.add(txtTokoPengirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 26, 60, 24));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Toko Pengirim");
        panelPenerimaan.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 80, 20));

        lblTokoPengirim.setBackground(new java.awt.Color(255, 255, 255));
        lblTokoPengirim.setFont(new java.awt.Font("Dialog", 0, 12));
        lblTokoPengirim.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTokoPengirim.setOpaque(true);
        panelPenerimaan.add(lblTokoPengirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 26, 290, 24));

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setText("Kota tujuan");
        panelPenerimaan.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 59, 80, 20));

        txtKotaPenerimaan.setFont(new java.awt.Font("Dialog", 0, 12));
        txtKotaPenerimaan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtKotaPenerimaan.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtKotaPenerimaan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKotaPenerimaanFocusLost(evt);
            }
        });
        txtKotaPenerimaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKotaPenerimaanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKotaPenerimaanKeyReleased(evt);
            }
        });
        panelPenerimaan.add(txtKotaPenerimaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 78, 60, 24));

        lblKotaPenerimaan.setBackground(new java.awt.Color(255, 255, 255));
        lblKotaPenerimaan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblKotaPenerimaan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblKotaPenerimaan.setOpaque(true);
        panelPenerimaan.add(lblKotaPenerimaan, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 78, 290, 24));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Toko tujuan");
        panelPenerimaan.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 112, 80, 20));

        txtTokoTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        txtTokoTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtTokoTujuan.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtTokoTujuan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokoTujuanFocusLost(evt);
            }
        });
        txtTokoTujuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTokoTujuanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTokoTujuanKeyReleased(evt);
            }
        });
        panelPenerimaan.add(txtTokoTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 131, 60, 24));

        lblTokoTujuan.setBackground(new java.awt.Color(255, 255, 255));
        lblTokoTujuan.setFont(new java.awt.Font("Dialog", 0, 12));
        lblTokoTujuan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTokoTujuan.setOpaque(true);
        panelPenerimaan.add(lblTokoTujuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 131, 290, 24));

        jLabel10.setText("Tgl. Penerimaan");
        panelPenerimaan.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 212, 100, 20));

        txtMerk.setFont(new java.awt.Font("Dialog", 0, 12));
        txtMerk.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtMerk.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtMerk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMerkActionPerformed(evt);
            }
        });
        txtMerk.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMerkFocusGained(evt);
            }
        });
        txtMerk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMerkKeyReleased(evt);
            }
        });
        panelPenerimaan.add(txtMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 167, 180, 24));
        panelPenerimaan.add(jcDateTerima1, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 210, -1, -1));

        jLabel4.setText("S/d");
        panelPenerimaan.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 213, 30, 20));
        panelPenerimaan.add(jcDateTerima2, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 210, -1, -1));

        jLabel18.setText("Merk");
        panelPenerimaan.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 167, 86, 20));

        jPanel1.add(panelPenerimaan, "card4");

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 36, 360, 250));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 29, 590, 330));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-611)/2, (screenSize.height-399)/2, 611, 399);
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isRptPackingList=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        lst = new ListRsbm();
        lst.setVisible(false);
        lst.setSize(500, 200);
        lst.con = conn;
            
        modelTrx=new DefaultListModel();
        jList1.setSelectedIndex(0);
        
        jcDateTerima1.setFormats("dd-MM-yyyy");
        jcDateTerima2.setFormats("dd-MM-yyyy");
        
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
        for(int i=0;i<panelKontainer.getComponentCount();i++){
            Component c = panelKontainer.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<panelKapal.getComponentCount();i++){
            Component c = panelKapal.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<panelKotaTujuan.getComponentCount();i++){
            Component c = panelKotaTujuan.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<panelEMKL.getComponentCount();i++){
            Component c = panelEMKL.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<panelPenerimaan.getComponentCount();i++){
            Component c = panelPenerimaan.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox") 
            || c.getClass().getSimpleName().equalsIgnoreCase("JRadioButton")        ) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(kListener);
            }
        }
    }//GEN-LAST:event_formInternalFrameOpened
            
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
        
    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        udfPrint();
        
    }//GEN-LAST:event_btnPreviewActionPerformed

    private void txtSatuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSatuActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtSatuActionPerformed

    private void txtSatuFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSatuFocusGained
//        txtSatu.setSelectionStart(0);
//        txtSatu.setSelectionEnd(txtSatu.getText().length());
}//GEN-LAST:event_txtSatuFocusGained

    private void txtSatuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSatuKeyReleased
        try {
            String sCari = txtSatu.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtSatu.setText(obj[0].toString());
                            lblTglBerangkat.setText(obj[1].toString());
                            lblSerialKon.setText(obj[2].toString());
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
                    txtSatu.setText("");
                    
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
                      
                        String sQry="select no_spnu, coalesce(to_char(tgl_berangkat, 'dd-MM-yyyy'), '') as tgl_berangkat, serial_kon " +
                                "from kontainer where active=true and no_spnu ilike upper('%"+sCari+"%') " +
                                "and coalesce(kode_kota,'') iLike '"+Utama.sKodeKota+"%' and active=true order by 1";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX() + this.jPanel2.getX()+this.jPanel1.getX()+panelKontainer.getX()+ this.txtSatu.getX()+6,
                                Utama.iTop+this.getY()+this.jPanel2.getY()+ this.jPanel1.getY()+this.txtSatu.getY()+panelKontainer.getY() + txtSatu.getHeight()+75,
                                txtSatu.getWidth()+40,
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtSatu);
                        lst.setLblDes(new javax.swing.JLabel[]{lblTglBerangkat, lblSerialKon});
                        lst.setColWidth(0, 120);
                        lst.udfRemoveColumn(2);
                        //lst.udfRemoveColumn(3);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtSatu.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtSatu.setText("");
                            txtSatu.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtSatuKeyReleased

    
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        panelKontainer.setVisible(false);
        panelKapal.setVisible(false);
        panelKotaTujuan.setVisible(false);
        panelEMKL.setVisible(false);
        
        switch(jList1.getSelectedIndex()){
            case 0:{//Per kontainer
                
                panelKontainer.setVisible(true);
                break;
            }
            
            case 1 :{//Per Kapal
                panelKapal.setVisible(true);
                break;
            }
            
            case 2:{//Per Kota Tujuan
                panelKotaTujuan.setVisible(true);
                break;
            }
            
            case 3:{//Per Emkl
                panelEMKL.setVisible(true);
                break;
            }
            
            case 4:{//Per Penerimaan
                panelPenerimaan.setVisible(true);
                break;
            }
//            
            default:{
                panelKapal.setVisible(true);
                break;
            }
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void txtKapalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKapalFocusLost
        if (!lst.isVisible())
            //udfLoadMerk();
            udfLoadTglBerangkat();
    }//GEN-LAST:event_txtKapalFocusLost

    private void txtKapalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER && !txtKapal.getText().trim().equalsIgnoreCase("")){
            udfLoadTglBerangkat();
        }
    }//GEN-LAST:event_txtKapalKeyPressed

    private void udfLoadTglBerangkat(){
        cmbTglBerangkat.removeAllItems();
        
        try {
            Statement st1 = conn.createStatement();
            ResultSet rs1=st1.executeQuery("select distinct coalesce(to_Char(tgl_berangkat,'dd-MM-yyyy'),'') as tanggal from kontainer " +
                    "where active=true and kode_kapal='"+txtKapal.getText()+"' ");
            
            cmbTglBerangkat.removeAllItems();
            
            while(rs1.next()){
                cmbTglBerangkat.addItem(rs1.getString(1));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPackingListBaru.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
    
    private void txtKapalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKapalKeyReleased
        try {
            String sCari = txtKapal.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKapal.setText(obj[0].toString());
                            lblKapal.setText(obj[1].toString());
                            
                            lst.setVisible(false);
                            udfLoadTglBerangkat();
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
                        //                        String sQry="select kode_kapal, coalesce(nama_kapal,'') as nama_kapal " +
                        //                                "from kapal where upper(kode_kapal||coalesce(nama_kapal,'')) " +
                        //                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        String sQry="select distinct kon.kode_kapal, nama_kapal " +
                                "from kontainer kon " +
                                "inner join kapal on kapal.kode_kapal=kon.kode_kapal " +
                                "where (kon.kode_kapal||nama_kapal) iLike '%"+txtKapal.getText()+"%' ";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+jPanel2.getX()+panelKapal.getX() + this.txtKapal.getX()+6,
                                Utama.iTop+this.getY()+jPanel1.getY()+jPanel2.getY()+panelKapal.getY()+this.txtKapal.getY()+4 + txtKapal.getHeight()+75,
                                txtKapal.getWidth()+lblKapal.getWidth()+15,
                                120);
                        
                        
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKapal);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKapal});
                        lst.setColWidth(0, txtKapal.getWidth());
                        lst.setColWidth(1, lblKapal.getWidth());
                        
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

    private void cmbTglBerangkatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTglBerangkatItemStateChanged
        //JOptionPane.showMessageDialog(this, cmbTglBerangkat.getSelectedItem().toString());
        
    }//GEN-LAST:event_cmbTglBerangkatItemStateChanged

    private void txtKotaTujuanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKotaTujuanFocusLost
        if (!lst.isVisible())
            //udfLoadMerk();
            udfLoadTglBerangkatKota();
}//GEN-LAST:event_txtKotaTujuanFocusLost

    private void txtKotaTujuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaTujuanKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtKotaTujuanKeyPressed

    private void txtKotaTujuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaTujuanKeyReleased
        try {
            String sCari = txtKotaTujuan.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKotaTujuan.setText(obj[0].toString());
                            lblKotaTujuan.setText(obj[1].toString());
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
                    txtKotaTujuan.setText("");
                    lblKotaTujuan.setText("");
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
                        String sQry="select kode_kota, coalesce(nama_kota,'') as nama_kota from kota " +
                                "where (kode_kota||coalesce(nama_kota,'') ) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+ this.jPanel1.getX()+ jPanel2.getX()+panelKotaTujuan.getX()+ this.txtKotaTujuan.getX()+6,
                                Utama.iTop+this.getY()+jPanel1.getY()+ jPanel2.getY()+panelKotaTujuan.getY()+this.txtKotaTujuan.getY() + txtKotaTujuan.getHeight()+75,
                                txtKotaTujuan.getWidth()+lblKotaTujuan.getWidth()+15,
                                120);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKotaTujuan);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKotaTujuan});
                        lst.setColWidth(0, txtKotaTujuan.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKotaTujuan.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKotaTujuan.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKotaTujuanKeyReleased

    private void cmbTglBerangkatKotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTglBerangkatKotaItemStateChanged
        // TODO add your handling code here:
}//GEN-LAST:event_cmbTglBerangkatKotaItemStateChanged

    private void radioKota2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioKota2ActionPerformed
        cmbTglBerangkatKota.setEnabled(false);
        jcDateKota1.setEnabled(true);
        jcDateKota2.setEnabled(true);
}//GEN-LAST:event_radioKota2ActionPerformed

    private void radioKota1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioKota1ActionPerformed
        cmbTglBerangkatKota.setEnabled(true);
        jcDateKota1.setEnabled(false);
        jcDateKota2.setEnabled(false);
}//GEN-LAST:event_radioKota1ActionPerformed

    private void txtEmklFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmklFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtEmklFocusLost

    private void txtEmklKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmklKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtEmklKeyPressed

    private void txtEmklKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmklKeyReleased
        try {
            String sCari = txtEmkl.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtEmkl.setText(obj[0].toString());
                            lblEmkl.setText(obj[1].toString());
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
                    txtEmkl.setText("");
                    lblEmkl.setText("");
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
                        String sQry="select kode_kota, coalesce(nama_kota,'') as nama_kota from kota " +
                                "where (kode_kota||coalesce(nama_kota,'') ) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+ this.jPanel1.getX()+ jPanel2.getX()+panelEMKL.getX()+ this.txtEmkl.getX()+6,
                                Utama.iTop+this.getY()+jPanel1.getY()+ jPanel2.getY()+panelEMKL.getY()+this.txtEmkl.getY() + txtEmkl.getHeight()+75,
                                txtEmkl.getWidth()+lblEmkl.getWidth()+15,
                                120);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtEmkl);
                        lst.setLblDes(new javax.swing.JLabel[]{lblEmkl});
                        lst.setColWidth(0, txtEmkl.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtEmkl.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtEmkl.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtEmklKeyReleased

    private void cmbTglBerangkatEMKLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTglBerangkatEMKLItemStateChanged
        // TODO add your handling code here:
}//GEN-LAST:event_cmbTglBerangkatEMKLItemStateChanged

    private void radioEmkl1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEmkl1ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_radioEmkl1ActionPerformed

    private void radioEmkl2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioEmkl2ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_radioEmkl2ActionPerformed

    private void txtTokoPengirimFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoPengirimFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtTokoPengirimFocusLost

    private void txtTokoPengirimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoPengirimKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTokoPengirimKeyPressed

    private void txtTokoPengirimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoPengirimKeyReleased
        try {
            String sCari = txtTokoPengirim.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtTokoPengirim.setText(obj[0].toString());
                            lblTokoPengirim.setText(obj[1].toString());
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
                    txtTokoPengirim.setText("");
                    lblTokoPengirim.setText("");
                    txtTokoPengirim.requestFocus();
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
                        String sQry="select kode_toko as kode, coalesce(nama,'') as nama, coalesce(alamat,'') as alamat " +
                                "from toko where upper(kode_toko||coalesce(nama,'')||coalesce(alamat,'')) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ jPanel2.getX()+panelPenerimaan.getX()+ this.txtTokoPengirim.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+ jPanel2.getY()+panelPenerimaan.getY() + this.txtTokoPengirim.getY()+  txtTokoPengirim.getHeight()+75,
                                txtTokoPengirim.getWidth()+lblTokoPengirim.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtTokoPengirim);
                        lst.setLblDes(new javax.swing.JLabel[]{lblTokoPengirim});
                        lst.setColWidth(0, txtTokoPengirim.getWidth());
                        lst.setColWidth(1, 250);
                        lst.setColWidth(2, 75);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtTokoPengirim.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtTokoPengirim.setText("");
                            lblTokoPengirim.setText("");
                            
                            txtTokoPengirim.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtTokoPengirimKeyReleased

    private void txtKotaPenerimaanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKotaPenerimaanFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtKotaPenerimaanFocusLost

    private void txtKotaPenerimaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaPenerimaanKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtKotaPenerimaanKeyPressed

    private void txtKotaPenerimaanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKotaPenerimaanKeyReleased
        try {
            String sCari = txtKotaPenerimaan.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtKotaPenerimaan.setText(obj[0].toString());
                            lblKotaPenerimaan.setText(obj[1].toString());
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
                    txtKotaPenerimaan.setText("");
                    lblKotaPenerimaan.setText("");
                    txtKotaPenerimaan.requestFocus();
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
                        String sQry="select kode_kota as kode, coalesce(nama_kota,'') as nama_kota " +
                                "from kota where upper(kode_kota||coalesce(nama_kota,'') ) " +
                                "ilike upper('%"+sCari+"%') order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ jPanel2.getX()+panelPenerimaan.getX()+ this.txtKotaPenerimaan.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+ jPanel2.getY()+panelPenerimaan.getY() + this.txtKotaPenerimaan.getY()+  txtKotaPenerimaan.getHeight()+75,
                                txtKotaPenerimaan.getWidth()+lblKotaPenerimaan.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtKotaPenerimaan);
                        lst.setLblDes(new javax.swing.JLabel[]{lblKotaPenerimaan});
                        lst.setColWidth(0, txtKotaPenerimaan.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtKotaPenerimaan.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtKotaPenerimaan.setText("");
                            lblKotaPenerimaan.setText("");
                            
                            txtKotaPenerimaan.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtKotaPenerimaanKeyReleased

    private void txtTokoTujuanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoTujuanFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtTokoTujuanFocusLost

    private void txtTokoTujuanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoTujuanKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtTokoTujuanKeyPressed

    private void txtTokoTujuanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoTujuanKeyReleased
        try {
            String sCari = txtTokoTujuan.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtTokoTujuan.setText(obj[0].toString());
                            lblTokoTujuan.setText(obj[1].toString());
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
                    txtTokoTujuan.setText("");
                    lblTokoTujuan.setText("");
                    txtKotaPenerimaan.requestFocus();
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
                        String sQry="select kode_cust as kode, coalesce(nama,'') as toko_tujuan " +
                                "from customer where upper(kode_cust||coalesce(nama,'') ) " +
                                "ilike upper('%"+sCari+"%') and kota like '"+txtKotaPenerimaan.getText()+"%' order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ jPanel2.getX()+panelPenerimaan.getX()+ this.txtTokoTujuan.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+ jPanel2.getY()+panelPenerimaan.getY() + this.txtTokoTujuan.getY()+  txtTokoTujuan.getHeight()+75,
                                txtTokoTujuan.getWidth()+lblTokoTujuan.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtTokoTujuan);
                        lst.setLblDes(new javax.swing.JLabel[]{lblTokoTujuan});
                        lst.setColWidth(0, txtTokoTujuan.getWidth());
                        lst.setColWidth(1, 250);
                        
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtTokoTujuan.requestFocus();
                        } else{
                            lst.setVisible(false);
                            txtTokoTujuan.setText("");
                            lblTokoTujuan.setText("");
                            
                            txtTokoTujuan.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtTokoTujuanKeyReleased

    private void txtMerkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMerkActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtMerkActionPerformed

    private void txtMerkFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMerkFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_txtMerkFocusGained

    private void txtMerkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMerkKeyReleased
        try {
            String sCari = txtMerk.getText();
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
                        
                        String sQry="select m.merk " +
                                "from merk m " +
                                "where coalesce(m.merk,'') " +
                                "ilike upper('%"+sCari+"%') and m.kode_cust like '"+txtTokoTujuan.getText()+"%' order by 1";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(Utama.iLeft+this.getX()+this.jPanel1.getX()+ jPanel2.getX()+panelPenerimaan.getX()+ this.txtMerk.getX()+4,
                                Utama.iTop+this.getY()+this.jPanel1.getY()+ jPanel2.getY()+ panelPenerimaan.getY() + this.txtMerk.getY()+  txtMerk.getHeight()+75,
                                420,
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
                            txtMerk.requestFocus();
                            //txtMerk.setText("");
                            
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtMerkKeyReleased

    void setUser(String sUser) {
        this.sUser=sUser;
    }

    private boolean udfCekBeforePrint(){
        boolean bSt=true;
        if(jList1.getSelectedIndex()==0 && txtSatu.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi No. Container terebih dulu");
            txtSatu.requestFocus();
            bSt=false;
        } 
        if(jList1.getSelectedIndex()==1 && txtKapal.getText().trim().equalsIgnoreCase("")){ //By Kapal
            JOptionPane.showMessageDialog(this, "Silakan isi Nama Kapal terebih dulu");
            txtKapal.requestFocus();
            bSt=false;
        } 
        if(jList1.getSelectedIndex()==2 && txtKotaTujuan.getText().trim().equalsIgnoreCase("")){ //By Kota
            JOptionPane.showMessageDialog(this, "Silakan isi Nama Kota tujuan terebih dulu");
            txtKotaTujuan.requestFocus();
            bSt=false;
        } 
        
        return bSt;
    }

    private void udfLoadTglBerangkatKota() {
        cmbTglBerangkatKota.removeAllItems();
        
        try {
            Statement st1 = conn.createStatement();
            ResultSet rs1=st1.executeQuery("select distinct coalesce(to_Char(tgl_berangkat,'dd-MM-yyyy'),'') as tanggal from kontainer " +
                    "where active=true and kode_kota='"+txtKotaTujuan.getText()+"' ");
            
            cmbTglBerangkatKota.removeAllItems();
            
            while(rs1.next()){
                cmbTglBerangkatKota.addItem(rs1.getString(1));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrmRptPackingListBaru.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void udfPrint() {
        if(udfCekBeforePrint())
            {
            ///Buku Register Pasien
            this.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
            FileInputStream file = null;
            try {
                HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                int pilRpt = 0;
                pilRpt = jList1.getSelectedIndex();
                switch (pilRpt) {
                   case 0:     //By Kontainer
                        {
                            String sRpt=chkExpedisi.isSelected()?"packing_list_per_kontainer_v2_Ex": "packing_list_per_kontainer_v2";
                            reportParam.put("kontainer", Integer.parseInt(lblSerialKon.getText()));
                            
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/"+sRpt+".jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 1:     //by Kapal
                        {   
                            String sTgl=cmbTglBerangkat.getSelectedItem().toString().trim().equalsIgnoreCase("")? "": fmtYMD.format(fmtDMY.parse(cmbTglBerangkat.getSelectedItem().toString()));
                            reportParam.put("kapal", txtKapal.getText());
                            reportParam.put("tgl_berangkat", sTgl);
                
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_kapal_per_tgl_berangkat_v2.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 2:     //By Kota Tujuan
                        {
                            String sTgl=cmbTglBerangkatKota.getSelectedItem().toString().trim().equalsIgnoreCase("")? "": fmtYMD.format(fmtDMY.parse(cmbTglBerangkatKota.getSelectedItem().toString()));
                            String sRpt="";
                            
                            if(radioKota1.isSelected()){
                                sRpt="packing_list_per_kota_tujuan_per_tgl_berangkat_v2";
                                reportParam.put("tgl_berangkat", sTgl);
                                reportParam.put("sHeadTanggal", cmbTglBerangkatKota.getSelectedItem().toString());
                            
                            }else{
                                sRpt="packing_list_per_kota_tujuan_per_tgl_berangkat_antara_v2";
                                reportParam.put("tanggal1", fmtYMD.format(jcDateKota1.getDate()));
                                reportParam.put("tanggal2", fmtYMD.format(jcDateKota2.getDate()));
                                reportParam.put("sHeadTanggal", fmtDMY.format(jcDateKota1.getDate())+ " s/d "+ fmtDMY.format(jcDateKota2.getDate()) );
                            }
                            
                            reportParam.put("kota_tujuan", txtKotaTujuan.getText());
                            reportParam.put("sHeadNamaKota", lblKotaTujuan.getText());
                            
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/"+sRpt+".jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 3:
                        {
                            reportParam.put("petugas", txtKapal.getText());
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/packing_list_per_merk22.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                    case 4:
                        {
                            reportParam.put("tanggal1", fmtYMD.format(jcDateTerima1.getDate()));
                            reportParam.put("tanggal2", fmtYMD.format(jcDateTerima2.getDate()));
                                
                            reportParam.put("kota", txtKotaPenerimaan.getText());
                            reportParam.put("dari", txtTokoPengirim.getText());
                            reportParam.put("kirim_ke", txtTokoTujuan.getText());
                            jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("Reports/penerimaan_per_tanggal_kota_toko_tujuan.jasper"));
                            JasperPrint print = JasperFillManager.fillReport(jasperReport, reportParam, conn);
                            print.setOrientation(jasperReport.ORIENTATION_PORTRAIT);
                            JasperViewer.viewReport(print, false);
                            break;
                        }
                }
                this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
            } catch (ParseException ex) {
                this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
                Logger.getLogger(FrmRptPackingListBaru.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JRException je) {
                this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));
                System.out.println(je.getMessage());
            }
            
            
        }
        //catch(NullPointerException ne){JOptionPane.showMessageDialog(null, ne.getMessage(), "SHS Open Source", JOptionPane.OK_OPTION);}
        
    }
    
    /**
     * @param args the command line arguments
     */
/*    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRptKasir().setVisible(true);
            }
        });
    }
 */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnPreview;
    public javax.swing.ButtonGroup buttonGroupKotaTujuan;
    public javax.swing.JCheckBox chkExpedisi;
    public javax.swing.JComboBox cmbTglBerangkat;
    public javax.swing.JComboBox cmbTglBerangkatEMKL;
    public javax.swing.JComboBox cmbTglBerangkatKota;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel10;
    public javax.swing.JLabel jLabel11;
    public javax.swing.JLabel jLabel12;
    public javax.swing.JLabel jLabel13;
    public javax.swing.JLabel jLabel14;
    public javax.swing.JLabel jLabel15;
    public javax.swing.JLabel jLabel16;
    public javax.swing.JLabel jLabel17;
    public javax.swing.JLabel jLabel18;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel8;
    public javax.swing.JLabel jLabel9;
    public javax.swing.JList jList1;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JScrollPane jScrollPane1;
    public org.jdesktop.swingx.JXDatePicker jcDateEmkl1;
    public org.jdesktop.swingx.JXDatePicker jcDateEmkl2;
    public org.jdesktop.swingx.JXDatePicker jcDateKota1;
    public org.jdesktop.swingx.JXDatePicker jcDateKota2;
    public org.jdesktop.swingx.JXDatePicker jcDateTerima1;
    public org.jdesktop.swingx.JXDatePicker jcDateTerima2;
    public javax.swing.JLabel lblEmkl;
    public javax.swing.JLabel lblKapal;
    public javax.swing.JLabel lblKotaPenerimaan;
    public javax.swing.JLabel lblKotaTujuan;
    public javax.swing.JLabel lblSerialKon;
    public javax.swing.JLabel lblTglBerangkat;
    public javax.swing.JLabel lblTokoPengirim;
    public javax.swing.JLabel lblTokoTujuan;
    public javax.swing.JPanel panelEMKL;
    public javax.swing.JPanel panelKapal;
    public javax.swing.JPanel panelKontainer;
    public javax.swing.JPanel panelKotaTujuan;
    public javax.swing.JPanel panelPenerimaan;
    public javax.swing.JRadioButton radioEmkl1;
    public javax.swing.JRadioButton radioEmkl2;
    public javax.swing.JRadioButton radioKota1;
    public javax.swing.JRadioButton radioKota2;
    public javax.swing.JTextField txtEmkl;
    public javax.swing.JTextField txtKapal;
    public javax.swing.JTextField txtKotaPenerimaan;
    public javax.swing.JTextField txtKotaTujuan;
    public javax.swing.JTextField txtMerk;
    public javax.swing.JTextField txtSatu;
    public javax.swing.JTextField txtTokoPengirim;
    public javax.swing.JTextField txtTokoTujuan;
    // End of variables declaration//GEN-END:variables
    
}
