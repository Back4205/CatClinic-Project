<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - EMR</title>

        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/MedicalRecordStyle.css" rel="stylesheet" type="text/css"/>
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
                        <!-- ===== TEST LAB SECTION ===== -->
                        <div class="section-title">
                            <div class="step-circle">2</div>
                            <h2>Blood Test Request</h2>
                        </div>

                        <c:choose>

                            <c:when test="${not empty testOrders}">

                                <div class="lab-request-box">
                                    <h3>Blood Test Information</h3>

                                    <c:forEach var="t" items="${testOrders}">
                                        <div class="info-box">
                                            <p><strong>Test Name:</strong> ${t.testName}</p>
                                            <p><strong>Status:</strong> ${t.status}</p>
                                            <p><strong>Result:</strong> 
                                                <c:choose>
                                                    <c:when test="${not empty t.result}">
                                                        ${t.result}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Waiting for result...
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                        <hr/>
                                    </c:forEach>
                                </div>

                            </c:when>

                            <c:otherwise>

                                <form action="bloodtest" method="post">
                                    <input type="hidden" name="action" id="actionField">
                                    <input type="hidden" name="medicalRecordID"
                                           value="${emr.medicalRecordID}"/>

                                    <div class="lab-request-box">

                                        <div class="lab-icon">
                                            <i class="fa-solid fa-heart-pulse"></i>
                                        </div>

                                        <h3>Do you want to request a Blood Test?</h3>

                                        <div class="lab-actions">

                                            <button type="button"
                                                    class="btn-skip"
                                                    onclick="confirmAction('skip')">
                                                No, Skip
                                            </button>

                                            <button type="button"
                                                    class="btn-request"
                                                    onclick="confirmAction('request')">
                                                Yes, Request Blood Test
                                            </button>

                                        </div>

                                    </div>

                                </form>

                            </c:otherwise>

                        </c:choose>
                        <!-- FLOAT NAVIGATION BUTTONS -->
                        <div class="floating-nav">

                            <!-- Previous -->
                            <a href="xray?medicalRecordID=${emr.medicalRecordID}"
                               class="btn-floating btn-prev">
                                <i class="fa fa-arrow-left"></i> Previous
                            </a>
                            <!-- Save & Next -->
                            <form action="preController" method="get">
                                <input type="hidden" name="medicalRecordID"
                                       value="${emr.medicalRecordID}" />
                                <input type="hidden" name="action" value="saveNext" />

                                <button type="submit" class="btn-floating btn-next">
                                    Next <i class="fa fa-arrow-right"></i> Prescription
                                </button>
                            </form>

                        </div>
                    </div>

                </div>

            </main>
        </div>
                                <script>
function confirmAction(actionType) {

    let message = "";

    if (actionType === "skip") {
        message = "Are you sure you want to skip blood test?";
    } else if (actionType === "request") {
        message = "Are you sure you want to request a blood test?";
    }

    if (confirm(message)) {
        document.getElementById("actionField").value = actionType;
        document.querySelector("form").submit();
    }
}
</script>
    </body>
</html>