<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - Assigned Cases</title>

        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/AssignCaseStyle.css" rel="stylesheet" type="text/css"/>
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

                <div class="dashboard-content">

                    <!-- ===== TITLE ===== -->
                    <div class="page-title">
                        <h2>TREATMENT RECORD</h2>
                    </div>

                    <!-- ===== FILTER ===== -->
                    <form action="assignedCases" method="get">

                        <div class="case-header">

                            <div class="search-container">
                                <i class="fa fa-search"></i>
                                <input type="text" 
                                       name="keyword"
                                       value="${keyword}"
                                       placeholder="Search pet, owner, or ID...">
                            </div>

                            <div class="header-actions">
                                <select class="status-dropdown" name="status">
                                    <option value="">All Status</option>

                                    <option value="In Treatment"
                                            ${status == 'In Treatment' ? 'selected' : ''}>
                                        In Treatment
                                    </option>

                                    <option value="Completed"
                                            ${status == 'Completed' ? 'selected' : ''}>
                                        Completed
                                    </option>
                                </select>

                                <button type="submit" class="btn-plus">
                                    <i class="fa fa-search"></i>
                                </button>
                            </div>

                        </div>

                    </form>

                    <div class="table-container">
                        <table class="vet-table">
                            <thead>
                                <tr>
                                    <th>CASE ID</th>
                                    <th>PET & SPECIES</th>
                                    <th>OWNER INFO</th>
                                    <th>PRELIMINARY DIAGNOSIS</th>
                                    <th>DATE ASSIGNED</th>
                                    <th>STATUS</th>
                                    <th style="text-align: right;">ACTIONS</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="c" items="${assignedCases}">
                                    <tr>
                                        <td>CASE-${c.medicalRecordID}</td>

                                        <td>
                                            <div class="pet-cell">
                                                <div class="avatar">
                                                    ${c.catName.charAt(0)}
                                                </div>
                                                <div class="info">
                                                    <span class="name">${c.catName}</span>
                                                    <span class="sub">Cat • ${c.breed}</span>
                                                </div>
                                            </div>
                                        </td>

                                        <td>
                                            <div class="info">
                                                <span class="name">${c.ownerName}</span>
                                                <span class="sub">${c.phone}</span>
                                            </div>
                                        </td>

                                        <td>${c.diagnosis}</td>

                                        <td>${c.appointmentDate}</td>

                                        <td>
                                            <span class="badge ${c.status.toLowerCase()}">
                                                ${c.status}
                                            </span>
                                        </td>

                                        <td class="actions-cell">
                                            <a href="EmrController?medicalRecordID=${c.medicalRecordID}" 
                                               class="btn-emr">
                                                <i class="fa-regular fa-file-lines"></i> EMR
                                            </a>
                                            <a href="xray?medicalRecordID=${c.medicalRecordID}"
                                               class="btn-view">
                                                <i class="fa-solid fa-flask icon-action"></i>
                                            </a>
                                            <a href="bloodtest?medicalRecordID=${c.medicalRecordID}"
                                               class="btn-view">
                                                <i class="fa-solid fa-table-cells-large icon-action"></i>
                                            </a>
                                                <a href="preController?medicalRecordID=${c.medicalRecordID}"
                                               class="btn-view">
                                                <i class="fa-solid fa-pills"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                </div>

                <!-- ===== PAGINATION ===== -->
                <div class="pagination">

                    <c:set var="startPage" value="${currentPage - 1}" />
                    <c:set var="endPage" value="${currentPage + 1}" />

                    <!-- Fix nếu ở đầu -->
                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1" />
                        <c:set var="endPage" value="3" />
                    </c:if>

                    <!-- Fix nếu ở cuối -->
                    <c:if test="${endPage > totalPages}">
                        <c:set var="endPage" value="${totalPages}" />
                        <c:set var="startPage" value="${totalPages - 2}" />
                    </c:if>

                    <!-- Tránh startPage < 1 -->
                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1" />
                    </c:if>

                    <!-- Nút << -->
                    <c:if test="${startPage > 1}">
                        <a href="assignedCases?page=${startPage - 1}&status=${statusFilter}">
                            &laquo;
                        </a>
                    </c:if>

                    <!-- Hiển thị tối đa 3 trang -->
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="assignedCases?page=${i}&status=${status}&keyword=${keyword}"
                           class="${i == currentPage ? 'active-page' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <!-- Nút >> -->
                    <c:if test="${endPage < totalPages}">
                        <a href="assignedCases?page=${endPage + 1}&status=${status}">
                            &raquo;
                        </a>
                    </c:if>

                </div>

            </main>
        </div>

    </body>
</html>