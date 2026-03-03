<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - Test Orders</title>

        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/EMRStyle.css" rel="stylesheet">
        <link href="css/MedicalRecordStyle.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
        <link href="css/TestRequestStyle.css" rel="stylesheet" type="text/css"/>

    </head>

    <body>
        <div class="layout">

            <jsp:include page="sidebar.jsp"/>

            <main class="main">
                <jsp:include page="header.jsp"/>

                <div class="emr-container">
                    <div class="page-title">
                        <h2>REQUEST TEST LAB</h2>
                    </div>
                    <div class="filter-bar">
                        <form action="testlist" method="get">

                            <!-- Search -->
                            <input type="text"
                                   name="keyword"
                                   placeholder="Search owner or cat..."
                                   value="${param.keyword}" />

                            <!-- Filter Status -->
                            <select name="status">
                                <option value="">All Status</option>
                                <option value="Pending"
                                        ${param.status == 'Pending' ? 'selected' : ''}>
                                    Pending
                                </option>
                                <option value="Completed"
                                        ${param.status == 'Completed' ? 'selected' : ''}>
                                    Completed
                                </option>
                            </select>

                            <button type="submit">Search</button>
                        </form>
                    </div>
                    <div class="emr-card">
                        <table class="custom-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Owner Name</th>
                                    <th>Cat</th>
                                    <th>Test Name</th>
                                    <th>Status</th>
                                    <th>Result</th>
                                </tr>
                            </thead>

                            <tbody>

                                <!-- Nếu không có dữ liệu -->
                                <c:if test="${empty testList}">
                                    <tr>
                                        <td colspan="5" style="text-align:center;">
                                            No test orders found.
                                        </td>
                                    </tr>
                                </c:if>

                                <!-- Nếu có dữ liệu -->
                                <c:forEach var="t" items="${testList}">
                                    <tr>
                                        <td>${t.testOrderID}</td>
                                        <td>${t.fullName}</td>
                                        <td>${t.catName}</td>
                                        <td>${t.testName}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${t.status eq 'Completed'}">
                                                    <span class="status-done">
                                                        ${t.status}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status-pending">
                                                        ${t.status}
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${t.testName eq 'X-Ray'}">
                                                    <a href="xray?medicalRecordID=${t.medicalRecordID}"
                                                       class="btn-view">
                                                        <i class="fa fa-eye"></i>
                                                    </a>
                                                </c:when>

                                                <c:otherwise>
                                                    <a href="bloodtest?medicalRecordID=${t.medicalRecordID}"
                                                       class="btn-view">
                                                        <i class="fa fa-eye"></i>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </tbody>
                        </table>

                        <!-- ===== PAGINATION ===== -->


                    </div>
                    <div class="pagination">

                        <c:set var="startPage" value="${currentPage - 1}" />
                        <c:set var="endPage" value="${currentPage + 1}" />

                        <c:if test="${startPage < 1}">
                            <c:set var="startPage" value="1" />
                            <c:set var="endPage" value="3" />
                        </c:if>

                        <c:if test="${endPage > totalPages}">
                            <c:set var="endPage" value="${totalPages}" />
                            <c:set var="startPage" value="${totalPages - 2}" />
                        </c:if>

                        <c:if test="${startPage < 1}">
                            <c:set var="startPage" value="1" />
                        </c:if>

                        <c:if test="${startPage > 1}">
                            <a href="testlist?page=${startPage - 1}&status=${statusFilter}&keyword=${keyword}">
                                &laquo;
                            </a>
                        </c:if>

                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <a href="testlist?page=${i}&status=${statusFilter}&keyword=${keyword}"
                               class="${i == currentPage ? 'active-page' : ''}">
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${endPage < totalPages}">
                            <a href="testlist?page=${endPage + 1}&status=${statusFilter}&keyword=${keyword}">
                                &raquo;
                            </a>
                        </c:if>

                    </div>
                </div>
            </main>
        </div>
    </body>
</html>