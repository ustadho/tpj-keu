/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ustasoft.services;

import com.ustasoft.Model.Pembayaran;
import com.ustasoft.Model.PembayaranDetail;
import com.ustasoft.Model.PembayaranDetailDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import tpjkeuangan.Utama;

/**
 *
 * @author cak-ust
 */
public class BayarNotaServices {
    private Connection conn;

    public BayarNotaServices(Connection con){
        this.conn=con;
    }

    public String getNoBayar(Date date) throws SQLException {
        String sNoBayar="";
        ResultSet rs=conn.createStatement().executeQuery(
                "select fn_nota_get_no_pembayaran('"+date+"')");
        if(rs.next()){
            sNoBayar=rs.getString(1);
        }
        rs.close();
        return sNoBayar;

    }

    public double telahBayarSelainKoreksi(String sNota, String sNoBayar) throws SQLException{
        double tBayar=0;
        String sQry="select fn_nota_pembayaran_exclude_bayar('"+sNota+"', '"+sNoBayar+"')";
        System.out.println(sQry);
        ResultSet rs=conn.createStatement().executeQuery(sQry);
        if(rs.next()){
            tBayar=rs.getDouble(1);
        }
        return tBayar;
    }
    
    public int savePembayaran(Pembayaran bayar) throws SQLException{
        String sIns="INSERT INTO nota_pembayaran("
                + "no_bayar, tanggal, kode_cust, pay_for, alat_bayar, rate, check_no, "
                + "tgl_cek, memo, check_amount, void_check, fiscal_payment, is_lunas, "
                + "tgl_lunas, user_tr, date_trx, to_tujuan) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?);";
        PreparedStatement ps=conn.prepareStatement(sIns);
        ps.setString(1, bayar.getNo_bayar());
        ps.setDate(2, new java.sql.Date(bayar.getTanggal().getTime()));
        ps.setString(3, bayar.getKode_cust());
        ps.setString(4, bayar.getPay_for());
        ps.setString(5, bayar.getAlat_bayar());
        ps.setDouble(6, bayar.getRate());
        ps.setString(7, bayar.getCheck_no());
        ps.setDate(8, bayar.getTgl_cek()==null? null: new java.sql.Date(bayar.getTgl_cek().getTime()));
        ps.setString(9, bayar.getMemo());
        ps.setDouble(10, bayar.getCheck_amount());
        ps.setBoolean(11, bayar.isVoid_check());
        ps.setBoolean(12, bayar.isFiscal_payment());
        ps.setBoolean(13, bayar.isIs_lunas());
        ps.setDate(14, bayar.getTgl_lunas()==null? null: new java.sql.Date(bayar.getTgl_lunas().getTime()));
        ps.setString(15, bayar.getUser_tr());
        ps.setDate(16, new java.sql.Date(bayar.getDate_trx().getTime()));
        ps.setBoolean(17, bayar.isTo_tujuan());

        int i =ps.executeUpdate();
        ps.close();
        return i;
    }

    public int[] savePembayaranDetail(List<PembayaranDetail> detail) throws SQLException{
        int i[]={};
        PreparedStatement ps=conn.prepareStatement(
                "INSERT INTO nota_pembayaran_detail("
                + "no_bayar, no_nota, jumlah, diskon, pajak, denda, klem, is_batal, "
                + "user_ins, date_ins, user_batal, date_batal) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,  "
                + "?, ?, ?, ?);");


        for(int r=0; r<detail.size(); r++){
            ps.setString(1, detail.get(r).getNo_bayar());
            ps.setString(2, detail.get(r).getNo_nota());
            ps.setDouble(3, detail.get(r).getJumlah());
            ps.setDouble(4, detail.get(r).getDiskon());
            ps.setDouble(5, detail.get(r).getPajak());
            ps.setDouble(6, detail.get(r).getDenda());
            ps.setDouble(7, detail.get(r).getKlem());
            ps.setBoolean(8, detail.get(r).isIs_batal());
            ps.setString(9, detail.get(r).getUser_ins());
            ps.setDate(10, detail.get(r).getDate_ins()==null? null: new java.sql.Date(detail.get(r).getDate_ins().getTime()));
            ps.setString(11, detail.get(r).getUser_batal());
            ps.setDate(12, detail.get(r).getDate_batal()==null? null: new java.sql.Date(detail.get(r).getDate_batal().getTime()));
            ps.addBatch();
        }
        i=ps.executeBatch();
        return i;
    }

    public int[] savePembayaranDetailDetail(List<PembayaranDetailDetail> detail) throws SQLException{
        int i[]={};
        PreparedStatement ps=conn.prepareStatement(
                "INSERT INTO nota_pembayaran_detail_detail("
                + "no_bayar, no_nota, no_nota_det, jumlah) "
                + "VALUES (?, ?, ?, ?);");


        for(int r=0; r<detail.size(); r++){
            ps.setString(1, detail.get(r).getNoBayar());
            ps.setString(2, detail.get(r).getNoNota());
            ps.setString(3, detail.get(r).getNoNotaDet());
            ps.setDouble(4, detail.get(r).getBayar());
            ps.addBatch();
        }
        i=ps.executeBatch();
        return i;
    }
    
    public void simpanLebih(String sNota, String sNoBayar, double lebih) throws SQLException{
        String ins="delete from nota_pembayaran_lebih where no_nota='"+sNota+"'; ";
        if(lebih>0)
            ins+="insert into nota_pembayaran_lebih(no_nota, no_bayar, lebih) "
                + "values('"+sNota+"', '"+sNoBayar+"', "+lebih+"); ";
        int i=conn.createStatement().executeUpdate(ins);
        System.out.println(ins);
    }
    
    public void printTagihanOutstanding(){
        
    }
    
    public void printKartuTagihan(String tgl1, String tgl2, String sCust, String sNamaCust, String sKota, 
            String tagihanPer){
        try {
            HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                reportParam.put("tgl_berangkat1", tgl1);
                reportParam.put("tgl_berangkat2", tgl2);
                reportParam.put("kode_cust", sCust);
                reportParam.put("customer", sNamaCust);
                reportParam.put("kota", sKota);
                reportParam.put("flag_nota", tagihanPer);
                jasperReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/tpjkeuangan/Reports/kartu_tagihan_per_tgl_berangkat_v2.jasper"));
                JasperPrint print = JasperFillManager.fillReport(
                        jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.getOrientation());
                JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            System.out.println("Error :"+ ex.getMessage());
        }
            
    }
    
    public void printTagihan(String tgl1, String tgl2, String sCust, String sNamaCust, String sKota, 
            String tagihanPer){
        try {
            HashMap reportParam = new HashMap();
                JasperReport jasperReport = null;
                reportParam.put("tgl_berangkat1", tgl1);
                reportParam.put("tgl_berangkat2", tgl2);
                reportParam.put("kode_cust", sCust);
                reportParam.put("customer", sNamaCust);
                reportParam.put("kota", sKota);
                reportParam.put("flag_nota", tagihanPer);
                jasperReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/tpjkeuangan/Reports/kartu_tagihan_per_tgl_berangkat_v2.jasper"));
                JasperPrint print = JasperFillManager.fillReport(
                        jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.getOrientation());
                JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            System.out.println("Error :"+ ex.getMessage());
        }
            
    }
    
    public void printRekapTagihan(String sKodeCust, String sNamaCust, 
            String tagihanPer, String tahun, String kodeKota, String namaKota, String sMerk){
        try {
            HashMap reportParam = new HashMap();
            
                JasperReport jasperReport = null;
                reportParam.put("kodeCust", sKodeCust);
                reportParam.put("namaCust", sNamaCust);
                reportParam.put("tagihanPer", tagihanPer);
                reportParam.put("customer", sNamaCust);
                reportParam.put("tahun", tahun);
                reportParam.put("kodeKota", kodeKota);
                reportParam.put("namaKota", namaKota);
                reportParam.put("merk", sMerk);
                jasperReport = (JasperReport) JRLoader.loadObject(
                        getClass().getResourceAsStream("/tpjkeuangan/Reports/NotaRekapCustKota2.jasper"));
                JasperPrint print = JasperFillManager.fillReport(
                        jasperReport, reportParam, conn);
                print.setOrientation(jasperReport.getOrientation());
                JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            System.out.println("Error :"+ ex.getMessage());
        }
    }

    public String koreksiPembayaran(String sNoBayarKoreksi) throws SQLException {
        
        String sHasil=""; 
        ResultSet rs=conn.createStatement().executeQuery(
                "select fn_nota_pembayaran_koreksi('"+sNoBayarKoreksi+"', '"+Utama.sUserName+"')");
        if(rs.next()){
            sHasil=rs.getString(1);
        }
        return sHasil;
    }
    
    
}
