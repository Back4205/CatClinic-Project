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

        <form action="${pageContext.request.contextPath}/reception/counter-booking" method="POST" id="bookingForm">
            <div class="booking-grid">

                <div class="booking-col">

                    <div class="section-title">
                        <span>Customer Information</span>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Phone Number *</label>
                        <input type="text" name="phone" class="form-control" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Full Name *</label>
                        <input type="text" name="fullName" class="form-control" required>
                    </div>

                    <div class="section-title" style="margin-top: 30px;">
                        <span>Pet Information</span>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Pet Name *</label>
                        <input type="text" name="petName" class="form-control" required>
                    </div>

                    <div class="row-flex">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Breed *</label>
                            <input type="text" name="breed" class="form-control" required>
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Age *</label>
                            <input type="number" name="age" class="form-control" min="0" required>
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

                <div class="booking-col">

                    <div class="section-title">
                        <span>Appointment Details</span>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Select Category *</label>
                        <select name="category" id="category" class="form-control" required>
                            <option value="" disabled selected>-- Choose Category --</option>
                            <c:forEach items="${categoryList}" var="cat">
                                <option value="${cat.categoryID}">
                                        ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row-flex">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Select Service *</label>
                            <select name="service" id="service" class="form-control" required>
                                <option value="" disabled selected>
                                    -- Choose Service --
                                </option>
                            </select>
                        </div>

                        <div class="form-group" id="doctorGroup" style="flex: 1;">
                            <label class="form-label">Select Doctor *</label>
                            <select name="doctor" id="doctor" class="form-control" required>
                                <option value="" disabled selected>
                                    -- Choose Doctor --
                                </option>
                                <c:forEach items="${vetList}" var="vet">
                                    <option value="${vet.VetID}">
                                         ${vet.FullName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group" id="staffGroup" style="display:none;">
                        <label class="form-label">Assign Staff *</label>
                        <select name="staffId" id="staffSelect" class="form-control">
                            <option value="" disabled selected>
                                -- Choose Staff --
                            </option>
                        </select>
                    </div>

                    <div id="boardingExtra" style="display:none;">
                        <div class="form-group">
                            <label class="form-label">Check In Time *</label>
                            <input type="time" name="checkInTime" class="form-control">
                        </div>

                        <div class="form-group">
                            <label class="form-label">End Date *</label>
                            <input type="date" name="endDate" class="form-control">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Appointment Date *</label>
                        <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" required>
                    </div>

                    <div class="form-group" id="timeSlotGroup">
                        <label class="form-label">Doctor's Schedule</label>
                        <div id="slotContainer" class="time-grid">
                            <p style="font-size:13px;color:#9ca3af;font-style:italic;">
                                Please select Doctor and Date to view available slots.
                            </p>
                        </div>
                        <input type="hidden" name="appointmentTime" id="selectedTime">
                        <input type="hidden" name="slotId" id="selectedSlotId">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Symptom Notes</label>
                        <textarea name="note" class="form-control" rows="3"></textarea>
                    </div>

                    <button type="submit" class="btn-submit-booking" id="btnSubmit">
                        CONFIRM BOOKING
                    </button>

                </div>
            </div>
        </form>
    </div>
</main>

<script>
    document.addEventListener("DOMContentLoaded", function(){
        const category = document.getElementById("category");
        const service = document.getElementById("service");
        const staffGroup = document.getElementById("staffGroup");
        const staffSelect = document.getElementById("staffSelect");
        const boardingExtra = document.getElementById("boardingExtra");
        const doctor = document.getElementById("doctor");
        const dateInput = document.getElementById("appointmentDate");
        const slotContainer = document.getElementById("slotContainer");
        const hiddenTime = document.getElementById("selectedTime");
        const hiddenSlotId = document.getElementById("selectedSlotId");


        const today = new Date().toISOString().split("T");
        dateInput.setAttribute("min", today);


        category.addEventListener("change", function(){
            const categoryId = this.value;
            service.innerHTML = "<option disabled selected>Loading...</option>";

            staffGroup.style.display = "none";
            boardingExtra.style.display = "none";
            document.getElementById("doctorGroup").style.display = "block";
            document.getElementById("timeSlotGroup").style.display = "block";

            doctor.setAttribute("required", "true");

            fetch('${pageContext.request.contextPath}/reception/counter-booking?action=getServicesByCategory&categoryId=' + categoryId)
                .then(res => res.json())
                .then(data => {
                    service.innerHTML = "<option value='' disabled selected>-- Choose Service --</option>";
                    data.forEach(s => {
                        service.innerHTML += '<option value="' + s.serviceID + '">' + s.nameService + ' (' + s.price + ' VND)</option>';
                    });
                })
                .catch(err => console.error("Error:", err));

            if(categoryId == 3 || categoryId == 4){
                document.getElementById("doctorGroup").style.display = "none";
                document.getElementById("timeSlotGroup").style.display = "none";
                doctor.removeAttribute("required");

                staffGroup.style.display = "block";

                if(categoryId == 3) {
                    loadStaff("Technician");
                } else if (categoryId == 4) {
                    boardingExtra.style.display = "block";
                    loadStaff("Care");
                }
            }
        });

        function loadStaff(position){
            fetch('${pageContext.request.contextPath}/reception/counter-booking?action=getStaffByPosition&position=' + position)
                .then(res => res.json())
                .then(data => {
                    staffSelect.innerHTML = "<option value='' disabled selected>-- Choose Staff --</option>";
                    data.forEach(st => {
                        staffSelect.innerHTML += '<option value="' + st.staffID + '">' + st.fullName + '</option>';
                    });
                })
                .catch(err => console.error("Error:", err));
        }

        function loadSlots() {
            const vetId = doctor.value;
            const dateVal = dateInput.value;

            if (!vetId || !dateVal) {
                slotContainer.innerHTML = '<p style="font-size:13px;color:#9ca3af;font-style:italic;">Please select Doctor and Date to view available slots.</p>';
                return;
            }

            slotContainer.innerHTML = '<p style="font-size:13px;color:#f97316;">Loading slots...</p>';

            fetch('${pageContext.request.contextPath}/reception/counter-booking?action=getSlots&vetId=' + vetId + '&date=' + dateVal)
                .then(res => res.json())
                .then(data => {
                    if (data.length === 0) {
                        slotContainer.innerHTML = '<p style="font-size:13px;color:#dc2626;">No slots available for this date.</p>';
                        return;
                    }

                    let html = '<div style="display: flex; gap: 10px; flex-wrap: wrap; margin-top: 10px;">';
                    data.forEach(slot => {
                        html += '<label class="slot-item" style="padding: 8px 15px; border: 1px solid #d1d5db; border-radius: 6px; cursor: pointer; background: #fff; transition: 0.2s;">';
                        html += '<input type="radio" name="slotRadio" value="' + slot.slotId + '" data-time="' + slot.startTime + '" style="margin-right: 5px;" required> ';
                        html += '<b>' + slot.startTime + '</b>';
                        html += '</label>';
                    });
                    html += '</div>';

                    slotContainer.innerHTML = html;

                    const radios = slotContainer.querySelectorAll('input[type="radio"]');
                    radios.forEach(radio => {
                        radio.addEventListener('change', function() {
                            hiddenSlotId.value = this.value;
                            hiddenTime.value = this.getAttribute('data-time');

                            slotContainer.querySelectorAll('.slot-item').forEach(lbl => {
                                lbl.style.borderColor = '#d1d5db';
                                lbl.style.backgroundColor = '#fff';
                                lbl.style.color = '#374151';
                            });
                            this.parentElement.style.borderColor = '#f97316';
                            this.parentElement.style.backgroundColor = '#fff7ed';
                            this.parentElement.style.color = '#f97316';
                        });
                    });
                })
                .catch(err => {
                    console.error("Error loading slots:", err);
                    slotContainer.innerHTML = '<p style="font-size:13px;color:#dc2626;">Error loading slots.</p>';
                });
        }

        doctor.addEventListener("change", loadSlots);
        dateInput.addEventListener("change", loadSlots);
    });
</script>

</body>
</html>