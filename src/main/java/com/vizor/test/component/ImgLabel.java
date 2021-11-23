package com.vizor.test.component;

import javax.swing.*;

public class ImgLabel extends JLabel {
    private static ImgLabel imgLabel = null;

    private ImgLabel() {
    }

    public static ImgLabel getInstance() {
        if (imgLabel == null) {
            imgLabel = new ImgLabel();
            return imgLabel;
        }
        return imgLabel;
    }
}
