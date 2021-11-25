package com.vizor.test.component;

import javax.swing.*;

public class ViewImgLabel extends JLabel {
    private static ViewImgLabel viewImgLabel = null;

    private ViewImgLabel() {
    }

    public static ViewImgLabel getInstance() {
        if (viewImgLabel == null) {
            viewImgLabel = new ViewImgLabel();
            return viewImgLabel;
        }
        return viewImgLabel;
    }
}
