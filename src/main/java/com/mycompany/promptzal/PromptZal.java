package com.mycompany.promptzal;

import com.mycompany.promptzal.frontend.Contenedor;
import javax.swing.UIManager;

public class PromptZal {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Contenedor().setVisible(true);
        });
    }
}
