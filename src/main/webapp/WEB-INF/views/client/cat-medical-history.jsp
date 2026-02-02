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
    <title>Medical History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/medical-history.css">
</head>

<body>

<div class="medical-history-container">
    <h2 class="medical-title">Medical History</h2>

    <table class="medical-table">
        <thead>
        <tr>
            <th>Date</th>
            <th>Doctor</th>
            <th>Diagnosis</th>
            <th style="text-align: center;">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${medicalHistoryList}" var="h">
            <tr>
                <td style="font-weight: 600; color: #333;">${h.visitDate}</td>
                <td>${h.doctorName}</td>
                <td style="max-width: 350px; line-height: 1.5;">${h.clinicalNote}</td>
                <td style="text-align: center;">
                    <c:choose>
                        <c:when test="${not empty h.bookingID}">
                            <a href="${pageContext.request.contextPath}/cats/medical-detail?idBooking=${h.bookingID}"
                               class="btn-view">
                                VIEW DETAIL
                            </a>
                        </c:when>
                        <c:otherwise>
                            <span class="no-detail">No Detail</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty medicalHistoryList}">
            <tr>
                <td colspan="4" style="text-align: center; padding: 40px; color: #999;">
                    No medical records found for this pet.
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<button type="button" class="btn-back"
        onclick="window.location.href='${pageContext.request.contextPath}/cats'">
    ← Back to Cat List
</button>

</body>
</html>