<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/25/2026
  Time: 9:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cat Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/medical-detail.css">
</head>

<body>



<div class="detail-container">
    <h2 class="main-title">Medical Detail</h2>

    <c:if test="${empty medicalDetail}">
        <p class="empty-msg">No medical detail found for this visit.</p>
    </c:if>

    <c:if test="${not empty medicalDetail}">

        <table class="detail-table info-table">
            <c:if test="${not empty medicalDetail.doctorName}">
                <tr>
                    <th>Doctor In Charge</th>
                    <td style="font-weight: bold; color: #333;">Dr. ${medicalDetail.doctorName}</td>
                </tr>
            </c:if>

            <c:if test="${not empty medicalDetail.clinicalNote}">
                <tr>
                    <th>Clinical Note</th>
                    <td style="line-height: 1.6;">${medicalDetail.clinicalNote}</td>
                </tr>
            </c:if>
        </table>

        <c:if test="${not empty serviceList}">
            <h3>Performed Services</h3>
            <table class="detail-table">
                <thead>
                <tr>
                    <th>Service Name</th>
                    <th class="center">Time</th>
                    <th>Description</th>
                    <th class="right">Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${serviceList}" var="s">
                    <tr>
                        <td style="font-weight: 500;">${s.nameService}</td>
                        <td class="center">${s.timeService}</td>
                        <td>${s.description}</td>
                        <td class="right" style="color: #22a06b; font-weight: bold;">$${s.price}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:if test="${not empty drugList}">
            <h3>Prescribed Drugs</h3>
            <table class="detail-table">
                <thead>
                <tr>
                    <th>Drug Name</th>
                    <th>Instruction</th>
                    <th class="center">Unit</th>
                    <th class="center">Qty</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${drugList}" var="d">
                    <tr>
                        <td>${d.drugName}</td>
                        <td>${d.instruction}</td>
                        <td class="center">${d.unit}</td>
                        <td class="center">${d.quantity}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <c:set var="showStaffWork" value="true"/>
        <c:forEach items="${staffWorks}" var="w">
            <c:if test="${empty w.staffName or empty w.testName or empty w.resultImage}">
                <c:set var="showStaffWork" value="false"/>
            </c:if>
        </c:forEach>

        <c:if test="${showStaffWork}">
            <h3>Lab Results & Staff</h3>
            <table class="detail-table">
                <thead>
                <tr>
                    <th>Staff Name</th>
                    <th>Test Type</th>
                    <th class="center">Result Image</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${staffWorks}" var="w">
                    <tr>
                        <td>${w.staffName}</td>
                        <td>${w.testName}</td>
                        <td class="center">
                            <img src="${w.resultImage}" class="result-img" width="120" alt="Test Result"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

    </c:if>

    <button type="button" class="btn-back"
            onclick="window.location.href='${pageContext.request.contextPath}/cats'">
        ← Back to Cat List
    </button>
</div>
</body>
</html>