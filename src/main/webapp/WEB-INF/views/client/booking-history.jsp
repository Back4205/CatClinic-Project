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
                <div class="stat-icon icon-cancelled"><i class="bi bi-x-circle"></i></div>
                <div class="stat-info">
                    <h3>${cancelledCount}</h3>
                    <p>Cancelled</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon icon-cancel">
                    <i class="bi bi-exclamation-circle"></i>
                </div>
                <div class="stat-info">
                    <h3>${pendingCancelCount}</h3>
                    <p>Pending Cancel</p>
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
                    <button type="submit" name="status" value="PendingCancel"
                            class="${currentStatus == 'PendingCancel' ? 'active' : ''}">
                        PENDING CANCEL
                    </button>

                </div>
            </div>
        </form>

        <div class="booking-list" id="bookingSection">

            <table class="booking-table">
                <thead>
                <tr>
                    <th>Cat</th>
                    <th>Day</th>
                    <th>Service</th>
                    <th>Price At Booking</th>
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

                                <c:when test="${b.status == 'PendingCancel'}">
                                    <span class="status-badge status-pendingcancel">Pending Cancel</span>
                                </c:when>

                                <c:when test="${b.status == 'Cancelled'}">
                                    <span class="status-badge status-cancelled">Cancelled</span>
                                </c:when>

                            </c:choose>
                        </td>

                        <!-- ACTION -->
                        <td>
                            <a href="booking-detail?id=${b.bookingID}" class="btn-view">Detail</a>
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
            <div class="pagination-container">
                <div class="pagination">

                        <%-- 1. Nút Prev --%>
                    <c:if test="${currentPage > 1}">
                        <a href="booking-history?page=${currentPage - 1}&search=${currentSearch}&status=${currentStatus}#bookingSection"
                           class="page-item">
                            &laquo;
                        </a>
                    </c:if>

                        <%-- 2. THUẬT TOÁN TRƯỢT SỐ TRANG (Max hiển thị 3 trang) --%>
                    <c:set var="maxVisible" value="3" />
                    <c:set var="startPage" value="${currentPage - 1}" />
                    <c:set var="endPage" value="${currentPage + 1}" />

                        <%-- Xử lý nếu lùi quá trang 1 --%>
                    <c:if test="${startPage < 1}">
                        <c:set var="startPage" value="1" />
                        <c:set var="endPage" value="${startPage + maxVisible - 1}" />
                    </c:if>

                        <%-- Xử lý nếu tiến quá tổng số trang --%>
                    <c:if test="${endPage > totalPage}">
                        <c:set var="endPage" value="${totalPage}" />
                        <c:set var="startPage" value="${endPage - maxVisible + 1}" />
                        <c:if test="${startPage < 1}">
                            <c:set var="startPage" value="1" />
                        </c:if>
                    </c:if>

                        <%-- 3. In các nút số dựa theo khoảng start - end vừa tính --%>
                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <a href="booking-history?page=${i}&search=${currentSearch}&status=${currentStatus}#bookingSection"
                           class="page-item ${i == currentPage ? 'active' : ''}">
                                ${i}
                        </a>
                    </c:forEach>

                        <%-- 4. Nút Next --%>
                    <c:if test="${currentPage < totalPage}">
                        <a href="booking-history?page=${currentPage + 1}&search=${currentSearch}&status=${currentStatus}#bookingSection" class="page-item">
                            &raquo;
                        </a>
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