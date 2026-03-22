<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Appointment Details | Receptionist</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/receptiondashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reception-cancel.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">


    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="css/reception-detail.css" rel="stylesheet" type="text/css"/>

</head>

<body>
<div class="app-container">
    <c:set var="activePage" value="dashboard" scope="request" />
    <%@include file="sidebar.jsp" %>
    <main class="main-content">

        <div class="appointment-card">
            <div class="card-header">
                <div class="pet-icon-bg"><i class="fa-solid fa-cat"></i></div>
                <div class="card-header-info">
                    <div class="card-title">
                        <h2>${booking.catName}</h2>
                        <span class="status-tag status-${booking.status.toLowerCase()}">${booking.status}</span>
                    </div>
                    <p class="booking-id">${booking.catBreed} • Booking ID: #BD-${booking.bookingID}</p>
                </div>
            </div>

            <div class="info-section">
                <span class="section-label">Visit Information</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-regular fa-calendar icon"></i> APPOINTMENT DATE</div>
                        <div class="value-text">${booking.appointmentDate}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-regular fa-clock icon"></i> TIME SLOT</div>
                        <div class="value-text">${booking.appointmentTime}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-notes-medical icon"></i> SERVICE TYPE</div>
                        <div class="value-text">${booking.serviceName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-user-doctor icon"></i> VETERINARIAN</div>
                        <div class="value-text">${empty booking.vetName ? 'Assigning...' : booking.vetName}</div>
                    </div>
                    <div class="info-row price-row">
                        <div class="label-with-icon"><i class="fa-solid fa-money-bill-wave icon"></i>PRICE AT BOOKING</div>
                        <div class="value-text price"><fmt:formatNumber value="${booking.price}" type="number"/> VND</div>
                    </div>
                </div>
            </div>

            <div class="info-section">
                <span class="section-label">Owner Contact</span>
                <div class="info-box">
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-user icon"></i> Owner Name</div>
                        <div class="value-text">${booking.customerName}</div>
                    </div>
                    <div class="info-row">
                        <div class="label-with-icon"><i class="fa-solid fa-phone icon"></i> Phone Number</div>
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

            <c:if test="${not empty boarding}">
                <div class="info-section">
                    <span class="section-label">Boarding Records</span>
                    <div class="boarding-summary">
                        <div class="record-title"><i class="fa-solid fa-file-medical"></i> ADMISSION</div>
                        <div class="info-row">
                            <div class="label-with-icon"><i class="fa-solid fa-clock-rotate-left"></i> Check-in Time</div>
                            <div class="value-text">
                                    ${boarding.checkInTime.length() > 16 ? boarding.checkInTime.substring(0, 16) : boarding.checkInTime}
                            </div>
                        </div>
                        <div class="info-row">
                            <div class="label-with-icon"><i class="fa-solid fa-notes-medical"></i> Condition</div>
                            <div class="value-text italic">"${boarding.checkInCondition}"</div>
                        </div>

                        <c:if test="${not empty boarding.checkOutTime}">
                            <hr class="modal-divider">
                            <div class="record-title success-text"><i class="fa-solid fa-house-chimney"></i> RELEASED</div>
                            <div class="info-row">
                                <div class="label-with-icon"><i class="fa-solid fa-hand-holding-heart"></i> Check-out Time</div>
                                <div class="value-text">
                                        ${boarding.checkOutTime.length() > 16 ? boarding.checkOutTime.substring(0, 16) : boarding.checkOutTime}
                                </div>
                            </div>
                            <div class="info-row">
                                <div class="label-with-icon"><i class="fa-solid fa-clipboard-check"></i> Condition</div>
                                <div class="value-text italic">"${boarding.checkOutCondition}"</div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:if>
            <c:if test="${param.error == 'not_today'}">
                <div style="background-color: #ffebee; color: #c62828; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #ef9a9a; display: flex; align-items: center; gap: 10px;">
                    <i class="fa-solid fa-circle-exclamation" style="font-size: 20px;"></i>
                    <div>
                        <strong>Check-in Error:</strong>The cat date is<b> <fmt:parseDate value="${booking.appointmentDate}" pattern="yyyy-MM-dd" var="parsedDate" />
                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" /> </b>.
                        You can only check in on the scheduled date (Today).
                    </div>
                </div>
            </c:if>

            <div class="button-group">
                <a href="view-booking-list" class="btn-action btn-close"><i class="fa-solid fa-arrow-left"></i> &nbsp; Close</a>

                <c:if test="${booking.status == 'Confirmed'}">
                    <button type="button" onclick="openCheckinModal()" class="btn-action btn-orange-fixed"><i class="fa-solid fa-check"></i> &nbsp; Check-in Now</button>
                    <%-- NÚT CANCEL MỚI THÊM --%>
                    <a href="reception-cancel?id=${booking.bookingID}" class="btn-cancel-outline">
                        <i class="fa-solid fa-trash-can"></i> &nbsp; Cancel Booking
                    </a>
                </c:if>


                <%-- Trường hợp ca khám mới đặt chưa confirm cũng cho phép Cancel --%>
                <c:if test="${booking.status == 'PendingPayment'}">
                    <a href="reception-cancel?id=${booking.bookingID}" class="btn-cancel-outline">
                        <i class="fa-solid fa-trash-can"></i> &nbsp; Cancel Booking
                    </a>
                </c:if>
            </div>
        </div>
    </main>
</div>

<%-- GIỮ NGUYÊN TOÀN BỘ MODAL VÀ SCRIPT CŨ CỦA CẬU --%>
<div id="checkinModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header-orange">
            <h3>Confirm Check-In</h3>
            <p>Patient: ${booking.catName}</p>
        </div>
        <div class="modal-body">
            <label>CHECK-IN CONDITION:</label>
            <textarea id="checkinNote" placeholder="Ví dụ: Bé khỏe mạnh, hơi ho..."></textarea>
            <div class="modal-footer">
                <button type="button" onclick="closeCheckinModal()" class="btn-modal-cancel">Cancel</button>
                <button type="button" onclick="submitCheckinForm(${booking.bookingID})" class="btn-modal-confirm">Confirm</button>
            </div>
        </div>
    </div>
</div>

<script>
    function openCheckinModal() { document.getElementById('checkinModal').style.display = 'flex'; }
    function closeCheckinModal() { document.getElementById('checkinModal').style.display = 'none'; }
    function submitCheckinForm(bookingId) {
        var note = document.getElementById('checkinNote').value;
        if (!note.trim()) { alert("Vui lòng ghi nhận tình trạng bé mèo lúc tiếp nhận!"); return; }
        window.location.href = "checkin?id=" + bookingId + "&status=Completed&condition=" + encodeURIComponent(note);
    }
   
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('status') === 'checkin_success') {
            alert("✅ Check-in thành công! Hồ sơ nội trú đã được khởi tạo.");
            window.history.replaceState({}, document.title, window.location.pathname);
        } else if (urlParams.get('status') === 'success') {
            alert("✨ Thành công! Bé mèo đã được làm thủ tục trả (Check-out).");
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    };
</script>
</body>
</html>