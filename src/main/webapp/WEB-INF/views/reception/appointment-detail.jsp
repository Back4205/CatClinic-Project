<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Appointment Details | Receptionist</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reception-detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<%@include file="header.jsp" %>
<div class="app-container">
    <c:set var="activePage" value="dashboard" scope="request" />
    <%@include file="sidebar.jsp" %>

    <main class="main-content">
        <div class="appointment-card">
            <div class="card-header">
                <div class="pet-icon-bg"><i class="fa-solid fa-cat"></i></div>
                <div style="flex: 1;">
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <h2 style="margin:0; font-size: 22px; color: #1e293b;">${booking.catName}</h2>
                        <span class="status-tag status-${booking.status.toLowerCase()}">${booking.status}</span>
                    </div>
                    <p style="margin: 5px 0; color: #64748b; font-size: 14px;">${booking.catBreed} • Booking ID: #BD-${booking.bookingID}</p>
                </div>
            </div>

            <div class="info-section">
                <span class="section-label">Visit Information</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-regular fa-calendar" style="color:#FF7E32"></i> APPOINTMENT DATE</div>
                        <div class="value-text">${booking.appointmentDate}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-regular fa-clock" style="color:#FF7E32"></i> TIME SLOT</div>
                        <div class="value-text">${booking.appointmentTime}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-notes-medical" style="color:#FF7E32"></i> SERVICE TYPE</div>
                        <div class="value-text">${booking.serviceName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-user-doctor" style="color:#FF7E32"></i> VETERINARIAN</div>
                        <div class="value-text">${empty booking.vetName ? 'Assigning...' : booking.vetName}</div>
                    </div>

                    <div class="info-row" style="margin-top: 10px; border-top: 1px dashed #e2e8f0; padding-top: 12px;">
                        <div class="label-with-icon"><i class="fa-solid fa-money-bill-wave" style="color:#FF7E32"></i> TOTAL PRICE</div>
                        <div class="value-text" style="color: #FF7E32; font-size: 16px; font-weight: 800;">
                            <fmt:formatNumber value="${booking.price}" type="number"/> VND
                        </div>
                    </div>
                </div>
            </div>

            <div class="info-section">
                <span class="section-label">Owner Contact</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-user" style="color:#FF7E32"></i> Owner Name</div>
                        <div class="value-text">${booking.customerName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-phone" style="color:#FF7E32"></i> Phone Number</div>
                        <div class="value-text">${booking.customerPhone}</div>
                    </div>
                </div>
            </div>

            <div class="info-section">
                <span class="section-label">Notes & Status Alert</span>

                <%-- Thông báo của hệ thống --%>
                <c:if test="${booking.status == 'Cancelled' && booking.note.contains('No-show')}">
                    <div class="system-alert-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <div>
                            <strong>System Alert:</strong> This appointment was auto-cancelled due to expiration.
                            <br>Policy: <strong>Non-refundable for no-show cases.</strong>
                        </div>
                    </div>
                </c:if>

                <%-- Note gốc của khách --%>
                <div class="note-area">
                    <i class="fa-solid fa-quote-left" style="font-size: 10px; opacity: 0.3; margin-right: 5px;"></i>
                    ${empty booking.note ? 'No additional requests provided for this visit.' : booking.note}
                </div>
            </div>

            <div style="padding: 0 30px 30px; display: flex; justify-content: center; gap: 15px;">
                <a href="home" style="background: #f1f5f9; color: #64748B; text-decoration: none; font-weight: 700; font-size: 14px; padding: 12px 25px; border-radius: 12px;">
                    Close
                </a>
                <c:if test="${booking.status == 'Confirmed'}">
                    <a href="update-status?id=${booking.bookingID}&status=Waiting"
                       style="background: #22C55E; color: white; text-decoration: none; font-weight: 700; font-size: 14px; padding: 12px 25px; border-radius: 12px;">
                        Check-in Now
                    </a>
                </c:if>
            </div>
        </div>
    </main>
</div>
<%@include file="footer.jsp" %>
</body>
</html>