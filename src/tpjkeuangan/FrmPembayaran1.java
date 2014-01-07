/*
 * FrmPharPenjualan.java
 *
 * Created on December 13, 2006, 4:29 PM
 */

package tpjkeuangan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.AbstractCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author  root
 */
public class FrmPembayaran1 extends javax.swing.JInternalFrame {
    MyTableCellEditor cEditor; 
    Connection conn;
    Statement st, stDet, stDel;
    ResultSet rs, rsDet;
    String sCariSql;
    private String sekarang="";
    private ListRsbm lst;
    private String sUserID="";
    private String sUserName="";
    private final static long serialVersionUID = 42;
    private DefaultTableModel modelNota;
    private static String sClose="close";
    public boolean bAsc=false;
    //final DecimalFormat formatter = new DecimalFormat("###,##0.00");
    private NumberFormat formatter = new DecimalFormat("#,###,###");
    
    private NumberFormat formatter1 = new DecimalFormat("#,###,###.###");
    private String sQry,shift;
    private Boolean bNew ;
    private Boolean bEdit,editItem=false,okSave=false;
    private SimpleDateFormat fdateformat;
    private String dateNow,dateFormatddmmyy;
    private JFormattedTextField jFDate1;
    private Integer rowSelected;
    private Double total,totalDiscount,totalVat,netto,batasPO=50000000.0;
    DefaultTableModel modelKeterangan;
    private int iRow;
    private MyKeyListener kListener =new MyKeyListener();
    private boolean isSave=false;
    
    /** Creates new form FrmPharPenjualan */
    public FrmPembayaran1() {
        initComponents();
        
//        f1.SetConn(conn);
        
        for(int i=0;i<jPanel3.getComponentCount();i++){
            Component c = jPanel3.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")  ) {
                c.addKeyListener(kListener);
            }
        }
        //tblItem.addKeyListener(kListener);
        btnTambahItem.addKeyListener(kListener);
        
        for(int i=0;i<jPanel1.getComponentCount();i++){
            Component c = jPanel1.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")  ) {
                c.addKeyListener(kListener);
            }
        }
        for(int i=0;i<jPanel2.getComponentCount();i++){
            Component c = jPanel2.getComponent(i);
            if(c.getClass().getSimpleName().equalsIgnoreCase("JTEXTFIELD")    || c.getClass().getSimpleName().equalsIgnoreCase("JFormattedTextField")
            || c.getClass().getSimpleName().equalsIgnoreCase("JTextArea") || c.getClass().getSimpleName().equalsIgnoreCase("JComboBox")
            || c.getClass().getSimpleName().equalsIgnoreCase("JButton") || c.getClass().getSimpleName().equalsIgnoreCase("JCheckBox")  ) {
                c.addKeyListener(kListener);
            }
        }
        
        this.addKeyListener(new MyKeyListener());
        
    }

    void setConn(Connection conn) {
        this.conn = conn;
    }

    public class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        private Toolkit toolkit;
        JTextField component=new JTextField(""); 
        JLabel label;// =new JLabel("");
        
        int col, row;
        
        public void setComponent(JTextField com){
            component=com;
            
            component.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    componentKeyPressed(evt);
                }
                
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    //if(col==4)
                        //componentKeyReleased(evt);
                }
            }); 
            
            component.addFocusListener(new java.awt.event.FocusAdapter() {
                 public void focusGained(java.awt.event.FocusEvent evt) {
                      componentFocusGained(evt);
                      
                 }
                 public void focusLost(java.awt.event.FocusEvent evt) {
                      componentFocusLost(evt);
                 }
            });                    
          
        }
        
        public void setCellLabel(JLabel lbl){
            label=lbl;
        }
        
        public void setComponentFocus(){
            component.setSelectionStart(0);
            component.setSelectionEnd(component.getText().length());
            component.requestFocusInWindow();
            component.requestFocus();
        }
        
//        Font ft=new Font("Tahoma",Font.BOLD,14);
//        component.setFont(new Font("Tahoma",Font.BOLD,14));
        
        private NumberFormat  nf=NumberFormat.getInstance();
         
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at (rowIndex, vColIndex)
            
           col=vColIndex;
           row=rowIndex;
           component.setBackground(new Color(248,255,167));
           if (isSelected) {
               
            
           }       
           //System.out.println("Value dari editor :"+value);
            component.setText(value==null? "": value.toString());                                    
            //component.setText(df.format(value));                                    
                        
            if(value instanceof Double || value instanceof Float|| value instanceof Integer){
                try {
                    //Double dVal=Double.parseDouble(value.toString().replace(",",""));
                    Number dVal = nf.parse(value.toString());
                    component.setText(nf.format(dVal));
                } catch (ParseException ex) {
                    Logger.getLogger(FrmPembayaran1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else
                component.setText(value==null? "":value.toString());
           return component;
        }
         
        private void componentFocusLost(java.awt.event.FocusEvent evt) {
//            if (component.isVisible()==false){
//                //System.out.println("Component Focus Lost"+"-"+tblJadwal.getSelectedRow()+"-"+tblJadwal.getSelectedColumn()); 
//                lst.setVisible(false);
//            }
//            if(lst.isVisible())
//                lst.setVisible(false)
        }  
        
        private void componentFocusGained(java.awt.event.FocusEvent evt) {
            //if(col>=6)
                component.setSelectionStart(0);
                component.setSelectionEnd(component.getText().length());
            //System.out.println("Component Focus Gained");               
        }  

        public void componentKeyPressed(java.awt.event.KeyEvent evt) {                                  
           //System.out.println(evt.getKeyChar());
           if (evt.getKeyChar() == evt.CHAR_UNDEFINED){
               
           }else{                
            
           }
            
           //----------------------------------
          char c = evt.getKeyChar();
          if (!((c >= '0') && (c <= '9') ||
             (c != KeyEvent.VK_BACK_SPACE) ||
             (c != KeyEvent.VK_DELETE) ||
             (c != KeyEvent.VK_ENTER))) {
                getToolkit().beep();
                evt.consume();
                return;
          }
           
           //-----------------------------------
           switch(evt.getKeyCode()) {
               case KeyEvent.VK_ENTER:{
                        //tblResep.changeSelection(tblResep.getSelectedRow()-1,tblResep.getSelectedColumn()+1, false, false);
                        if(component.isVisible()){
                            
                            tblItem.setRowSelectionInterval(tblItem.getSelectedRow(), tblItem.getSelectedRow());
                            component.setSelectionStart(0);
                            component.setSelectionEnd(component.getText().length());
//                            component.setVisible(false);
                        }else{
                            component.setVisible(true);
                            component.setFocusable(true);
                            component.setSelectionStart(0);
                            component.setSelectionEnd(component.getText().length());
                            }
                        
                        if(col >=6){
                            //tblResep.changeSelection(row, col, false, false);
                            //tblResep.setRowSelectionInterval(row-1, row-1);
                            tblItem.setColumnSelectionInterval(col, col);
                            
                            if(tblItem.getSelectedRow()>0)
                                tblItem.setRowSelectionInterval(row-1, row-1);
                            
                        }else
                        {
                            tblItem.changeSelection(row, col, false, false);
                            tblItem.setColumnSelectionInterval(row, 3);
                        }
                        }
                        break;
                    
                    case java.awt.event.KeyEvent.VK_DOWN:{
                        //System.out.println("ok Key down");
                        int row=tblItem.getSelectedRow();
                        System.out.println(row);
                        
                        }
                        break;
                        
                    case java.awt.event.KeyEvent.VK_UP:{
                        //System.out.println("ok Key Up");
                        if(tblItem.getSelectedRow()>0)
                        
                            tblItem.setRowSelectionInterval(tblItem.getSelectedRow(), tblItem.getSelectedRow());
                        else
                            tblItem.setRowSelectionInterval(0,0);
                        }
                    
                    default:
                        {
                            component.setSelectionEnd(component.getText().length());
                        }                        
                        break;
                }
        }
                    
        public Object getCellEditorValue() {
//            //System.out.println(lf.getResCode()+"  "+lf.getResDes());
            Object o="";//=component.getText();
//            if((Double)Double.parseDouble(component.getText().replace(",","")) instanceof Number)
//            {
////            if ((o!=null) && o instanceof Double)
//                //return ((JTextField)component).getText();
//                o=((JTextField)component).getText();
//                //o=doubleFormatter.format(((JTextField)component).getText());
//            }
//            else
//                o=0;
            
            //====================================================================
            Object retVal = 0;
		if(col>=6){
                    try {
                        retVal = Integer.parseInt(((JTextField)component).getText().replace(",",""));
                        
//                        if(Integer.parseInt(retVal.toString() > Integer.parseInt(tblItem.getValueAt(tblItem.getSelectedRow(), 5).toString())){
//                            JOptionPane.showMessageDialog(null, "Jumlah pembayaran melebihi jumlah terhutang!");
//                            toolkit.beep();
//                            retVal=0;
//                        }
                        o=nf.format(retVal);
                        
                        
                        return o;
                    } catch (Exception e) {
                        toolkit.beep();
                        retVal=0;
                    }
                }else
                    retVal=(Object)component.getText();
		return retVal;
                
            //return o;                                    
        }
        
        public int getValue() {
		int retVal = 0;
		try {
                    retVal = Integer.parseInt(((JTextField)component).getText());
		} catch (Exception e) {
                    toolkit.beep();
		}
		return retVal;
	}
    }
    
    private boolean udfCekBeforeSave(){
        boolean b=true;
        if(txtToko.getText().trim().equalsIgnoreCase("")){
            udfShowMessage("Silakan Toko atau pengirim terlebih dulu!");
            txtToko.requestFocus();
            return false;
        }
        if(tblItem.getRowCount()<=0){
            JOptionPane.showMessageDialog(this, "Tidak ada piutang yang ditampilkan!", "Message", JOptionPane.OK_OPTION);
            tblItem.requestFocus();
            b=false;
            return false;
        }
        if(udfGetFloat(lblGrandTotal.getText())==0){
            JOptionPane.showMessageDialog(this, "Tidak ada jumlah yang dibayarkan!", "Message", JOptionPane.OK_OPTION);
            tblItem.requestFocus();
            b=false;
            return false;
        }
        if(udfGetFloat(lblGrandTotal.getText())>udfGetFloat(lblTotalTerutang.getText())){
            JOptionPane.showMessageDialog(this, "Pembayaran melebihi Sisa nota yang ada!", "Message", JOptionPane.OK_OPTION);
            tblItem.requestFocus();
            b=false;
            return false;
        }
        if( txtJenisBayar.getText().trim().equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Silakan isi jenis pembayaran terlebih dulu!", "Message", JOptionPane.OK_OPTION);
            txtJenisBayar.requestFocus();
            b=false;
            return false;
        }
        if(txtNoForm.getText().length()>0){
            try{
               Statement sCek=conn.createStatement();
               ResultSet rCek=sCek.executeQuery("Select * from nota_pembayaran where no_bayar='"+txtNoForm.getText()+"'");
               
               if(rCek.next()){
                pesanError("No. Formulir sudah ada, Silakan masukkan No. Formulir lain!");
               }
            
               rCek.close();
               sCek.close();
            }catch(SQLException se){
                System.err.println(se.getMessage());
            }
        }
        return b;
    }
    
    private String udfGetNewKodeBayar(){
        String s="select fn_nota_get_no_bayar('"+new SimpleDateFormat("yyyy-MM-dd").format(jCalBayar.getDate())+"')";
        
        try{
            Statement stm=conn.createStatement();
            ResultSet rsm=stm.executeQuery(s);

            if(rsm.next()){
                s=rsm.getString(1);
            }
            
            rsm.close();
            stm.close();
            
        }catch(SQLException se){
            System.out.println(se.getMessage());
        }
        return s;
    }
    
    private void udfBayar() {
        
        if(udfCekBeforeSave()){
//            String sInput="select fn_nota_save_bayar(" +
//                        "'"+new SimpleDateFormat("yyyy-MM-dd").format(jCalBayar.getDate()) +"', " +
//                        "'"+sCustomer+"', '', '"+txtJenisBayar.getText()+"', " +
//                        "'"+txtNoCek.getText()+"', '"+new SimpleDateFormat("yyyy-MM-dd").format(jCalCek.getDate()) +"'," +
//                        "'"+txtMemo.getText()+"', "+udfGetFloat(lblTotalBayar.getText())+", " +
//                        "'"+sNota+"', "+udfGetFloat(lblGrandTotal.getText())+", " +
//                        ""+udfGetFloat(txtKlem.getText())+", '"+sUserName+"')";
            
            String sNewCode=(txtNoForm.getText().trim().equalsIgnoreCase("")? udfGetNewKodeBayar(): txtNoForm.getText());
            txtNoForm.setText(sNewCode);
            String sH="Insert into nota_pembayaran(no_bayar, tanggal , kode_cust, alat_bayar, check_no, tgl_cek, memo, check_amount, to_tujuan, user_tr) values" +
                    "('"+sNewCode+"', '"+new SimpleDateFormat("yyyy-MM-dd").format(jCalBayar.getDate()) +"', " +
                    "'"+txtToko.getText()+"', '"+txtJenisBayar.getText()+"', '"+txtNoCek.getText()+"', " +
                    " '"+new SimpleDateFormat("yyyy-MM-dd").format(jCalCek.getDate()) +"','"+txtMemo.getText()+"'," +
                    ""+udfGetFloat(lblGrandTotal.getText())+", "+radioToko.isSelected()+", '"+sUserName+"' )";
            
            String sD="";
            System.out.println(sH);
            
            for(int i=0; i<modelNota.getRowCount(); i++){
                if(udfGetFloat(modelNota.getValueAt(i, 8).toString())>0 ||udfGetFloat(modelNota.getValueAt(i, 9).toString())>0){
                    sD+="Insert into nota_pembayaran_detail(no_bayar, no_nota, jumlah, klem ) values " +
                        "('"+sNewCode+"', '"+modelNota.getValueAt(i, 1).toString()+"', " +
                        ""+udfGetFloat(modelNota.getValueAt(i, 8).toString())+", " +
                        ""+udfGetFloat(modelNota.getValueAt(i, 9).toString())+"); ";
                }
            }
            
            try{
                conn.setAutoCommit(false);
                Statement stH=conn.createStatement();
                Statement stD=conn.createStatement();
                
                int iH=stH.executeUpdate(sH);
                int iD=stD.executeUpdate(sD);
                
                if(iH==0){
                    JOptionPane.showMessageDialog(this, "Simpan data gagal!", "Message", JOptionPane.WARNING_MESSAGE);
                    conn.setAutoCommit(true);
                }else{
                    JOptionPane.showMessageDialog(this, "Simpan data Sukses!", "Message", JOptionPane.OK_OPTION);
                    isSave=true;
                    udfClear();
                }
                
                conn.setAutoCommit(true);
                
            }catch(SQLException se){
                try {
                    JOptionPane.showMessageDialog(this, "Simpan data gagal : \n" + se.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(FrmPembayaran1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void udfDeleteItem() {
        iRow=tblItem.getSelectedRow();
        
        if(tblItem.getSelectedRow()>=0){
            if(JOptionPane.showConfirmDialog(this, "Anda yakin untuk menghapus item '"+tblItem.getValueAt(iRow, 6)+"' No. Penerimaan '"+tblItem.getValueAt(iRow, 1)+"'?", "Konfirmasi", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
//                try {
//                    Statement st = conn.createStatement();
//                    st.executeUpdate("delete from tmp_keterangan_detail where kode_barang='" + tblItem.getValueAt(iRow, 1) + "'");
                    modelNota.removeRow(iRow);
//                } catch (SQLException ex) {
//                    Logger.getLogger(FrmPengiriman.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            
        }
    }

    private void udfEditItemPengiriman() {
        int iRow=tblItem.getSelectedRow();
        if(iRow>=0){
//            DlgEditItemKirim f1=new DlgEditItemKirim(JOptionPane.getFrameForComponent(this), false);
//            f1.setCon(conn);
//            f1.setSrcTable(tblItem);
//            f1.setKode(tblItem.getValueAt(iRow, 5).toString());
//            f1.setNamaItem(tblItem.getValueAt(iRow, 6).toString());
//            f1.setIsPaket((tblItem.getValueAt(iRow, 11).toString().equalsIgnoreCase("paket")? true: false));
//            f1.setPanjang(tblItem.getValueAt(iRow, 8).toString());
//            f1.setLebar(tblItem.getValueAt(iRow, 9).toString());
//            f1.setTinggi(tblItem.getValueAt(iRow, 10).toString());
//            f1.setVolume((tblItem.getValueAt(iRow, 11).toString().equalsIgnoreCase("paket")? "": tblItem.getValueAt(iRow, 11).toString()));
//            f1.setQty(tblItem.getValueAt(iRow, 7).toString());
//            f1.setNoPenerimaan(tblItem.getValueAt(iRow, 1).toString());
//            f1.setTitle("Edit item pengiriman");
//            f1.setVisible(true);
        }
    }

    private void udfLoadTagihan(){
        
        
        String sQry="select * from fn_nota_list_tagihan('"+txtToko.getText()+"', '"+(radioToko.isSelected()? "T":"P" )+"') as (no_nota varchar, " +
                "tanggal text, kapal varchar, tgl_berangkat text, jumlah double precision, terbayar double precision, " +
                "terutang double precision)";
        
        try{
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sQry);
        
            modelNota.setNumRows(0);
            int i=1;
            while(rs.next()){
                modelNota.addRow(new Object[]{
                    i,
                    rs.getString("no_nota"),
                    rs.getString("tanggal"),
                    rs.getString("kapal"),
                    rs.getString("tgl_berangkat"),
                    rs.getFloat("jumlah"),
                    rs.getFloat("terbayar"),
                    rs.getFloat("terutang"),
                    0.0,
                    0.0,
                    ""
                });
                i++;
            }
        }catch(SQLException st){
            JOptionPane.showMessageDialog(this, st.getMessage());
            
        }
        
        
    }
   
    private void udfSave() {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String sNoNota="";
        
        if(udfCekBeforeSave()){
            try {
                
                if(bNew){
                    ResultSet rs = st.executeQuery("select fn_get_no_nota(current_date::varchar)");
                
                    if(rs.next())
                        sNoNota=rs.getString(1);
                
                    rs.close();
                    st.close();
                }
                
                conn.setAutoCommit(false);
                
                st=conn.createStatement();
                rs=st.executeQuery("select * from pembayaran where no_nota='"+sNoNota+"'");
                
                if(bNew)
                    rs.moveToInsertRow();
                
                
                rs.updateString("no_nota", sNoNota);
                rs.updateDate("tanggal", java.sql.Date.valueOf(df.format(jCalBayar.getDate())));
                rs.updateString("jenis_pembayaran",txtJenisBayar.getText() );
                //rs.updateString("terima_dari", txtTerimaDari.getText());
                rs.updateString("ket_pembayaran", txtMemo.getText());
                //rs.updateString("catatan", txtCatatan.getText());
                
                if(bNew)
                    rs.insertRow();
                else
                    rs.updateRow();
                
                int iDel=stDel.executeUpdate("delete from pembayaran_detail where no_nota='"+sNoNota+"'");
                
                rsDet=st.executeQuery("select * from pembayaran_detail limit 0");
                rsDet.moveToInsertRow();
                for (int i=0; i<tblItem.getRowCount(); i++){
                    rsDet.updateString("no_nota", sNoNota);
                    rsDet.updateString("no_penerimaan", tblItem.getValueAt(i, 1).toString());
                    rsDet.updateFloat("jumlah", Float.valueOf(tblItem.getValueAt(i, 6).toString().replace(",", "")));
                    rsDet.insertRow();
                }
                                                
                conn.setAutoCommit(true);
                
                rsDet.close(); stDet.close();
                
                udfBlank();
                
            } catch (SQLException ex) {
                try {
                    Logger.getLogger(FrmPembayaran1.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                    conn.rollback();
                    conn.setAutoCommit(true);
                    
                } catch (SQLException ex1) {
                    Logger.getLogger(FrmPembayaran1.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            
        }
    }

    private void udfSetFocusText(JFormattedTextField txt) {
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
    }

    private void udfSetFocusText(JTextField txt) {
        txt.setSelectionStart(0);
        txt.setSelectionEnd(txt.getText().length());
    }
    
    private void udfSetTotalLine(){
//        float totalLine=0, qty=0, harga=0, disc=0, ppn=0;
//        qty=udfGetFloat(ftQty.getText().trim());
//        harga=udfGetFloat(ftPrice.getText().trim());
//        disc=udfGetFloat(txtDiscPerItem.getText().trim());
//        ppn=udfGetFloat(txtPPNperItem.getText().trim());
//        
//        if (checkDisc_persen.isSelected()){ //Diskon persen
//            disc=qty*disc*harga/100;
//        }
//        if (checkPPN_persen.isSelected()){ //Diskon persen
//            ppn=qty*ppn*harga/100;
//        }
//        
//        totalLine=(qty*harga)-disc+ppn;
//        txtTotalLine.setText(formatter.format(totalLine));
        
    }
    
    private void udfBlank(){
        txtJenisBayar.setText("");
        lblJenisBayar.setText("");
        txtMemo.setText("");
        txtToko.setText("");
        lblToko.setText("");
        modelNota.setNumRows(0);
        lblGrandTotal.setText("0");
        
        txtJenisBayar.requestFocus();
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
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtJenisBayar = new javax.swing.JTextField();
        lblJenisBayar = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCalCek = new org.jdesktop.swingx.JXDatePicker();
        jLabel16 = new javax.swing.JLabel();
        txtNoCek = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMemo = new javax.swing.JTextArea();
        btnTambahItem = new javax.swing.JButton();
        btnTambahItem1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCalBayar = new org.jdesktop.swingx.JXDatePicker();
        jLabel6 = new javax.swing.JLabel();
        txtNoForm = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtToko = new javax.swing.JTextField();
        lblToko = new javax.swing.JLabel();
        radioToko = new javax.swing.JRadioButton();
        radioPengirim = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        lblGrandTotal = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        lblTotalNota = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lblTotalTerutang = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        lblTotalBayar = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        lblTotalKlem = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblTotalTerbayar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Pembayaran Pelanggan");
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
                formInternalFrameIconified(evt);
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText("Memo");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 69, 110, -1));

        jLabel24.setText("Jenis Pembayaran");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 13, 100, -1));

        txtJenisBayar.setFont(new java.awt.Font("Dialog", 0, 12));
        txtJenisBayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtJenisBayar.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtJenisBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJenisBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJenisBayarKeyReleased(evt);
            }
        });
        jPanel1.add(txtJenisBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 73, 24));

        lblJenisBayar.setFont(new java.awt.Font("Dialog", 0, 12));
        lblJenisBayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.add(lblJenisBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(193, 10, 390, 24));

        jLabel7.setText("Tgl. Cek");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(341, 42, 70, -1));
        jPanel1.add(jCalCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 39, 170, -1));

        jLabel16.setText("No. Cek");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 41, 100, -1));

        txtNoCek.setFont(new java.awt.Font("Dialog", 0, 12));
        txtNoCek.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtNoCek.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        txtNoCek.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoCekFocusLost(evt);
            }
        });
        txtNoCek.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoCekKeyReleased(evt);
            }
        });
        jPanel1.add(txtNoCek, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 38, 180, 24));

        txtMemo.setColumns(20);
        txtMemo.setRows(5);
        jScrollPane2.setViewportView(txtMemo);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 64, 462, 50));

        btnTambahItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/pulpen.png"))); // NOI18N
        btnTambahItem.setText("Simpan");
        btnTambahItem.setToolTipText("Simpan data pembayaran");
        btnTambahItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTambahItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTambahItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahItemActionPerformed(evt);
            }
        });
        jPanel1.add(btnTambahItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(592, 8, 100, 100));

        btnTambahItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/Exit_1.png"))); // NOI18N
        btnTambahItem1.setText("Tutup");
        btnTambahItem1.setToolTipText("Tutup aplikasi pembayaran");
        btnTambahItem1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTambahItem1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTambahItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahItem1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnTambahItem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(695, 8, 100, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 425, 800, 120));

        jPanel2.setBackground(new java.awt.Color(51, 102, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("No. Form");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(545, 10, 80, -1));
        jPanel2.add(jCalBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(632, 34, 160, -1));

        jLabel6.setText("Tgl. Bayar");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(544, 38, 90, -1));

        txtNoForm.setBackground(new java.awt.Color(234, 255, 246));
        txtNoForm.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNoForm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoFormKeyReleased(evt);
            }
        });
        jPanel2.add(txtNoForm, new org.netbeans.lib.awtextra.AbsoluteConstraints(631, 6, 160, 24));

        jLabel8.setText("Toko");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 35, 70, -1));

        txtToko.setFont(new java.awt.Font("Dialog", 0, 12));
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
        jPanel2.add(txtToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 31, 64, 24));

        lblToko.setBackground(new java.awt.Color(255, 255, 255));
        lblToko.setFont(new java.awt.Font("Dialog", 0, 12));
        lblToko.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblToko.setOpaque(true);
        jPanel2.add(lblToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 31, 330, 24));

        radioToko.setSelected(true);
        radioToko.setText("Toko Tujuan");
        radioToko.setOpaque(false);
        radioToko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioTokoActionPerformed(evt);
            }
        });
        jPanel2.add(radioToko, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 7, 130, -1));

        radioPengirim.setText("Toko Pengirim");
        radioPengirim.setOpaque(false);
        radioPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioPengirimActionPerformed(evt);
            }
        });
        jPanel2.add(radioPengirim, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 6, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 66, 800, 70));

        tblItem.setAutoCreateRowSorter(true);
        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "No Nota", "Tgl. Nota", "Kapal", "Tgl. Brngkt", "Jumlah", "Terbayar", "Terutang", "Jumlah Bayar", "Klem", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItem.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblItem.setCellSelectionEnabled(true);
        tblItem.getTableHeader().setReorderingAllowed(false);
        tblItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemMouseClicked(evt);
            }
        });
        tblItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblItemFocusGained(evt);
            }
        });
        tblItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblItemKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblItemKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tblItem);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 137, 800, 200));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("GRAND TOTAL");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(581, 51, 101, -1));

        lblGrandTotal.setFont(new java.awt.Font("Dialog", 3, 12));
        lblGrandTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrandTotal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblGrandTotal.setOpaque(true);
        jPanel3.add(lblGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(676, 49, 119, 22));

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Jumlah Nota");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 8, 101, -1));

        lblTotalNota.setFont(new java.awt.Font("Dialog", 3, 12));
        lblTotalNota.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalNota.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTotalNota.setOpaque(true);
        jPanel3.add(lblTotalNota, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 4, 119, 22));

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Sisa Piutang");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 50, 126, -1));

        lblTotalTerutang.setFont(new java.awt.Font("Dialog", 3, 12));
        lblTotalTerutang.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTerutang.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTotalTerutang.setOpaque(true);
        jPanel3.add(lblTotalTerutang, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 48, 119, 22));

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("TOTAL BAYAR");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 9, 101, -1));

        lblTotalBayar.setFont(new java.awt.Font("Dialog", 3, 12));
        lblTotalBayar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalBayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTotalBayar.setOpaque(true);
        jPanel3.add(lblTotalBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(676, 5, 119, 22));

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("TOTAL KLEM");
        jPanel3.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(581, 29, 101, -1));

        lblTotalKlem.setFont(new java.awt.Font("Dialog", 3, 12));
        lblTotalKlem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalKlem.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTotalKlem.setOpaque(true);
        jPanel3.add(lblTotalKlem, new org.netbeans.lib.awtextra.AbsoluteConstraints(676, 27, 119, 22));

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Sudah Bayar");
        jPanel3.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 28, 126, -1));

        lblTotalTerbayar.setFont(new java.awt.Font("Dialog", 3, 12));
        lblTotalTerbayar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalTerbayar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        lblTotalTerbayar.setOpaque(true);
        jPanel3.add(lblTotalTerbayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 26, 119, 22));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 343, 800, 80));

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 0, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("Pembayaran Nota Tagihan");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 17, 480, 40));

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 0, 36));
        jLabel2.setText("Pembayaran Nota Tagihan");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(327, 18, 480, 40));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-828)/2, (screenSize.height-588)/2, 828, 588);
    }// </editor-fold>//GEN-END:initComponents

    private void tblItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblItemFocusGained
        //setEnableComp1(false);
}//GEN-LAST:event_tblItemFocusGained

    public void setUpSportColumn(JTable table,
                                 TableColumn sportColumn) {
//        sportColumn.setCellEditor(new TextEditorOk(conn,"kode_barang,nama_baang","phar_item",jScrollPane1.getX()+this.getX(), jScrollPane1.getY()+this.getY()));
    }
    
//    private void isilist(String sss,JTextField txt){
//    try{
//        lst.setBounds(this.getParent().getParent().getX()+this.getParent().getX()+this.getX()+this.jPanel1.getX() + txt.getX()+10,
//                                this.getParent().getParent().getY()+this.getParent().getY()+this.getY()+this.jPanel1.getY()+txt.getY()+txt.getHeight()+75,300,200);
//        lst.setSQuery(sss);
//        lst.setFocusableWindowState(false);
//        lst.setTxtCari(txt);
//        lst.setLblDes(new javax.swing.JLabel[]{lblSite,lblGudang});
//        lst.setColWidth(0, txtLocation.getWidth()-1);
//        lst.setColWidth(1, 75);
//        lst.setColWidth(2, 75);
//        lst.setColWidth(3, 120);
//        if(lst.getIRowCount()>0){
//            lst.setVisible(true);
//        } else{
//            lst.setVisible(false);
//        }
//    }catch(SQLException se){}
//    }
    
//    private void findData(){
//        txtNoPR.requestFocus();
//        String sss="select no_pr,site_id,location_id,to_char(release_date,'yyyy/MM/dd') from phar_pr where coalesce(flag_tr,'')='T' and upper(no_pr||site_id||location_id||to_char(release_date,'yyyy/MM/dd')) Like upper('%" + txtNoPR.getText().trim()+"%') order by 1";
//        isilist(sss,txtNoPR);
//    }
    
    private void delItem(){
      if (tblItem.getSelectedRow()>=0){
           int iDel = tblItem.getSelectedRow();
           DefaultTableModel model = (DefaultTableModel) tblItem.getModel();
           model.removeRow(iDel); 
           if (model.getRowCount()>0){
                tblItem.setRowSelectionInterval(model.getRowCount()-1,model.getRowCount()-1);
                tblItem.requestFocus();
           }
      }
    }
    
    private void udfInsertDetail(){
//        lblNo.setText(String.valueOf(tblGR.getRowCount()+1));    
//        ftDateExpired.setText(dateNow);
//        editItem=false;
//        setClearComp2();
//        setEnableComp1(true);
//        txtKodeBarang.selectAll();
//        txtKodeBarang.requestFocus();
    }
    
    
    
    private void tblItemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemKeyPressed
        switch (evt.getKeyCode()){
            case java.awt.event.KeyEvent.VK_F2:{
                udfUpdateData();
                break;
            }
            case java.awt.event.KeyEvent.VK_INSERT:{
                udfInsertDetail();
                break;
            }
            case java.awt.event.KeyEvent.VK_DELETE:{
                delItem();
                break;
            }
            case java.awt.event.KeyEvent.VK_ENTER:{
               if(tblItem.getRowCount()>=0){
//                    setEnableComp1(true);
//                    txtKodeBarang.setEnabled(false);
//                    rowSelected=tblGR.getSelectedRow();
//                    if (rowSelected==0){
//                        if (tblGR.getRowCount()>0){
//                        rowSelected=tblGR.getRowCount()-1;}
//                        else {rowSelected=0;}
//                    }
//                    else {if (tblGR.getRowCount()>0){rowSelected--;}else {rowSelected=0;}
//                    }
//                    tblGR.setRowSelectionInterval(rowSelected,rowSelected);
//                    ftQty.requestFocus();
//                    editItem=true;
                    break;
               } 
            }
        }
}//GEN-LAST:event_tblItemKeyPressed

  private void printKwitansi(String sNo_GR,Boolean okCpy){
            
            PrinterJob job = PrinterJob.getPrinterJob();
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            PrintServiceAttributeSet pset = new HashPrintServiceAttributeSet();
            PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
            int i=0;
            for(i=0;i<services.length;i++){
                if(services[i].getName().equalsIgnoreCase("PRINTER")){
                    break;
                }
            }
            if (JOptionPane.showConfirmDialog(null,"Siapkan Printer!","Message ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {            
              //  PrintGood_receipt pn = new PrintGood_receipt(conn,sNo_GR,okCpy,services[i]);
//                PrintGood_receipt pn = new PrintGood_receipt(conn,sNo_GR,okCpy,sUserName,services[i]);
            }
    }  
  
  private void setOpenClosePO(){
//      try{
//          boolean itsok=false;
//          String sSql="select fn_phar_get_ok_po_close('"+txtNo_PO.getText().trim()+"') as ok_close";
//          conn.setAutoCommit(false);
//          Statement stat=conn.createStatement();
//          ResultSet rs=stat.executeQuery(sSql);
//          if (rs.next()){
//              itsok=rs.getBoolean("ok_close");
//          }
//          if (itsok){
//                stat.executeUpdate("update phar_po set closed=true where no_po='"+txtNo_PO.getText().trim()+"'");
//          }
//          conn.commit();
//          conn.setAutoCommit(true);
//          rs.close();
//          stat.close();
//      }catch(SQLException se){System.out.println(se.getMessage());
//            try{
//                conn.rollback();
//                conn.setAutoCommit(true);
//            }catch(SQLException ser){System.out.println(ser.getMessage());}
//      }
  }
  
//  private Boolean okSaveGood(phar_GoodReceiptBean prBean){
//      int ii=0;Boolean itsOk=false;
//      while(ii<tblGR.getRowCount() && !itsOk){
//          if (Double.valueOf(tblGR.getValueAt(ii,4).toString().trim().replace(",",""))>0){
//              itsOk=true;
//          }
//          ii++;
//      }
//      return itsOk;
//  }
  
  private float udfGetFloat(String sNum){
    float hsl=0;
    if(!sNum.trim().equalsIgnoreCase("")){
        try{
            hsl=Float.valueOf(sNum.replace(",", ""));
        }catch(NumberFormatException ne){
            hsl=0;
        }catch(IllegalArgumentException i){
            hsl=0;
        }
    }
    return hsl;
  }
  
  
  private void udfShowMessage(String sMsg){
    JOptionPane.showMessageDialog(this, sMsg);
  }
  
    
    private void formInternalFrameIconified(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameIconified
// TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameIconified

        
    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        Utama.isBayarOn=false;
        if (lst.isVisible()) lst.dispose();
    }//GEN-LAST:event_formInternalFrameClosed

    public void setBEdit(Boolean lEdit) {
        bEdit = lEdit;
    }
    
    public Boolean getBEdit() {
        return bEdit;
    }
    
    public void setShift(String Shift){
        this.shift=shift;
    }
    
    public void setBNew(Boolean lNew) {
        bNew = lNew;
    }
    
    public class MyRowRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //Component comp = getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
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
            
            JTextField tx=new JTextField();
                
            if(value instanceof Float ||value instanceof Double){
                setHorizontalAlignment(tx.RIGHT);
                value=formatter.format(value);
            }
//            if(column==0 ||(column>=3 && column<7)){
//                setHorizontalAlignment(tx.CENTER);
//                value=formatter1.format(value);
//            }
            
//            if(column==5){
//                value=formatter.format(value);
//            }
            
            setForeground(new Color(0,0,0));
            if (row%2==0){
                setBackground(w);
            }else{
                setBackground(g);
            }
            
            if(column==6)
                setFont(new Font("Tahoma", 1, 12));
            else
                setFont(new Font("Tahoma", 0, 12));
            
            if(isSelected){
                setBackground(new Color(0,102,255));
                setForeground(new Color(255,255,255));
            }
            
            setValue(value);
            return this;
        }
    }
    
    private void TableLook(int kk){
    
        modelNota=(DefaultTableModel)tblItem.getModel();
        modelNota.setNumRows(0);
        tblItem.setModel(modelNota);
        
        tblItem.getColumnModel().getColumn(0).setPreferredWidth(30);    //No
        tblItem.getColumnModel().getColumn(1).setPreferredWidth(130);    //No. Nota
        tblItem.getColumnModel().getColumn(2).setPreferredWidth(60);   //Tgl. Nota
        tblItem.getColumnModel().getColumn(3).setPreferredWidth(100);    //Kapal
        tblItem.getColumnModel().getColumn(4).setPreferredWidth(70);    //Tgl. Berangkat
        tblItem.getColumnModel().getColumn(5).setPreferredWidth(90);    //Jumlah
        tblItem.getColumnModel().getColumn(6).setPreferredWidth(90);    //Terbayar
        tblItem.getColumnModel().getColumn(7).setPreferredWidth(90);    //Terutang
        tblItem.getColumnModel().getColumn(8).setPreferredWidth(90);    //Bayar
        tblItem.getColumnModel().getColumn(9).setPreferredWidth(90);    //Klem
         tblItem.getColumnModel().getColumn(9).setPreferredWidth(100);    //Ketrangan
         
        tblItem.setRowHeight(25);
        for (int i=0;i<tblItem.getColumnCount();i++){
            tblItem.getColumnModel().getColumn(i).setCellRenderer(new MyRowRenderer());
        }
        
   
        tblItem.setAutoscrolls(true);
     }
    
    private void writeMsg(String sMsg){
        JOptionPane.showMessageDialog(this,sMsg);
    }
    
   public class MyTableModelListener implements TableModelListener {
        JTable table;
        
        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        MyTableModelListener(JTable table) {
            this.table = table;
        }
        
        public void tableChanged(TableModelEvent e) {
            int firstRow = e.getFirstRow();
            int lastRow = e.getLastRow();
            int mColIndex = e.getColumn();
                        
            udfSetTotal();
            
        }
    }
    
   private void udfSetTotal(){
        int i=0;
        float tot_jumlah=0,tot_terbayar=0, tot_bayar=0, tot_klem=0;
        
        float f_tot_terutang=0;
        
        for (i=0; i<tblItem.getModel().getRowCount();i++){
            tot_jumlah+=udfGetFloat(tblItem.getValueAt(i, 5).toString());
            tot_terbayar+=udfGetFloat(tblItem.getValueAt(i, 6).toString());
            f_tot_terutang+=udfGetFloat(tblItem.getValueAt(i, 7).toString());
            
            tot_bayar+=udfGetFloat(tblItem.getValueAt(i, 8).toString());
            tot_klem+=udfGetFloat(tblItem.getValueAt(i, 9).toString());
//            tot_jumlah=tot_jumlah+f_tot_jumlah;
//            tot_terutang=tot_terutang+f_tot_terutang;
//            tot_bayar=tot_bayar+f_tot_bayar;
            
        }
        
        lblTotalNota.setText(formatter1.format(tot_jumlah));
        lblTotalTerbayar.setText(formatter1.format(tot_terbayar));
        lblTotalTerutang.setText(formatter1.format(f_tot_terutang));
        
        lblTotalBayar.setText(formatter1.format(tot_bayar));
        lblTotalKlem.setText(formatter1.format(tot_klem));
        lblGrandTotal.setText(formatter1.format(tot_bayar+tot_klem));
               
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
                tblItem.getModel().addTableModelListener(new MyTableModelListener(tblItem));
                rowPos = table.getSelectedRow();           
                udfSetTotal();
                btnTambahItem.setEnabled(false);
                    
                if (rowPos >=0 ) { 
                    btnTambahItem.setEnabled(true);
                    
              }
            }
        }
        
    }
    
    public void addPOItem(String sNo) {
       try {
//            TableLook(0); 
            //DefaultTableModel modelNota=(DefaultTableModel)tblItem.getModel();
//            tblGR.setAutoResizeMode(tblGR.AUTO_RESIZE_OFF);
//            tblGR.getModel().addTableModelListener(new MyTableModelListener(tblGR));
            String sPO= "select * from fn_show_penerimaan('"+sNo+"') " +
                            "as(kode_barang varchar, nama_barang varchar, coli double precision, " +
                            "panjang double precision , lebar double precision, tinggi double precision, m3 varchar)";
                    
                    System.out.println(sPO);
                    st = conn.createStatement();
                    ResultSet rs=st.executeQuery(sPO);
                    modelNota.setNumRows(0);
                    
                    int i=1;
                    while(rs.next()){
                        //btnOK.setEnabled(true);
                        modelNota.addRow(new Object[]{i,
                                                rs.getString("kode_barang"),
                                                rs.getString("nama_barang"),
                                                rs.getString("coli"),
                                                rs.getDouble("panjang"),
                                                rs.getDouble("lebar"),
                                                rs.getDouble("tinggi"),
                                                rs.getString("m3")
                        });
                        i++;
                    }
            
            //tblItem.setRowSelectionInterval(row-2, row-2);
            
            rs.close();
            tblItem.setRequestFocusEnabled(true);
            
        } catch(SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    
    public void initJDBC() {
//       try {
            TableLook(0); 
            modelNota=(DefaultTableModel)tblItem.getModel();
            
            
            SelectionListener listener = new SelectionListener(tblItem);
            tblItem.getSelectionModel().addListSelectionListener(listener);
            tblItem.getColumnModel().getSelectionModel().addListSelectionListener(listener);
            tblItem.setRequestFocusEnabled(true);
        
            JTableHeader header = tblItem.getTableHeader();
            Font fH;
            fH=new Font("Tahoma",Font.BOLD,14);
            header.setFont(fH);
            //header.setBackground((new Color(234,243,244)));
            tblItem.setAutoscrolls(true);
//        } catch(SQLException se) {
//            System.out.println(se.getMessage());
//        }
    }
    
   private void udfCancel(){
        if (lst.isVisible()){lst.dispose();}
        if (getBEdit()) {
            setBEdit(false);
            setBNew(false);
            setClearComp();
            
            tblItem.setRequestFocusEnabled(true);
        } else {
            this.dispose();
        }
    }
       
    private void setClearComp(){
//        txtSupplier.setText("");
//        lblSupplier.setText("");
//        txtRemark.setText("");
//        txtGudang.setText("");
//        lblGudang.setText("");
//        txtPenerima.setText("");
//        txtNoDO.setText("");
//        txtSite.setText("");
//        lblSite.setText("");
//        txtLocation.setText("");
    }   
           
    private void setTab2Enter(){
        Set forwordKeys= getFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set newForwardKeys = new HashSet(forwordKeys);
        newForwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,newForwardKeys);
    }   
    
    public class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            int keyKode = evt.getKeyCode();
            switch(keyKode){
                case KeyEvent.VK_ENTER : {
		    if(evt.getSource().getClass().getName().equals("JTable")){
                        return;
                    }
                    if (!lst.isVisible()){
			Component c = findNextFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_UP : {
                    
                    if(evt.getSource().getClass().getName().equals("JTable")){
                        return;
                    }
		    if (!lst.isVisible()){
			Component c = findPrevFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_DOWN : {
		    if(evt.getSource().getClass().getName().equals("JTable")){
                        return;
                    }
                    if (!lst.isVisible()){
			Component c = findNextFocus();
			if (c!=null) c.requestFocus();
		    }else{
			lst.requestFocus();
		    }
		    break;
		}
                case KeyEvent.VK_INSERT: {  //
//                    if (getBEdit()){
//                        if (tblItem.getRowCount()>=0){
//                            udfInsertDetail();
//                        }
//                    }    
                    break;
                }
                 case KeyEvent.VK_F2 : {  //Add
//                    udfLookupPenerimaan();
                    break;
                }
                case KeyEvent.VK_F3: {  //Edit
                 //   udfFilter();
                    break;
                }
                case KeyEvent.VK_F4: {  //Delete
                    udfDeleteItem();
                    break;
                }
                
                case KeyEvent.VK_ESCAPE: {
                    //Jika status button adalah Close
                    if(sClose.equalsIgnoreCase("close")){
                        if(!getBEdit()){
                            if(JOptionPane.showConfirmDialog(null,"Anda Yakin Untuk Keluar?","Message",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                                if(lst.isVisible()){lst.dispose();}
                                dispose();
                            }
                        }
                        else
                            if(JOptionPane.showConfirmDialog(null,"Apakah data disimpan sebelum anda keluar?","Message",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                                if(lst.isVisible()){lst.dispose();}
                                udfUpdateData();
                            }
                            else{
                                if(lst.isVisible()){lst.dispose();}
                                dispose();
                            }

                            break;
                    }   //Jika cancel
                    else
                        udfCancel();
                }
                //default ;
                
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
    
    private void onOpen(String sField,  boolean bFilter){
        
   }
    
    public void setUserId(String newUserID){
	sUserID = newUserID;
    }
    
    public void setUserName(String newUserName){
	sUserName = newUserName;
        
    }
    
    public Boolean getBNew() {
        return bNew;
    }
    
     
    private void ButtonIcon(String aFile,javax.swing.JButton newBtn) {              
       javax.swing.ImageIcon myIcon = new javax.swing.ImageIcon(getClass().getResource(aFile));
       newBtn.setIcon(myIcon);
   }
    
    
    
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        cEditor=new MyTableCellEditor();
        jCalCek.setVisible(false);
        
        tblItem.getColumnModel().getColumn(7).setCellEditor(cEditor);
        tblItem.getColumnModel().getColumn(8).setCellEditor(cEditor);
        tblItem.getColumnModel().getColumn(9).setCellEditor(cEditor);
        try {
            lst = new ListRsbm();
            lst.setVisible(false);
            lst.setSize(500, 200);
            lst.con = conn;

            st = conn.createStatement();
            stDet = conn.createStatement();
            stDel = conn.createStatement();
            
            bNew=true;
            
            MaskFormatter fmttgl = null;
            try {
                fmttgl = new MaskFormatter("##/##/####");
            } catch (java.text.ParseException e) {
            }

            try {
                Statement stTgl = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rtgl = stTgl.executeQuery("select cast(current_date as varchar) as tanggal");
                if (rtgl.next()) {
                    fdateformat = new SimpleDateFormat("yyyy/MM/dd");
                    dateFormatddmmyy = fdateformat.format(rtgl.getDate(1));
                    fdateformat = new SimpleDateFormat("dd/MM/yyyy");
                    dateNow = fdateformat.format(rtgl.getDate(1));
                }
                stTgl.close();
                rtgl.close();
            } catch (SQLException se) {
            }

            jFDate1 = new JFormattedTextField(fmttgl);
            SelectionListener listener = new SelectionListener(tblItem);
            tblItem.getSelectionModel().addListSelectionListener(listener);
            tblItem.getColumnModel().getSelectionModel().addListSelectionListener(listener);
            tblItem.getModel().addTableModelListener(new MyTableModelListener(tblItem));
            tblItem.setRequestFocusEnabled(true);

            
            TableLook(0);
            //setTab2Enter();
            setBEdit(false);
            setBNew(true);
            setFocus();
            //setMouseList();
            txtToko.requestFocus();

            requestFocusInWindow();
        } catch (SQLException ex) {
            Logger.getLogger(FrmPembayaran1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }//GEN-LAST:event_formInternalFrameOpened

    private void txtJenisBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisBayarKeyPressed
        // TODO add your handling code here:
}//GEN-LAST:event_txtJenisBayarKeyPressed

    private void txtJenisBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJenisBayarKeyReleased
        try {
            String sCari = txtJenisBayar.getText();
            switch(evt.getKeyCode()) {
                
                case java.awt.event.KeyEvent.VK_ENTER : {
                    if (lst.isVisible()){
                        Object[] obj = lst.getOResult();
                        if (obj.length > 0) {
                            txtJenisBayar.setText(obj[0].toString());
                            lblJenisBayar.setText(obj[1].toString());
                            //lblAlamatToko.setText(obj[2].toString());
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
//                        if(txtSupplier.getText().trim().equalsIgnoreCase("")){
//                            JOptionPane.showMessageDialog(this, "Silakan isi supplier terlebih dulu!");
//                            txtSupplier.requestFocus();
//                            return;
//                        }   
                        
                        String sQry="select coalesce(kode_jenis, '') as kode, coalesce(jenis_pembayaran,'') as Jenis_pembayaran " +
                                "from jenis_pembayaran where (coalesce(kode_jenis, '')||coalesce(jenis_pembayaran,'')) iLike '%"+sCari+"%' order by 2";
                        
                        System.out.println(sQry);
                        lst.setSQuery(sQry);
                        
                        lst.setBounds(this.getX()+this.jPanel1.getX()+ this.txtJenisBayar.getX()+Utama.iLeft+17, 
                                this.getY()+this.jPanel1.getY()+this.txtJenisBayar.getY() + txtJenisBayar.getHeight()+Utama.iTop+77, 
                                txtJenisBayar.getWidth()+lblJenisBayar.getWidth()+5,
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtJenisBayar);
                        lst.setLblDes(new javax.swing.JLabel[]{lblJenisBayar});
                        lst.setColWidth(0, txtJenisBayar.getWidth());
                        lst.setColWidth(1, lblJenisBayar.getWidth());
                        if(lst.getIRowCount()>0){
                            lst.setVisible(true);
                            requestFocusInWindow();
                            txtJenisBayar.requestFocus();
                        } else{
                            txtJenisBayar.setText("");
                            lblJenisBayar.setText("");
                            lst.setVisible(false);
                            txtJenisBayar.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
}//GEN-LAST:event_txtJenisBayarKeyReleased

    private void btnTambahItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahItemActionPerformed
        udfBayar();
}//GEN-LAST:event_btnTambahItemActionPerformed

    private void tblItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemMouseClicked
        if(evt.getClickCount()==2){
            udfBayar();
        }
    }//GEN-LAST:event_tblItemMouseClicked

    private void txtNoFormKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoFormKeyReleased
        // TODO add your handling code here:
}//GEN-LAST:event_txtNoFormKeyReleased

    private void txtNoCekFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoCekFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtNoCekFocusLost

    private void txtNoCekKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoCekKeyReleased
        jCalCek.setVisible(txtNoCek.getText().length()>0? true: false); 
}//GEN-LAST:event_txtNoCekKeyReleased

    private void txtTokoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokoFocusLost
        if(!lblToko.getText().equalsIgnoreCase("")){
            udfLoadTagihan();
        }
    }//GEN-LAST:event_txtTokoFocusLost

    private void txtTokoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTokoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTokoKeyPressed

    private String getQuery(){
        //String sQry;
        if(radioToko.isSelected()){
            sQry="select c.kode_cust as kode,  coalesce(c.nama,'') as toko, " +
                 "coalesce(c.nama,'') ||' ('|| gabung(merk.merk)||'), ' ||coalesce(nama_kota,'') as kepada " +
                 "from customer c inner join merk on c.kode_cust=merk.kode_cust " +
                 "left join kota on kota.kode_kota=c.kota " +
                 "where (c.kode_cust|| coalesce(c.nama,'')||coalesce(merk.merk,'')||coalesce(nama_kota,'')) iLike '%"+txtToko.getText()+"%' " +
                 "group by c.kode_cust, coalesce(c.nama,''), coalesce(nama_kota,'') " +
                 "order by coalesce(c.nama,'')";
                    
            return sQry;
        }
        
        if(radioPengirim.isSelected()){
            sQry=   "select kode_toko, coalesce(nama,'') as nama, coalesce(nama,'') as kepada " +
                    "from toko where (coalesce(nama,'')||kode_toko) iLike '%"+txtToko.getText()+"%' order by coalesce(nama,'') ";
            
            return sQry;
        }
        
        return sQry;
    }
    
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
                        
                        lst.setBounds(Utama.iLeft+ this.getX()+this.jPanel2.getX()+ this.txtToko.getX()+17,
                                Utama.iTop + this.getY() + this.jPanel2.getY()+this.txtToko.getY() + txtToko.getHeight()+77,
                                txtToko.getWidth()+lblToko.getWidth(),
                                150);
                        lst.setFocusableWindowState(false);
                        lst.setTxtCari(txtToko);
                        lst.setLblDes(new javax.swing.JLabel[]{lblToko});
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
                            txtToko.requestFocus();
                        }
                    }
                    break;
                }
            }
        } catch (SQLException se) {System.out.println(se.getMessage());}
    }//GEN-LAST:event_txtTokoKeyReleased

    private void radioTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioTokoActionPerformed
        udfClear();
    }//GEN-LAST:event_radioTokoActionPerformed

    private void radioPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioPengirimActionPerformed
        udfClear();
    }//GEN-LAST:event_radioPengirimActionPerformed

    private void tblItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblItemKeyTyped
        cEditor.setComponentFocus();
    }//GEN-LAST:event_tblItemKeyTyped

    private void btnTambahItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnTambahItem1ActionPerformed

    private void udfClear() {
        if(modelNota.getRowCount()>0 && !isSave){
            if(JOptionPane.showConfirmDialog(this, "Data akan dihilangkan senelum disimpan?", "Konfirmasi", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
            return;
            }
        }  
        
        modelNota.setNumRows(0);
        tblItem.setModel(modelNota);

        txtToko.setText(""); lblToko.setText("");
        lblTotalNota.setText("0");  lblTotalTerbayar.setText("0");  lblTotalTerutang.setText("0");
        lblTotalBayar.setText("0"); lblTotalKlem.setText("0");      lblGrandTotal.setText("0");

        txtJenisBayar.setText("");lblJenisBayar.setText("");
        txtToko.requestFocus();
        txtNoForm.setText("");
        
        isSave=false;
    }
    
    private void pesanError(String Err){
        JOptionPane.showMessageDialog(this,Err,"Message",JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean CekBeforeSave(){
        boolean bSt=true;
//        if(txtMerk.getText().trim().equalsIgnoreCase("")){
//            pesanError("Silahkan isi Supplier Id Terlebih Dahulu!");
//            txtMerk.requestFocus();
//            bSt=false;
//            return false;
//        }else 
//        if(txtNo_PO.getText().trim().equalsIgnoreCase("")){
//            pesanError("Silahkan isi NO PO. Terlebih Dahulu!");
//            txtNo_PO.requestFocus();
//            bSt=false;
//        }
//        if(txtGudang.getText().trim().equalsIgnoreCase("")){
//            pesanError("Silahkan isi Gudang Terlebih Dahulu!");
//            txtGudang.requestFocus();
//            bSt=false;
//            return false;
//        }else 
//        if(txtNoDO.getText().trim().equalsIgnoreCase("")){
//            pesanError("Silahkan isi NO DO/SJ. Terlebih Dahulu!");
//            txtNoDO.requestFocus();
//            bSt=false;
//            return false;
//        }
       return bSt;
     }
    
    private void udfUpdateData(){
//        Phar_TarifBarangBean iTrBean =new Phar_TarifBarangBean();
//        if (getBEdit()) {
//            if (CekBeforeSave()){
//                if (getBNew()) { 
//                    SaveData();
//                    okSave=true;
//                }else{
//                    KoreksiPR();
//                    setBEdit(false);
//                    setBNew(false);
//                    setUpBtn();
//                }
//            }
//        }
    }    
    
     private void setMouseList(){
        //jPanel5.addMouseListener(miceListener);
        //txtNo_PO.addMouseListener(miceListener);
//        txtGudang.addMouseListener(miceListener);
//        txtNoDO.addMouseListener(miceListener);
//        txtRemark.addMouseListener(miceListener);
//        txtPPNperItem.addMouseListener(miceListener);
//        txtSupplier.addMouseListener(miceListener);
//        txtDiscPerItem.addMouseListener(miceListener);
//        txtKodeBarang.addMouseListener(miceListener);
//        ftDateExpired.addMouseListener(miceListener);
//        ftQty.addMouseListener(miceListener);
//        ftPrice.addMouseListener(miceListener);
        //jFDatePO.addMouseListener(miceListener);
   }
    
   private void setFocus(){
//        txtKodeBarang.addFocusListener(txtFoculListener);
//        txtGudang.addFocusListener(txtFoculListener);
//        //txtNo_PO.addFocusListener(txtFoculListener);
//        txtRemark.addFocusListener(txtFoculListener);
//        txtPPNperItem.addFocusListener(txtFoculListener);
//        txtSupplier.addFocusListener(txtFoculListener);
//        txtDiscPerItem.addFocusListener(txtFoculListener);
//        txtNoDO.addFocusListener(txtFoculListener);
//        ftDateExpired.addFocusListener(txtFoculListener);
//        ftQty.addFocusListener(txtFoculListener);
//        ftPrice.addFocusListener(txtFoculListener);
//        txtPengirim.addFocusListener(txtFoculListener);
//        txtNoBatch.addFocusListener(txtFoculListener);
   }
    
   private MouseListener miceListener=new MouseListener() {
        Color g1 = new Color(255,255,0);
        Color g2 = new Color(255,255,255);  
        public void mouseClicked(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(g1);
        }
        public void mouseExited(MouseEvent e) {
            Component c=(Component) e.getSource();
            c.setBackground(g2);
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
   };
        
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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTambahItem;
    private javax.swing.JButton btnTambahItem1;
    private javax.swing.ButtonGroup buttonGroup1;
    private org.jdesktop.swingx.JXDatePicker jCalBayar;
    private org.jdesktop.swingx.JXDatePicker jCalCek;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGrandTotal;
    private javax.swing.JLabel lblJenisBayar;
    private javax.swing.JLabel lblToko;
    private javax.swing.JLabel lblTotalBayar;
    private javax.swing.JLabel lblTotalKlem;
    private javax.swing.JLabel lblTotalNota;
    private javax.swing.JLabel lblTotalTerbayar;
    private javax.swing.JLabel lblTotalTerutang;
    private javax.swing.JRadioButton radioPengirim;
    private javax.swing.JRadioButton radioToko;
    private javax.swing.JTable tblItem;
    private javax.swing.JTextField txtJenisBayar;
    private javax.swing.JTextArea txtMemo;
    private javax.swing.JTextField txtNoCek;
    private javax.swing.JTextField txtNoForm;
    private javax.swing.JTextField txtToko;
    // End of variables declaration//GEN-END:variables
    
    Color g1 = new Color(153,255,255);
    Color g2 = new Color(255,255,255); 
    
    Color fHitam = new Color(0,0,0);
    Color fPutih = new Color(255,255,255); 
    
    Color crtHitam =new java.awt.Color(0, 0, 0);
    Color crtPutih = new java.awt.Color(255, 255, 255); 
    
}

