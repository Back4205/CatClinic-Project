<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cancel Appointment | CatClinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/cancel-booking.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<%@include file="header.jsp" %>

<div class="main-content-wrapper">
    <div class="cancel-container">
        <c:choose>
            <c:when test="${empty param.step || param.step == '1'}">
                <div class="cancel-header-card">
                    <h2>Cancel Appointment?</h2>
                    <p>We're sorry you can't make it. Please review your appointment details below.</p>
                </div>
                <div class="refund-form">
                    <div class="patient-info-box">
                        <div class="icon-cat">🐱</div>
                        <div>
                            <p class="label-orange">PATIENT</p>
                            <p class="patient-name">${booking.catName}</p>
                            <p class="service-name">${booking.serviceName}</p>
                        </div>
                    </div>
                    <a href="cancel-booking?step=2&id=${booking.bookingID}" class="btn-full btn-pay">Continue to Cancel</a>
                    <a href="booking-history" class="btn-full btn-back-history">Keep Appointment</a>
                </div>
            </c:when>

            <c:when test="${param.step == '2'}">
                <div class="cancel-header-card">
                    <a href="cancel-booking?step=1&id=${booking.bookingID}" class="back-link">&lt; Back</a>
                    <h2>Why are you cancelling?</h2>
                    <p>Your feedback helps us improve our service.</p>
                </div>
                <form action="cancel-booking" method="GET" class="refund-form">
                    <input type="hidden" name="step" value="3">
                    <input type="hidden" name="id" value="${booking.bookingID}">

                    <label class="reason-option"><input type="radio" name="reason" value="Scheduling conflict" required> Scheduling conflict</label>
                    <label class="reason-option"><input type="radio" name="reason" value="Personal emergency"> Personal emergency</label>
                    <label class="reason-option"><input type="radio" name="reason" value="My cat is feeling better"> My cat is feeling better</label>
                    <label class="reason-option"><input type="radio" name="reason" value="Too far to travel"> Too far to travel</label>

                    <label class="reason-option" style="margin-bottom: 8px;">
                        <input type="radio" name="reason" value="Other" id="radioOther"> Other reason
                    </label>

                    <input type="text" name="otherReason" id="inputOtherReason" placeholder="Please specify your reason..."
                           style="display: none; width: 100%; padding: 14px 16px; border: 1px solid #E2E8F0; border-radius: 12px; margin-bottom: 12px; font-size: 15px; box-sizing: border-box;">

                    <button type="submit" class="btn-full btn-pay" style="margin-top: 10px;">Next Step</button>
                </form>

                <script>
                    const radios = document.querySelectorAll('input[name="reason"]');
                    const otherInput = document.getElementById('inputOtherReason');
                    radios.forEach(radio => {
                        radio.addEventListener('change', function() {
                            if (this.id === 'radioOther') {
                                otherInput.style.display = 'block';
                                otherInput.required = true;
                            } else {
                                otherInput.style.display = 'none';
                                otherInput.required = false;
                                otherInput.value = '';
                            }
                        });
                    });
                </script>
            </c:when>

            <c:when test="${param.step == '3'}">
                <div class="cancel-header-card">
                    <a href="cancel-booking?step=2&id=${booking.bookingID}" class="back-link">&lt; Back</a>
                    <h2>Refund Information</h2>
                    <p>Please provide your bank details to receive your deposit refund.</p>
                </div>
                <form action="cancel-booking" method="POST" class="refund-form">
                    <input type="hidden" name="bookingID" value="${booking.bookingID}">
                    <input type="hidden" name="slotID" value="${booking.slotID}">

                    <input type="hidden" name="cancelReason" value="${param.reason eq 'Other' ? param.otherReason : param.reason}">

                    <label class="info-label">BANK NAME</label>
                    <input type="text" name="bankName" placeholder="mb bank" required>

                    <label class="info-label">ACCOUNT NUMBER</label>
                    <input type="text" name="accNum" placeholder="0348939912" required>

                    <label class="info-label">ACCOUNT HOLDER NAME</label>
                    <input type="text" name="accName" placeholder="Vuong Thi Qui" required>

                    <button type="submit" class="btn-full btn-pay">Confirm & Request Refund</button>
                </form>
            </c:when>

            <c:when test="${param.step == 'success'}">
                <div class="success-state">
                    <div class="success-icon-circle">
                        <i class="bi bi-check-lg"></i>
                    </div>
                    <h2>Appointment Cancelled</h2>
                    <p style="color: #64748B;">Your appointment has been successfully cancelled.</p>

                    <div class="next-steps-card">
                        <p class="steps-title">WHAT'S NEXT?</p>
                        <div class="step-item"><i class="bi bi-calendar-event" style="color: #FF5C00; font-size: 18px;"></i> <span>You can reschedule anytime through our portal.</span></div>
                    </div>

                    <a href="booking-history" class="btn-full btn-dark">Return to Dashboard</a>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>

<%@include file="footer.jsp" %>
</body>
</html>