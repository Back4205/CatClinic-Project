package com.mycompany.catclinicproject.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private String banner;
    public  String description;
    public  boolean isActive;
    public Category() {

    }
    public Category(int categoryId, String categoryName, String banner, String description, boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.banner = banner;
        this.description = description;
        this.isActive = isActive;

    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getBanner() {
        return banner;
    }
    public void setBanner(String banner) {
        this.banner = banner;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
   public boolean isActive() {
        return isActive;
   }
   public void setActive(boolean active) {
        isActive = active;
   }
}
