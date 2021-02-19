package com.system.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PaginationAPI<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1L;
    private int pageSize;

    public PaginationAPI(int pageSize) {
        this(pageSize, (List) (new ArrayList()));
    }

    public PaginationAPI(int pageSize, T... objects) {
        this(pageSize, Arrays.asList(objects));
    }

    public PaginationAPI(int pageSize, List<T> objects) {
        this.pageSize = pageSize;
        this.addAll(objects);
    }

    public int pageSize() {
        return this.pageSize;
    }

    public int totalPages() {
        return (int) Math.ceil((double) this.size() / (double) this.pageSize);
    }

    public boolean exists(int page) {
        return page >= 0 && page < this.totalPages();
    }

    public List<T> getPage(int page) {
        if (page >= 0 && page < this.totalPages()) {
            List<T> objects = new ArrayList();
            int min = page * this.pageSize;
            int max = page * this.pageSize + this.pageSize;
            if (max > this.size()) {
                max = this.size();
            }

            for (int i = min; max > i; ++i) {
                objects.add(this.get(i));
            }

            return objects;
        } else {
            return null;
        }
    }
}
