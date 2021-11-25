package com.vizor.test.component;

import com.vizor.test.service.ImgCollectionService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    public static final int PAGE_SIZE = 13;
    public static int CURRENT_PAGE = 0;
    private ImgCollectionService imgCollectionService;
    private List<List<ImgIconLabel>> paginatedCollection;

    private JPanel footPanel;
    private JPanel paginationPanel;
    private ViewImgLabel viewImgLabel;
    private JPanel imgIconsPanel;
    private JPanel toolsPanel;
    private JPanel pageButtonsPanel;
    private JTextField searchField;

    private JMenuBar topMenuBar;
    private JMenu fileMenu;
    private JMenuItem addImageMenuItem;
    private JMenuItem selectDirectoryMenuItem;

    private JButton previousButton;
    private JButton nextButton;

    public MainFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        //инициализируем сервис для работы с коллекцией картинок
        imgCollectionService = ImgCollectionService.getInstance();

        viewImgLabel = ViewImgLabel.getInstance();
        paginationPanel = new JPanel();

        toolsPanel = new JPanel();
        footPanel = new JPanel();
        imgIconsPanel = new JPanel();
        pageButtonsPanel = new JPanel();

        searchField = new JTextField(10);
        previousButton = new JButton("<<");
        nextButton = new JButton(">>");

        topMenuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addImageMenuItem = new JMenuItem("Add image");
        selectDirectoryMenuItem = new JMenuItem("Select new root directory");

        //добавление компонентов меню
        setJMenuBar(topMenuBar);
        topMenuBar.add(fileMenu);
        fileMenu.add(addImageMenuItem);
        fileMenu.add(selectDirectoryMenuItem);

        viewImgLabel.setHorizontalAlignment(JLabel.CENTER);
        //добавление лейбла отображающего картинку в полном размере
        add(new JScrollPane(viewImgLabel), BorderLayout.CENTER);

        //добавление панели компонентов навигации по колекции картинок
        add(paginationPanel, BorderLayout.SOUTH);
        paginationPanel.setLayout(new BorderLayout());

        //добавление панели содержащей кнопки паджинации и поле поиска
        paginationPanel.add(toolsPanel, BorderLayout.NORTH);
        toolsPanel.add(new JLabel("Search: "));
        toolsPanel.add(searchField);
        toolsPanel.add(pageButtonsPanel);

        //добавление панели содержащей список картинок и кнопки назад/вперёд
        paginationPanel.add(footPanel, BorderLayout.SOUTH);
        footPanel.add(previousButton, BorderLayout.WEST);
        footPanel.add(imgIconsPanel, BorderLayout.CENTER);
        footPanel.add(nextButton, BorderLayout.EAST);

        //получаем отпадженированную коллекцию картинок
        paginatedCollection = imgCollectionService.doPaginatedCollection(PAGE_SIZE);
        //обновляем список иконок картинок
        updateImgIconList(paginatedCollection.get(CURRENT_PAGE));
        //создаём набор кнопок паджинации
        recreatePageButtonList(paginatedCollection);
        //обновляем состояние всех компонентов паджинации
        updateStatePaginationComponents();
        //добавляем листенеры к компанентам управления
        initListeners();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initListeners(){
        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (CURRENT_PAGE > 0) {
                    CURRENT_PAGE--;
                    updateImgIconList(paginatedCollection.get(CURRENT_PAGE));
                    updateStatePaginationComponents();
                }
            }
        });
        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (CURRENT_PAGE < paginatedCollection.size() - 1) {
                    CURRENT_PAGE++;
                    updateImgIconList(paginatedCollection.get(CURRENT_PAGE));
                    updateStatePaginationComponents();
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    paginatedCollection = imgCollectionService.filterCollectionByName(searchField.getText(), PAGE_SIZE);
                    if (paginatedCollection.size() > 0) {
                        CURRENT_PAGE= 0;
                        updateImgIconList(paginatedCollection.get(0));
                        recreatePageButtonList(paginatedCollection);
                        updateStatePaginationComponents();
                    } else {
                        JOptionPane.showMessageDialog(null, "Image not found!");
                        searchField.setText("");
                    }
                }
            }
        });

        addImageMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));

                if (jFileChooser.showOpenDialog(addImageMenuItem) == JFileChooser.APPROVE_OPTION) {
                    File iFile = jFileChooser.getSelectedFile();
                    imgCollectionService.addImgFile(iFile);
                    paginatedCollection = imgCollectionService.doPaginatedCollection(PAGE_SIZE);
                    updateImgIconList(paginatedCollection.get(CURRENT_PAGE));
                    recreatePageButtonList(paginatedCollection);
                    updateStatePaginationComponents();
                }
            }
        });

        selectDirectoryMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (jFileChooser.showOpenDialog(selectDirectoryMenuItem) == JFileChooser.APPROVE_OPTION) {
                    File iFile = jFileChooser.getSelectedFile();
                    imgCollectionService.selectRootDirectory(iFile);
                    CURRENT_PAGE = 0;
                    paginatedCollection = imgCollectionService.doPaginatedCollection(PAGE_SIZE);
                    updateImgIconList(paginatedCollection.get(CURRENT_PAGE));
                    recreatePageButtonList(paginatedCollection);
                    updateStatePaginationComponents();
                }
            }
        });
    }

    //обновляем список иконок картинок
    private void updateImgIconList(List<ImgIconLabel> collection) {
        imgIconsPanel.removeAll();
        for (ImgIconLabel imgIconLabel : collection) {
            imgIconsPanel.add(imgIconLabel);
            imgIconLabel.repaint();
        }

        imgIconsPanel.revalidate();
    }

    //создаём/пересоздаём список кнопок паджинации
    private void recreatePageButtonList(List<List<ImgIconLabel>> paginatedCollection) {
        pageButtonsPanel.removeAll();
        Integer i = 1;
        for (List<ImgIconLabel> imgIconLabels : paginatedCollection) {
            int tempPage = i - 1;
            JButton button = new JButton(i.toString());
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateImgIconList(imgIconLabels);
                    CURRENT_PAGE = tempPage;
                    updateStatePaginationComponents();
                }
            });
            pageButtonsPanel.add(button);
            i++;
        }
        pageButtonsPanel.revalidate();
    }

    private void updateStatePaginationComponents(){
        updateStatePageButtons();
        updateStatePrevAndNextButtons();
    }

    // обновляем состояние доступности кнопок - назад/вперёд
    private void updateStatePrevAndNextButtons() {
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
        if (CURRENT_PAGE == 0) {
            previousButton.setEnabled(false);
        }
        if (CURRENT_PAGE == paginatedCollection.size() - 1) {
            nextButton.setEnabled(false);
        }
    }

    // обновляем состояние доступности кнопок паджинации
    private void updateStatePageButtons() {
        Component[] components = pageButtonsPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (i == CURRENT_PAGE) {
                components[i].setEnabled(false);
            } else {
                components[i].setEnabled(true);
            }
        }
    }

}
