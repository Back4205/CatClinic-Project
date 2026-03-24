<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Visit History | CatClinic</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/booking-history.css">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>

<%@include file="header.jsp" %>

<div class="container">
    <c:set var="activePage" value="history" />
    <%@include file="sidebar.jsp" %>

    <main class="content">
        <div class="page-header-actions">
            <div class="page-title">
                <h2>BOOKING HISTORY</h2>
                <p>MANAGEMENT LOG FOR MEDICAL AND GROOMING SERVICES.</p>
            </div>
            <a href="${pageContext.request.contextPath}/Booking" class="btn-new-booking">
                <i class="bi bi-plus-circle-fill"></i> New Booking
            </a>
        </div>

        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-icon icon-total"><i class="bi bi-clock-history"></i></div>
                <div class="stat-info">
                    <h3>${total}</h3>
                    <p>Total Visits</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-pending"><i class="bi bi-credit-card"></i></div>
                <div class="stat-info">
                    <h3>${pendingPaymentCount}</h3>
                    <p>Pending Payment</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-scheduled"><i class="bi bi-calendar-check"></i></div>
                <div class="stat-info">
                    <h3>${confirmedCount}</h3>
                    <p>Confirmed</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-completed"><i class="bi bi-check-circle-fill"></i></div>
                <div class="stat-info">
                    <h3>${completed}</h3>
                    <p>Completed</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-cancel-refund">
                    <i class="bi bi-exclamation-circle"></i>
                </div>
                <div class="stat-info">
                    <h3>${pendingCancelCount}</h3>
                    <p>Pending Refund</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-rejected">
                    <i class="bi bi-x-octagon-fill"></i>
                </div>
                <div class="stat-info">
                    <h3>${rejectedCancelCount}</h3>
                    <p>Rejected Refund</p>
                </div>
            </div>
        </div>

        <form action="booking-history#bookingSection" method="GET">
            <div class="filter-bar">
                <input type="text" name="search" class="search-input"
                       placeholder="Search cat name or service" value="${currentSearch}">
                <input type="date" name="dateFilter" class="date-input" value="${currentDate}">
                <select name="status" class="status-dropdown">
                    <option value="ALL" ${empty currentStatus || currentStatus == 'ALL' ? 'selected' : ''}>ALL STATUS</option>
                    <option value="PendingPayment" ${currentStatus == 'PendingPayment' ? 'selected' : ''}>PENDING PAYMENT</option>
                    <option value="Confirmed" ${currentStatus == 'Confirmed' ? 'selected' : ''}>CONFIRMED</option>
                    <option value="Completed" ${currentStatus == 'Completed' ? 'selected' : ''}>COMPLETED</option>
                    <option value="Cancelled" ${currentStatus == 'Cancelled' ? 'selected' : ''}>CANCELLED</option>
                    <option value="CancelRefund" ${currentStatus == 'CancelRefund' ? 'selected' : ''}>PENDING REFUND</option>
                    <option value="RejectedCancelRefund" ${currentStatus == 'RejectedCancelRefund' ? 'selected' : ''}>REJECTED REFUND</option>
                </select>

                <button type="submit" class="btn-search">Search</button>
            </div>
        </form>

        <div class="booking-list" id="bookingSection">
            <table class="booking-table">
                <thead>
                <tr>
                    <th>Cat</th>
                    <th>Day</th>
                    <th>Service</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="b" items="${bookingList}">
                    <tr>
                        <td>
                            <strong>${b.catName}</strong><br>
                            <span style="font-size:12px;color:#777;">${b.catBreed}</span>
                        </td>
                        <td>
                            ${b.appointmentDate}
                            <div style="font-size:12px;color:#777;">${b.appointmentTime}</div>
                        </td>
                        <td>
                            <i class="bi ${b.serviceType == 'Spa' ? 'bi-scissors' : 'bi-capsule'}"></i>
                            ${b.serviceName}
                        </td>
                        <td>
                            <fmt:formatNumber value="${b.price}" type="number"/> VND
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${b.status == 'PendingPayment'}">
                                    <span class="status-badge status-pendingpayment">Pending Payment</span>
                                </c:when>
                                <c:when test="${b.status == 'Confirmed'}">
                                    <span class="status-badge status-upcoming">Confirmed</span>
                                </c:when>
                                <c:when test="${b.status == 'Completed'}">
                                    <span class="status-badge status-completed">Completed</span>
                                </c:when>
                                <c:when test="${b.status == 'CancelRefund'}">
                                    <span class="status-badge status-cancelrefund">Pending Refund</span>
                                </c:when>
                                <c:when test="${b.status == 'Cancelled'}">
                                    <span class="status-badge status-cancelled">Cancelled</span>
                                </c:when>
                                <c:when test="${b.status == 'RejectedCancelRefund'}">
                                    <span class="status-badge status-rejected">Rejected Refund</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge">${b.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="booking-detail?id=${b.bookingID}" class="btn-view">Detail</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty bookingList}">
                <div class="empty-state-container">
                    <div class="empty-state-content">
                        <i class="bi bi-search-heart"></i>
                        <h3>No Bookings Found</h3>
                        <p>We couldn't find any booking history matching your search or filter.</p>
                        <a href="${pageContext.request.contextPath}/booking-history" class="btn-reset">Clear All Filters</a>
                    </div>
                </div>
            </c:if>
        </div>

        <c:if test="${totalPage > 1}">
            <div class="pagination-container">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="booking-history?page=${currentPage - 1}&search=${currentSearch}&status=${currentStatus}&dateFilter=${currentDate}#bookingSection"
                           class="page-item">&laquo;</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPage}" var="i">
                        <a href="booking-history?page=${i}&search=${currentSearch}&status=${currentStatus}&dateFilter=${currentDate}#bookingSection"
                           class="page-item ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPage}">
                        <a href="booking-history?page=${currentPage + 1}&search=${currentSearch}&status=${currentStatus}&dateFilter=${currentDate}#bookingSection" 
                           class="page-item">&raquo;</a>
                    </c:if>
                </div>
            </div>
        </c:if>
    </main>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>
</body>
</html>