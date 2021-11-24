package com.vizor.test.component;

import com.vizor.test.service.ImgManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainFrame extends JFrame {
    public static final int PAGE_SIZE = 13;
    private static int CURRENT_PAGE = 0;
    private ImgManager imgManager;
    private List<List<IconLabel>> paginatedCollection;

    private JPanel footPanel;
    private JPanel bigFootPanel;
    private ImgLabel imgLabel;
    private JPanel imgListPanel;
    private JPanel toolsPanel;
    private JPanel buttonListPanel;
    private JTextField searchField;

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
        toolsPanel = new JPanel();
        buttonListPanel = new JPanel();
        searchField = new JTextField(10);

        previousButton = new JButton("<<");
        //previousButton.setEnabled(false);
        nextButton = new JButton(">>");

/*        topMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        addImageItem = new JMenuItem();
        loadImageItem = new JMenuItem();*/

        imgManager = new ImgManager();
        paginatedCollection = imgManager.doPaginatedCollection(PAGE_SIZE);

/*        fileMenu.add(addImageItem);
        fileMenu.add(loadImageItem);*/

/*        topMenuBar.add(fileMenu);
        setJMenuBar(topMenuBar);*/


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

        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (CURRENT_PAGE > 0) {
                    CURRENT_PAGE--;
                    updatePageList(paginatedCollection.get(CURRENT_PAGE));
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

                if (CURRENT_PAGE < paginatedCollection.size() - 1) {
                    CURRENT_PAGE++;
                    updatePageList(paginatedCollection.get(CURRENT_PAGE));
/*                    previousButton.setEnabled(true);
                    if (page == paginatedCollection.size() - 1) {
                        nextButton.setEnabled(false);
                    }*/
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    List<IconLabel> iconLabels = imgManager.searchImages(searchField.getText());
                    paginatedCollection = imgManager.doPaginatedCollection(iconLabels, PAGE_SIZE);
                    updatePageList(paginatedCollection.get(0));
                    updateButtonList(paginatedCollection);
                }
            }
        });

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
/*                searchField.setText("");
                updatePageList(paginatedCollection.get(page));
                updateButtonList(paginatedCollection);*/
            }
        });

        updatePageList(paginatedCollection.get(CURRENT_PAGE));
        updateButtonList(paginatedCollection);

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
        System.out.println(CURRENT_PAGE);
        System.out.println(paginatedCollection.size());
    }

    private void updateButtonList(List<List<IconLabel>> paginatedCollection) {
        buttonListPanel.removeAll();
        Integer i = 1;
        for (List<IconLabel> iconLabels : paginatedCollection) {
            int tempPage = i - 1;
            JButton button = new JButton(i.toString());
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updatePageList(iconLabels);
                    CURRENT_PAGE = tempPage;
                }
            });
            buttonListPanel.add(button);
            i++;
        }
        buttonListPanel.revalidate();
    }

}
