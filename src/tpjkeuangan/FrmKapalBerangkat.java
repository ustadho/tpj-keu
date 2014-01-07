/*
 * FrmContainer.java
 *
 * Created on November 6, 2007, 9:54 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import main.GeneralFunction;

/**
 *
 * @author  oestadho
 */
public class FrmKapalBerangkat extends javax.swing.JInternalFrame {
    Connection conn;
    DefaultTableModel modelMerk;
    private String sUserName;
    
    
    /** Creates new form FrmContainer */
    public FrmKapalBerangkat() {
        initComponents();
    }

    void setUserName(String sUserName) {
        this.sUserName=sUserName;
    }
    
    
    
    private void onOpen(String sQry){
        myModel = (DefaultTableModel) tblKapal.getModel();
        int i=0;
        try {
            
            while(myModel.getRowCount()>=1){
                myModel.removeRow(0);
            }
            
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery(sQry);
                
            System.out.println(sQry);
            while (rs.next()) {
            myModel.addRow(new Object[]{rs.getString("kode_kapal"),
                                        rs.getString("nama_kapal"),
                                        rs.getString("tgl_berangkat"),
                                        rs.getString("kode_kota"),
                                        rs.getString("nama_kota"),
                                        rs.getString("keterangan"),
                                        rs.getInt("serial_kode"),
                                        });
            }
            
            if(myModel.getRowCount()>0)
                tblKapal.setRowSelectionInterval(0, 0);
            
            rs.close();
            st.close();
        } catch (SQLException eswl){ System.out.println(eswl.getMessage());}
        if(i>0){
            tblKapal.requestFocusInWindow();
            tblKapal.setRowSelectionInterval(0,0);
        }
   }

    private void udfNew() {
        FrmKapalBerangkatEdit fEdit=new FrmKapalBerangkatEdit(JOptionPane.getFrameForComponent(this), false);
        fEdit.setConn(conn);
        fEdit.setTitle("Kapal Baru");
        fEdit.setIsNew(true);
        fEdit.setSerialKapal(0);
        fEdit.setModel(myModel);
        fEdit.setRowPos(tblKapal.getSelectedRow());
        fEdit.setSrcTable(tblKapal);
        fEdit.setVisible(true);
    }

    private void udfSearch() {
        String sQry="select tujuan.kode_kapal, nama_kapal, tgl_berangkat, kota_tujuan as kode_kota, " +
                    "nama_kota, keterangan, serial_kode " +
                    "from nota_kapal_tujuan tujuan " +
                    "left join kapal on kapal.kode_kapal=tujuan.kode_kapal " +
                    "left join kota on kota.kode_kota=kota_tujuan " +
                    "where (tujuan.kode_kapal||coalesce(nama_kapal,'')||coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'),'')||" +
                    "coalesce(nama_kota,'')) " +
                    "iLike '%"+txtSearch.getText().toUpperCase()+"%' and " +
                    "coalesce(kota_tujuan,'') iLike  '"+(chkKota.isSelected()? "": Utama.sKodeKota)+"%' " +
                    "and active ="+chkActive.isSelected()+" " +
                    "order by tgl_berangkat desc";
        
        System.out.println(sQry);
        onOpen(sQry);
        txtSearch.requestFocus();
    }
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            switch(keyKode){
                
                case KeyEvent.VK_F2: {  //Save
//                    if (getBEdit())
//                        udfUpdateData();
//                    
//                    break;
                    udfNew();
                }
                
                case KeyEvent.VK_F3: {  //Search
//                    udfFilter();
                    udfEditKapalTujuan();
                    break;
                }
                
                case KeyEvent.VK_F4: {  //Edit
//                    udfEdit();
//                    break;
                }
                
                case KeyEvent.VK_F5: {  //New -- Add
//                    udfNew();
//                    break;
                }
                
                case KeyEvent.VK_F6: {  //Filter
//                    onOpen(cmbFilter.getSelectedItem().toString(),true);
                    break;
                }
                
                case KeyEvent.VK_F12: {  //Delete
//                    if (!getBEdit() && tblContainer.getRowCount()>0)
//                        udfUpdateData();
//                    
//                    break;
                }
                case KeyEvent.VK_ENTER : {
//                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
//                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
//                        {
//                    if (!lst.isVisible()){
//                        Component c = findNextFocus();
//                        c.requestFocus();
//                    }else{
//                        lst.requestFocus();
//                    }
//                    }
//                    break;
                }
                case KeyEvent.VK_DOWN: {
//                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
//                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
//                        {                        
//                            if (!lst.isVisible()){
//			    Component c = findNextFocus();
//			    c.requestFocus();
//                            }else
//                                lst.requestFocus();
//                            
//                            break;
//                    }
                }
                case KeyEvent.VK_UP: {
                    Component ct = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
                    if(!(ct.getClass().getSimpleName().equalsIgnoreCase("JTABLE")))
                    {    
                        Component c = findPrevFocus();
                        c.requestFocus();
                    }
                    break;
                }
                
                //lempar aja ke udfCancel
                case KeyEvent.VK_ESCAPE: {
                    dispose();
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
//            if (lst.isVisible())
//                lst.setVisible(false);
            
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
//            if (lst.isVisible()) lst.setVisible(false);
            
            return prevFocus;
        }
        return null;
    }
    
    private void udfEditKapalTujuan(){
        int iRow=tblKapal.getSelectedRow();
        
        if(iRow>=0){
            FrmKapalBerangkatEdit fEdit=new FrmKapalBerangkatEdit(JOptionPane.getFrameForComponent(this), false);
            fEdit.setConn(conn);
            fEdit.setTitle("Edit Kapal Tujuan");
            fEdit.setIsNew(false);
            fEdit.setSerialKapal((Integer)tblKapal.getValueAt(iRow, 4));
            fEdit.setModel(myModel);
            fEdit.setRowPos(tblKapal.getSelectedRow());
            fEdit.setSrcTable(tblKapal);
            fEdit.setVisible(true);
        }
    }
    
    private void TableLook(){
            int lbr=tblKapal.getWidth();
            
            tblKapal.getColumnModel().getColumn(0).setPreferredWidth(120);
            tblKapal.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblKapal.getColumnModel().getColumn(2).setPreferredWidth(90);
            tblKapal.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblKapal.getColumnModel().getColumn(4).setPreferredWidth(100);
            tblKapal.getColumnModel().getColumn(5).setPreferredWidth(200);
             
            tblKapal.setRowHeight(20);
            for (int i=0;i<tblKapal.getColumnCount();i++){
                tblKapal.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
            }
            
            if (myModel.getRowCount() > 0) {
                tblKapal.changeSelection(0, 0,false,false);                
            }          
            
            SelectionListener listener = new SelectionListener(tblKapal);
            tblKapal.getSelectionModel().addListSelectionListener(listener);
            tblKapal.getColumnModel().getSelectionModel().addListSelectionListener(listener);
            tblKapal.setRequestFocusEnabled(true);
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
            if(value instanceof Date){
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
                setBackground(new Color(248,255,167));
                //setForeground(new Color(255,255,255));
            }
            
            setValue(value);
            return this;
        }
    }
    
    private void udfDelete(){
        int iRow =tblKapal.getSelectedRow(); 
        if(iRow>=0){
            if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus kapal '"+tblKapal.getValueAt(iRow, 4).toString()+"'", "Expedsi", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                try {
                    Statement st=conn.createStatement();
                    st.executeUpdate("Delete from nota_kapal_tujuan where serial_kode="+tblKapal.getValueAt(iRow, 4).toString()+"");
                     
                    myModel.removeRow(iRow);
                    JOptionPane.showMessageDialog(this, "Hapus data sukses!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Hapus data tidak bisa dlakukan karena telah digunakan pada tabel lain! \n "+ex.getMessage());
                }
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        cmbFilter = new javax.swing.JComboBox();
        cmbOperator = new javax.swing.JComboBox();
        txtFilter = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        chkActive = new javax.swing.JCheckBox();
        chkKota = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKapal = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();

        jMenu1.setText("Menu");

        jMenuItem1.setText("Item");
        jMenu1.add(jMenuItem1);

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Kapal Berangkat");
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

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/New.png"))); // NOI18N
        btnNew.setToolTipText("Baru     (F2)");
        btnNew.setBorder(null);
        btnNew.setMaximumSize(new java.awt.Dimension(40, 40));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Edit.png"))); // NOI18N
        btnEdit.setToolTipText("Edit     (F3)");
        btnEdit.setBorder(null);
        btnEdit.setMaximumSize(new java.awt.Dimension(40, 40));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Delete.png"))); // NOI18N
        btnDelete.setToolTipText("Hapus     (F12)");
        btnDelete.setBorder(null);
        btnDelete.setMaximumSize(new java.awt.Dimension(40, 40));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(btnDelete);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Exit.png"))); // NOI18N
        btnClose.setToolTipText("Kaluar (Esc)");
        btnClose.setBorder(null);
        btnClose.setMaximumSize(new java.awt.Dimension(40, 40));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnClose);

        jPanel1.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 50));

        cmbFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Nama Kapal", "Tgl. Berangkat", "Kota Tujuan" }));
        cmbFilter.setMaximumSize(new java.awt.Dimension(150, 24));
        cmbFilter.setMinimumSize(new java.awt.Dimension(20, 24));
        cmbFilter.setPreferredSize(new java.awt.Dimension(20, 24));
        jToolBar2.add(cmbFilter);

        cmbOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "=", ">", ">=", "<", "<=", "Like" }));
        cmbOperator.setMaximumSize(new java.awt.Dimension(150, 24));
        cmbOperator.setMinimumSize(new java.awt.Dimension(20, 24));
        cmbOperator.setPreferredSize(new java.awt.Dimension(20, 24));
        jToolBar2.add(cmbOperator);

        txtFilter.setMaximumSize(new java.awt.Dimension(200, 24));
        txtFilter.setMinimumSize(new java.awt.Dimension(4, 4));
        jToolBar2.add(txtFilter);

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Filter.png"))); // NOI18N
        btnFilter.setToolTipText("Fiter      (F6)");
        btnFilter.setBorder(null);
        btnFilter.setMaximumSize(new java.awt.Dimension(40, 40));
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        jToolBar2.add(btnFilter);

        txtSearch.setMaximumSize(new java.awt.Dimension(200, 24));
        txtSearch.setMinimumSize(new java.awt.Dimension(4, 4));
        txtSearch.setNextFocusableComponent(btnSearch);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });
        jToolBar2.add(txtSearch);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Search.png"))); // NOI18N
        btnSearch.setToolTipText("Search     (F3)");
        btnSearch.setBorder(null);
        btnSearch.setMaximumSize(new java.awt.Dimension(40, 40));
        btnSearch.setMinimumSize(new java.awt.Dimension(40, 40));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSearch);

        jPanel1.add(jToolBar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 500, 50));

        chkActive.setSelected(true);
        chkActive.setText("Hanya yg aktif");
        chkActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActiveActionPerformed(evt);
            }
        });
        jPanel1.add(chkActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 130, -1));

        chkKota.setText("Semua kota");
        chkKota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkKotaActionPerformed(evt);
            }
        });
        jPanel1.add(chkKota, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 130, -1));

        jLabel2.setBackground(new java.awt.Color(0, 51, 255));
        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Kapal");
        jLabel2.setOpaque(true);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tblKapal.setAutoCreateRowSorter(true);
        tblKapal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Kapal", "Nama Kapal", "Tgl. Berangkat", "Kode Kota", "Kota Tujuan", "Keterangan", "Serial"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKapal.getTableHeader().setReorderingAllowed(false);
        tblKapal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKapalMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblKapalMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tblKapal);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isKapalOn=false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void tblKapalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKapalMouseClicked
        if(evt.getClickCount()==2){
            udfEditKapalTujuan();
        }
}//GEN-LAST:event_tblKapalMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        myModel=(DefaultTableModel)tblKapal.getModel();
        myModel.setNumRows(0)    ;
        tblKapal.setModel(myModel);
        
//        String sQry="select no_spnu , coalesce(k.emkl,'') as kode_emkl, coalesce(emkl.nama,'') as emkl, " +
//                    "coalesce(k.kode_kapal,'') as kode_kapal, coalesce(nama_kapal,'') as nama_kapal," +
//                    "coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'),'') as tgl_berangkat, " +
//                    "case when satuan_kirim='LCL' then 'LCL' " +
//                    "   when satuan_kirim='KON' then '1 KONTAINER' " +
//                    "   else '' end as satuan_kirim, " +
//                    "coalesce(keterangan,'') as keterangan, coalesce(nama_kota) as kota " +
//                    "from kontainer k " +
//                    "left join emkl on emkl.kode_emkl=k.emkl " +
//                    "left join kapal on kapal.kode_kapal=k.kode_kapal " +
//                    "left join kota on kota.kode_kota=k.kode_kota where coalesce(k.kode_kota,'') iLike  '"+""+"%' " +
//                    "and active ="+chkActive.isSelected()+" " +
//                    "order by no_spnu, coalesce(to_char(tgl_berangkat,'dd-MM-yyyy'),'')" ;
//                    
//                    
//        System.out.println(sQry);
//        onOpen(sQry);
       udfSearch();
//        for(int i=0;i<jToolBar2.getComponentCount();i++){
//            Component c = jToolBar2.getComponent(i);
//            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
//            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
//            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
//                //System.out.println(c.getClass().getSimpleName());
//                c.addKeyListener(new MyKeyListener());
//            }
//        }
        for(int i=0;i<jToolBar1.getComponentCount();i++){
            Component c = jToolBar1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
         
         jScrollPane1.addKeyListener(new MyKeyListener());
         tblKapal.addKeyListener(new MyKeyListener());
         
        System.out.println(jPanel1.getComponentCount());
        for(int i=0;i<jPanel1.getComponentCount();i++){
            Component c = jPanel1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton")   || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")) {
                //System.out.println(c.getClass().getSimpleName());
                c.addKeyListener(new MyKeyListener());
            }
        }
        TableLook();
        requestFocusInWindow(true);
        tblKapal.requestFocusInWindow();
        
        tblKapal.getColumnModel().removeColumn(tblKapal.getColumnModel().getColumn(0));
        tblKapal.getColumnModel().removeColumn(tblKapal.getColumnModel().getColumn(2));
//        tblKapal.getColumnModel().removeColumn(tblKapal.getColumnModel().getColumn(4));
        
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        udfSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        String sField="";
        switch(cmbFilter.getSelectedIndex()){
            case 0:{
                sField="(kode_cust||coalesce(nama,'')||coalesce(alamat_1,'')||coalesce(nama_kota,'') ) iLike ";
                break;
            }
            case 1:{
                sField="kode_cust= ";
                break;
            }
            case 2:{
                sField="nama= ";
                break;
            }
            //Semua, Kode, Nama Customer, MERK, Alamat, Kota
            case 3:{
                sField="merk= ";
                break;
            }
            case 4:{
                sField="alamat= ";
                break;
            }
            case 5:{
                sField="kota= ";
                break;
            }
            
            default:{
                sField="nama= ";
                break;
            }
            
        }
//        String sQry="select * from customer where "+sField+" '"+txtFilter.getText().toUpperCase()+"' " +
//                "order by 1";
        
        String sQry="select kode_cust, coalesce(nama,'') as nama, coalesce(alamat_1, '') as alamat," +
                "coalesce(nama_kota,'') as kota, coalesce(contact_person,'') as kontak, coalesce(telp,'') as telp, " +
                "coalesce(hp, '') as hp, coalesce(termin_pembayaran,0) as termin " +
                "from customer c " +
                "left join kota on kota=kode_kota where "+sField+" '"+txtFilter.getText().toUpperCase()+"' " +
                "order by 1";
        
        System.out.println(sQry);
        onOpen(sQry);
        txtSearch.requestFocus();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        udfDelete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        udfEditKapalTujuan();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        udfNew();
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void chkActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActiveActionPerformed
        udfSearch();
        
    }//GEN-LAST:event_chkActiveActionPerformed

    private void chkKotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkKotaActionPerformed
        udfSearch();
    }//GEN-LAST:event_chkKotaActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            udfSearch();
    }//GEN-LAST:event_txtSearchKeyPressed

    private void tblKapalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKapalMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblKapalMouseEntered

    void setCon(Connection conn) {
        this.conn=conn;
    }
    
    public class SelectionListener implements ListSelectionListener {
         JTable table;
         int rowPos;
         int colPos;
    
        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        SelectionListener(JTable table) {
            this.table = table;
        }
        public void valueChanged(ListSelectionEvent e) {
            if(table.getSelectedRow()>=0){
                
                rowPos = table.getSelectedRow();           
                if (rowPos >=0) { 
                    
                    
                }
            }
        }
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox chkActive;
    private javax.swing.JCheckBox chkKota;
    private javax.swing.JComboBox cmbFilter;
    private javax.swing.JComboBox cmbOperator;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTable tblKapal;
    private javax.swing.JTextField txtFilter;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private DefaultTableModel myModel;
    private Statement stMerk;
    private ResultSet rsMerk;
}
