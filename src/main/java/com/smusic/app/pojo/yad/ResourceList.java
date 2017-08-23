package com.smusic.app.pojo.yad;

import java.util.Arrays;

/**
 * Created by sergey on 20.08.17.
 */
public class ResourceList {
    private Resource[] items;
    private String path;
    private int total;
    private int offset;

    public Resource[] getItems() {
        return items;
    }

    public void setItems(Resource[] items) {
        this.items = items;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ResourceList{" +
                "items=" + Arrays.toString(items) +
                ", path='" + path + '\'' +
                ", total=" + total +
                ", offset=" + offset +
                '}';
    }


}