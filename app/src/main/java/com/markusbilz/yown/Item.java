package com.markusbilz.yown;


class Item {
    private String title;
    private String description;
    private int id;
    private boolean isNeeded;

    Item(int id, String title, String description, int isNeeded) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isNeeded = isNeeded % 2 == 0;
    }

    public boolean isNeeded() {
        return isNeeded;
    }

    public void setNeeded(boolean needed) {
        isNeeded = needed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
