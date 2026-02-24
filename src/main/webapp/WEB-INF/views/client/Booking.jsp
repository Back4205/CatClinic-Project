<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Appointment | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <style>
        /* --- CSS STYLES --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f7fa; }
        .container { width: 1200px; margin: 30px auto; }
        h2 { color: #1f2937; font-weight: 800; text-transform: uppercase; border-left: 5px solid #ff6600; padding-left: 15px; margin-bottom: 30px; }
        .booking-wrapper { display: flex; gap: 30px; align-items: flex-start; }
        .left-panel { width: 45%; }
        .right-panel { width: 55%; }
        .card { background: #fff; padding: 25px; border-radius: 12px; margin-bottom: 25px; border: 1px solid #e5e7eb; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); }
        .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f3f4f6; padding-bottom: 10px; }
        .card-header h4 { margin: 0; color: #374151; font-size: 16px; text-transform: uppercase; }

        .selection-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px; margin-bottom: 25px; }
        .selection-grid.hotel-layout { grid-template-columns: 1fr 1fr 1fr 1fr !important; }
        .selection-grid label { display: block; font-size: 12px; font-weight: 700; color: #6b7280; text-transform: uppercase; margin-bottom: 8px; }
        .selection-grid select, .selection-grid input[type="date"] { width: 100%; padding: 10px; border-radius: 8px; border: 1.5px solid #e5e7eb; background-color: #fff; color: #374151; font-size: 13px; }

        table { width: 100%; border-collapse: collapse; }
        th { background: #f9fafb; padding: 10px; text-align: left; font-size: 11px; color: #6b7280; text-transform: uppercase; }
        td { padding: 10px; border-bottom: 1px solid #f3f4f6; vertical-align: middle; font-size: 13px; }
        .pet-img { width: 40px; height: 40px; border-radius: 6px; object-fit: cover; }
        .badge { padding: 3px 8px; border-radius: 12px; font-size: 10px; font-weight: 700; }
        .male { background: #e0f2fe; color: #0369a1; }
        .female { background: #fce7f3; color: #9d174d; }

        /* Schedule Grid */
        .schedule-scroll { max-height: 500px; overflow-y: auto; padding-right: 5px; }
        .day-group { margin-bottom: 25px; border-top: 1px solid #f3f4f6; padding-top: 15px; }
        .day-title { font-size: 13px; font-weight: 800; color: #374151; text-transform: uppercase; margin-bottom: 15px; display: block; }
        .day-title::before { content: '• '; color: #ff6600; }
        .session-container { display: flex; gap: 20px; }
        .session-box { flex: 1; }
        .session-label { font-size: 10px; color: #9ca3af; font-weight: 700; text-transform: uppercase; margin-bottom: 10px; display: block; }
        .slot-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
        .slot { padding: 10px; border: 1px solid #e5e7eb; border-radius: 6px; cursor: pointer; display: flex; justify-content: center; align-items: center; font-size: 12px; transition: 0.2s; color: #4b5563; font-weight: 600; }
        .slot:hover { border-color: #ff6600; color: #ff6600; }
        .slot input[type="radio"] { display: none; }
       .slot.active-slot {
    background: #ff6600 !important;
    border-color: #ff6600 !important;
    color: white !important;
}

        /* Buttons & Summary */
        .boarding-summary { padding: 20px; background: #fff7f0; border: 1px solid #ffd8bf; border-radius: 8px; color: #ff6600; margin-bottom: 15px; }
        .summary-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 13px; }
        .summary-total { display: flex; justify-content: space-between; border-top: 1px dashed #ffd8bf; padding-top: 10px; margin-top: 5px; font-size: 16px; font-weight: 800; }

        .btn-main { width: 100%; padding: 15px; border: none; border-radius: 8px; margin-top: 10px; font-weight: 800; text-transform: uppercase; background: #ff6600; color: white; }
        .btn-main:disabled { background: #ff6600; color: white; cursor: default; }

        .btn-pay-now {
            display: block; width: 100%; padding: 15px; margin-top: 12px;
            border: 2px solid #ff6600; border-radius: 8px;
            background: #fff; color: #ff6600;
            text-align: center; text-decoration: none;
            font-weight: 800; font-size: 15px; text-transform: uppercase; transition: 0.3s;
            cursor: pointer;
        }
        .btn-pay-now:hover { background: #ff6600; color: #fff; }

        .note-textarea { width: 100%; padding: 12px; border-radius: 8px; border: 1.5px solid #e5e7eb; min-height: 80px; font-size: 13px; font-family: inherit; }
        .pagination { margin-top: 20px; display: flex; justify-content: center; gap: 5px; }
        .pagination a { padding: 6px 12px; border: 1px solid #e5e7eb; text-decoration: none; color: #4b5563; border-radius: 6px; font-size: 12px; }
        .pagination a.active { background: #ff6600; color: white; border-color: #ff6600; font-weight: bold; }
        
        .owner-info { line-height: 2; }
        .owner-info div { padding: 5px 0; }
        hr { margin: 20px 0; border: none; border-top: 1px solid #e5e7eb; }
    </style>
</head>
<body>
<%@include file="header.jsp" %>

<div class="container">
    <h2><i class="bi bi-calendar-check"></i> Book Appointment</h2>

    <div class="booking-wrapper">
        <div class="left-panel">
            <div class="card">
                <div class="card-header"><h4><i class="bi bi-person-badge"></i> Owner Information</h4></div>
                <div class="owner-info">
                    <div><strong>Name:</strong> ${acc.fullName}</div>
                    <div><strong>Phone:</strong> ${acc.phone}</div>
                    <div><strong>Email:</strong> ${acc.email}</div>
                </div>
            </div>

            <div class="card">
                <div class="card-header">
                    <h4><i class="bi bi-github"></i> Your Cats</h4>
                    <a href="${pageContext.request.contextPath}/cats/cat-add?from=booking" style="background: #22a06b; color: white; padding: 6px 12px; border-radius: 6px; font-size: 11px; text-decoration: none;">+ Add New Cats</a>
                </div>
                <form action="Booking" method="post" id="bookingForm">
                    <table>
                        <thead>
                        <tr><th>Select</th><th>Photo</th><th>Name</th><th>Age</th><th>Gender</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${catList}" var="c" varStatus="status">
                            <tr>
                                <td><input type="radio" name="catID" value="${c.catID}" ${param.catID == c.catID || (status.first && empty param.catID) ? 'checked' : ''} style="accent-color:#ff6600"></td>
                                <td><img src="${pageContext.request.contextPath}/${c.img}" class="pet-img" alt="${c.name}"></td>
                                <td><strong>${c.name}</strong></td>
                                <td>${c.age == 0 ? '&lt; 1' : c.age} Years</td>
                                <td><span class="badge ${c.gender == 1 ? 'male' : 'female'}">${c.gender == 1 ? 'Male' : 'Female'}</span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${totalPage > 1}">
                    <div class="pagination">
                        <c:if test="${indexPage > 1}">
                            <a href="Booking?indexPage=${indexPage - 1}&serviceID=${param.serviceID}&assigneeInfo=${param.assigneeInfo}&startDate=${param.startDate}&endDate=${param.endDate}">
                                <i class="bi bi-chevron-left"></i>
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPage}" var="i">
                            <a href="Booking?indexPage=${i}&serviceID=${param.serviceID}&assigneeInfo=${param.assigneeInfo}&startDate=${param.startDate}&endDate=${param.endDate}"
                               class="${i == indexPage ? 'active' : ''}">
                                    ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${indexPage < totalPage}">
                            <a href="Booking?indexPage=${indexPage + 1}&serviceID=${param.serviceID}&assigneeInfo=${param.assigneeInfo}&startDate=${param.startDate}&endDate=${param.endDate}">
                                <i class="bi bi-chevron-right"></i>
                            </a>
                        </c:if>
                    </div>
                    </c:if>
            </div>
        </div>

        <div class="right-panel">
            <div class="card">
                <div class="card-header"><h4><i class="bi bi-pencil-square"></i> Appointment Details</h4></div>

                <div class="selection-grid ${param.serviceID == '2' ? 'hotel-layout' : ''}">
                    <div>
                        <label>Service</label>
                        <select name="serviceID" id="serviceSelect" required onchange="this.form.action='Booking'; this.form.method='get'; this.form.submit();">
                            <option value="">-- Select --</option>
                            <c:forEach items="${serviceList}" var="ser">
                                <option value="${ser.serviceID}" data-price="${ser.price}" ${param.serviceID == ser.serviceID ? 'selected' : ''}>${ser.nameService}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label>${param.serviceID == '2' ? 'Care Staff' : 'Veterinarian'}</label>
                        <select name="assigneeInfo" id="assigneeSelect" required onchange="this.form.action='Booking'; this.form.method='get'; this.form.submit();">
                            <option value="">-- Select --</option>
                            <c:forEach items="${listPerson}" var="p">
                                <option value="${p.userID}" ${param.assigneeInfo == p.userID ? 'selected' : ''}>${p.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label>${param.serviceID == '2' ? 'Start Date' : 'Date'}</label>
                        <input type="date" name="startDate" id="startDate" value="${empty param.startDate ? currentDate : param.startDate}" min="${currentDate}" onchange="this.form.action='Booking'; this.form.method='get'; this.form.submit();">
                    </div>
                    <c:if test="${param.serviceID == '2'}">
                        <div>
                            <label>Check-in Time</label>
                            <select name="checkInTime" id="checkInTime" required>
                                <c:forEach begin="7" end="17" var="h">
                                    <c:if test="${h != 12}">
                                        <c:set var="hour" value="${h < 10 ? '0' : ''}${h}" />
                                        <option value="${hour}:00:00">
                                                ${hour}:00
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>

                        <div>
                            <label>End Date</label>
                            <input type="date" name="endDate" id="endDate"
                                   value="${param.endDate}"
                                   min="${param.startDate}"
                                   required
                                   onchange="calculatePrice()">
                        </div>
                    </c:if>
                </div>

                <div id="costSummaryArea" class="boarding-summary" style="display: none;">
                    <div class="summary-row">
                        <span id="priceLabel">Service Price:</span>
                        <strong id="priceBaseDisplay">0 VND</strong>
                    </div>
                    <div class="summary-row" style="color: #22a06b; font-weight: 700;">
                        <span>Deposit (20%):</span>
                        <strong id="depositDisplay">0 VND</strong>
                    </div>
                    <div class="summary-total">
                        <span id="totalLabel">Estimated Total:</span>
                        <span id="totalDisplay">0 VND</span>
                    </div>
                </div>

                <c:if test="${param.serviceID != '2' && not empty param.serviceID}">
                    <div class="card-header" style="border:none; margin-top:10px;"><h4 style="color:#9ca3af; font-size:12px;"><i class="bi bi-clock"></i> Veterinarian's Schedule</h4></div>
                    <div class="schedule-scroll">
                        <c:forEach items="${slotListGrouped}" var="entry">
                            <div class="day-group">
                                <span class="day-title">${entry.key}</span>
                                <div class="session-container">
                                    <div class="session-box">
                                        <span class="session-label">Morning</span>
                                        <div class="slot-grid">
                                            <c:forEach items="${entry.value}" var="s">
                                                <c:if test="${s.startTime.hours < 12}">
                                                    <label class="slot">
                                                        <input type="radio" name="slotID" value="${s.slotID}" data-time="${s.startTime.toString().substring(0,5)}" required onchange="calculatePrice()">
                                                            ${s.startTime.toString().substring(0,5)}
                                                    </label>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div class="session-box">
                                        <span class="session-label">Afternoon</span>
                                        <div class="slot-grid">
                                            <c:forEach items="${entry.value}" var="s">
                                                <c:if test="${s.startTime.hours >= 12}">
                                                    <label class="slot">
                                                        <input type="radio" name="slotID" value="${s.slotID}" data-time="${s.startTime.toString().substring(0,5)}" required onchange="calculatePrice()">
                                                            ${s.startTime.toString().substring(0,5)}
                                                    </label>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>

                <c:if test="${param.serviceID == '2'}"><input type="hidden" name="slotID" value="0"></c:if>

                <div style="margin-top: 20px;">
                    <label style="font-weight: bold; display: block; font-size: 11px; color:#9ca3af; margin-bottom: 8px;">SPECIAL NOTES:</label>
                    <textarea name="note" class="note-textarea" placeholder="Describe symptoms...">${param.note}</textarea>
                </div>

                <div class="action-buttons">
                    <button type="button" class="btn-main" id="confirmBtn" disabled>Confirm Appointment</button>
                    <button type="submit" class="btn-pay-now">Pay 20% Deposit Now</button>
                </div>
                </form>
            </div>
        </div>
    </div>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>

<script>

        setTimeout(function(){
        location.reload();
    }, 60000); // 60 giây

function calculatePrice() {
const serviceSelect = document.getElementById('serviceSelect');
        const summaryArea = document.getElementById('costSummaryArea');
        const priceBaseDisplay = document.getElementById('priceBaseDisplay');
        const depositDisplay = document.getElementById('depositDisplay');
        const totalDisplay = document.getElementById('totalDisplay');
        const totalLabel = document.getElementById('totalLabel');
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');
        const confirmBtn = document.getElementById('confirmBtn');

        if (!serviceSelect || serviceSelect.value === "") {
            summaryArea.style.display = "none";
            return;
        }

        const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
        const priceBase = parseFloat(selectedOption.getAttribute('data-price')) || 0;
        const serviceID = serviceSelect.value;
        let finalPrice = 0;

        if (serviceID === '2') {
            summaryArea.style.display = "block";
            document.getElementById('priceLabel').innerText = "Price per day:";
            priceBaseDisplay.innerText = priceBase.toLocaleString('vi-VN') + " VND";

            if (startDateInput.value && endDateInput && endDateInput.value) {
                const start = new Date(startDateInput.value);
                const end = new Date(endDateInput.value);
                const diffTime = Math.abs(end - start);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
                finalPrice = diffDays * priceBase;

                totalLabel.innerHTML = "Estimated Total (<strong>" + diffDays + "</strong> days):";
                confirmBtn.innerText = "Book Appointment from " + startDateInput.value + " to " + endDateInput.value;
            }
        } else {
            const selectedSlot = document.querySelector('input[name="slotID"]:checked');
            if (selectedSlot) {
                summaryArea.style.display = "block";
                finalPrice = priceBase;
                document.getElementById('priceLabel').innerText = "Service Price:";
                priceBaseDisplay.innerText = priceBase.toLocaleString('vi-VN') + " VND";
                totalLabel.innerText = "Total Amount:";

                const selectedTime = selectedSlot.getAttribute('data-time');
                const dayGroup = selectedSlot.closest('.day-group');
                const dayTitle = dayGroup ? dayGroup.querySelector('.day-title').innerText : '';
                confirmBtn.innerText = "Book Appointment at " + selectedTime + " on " + dayTitle;
            } else {
                summaryArea.style.display = "none";
            }
        }

        if (finalPrice > 0) {
            const deposit = finalPrice * 0.2;
            depositDisplay.innerText = deposit.toLocaleString('vi-VN') + " VND";
            totalDisplay.innerText = finalPrice.toLocaleString('vi-VN') + " VND";
        }
    }
    
    // Disable giờ đã qua nếu chọn hôm nay
    function updateCheckInTime() {
        const startDateInput = document.getElementById("startDate");
        const checkInSelect = document.getElementById("checkInTime");

        if (!startDateInput || !checkInSelect) return;

        const selectedDate = startDateInput.value;
        const now = new Date();
        // Chuyển về múi giờ địa phương để so sánh ngày chính xác
        const todayStr = now.getFullYear() + '-' +
            String(now.getMonth() + 1).padStart(2, '0') + '-' +
            String(now.getDate()).padStart(2, '0');

        // Mặc định hiện lại tất cả các giờ
        for (let option of checkInSelect.options) {
            option.style.display = "block";
            option.disabled = false;
        }

        // Nếu khách chọn ngày là hôm nay
        if (selectedDate === todayStr) {
            const currentTotalMinutes = (now.getHours() * 60) + now.getMinutes();

            for (let option of checkInSelect.options) {
                const [hour, minute] = option.value.split(":");
                const optionMinutes = parseInt(hour) * 60 + parseInt(minute);

                // Nếu giờ trong danh sách nhỏ hơn hoặc bằng giờ hiện tại
                if (optionMinutes <= currentTotalMinutes) {
                    option.style.display = "none"; // ẨN HOÀN TOÀN
                    option.disabled = true;        // Chặn chọn bằng phím mũi tên
                }
            }

            // Kiểm tra nếu option đang được chọn bị ẩn, thì tự động chọn option hiện có đầu tiên
            if (checkInSelect.selectedOptions[0] && checkInSelect.selectedOptions[0].style.display === "none") {
                const firstAvailable = Array.from(checkInSelect.options).find(opt => opt.style.display === "block");
                if (firstAvailable) {
                    checkInSelect.value = firstAvailable.value;
                } else {
                    // Nếu hết sạch giờ trong ngày, có thể báo hết chỗ hoặc yêu cầu chọn ngày mai
                    checkInSelect.value = "";
                }
            }
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        updateCheckInTime();
        const startDateInput = document.getElementById("startDate");
        if (startDateInput) {
            startDateInput.addEventListener("change", updateCheckInTime);
        }
        calculatePrice();
    });
    // Thêm vào phần <script> ở cuối file
document.addEventListener('DOMContentLoaded', function() {
    // Xử lý radio buttons cho slot
    const slotRadios = document.querySelectorAll('input[name="slotID"]');
    
    slotRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            // Bỏ class active khỏi tất cả các slot
            document.querySelectorAll('.slot').forEach(slot => {
                slot.classList.remove('active-slot');
            });
            
            // Thêm class active cho slot được chọn
            if (this.checked) {
                this.closest('.slot').classList.add('active-slot');
            }
        });
    });
});
</script>
</body>
</html>