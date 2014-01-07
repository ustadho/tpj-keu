/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ustasoft.Model;

import java.util.Date;

/**
 *
 * @author cak-ust
 */
public class RekapTagihanCust {
    int no;
    String noNota="";
    String namaKapal="";
    String tglBerangkat;
    double jumlah;
    Date tglLunas;
    double pelunasan;
    double klaim;
    double lebih ;
    double lain2;
    double piutang;
    String keterangan;
    boolean isGabungan;
    String ketKlaim;
    String noPembayaran;
    String alatPembayaran;

    public boolean isIsGabungan() {
        return isGabungan;
    }

    public void setIsGabungan(boolean isGabungan) {
        this.isGabungan = isGabungan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getKetKlaim() {
        return ketKlaim;
    }

    public void setKetKlaim(String ketKlaim) {
        this.ketKlaim = ketKlaim;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getKlaim() {
        return klaim;
    }

    public void setKlaim(double klaim) {
        this.klaim = klaim;
    }

    public double getLain2() {
        return lain2;
    }

    public void setLain2(double lain2) {
        this.lain2 = lain2;
    }

    public double getLebih() {
        return lebih;
    }

    public void setLebih(double lebih) {
        this.lebih = lebih;
    }

    public String getNamaKapal() {
        return namaKapal;
    }

    public void setNamaKapal(String namaKapal) {
        this.namaKapal = namaKapal;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNoNota() {
        return noNota;
    }

    public void setNoNota(String noNota) {
        this.noNota = noNota;
    }

    public String getNoPembayaran() {
        return noPembayaran;
    }

    public void setNoPembayaran(String noPembayaran) {
        this.noPembayaran = noPembayaran;
    }

    public double getPelunasan() {
        return pelunasan;
    }

    public void setPelunasan(double pelunasan) {
        this.pelunasan = pelunasan;
    }

    public double getPiutang() {
        return piutang;
    }

    public void setPiutang(double piutang) {
        this.piutang = piutang;
    }

    public String getTglBerangkat() {
        return tglBerangkat;
    }

    public void setTglBerangkat(String tglBerangkat) {
        this.tglBerangkat = tglBerangkat;
    }

    public Date getTglLunas() {
        return tglLunas;
    }

    public void setTglLunas(Date tglLunas) {
        this.tglLunas = tglLunas;
    }

    public void setAlatPembayaran(String string) {
        this.alatPembayaran=string;
    }

    public String getAlatPembayaran() {
        return alatPembayaran;
    }

    
}
