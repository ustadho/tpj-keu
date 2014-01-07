/*
 * Generated by JasperReports - 4/20/09 3:47 AM
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
public class nota_cetak_v5_1240174062718_27649 extends JREvaluator
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
    private JRFillParameter parameter_no_nota = null;
    private JRFillParameter parameter_REPORT_DATA_SOURCE = null;
    private JRFillParameter parameter_REPORT_URL_HANDLER_FACTORY = null;
    private JRFillParameter parameter_IS_IGNORE_PAGINATION = null;
    private JRFillParameter parameter_REPORT_FORMAT_FACTORY = null;
    private JRFillParameter parameter_REPORT_MAX_COUNT = null;
    private JRFillParameter parameter_REPORT_TEMPLATES = null;
    private JRFillParameter parameter_REPORT_RESOURCE_BUNDLE = null;
    private JRFillField field_biaya_satuan = null;
    private JRFillField field_terbilang = null;
    private JRFillField field_item_trx = null;
    private JRFillField field_keterangan_trx = null;
    private JRFillField field_sub_total = null;
    private JRFillField field_kapal = null;
    private JRFillField field_tgl_nota = null;
    private JRFillField field_satuan = null;
    private JRFillField field_kepada = null;
    private JRFillField field_qty = null;
    private JRFillField field_no_nota = null;
    private JRFillVariable variable_PAGE_NUMBER = null;
    private JRFillVariable variable_COLUMN_NUMBER = null;
    private JRFillVariable variable_REPORT_COUNT = null;
    private JRFillVariable variable_PAGE_COUNT = null;
    private JRFillVariable variable_COLUMN_COUNT = null;
    private JRFillVariable variable_grand_total = null;
    private JRFillVariable variable_x = null;
    private JRFillVariable variable_s_qty = null;


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
        parameter_no_nota = (JRFillParameter)pm.get("no_nota");
        parameter_REPORT_DATA_SOURCE = (JRFillParameter)pm.get("REPORT_DATA_SOURCE");
        parameter_REPORT_URL_HANDLER_FACTORY = (JRFillParameter)pm.get("REPORT_URL_HANDLER_FACTORY");
        parameter_IS_IGNORE_PAGINATION = (JRFillParameter)pm.get("IS_IGNORE_PAGINATION");
        parameter_REPORT_FORMAT_FACTORY = (JRFillParameter)pm.get("REPORT_FORMAT_FACTORY");
        parameter_REPORT_MAX_COUNT = (JRFillParameter)pm.get("REPORT_MAX_COUNT");
        parameter_REPORT_TEMPLATES = (JRFillParameter)pm.get("REPORT_TEMPLATES");
        parameter_REPORT_RESOURCE_BUNDLE = (JRFillParameter)pm.get("REPORT_RESOURCE_BUNDLE");
    }


    /**
     *
     */
    private void initFields(Map fm)
    {
        field_biaya_satuan = (JRFillField)fm.get("biaya_satuan");
        field_terbilang = (JRFillField)fm.get("terbilang");
        field_item_trx = (JRFillField)fm.get("item_trx");
        field_keterangan_trx = (JRFillField)fm.get("keterangan_trx");
        field_sub_total = (JRFillField)fm.get("sub_total");
        field_kapal = (JRFillField)fm.get("kapal");
        field_tgl_nota = (JRFillField)fm.get("tgl_nota");
        field_satuan = (JRFillField)fm.get("satuan");
        field_kepada = (JRFillField)fm.get("kepada");
        field_qty = (JRFillField)fm.get("qty");
        field_no_nota = (JRFillField)fm.get("no_nota");
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
        variable_grand_total = (JRFillVariable)vm.get("grand_total");
        variable_x = (JRFillVariable)vm.get("x");
        variable_s_qty = (JRFillVariable)vm.get("s_qty");
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
                value = (java.lang.String)("");//$JR_EXPR_ID=0$
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
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)(((java.lang.String)variable_s_qty.getValue()));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kepada.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)("NO. NOTA "+((java.lang.String)field_no_nota.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)field_item_trx.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_biaya_satuan.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)field_satuan.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)((((java.lang.Double)field_qty.getValue())==null||((java.lang.String)field_satuan.getValue())==null||((java.lang.String)field_satuan.getValue())==""||((java.lang.Double)field_biaya_satuan.getValue())==null? null:"X"));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.lang.Double)field_qty.getValue())==null? null:(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).substring(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).length()-4, new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).length()).equalsIgnoreCase(".000") && (((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Cntnr")||((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Dos")||((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Unit") ))? new DecimalFormat("#,##0;(#,##0)").format(((java.lang.Double)field_qty.getValue()))//$JR_EXPR_ID=19$
            :new DecimalFormat("#,##0.000;(#,##0.000)").format(((java.lang.Double)field_qty.getValue())));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.lang.String)field_keterangan_trx.getValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_grand_total.getValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)field_terbilang.getValue()).substring( 0, 1 ).toUpperCase()+//$JR_EXPR_ID=22$
((java.lang.String)field_terbilang.getValue()).substring(1, ((java.lang.String)field_terbilang.getValue()).length() ) +" rupiah,--");//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.lang.String)field_tgl_nota.getValue()));//$JR_EXPR_ID=23$
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
                value = (java.lang.String)("");//$JR_EXPR_ID=0$
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
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getOldValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)(((java.lang.String)variable_s_qty.getOldValue()));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kepada.getOldValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getOldValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)("NO. NOTA "+((java.lang.String)field_no_nota.getOldValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)field_item_trx.getOldValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getOldValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_biaya_satuan.getOldValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)field_satuan.getOldValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)((((java.lang.Double)field_qty.getOldValue())==null||((java.lang.String)field_satuan.getOldValue())==null||((java.lang.String)field_satuan.getOldValue())==""||((java.lang.Double)field_biaya_satuan.getOldValue())==null? null:"X"));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.lang.Double)field_qty.getOldValue())==null? null:(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getOldValue())).substring(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getOldValue())).length()-4, new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getOldValue())).length()).equalsIgnoreCase(".000") && (((java.lang.String)field_satuan.getOldValue()).equalsIgnoreCase("Cntnr")||((java.lang.String)field_satuan.getOldValue()).equalsIgnoreCase("Dos")||((java.lang.String)field_satuan.getOldValue()).equalsIgnoreCase("Unit") ))? new DecimalFormat("#,##0;(#,##0)").format(((java.lang.Double)field_qty.getOldValue()))//$JR_EXPR_ID=19$
            :new DecimalFormat("#,##0.000;(#,##0.000)").format(((java.lang.Double)field_qty.getOldValue())));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.lang.String)field_keterangan_trx.getOldValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_grand_total.getOldValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)field_terbilang.getOldValue()).substring( 0, 1 ).toUpperCase()+//$JR_EXPR_ID=22$
((java.lang.String)field_terbilang.getOldValue()).substring(1, ((java.lang.String)field_terbilang.getOldValue()).length() ) +" rupiah,--");//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.lang.String)field_tgl_nota.getOldValue()));//$JR_EXPR_ID=23$
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
                value = (java.lang.String)("");//$JR_EXPR_ID=0$
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
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getValue()));//$JR_EXPR_ID=9$
                break;
            }
            case 10 : 
            {
                value = (java.lang.String)(((java.lang.String)variable_s_qty.getEstimatedValue()));//$JR_EXPR_ID=10$
                break;
            }
            case 11 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kepada.getValue()));//$JR_EXPR_ID=11$
                break;
            }
            case 12 : 
            {
                value = (java.lang.String)(((java.lang.String)field_kapal.getValue()));//$JR_EXPR_ID=12$
                break;
            }
            case 13 : 
            {
                value = (java.lang.String)("NO. NOTA "+((java.lang.String)field_no_nota.getValue()));//$JR_EXPR_ID=13$
                break;
            }
            case 14 : 
            {
                value = (java.lang.String)(((java.lang.String)field_item_trx.getValue()));//$JR_EXPR_ID=14$
                break;
            }
            case 15 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_sub_total.getValue()));//$JR_EXPR_ID=15$
                break;
            }
            case 16 : 
            {
                value = (java.lang.Double)(((java.lang.Double)field_biaya_satuan.getValue()));//$JR_EXPR_ID=16$
                break;
            }
            case 17 : 
            {
                value = (java.lang.String)(((java.lang.String)field_satuan.getValue()));//$JR_EXPR_ID=17$
                break;
            }
            case 18 : 
            {
                value = (java.lang.String)((((java.lang.Double)field_qty.getValue())==null||((java.lang.String)field_satuan.getValue())==null||((java.lang.String)field_satuan.getValue())==""||((java.lang.Double)field_biaya_satuan.getValue())==null? null:"X"));//$JR_EXPR_ID=18$
                break;
            }
            case 19 : 
            {
                value = (java.lang.String)(((java.lang.Double)field_qty.getValue())==null? null:(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).substring(new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).length()-4, new DecimalFormat("#,###.000").format(((java.lang.Double)field_qty.getValue())).length()).equalsIgnoreCase(".000") && (((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Cntnr")||((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Dos")||((java.lang.String)field_satuan.getValue()).equalsIgnoreCase("Unit") ))? new DecimalFormat("#,##0;(#,##0)").format(((java.lang.Double)field_qty.getValue()))//$JR_EXPR_ID=19$
            :new DecimalFormat("#,##0.000;(#,##0.000)").format(((java.lang.Double)field_qty.getValue())));//$JR_EXPR_ID=19$
                break;
            }
            case 20 : 
            {
                value = (java.lang.String)(((java.lang.String)field_keterangan_trx.getValue()));//$JR_EXPR_ID=20$
                break;
            }
            case 21 : 
            {
                value = (java.lang.Double)(((java.lang.Double)variable_grand_total.getEstimatedValue()));//$JR_EXPR_ID=21$
                break;
            }
            case 22 : 
            {
                value = (java.lang.String)(((java.lang.String)field_terbilang.getValue()).substring( 0, 1 ).toUpperCase()+//$JR_EXPR_ID=22$
((java.lang.String)field_terbilang.getValue()).substring(1, ((java.lang.String)field_terbilang.getValue()).length() ) +" rupiah,--");//$JR_EXPR_ID=22$
                break;
            }
            case 23 : 
            {
                value = (java.lang.String)(((java.lang.String)field_tgl_nota.getValue()));//$JR_EXPR_ID=23$
                break;
            }
           default :
           {
           }
        }
        
        return value;
    }


}
