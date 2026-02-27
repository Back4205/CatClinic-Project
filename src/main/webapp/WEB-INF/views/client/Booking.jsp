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
        /* === GIỮ NGUYÊN TOÀN BỘ CSS CŨ + THÊM RẤT ÍT === */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f5f7fa; }
        .container { width: 1200px; margin: 30px auto; }
        h2 { color: #1f2937; font-weight: 800; text-transform: uppercase; border-left: 5px solid #ff6600; padding-left: 15px; margin-bottom: 30px; }
        .booking-wrapper { display: flex; gap: 30px; align-items: flex-start; }
        .left-panel { width: 45%; }
        .right-panel { width: 55%; }
        .card { background: #fff; padding: 25px; border-radius: 12px; margin-bottom: 25px; border: 1px solid #e5e7eb; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
        .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f3f4f6; padding-bottom: 10px; }
        .card-header h4 { margin: 0; color: #374151; font-size: 16px; text-transform: uppercase; }
        .selection-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 15px; margin-bottom: 25px; }
        .selection-grid.four-col { grid-template-columns: 1fr 1fr 1fr 1fr !important; }
        .selection-grid label { display: block; font-size: 12px; font-weight: 700; color: #6b7280; text-transform: uppercase; margin-bottom: 8px; }
        .selection-grid select, .selection-grid input[type="date"], .selection-grid input[type="time"] { width: 100%; padding: 10px; border-radius: 8px; border: 1.5px solid #e5e7eb; background-color: #fff; color: #374151; font-size: 13px; }
        table { width: 100%; border-collapse: collapse; }
        th { background: #f9fafb; padding: 10px; text-align: left; font-size: 11px; color: #6b7280; text-transform: uppercase; }
        td { padding: 10px; border-bottom: 1px solid #f3f4f6; vertical-align: middle; font-size: 13px; }
        .pet-img { width: 40px; height: 40px; border-radius: 6px; object-fit: cover; }
        .badge { padding: 3px 8px; border-radius: 12px; font-size: 10px; font-weight: 700; }
        .male { background: #e0f2fe; color: #0369a1; }
        .female { background: #fce7f3; color: #9d174d; }
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
        .slot.active-slot { background: #ff6600 !important; border-color: #ff6600 !important; color: white !important; }
        .boarding-summary { padding: 20px; background: #fff7f0; border: 1px solid #ffd8bf; border-radius: 8px; color: #ff6600; margin-bottom: 15px; }
        .summary-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 13px; }
        .summary-total { display: flex; justify-content: space-between; border-top: 1px dashed #ffd8bf; padding-top: 10px; margin-top: 5px; font-size: 16px; font-weight: 800; }
        .btn-main { width: 100%; padding: 15px; border: none; border-radius: 8px; margin-top: 10px; font-weight: 800; text-transform: uppercase; background: #ff6600; color: white; }
        .btn-pay-now { display: block; width: 100%; padding: 15px; margin-top: 12px; border: 2px solid #ff6600; border-radius: 8px; background: #fff; color: #ff6600; text-align: center; text-decoration: none; font-weight: 800; font-size: 15px; text-transform: uppercase; transition: 0.3s; cursor: pointer; }
        .btn-pay-now:hover { background: #ff6600; color: #fff; }
        .note-textarea { width: 100%; padding: 12px; border-radius: 8px; border: 1.5px solid #e5e7eb; min-height: 80px; font-size: 13px; font-family: inherit; }
        .booking-error { margin-top: 12px; padding: 12px; border-radius: 8px; background: #fee2e2; color: #b91c1c; font-size: 13px; font-weight: 600; border: 1px solid #fecaca; display: flex; align-items: center; gap: 8px; }
        .hidden { display: none !important; }
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container">
    <h2><i class="bi bi-calendar-check"></i> Book Appointment</h2>
    <div class="booking-wrapper">
        <div class="left-panel">
            <!-- OWNER + CATS giữ nguyên -->
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
                    <!-- table cats giữ nguyên -->
                    <table>
                        <thead><tr><th>Select</th><th>Photo</th><th>Name</th><th>Age</th><th>Gender</th></tr></thead>
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
            </div>
        </div>
    </div>

    <div class="right-panel">
        <div class="card">
            <div class="card-header"><h4><i class="bi bi-pencil-square"></i> Appointment Details</h4></div>

            <div class="selection-grid ${isBoarding ? 'four-col' : ''}" id="selectionGrid">
                <!-- CATEGORY -->
                <div>
                    <label>Category</label>
                    <select name="categoryID" id="categorySelect" required onchange="this.form.submit();">
                        <option value="">-- Select Category --</option>
                        <c:forEach items="${categoryList}" var="cat">
                            <option value="${cat.categoryID}" ${selectedCategoryID == cat.categoryID ? 'selected' : ''}>${cat.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- SERVICE -->
                <div>
                    <label>Service</label>
                    <select name="serviceID" id="serviceSelect" required onchange="calculatePrice()">
                        <option value="">-- Select Service --</option>
                        <c:forEach items="${serviceList}" var="ser">
                            <option value="${ser.serviceID}" data-price="${ser.price}" ${param.serviceID == ser.serviceID ? 'selected' : ''}>${ser.nameService}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- ASSIGNEE (Veterinarian) - chỉ hiện khi needsVet -->
                <div id="assigneeDiv" class="${needsVet ? '' : 'hidden'}">
                    <label>Veterinarian</label>
                    <select name="assigneeInfo" id="assigneeSelect" required onchange="this.form.submit();">
                        <option value="">-- Select Veterinarian --</option>
                        <c:forEach items="${listPerson}" var="p">
                            <option value="${p.userID}" ${param.assigneeInfo == p.userID ? 'selected' : ''}>${p.fullName}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- START DATE -->
                <div>
                    <label id="dateLabel">${isBoarding ? 'Start Date' : 'Date'}</label>
                    <input type="date" name="startDate" id="startDate" value="${empty param.startDate ? currentDate : param.startDate}" min="${currentDate}" onchange="this.form.submit(); calculatePrice();">
                </div>

                <!-- CHECK-IN TIME + END DATE (chỉ Boarding) -->
                <div id="checkInDiv" class="${isBoarding ? '' : 'hidden'}">
                    <label>Check-in Time</label>
                    <select name="checkInTime" id="checkInTime" required onchange="calculatePrice()">
                        <c:forEach begin="7" end="17" var="h">
                            <c:if test="${h != 12}">
                                <option value="${h<10?'0':''}${h}:00:00">${h<10?'0':''}${h}:00</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div id="endDateDiv" class="${isBoarding ? '' : 'hidden'}">
                    <label>End Date</label>
                    <input type="date" name="endDate" id="endDate" value="${param.endDate}" min="${param.startDate}" required onchange="calculatePrice()">
                </div>
            </div>

            <!-- SLOT SCHEDULE - chỉ hiện khi needsVet -->
            <c:if test="${needsVet && not empty slotListGrouped}">
                <div class="card-header" style="border:none; margin-top:10px;"><h4 style="color:#9ca3af; font-size:12px;"><i class="bi bi-clock"></i> Veterinarian's Schedule - 1h/Slot</h4></div>
                <div class="schedule-scroll">
                    <c:forEach items="${slotListGrouped}" var="entry">
                        <div class="day-group">
                            <span class="day-title">${entry.key}</span>
                            <div class="session-container">
                                <!-- Morning / Afternoon giữ nguyên -->
                                ... (giữ nguyên phần slot-grid như file cũ của bạn)
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <!-- NOTE -->
            <div style="margin-top: 20px;">
                <label style="font-weight: bold; display: block; font-size: 11px; color:#9ca3af; margin-bottom: 8px;">SPECIAL NOTES:</label>
                <textarea name="note" class="note-textarea" placeholder="Describe symptoms...">${param.note}</textarea>
            </div>

            <!-- COST SUMMARY -->
            <div id="costSummaryArea" class="boarding-summary" style="display: none;">
                <!-- JS sẽ fill -->
            </div>

            <div class="action-buttons">
                <button type="button" class="btn-main" id="confirmBtn" disabled>Confirm Appointment</button>
                <button type="submit" class="btn-pay-now">Pay 20% Deposit Now</button>
                <c:if test="${not empty error}">
                    <div class="booking-error"><i class="bi bi-exclamation-circle"></i> ${error}</div>
                </c:if>
            </div>
            </form>
        </div>
    </div>
</div>
</div>

<script>
    // ====================== TOÀN BỘ JS MỚI ======================
    function calculatePrice() {
        const serviceSelect = document.getElementById('serviceSelect');
        if (!serviceSelect || !serviceSelect.value) return;

        const priceBase = parseFloat(serviceSelect.options[serviceSelect.selectedIndex].getAttribute('data-price')) || 0;
        const isBoarding = ${isBoarding ? 'true' : 'false'};
        let finalPrice = priceBase;
        let days = 1;

        const summaryArea = document.getElementById('costSummaryArea');
        summaryArea.style.display = 'block';

        if (isBoarding) {
            const start = new Date(document.getElementById('startDate').value);
            const end = new Date(document.getElementById('endDate').value);
            if (start && end) {
                days = Math.ceil((end - start) / (1000*60*60*24)) + 1;
                finalPrice = priceBase * days;
            }
            document.getElementById('priceLabel').innerText = "Price per day:";
        } else {
            document.getElementById('priceLabel').innerText = "Service Price:";
        }

        document.getElementById('priceBaseDisplay').innerText = priceBase.toLocaleString('vi-VN') + " VND";
        document.getElementById('totalDisplay').innerText = finalPrice.toLocaleString('vi-VN') + " VND";
        document.getElementById('depositDisplay').innerText = (finalPrice * 0.2).toLocaleString('vi-VN') + " VND";
        document.getElementById('totalLabel').innerHTML = `Estimated Total (<strong>${days}</strong> days):`;
    }

    document.addEventListener("DOMContentLoaded", function () {
        calculatePrice();
        // active slot
        document.querySelectorAll('input[name="slotID"]').forEach(radio => {
            radio.addEventListener('change', () => {
                document.querySelectorAll('.slot').forEach(s => s.classList.remove('active-slot'));
                radio.closest('.slot').classList.add('active-slot');
            });
        });
    });
</script>
</body>
</html>