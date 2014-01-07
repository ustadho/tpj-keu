/*
 * FrmNotTerkait.java
 *
 * Created on July 11, 2008, 7:56 PM
 */

package tpjkeuangan;

import java.awt.Cursor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  oestadho
 */
public class FrmNotaLookup extends javax.swing.JDialog {
    private boolean isLookup=false;
    private DefaultTableModel myModel, srcModel;
    private ResultSet rs=null;
    private Connection conn;
    private String sNotaHeader="", sTglBerangkat="";
    private boolean wisUrut=false;
    private String sTo;
    private String sCustomer;
    SimpleDateFormat fYMD=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fDMY=new SimpleDateFormat("dd-MM-yyyy");
    
    /** Creates new form FrmNotTerkait */
    public FrmNotaLookup(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    void setCustomer(String text) {
        sCustomer=text;
    }

    void setNoNotaHeader(String s){
        sNotaHeader=s;
    }

    void setSrcModel(DefaultTableModel m){
        srcModel=m;
    }
    
    void setTglBerangkat(String text) {
        try {
            sTglBerangkat = fYMD.format(fDMY.parse(text));
        } catch (ParseException ex) {
            Logger.getLogger(FrmNotaLookup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setTo(String s){
        sTo=s;
    }
    
    void setConn(Connection con){
        conn=con;
    }
    
    void setIsLookup(boolean b){
        isLookup=b;
        btnSelect.setVisible(b);
        btnCancel.setText(b? "Batal": "OK");
        btnAtas.setVisible(!b);
        btnBawah.setVisible(!b);
        btnUrutkanSimpan.setVisible(!b);
    }
    
    private void udfLoadTable(){
        try {
            rs = conn.createStatement().executeQuery(getQuery());
            //myModel.setNumRows(0);
            while(rs.next()){
                myModel.addRow(new Object[]{
                    rs.getInt("priority"),
                    rs.getString("nota_header"),
                    rs.getString("kapal"),
                    rs.getString("tgl_berangkat"),
                    rs.getFloat("sub_total")
                });
                if(rs.getInt("priority")>0) wisUrut=true;
                //System.out.println(rs.getString("nota_header"));
            }
            
            if(wisUrut) {
                wisUrut=true;
                //btnUrutkanSimpan.setEnabled(false);
            }else{
                wisUrut=false;
                //btnUrutkanSimpan.setEnabled(true);
            }
            
        } catch (SQLException s) {
            System.err.println(s.getMessage());
        }
    }
    
    private String getQuery(){
        String sQry="";
        if(isLookup){
            sQry   ="select  coalesce(priority,0) as priority, d.no_nota as nota_header, " +
                    "kapal.nama_kapal as kapal, to_char(ktj.tgl_berangkat,'dd-MM-yyyy') as tgl_berangkat, " +
                    "sum(coalesce(sub_total,0)) as sub_total " +
                    "from nota nh " +
                    "left join nota_detail d on d.no_nota=nh.no_nota " +
                    "left join nota_kapal_tujuan ktj on ktj.serial_kode=nh.seri_kapal " +
                    "left join kota on ktj.kota_tujuan=kota.kode_kota  " +
                    "left join kapal on kapal.kode_kapal=ktj.kode_kapal " +
                    "where coalesce(nh.nota_header,'')='' " +
                    "and tagihan_per='"+sTo+"' and customer='"+sCustomer+"' " +
                    "and to_Char(tgl_berangkat,'yyyy-MM-dd') >= '"+sTglBerangkat+"' " +
                    "and nh.no_nota <> '"+sNotaHeader+"' " +
                    "group by coalesce(priority,0) , d.no_nota , " +
                    "kapal.nama_kapal , to_char(ktj.tgl_berangkat,'dd-MM-yyyy') " +
                    "order by priority ";
            
        }else{ //Jika tidak lookup --> Tujuannya untuk diurutkan saja
            sQry   ="select * from (" +
                    "select  coalesce(priority,0) as priority, d.no_nota as nota_header, " +
                    "kapal.nama_kapal as kapal, to_char(ktj.tgl_berangkat,'dd-MM-yyyy') as tgl_berangkat, " +
                    "sum(coalesce(sub_total,0)) as sub_total " +
                    "from nota nh " +
                    "left join nota_detail d on d.no_nota=nh.no_nota " +
                    "left join nota_kapal_tujuan ktj on ktj.serial_kode=nh.seri_kapal " +
                    "left join kota on ktj.kota_tujuan=kota.kode_kota  " +
                    "left join kapal on kapal.kode_kapal=ktj.kode_kapal " +
                    "where coalesce(nh.nota_header,'')='' and nh.no_nota='"+sNotaHeader+"' " +
                    "group by coalesce(priority,0) , d.no_nota , " +
                    "kapal.nama_kapal , to_char(ktj.tgl_berangkat,'dd-MM-yyyy') " +
                    " " +
                    "union all " +
                    "select coalesce(nota.priority,0) as priority, nota.no_nota as nota_header, " +
                    "kapal.nama_kapal as kapal ,to_char(ktj.tgl_berangkat,'dd-MM-yyyy') as tgl_berangkat, " +
                    "sum(coalesce(sub_total,0)) as sub_total " +
                    "from nota " +
                    "inner join nota_detail d on d.no_nota=nota.no_nota " +
                    "inner join nota nh on nh.no_nota=nota.nota_header " +
                    "left join nota_kapal_tujuan ktj on ktj.serial_kode=nota.seri_kapal " +
                    "left join kota on ktj.kota_tujuan=kota.kode_kota  " +
                    "left join kapal on kapal.kode_kapal=ktj.kode_kapal " +
                    "where nota.nota_header='"+sNotaHeader+"' " +
                    "group by coalesce(nota.priority,0) , nota.no_nota , " +
                    "kapal.nama_kapal ,to_char(ktj.tgl_berangkat,'dd-MM-yyyy') ) A " +
                    "order by priority ";
        }
        System.out.println(sQry);
        return sQry;
    }
    
    private void udfUrutkan(){
        for(int i=0; i< jTable1.getRowCount(); i++) {
            try {
                jTable1.setValueAt(i + 1, i, 0);
                conn.createStatement().executeUpdate("Update nota set priority =" + (i+1) + " where no_nota ='" + jTable1.getValueAt(i, 1) + "' ");
            } catch (SQLException ex) {
                Logger.getLogger(FrmNotaLookup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void udfAddNota(){
        int iRow=jTable1.getSelectedRow();
            
        for(int i=0; i<srcModel.getRowCount(); i++){
            if(srcModel.getValueAt(i, 6).toString().trim().equalsIgnoreCase(jTable1.getValueAt(iRow, 1).toString())){
                JOptionPane.showMessageDialog(this, "Anda sudah memasukkan No. Nota tersebut!", "Message", JOptionPane.OK_OPTION);
                //txtNoNotaDetail.requestFocus();
                return;
            }
        }
        try{
            
            Statement sD=conn.createStatement();
            ResultSet rD=sD.executeQuery("select coalesce(merk,'') as merk, item_trx, ukuran, satuan, tarif, sub_total " +
                    "from nota_detail where no_nota='"+jTable1.getValueAt(iRow, 1).toString()+"' ");
            
            if(rD.next()){
                //Tambahkan dulu Nota Headernya
                srcModel.addRow(new Object[]{
                    "",
                    jTable1.getValueAt(iRow, 2).toString()+" , Brgkt tgl. "+jTable1.getValueAt(iRow, 3).toString(),
                    "",
                    "",
                    "",
                    "",
                    jTable1.getValueAt(iRow, 1).toString()
                });
                
                //Tambahkan nota detailnya
                
                srcModel.addRow(new Object[]{
                    rD.getString("merk"),
                    rD.getString("item_trx"),
                    rD.getFloat("ukuran"),
                    rD.getString("satuan"),
                    rD.getFloat("tarif"),
                    rD.getFloat("sub_total"),
                    ""
                });
                
                while(rD.next()){
                    srcModel.addRow(new Object[]{
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
        }catch(SQLException se){
            JOptionPane.showMessageDialog(this, "Ada kesalahan dalam menambahkan nota!\n"+se.getMessage());
            return;
        }
        
        //udfClearItem();
        
        //txtNoNota.requestFocus();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnSelect = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnBawah = new javax.swing.JButton();
        btnUrutkanSimpan = new javax.swing.JButton();
        btnAtas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Urutan", "No. Nota", "Kapal", "Tgl. Berangkat", "Total Nota"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class
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
        jTable1.setName("jTable1"); // NOI18N
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(70);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 640, 290));

        btnSelect.setText("Pilih");
        btnSelect.setName("btnSelect"); // NOI18N
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        getContentPane().add(btnSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 300, 90, 30));

        btnCancel.setText("Tutup");
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 300, 90, 30));

        btnBawah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/bawah.PNG"))); // NOI18N
        btnBawah.setName("btnBawah"); // NOI18N
        btnBawah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBawahActionPerformed(evt);
            }
        });
        getContentPane().add(btnBawah, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 30, 30));

        btnUrutkanSimpan.setText("Urutkan & Simpan");
        btnUrutkanSimpan.setName("btnUrutkanSimpan"); // NOI18N
        btnUrutkanSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUrutkanSimpanActionPerformed(evt);
            }
        });
        getContentPane().add(btnUrutkanSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, -1, 30));

        btnAtas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/atas.PNG"))); // NOI18N
        btnAtas.setName("btnAtas"); // NOI18N
        btnAtas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtasActionPerformed(evt);
            }
        });
        getContentPane().add(btnAtas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 30, 30));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-651)/2, (screenSize.height-372)/2, 651, 372);
    }// </editor-fold>//GEN-END:initComponents

private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    myModel = (DefaultTableModel)jTable1.getModel();
    myModel.setNumRows(0);
    jTable1.setModel(myModel);
    
    udfLoadTable();
}//GEN-LAST:event_formWindowOpened

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    try {
            rs.close();//GEN-LAST:event_formWindowClosed
    } catch (SQLException ex) {
        Logger.getLogger(FrmNotaLookup.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    this.dispose();
}//GEN-LAST:event_btnCancelActionPerformed

private void btnAtasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtasActionPerformed
    if(jTable1.getSelectedRow()>0){
        myModel.moveRow(jTable1.getSelectedRow(), jTable1.getSelectedRow(), jTable1.getSelectedRow()-1);
        udfUrutkan();
        jTable1.setRowSelectionInterval(jTable1.getSelectedRow()-1, jTable1.getSelectedRow()-1);
        
    }
}//GEN-LAST:event_btnAtasActionPerformed

private void btnBawahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBawahActionPerformed
    if(jTable1.getSelectedRow()< jTable1.getRowCount()-1){
        myModel.moveRow(jTable1.getSelectedRow(), jTable1.getSelectedRow(), jTable1.getSelectedRow()+1);
        udfUrutkan();
        jTable1.setRowSelectionInterval(jTable1.getSelectedRow()+1, jTable1.getSelectedRow()+1);
        
    }
}//GEN-LAST:event_btnBawahActionPerformed

private void btnUrutkanSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUrutkanSimpanActionPerformed
    if(JOptionPane.showConfirmDialog(this, "Sistem akan membuat urutan prioritas nota sebagaimana terdaftar. Apakah akan diteruskan?", "Urutan nota", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        for(int i=0; i< jTable1.getRowCount(); i++) {
            try {
                jTable1.setValueAt(i + 1, i, 0);
                conn.createStatement().executeUpdate("Update nota set priority =" + (i+1) + " where no_nota ='" + jTable1.getValueAt(i, 1) + "' ");
            } catch (SQLException ex) {
                Logger.getLogger(FrmNotaLookup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}//GEN-LAST:event_btnUrutkanSimpanActionPerformed

private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
    udfAddNota();
}//GEN-LAST:event_btnSelectActionPerformed
    

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmNotaLookup dialog = new FrmNotaLookup(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAtas;
    private javax.swing.JButton btnBawah;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSelect;
    private javax.swing.JButton btnUrutkanSimpan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
