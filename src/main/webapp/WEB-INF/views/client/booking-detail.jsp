<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Booking Detail | Tone Cam</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/booking-detail.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <c:set var="activePage" value="history" />
    <%@include file="sidebar.jsp" %>

    <main class="content">
        <div class="detail-container">
            <div class="detail-header-card">
                <div class="pet-profile">
                    <div class="pet-avatar">🐱</div>
                    <div>
                        <h2 style="margin:0; font-size: 24px;">${booking.catName}</h2>
                        <span>Booking ID: #${booking.bookingID}</span>
                    </div>
                </div>
                <div style="text-align: right;">
                    <span class="status-badge" style="background: rgba(255,255,255,0.2); color: white; border: 1px solid white;">
                        ${booking.status == 'PendingPayment' ? 'UNPAID' : booking.status}
                    </span>
                    <p style="margin: 5px 0 0 0; font-size: 13px;">Created: Today</p>
                </div>
            </div>

            <div class="detail-section">
                <div class="section-title"><i class="bi bi-info-circle-fill"></i> Visit Information</div>
                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-label">APPOINTMENT DATE</span>
                        <span class="info-value">${booking.appointmentDate}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">TIME SLOT</span>
                        <span class="info-value">${booking.appointmentTime}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">SERVICE TYPE</span>
                        <span class="info-value">${booking.serviceName}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">TOTAL PRICE</span>
                        <span class="info-value" style="color: var(--primary-orange); font-size: 18px;">
                            <fmt:formatNumber value="${booking.price}" type="number"/> VND
                        </span>
                    </div>
                </div>
            </div>

            <div class="detail-section">
                <div class="section-title"><i class="bi bi-chat-left-text-fill"></i> Additional Details</div>
                <div class="info-grid" style="margin-bottom: 20px;">
                    <div class="info-item">
                        <span class="info-label">VETERINARIAN</span>
                        <span class="info-value">${empty booking.vetName ? 'Assigning...' : booking.vetName}</span>
                    </div>
                </div>
                <div class="info-label">CLIENT NOTES</div>
                <div class="notes-container">
                    <p class="notes-text">"${empty booking.note ? 'No special requests for this visit.' : booking.note}"</p>
                </div>
            </div>

            <div class="btn-action-group">
                <a href="booking-history" class="btn-custom btn-back-history">Back to History</a>

                <c:choose>

                        <c:when test="${booking.status == 'Confirmed'}">
                            <a href="cancel-booking?id=${booking.bookingID}" class="btn-custom btn-cancel">
                                <i class="bi bi-trash"></i> Cancel Booking
                            </a>
                        </c:when>

                    <c:when test="${booking.status == 'PendingPayment'}">
                        <a href="${pageContext.request.contextPath}//vnpay?bookingID=${booking.bookingID}" class="btn-custom btn-pay">
                            <i class="bi bi-credit-card"></i> Pay Deposit Now
                        </a>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </main>
</div>
</body>
</html>