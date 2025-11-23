package main;

import controller.TriageController;
import view.TriageView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                crearYMostrarGUI();
            }
        });
    }

    private static void crearYMostrarGUI() {
        JFrame ventana = new JFrame("TriageED - Sistema de Triage Hospitalario");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TriageController controlador = new TriageController();
        TriageView vista = new TriageView();

        controlador.setVista(vista);
        vista.setControlador(controlador);

        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
}