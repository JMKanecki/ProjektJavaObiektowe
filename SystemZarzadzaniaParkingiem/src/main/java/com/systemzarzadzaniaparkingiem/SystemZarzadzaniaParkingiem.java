/**
 * @autorzy: Jakub Kanecki, Dawid Łazarski
 */
package com.systemzarzadzaniaparkingiem;

import com.systemzarzadzaniaparkingiem.model.Parking;
import com.systemzarzadzaniaparkingiem.widok.MainWindow;
import javax.swing.UIManager;

public class SystemZarzadzaniaParkingiem {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. start GUI w EDT
        java.awt.EventQueue.invokeLater(() -> {
            MainWindow widok = new MainWindow();
            
            widok.setVisible(true);
        });
        
    }
}
