/*
 * NewJFrame.java
 *
 * Created on April 13, 2008, 10:21 PM
 */
package tpjkeuangan;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * load
 *
 * @author oestadho
 */
public class Utama extends javax.swing.JFrame {
    static boolean isRptPackingList;
    static boolean isListTagihanOn;
    static boolean isPckPerMerk;
    static boolean isRptTagihan;
    static boolean isDetailBayarNota = false;
    static boolean isKapalBerangkat;
    static boolean isRptKartuTagihan;
    static boolean isJenisBayarOn;
    public static String sUserName = "";
    private Connection conn;
    static boolean isNotaOn;
    static boolean isCustOn;
    static boolean isTokoOn;
    static boolean isTagihanOn;
    static boolean isBayarOn;
    static int iLeft, iTop;
    FrmListTagihan fListTagihan = new FrmListTagihan();
    FrmToko fToko = new FrmToko();
    static boolean isKeteranganOn;
    static String sKodeKota;
    static String sKotaSingkat;
    static String sNamaKota;
    static boolean isKapalOn;
    static boolean isRptTagihanPerKapal;
    static boolean isNotaGabungOn;
    static boolean isRptLainOn;
    static boolean isListBayar;
    static boolean isRptLookup;

    /**
     * Creates new form NewJFrame
     */
    public Utama() {
        initComponents();

        udfAddActionTrx();
        udfAddActionDaftar();
        udfAddActionReport();
        udfAddActionSetting();

        changeUIdefaults();

    }

    void setKodeKota(String sKota) {
        sKodeKota = sKota;
    }

    void setNamaKota(String toString) {
        sNamaKota = toString;
        lblKotaTujuan.setText(toString);
    }

    private void udfAddActionTrx() {
        taskpane_trx.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Nota Tagihan");
                putValue(Action.SHORT_DESCRIPTION, "nota baru");
                putValue(Action.SMALL_ICON, Images.Book.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadNotaBaru();

            }
        });

        taskpane_trx.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Nota Gabungan");
                putValue(Action.SHORT_DESCRIPTION, "Nota gabungan");
                putValue(Action.SMALL_ICON, Images.Books.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadNotaGabungan();
            }
        });

//        taskpane_trx.add(new AbstractAction() {
//            {
//                putValue(Action.NAME, "Pembayaran Tagihan");
//                putValue(Action.SHORT_DESCRIPTION, "Pembayaran Tagihan");
//                putValue(Action.SMALL_ICON, Images.Pen.getIcon(16, 16));
//            }
//
//            public void actionPerformed(ActionEvent e) {
//                udfBayarTagihan();
//            }
//        });

    }

    private void udfAddActionDaftar() {
        taskpane_daftar.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Daftar Tagihan");
                putValue(Action.SHORT_DESCRIPTION, "Daftar Tagihan");
                putValue(Action.SMALL_ICON, Images.Order.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadListTagihan();
            }
        });

        taskpane_daftar.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Customer/ Toko Tujuan");
                putValue(Action.SHORT_DESCRIPTION, "Customer/ Toko Tujuan");
                putValue(Action.SMALL_ICON, Images.Molecule.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadMasterCustomer();
            }
        });

        taskpane_daftar.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Pengirim");
                putValue(Action.SHORT_DESCRIPTION, "Pengirim");
                putValue(Action.SMALL_ICON, Images.CubeClass.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadPengirim();
            }
        });
        taskpane_daftar.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Outstanding/Kota");
                putValue(Action.SHORT_DESCRIPTION, "Outstanding Tagihan per Kota");
                putValue(Action.SMALL_ICON, Images.CubeClass.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                mnuDaftarOutstandingPerKotaActionPerformed(e);
            }
        });
        taskpane_daftar.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Outstanding/Kapal");
                putValue(Action.SHORT_DESCRIPTION, "Outstanding Tagihan per Kapal");
                putValue(Action.SMALL_ICON, Images.CubeClass.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                mnuDaftarOutstandingPerKapalActionPerformed(e);
            }
        });

    }

    private void udfAddActionReport() {
        taskpane_report.add(new AbstractAction() {
            {
                putValue(Action.NAME, "laporan per Kapal");
                putValue(Action.SHORT_DESCRIPTION, "Laporan per Kapal & tanggal berangkat");
                putValue(Action.SMALL_ICON, Images.Diagram.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadRptTagihanPerKapal();
            }
        });
        taskpane_report.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Laporan per customer");
                putValue(Action.SHORT_DESCRIPTION, "Laporan per customer");
                putValue(Action.SMALL_ICON, Images.Order.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadRptPerCustomer();
            }
        });

        taskpane_report.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Laporan lain-lain");
                putValue(Action.SHORT_DESCRIPTION, "Laporan lainnya");
                putValue(Action.SMALL_ICON, Images.New_file.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadRptLain();
            }
        });


    }

    private void udfAddActionSetting() {
        taskpane_setting.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Kapal ~ Kota tujuan");
                putValue(Action.SHORT_DESCRIPTION, "Master kapal, kota tujuan & Tgl. Berangkat untuk Nota");
                putValue(Action.SMALL_ICON, Images.Setting.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadKapal();
            }
        });

        taskpane_setting.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Keterangan nota");
                putValue(Action.SHORT_DESCRIPTION, "Daftar Tagihan");
                putValue(Action.SMALL_ICON, Images.Setting.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                udfLoadKeteranganNota();
            }
        });

        taskpane_setting.add(new AbstractAction() {
            {
                putValue(Action.NAME, "Item Nota");
                putValue(Action.SHORT_DESCRIPTION, "Maintenance Item Nota");
                putValue(Action.SMALL_ICON, Images.Setting.getIcon(16, 16));
            }

            public void actionPerformed(ActionEvent e) {
                mnuItemNotaMaintenanceActionPerformed1(null);
            }
        });

    }

    private void changeUIdefaults() {
        // JXTaskPaneContainer settings (developer defaults)
  /* These are all the properties that can be set (may change with new version of SwingX)
         "TaskPaneContainer.useGradient",
         "TaskPaneContainer.background",
         "TaskPaneContainer.backgroundGradientStart",
         "TaskPaneContainer.backgroundGradientEnd",
         etc.
         */

        // setting taskpanecontainer defaults
        UIManager.put("TaskPaneContainer.useGradient", Boolean.FALSE);
        UIManager.put("TaskPaneContainer.background", Colors.LightGray.color(0.5f));

        // setting taskpane defaults
        UIManager.put("TaskPane.font", new FontUIResource(new Font("Verdana", Font.PLAIN, 16)));
        UIManager.put("TaskPane.titleBackgroundGradientStart", Colors.White.color());
        UIManager.put("TaskPane.titleBackgroundGradientEnd", Colors.LightBlue.color());


    }

    public void setConn(Connection con) {
        conn = con;
    }

    public void setUserName(String s) {
        sUserName = s;
        lblUserName.setText(s);
    }

    private void udfLoadKoreksiNota() {
        FrmNota fNota = new FrmNota();

        fNota = new FrmNota();
        fNota.setConn(conn);
        fNota.setUserName(sUserName);
        fNota.setKoreksi(true);
        fNota.setVisible(true);
        fNota.setTitle("Koreksi nota tagihan (Non Gabungan)");

        jDesktopPane1.add(fNota, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            fNota.setMaximum(true);
            fNota.setSelected(true);
        } catch (PropertyVetoException PO) {
            System.err.println(PO.getMessage());
        }

    }

    private void udfLoadKoreksiNotaGabungan() {
        FrmNotaGabung fNotaGabung = new FrmNotaGabung();
        fNotaGabung.setConn(conn);
        fNotaGabung.setVisible(true);
        fNotaGabung.setUserName(sUserName);
        fNotaGabung.setTitle("Koreksi nota tagihan (Non Gabungan)");
        fNotaGabung.udfClearAll();
        fNotaGabung.setIsKoreksi(true);
        fNotaGabung.setBounds(0, 0, fNotaGabung.getWidth(), fNotaGabung.getWidth());
        jDesktopPane1.add(fNotaGabung, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            fNotaGabung.setMaximum(true);
            fNotaGabung.setSelected(true);
        } catch (PropertyVetoException PO) {
            System.err.println(PO.getMessage());
        }
    }

    public boolean isExistsOnDesktop(JInternalFrame form) {
        JInternalFrame ji[] = jDesktopPane1.getAllFrames();

        for (int i = 0; i < ji.length; i++) {
            if (ji[i].getClass().getSimpleName().equalsIgnoreCase(form.getClass().getSimpleName())) {
                try {
                    ji[i].setSelected(true);
                    form.dispose();
                    return true;
                } catch (PropertyVetoException po) {
                }
            }
        }

        return false;
    }

    private void udfLoadNotaBaru() {
        if (!isExistsOnDesktop(new FrmNota())) {
            FrmNota fNota = new FrmNota();
            fNota.setConn(conn);
            fNota.setUserName(sUserName);
            fNota.setVisible(true);
            fNota.setBounds(0, 0, fNota.getWidth(), fNota.getHeight());
            jDesktopPane1.add(fNota, javax.swing.JLayeredPane.DEFAULT_LAYER);

            try {
                fNota.setMaximum(true);
                fNota.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isNotaOn = true;
        }

    }

    private void udfLoadNotaGabungan() {
        if (!isNotaGabungOn) {
            FrmNotaGabung fNotaGabung = new FrmNotaGabung();
            fNotaGabung.setConn(conn);
            fNotaGabung.setVisible(true);
            fNotaGabung.udfClearAll();
            fNotaGabung.setUserName(sUserName);
            fNotaGabung.udfClearAll();
            fNotaGabung.setIsKoreksi(false);
            fNotaGabung.setBounds(0, 0, fNotaGabung.getWidth(), fNotaGabung.getHeight());
            jDesktopPane1.add(fNotaGabung, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fNotaGabung.setMaximum(true);
                fNotaGabung.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isNotaGabungOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Nota Gabungan")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadMasterCustomer() {
        if (!isCustOn) {
            FrmCustomer f1 = new FrmCustomer();
            f1.setCon(conn);
            f1.setVisible(true);
            jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                f1.setMaximum(true);
                f1.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isCustOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Kapal")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadPengirim() {
        if (!isTokoOn) {

            fToko.setCon(conn);
            fToko.setVisible(true);
            jDesktopPane1.add(fToko, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                fToko.setMaximum(true);
                fToko.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isTokoOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].equals(fToko)) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadKeteranganNota() {
        if (!isKeteranganOn) {
            FrmKeterangan fKeterangan = new FrmKeterangan();

            fKeterangan.setCon(conn);
            fKeterangan.setVisible(true);
            jDesktopPane1.add(fKeterangan, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                //fKeterangan.setMaximum(true);
                fKeterangan.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isKeteranganOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Keterangan Nota")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadKapal() {
        if (!isKapalOn) {
            FrmKapalBerangkat fKapal = new FrmKapalBerangkat();

            fKapal.setCon(conn);
            fKapal.setVisible(true);
            jDesktopPane1.add(fKapal, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fKapal.setMaximizable(true);
                fKapal.setMaximum(true);
                fKapal.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isKapalOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();

            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Kapal Berangkat")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }
        }
    }

    private void udfLoadListTagihan() {
        if (!isTagihanOn) {
            fListTagihan.setCon(conn);
            fListTagihan.setVisible(true);
            fListTagihan.setUserName(sUserName);
            fListTagihan.udfSetDesktopPane(jDesktopPane1);
            fListTagihan.setBounds(0, 0, fListTagihan.getWidth(), fListTagihan.getHeight());
            jDesktopPane1.add(fListTagihan, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                fListTagihan.setMaximum(true);
                fListTagihan.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isTagihanOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].equals(fListTagihan)) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadRptPerCustomer() {

        if (!isRptKartuTagihan) {
            FrmRptKartuTagihan fRptKartu = new FrmRptKartuTagihan();
            fRptKartu.setConn(conn);
            fRptKartu.setVisible(true);
            fRptKartu.setBounds(0, 0, fRptKartu.getWidth(), fRptKartu.getHeight());
            //fRptKartu.setUserName(sUserName);
            jDesktopPane1.add(fRptKartu, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                //fRptKartu.setMaximum(true);
                fRptKartu.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptKartuTagihan = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Laporan per Customer")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadRptTagihanPerKapal() {
        if (!isRptTagihanPerKapal) {
            FrmRptTagihanPerKapal fRptTagihanPerKapal = new FrmRptTagihanPerKapal();
            fRptTagihanPerKapal.setCon(conn);
            fRptTagihanPerKapal.setVisible(true);
            //fRptKartu.setUserName(sUserName);
            jDesktopPane1.add(fRptTagihanPerKapal, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fRptTagihanPerKapal.setBounds(0, 0, fRptTagihanPerKapal.getWidth(), fRptTagihanPerKapal.getHeight());
                //f1.setMaximizable(true);
                //fRptKartu.setMaximum(true);
                fRptTagihanPerKapal.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptTagihanPerKapal = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Tagihan per Kapal")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadRptLookupNota() {
        if (!isRptLookup) {
            FrmRptNota fRptTagihanPerKapal = new FrmRptNota();
            fRptTagihanPerKapal.setCon(conn);
            fRptTagihanPerKapal.setVisible(true);
            //fRptKartu.setUserName(sUserName);
            jDesktopPane1.add(fRptTagihanPerKapal, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fRptTagihanPerKapal.setBounds(0, 0, fRptTagihanPerKapal.getWidth(), fRptTagihanPerKapal.getHeight());
                //f1.setMaximizable(true);
                //fRptKartu.setMaximum(true);
                fRptTagihanPerKapal.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptLookup = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Searching Nota")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfLoadRptLain() {
        if (!isRptLainOn) {
            FrmRptLain fRptLain = new FrmRptLain();
            fRptLain.setCon(conn);
            fRptLain.setVisible(true);
            fRptLain.setBounds(0, 0, fRptLain.getWidth(), fRptLain.getHeight());
            jDesktopPane1.add(fRptLain, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fRptLain.setAlignmentX(TOP_ALIGNMENT);
                fRptLain.setAlignmentY(LEFT_ALIGNMENT);
                fRptLain.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptLainOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Laporan lain")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    private void udfBayarTagihan() {
        if (!isBayarOn) {
            FrmPembayaran fBayar = new FrmPembayaran();
            fBayar.setConn(conn);
            fBayar.steKoreksi(false);
            fBayar.setVisible(true);
            fBayar.setUserName(sUserName);
            fBayar.setBounds(0, 0, fBayar.getWidth(), fBayar.getHeight());
            jDesktopPane1.add(fBayar, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                fBayar.setMaximum(true);
                fBayar.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isBayarOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("pembayaran pelanggan")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollKiri = new javax.swing.JScrollPane();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        taskpane_trx = new org.jdesktop.swingx.JXTaskPane();
        taskpane_daftar = new org.jdesktop.swingx.JXTaskPane();
        taskpane_report = new org.jdesktop.swingx.JXTaskPane();
        taskpane_setting = new org.jdesktop.swingx.JXTaskPane();
        jScrollDesktop = new javax.swing.JScrollPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jXStatusBar1 = new org.jdesktop.swingx.JXStatusBar();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        jXPanel2 = new org.jdesktop.swingx.JXPanel();
        lblUserName = new javax.swing.JLabel();
        jXPanel3 = new org.jdesktop.swingx.JXPanel();
        jXPanel4 = new org.jdesktop.swingx.JXPanel();
        lblKotaTujuan = new javax.swing.JLabel();
        jXPanel5 = new org.jdesktop.swingx.JXPanel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMnItemToko = new javax.swing.JMenuItem();
        jMnItemPengirim = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMnItemSwitchUser = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMnItemExit = new javax.swing.JMenuItem();
        jMenuSetting = new javax.swing.JMenu();
        jMenuSettingKapalTujuan = new javax.swing.JMenuItem();
        jMenuItemUserSetup1 = new javax.swing.JMenuItem();
        jMenuItemSettingKeterangan = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        mnuItemNotaMaintenance = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuItemUserSetup = new javax.swing.JMenuItem();
        Rep2 = new javax.swing.JMenu();
        jMenuNota = new javax.swing.JMenuItem();
        jMenuNotaGabungan = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuBayarNota = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jMenuKoreksiNota = new javax.swing.JMenuItem();
        jMenuKoreksiGabungan = new javax.swing.JMenuItem();
        jMenuKoreksiPembayaran = new javax.swing.JMenuItem();
        jMenuNotaGabungan1 = new javax.swing.JMenuItem();
        jMenuListNotaTagihan = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        mnuDaftarOutstandingPerKota = new javax.swing.JMenuItem();
        mnuDaftarOutstandingPerKapal = new javax.swing.JMenuItem();
        jMenuReport = new javax.swing.JMenu();
        jMenuItemRptPerKapal = new javax.swing.JMenuItem();
        jMenuItemRptPerCust = new javax.swing.JMenuItem();
        jMenuItemRptLain = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItemRptLain3 = new javax.swing.JMenuItem();
        jMenuItemRptLain1 = new javax.swing.JMenuItem();
        jMenuItemRptLain4 = new javax.swing.JMenuItem();
        jMenuItemRptPerKapal1 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ekspedisi Muatan Kapal Laut Trans Papua Jaya ~ (Nota Keuangan v.10.11.06)"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jSplitPane1ComponentResized(evt);
            }
        });
        jSplitPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSplitPane1PropertyChange(evt);
            }
        });
        jSplitPane1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jSplitPane1AncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jSplitPane1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        taskpane_trx.setScrollOnExpand(true);
        taskpane_trx.setTitle("Transaksi");
        jXTaskPaneContainer1.add(taskpane_trx);

        taskpane_daftar.setTitle("Daftar");
        jXTaskPaneContainer1.add(taskpane_daftar);

        taskpane_report.setTitle("Report");
        jXTaskPaneContainer1.add(taskpane_report);

        taskpane_setting.setExpanded(false);
        taskpane_setting.setTitle("Setting");
        jXTaskPaneContainer1.add(taskpane_setting);

        jScrollKiri.setViewportView(jXTaskPaneContainer1);

        jSplitPane1.setLeftComponent(jScrollKiri);

        jDesktopPane1.setBackground(new java.awt.Color(204, 204, 255));
        jScrollDesktop.setViewportView(jDesktopPane1);

        jSplitPane1.setRightComponent(jScrollDesktop);

        javax.swing.GroupLayout jXPanel1Layout = new javax.swing.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jXPanel1Layout.setVerticalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jXStatusBar1.add(jXPanel1);

        jXPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblUserName.setText("XXXXXXXXXXXXXXXXXXXXXXXX");

        javax.swing.GroupLayout jXPanel2Layout = new javax.swing.GroupLayout(jXPanel2);
        jXPanel2.setLayout(jXPanel2Layout);
        jXPanel2Layout.setHorizontalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
            .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jXPanel2Layout.createSequentialGroup()
                    .addGap(0, 43, Short.MAX_VALUE)
                    .addComponent(lblUserName)
                    .addGap(0, 43, Short.MAX_VALUE)))
        );
        jXPanel2Layout.setVerticalGroup(
            jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
            .addGroup(jXPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jXPanel2Layout.createSequentialGroup()
                    .addGap(0, 2, Short.MAX_VALUE)
                    .addComponent(lblUserName)
                    .addGap(0, 1, Short.MAX_VALUE)))
        );

        jXStatusBar1.add(jXPanel2);

        javax.swing.GroupLayout jXPanel3Layout = new javax.swing.GroupLayout(jXPanel3);
        jXPanel3.setLayout(jXPanel3Layout);
        jXPanel3Layout.setHorizontalGroup(
            jXPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jXPanel3Layout.setVerticalGroup(
            jXPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jXStatusBar1.add(jXPanel3);

        jXPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblKotaTujuan.setText("Kota xxxxxxxxx");

        javax.swing.GroupLayout jXPanel4Layout = new javax.swing.GroupLayout(jXPanel4);
        jXPanel4.setLayout(jXPanel4Layout);
        jXPanel4Layout.setHorizontalGroup(
            jXPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblKotaTujuan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jXPanel4Layout.setVerticalGroup(
            jXPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jXPanel4Layout.createSequentialGroup()
                .addComponent(lblKotaTujuan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jXStatusBar1.add(jXPanel4);

        jXPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jXPanel5Layout = new javax.swing.GroupLayout(jXPanel5);
        jXPanel5.setLayout(jXPanel5Layout);
        jXPanel5Layout.setHorizontalGroup(
            jXPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jXPanel5Layout.setVerticalGroup(
            jXPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jXStatusBar1.add(jXPanel5);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Icon/utama.png"))); // NOI18N

        jMenuFile.setMnemonic('F');
        jMenuFile.setText("File");

        jMnItemToko.setText("Toko/ Tujuan");
        jMnItemToko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemTokoActionPerformed(evt);
            }
        });
        jMenuFile.add(jMnItemToko);

        jMnItemPengirim.setText("Pengirim");
        jMnItemPengirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemPengirimActionPerformed(evt);
            }
        });
        jMenuFile.add(jMnItemPengirim);
        jMenuFile.add(jSeparator2);

        jMnItemSwitchUser.setText("Ganti kota tujuan");
        jMnItemSwitchUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnItemSwitchUserActionPerformed(evt);
            }
        });
        jMenuFile.add(jMnItemSwitchUser);
        jMenuFile.add(jSeparator1);

        jMnItemExit.setText("Keluar");
        jMenuFile.add(jMnItemExit);

        jMenuBar2.add(jMenuFile);

        jMenuSetting.setMnemonic('S');
        jMenuSetting.setText("Setting");

        jMenuSettingKapalTujuan.setText("Kapal ~ Kota tujuan");
        jMenuSettingKapalTujuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuSettingKapalTujuan);

        jMenuItemUserSetup1.setText("Jenis pembayaran");
        jMenuItemUserSetup1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUserSetup1jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItemUserSetup1);

        jMenuItemSettingKeterangan.setText("Keterangan nota");
        jMenuItemSettingKeterangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSettingKeteranganjMenuItem1ActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItemSettingKeterangan);

        jMenuItem4.setText("Satuan Nota");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItem4);

        mnuItemNotaMaintenance.setText("Maintenance item Nota");
        mnuItemNotaMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemNotaMaintenanceActionPerformed1(evt);
            }
        });
        jMenuSetting.add(mnuItemNotaMaintenance);
        jMenuSetting.add(jSeparator5);

        jMenuItemUserSetup.setText("User setup");
        jMenuItemUserSetup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUserSetupjMenuItem1ActionPerformed(evt);
            }
        });
        jMenuSetting.add(jMenuItemUserSetup);

        jMenuBar2.add(jMenuSetting);

        Rep2.setMnemonic('T');
        Rep2.setText("Transaksi");

        jMenuNota.setText("Nota tagihan");
        jMenuNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNotajMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuNota);

        jMenuNotaGabungan.setText("Nota tagihan Gabungan");
        jMenuNotaGabungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNotaGabunganjMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuNotaGabungan);
        Rep2.add(jSeparator3);

        jMenuBayarNota.setText("Pembayaran nota");
        jMenuBayarNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBayarNotajMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuBayarNota);
        Rep2.add(jSeparator4);

        jMenuKoreksiNota.setText("- Koreksi nota tagihan");
        jMenuKoreksiNota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuKoreksiNotajMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuKoreksiNota);

        jMenuKoreksiGabungan.setText("- Koreksi nota gabungan");
        jMenuKoreksiGabungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuKoreksiGabunganjMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuKoreksiGabungan);

        jMenuKoreksiPembayaran.setText("- Koreksi Pembayaran nota");
        jMenuKoreksiPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuKoreksiPembayaranjMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuKoreksiPembayaran);

        jMenuNotaGabungan1.setText("Nota tagihan Gabungan V2");
        jMenuNotaGabungan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNotaGabungan1jMenuItem1ActionPerformed(evt);
            }
        });
        Rep2.add(jMenuNotaGabungan1);

        jMenuBar2.add(Rep2);

        jMenuListNotaTagihan.setMnemonic('d');
        jMenuListNotaTagihan.setText("Daftar");
        jMenuListNotaTagihan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuListNotaTagihanActionPerformed(evt);
            }
        });

        jMenuItem2.setText("Nota tagihan");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(jMenuItem2);

        jMenuItem3.setText("Pembayaran");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(jMenuItem3);
        jMenuListNotaTagihan.add(jSeparator7);

        jMenuItem5.setText("Nota per Pelanggan");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(jMenuItem5);

        jMenuItem7.setText("Nota Jatuh Tempo");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(jMenuItem7);

        jMenuItem8.setText("Rekap Kapal per Kota");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(jMenuItem8);

        mnuDaftarOutstandingPerKota.setText("Outstanding Tagihan per Kota");
        mnuDaftarOutstandingPerKota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDaftarOutstandingPerKotaActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(mnuDaftarOutstandingPerKota);

        mnuDaftarOutstandingPerKapal.setText("Outstanding Tagihan per Kapal");
        mnuDaftarOutstandingPerKapal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDaftarOutstandingPerKapalActionPerformed(evt);
            }
        });
        jMenuListNotaTagihan.add(mnuDaftarOutstandingPerKapal);

        jMenuBar2.add(jMenuListNotaTagihan);

        jMenuReport.setMnemonic('r');
        jMenuReport.setText("Report");

        jMenuItemRptPerKapal.setText("Laporan per kapal");
        jMenuItemRptPerKapal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptPerKapaljMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptPerKapal);

        jMenuItemRptPerCust.setText("Laporan per customer");
        jMenuItemRptPerCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptPerCustjMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptPerCust);

        jMenuItemRptLain.setText("Laporan lainnya");
        jMenuItemRptLain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptLainjMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptLain);
        jMenuReport.add(jSeparator6);

        jMenuItemRptLain3.setText("Packing List");
        jMenuItemRptLain3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptLain3jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptLain3);

        jMenuItemRptLain1.setText("Packing List V2");
        jMenuItemRptLain1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptLain1jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptLain1);

        jMenuItemRptLain4.setText("Packing List per Merk");
        jMenuItemRptLain4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptLain4jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptLain4);

        jMenuItemRptPerKapal1.setText("Lookup History Nota");
        jMenuItemRptPerKapal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRptPerKapal1jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuReport.add(jMenuItemRptPerKapal1);

        jMenuItem1.setText("Tagihan");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed1(evt);
            }
        });
        jMenuReport.add(jMenuItem1);

        jMenuBar2.add(jMenuReport);

        jMenuHelp.setMnemonic('h');
        jMenuHelp.setText("Help");

        jMenuHelpAbout.setText("About");
        jMenuHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpAboutjMenuItem1ActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuHelpAbout);

        jMenuBar2.add(jMenuHelp);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jXStatusBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(6, 6, 6)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jXStatusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1019)/2, (screenSize.height-474)/2, 1019, 474);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setExtendedState(MAXIMIZED_BOTH);
////        iLeft=this.getX()+ jSplitPane1.getX()+jSplitPane1.getDividerSize()+jScrollDesktop.getX()+ jDesktopPane1.getX();
////        iTop=this.getY()+ jScrollDesktop.getX()+ jSplitPane1.getY();
    }//GEN-LAST:event_formWindowOpened

    private void jSplitPane1AncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jSplitPane1AncestorMoved
    }//GEN-LAST:event_jSplitPane1AncestorMoved

    private void jSplitPane1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jSplitPane1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jSplitPane1AncestorAdded

    private void jSplitPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSplitPane1ComponentResized
        udfSetTopLeft();
    }//GEN-LAST:event_jSplitPane1ComponentResized

    private void jSplitPane1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSplitPane1PropertyChange
    }//GEN-LAST:event_jSplitPane1PropertyChange

    private void udfSetTopLeft() {
        iLeft = this.getX() + jSplitPane1.getX() + jSplitPane1.getRightComponent().getX() + jDesktopPane1.getX() - 7;
        iTop = this.getY() + jSplitPane1.getY() + jDesktopPane1.getY();
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (isExistsOnDesktop(new FrmKapalBerangkat())) {
            return;
        }
        FrmKapalBerangkat f1 = new FrmKapalBerangkat();
        f1.setCon(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuNotajMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNotajMenuItem1ActionPerformed
        udfLoadNotaBaru();
}//GEN-LAST:event_jMenuNotajMenuItem1ActionPerformed

    private void jMenuItemRptPerKapaljMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptPerKapaljMenuItem1ActionPerformed
        // TODO add your handling code here:
        udfLoadRptTagihanPerKapal();
}//GEN-LAST:event_jMenuItemRptPerKapaljMenuItem1ActionPerformed

    private void jMenuHelpAboutjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuHelpAboutjMenuItem1ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jMenuHelpAboutjMenuItem1ActionPerformed

    private void jMenuNotaGabunganjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNotaGabunganjMenuItem1ActionPerformed
        udfLoadNotaGabungan();
}//GEN-LAST:event_jMenuNotaGabunganjMenuItem1ActionPerformed

    private void jMenuKoreksiNotajMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuKoreksiNotajMenuItem1ActionPerformed
        udfLoadKoreksiNota();
}//GEN-LAST:event_jMenuKoreksiNotajMenuItem1ActionPerformed

    private void jMenuKoreksiGabunganjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuKoreksiGabunganjMenuItem1ActionPerformed
        udfLoadKoreksiNotaGabungan();
}//GEN-LAST:event_jMenuKoreksiGabunganjMenuItem1ActionPerformed

    private void jMenuBayarNotajMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBayarNotajMenuItem1ActionPerformed
        udfBayarTagihan();
}//GEN-LAST:event_jMenuBayarNotajMenuItem1ActionPerformed

    private void jMenuKoreksiPembayaranjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuKoreksiPembayaranjMenuItem1ActionPerformed
        // TODO add your handling code here:
        DlgKoreksiPembayaran d1 = new DlgKoreksiPembayaran(this, true);
        d1.setUserName(sUserName);
        d1.setConn(conn);
        d1.show();

}//GEN-LAST:event_jMenuKoreksiPembayaranjMenuItem1ActionPerformed

    private void jMenuItemSettingKeteranganjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSettingKeteranganjMenuItem1ActionPerformed
        udfLoadKeteranganNota();
}//GEN-LAST:event_jMenuItemSettingKeteranganjMenuItem1ActionPerformed

    private void jMenuItemUserSetupjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserSetupjMenuItem1ActionPerformed
        DlgUserSet usr = new DlgUserSet(this, true);
        usr.setConn(conn);
        usr.setVisible(true);
}//GEN-LAST:event_jMenuItemUserSetupjMenuItem1ActionPerformed

    private void jMenuItemRptPerCustjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptPerCustjMenuItem1ActionPerformed
        // TODO add your handling code here:
        udfLoadRptPerCustomer();
}//GEN-LAST:event_jMenuItemRptPerCustjMenuItem1ActionPerformed

    private void jMenuItemRptLainjMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptLainjMenuItem1ActionPerformed
        // TODO add your handling code here:
        udfLoadRptLain();
}//GEN-LAST:event_jMenuItemRptLainjMenuItem1ActionPerformed

    private void jMenuNotaGabungan1jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNotaGabungan1jMenuItem1ActionPerformed
        if (!isNotaGabungOn) {
            FrmNotaGabung fNotaGabung = new FrmNotaGabung();
            fNotaGabung.setConn(conn);
            fNotaGabung.setVisible(true);
            fNotaGabung.setUserName(sUserName);
            fNotaGabung.udfClearAll();
            jDesktopPane1.add(fNotaGabung, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                fNotaGabung.setMaximum(true);
                fNotaGabung.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isNotaGabungOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Koreksi Nota Gabungan")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }//GEN-LAST:event_jMenuNotaGabungan1jMenuItem1ActionPerformed

    private void jMenuItemUserSetup1jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserSetup1jMenuItem1ActionPerformed
        if (!isJenisBayarOn) {
            FrmJenisBayar f1 = new FrmJenisBayar();
            f1.setConn(conn);
            f1.setVisible(true);
            f1.setUserName(sUserName);
            jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                f1.setMaximum(true);
                f1.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isNotaGabungOn = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Jenis Bayar")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }//GEN-LAST:event_jMenuItemUserSetup1jMenuItem1ActionPerformed

    private void jMenuItemRptLain1jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptLain1jMenuItem1ActionPerformed
        FrmRptPackingListBaru f1 = new FrmRptPackingListBaru();
        if (!isRptPackingList) {

            f1.setCon(conn);
            f1.setUserName(sUserName);
            f1.setVisible(true);
            jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                //f1.setMaximum(true);
                f1.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptPackingList = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].equals(f1)) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }//GEN-LAST:event_jMenuItemRptLain1jMenuItem1ActionPerformed

    private void jMenuItemRptLain3jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptLain3jMenuItem1ActionPerformed
        if (!isRptPackingList) {
            FrmRptPackingListFix f1 = new FrmRptPackingListFix();
            f1.setCon(conn);
            f1.setUserName(sUserName);
            f1.setVisible(true);
            jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                //f1.setMaximizable(true);
                //f1.setMaximum(true);
                f1.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isRptPackingList = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Report")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }//GEN-LAST:event_jMenuItemRptLain3jMenuItem1ActionPerformed

    private void jMenuItemRptLain4jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptLain4jMenuItem1ActionPerformed
        if (!isPckPerMerk) {
            FrmRptPerMerk_v2 f1 = new FrmRptPerMerk_v2();
            f1.setConn(conn);
            f1.setVisible(true);
            jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
            try {
                f1.setSelected(true);
            } catch (PropertyVetoException PO) {
            }
            isPckPerMerk = true;
        } else {
            JInternalFrame ji[] = jDesktopPane1.getAllFrames();
            for (int i = 0; i < ji.length; i++) {
                System.out.println(ji[i].getTitle());

                if (ji[i].getTitle().equalsIgnoreCase("Laporan per Merk")) {
                    try {
                        ji[i].setSelected(true);
                    } catch (PropertyVetoException PO) {
                    }
                    break;
                }
            }

        }
    }//GEN-LAST:event_jMenuItemRptLain4jMenuItem1ActionPerformed

    private void jMnItemTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemTokoActionPerformed
        udfLoadMasterCustomer();
    }//GEN-LAST:event_jMnItemTokoActionPerformed

    private void jMnItemPengirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemPengirimActionPerformed
        udfLoadPengirim();
    }//GEN-LAST:event_jMnItemPengirimActionPerformed

    private void jMnItemSwitchUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnItemSwitchUserActionPerformed
        DlgGantiKota d1 = new DlgGantiKota(this, true);
        d1.setConn(conn);
        d1.setMainForm(this);
        d1.setVisible(true);
    }//GEN-LAST:event_jMnItemSwitchUserActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if(isExistsOnDesktop(new FrmListPembayaranFilter()))
            return;
        FrmListPembayaranFilter f1 = new FrmListPembayaranFilter();
        f1.setConn(conn);
        f1.setUserName(sUserName);
        f1.setDesktop(jDesktopPane1);
        f1.setVisible(true);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            f1.setSelected(true);
        } catch (PropertyVetoException PO) {
        }
        isListBayar = true;
}//GEN-LAST:event_jMenuItem3ActionPerformed

private void jMenuListNotaTagihanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuListNotaTagihanActionPerformed
}//GEN-LAST:event_jMenuListNotaTagihanActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    udfLoadListTagihan();
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItemRptPerKapal1jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRptPerKapal1jMenuItem1ActionPerformed
    udfLoadRptLookupNota();
}//GEN-LAST:event_jMenuItemRptPerKapal1jMenuItem1ActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
    DlgSatuanKirim d1 = new DlgSatuanKirim(this, true);
    d1.setConn(conn);
    d1.setVisible(true);
}//GEN-LAST:event_jMenuItem4ActionPerformed

private void mnuItemNotaMaintenanceActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemNotaMaintenanceActionPerformed1
    DlgItemNota d1 = new DlgItemNota(this, true);
    d1.setConn(conn);
    d1.setVisible(true);
}//GEN-LAST:event_mnuItemNotaMaintenanceActionPerformed1

private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    if (isExistsOnDesktop(new FrmListTagihanPerCust())) {
        return;
    }
    FrmListTagihanPerCust f1 = new FrmListTagihanPerCust();
    f1.setCon(conn);
    jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
    f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
    f1.setVisible(true);
}//GEN-LAST:event_jMenuItem5ActionPerformed

private void mnuDaftarOutstandingPerKotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDaftarOutstandingPerKotaActionPerformed
        if (isExistsOnDesktop(new FrmListTagihanPerKotaOutstanding())) {
            return;
        }
        FrmListTagihanPerKotaOutstanding f1 = new FrmListTagihanPerKotaOutstanding();
        f1.setCon(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
}//GEN-LAST:event_mnuDaftarOutstandingPerKotaActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        if (isExistsOnDesktop(new FrmNotaJatuhTempo())) {
            return;
        }
        FrmNotaJatuhTempo f1 = new FrmNotaJatuhTempo();
        f1.setConn(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem1ActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed1
        if (isExistsOnDesktop(new FrmRptTagihan())) {
            return;
        }
        FrmRptTagihan f1 = new FrmRptTagihan();
        f1.setConn(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed1

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        if (isExistsOnDesktop(new FrmListBiayaKapal())) {
            return;
        }
        FrmListBiayaKapal f1 = new FrmListBiayaKapal();
        f1.setCon(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void mnuDaftarOutstandingPerKapalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDaftarOutstandingPerKapalActionPerformed
        if (isExistsOnDesktop(new FrmListTagihanPerKapalOutstanding())) {
            return;
        }
        FrmListTagihanPerKapalOutstanding f1 = new FrmListTagihanPerKapalOutstanding();
        f1.setCon(conn);
        jDesktopPane1.add(f1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        f1.setBounds(0, 0, f1.getWidth(), f1.getHeight());
        f1.setVisible(true);
}//GEN-LAST:event_mnuDaftarOutstandingPerKapalActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Utama().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Rep2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuBayarNota;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuHelpAbout;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItemRptLain;
    private javax.swing.JMenuItem jMenuItemRptLain1;
    private javax.swing.JMenuItem jMenuItemRptLain3;
    private javax.swing.JMenuItem jMenuItemRptLain4;
    private javax.swing.JMenuItem jMenuItemRptPerCust;
    private javax.swing.JMenuItem jMenuItemRptPerKapal;
    private javax.swing.JMenuItem jMenuItemRptPerKapal1;
    private javax.swing.JMenuItem jMenuItemSettingKeterangan;
    private javax.swing.JMenuItem jMenuItemUserSetup;
    private javax.swing.JMenuItem jMenuItemUserSetup1;
    private javax.swing.JMenuItem jMenuKoreksiGabungan;
    private javax.swing.JMenuItem jMenuKoreksiNota;
    private javax.swing.JMenuItem jMenuKoreksiPembayaran;
    private javax.swing.JMenu jMenuListNotaTagihan;
    private javax.swing.JMenuItem jMenuNota;
    private javax.swing.JMenuItem jMenuNotaGabungan;
    private javax.swing.JMenuItem jMenuNotaGabungan1;
    private javax.swing.JMenu jMenuReport;
    private javax.swing.JMenu jMenuSetting;
    private javax.swing.JMenuItem jMenuSettingKapalTujuan;
    private javax.swing.JMenuItem jMnItemExit;
    private javax.swing.JMenuItem jMnItemPengirim;
    private javax.swing.JMenuItem jMnItemSwitchUser;
    private javax.swing.JMenuItem jMnItemToko;
    private javax.swing.JScrollPane jScrollDesktop;
    private javax.swing.JScrollPane jScrollKiri;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    private org.jdesktop.swingx.JXPanel jXPanel2;
    private org.jdesktop.swingx.JXPanel jXPanel3;
    private org.jdesktop.swingx.JXPanel jXPanel4;
    private org.jdesktop.swingx.JXPanel jXPanel5;
    private org.jdesktop.swingx.JXStatusBar jXStatusBar1;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    private javax.swing.JLabel lblKotaTujuan;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JMenuItem mnuDaftarOutstandingPerKapal;
    private javax.swing.JMenuItem mnuDaftarOutstandingPerKota;
    private javax.swing.JMenuItem mnuItemNotaMaintenance;
    private org.jdesktop.swingx.JXTaskPane taskpane_daftar;
    private org.jdesktop.swingx.JXTaskPane taskpane_report;
    private org.jdesktop.swingx.JXTaskPane taskpane_setting;
    private org.jdesktop.swingx.JXTaskPane taskpane_trx;
    // End of variables declaration//GEN-END:variables
}
