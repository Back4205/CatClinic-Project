/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.model;

import java.util.Date;

/**
 *
 * @author Vuong Bach
 */
public class MedicalHistoryDTO {

    private int bookingID;
    private Date visitDate;
    private String doctorName;
    private String clinicalNote;

    public MedicalHistoryDTO(int bookingID, Date visitDate, String doctorName, String clinicalNote) {
        this.bookingID = bookingID;
        this.visitDate = visitDate;
        this.doctorName = doctorName;
        this.clinicalNote = clinicalNote;
    }

    public MedicalHistoryDTO() {
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicalNote() {
        return clinicalNote;
    }

    public void setClinicalNote(String clinicalNote) {
        this.clinicalNote = clinicalNote;
    }

}
