package com.mycompany.catclinicproject.model;

import java.time.LocalDateTime;
import java.util.Date;

public class MedicalRecordDetailView {
    private int bookingID;
    private int medicalRecordID;
    private Date visitDate;
    private String doctorName;
    private String diagnosis;
    private String clinicalNote;
    public MedicalRecordDetailView() {
    }
    public MedicalRecordDetailView( int bookingID,int medicalRecordID, Date visitDate, String doctorName, String diagnosis, String clinicalNote) {
        this.bookingID = bookingID;
        this.medicalRecordID = medicalRecordID;
        this.visitDate = visitDate;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.clinicalNote = clinicalNote;
    }
    public MedicalRecordDetailView( int medicalRecordID, Date visitDate, String doctorName, String diagnosis, String clinicalNote) {

        this.medicalRecordID = medicalRecordID;
        this.visitDate = visitDate;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.clinicalNote = clinicalNote;
    }
    public int getBookingID() {
        return bookingID;
    }
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
    public int getMedicalRecordID() {
        return medicalRecordID;
    }
    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
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
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getClinicalNote() {
        return clinicalNote;
    }
    public void setClinicalNote(String clinicalNote) {
        this.clinicalNote = clinicalNote;
    }

}
