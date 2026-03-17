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

            <c:if test="${booking.checkOutTime != null}">                <div class="detail-section" style="margin-top: 25px; border-top: 1px dashed #FFE8D9; padding-top: 25px;">
                    <div class="section-title"><i class="bi bi-star-fill" style="color: #FF6B00;"></i> Service Feedback</div>

                    <c:choose>
                        <c:when test="${empty feedback}">
                            <div class="feedback-banner">
                                <div class="feedback-banner-left">
                                    <div class="feedback-icon-wrapper">
                                        <i class="bi bi-star-fill"></i>
                                    </div>
                                    <div class="feedback-text">
                                        <h4>We value your feedback!</h4>
                                        <p>How was your experience with ${booking.catName} today?</p>
                                    </div>
                                </div>
                                <button type="button" class="btn-rate-visit" onclick="document.getElementById('rateModal').style.display='flex'">
                                    Rate This Visit
                                </button>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="feedback-result-box">
                                <div class="feedback-score-section">
                                    <div class="feedback-stars">
                                        <c:forEach begin="1" end="${feedback.rating}">★</c:forEach><c:forEach begin="${feedback.rating + 1}" end="5">☆</c:forEach>
                                    </div>
                                    <span class="feedback-score-text">${feedback.rating}.0 / 5</span>
                                </div>
                                <div class="feedback-comment-section">
                                    <c:choose>
                                        <%-- Nếu có bình luận thì in ra --%>
                                        <c:when test="${not empty feedback.comment}">
                                            <p>"${feedback.comment}"</p>
                                        </c:when>
                                        <%-- Nếu không có bình luận thì hiện chữ mờ --%>
                                        <c:otherwise>
                                            <p style="color: #94a3b8; opacity: 0.7;">No additional comments.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <div class="btn-action-group">
                <c:if test="${not empty booking.checkOutTime}">
                    <a href="view-invoice?id=${booking.bookingID}" class="btn-custom btn-invoice">
                        <i class="bi bi-receipt"></i> View Invoice
                    </a>
                </c:if>
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

<div id="rateModal" class="rate-modal-overlay" style="display:none;">
    <div class="rate-modal-content">
        <span class="rate-modal-close" onclick="document.getElementById('rateModal').style.display='none'">&times;</span>

        <h3 class="rate-modal-title">Rate Appointment</h3>
        <p class="rate-modal-subtitle">How was ${booking.catName}'s visit?</p>

        <form action="submit-feedback" method="POST">
            <input type="hidden" name="bookingID" value="${booking.bookingID}">

            <div class="star-rating-input">
                <input type="radio" id="st5" name="rating" value="5" required><label for="st5">★</label>
                <input type="radio" id="st4" name="rating" value="4"><label for="st4">★</label>
                <input type="radio" id="st3" name="rating" value="3"><label for="st3">★</label>
                <input type="radio" id="st2" name="rating" value="2"><label for="st2">★</label>
                <input type="radio" id="st1" name="rating" value="1"><label for="st1">★</label>
            </div>

            <div class="rate-modal-comment-group">
                <label class="rate-modal-label">Comments (Optional)</label>
                <textarea name="comment" class="rate-modal-textarea" placeholder="Tell us about the service..."></textarea>
            </div>

            <button type="submit" class="btn-submit-feedback">Submit Feedback</button>
        </form>
    </div>
</div>

</body>
</html>