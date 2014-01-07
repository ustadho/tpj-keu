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
public class PembayaranDetail {
    String no_bayar;
    String no_nota;
    double jumlah;
    double diskon;
    double pajak;
    double denda;
    double klem;
    boolean is_batal;
    String user_ins;
    Date date_ins;
    String user_batal;
    Date date_batal;

    public Date getDate_batal() {
        return date_batal;
    }

    public void setDate_batal(Date date_batal) {
        this.date_batal = date_batal;
    }

    public Date getDate_ins() {
        return date_ins;
    }

    public void setDate_ins(Date date_ins) {
        this.date_ins = date_ins;
    }

    public double getDenda() {
        return denda;
    }

    public void setDenda(double denda) {
        this.denda = denda;
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }

    public boolean isIs_batal() {
        return is_batal;
    }

    public void setIs_batal(boolean is_batal) {
        this.is_batal = is_batal;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public double getKlem() {
        return klem;
    }

    public void setKlem(double klem) {
        this.klem = klem;
    }

    public String getNo_bayar() {
        return no_bayar;
    }

    public void setNo_bayar(String no_bayar) {
        this.no_bayar = no_bayar;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(String no_nota) {
        this.no_nota = no_nota;
    }

    public double getPajak() {
        return pajak;
    }

    public void setPajak(double pajak) {
        this.pajak = pajak;
    }

    public String getUser_batal() {
        return user_batal;
    }

    public void setUser_batal(String user_batal) {
        this.user_batal = user_batal;
    }

    public String getUser_ins() {
        return user_ins;
    }

    public void setUser_ins(String user_ins) {
        this.user_ins = user_ins;
    }
}
