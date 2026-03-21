/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

/**
 *
 * @author Son
 */
public class PageInfo {
    private int page;
    private int pageSize;
    private int totalRecord;

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public int getTotalPage() {
        return (int) Math.ceil((double) totalRecord / pageSize);
    }

    // getter / setter


}
