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
                        <div class="step-nav">

                            <a href="EmrController?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 1 ? 'active' : ''}">
                                <span class="step-number">1</span>
                                <span class="step-label">EXAM</span>
                            </a>



                            <a href="xray?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 3 ? 'active' : ''}">
                                <span class="step-number">2</span>
                                <span class="step-label">X-RAY</span>
                            </a>
                            <a href="bloodtest?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 2 ? 'active' : ''}">
                                <span class="step-number">3</span>
                                <span class="step-label">BLOOD TEST</span>
                            </a>

                            <a href="preController?medicalRecordID=${emr.medicalRecordID}"
                               class="step-item ${activeStep == 4 ? 'active' : ''}">
                                <span class="step-number">4</span>
                                <span class="step-label">PRESCRIPTION</span>
                            </a>

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

                                <form action="bloodtest" method="post" id="xrayForm">
                                    <input type="hidden" name="action" id="actionField">
                                    <input type="hidden" name="medicalRecordID"
                                           value="${emr.medicalRecordID}"/>
                                    <div class="lab-request-box">

                                        <c:choose>
                                            <c:when test="${sessionScope.status ne 'Completed'}">
                                                 <button type="button"
                                                        class="btn-request"
                                                        onclick="openPopup()">
                                                    Request Blood Test

                                                </div>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="no-request">No request</span>
                                            </c:otherwise>
                                        </c:choose>


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
                <div id="popupForm" class="popup-overlay">
                    <div class="popup-box">
                        <h3>Enter Result Name</h3>

                        <input type="text" name="resultName" id="resultName" placeholder="Enter result name..." required/>

                        <div class="popup-actions">
                            <button type="button" onclick="submitPopup()" class="btn-confirm">Submit</button>
                            <button type="button" onclick="closePopup()" class="btn-cancel">Cancel</button>
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
            function openPopup() {
                document.getElementById("popupForm").style.display = "flex";
            }

            function closePopup() {
                document.getElementById("popupForm").style.display = "none";
            }

            function submitPopup() {
                let resultName = document.getElementById("resultName").value.trim();

                if (resultName === "") {
                    alert("Please enter result name!");
                    return;
                }

                let form = document.getElementById("xrayForm");

                document.getElementById("actionField").value = "request";

                let input = document.createElement("input");
                input.type = "hidden";
                input.name = "resultName";
                input.value = resultName;

                form.appendChild(input);

                form.submit();
            }
        </script>
    </body>
</html>