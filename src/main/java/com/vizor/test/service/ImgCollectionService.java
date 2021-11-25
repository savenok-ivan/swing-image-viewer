package com.vizor.test.service;

import com.vizor.test.component.ImgIconLabel;
import com.vizor.test.util.ListUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ImgCollectionService {
    private static ImgCollectionService imgCollectionService = null;
    private List<ImgIconLabel> imageCollection = new ArrayList<ImgIconLabel>();
    private File rootDirectory;

    private ImgCollectionService() {
        rootDirectory = new File("assets");
        reloadCollection();
    }

    public static ImgCollectionService getInstance() {
        if (imgCollectionService == null) {
            imgCollectionService = new ImgCollectionService();
            return imgCollectionService;
        }
        return imgCollectionService;
    }

    public void reloadCollection() {
        if (rootDirectory.isDirectory()) {
            imageCollection = new ArrayList<ImgIconLabel>();
            if (rootDirectory.isDirectory()) {
                File[] iFiles = rootDirectory.listFiles();
                for (File iFile : iFiles) {
                    imageCollection.add(new ImgIconLabel(iFile));
                }
            }
        }
    }

    public List<List<ImgIconLabel>> filterCollectionByName(String name, int pageSize) {
        List<ImgIconLabel> imgIconLabels = imageCollection.stream().filter(i -> i.getImgName().contains(name)).collect(Collectors.toList());
        return doPaginatedCollection(imgIconLabels, pageSize);
    }

    public void addImgFile(File iFile) {
        try {
            Files.copy(iFile.toPath(), new File(rootDirectory.getAbsolutePath() + File.separator + iFile.getName()).toPath());
            reloadCollection();
            JOptionPane.showMessageDialog(null, "Image has been successfully added to collection!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't add this image!");
        }
    }

    public List<List<ImgIconLabel>> doPaginatedCollection(int pageSize) {
        return doPaginatedCollection(imageCollection, pageSize);
    }

    public List<List<ImgIconLabel>> doPaginatedCollection(List<ImgIconLabel> imgIconLabels, int pageSize) {
        List<List<ImgIconLabel>> list = new ArrayList<>();
        ListUtils.doPaginated(imgIconLabels, pageSize, x -> {
            list.add(x);
        });
        return list;
    }

    public void selectRootDirectory(File directory) {
        if (directory.isDirectory()) {
            rootDirectory = directory;
            reloadCollection();
        } else {
            JOptionPane.showMessageDialog(null, "Error defining a new root directory!");
        }
    }

    public void sortCollection() {
        imageCollection.sort(Comparator.comparing(ImgIconLabel::getImgName));
    }

    public void reversedCollection() {
        imageCollection.sort(Comparator.comparing(ImgIconLabel::getImgName).reversed());
    }
}
