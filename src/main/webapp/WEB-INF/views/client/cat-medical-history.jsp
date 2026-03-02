<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/24/2026
  Time: 8:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

    <c:if test="${sessionScope.acc != null && sessionScope.acc.roleID == 5}">
        <%@include file="sidebar.jsp" %>
    </c:if>


    <main class="content">
        <div class="page-header">
            <div class="page-title">
                <h2>MEDICAL HISTORY</h2>
                <p>View all past medical records and diagnoses.</p>
            </div>
        </div>

        <div class="filter-section">
            <form action="${pageContext.request.contextPath}/cats/medical-history" method="GET" class="search-form">
                <input type="hidden" name="catId" value="${catId}">

                <div class="filter-inputs">
                    <div class="filter-group">
                        <label><i class="bi bi-calendar3"></i> Date</label>
                        <input type="date" name="searchDate" value="${param.searchDate}" class="form-input">
                    </div>

                    <div class="filter-group">
                        <label><i class="bi bi-person-badge"></i> Doctor</label>
                        <input type="text" name="searchDoctor" placeholder="Enter doctor name..."
                               value="${param.searchDoctor}" class="form-input">
                    </div>
                </div>

                <div class="filter-actions">
                    <button type="submit" class="btn-search">
                        <i class="bi bi-search"></i> SEARCH
                    </button>

                </div>
            </form>
        </div>

        <div class="table-wrapper">
            <table class="medical-table">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Doctor</th>
                    <th>Diagnosis & Clinical Notes</th>
                    <th class="text-center">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${medicalHistoryList}" var="h">
                    <tr>
                        <td class="date-cell">
                            <fmt:formatDate value="${h.visitDate}" pattern="dd/MM/yyyy"/>
                        </td>
                        <td class="doctor-cell">
                            <i class="bi bi-person-circle"></i>
                            <strong>${h.doctorName}</strong>
                        </td>
                        <td class="note-cell">${h.clinicalNote}</td>
                        <td class="text-center">
                            <a href="${pageContext.request.contextPath}/cats/medical-detail?idBooking=${h.bookingID}&catId=${catId}"
                               class="btn-view-detail">
                                VIEW DETAIL
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty medicalHistoryList}">
                    <tr>
                        <td colspan="4">
                            <div class="empty-state">
                                <i class="bi bi-clipboard-x"></i>
                                <p>No medical records found for the selected criteria.</p>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <a href="${pageContext.request.contextPath}/cats/medical-history?catId=${catId}&page=${currentPage-1}&searchDate=${param.searchDate}&searchDoctor=${param.searchDoctor}"
                       class="${currentPage == 1 ? 'disabled' : ''}">
                        PREV
                    </a>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="${pageContext.request.contextPath}/cats/medical-history?catId=${catId}&page=${i}&searchDate=${param.searchDate}&searchDoctor=${param.searchDoctor}"
                           class="${i == currentPage ? 'active' : ''}">
                                ${i}
                        </a>
                    </c:forEach>

                    <a href="${pageContext.request.contextPath}/cats/medical-history?catId=${catId}&page=${currentPage+1}&searchDate=${param.searchDate}&searchDoctor=${param.searchDoctor}"
                       class="${currentPage == totalPages ? 'disabled' : ''}">
                        NEXT
                    </a>
                </div>
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