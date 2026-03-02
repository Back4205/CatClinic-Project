<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/24/2026
  Time: 8:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Medical History | CatClinic</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/medical-history.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>

<body>

<%@include file="header.jsp" %>

<div class="container">

    <c:set var="activePage" value="cats" />
    <%@include file="sidebar.jsp" %>

    <main class="content">
        <div class="page-header">
            <div class="page-title">
                <h2>MEDICAL HISTORY</h2>
                <p>View all past medical records and diagnoses.</p>
            </div>
        </div>

        <div class="table-wrapper">
            <table class="medical-table">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Doctor</th>
                    <th>Diagnosis & Clinical Notes</th>
                    <th style="text-align: center;">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${medicalHistoryList}" var="h">
                    <tr>
                        <td class="date-cell">${h.visitDate}</td>
                        <td class="doctor-cell">
                            <i class="bi bi-person-badge"></i> ${h.doctorName}
                        </td>
                        <td class="note-cell">${h.clinicalNote}</td>
                        <td style="text-align: center;">
                            <c:choose>
                                <c:when test="${not empty h.bookingID}">
                                    <a href="${pageContext.request.contextPath}/cats/medical-detail?idBooking=${h.bookingID}"
                                       class="btn-view-detail">
                                        VIEW DETAIL
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-no-detail">N/A</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty medicalHistoryList}">
                    <tr>
                        <td colspan="4">
                            <div class="empty-state">
                                <i class="bi bi-folder2-open" style="font-size: 40px;"></i>
                                <p>No medical records found for this pet.</p>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </main>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>

</body>
</html>