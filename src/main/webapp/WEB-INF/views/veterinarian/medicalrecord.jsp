<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - EMR</title>
        <link href="css/MedicalRecordStyle.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/EMRStyle.css" rel="stylesheet">

        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    </head>

    <body>
        <div class="layout">

            <!-- SIDEBAR -->
            <jsp:include page="sidebar.jsp"/>

            <!-- MAIN -->
            <main class="main">

                <!-- HEADER -->
                <jsp:include page="header.jsp"/>

                <div class="emr-container">

                    <!-- Back -->
                    <div class="back-link">
                        <a href="assignedCases">
                            <i class="fa fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                 

                    <!-- ===== MEDICAL RECORD CARD ===== -->
                    <div class="emr-card">

                        <div class="emr-header">
                            <div>
                                <h1>MEDICAL RECORD</h1>
                                <span class="ref">
                                    REF: CASE-${emr.medicalRecordID} |
                                    DATE: ${emr.appointmentDate}
                                </span>
                            </div>

                            <div class="clinic-info">
                                <span class="clinic-label">CLINIC</span>
                                <span class="clinic-name">VetCare Pro Center</span>
                            </div>
                        </div>

                        <!-- link medical record history -->
                        <div class="btn-wrapper">
                            <a href="${pageContext.request.contextPath}/cats/medical-history?catId=${catId}"
                               class="complete-btn">
                                Medical Record History
                            </a>
                        </div>
                        <!-- ===== PATIENT & OWNER ===== -->
                        <div class="emr-info">

                            <!-- PATIENT -->
                            <div class="info-box">
                                <h4>PATIENT DETAILS</h4>
                                <div class="info-grid">
                                    <div>
                                        <label>Name</label>
                                        <p>${emr.catName}</p>
                                    </div>
                                    <div>
                                        <label>Species</label>
                                        <p>Cat</p>
                                    </div>
                                    <div>
                                        <label>Breed</label>
                                        <p>${emr.breed}</p>
                                    </div>
                                    <div>
                                        <label>Age</label>
                                        <p>${emr.age} years</p>
                                    </div>
                                    <div>
                                        <label>Booking Note</label>
                                        <p>${emr.note}</p>
                                    </div>
                                </div>
                            </div>

                            <!-- OWNER -->
                            <div class="info-box">
                                <h4>OWNER DETAILS</h4>
                                <div class="info-grid">
                                    <div>
                                        <label>Owner Name</label>
                                        <p>${emr.ownerName}</p>
                                    </div>
                                    <div>
                                        <label>Contact Phone</label>
                                        <p>${emr.phone}</p>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <hr class="divider"/>

                        <!-- ===== CLINICAL SECTION ===== -->
                         <div class="step-nav">

                            <a href="EmrController?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 1 ? 'active' : ''}">
                                <span class="step-number">1</span>
                                <span class="step-label">EXAM</span>
                            </a>

                            <a href="xray?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 2 ? 'active' : ''}">
                                <span class="step-number">2</span>
                                <span class="step-label">X-RAY</span>
                            </a>
                                
                                <a href="bloodtest?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 3 ? 'active' : ''}">
                                <span class="step-number">3</span>
                                <span class="step-label">BLOOD TEST</span>
                            </a>

                            <a href="preController?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 4 ? 'active' : ''}">
                                <span class="step-number">4</span>
                                <span class="step-label">PRESCRIPTION</span>
                            </a>

                        </div>
                        
                        <form action="EmrController" method="post">
                            
                            <input type="hidden" name="medicalRecordID" 
                                   value="${emr.medicalRecordID}"/>
                            <div class="form-grid">

                                <div class="form-group">
                                    <label>Symptoms & Observations</label>
                                    <textarea name="symptoms"
                                              placeholder="Enter observed symptoms...">${emr.symptoms}</textarea>
                                </div>

                                <div class="form-group">
                                    <label>Diagnosis</label>
                                    <textarea name="diagnosis"
                                              placeholder="Enter final or working diagnosis...">${emr.diagnosis}</textarea>
                                </div>

                                <div class="form-group">
                                    <label>Treatment Protocol</label>
                                    <textarea name="treatmentPlan"
                                              placeholder="Describe treatment steps...">${emr.treatmentPlan}</textarea>
                                </div>

                            </div>

                            <div class="form-actions">
                                 <c:if test="${status ne 'Completed'}">
                                <button type="submit"  class="btn-save " name="button" value="save">
                                    Save 
                                </button>
                                
                                <button type="submit"  class="btn-complete " name="button" value="completed">
                                    Completed
                                </button>
                                 </c:if>
                                <button type="submit" class="btn-next" name="button" value="next">
                                    Next <i class="fa fa-arrow-right"></i> X-Ray
                                </button>
                            </div>
                                
                                
                        </form>

                    </div>
                </div>
            </main>
        </div>
    </body>
</html>