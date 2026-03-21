<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lab Management | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lab-management.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

</head>
<body class="lab-body">
<%@include file="header.jsp" %>

<div style="display: flex; min-height: 100vh; background-color: #f8f9fa;">

    <div style="width: 300px; flex-shrink: 0; background-color: #ffffff; border-right: 1px solid #e5e7eb;">
        <c:set var="activePage" value="lab-hub" scope="request"/>
        <%@include file="sidebar.jsp" %>
    </div>

    <div style="flex-grow: 1; overflow-x: hidden;">

        <div class="lab-main-wrapper" style="max-width: 1200px; margin: 40px auto; padding: 0 20px;">
            <header class="lab-welcome">
                <h1>Laboratory Management</h1>
                <p>Welcome back, Technician Alex</p>
            </header>

            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon orange"><i class="fas fa-clipboard-list"></i></div>
                    <div class="stat-info">
                        <span class="label">Total Requests</span>
                        <div class="value">${empty stats['Total'] ? 0 : stats['Total']}</div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background: #fff7ed; color: #f97316;"><i class="fa-solid fa-hourglass-start"></i></div>
                    <div class="stat-info">
                        <span class="label">Pending</span>
                        <div class="value">${empty stats['Pending'] ? 0 : stats['Pending']}</div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon blue"><i class="fas fa-flask"></i></div>
                    <div class="stat-info">
                        <span class="label">In Testing</span>
                        <div class="value">${empty stats['InProgress'] ? 0 : stats['InProgress']}</div>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon green"><i class="fas fa-check-circle"></i></div>
                    <div class="stat-info">
                        <span class="label">Completed</span>
                        <div class="value">${empty stats['Completed'] ? 0 : stats['Completed']}</div>
                    </div>
                </div>
            </div>

            <div class="queue-card">
                <div class="queue-header">
                    <h3><i class="fas fa-clock"></i> Laboratory Queue</h3>
                </div>

                <div class="filter-tabs">
                    <a href="lab-hub?status=All"
                       class="filter-btn ${currentStatus == 'All' ? 'active' : ''}">
                        All
                    </a>

                    <a href="lab-hub?status=Pending"
                       class="filter-btn ${currentStatus == 'Pending' ? 'active' : ''}">
                        Pending
                    </a>

                    <a href="lab-hub?status=In-progress"
                       class="filter-btn ${currentStatus == 'In-progress' ? 'active' : ''}">
                        In Progress
                    </a>

                    <a href="lab-hub?status=Completed"
                       class="filter-btn ${currentStatus == 'Completed' ? 'active' : ''}">
                        Completed
                    </a>
                </div>

                <table class="lab-table">
                    <thead>
                    <tr>
                        <th>CAT NAME</th>
                        <th>TEST TYPE</th>
                        <th>ASSIGNED DOCTOR</th>
                        <th>STATUS</th>
                        <th>ACTION</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach items="${labQueue}" var="t">
                        <tr>
                            <td class="cat-cell">
                                <span class="avatar"
                                      style="width: 35px; height: 35px; background: #ffe0b2; color: #e65100; border-radius: 8px; display: inline-flex; align-items: center; justify-content: center; font-weight: 900; margin-right: 10px;">
                                        ${t.catName.substring(0,1)}
                                </span>
                                <strong>${t.catName}</strong>
                            </td>
                            <td>${t.testName}</td>
                            <td>Dr. ${t.doctorName}</td>
                            <td>
                                <span class="badge ${t.status.toLowerCase().replace(' ', '-')}">
                                        ${t.status}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.status == 'Pending'}">
                                        <a href="${pageContext.request.contextPath}/technician/update-test?id=${t.testOrderID}&status=In-progress&filter=${currentStatus}"
                                           class="btn btn-orange">
                                            Start Test
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/ReportResultController?id=${t.testOrderID}" class="btn btn-blue">
                                            Report Result
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty labQueue}">
                        <tr>
                            <td colspan="5" style="text-align:center; padding:20px;">
                                No records found.
                            </td>
                        </tr>
                    </c:if>

                    </tbody>
                </table>

                <c:if test="${totalPage > 1}">
                    <div style="margin-top:20px; text-align:center;">

                        <c:if test="${currentPage > 1}">
                            <a href="lab-hub?page=${currentPage - 1}&status=${currentStatus}"
                               class="filter-btn">
                                «
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPage}" var="i">
                            <a href="lab-hub?page=${i}&status=${currentStatus}"
                               class="filter-btn ${i == currentPage ? 'active' : ''}">
                                    ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPage}">
                            <a href="lab-hub?page=${currentPage + 1}&status=${currentStatus}"
                               class="filter-btn">
                                »
                            </a>
                        </c:if>

                    </div>
                </c:if>

            </div>
        </div>
    </div>
</div>
<%@include file="footer.jsp" %>
</body>
</html>