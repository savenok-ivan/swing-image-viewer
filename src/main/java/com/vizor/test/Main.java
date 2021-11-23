package com.vizor.test;

import com.vizor.test.component.MainFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class Main {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    public void run() {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main()::run);
    }
}
