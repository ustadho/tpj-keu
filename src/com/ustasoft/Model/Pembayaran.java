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
public class Pembayaran {

    public String getAlat_bayar() {
        return alat_bayar;
    }

    public void setAlat_bayar(String alat_bayar) {
        this.alat_bayar = alat_bayar;
    }

    public double getCheck_amount() {
        return check_amount;
    }

    public void setCheck_amount(double check_amount) {
        this.check_amount = check_amount;
    }

    public String getCheck_no() {
        return check_no;
    }

    public void setCheck_no(String check_no) {
        this.check_no = check_no;
    }

    public Date getDate_trx() {
        return date_trx;
    }

    public void setDate_trx(Date date_trx) {
        this.date_trx = date_trx;
    }

    public boolean isFiscal_payment() {
        return fiscal_payment;
    }

    public void setFiscal_payment(boolean fiscal_payment) {
        this.fiscal_payment = fiscal_payment;
    }

    public boolean isIs_lunas() {
        return is_lunas;
    }

    public void setIs_lunas(boolean is_lunas) {
        this.is_lunas = is_lunas;
    }

    public String getKode_cust() {
        return kode_cust;
    }

    public void setKode_cust(String kode_cust) {
        this.kode_cust = kode_cust;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNo_bayar() {
        return no_bayar;
    }

    public void setNo_bayar(String no_bayar) {
        this.no_bayar = no_bayar;
    }

    public String getPay_for() {
        return pay_for;
    }

    public void setPay_for(String pay_for) {
        this.pay_for = pay_for;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Date getTgl_cek() {
        return tgl_cek;
    }

    public void setTgl_cek(Date tgl_cek) {
        this.tgl_cek = tgl_cek;
    }

    public Date getTgl_lunas() {
        return tgl_lunas;
    }

    public void setTgl_lunas(Date tgl_lunas) {
        this.tgl_lunas = tgl_lunas;
    }

    public boolean isTo_tujuan() {
        return to_tujuan;
    }

    public void setTo_tujuan(boolean to_tujuan) {
        this.to_tujuan = to_tujuan;
    }

    public String getUser_tr() {
        return user_tr;
    }

    public void setUser_tr(String user_tr) {
        this.user_tr = user_tr;
    }

    public boolean isVoid_check() {
        return void_check;
    }

    public void setVoid_check(boolean void_check) {
        this.void_check = void_check;
    }
    String no_bayar;
        Date tanggal;
        String kode_cust;
        String pay_for;
        String alat_bayar;
        double rate;
        String check_no;
        Date tgl_cek;
        String memo;
        double check_amount;
        boolean void_check;
        boolean fiscal_payment;
        boolean is_lunas;
        Date tgl_lunas;
        String user_tr;
        String keteranganNota;

    public String getKeteranganNota() {
        return keteranganNota;
    }

    public void setKeteranganNota(String keteranganNota) {
        this.keteranganNota = keteranganNota;
    }
        Date date_trx;
        boolean to_tujuan;

}
