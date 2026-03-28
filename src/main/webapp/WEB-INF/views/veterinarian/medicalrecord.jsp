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
                            <input type="hidden" name="status" 
                                   value="${status}"/>
                            <div class="form-grid">
                                <div class="form-group">
                                    <label>Symptoms & Observations</label>
                                    <textarea name="symptoms"
                                              placeholder="Enter observed symptoms..."
                                              ${status eq 'Completed' ? 'readonly' : ''}>${emr.symptoms}</textarea>
                                </div>

                                <div class="form-group">
                                    <label>Diagnosis</label>
                                    <textarea name="diagnosis"
                                              placeholder="Enter final or working diagnosis..."
                                              ${status eq 'Completed' ? 'readonly' : ''}>${emr.diagnosis}</textarea>

                                </div>
                                <div class="form-group">
                                    <label>Treatment Protocol</label>
                                    <textarea name="treatmentPlan"
                                              placeholder="Describe treatment steps..."
                                              ${status eq 'Completed' ? 'readonly' : ''}>${emr.treatmentPlan}</textarea>
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
                <c:if test="${not empty sessionScope['toast-messenger']}">
                    <div id="toast-message">
                        ${sessionScope['toast-messenger']}
                    </div>
                    <c:remove var="toast-messenger" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope['toast-messenger-complete']}">
                    <div id="bubble-overlay">
                        <div id="bubble-box">
                            <div class="bubble-icon">
                                ✔
                            </div>
                            <p class="bubble-text">
                                ${sessionScope['toast-messenger-complete']}
                            </p>
                            <button id="bubble-continue-btn">
                                Continue
                            </button>
                        </div>
                    </div>
                    <c:remove var="toast-messenger-complete" scope="session"/>
                </c:if>
            </main>
        </div>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const toast = document.getElementById("toast-message");
                if (toast) {
                    setTimeout(function () {
                        toast.style.opacity = "0";
                        setTimeout(function () {
                            toast.remove();
                        }, 500);
                    }, 5000);
                }
            });
            document.addEventListener("DOMContentLoaded", function () {
                const btn = document.getElementById("bubble-continue-btn");
                if (btn) {
                    btn.addEventListener("click", function () {
                        window.location.href = "EmrController?medicalRecordID=${param.medicalRecordID}";
                    });
                }

            });
            document.addEventListener("DOMContentLoaded", function () {

                const btnComplete = document.querySelector("button[value='completed']");
                const btnSave = document.querySelector("button[value='save']");

                const symptoms = document.querySelector("textarea[name='symptoms']");
                const diagnosis = document.querySelector("textarea[name='diagnosis']");
                const treatment = document.querySelector("textarea[name='treatmentPlan']");

                // Khi bấm COMPLETE → bật required
                if (btnComplete) {
                    btnComplete.addEventListener("click", function () {
                        symptoms.required = true;
                        diagnosis.required = true;
                        treatment.required = true;
                    });
                }

                // Khi bấm SAVE → tắt required
                if (btnSave) {
                    btnSave.addEventListener("click", function () {
                        symptoms.required = false;
                        diagnosis.required = false;
                        treatment.required = false;
                    });
                }
            });
        </script>
    </body>
</html>