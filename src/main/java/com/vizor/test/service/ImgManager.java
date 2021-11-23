package com.vizor.test.service;

import com.vizor.test.component.IconLabel;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ImgManager {

    public ImgManager() {
        reloadCollection();
    }

    private List<IconLabel> imageCollection = new ArrayList<IconLabel>();

    public void reloadCollection(File directory) {
        if (directory.isDirectory()) {
            imageCollection = new ArrayList<IconLabel>();
            if (directory.isDirectory()) {
                File[] iFiles = directory.listFiles();
                for (File iFile : iFiles) {
                    imageCollection.add(new IconLabel(iFile));
                }
            }
        }
    }

    public void reloadCollection() {
        reloadCollection(new File("assets"));
    }


    public List<IconLabel> getByImgName(String name) {
        return imageCollection.stream().filter(i -> i.getName().contains(name)).collect(Collectors.toList());
    }

    public void add(IconLabel imageIcon) {
        imageCollection.add(imageIcon);
    }

    public void delete(IconLabel imageIcon) {
        imageCollection.remove(imageIcon);
    }

    public List<IconLabel> getCollection() {
        return imageCollection;
    }

    public void sortCollection() {
        imageCollection.sort(Comparator.comparing(IconLabel::getName));
    }

    public void reversedCollection() {
        imageCollection.sort(Comparator.comparing(IconLabel::getName).reversed());
    }
}
