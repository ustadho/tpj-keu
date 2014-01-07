/*
 * Generated by JasperReports - 8/29/09 3:17 PM
 */
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.*;

import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import java.net.*;

import net.sf.jasperreports.engine.*;
import java.util.*;
import net.sf.jasperreports.engine.data.*;


/**
 *
 */
public class rekap_per_kapal_periodik_1251533844046_587084 extends JREvaluator
{


    /**
     *
     */
    private JRFillParameter parameter_REPORT_LOCALE = null;
    private JRFillParameter parameter_REPORT_TIME_ZONE = null;
    private JRFillParameter parameter_REPORT_VIRTUALIZER = null;
    private JRFillParameter parameter_REPORT_FILE_RESOLVER = null;
    private JRFillParameter parameter_REPORT_SCRIPTLET = null;
    private JRFillParameter parameter_REPORT_PARAMETERS_MAP = null;
    private JRFillParameter parameter_REPORT_CONNECTION = null;
    private JRFillParameter parameter_REPORT_CLASS_LOADER = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_REPORT_URL_HANDLER_FACTORY = null;
    private JRFillParameter parameter_IS_IGNORE_PAGINATION = null;
    private JRFillParameter parameter_REPORT_FORMAT_FACTORY = null;
    private JRFillParameter parameter_REPORT_MAX_COUNT = null;
    private JRFillParameter parameter_REPORT_TEMPLATES = null;
    private JRFillParameter parameter_tanggal1 = null;
    private JRFillParameter parameter_tanggal2 = null;
    private JRFillParameter parameter_kota_tujuan = null;
    private JRFillParameter parameter_REPORT_RESOURCE_BUNDLE = null;
    private JRFillField field_tgl_berangkat = null;
    private JRFillField field_jml_klem = null;
    private JRFillField field_sisa = null;
    private JRFillField field_kapal = null;
    private JRFillField field_total_nota = null;
    private JRFillField field_nama_kota = null;
    private JRFillField field_kondisi = null;
    private JRFillField field_jml_bayar = null;
    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;
    private JRFillVariable variable_kota_COUNT = null;
    private JRFillVariable variable_nomor = null;
    private JRFillVariable variable_total_nota1 = null;
    private JRFillVariable variable_total_terbayar1 = null;
    private JRFillVariable variable_total_klem1 = null;
    private JRFillVariable variable_total_sisa1 = null;
    private JRFillVariable variable_total_nota2 = null;
    private JRFillVariable variable_total_bayar2 = null;
    private JRFillVariable variable_total_klem2 = null;
    private JRFillVariable variable_total_sisa2 = null;


    /**
     *
     */
    public void customizedInit(
        Map pm,
        Map fm,
        Map vm
        )
    {
        initParams(pm);
        initFields(fm);
        initVars(vm);
    }


    /**
     *
     */
    private void initParams(Map pm)
    {
        parameter_REPORT_LOCALE = (JRFillParameter)pm.get("REPORT_LOCALE");
        parameter_REPORT_TIME_ZONE = (JRFillParameter)pm.get("REPORT_TIME_ZONE");
        parameter_REPORT_VIRTUALIZER = (JRFillParameter)pm.get("REPORT_VIRTUALIZER");
        parameter_REPORT_FILE_RESOLVER = (JRFillParameter)pm.get("REPORT_FILE_RESOLVER");
        parameter_REPORT_SCRIPTLET = (JRFillParameter)pm.get("REPORT_SCRIPTLET");
        parameter_REPORT_PARAMETERS_MAP = (JRFillParameter)pm.get("REPORT_PARAMETERS_MAP");
        parameter_REPORT_CONNECTION = (JRFillParameter)pm.get("REPORT_CONNECTION");
        parameter_REPORT_CLASS_LOADER = (JRFillParameter)pm.get("REPORT_CLASS_LOADER");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)pm.get("REPORT_DATA_SOURCE");
        parameter_REPORT_URL_HANDLER_FACTORY = (JRFillParameter)pm.get("REPORT_URL_HANDLER_FACTORY");
        parameter_IS_IGNORE_PAGINATION = (JRFillParameter)pm.get("IS_IGNORE_PAGINATION");
        parameter_REPORT_FORMAT_FACTORY = (JRFillParameter)pm.get("REPORT_FORMAT_FACTORY");
        parameter_REPORT_MAX_COUNT = (JRFillParameter)pm.get("REPORT_MAX_COUNT");
        parameter_REPORT_TEMPLATES = (JRFillParameter)pm.get("REPORT_TEMPLATES");
        parameter_tanggal1 = (JRFillParameter)pm.get("tanggal1");
        parameter_tanggal2 = (JRFillParameter)pm.get("tanggal2");
        parameter_kota_tujuan = (JRFillParameter)pm.get("kota_tujuan");
        parameter_REPORT_RESOURCE_BUNDLE = (JRFillParameter)pm.get("REPORT_RESOURCE_BUNDLE");
    }


    /**
     *
     */
    private void initFields(Map fm)
    {
        field_tgl_berangkat = (JRFillField)fm.get("tgl_berangkat");
        field_jml_klem = (JRFillField)fm.get("jml_klem");
        field_sisa = (JRFillField)fm.get("sisa");
        field_kapal = (JRFillField)fm.get("kapal");
        field_total_nota = (JRFillField)fm.get("total_nota");
        field_nama_kota = (JRFillField)fm.get("nama_kota");
        field_kondisi = (JRFillField)fm.get("kondisi");
        field_jml_bayar = (JRFillField)fm.get("jml_bayar");
    }


    /**
     *
     */
    private void initVars(Map vm)
    {
        variable_PAGE_NUMBER = (JRFillVariable)vm.get("PAGE_NUMBER");
        variable_COLUMN_NUMBER = (JRFillVariable)vm.get("COLUMN_NUMBER");
        variable_REPORT_COUNT = (JRFillVariable)vm.get("REPORT_COUNT");
        variable_PAGE_COUNT = (JRFillVariable)vm.get("PAGE_COUNT");
        variable_COLUMN_COUNT = (JRFillVariable)vm.get("COLUMN_COUNT");
        variable_kota_COUNT = (JRFillVariable)vm.get("kota_COUNT");
        variable_nomor = (JRFillVariable)vm.get("nomor");
        variable_total_nota1 = (JRFillVariable)vm.get("total_nota1");
        variable_total_terbayar1 = (JRFillVariable)vm.get("total_terbayar1");
        variable_total_klem1 = (JRFillVariable)vm.get("total_klem1");
        variable_total_sisa1 = (JRFillVariable)vm.get("total_sisa1");
        variable_total_nota2 = (JRFillVariable)vm.get("total_nota2");
        variable_total_bayar2 = (JRFillVariable)vm.get("total_bayar2");
        variable_total_klem2 = (JRFillVariable)vm.get("total_klem2");
        variable_total_sisa2 = (JRFillVariable)vm.get("total_sisa2");
    }


    /**
     *
     */
    public Object evaluate(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("MERAUKE");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa1.getValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem1.getValue()));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_terbayar1.getValue()));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota1.getValue()));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)("Sub Total : " +((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getValue()) + " of ");//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getValue()) + "");//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Tanggal : " +((java.lang.String)parameter_tanggal1.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal1.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal1.getValue()).substring(0,4)+" S/d "+((java.lang.String)parameter_tanggal2.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal2.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal2.getValue()).substring(0,4));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kondisi.getValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_nomor.getValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.util.Date)(((java.sql.Date)field_tgl_berangkat.getValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getValue()) + " of ");//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getValue()) + "");//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.util.Date)(new java.util.Date());//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa2.getValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem2.getValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_bayar2.getValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota2.getValue()));//$JR_EXPR_ID=44$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateOld(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("MERAUKE");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_kapal.getOldValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getOldValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getOldValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getOldValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getOldValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getOldValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getOldValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getOldValue()));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getOldValue()));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_nama_kota.getOldValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)field_nama_kota.getOldValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa1.getOldValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem1.getOldValue()));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_terbayar1.getOldValue()));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota1.getOldValue()));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)("Sub Total : " +((java.lang.String)field_nama_kota.getOldValue()));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()) + " of ");//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()) + "");//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Tanggal : " +((java.lang.String)parameter_tanggal1.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal1.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal1.getValue()).substring(0,4)+" S/d "+((java.lang.String)parameter_tanggal2.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal2.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal2.getValue()).substring(0,4));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kondisi.getOldValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getOldValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getOldValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getOldValue()));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getOldValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getOldValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_nomor.getOldValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.util.Date)(((java.sql.Date)field_tgl_berangkat.getOldValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()) + " of ");//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getOldValue()) + "");//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.util.Date)(new java.util.Date());//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa2.getOldValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem2.getOldValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_bayar2.getOldValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota2.getOldValue()));//$JR_EXPR_ID=44$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


    /**
     *
     */
    public Object evaluateEstimated(int id) throws Throwable
    {
        Object value = null;

        switch (id)
        {
            case 0 : 
            {
                value = (java.lang.String)("MERAUKE");//$JR_EXPR_ID=0$
                break;
            }
            case 1 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=1$
                break;
            }
            case 2 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=2$
                break;
            }
            case 3 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=3$
                break;
            }
            case 4 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=4$
                break;
            }
            case 5 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=5$
                break;
            }
            case 6 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=6$
                break;
            }
            case 7 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=7$
                break;
            }
            case 8 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=8$
                break;
            }
            case 9 : 
            {
                value = (java.lang.Integer)(new Integer(1));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.Integer)(new Integer(0));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.Object)(((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.String)(((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa1.getEstimatedValue()));//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem1.getEstimatedValue()));//$JR_EXPR_ID=23$
                break;
            }
            case 24 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_terbayar1.getEstimatedValue()));//$JR_EXPR_ID=24$
                break;
            }
            case 25 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota1.getEstimatedValue()));//$JR_EXPR_ID=25$
                break;
            }
            case 26 : 
            {
                value = (java.lang.String)("Sub Total : " +((java.lang.String)field_nama_kota.getValue()));//$JR_EXPR_ID=26$
                break;
            }
            case 27 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()) + " of ");//$JR_EXPR_ID=27$
                break;
            }
            case 28 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()) + "");//$JR_EXPR_ID=28$
                break;
            }
            case 29 : 
            {
                value = (java.lang.String)("Tanggal : " +((java.lang.String)parameter_tanggal1.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal1.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal1.getValue()).substring(0,4)+" S/d "+((java.lang.String)parameter_tanggal2.getValue()).substring(8,10)+ ((java.lang.String)parameter_tanggal2.getValue()).substring(4,7)+'-'+((java.lang.String)parameter_tanggal2.getValue()).substring(0,4));//$JR_EXPR_ID=29$
                break;
            }
            case 30 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kondisi.getValue()));//$JR_EXPR_ID=30$
                break;
            }
            case 31 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=31$
                break;
            }
            case 32 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_total_nota.getValue()));//$JR_EXPR_ID=32$
                break;
            }
            case 33 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_bayar.getValue()));//$JR_EXPR_ID=33$
                break;
            }
            case 34 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_jml_klem.getValue()));//$JR_EXPR_ID=34$
                break;
            }
            case 35 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sisa.getValue()));//$JR_EXPR_ID=35$
                break;
            }
            case 36 : 
            {
                value = (java.lang.Integer)(((java.lang.Integer)variable_nomor.getEstimatedValue()));//$JR_EXPR_ID=36$
                break;
            }
            case 37 : 
            {
                value = (java.util.Date)(((java.sql.Date)field_tgl_berangkat.getValue()));//$JR_EXPR_ID=37$
                break;
            }
            case 38 : 
            {
                value = (java.lang.String)("Page " + ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()) + " of ");//$JR_EXPR_ID=38$
                break;
            }
            case 39 : 
            {
                value = (java.lang.String)("" + ((java.lang.Integer)variable_PAGE_NUMBER.getEstimatedValue()) + "");//$JR_EXPR_ID=39$
                break;
            }
            case 40 : 
            {
                value = (java.util.Date)(new java.util.Date());//$JR_EXPR_ID=40$
                break;
            }
            case 41 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_sisa2.getEstimatedValue()));//$JR_EXPR_ID=41$
                break;
            }
            case 42 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_klem2.getEstimatedValue()));//$JR_EXPR_ID=42$
                break;
            }
            case 43 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_bayar2.getEstimatedValue()));//$JR_EXPR_ID=43$
                break;
            }
            case 44 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_total_nota2.getEstimatedValue()));//$JR_EXPR_ID=44$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
