<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - Dashboard</title>
        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet" type="text/css"/>
        <link href="css/headerVeteStyle.css" rel="stylesheet" type="text/css"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    </head>
    <body>

        <div class="layout">
            <jsp:include page="sidebar.jsp"/>
            <!-- MAIN -->

            <main class="main">

                <!-- HEADER -->

                <jsp:include page="header.jsp"/>

                <div class="dashboard-content">

                    <!-- ===== STATUS CARDS ===== -->

                    <!-- ===== FILTER ===== -->
                    <div class="filter-bar">
                        <form method="get" action="DashboardController" class="filter-form">

                            <div class="search-box">
                                <i class="fa-solid fa-magnifying-glass"></i>
                                <input type="text"
                                       name="keyword"
                                       placeholder="Search pet, owner, or ID..."
                                       value="${keyword}">
                            </div>

                            <div class="date-group">
                                <label>From</label>
                                <input type="date" name="dateFrom" value="${dateFrom}">
                            </div>

                            <div class="date-group">
                                <label>To</label>
                                <input type="date" name="dateTo" value="${dateTo}">
                            </div>

                            <button type="submit" class="filter-btn">Filter</button>

                        </form>
                    </div>


                    <!-- ===== MAIN GRID ===== -->
                    <div class="main-grid">

                        <!-- LEFT -->
                        <div class="case-table-card">
                            <h3>Assigned Cases</h3>

                            <table>
                                <thead>
                                    <tr>
                                        <th>Case ID</th>
                                        <th>Pet Name</th>
                                        <th>Owner</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty assignList}">
                                            <c:forEach var="c" items="${assignList}">
                                                <tr>
                                                    <td>${c.bookingID}</td>
                                                    <td>${c.catName}</td>
                                                    <td>${c.fullName}</td>
                                                    <td class="action-buttons">
                                                        <a href="addemr?&bookingID=${c.bookingID}" 
                                                           class="btn-start">
                                                            Start
                                                        </a>
                                                        <a href="DashboardController?bookingID=${c.bookingID}&page=${currentPage}&dateFrom=${dateFrom}&dateTo=${dateTo}" 
                                                           class="btn-detail">
                                                            Detail
                                                        </a>

                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="4" style="text-align:center">
                                                    No cases found
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- RIGHT -->
                        <div class="case-detail-card">
                            <c:choose>
                                <c:when test="${not empty selectedCase}">
                                    <h3 class="detail-title">Case Detail</h3>

                                    <div class="detail-row">
                                        <span class="label">Booking ID</span>
                                        <span class="value">${selectedCase.bookingID}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Owner</span>
                                        <span class="value">${selectedCase.ownerName}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Phone</span>
                                        <span class="value">${selectedCase.phone}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Pet</span>
                                        <span class="value">${selectedCase.catName}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Breed</span>
                                        <span class="value">${selectedCase.breed}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Appointment</span>
                                        <span class="value">${selectedCase.appointmentDate}</span>
                                    </div>

                                    <div class="detail-row">
                                        <span class="label">Status</span>
                                        <span class="value status-badge">
                                            ${selectedCase.status}
                                        </span>
                                    </div>

                                    <div class="note-box">
                                        <span class="label">Note</span>
                                        <p>${selectedCase.note}</p>
                                    </div>

                                </c:when>

                                <c:otherwise>
                                    <h3 class="detail-title">Case Detail Panel</h3>
                                    <p class="empty-text">Select a case from the table to view details</p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </div>
                </div>
                <!-- PAGINATION -->
                <div class="pagination">

                    <c:if test="${currentPage > 1}">
                        <a href="DashboardController?page=${currentPage - 1}&dateFrom=${dateFrom}&dateTo=${dateTo}&keyword=${keyword}">
                            &laquo;
                        </a>
                    </c:if>

                    <c:set var="startPage" value="${currentPage - 1}" />
                    <c:set var="endPage" value="${currentPage + 1}" />

                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1"/>
                        <c:set var="endPage" value="${startPage + 2}"/>
                    </c:if>

                    <c:if test="${endPage > totalPages}">
                        <c:set var="endPage" value="${totalPages}"/>
                        <c:set var="startPage" value="${endPage - 2 > 0 ? endPage - 2 : 1}"/>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="DashboardController?page=${i}&dateFrom=${dateFrom}&dateTo=${dateTo}&keyword=${keyword}"
                           class="${i == currentPage ? 'active-page' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="DashboardController?page=${currentPage + 1}&dateFrom=${dateFrom}&dateTo=${dateTo}&keyword=${keyword}">
                            &raquo;
                        </a>
                    </c:if>

                </div>
        </div>

        <!-- DETAIL PANEL -->

    </div>

</main>
</div>

</body>
</html>