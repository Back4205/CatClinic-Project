<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
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
                    <p>PendingPayment</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon icon-scheduled"><i class="bi bi-calendar-check"></i></div>
                <div class="stat-info">
                    <h3>${scheduled}</h3>
                    <p>Scheduled</p>
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
                <div class="stat-icon icon-cancelled"><i class="bi bi-x-circle"></i></div>
                <div class="stat-info">
                    <h3>${cancelledCount}</h3>
                    <p>Cancelled</p>
                </div>
            </div>
        </div>

        <form action="booking-history" method="GET">
            <div class="filter-bar">

                <input type="text" name="search" class="search-input"
                       placeholder="Search cat name or service..."
                       value="${currentSearch}">

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
        </form>

        <div class="booking-list">

            <table class="booking-table">
                <thead>
                <tr>
                    <th>Cat</th>
                    <th>Day</th>
                    <th>Service</th>
                    <th>Price Total</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="b" items="${bookingList}">
                    <tr>
                        <!-- CAT -->
                        <td>
                            <strong>${b.catName}</strong><br>
                            <span style="font-size:12px;color:#777;">${b.catBreed}</span>
                        </td>

                        <!-- DAY -->
                        <td>
                                ${b.appointmentDate}
                            <div style="font-size:12px;color:#777;">
                                    ${b.appointmentTime}
                            </div>
                        </td>

                        <!-- SERVICE -->
                        <td>
                            <i class="bi ${b.serviceType == 'Spa' ? 'bi-scissors' : 'bi-capsule'}"></i>
                                ${b.serviceName}
                        </td>

                        <!-- PRICE -->
                        <td>
                            <fmt:formatNumber value="${b.price}" type="number"/> VND
                        </td>

                        <!-- STATUS -->
                        <td>
                        <span class="status-badge
                            ${b.status == 'Completed' || b.status == 'Done' ? 'status-completed' : ''}
                            ${b.status == 'Confirmed' || b.status == 'Upcoming' || b.status == 'Pending' ? 'status-upcoming' : ''}
                            ${b.status == 'Cancelled' ? 'status-cancelled' : ''}
                            ${b.status == 'In Progress' ? 'status-inprogress' : ''}
                            ${b.status == 'PendingPayment' ? 'status-pendingpayment' : ''}">
                                ${b.status == 'PendingPayment' ? 'PendingPayment' : b.status}
                        </span>
                        </td>

                        <!-- ACTION -->
                        <td>
                            <a href="booking-history?action=detail&id=${b.bookingID}" class="btn-view">
                                Detail
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

            <c:if test="${empty bookingList}">
                <div class="empty-state">
                    <i class="bi bi-inbox"></i>
                    <p>No booking history found.</p>
                </div>
            </c:if>

        </div>
        <c:if test="${totalPage > 1}">
            <div class="pagination">


                <c:if test="${currentPage > 1}">
                    <a href="booking-history?page=${currentPage - 1}&search=${currentSearch}&status=${currentStatus}"
                       class="page-btn">
                        Prev
                    </a>
                </c:if>


                <c:forEach begin="1" end="${totalPage}" var="i">
                    <a href="booking-history?page=${i}&search=${currentSearch}&status=${currentStatus}"
                       class="page-btn ${i == currentPage ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>


                <c:if test="${currentPage < totalPage}">
                    <a href="booking-history?page=${currentPage + 1}&search=${currentSearch}&status=${currentStatus}"
                       class="page-btn">
                        Next
                    </a>
                </c:if>

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