package com.vizor.test.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {

    public static <T> void doPaginated(Collection<T> fullList, Integer pageSize, Page<T> pageInterface) {

        final List<T> list = new ArrayList<T>(fullList);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
            pageSize = list.size();
        }

        final int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
        for (int pageNum = 0; pageNum < numPages; ) {
            final List<T> page = list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size()));
            pageInterface.run(page);
        }
    }

    public interface Page<T> {
        void run(List<T> item);
    }
}
