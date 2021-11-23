package com.vizor.test.component;

import com.vizor.test.service.ImgManager;
import com.vizor.test.util.ListUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private ImgManager imgManager;
    private int page = 1;

    private JPanel footPanel;
    private ImgLabel imgLabel;
    private JPanel imgListPanel;

    private JMenuBar topMenuBar;
    private JMenu fileMenu;
    private JMenuItem addImageItem;
    private JMenuItem loadImageItem;

    private JButton previousButton;
    private JButton nextButton;

    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        imgLabel = ImgLabel.getInstance();
        footPanel = new JPanel();
        imgListPanel = new JPanel();

        previousButton = new JButton("<<");
        nextButton = new JButton(">>");

        topMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        addImageItem = new JMenuItem();
        loadImageItem = new JMenuItem();

        imgManager = new ImgManager();


        fileMenu.add(addImageItem);
        fileMenu.add(loadImageItem);

        topMenuBar.add(fileMenu);
        setJMenuBar(topMenuBar);


        add(new JScrollPane(imgLabel), BorderLayout.CENTER);
        add(footPanel, BorderLayout.SOUTH);

        footPanel.add(previousButton, BorderLayout.WEST);
        footPanel.add(imgListPanel, BorderLayout.CENTER);
        footPanel.add(nextButton, BorderLayout.EAST);

        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                page--;
                imgListPanel.removeAll();
                updatePageList();
                imgListPanel.validate();
                System.out.println(page);
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                page++;
                imgListPanel.removeAll();
                updatePageList();
                imgListPanel.validate();
                System.out.println(page);
            }
        });

        updatePageList();

        fileMenu.setText("File");
        addImageItem.setText("Add image");
        addImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        loadImageItem.setText("Load image by URL");
        loadImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void updatePageList() {
        for (IconLabel iconLabel : ListUtils.getPage(imgManager.getCollection(), page, 10)) {
            imgListPanel.add(iconLabel);
            iconLabel.repaint();
            System.out.print(iconLabel);
        }
        System.out.println();
    }

}
