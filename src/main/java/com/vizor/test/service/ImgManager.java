package com.vizor.test.service;

import com.vizor.test.component.IconLabel;
import com.vizor.test.util.ListUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ImgManager {

    private File rootDirectory;

    public ImgManager() {
        rootDirectory = new File("assets");
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
        reloadCollection(rootDirectory);
    }


    public List<IconLabel> searchImages(String name) {
        return imageCollection.stream().filter(i -> i.getImgName().contains(name)).collect(Collectors.toList());
    }

    public void addImageFile(File iFile) {
        try {
            Files.copy(iFile.toPath(), new File(rootDirectory.getAbsolutePath() + File.separator + iFile.getName()).toPath());
            reloadCollection();
            JOptionPane.showMessageDialog(null, "Image has been successfully added to collection!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't add this image!");
        }

    }

    public void delete(IconLabel imageIcon) {
        imageCollection.remove(imageIcon);
    }

    public List<IconLabel> getCollection() {
        return imageCollection;
    }

    public List<List<IconLabel>> doPaginatedCollection(int pageSize){
        return doPaginatedCollection(getCollection(), pageSize);
    }

    public List<List<IconLabel>> doPaginatedCollection(List<IconLabel> iconLabels, int pageSize){
        List<List<IconLabel>> list = new ArrayList<>();
        ListUtils.doPaginated(iconLabels, pageSize, x -> {
            list.add(x);
        });
        return list;
    }

    public void sortCollection() {
        imageCollection.sort(Comparator.comparing(IconLabel::getName));
    }

    public void reversedCollection() {
        imageCollection.sort(Comparator.comparing(IconLabel::getName).reversed());
    }
}
