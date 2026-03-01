<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Counter Booking | Cat Clinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/counter-booking-style.css" rel="stylesheet">
</head>
<body>
<c:set var="activePage" value="counter-booking" scope="request" />
<jsp:include page="sidebar.jsp" />

<main class="main-wrapper">
    <header class="top-header">
        <div class="page-title" style="margin: 0;">
            <h2 style="font-size: 20px;">Book Appointment at Counter</h2>
        </div>
    </header>

    <div class="dashboard-content booking-container">
        <c:if test="${not empty param.error}">
            <div style="color: red; margin-bottom: 15px; padding: 10px; background: #fef2f2; border-radius: 6px;">
                <b>Error:</b> ${param.error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reception/counter-booking" method="POST" id="bookingForm">
            <div class="booking-grid">

                <!-- CỘT TRÁI: KHÁCH HÀNG & THÚ CƯNG -->
                <div class="booking-col">
                    <div class="section-title">
                        <span>Customer Information</span>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Phone Number *</label>
                        <input type="text" name="phone" class="form-control" placeholder="09xxxxxxx" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Full Name *</label>
                        <input type="text" name="fullName" class="form-control" placeholder="Ex: John Doe" required>
                    </div>

                    <div class="section-title" style="margin-top: 30px;">
                        <span>Pet Information</span>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Pet Name *</label>
                        <input type="text" name="petName" class="form-control" placeholder="Ex: Mochi" required>
                    </div>
                    <div class="row-flex">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Breed *</label>
                            <input type="text" name="breed" class="form-control" placeholder="British Shorthair" required>
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Age (Years) *</label>
                            <input type="number" name="age" class="form-control" min="0" placeholder="2" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Gender *</label>
                        <select name="gender" class="form-control" required>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                    </div>
                </div>

                <!-- CỘT PHẢI: LỊCH HẸN VÀ AJAX TIME SLOT -->
                <div class="booking-col">
                    <div class="section-title">
                        <span>Appointment Details</span>
                    </div>

                    <div class="row-flex">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Select Service *</label>
                            <select name="service" class="form-control" required>
                                <option value="" disabled selected>-- Choose Service --</option>
                                <c:forEach items="${serviceList}" var="srv">
                                    <option value="${srv.ServiceID}">${srv.NameService} ($${srv.Price})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Select Doctor *</label>
                            <select name="doctor" id="doctor" class="form-control" required>
                                <option value="" disabled selected>-- Choose Doctor --</option>
                                <c:forEach items="${vetList}" var="vet">
                                    <option value="${vet.VetID}">Dr. ${vet.FullName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top: 20px;">
                        <label class="form-label">Appointment Date *</label>
                        <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Doctor's Schedule</label>
                        <div id="slotContainer" class="time-grid" style="margin-top: 10px;">
                            <p style="font-size: 13px; color: #9ca3af; font-style: italic;">
                                Please select Doctor and Date to view available slots.
                            </p>
                        </div>
                        <!-- Lưu Giờ & SlotID để submit -->
                        <input type="hidden" name="appointmentTime" id="selectedTime" required>
                        <input type="hidden" name="slotId" id="selectedSlotId" required>
                    </div>

                    <div class="form-group" style="margin-top: 20px;">
                        <label class="form-label">Symptom Notes</label>
                        <textarea name="note" class="form-control" rows="3" placeholder="Describe the pet's condition..."></textarea>
                    </div>

                    <button type="submit" class="btn-submit-booking" id="btnSubmit">CONFIRM BOOKING</button>
                </div>
            </div>
        </form>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const dateInput = document.getElementById('appointmentDate');
        const doctorSelect = document.getElementById('doctor');
        const slotContainer = document.getElementById('slotContainer');
        const hiddenTime = document.getElementById('selectedTime');
        const hiddenSlotId = document.getElementById('selectedSlotId');
        const btnSubmit = document.getElementById('btnSubmit');

        // Set Min Date = Today
        const today = new Date().toISOString().split('T');
        dateInput.setAttribute('min', today);

        function fetchSlots() {
            const vetId = doctorSelect.value;
            const date = dateInput.value;
            hiddenTime.value = '';
            hiddenSlotId.value = '';
            btnSubmit.innerText = 'CONFIRM BOOKING';

            if (vetId && date) {
                slotContainer.innerHTML = '<p style="font-size:13px; color:#6b7280;">Loading available slots...</p>';

                fetch(`${pageContext.request.contextPath}/reception/counter-booking?action=getSlots&vetId=${vetId}&date=${date}`)
                    .then(response => response.json())
                    .then(slots => {
                        slotContainer.innerHTML = '';
                        if (slots.length === 0) {
                            slotContainer.innerHTML = '<p style="color:#dc2626; font-size:13px; font-weight:600;">No available slots for this doctor on selected date.</p>';
                            return;
                        }

                        slots.forEach(slot => {
                            const btn = document.createElement('div');
                            btn.className = 'time-slot';
                            btn.innerText = slot.startTime;

                            btn.onclick = function() {
                                document.querySelectorAll('.time-slot').forEach(el => el.classList.remove('active'));
                                this.classList.add('active');
                                hiddenTime.value = slot.startTime;
                                hiddenSlotId.value = slot.slotId;
                                btnSubmit.innerText = 'BOOK APPOINTMENT AT ' + slot.startTime;
                            };
                            slotContainer.appendChild(btn);
                        });
                    })
                    .catch(err => {
                        console.error(err);
                        slotContainer.innerHTML = '<p style="color:#dc2626; font-size:13px;">Error loading schedule.</p>';
                    });
            }
        }

        doctorSelect.addEventListener('change', fetchSlots);
        dateInput.addEventListener('change', fetchSlots);

        // Validate form trước khi gửi
        document.getElementById('bookingForm').addEventListener('submit', function(e) {
            if(hiddenSlotId.value === '') {
                e.preventDefault();
                alert("Please select a time slot from the Doctor's Schedule!");
            }
        });
    });
</script>
</body>
</html>