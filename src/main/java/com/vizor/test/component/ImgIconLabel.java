package com.vizor.test.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

//класс для отображения маленькой иконки картинки в паджинированной коллекции
public class ImgIconLabel extends JLabel {
    private String imgName;
    private ImageIcon imageIcon;
    private ViewImgLabel viewImgLabel;

    public ImgIconLabel(File imgFile) {
        this.viewImgLabel = ViewImgLabel.getInstance();
        this.imageIcon = new ImageIcon(imgFile.toString());
        this.imgName = imgFile.getName();
        this.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
        this.setHorizontalAlignment(JLabel.CENTER);

        //навершиваем обработчик для отображения текущей картинки в полном размере в лейбле ViewImgLabel
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                viewImgLabel.setIcon(imageIcon);
                viewImgLabel.setText(imgName);
                viewImgLabel.setHorizontalTextPosition(JLabel.CENTER);
                viewImgLabel.setVerticalTextPosition(JLabel.BOTTOM);
            }
        });
    }

    public String getImgName() {
        return imgName;
    }

    @Override
    public String toString() {
        return "IconLabel{" +
                "imgName='" + imgName + '\'' +
                '}';
    }
}
