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
    <meta charset="UTF-8">
    <title>Medical Detail | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/medical-detail.css">
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
                <h2>MEDICAL DETAIL</h2>
                <p>Detailed information about services, prescriptions, and lab results.</p>
            </div>
            <div class="top-action">
                <button type="button" class="btn-back" onclick="window.location.href='${pageContext.request.contextPath}//cats/medical-history?catId=${requestScope.catId}'">
                    <i class="bi bi-arrow-left"></i> BACK TO MEDICAL HISTORY
                </button>
            </div>
        </div>

        <div class="detail-wrapper">
            <c:if test="${empty medicalDetail}">
                <div class="empty-state">
                    <i class="bi bi-search" style="font-size: 40px; color: #94a3b8;"></i>
                    <p>No medical detail found for this visit.</p>
                </div>
            </c:if>

            <c:if test="${not empty medicalDetail}">
                <section class="detail-section">
                    <h3 class="section-title"><i class="bi bi-info-circle"></i> General Information</h3>
                    <table class="detail-table info-table">
                        <c:if test="${not empty medicalDetail.doctorName}">
                            <tr>
                                <th>Doctor In Charge</th>
                                <td class="highlight-text">${medicalDetail.doctorName}</td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty medicalDetail.clinicalNote}">
                            <tr>
                                <th>Clinical Note</th>
                                <td class="note-text">${medicalDetail.clinicalNote}</td>
                            </tr>
                        </c:if>
                    </table>
                </section>

                <c:if test="${not empty serviceList}">
                    <section class="detail-section">
                        <h3 class="section-title"><i class="bi bi-activity"></i> Performed Services</h3>
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
                                    <td class="semi-bold">${s.nameService}</td>
                                    <td class="center">${s.timeService}</td>
                                    <td>${s.description}</td>
                                    <td class="right price-text">${s.price} VND</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>

                <c:if test="${not empty drugList}">
                    <section class="detail-section">
                        <h3 class="section-title"><i class="bi bi-capsule"></i> Prescribed Drugs</h3>
                        <table class="detail-table">
                            <thead>
                            <tr>
                                <th>Drug Name</th>
                                <th>Instruction</th>
                                <th class="center">Unit</th>
                                <th class="center">Quantity</th>
                                <th class="right">Price</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${drugList}" var="d">
                                <tr>
                                    <td class="semi-bold">${d.drugName}</td>
                                    <td>${d.instruction}</td>
                                    <td class="center">${d.unit}</td>
                                    <td class="center">${d.quantity}</td>
                                    <td class="right price-text">${d.price} VND</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>

                <c:set var="showStaffWork" value="true"/>
                <c:forEach items="${staffWorks}" var="w">
                    <c:if test="${empty w.staffName or empty w.testName or empty w.resultImage}">
                        <c:set var="showStaffWork" value="false"/>
                    </c:if>
                </c:forEach>

                <c:if test="${showStaffWork}">
                    <section class="detail-section">
                        <h3 class="section-title"><i class="bi bi-flask"></i> Lab Results & Staff</h3>
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
                                    <td class="semi-bold">${w.staffName}</td>
                                    <td>${w.testName}</td>
                                    <td class="center">
                                        <div class="img-container">
                                            <img src="${w.resultImage}" class="result-img" alt="Test Result"/>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>
            </c:if>


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