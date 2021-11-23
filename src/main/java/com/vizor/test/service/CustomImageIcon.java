package com.vizor.test.service;

import javax.swing.*;
import java.io.File;

public class CustomImageIcon extends ImageIcon{
    private String name;

    public CustomImageIcon(File file) {
        super(file.toString());
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
