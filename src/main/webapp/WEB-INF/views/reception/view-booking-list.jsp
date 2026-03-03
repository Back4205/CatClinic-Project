<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">--%>
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<%@include file="header.jsp" %>

<div class="app-container">
    <c:set var="activePage" value="dashboard" scope="request" />
    <%@include file="sidebar.jsp" %>

    <main class="main-content">

        <!-- TOP BAR -->
        <div class="top-bar">
            <a href="counter-booking" class="btn-new-booking">+ New Booking</a>
        </div>

        <div class="hub-header">
            <h2>Receptionist Hub</h2>
            <p>Welcome back, ${sessionScope.acc.fullName}. Here is your control center for today.</p>
        </div>

        <!-- STAT CARDS -->
        <div class="stat-cards">
            <div class="stat-card card-blue">
                <div class="stat-icon icon-blue"><i class="fa-regular fa-calendar-plus"></i></div>
                <div class="stat-title">COUNTER BOOKING</div>
                <p class="stat-number">0</p>
            </div>

            <div class="stat-card card-orange">
                <div class="stat-icon icon-orange"><i class="fa-regular fa-clock"></i></div>
                <div class="stat-title">PENDING</div>
                <p class="stat-number">${stats['Pending'] != null ? stats['Pending'] : 0}</p>
            </div>

            <div class="stat-card card-sky">
                <div class="stat-icon icon-sky"><i class="fa-solid fa-clipboard-user"></i></div>
                <div class="stat-title">WAITING</div>
                <p class="stat-number">${stats['Waiting'] != null ? stats['Waiting'] : 0}</p>
            </div>

            <div class="stat-card card-purple">
                <div class="stat-icon icon-purple"><i class="fa-solid fa-stethoscope"></i></div>
                <div class="stat-title">IN TREATMENT</div>
                <p class="stat-number">${stats['In Treatment'] != null ? stats['In Treatment'] : 0}</p>
            </div>

            <div class="stat-card card-green">
                <div class="stat-icon icon-green"><i class="fa-regular fa-circle-check"></i></div>
                <div class="stat-title">COMPLETED</div>
                <p class="stat-number">${stats['Completed'] != null ? stats['Completed'] : 0}</p>
            </div>

            <div class="stat-card card-red">
                <div class="stat-icon icon-red"><i class="fa-solid fa-xmark"></i></div>
                <div class="stat-title">CANCELLED</div>
                <p class="stat-number">${stats['Cancelled'] != null ? stats['Cancelled'] : 0}</p>
            </div>
        </div>

        <!-- TABLE SECTION -->
        <div class="table-section">

            <div class="table-header">
                <h3>View Booking List</h3>
                <p>Overview of today's patient flow</p>
            </div>

            <form action="view-booking-list" method="GET">
                <div class="table-toolbar">

                    <div class="table-search">
                        <input type="hidden" name="page" value="1">

                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text" name="search"
                               value="${currentSearch}"
                               placeholder="Search by cat name, phone or owner...">
                    </div>

                    <div class="filter-bar">
                        <div class="filter-buttons">

                            <button type="submit" name="status" value="ALL"
                                    class="${empty currentStatus || currentStatus == 'ALL' ? 'active' : ''}">
                                ALL STATUS
                            </button>

                            <button type="submit" name="status" value="PendingPayment"
                                    class="${currentStatus == 'PendingPayment' ? 'active' : ''}">
                                PENDING PAYMENT
                            </button>

                            <button type="submit" name="status" value="Completed"
                                    class="${currentStatus == 'Completed' ? 'active' : ''}">
                                COMPLETED
                            </button>

                            <button type="submit" name="status" value="Confirmed"
                                    class="${currentStatus == 'Confirmed' ? 'active' : ''}">
                                CONFIRMED
                            </button>

                            <button type="submit" name="status" value="Cancelled"
                                    class="${currentStatus == 'Cancelled' ? 'active' : ''}">
                                CANCELLED
                            </button>

                        </div>
                    </div>

                </div>
            </form>

            <!-- TABLE -->
            <table class="modern-table">
                <thead>
                <tr>
                    <th>Patient</th>
                    <th>Date & Time</th>
                    <th>Status</th>
                    <th class="col-actions">Actions</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${bookingList}" var="b">
                    <tr>
                        <td>
                            <div class="patient-cell">
                                <div class="cat-avatar">🐱</div>
                                <div>
                                    <p class="patient-name">${b.catName}</p>
                                    <p class="patient-id">Owner: ${b.customerName} - ${b.customerPhone}</p>
                                </div>
                            </div>
                        </td>

                        <td>
                            <span class="date-text">
                                <i class="fa-regular fa-calendar table-icon"></i>
                                ${b.appointmentDate}
                            </span><br>
                            <span class="time-text">
                                <i class="fa-regular fa-clock table-icon"></i>
                                ${b.appointmentTime}
                            </span>
                        </td>

                        <td>
                            <span class="badge ${b.status.toLowerCase().replace(' ', '-')}">
                                    ${b.status}
                            </span>
                        </td>

                        <td class="col-actions">

                            <c:if test="${b.status == 'Pending'}">
                                <a href="update-status?id=${b.bookingID}&status=Confirmed"
                                   class="btn-text action-confirm">
                                    Confirm
                                </a>
                            </c:if>

                            <c:if test="${b.status == 'Confirmed'}">
                                <a href="update-status?id=${b.bookingID}&status=Waiting"
                                   class="btn-text action-checkin">
                                    Check-in
                                </a>
                            </c:if>

                            <a href="appointment-detail?id=${b.bookingID}"
                               class="btn-text">
                                Details >
                            </a>

                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty bookingList}">
                    <tr>
                        <td colspan="4" class="empty-message">
                            No appointments found.
                        </td>
                    </tr>
                </c:if>

                </tbody>
            </table>

            <!-- PAGINATION -->
            <c:if test="${totalPage > 1}">
                <div class="pagination-wrapper">
                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a href="view-booking-list?page=${currentPage - 1}&status=${currentStatus}&search=${currentSearch}"
                               class="page-btn">«</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPage}" var="i">
                            <a href="view-booking-list?page=${i}&status=${currentStatus}&search=${currentSearch}"
                               class="page-btn ${i == currentPage ? 'active-page' : ''}">
                                    ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPage}">
                            <a href="view-booking-list?page=${currentPage + 1}&status=${currentStatus}&search=${currentSearch}"
                               class="page-btn">»</a>
                        </c:if>

                    </div>
                </div>
            </c:if>

        </div>
    </main>
</div>

<%@include file="footer.jsp" %>
</body>
</html>