package com.vizor.test.component;

import com.vizor.test.service.ImgManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private ImgManager imgManager;
    private List<List<IconLabel>> paginatedCollection;
    private int page = 0;

    private JPanel footPanel;
    private JPanel bigFootPanel;
    private ImgLabel imgLabel;
    private JPanel imgListPanel;
    private JPanel buttonListPanel;

/*    private JMenuBar topMenuBar;
    private JMenu fileMenu;
    private JMenuItem addImageItem;
    private JMenuItem loadImageItem;*/

    private JButton previousButton;
    private JButton nextButton;

    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        imgLabel = ImgLabel.getInstance();
        footPanel = new JPanel();
        bigFootPanel = new JPanel();
        imgListPanel = new JPanel();
        buttonListPanel = new JPanel();

        previousButton = new JButton("<<");
        //previousButton.setEnabled(false);
        nextButton = new JButton(">>");

/*        topMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        addImageItem = new JMenuItem();
        loadImageItem = new JMenuItem();*/

        imgManager = new ImgManager();
        paginatedCollection = imgManager.doPaginatedCollection(13);

/*        fileMenu.add(addImageItem);
        fileMenu.add(loadImageItem);*/

/*        topMenuBar.add(fileMenu);
        setJMenuBar(topMenuBar);*/


        add(new JScrollPane(imgLabel), BorderLayout.CENTER);
        add(bigFootPanel, BorderLayout.SOUTH);

        bigFootPanel.setLayout(new BorderLayout());
        bigFootPanel.add(buttonListPanel, BorderLayout.NORTH);
        bigFootPanel.add(footPanel, BorderLayout.SOUTH);

        footPanel.add(previousButton, BorderLayout.WEST);
        footPanel.add(imgListPanel, BorderLayout.CENTER);
        footPanel.add(nextButton, BorderLayout.EAST);


        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (page > 0) {
                    page--;
                    updatePageList(paginatedCollection.get(page));
/*                    nextButton.setEnabled(true);
                    if (page == 0) {
                        previousButton.setEnabled(false);
                    }*/
                }
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (page < paginatedCollection.size() - 1) {
                    page++;
                    updatePageList(paginatedCollection.get(page));
/*                    previousButton.setEnabled(true);
                    if (page == paginatedCollection.size() - 1) {
                        nextButton.setEnabled(false);
                    }*/
                }
            }
        });

        updatePageList(paginatedCollection.get(page));
        updateButtonList();

/*        fileMenu.setText("File");
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
        });*/

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void updatePageList(List<IconLabel> collection) {
        imgListPanel.removeAll();

        for (IconLabel iconLabel : collection) {
            imgListPanel.add(iconLabel);
            iconLabel.repaint();
            System.out.print(iconLabel);
        }

        imgListPanel.revalidate();
        //footPanel.revalidate();
        System.out.println();
        System.out.println(page);
        System.out.println(paginatedCollection.size());
    }

    private void updateButtonList() {
        Integer i = 1;
        for (List<IconLabel> iconLabels : paginatedCollection) {
            int tempPage = i - 1;
            JButton button = new JButton(i.toString());
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updatePageList(iconLabels);
                    page = tempPage;
                }
            });
            buttonListPanel.add(button);
            i++;
        }

    }

}
