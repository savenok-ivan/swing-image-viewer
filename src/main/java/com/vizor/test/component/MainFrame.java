package com.vizor.test.component;

import com.vizor.test.service.ImgCollectionManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    public static final int PAGE_SIZE = 13;
    private static int CURRENT_PAGE = 0;
    private ImgCollectionManager imgCollectionManager;
    private List<List<IconLabel>> paginatedCollection;

    private JPanel footPanel;
    private JPanel bigFootPanel;
    private ImgLabel imgLabel;
    private JPanel imgListPanel;
    private JPanel toolsPanel;
    private JPanel buttonListPanel;
    private JTextField searchField;

    private JMenuBar topMenuBar;
    private JMenu fileMenu;
    private JMenuItem addImageItem;
    private JMenuItem selectRootDirectoryItem;

    private JButton previousButton;
    private JButton nextButton;

    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        imgCollectionManager = ImgCollectionManager.getInstance();

        imgLabel = ImgLabel.getInstance();
        footPanel = new JPanel();
        bigFootPanel = new JPanel();
        imgListPanel = new JPanel();
        toolsPanel = new JPanel();
        buttonListPanel = new JPanel();
        searchField = new JTextField(10);
        previousButton = new JButton("<<");
        nextButton = new JButton(">>");

        topMenuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addImageItem = new JMenuItem("Add image");
        selectRootDirectoryItem = new JMenuItem("Select new root directory");


        setJMenuBar(topMenuBar);
        topMenuBar.add(fileMenu);
        fileMenu.add(addImageItem);
        fileMenu.add(selectRootDirectoryItem);


        add(new JScrollPane(imgLabel), BorderLayout.CENTER);
        add(bigFootPanel, BorderLayout.SOUTH);

        bigFootPanel.setLayout(new BorderLayout());
        bigFootPanel.add(toolsPanel, BorderLayout.NORTH);
        bigFootPanel.add(footPanel, BorderLayout.SOUTH);

        toolsPanel.add(new JLabel("Search: "));
        toolsPanel.add(searchField);
        toolsPanel.add(buttonListPanel);


        footPanel.add(previousButton, BorderLayout.WEST);
        footPanel.add(imgListPanel, BorderLayout.CENTER);
        footPanel.add(nextButton, BorderLayout.EAST);

        imgLabel.setHorizontalAlignment(JLabel.CENTER);


        paginatedCollection = imgCollectionManager.doPaginatedCollection(PAGE_SIZE);
        updateImageList(paginatedCollection.get(CURRENT_PAGE));
        updatePageButtonList(paginatedCollection);
        validateEnabledPaginationComponents();

        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (CURRENT_PAGE > 0) {
                    CURRENT_PAGE--;
                    updateImageList(paginatedCollection.get(CURRENT_PAGE));
                    validateEnabledPaginationComponents();
                }
            }
        });

        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (CURRENT_PAGE < paginatedCollection.size() - 1) {
                    CURRENT_PAGE++;
                    updateImageList(paginatedCollection.get(CURRENT_PAGE));
                    validateEnabledPaginationComponents();
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    List<IconLabel> iconLabels = imgCollectionManager.filterImageCollectionByName(searchField.getText());
                    paginatedCollection = imgCollectionManager.doPaginatedCollection(iconLabels, PAGE_SIZE);
                    if (paginatedCollection.size() > 0) {
                        CURRENT_PAGE= 0;
                        updateImageList(paginatedCollection.get(0));
                        updatePageButtonList(paginatedCollection);
                        validateEnabledPaginationComponents();
                    } else {
                        JOptionPane.showMessageDialog(null, "Image not found!");
                        searchField.setText("");
                    }
                }
            }
        });


        addImageItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));

                if (jFileChooser.showOpenDialog(addImageItem) == JFileChooser.APPROVE_OPTION) {
                    File iFile = jFileChooser.getSelectedFile();
                    imgCollectionManager.addImageFile(iFile);
                    paginatedCollection = imgCollectionManager.doPaginatedCollection(PAGE_SIZE);
                    updateImageList(paginatedCollection.get(CURRENT_PAGE));
                    updatePageButtonList(paginatedCollection);
                }
            }
        });

        selectRootDirectoryItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (jFileChooser.showOpenDialog(selectRootDirectoryItem) == JFileChooser.APPROVE_OPTION) {
                    File iFile = jFileChooser.getSelectedFile();
                    imgCollectionManager.selectRootDirectory(iFile);
                    CURRENT_PAGE = 0;
                    paginatedCollection = imgCollectionManager.doPaginatedCollection(PAGE_SIZE);
                    updateImageList(paginatedCollection.get(CURRENT_PAGE));
                    updatePageButtonList(paginatedCollection);
                }
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void updateImageList(List<IconLabel> collection) {
        imgListPanel.removeAll();

        for (IconLabel iconLabel : collection) {
            imgListPanel.add(iconLabel);
            iconLabel.repaint();
        }

        imgListPanel.revalidate();
    }

    private void updatePageButtonList(List<List<IconLabel>> paginatedCollection) {
        buttonListPanel.removeAll();
        Integer i = 1;
        for (List<IconLabel> iconLabels : paginatedCollection) {
            int tempPage = i - 1;
            JButton button = new JButton(i.toString());
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateImageList(iconLabels);
                    CURRENT_PAGE = tempPage;
                    validateEnabledPaginationComponents();
                }
            });
            buttonListPanel.add(button);
            i++;
        }
        buttonListPanel.revalidate();
    }

    private void validateEnabledPaginationComponents(){
        validateEnabledPageButtons();
        validateEnabledPrevAndNextButtons();
    }

    private void validateEnabledPrevAndNextButtons() {
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
        if (CURRENT_PAGE == 0) {
            previousButton.setEnabled(false);
        }
        if (CURRENT_PAGE == paginatedCollection.size() - 1) {
            nextButton.setEnabled(false);
        }
    }

    private void validateEnabledPageButtons() {
        Component[] components = buttonListPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (i == CURRENT_PAGE) {
                components[i].setEnabled(false);
            } else {
                components[i].setEnabled(true);
            }
        }
    }

}
