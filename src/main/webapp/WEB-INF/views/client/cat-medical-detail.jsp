<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/25/2026
  Time: 9:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Medical Detail | CatClinic</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/medical-detail.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
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
                <p>Detailed information about services and prescriptions.</p>
            </div>

            <button type="button"
                    class="btn-back"
                    onclick="window.location.href='${pageContext.request.contextPath}/cats/medical-history?catId=${catId}'">
                <i class="bi bi-arrow-left"></i>
                BACK TO MEDICAL HISTORY
            </button>
        </div>

        <div class="detail-wrapper">

            <c:if test="${empty medicalDetail}">
                <div class="empty-state">
                    <i class="bi bi-search" style="font-size: 40px;"></i>
                    <p>No medical detail found for this visit.</p>
                </div>
            </c:if>

            <c:if test="${not empty medicalDetail}">

                <!-- GENERAL INFO -->
                <section class="detail-section">
                    <h3 class="section-title">
                        <i class="bi bi-info-circle"></i> General Information
                    </h3>

                    <table class="detail-table info-table">
                        <tr>
                            <th>Doctor In Charge</th>
                            <td><strong>${medicalDetail.doctorName}</strong></td>
                        </tr>
                        <tr>
                            <th>Clinical Note</th>
                            <td>${medicalDetail.clinicalNote}</td>
                        </tr>
                    </table>
                </section>

                <!-- DRUGS -->
                <c:if test="${not empty drugList}">
                    <section class="detail-section">
                        <h3 class="section-title">
                            <i class="bi bi-capsule"></i> Prescribed Drugs
                        </h3>

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
                                    <td><strong>${d.drugName}</strong></td>
                                    <td>${d.instruction}</td>
                                    <td class="center">${d.unit}</td>
                                    <td class="center">${d.quantity}</td>
                                    <td class="right price-text">
                                        <strong>
                                            <fmt:formatNumber value="${d.price}" pattern="#,###" /> VND
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>

                <!-- SERVICES -->
                <c:if test="${not empty serviceList}">
                    <section class="detail-section">
                        <h3 class="section-title">
                            <i class="bi bi-activity"></i> Performed Services
                        </h3>

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
                                    <td><strong>${s.nameService}</strong></td>
                                    <td class="center">${s.timeService}</td>
                                    <td>${s.description}</td>
                                    <td class="right price-text">
                                        <strong>
                                            <fmt:formatNumber value="${s.price}" pattern="#,###" /> VND
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </c:if>
                <!-- TEST LAB -->
                <c:if test="${not empty staffWorks}">
                    <section class="detail-section">
                        <h3 class="section-title">
                            <i class="bi bi-beaker"></i> Laboratory Tests
                        </h3>

                        <table class="detail-table">
                            <thead>
                            <tr>
                                <th>Staff Name</th>
                                <th>Role</th>
                                <th>Test Name</th>
                                <th>Result Type</th>
                                <th>Result</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${staffWorks}" var="sw">
                                <tr>
                                    <td><strong>${sw.staffName}</strong></td>
                                    <td>${sw.staffRole}</td>
                                    <td>${sw.testName}</td>
                                    <td>${sw.resultName}</td>
                                    <td>${sw.result}</td>
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

</body>
</html>