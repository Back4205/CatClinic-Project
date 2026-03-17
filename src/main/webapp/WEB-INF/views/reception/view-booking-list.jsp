<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<%@include file="header.jsp" %>

<div class="app-container">
    <c:set var="activePage" value="dashboard" scope="request"/>
    <%@include file="sidebar.jsp" %>

    <main class="main-content">
        <div class="top-bar">
            <a href="counter-booking" class="btn-new-booking">+ New Booking</a>
        </div>

        <div class="hub-header">
            <h2>Receptionist Hub</h2>
            <p>Welcome back, ${sessionScope.acc.fullName}. Here is your control center for today.</p>
        </div>

        <div class="stat-cards">
            <div class="stat-card card-orange">
                <div class="stat-icon icon-orange"><i class="fa-regular fa-clock"></i></div>
                <div class="stat-title">PENDING PAYMENT</div>
                <p class="stat-number">${stats['PendingPayment'] != null ? stats['PendingPayment'] : 0}</p>
            </div>
            <div class="stat-card card-sky">
                <div class="stat-icon icon-sky"><i class="fa-solid fa-clipboard-user"></i></div>
                <div class="stat-title">CONFIRMED</div>
                <p class="stat-number">${stats['Confirmed'] != null ? stats['Confirmed'] : 0}</p>
            </div>
            <div class="stat-card card-purple">
                <div class="stat-icon icon-purple"><i class="fa-solid fa-stethoscope"></i></div>
                <div class="stat-title">COMPLETED</div>
                <p class="stat-number">${stats['Completed'] != null ? stats['Completed'] : 0}</p>
            </div>
            <div class="stat-card card-green">
                <div class="stat-icon icon-green"><i class="fa-regular fa-circle-check"></i></div>
                <div class="stat-title">PENDING CANCEL</div>
                <p class="stat-number">${stats['PendingCancel'] != null ? stats['PendingCancel'] : 0}</p>
            </div>
            <div class="stat-card card-red">
                <div class="stat-icon icon-red"><i class="fa-solid fa-xmark"></i></div>
                <div class="stat-title">CANCELLED</div>
                <p class="stat-number">${stats['Cancelled'] != null ? stats['Cancelled'] : 0}</p>
            </div>
        </div>

        <div class="table-section" id="table-section">
            <div class="table-header">
                <h3>View Booking List</h3>
                <p>Overview of today's patient flow</p>
            </div>

            <form action="view-booking-list#table-section" method="GET">
                <div class="table-toolbar">
                    <input type="hidden" name="page" value="1">
                    <div class="table-search">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text" name="search" value="${currentSearch}" placeholder="Search by cat name, phone or owner...">
                    </div>
                    <div class="filter-bar">
                        <select name="status">
                            <option value="" ${empty currentStatus ? 'selected' : ''}>ALL STATUS</option>
                            <option value="PendingPayment" ${currentStatus == 'PendingPayment' ? 'selected' : ''}>PENDING PAYMENT</option>
                            <option value="Confirmed" ${currentStatus == 'Confirmed' ? 'selected' : ''}>CONFIRMED</option>
                            <option value="Completed" ${currentStatus == 'Completed' ? 'selected' : ''}>COMPLETED</option>
                            <option value="Cancelled" ${currentStatus == 'Cancelled' ? 'selected' : ''}>CANCELLED</option>
                        </select>
                        <button type="submit">Search</button>
                    </div>
                </div>
            </form>

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
                                    <p class="patient-name"><strong>${b.catName}</strong></p>
                                    <p class="patient-id">Owner: ${b.ownerName} - ${b.ownerPhone}</p>
                                        <%-- THÊM DÒNG SERVICE NAME Ở ĐÂY --%>
                                    <p style="color: #FF6B00; font-size: 12px; font-weight: 600; margin-top: 2px;">
                                        Service: ${empty b.serviceName ? 'General Check' : b.serviceName}
                                    </p>
                                </div>
                            </div>
                        </td>
                        <td>
                            <span class="date-text"><i class="fa-regular fa-calendar table-icon"></i> ${b.appointmentDate}</span><br>
                                <%-- ĐẢM BẢO HIỆN GIỜ KHÁM --%>
                            <span class="time-text"><i class="fa-regular fa-clock table-icon"></i> ${b.appointmentTime}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${fn:contains(b.status, 'PendingPayment')}"><span class="badge pendingpayment">${b.status}</span></c:when>
                                <c:when test="${fn:contains(b.status, 'Confirmed')}"><span class="badge confirmed">${b.status}</span></c:when>
                                <c:when test="${fn:contains(b.status, 'Completed')}"><span class="badge completed">${b.status}</span></c:when>
                                <c:when test="${fn:contains(b.status, 'PendingCancel')}"><span class="badge pendingcancelrefund">${b.status}</span></c:when>
                                <c:when test="${fn:contains(b.status, 'Cancelled')}"><span class="badge cancelled">${b.status}</span></c:when>
                                <c:otherwise><span class="badge">${b.status}</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="col-actions">
                            <a href="appointmentdetail?id=${b.bookingID}" class="btn-text">Details ></a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty bookingList}">
                    <tr><td colspan="4" class="empty-message">No appointments found.</td></tr>
                </c:if>
                </tbody>
            </table>

            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="view-booking-list?page=${currentPage - 1}&status=${currentStatus}&search=${currentSearch}#table-section">&laquo;</a>
                </c:if>

                <c:set var="p_begin" value="${currentPage - 1}" />
                <c:set var="p_end" value="${currentPage + 1}" />
                <c:if test="${p_begin < 1}"><c:set var="p_begin" value="1" /><c:set var="p_end" value="${totalPage < 3 ? totalPage : 3}" /></c:if>
                <c:if test="${p_end > totalPage}"><c:set var="p_end" value="${totalPage}" /><c:set var="p_begin" value="${totalPage - 2 > 0 ? totalPage - 2 : 1}" /></c:if>

                <c:forEach begin="${p_begin}" end="${p_end}" var="i">
                    <a href="view-booking-list?page=${i}&status=${currentStatus}&search=${currentSearch}#table-section" class="${i == currentPage ? 'active-page' : ''}">${i}</a>
                </c:forEach>

                <c:if test="${currentPage < totalPage}">
                    <a href="view-booking-list?page=${currentPage + 1}&status=${currentStatus}&search=${currentSearch}#table-section">&raquo;</a>
                </c:if>
            </div>
        </div>
    </main>
</div>
<%@include file="footer.jsp" %>
</body>
</html>