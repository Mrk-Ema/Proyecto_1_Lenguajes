package com.mycompany.promptzal;

import com.mycompany.promptzal.frontend.Contenedor;
import javax.swing.UIManager;

public class PromptZal {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Contenedor().setVisible(true);
        });
    }
}
