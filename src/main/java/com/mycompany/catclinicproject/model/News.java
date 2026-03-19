/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class News {
    private int newsId;
    private String banner;
    private String title;
    private String description;
    private Date createdDate;
    private boolean isActive;

    public News() {
    }

    public News(int newsId, String banner, String title, String description, Date createdDate, boolean isActive) {
        this.newsId = newsId;
        this.banner = banner;
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    
    
}
