package com.vizor.test.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class IconLabel extends JLabel {
    private String imgName;
    private ImageIcon imageIcon;

    public IconLabel(File imgFile) {
        this.imageIcon = new ImageIcon(imgFile.toString());
        this.imgName = imgFile.getName();
        this.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT)));
        this.setHorizontalAlignment(JLabel.CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImgLabel.getInstance().setIcon(imageIcon);
                ImgLabel.getInstance().setText(imgName);
                ImgLabel.getInstance().setHorizontalTextPosition(JLabel.CENTER);
                ImgLabel.getInstance().setVerticalTextPosition(JLabel.BOTTOM);
            }
        });

    }

    public String getImgName() {
        return imgName;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    @Override
    public String toString() {
        return "IconLabel{" +
                "imgName='" + imgName + '\'' +
                '}';
    }
}
