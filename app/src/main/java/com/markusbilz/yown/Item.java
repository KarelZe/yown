package com.markusbilz.yown;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
class Item {
    private final int id;
    private final String uuidNfc;
    private final String dateOfCreation;
    private byte[] thumbnail;
    private String title;
    private String description;
    @SuppressWarnings("CanBeFinal")
    private String dateOfLastUsage;
    private String category;

    /**
     * @param id              primary key of item in database
     * @param thumbnail       thumbnail image of item
     * @param title           title of item
     * @param description     description of item
     * @param category        category of item
     * @param uuidNfc         unique identifier stored on nfc tag
     * @param dateOfCreation  initial date of creation
     * @param dateOfLastUsage date of last usage or update
     */
    Item(int id, byte[] thumbnail, String title, String description, String category, String uuidNfc, String dateOfCreation, String dateOfLastUsage) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.category = category;
        this.uuidNfc = uuidNfc;
        this.dateOfCreation = dateOfCreation;
        this.dateOfLastUsage = dateOfLastUsage;
    }

    byte[] getThumbnail() {
        return thumbnail;
    }

    void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    String getCategory() {
        return category;
    }

    void setCategory(String category) {
        this.category = category;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    int getId() {
        return id;
    }

    public String getDateOfLastUsage() {
        return dateOfLastUsage;
    }
}
