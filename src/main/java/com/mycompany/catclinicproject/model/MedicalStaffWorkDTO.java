package com.mycompany.catclinicproject.model;

public class MedicalStaffWorkDTO {
    private String staffName;
    private String staffRole;

    private String testName;
    private String resultImage;

    public MedicalStaffWorkDTO() {
    }

    public MedicalStaffWorkDTO(String staffName, String staffRole, String testName, String resultImage) {
        this.staffName = staffName;
        this.staffRole = staffRole;
        this.testName = testName;
        this.resultImage = resultImage;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public String getTestName() {
        return testName;
    }

    public String getResultImage() {
        return resultImage;
    }
    }


