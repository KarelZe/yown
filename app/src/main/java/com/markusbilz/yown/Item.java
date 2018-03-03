package com.markusbilz.yown;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
class Item {
    private final int id;
    private final int isNeeded;
    private final String dateOfCreation;
    private byte[] thumbnail;
    private String title;
    private String description;
    @SuppressWarnings("CanBeFinal")
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

}
