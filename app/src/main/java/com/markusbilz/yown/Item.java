package com.markusbilz.yown;


class Item {
    private int id;
    private byte[] thumbnail;
    private String title;
    private String description;
    private int isNeeded;
    private String dateOfCreation;
    private String dateOfLastUsage;
    private String category;

    Item(int id, byte[] thumbnail, String title, String description, String category, int isNeeded, String dateOfCreation, String dateOfLastUsage) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isNeeded = isNeeded;
        this.dateOfCreation = dateOfCreation;
        this.dateOfLastUsage = dateOfLastUsage;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getIsNeeded() {
        return isNeeded;
    }

    public void setIsNeeded(int isNeeded) {
        this.isNeeded = isNeeded;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDateOfLastUsage() {
        return dateOfLastUsage;
    }

    public void setDateOfLastUsage(String dateOfLastUsage) {
        this.dateOfLastUsage = dateOfLastUsage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
