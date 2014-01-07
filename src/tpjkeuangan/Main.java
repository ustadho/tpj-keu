/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tpjkeuangan;

import javax.swing.UIManager;

/**
 *
 * @author oestadho
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception evt) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginUser().setVisible(true);
            }
        });
    }

}
