<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Appointment Details | Receptionist</title>
       <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">--%>
        <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">
        <link href="css/reception-detail.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<div class="app-container">
    <c:set var="activePage" value="dashboard" scope="request" />
    <%@include file="sidebar.jsp" %>
    <main class="main-content">
        <div class="appointment-card">
            <div class="card-header">
                <div class="pet-icon-bg">
                    <i class="fa-solid fa-cat"></i>
                </div>
                <div class="card-header-info">
                    <div class="card-title">
                        <h2>${booking.catName}</h2>
                        <span class="status-tag status-${booking.status.toLowerCase()}">
                            ${booking.status}
                        </span>
                    </div>
                    <p class="booking-id">
                        ${booking.catBreed} • Booking ID: #BD-${booking.bookingID}
                    </p>
                </div>
            </div>
            <div class="info-section">
                <span class="section-label">Visit Information</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-regular fa-calendar icon"></i>
                            APPOINTMENT DATE
                        </div>
                        <div class="value-text">${booking.appointmentDate}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-regular fa-clock icon"></i>
                            TIME SLOT
                        </div>
                        <div class="value-text">${booking.appointmentTime}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-solid fa-notes-medical icon"></i>
                            SERVICE TYPE
                        </div>
                        <div class="value-text">${booking.serviceName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-solid fa-user-doctor icon"></i>
                            VETERINARIAN
                        </div>
                        <div class="value-text">
                            ${empty booking.vetName ? 'Assigning...' : booking.vetName}
                        </div>
                    </div>
                    <div class="info-row price-row">
                        <div class="label-with-icon">
                            <i class="fa-solid fa-money-bill-wave icon"></i>
                            TOTAL PRICE
                        </div>
                        <div class="value-text price">
                            <fmt:formatNumber value="${booking.price}" type="number"/> VND
                        </div>
                    </div>

                </div>
            </div>
            <!-- Owner -->
            <div class="info-section">
                <span class="section-label">Owner Contact</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-solid fa-user icon"></i>
                            Owner Name
                        </div>
                        <div class="value-text">${booking.customerName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon">
                            <i class="fa-solid fa-phone icon"></i>
                            Phone Number
                        </div>
                        <div class="value-text">${booking.customerPhone}</div>
                    </div>
                </div>
            </div>
            <div class="info-section">
                <span class="section-label">Customer Notes</span>
                <div class="note-area">
                    "${empty booking.note ? 'No additional requests provided for this visit.' : booking.note}"
                </div>
            </div>
            <!-- Buttons -->
            <div class="button-group">
                <a href="view-booking-list" class="btn-close">
                    Close
                </a>
                <c:if test="${booking.status == 'Confirmed'}">
                    <a href="update-status?id=${booking.bookingID}&status=Waiting"
                       class="btn-checkin">
                        Cancel Booking
                    </a>
                </c:if>
                 <c:if test="${booking.status == 'PendingPayment'}">
                    <a href="update-status?id=${booking.bookingID}&status=Waiting"
                       class="btn-checkin">
                        Pay Deposit 
                    </a>
                </c:if>
            </div>
        </div>

    </main>

</div>

</body>
</html>