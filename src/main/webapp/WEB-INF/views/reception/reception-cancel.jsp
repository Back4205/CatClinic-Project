<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist | Process Cancellation</title>
    <%-- Chỉ giữ lại link CSS của reception để tránh xung đột --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reception-cancel.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body style="background-color: #f8fafc; margin: 0; padding: 0;">

<div class="main-content-wrapper">
    <div class="cancel-container">
        <c:choose>
            <%-- BƯỚC 1: XÁC NHẬN CHI TIẾT --%>
            <c:when test="${empty param.step || param.step == '1'}">
                <div class="cancel-header-card">
                    <span class="reception-badge">RECEPTIONIST TASK</span>
                    <h2>Cancel this Appointment?</h2>
                    <p>Please confirm with the customer before proceeding.</p>
                </div>
                <div class="refund-form">
                    <div class="patient-info-box">
                        <div class="icon-cat">🐱</div>
                        <div>
                            <p class="label-orange">PATIENT & OWNER</p>
                            <p class="patient-name">${booking.catName} (${booking.customerName})</p>
                            <p class="service-name">${booking.serviceName}</p>
                        </div>
                    </div>
                    <a href="reception-cancel?step=2&id=${booking.bookingID}" class="btn-full btn-pay">Continue to Cancel</a>
                    <a href="appointmentdetail?id=${booking.bookingID}" class="btn-full btn-back-history">No, Go Back</a>
                </div>
            </c:when>

            <%-- BƯỚC 2: CHỌN LÝ DO HỦY --%>
            <c:when test="${param.step == '2'}">
                <div class="cancel-header-card">
                    <a href="reception-cancel?step=1&id=${booking.bookingID}" class="back-link">
                        <i class="bi bi-chevron-left"></i> Back
                    </a>
                    <h2>Cancellation Reason</h2>
                    <p>Why is this appointment being cancelled?</p>
                </div>
                <form action="reception-cancel" method="GET" class="refund-form">
                    <input type="hidden" name="step" value="3">
                    <input type="hidden" name="id" value="${booking.bookingID}">

                    <label class="reason-option"><input type="radio" name="reason" value="Customer requested via phone" required> Customer requested via phone</label>
                    <label class="reason-option"><input type="radio" name="reason" value="No-show (Customer did not arrive)"> No-show (Customer did not arrive)</label>
                    <label class="reason-option"><input type="radio" name="reason" value="Mistake in booking information"> Mistake in booking information</label>
                    <label class="reason-option"><input type="radio" name="reason" value="Doctor/Clinic Emergency"> Doctor/Clinic Emergency</label>
                    <label class="reason-option"><input type="radio" name="reason" value="Other" id="radioOther"> Other / Special case</label>

                    <input type="text" name="otherReason" id="inputOtherReason" placeholder="Please specify detail..."
                           class="modal-textarea" style="display: none; width: 100%; box-sizing: border-box; margin-bottom: 15px;">

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

            <%-- BƯỚC 3: NHẬP THÔNG TIN BANK --%>
            <c:when test="${param.step == '3'}">
                <div class="cancel-header-card">
                    <a href="reception-cancel?step=2&id=${booking.bookingID}" class="back-link">
                        <i class="bi bi-chevron-left"></i> Back
                    </a>
                    <h2>Refund Details</h2>
                    <p>Enter the bank account where the customer wants to receive the refund.</p>
                </div>
                <form action="reception-cancel" method="POST" class="refund-form">
                    <input type="hidden" name="bookingID" value="${booking.bookingID}">
                    <input type="hidden" name="slotID" value="${booking.slotID}">
                    <input type="hidden" name="cancelReason" value="${param.reason eq 'Other' ? param.otherReason : param.reason}">

                    <label class="info-label">BANK NAME</label>
                    <input type="text" name="bankName" placeholder="e.g., Vietcombank, MB..." required>

                    <label class="info-label">ACCOUNT NUMBER</label>
                    <input type="text" name="accNum" placeholder="Account Number" required>

                    <label class="info-label">ACCOUNT HOLDER NAME</label>
                    <input type="text" name="accName" placeholder="Full Name" required>

                    <button type="submit" class="btn-full btn-pay">Process Refund Request</button>
                </form>
            </c:when>

            <%-- BƯỚC 4: THÔNG BÁO THÀNH CÔNG --%>
            <c:when test="${param.step == 'success'}">
                <div class="success-state">
                    <div class="success-icon-circle">
                        <i class="bi bi-check-lg"></i>
                    </div>
                    <h2>Successfully Cancelled</h2>
                    <p style="color: #64748B;">The appointment #${booking.bookingID} is now pending refund.</p>

                    <div class="next-steps-card">
                        <p class="steps-title">NOTICE</p>
                        <div style="font-size: 14px; color: #64748b; display: flex; align-items: center; justify-content: center; gap: 8px;">
                            <i class="bi bi-info-circle" style="color: #FF6B00;"></i>
                            <span>The accounting department will process the refund shortly.</span>
                        </div>
                    </div>

                    <a href="view-booking-list" class="btn-full btn-pay">Return to Dashboard</a>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>

</body>
</html>