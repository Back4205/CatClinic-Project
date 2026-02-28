<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Book Appointment | CatClinic</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
  <style>
    /* === RESET & LAYOUT === */
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Segoe UI', Roboto, sans-serif; background: #f5f7fa; color: #374151; min-height: 100vh; }
    .submitting { opacity: 0.5; pointer-events: none; }
    .container { width: 1300px; margin: 30px auto; padding: 0 20px; }
    h2 { color: #1f2937; font-weight: 800; text-transform: uppercase; border-left: 5px solid #ff6600; padding-left: 15px; margin-bottom: 30px; }

    .booking-wrapper { display: flex; gap: 30px; align-items: flex-start; }

    /* Panels */
    .left-panel { flex: 1; min-width: 480px; position: sticky; top: 20px; align-self: flex-start; }
    .right-panel { flex: 1.3; min-width: 650px; }

    .card { background: #fff; padding: 25px; border-radius: 12px; margin-bottom: 25px; border: 1px solid #e5e7eb; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05); }
    .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f3f4f6; padding-bottom: 12px; }
    .card-header h4 { margin: 0; color: #1f2937; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px; }

    /* Tables & Pagination */
    table { width: 100%; border-collapse: collapse; }
    th { background: #f9fafb; padding: 12px; text-align: left; font-size: 11px; color: #6b7280; text-transform: uppercase; }
    td { padding: 12px; border-bottom: 1px solid #f3f4f6; font-size: 13px; vertical-align: middle; }
    .pet-img { width: 45px; height: 45px; border-radius: 8px; object-fit: cover; border: 1px solid #eee; }
    .badge { padding: 4px 10px; border-radius: 12px; font-size: 10px; font-weight: 700; text-transform: uppercase; }
    .male { background: #e0f2fe; color: #0369a1; }
    .female { background: #fce7f3; color: #9d174d; }

    .pagination-btn { padding: 8px 14px; border: 1px solid #d1d5db; border-radius: 6px; background: #fff; cursor: pointer; text-decoration: none; color: inherit; font-size: 13px; }
    .pagination-btn.active { background: #ff6600; color: #fff; border-color: #ff6600; font-weight: bold; }

    /* Form Controls */
    .selection-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 25px; }
    .selection-grid label { display: block; font-size: 11px; font-weight: 700; color: #6b7280; text-transform: uppercase; margin-bottom: 8px; }
    .selection-grid select, .selection-grid input { width: 100%; padding: 11px; border-radius: 8px; border: 1.5px solid #e5e7eb; font-size: 13px; outline: none; transition: 0.2s; }
    .selection-grid select:focus, .selection-grid input:focus { border-color: #ff6600; box-shadow: 0 0 0 3px rgba(255, 102, 0, 0.1); }

    /* Slots Grid */
    .schedule-scroll { max-height: 400px; overflow-y: auto; padding: 15px; border: 1px solid #f3f4f6; border-radius: 10px; background: #fafafa; }
    .day-title { font-size: 13px; font-weight: 800; color: #ff6600; margin: 15px 0 10px 0; display: block; }
    .day-title:first-of-type { margin-top: 0; }
    .slot-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 20px; }
    .slot { padding: 10px; border: 1px solid #e5e7eb; border-radius: 8px; cursor: pointer; text-align: center; font-size: 12px; font-weight: 700; background: #fff; color: #4b5563; transition: 0.2s; }
    .slot:hover { border-color: #ff6600; color: #ff6600; }
    .slot input { display: none; }
    .slot.active-slot { background: #ff6600 !important; border-color: #ff6600 !important; color: #fff !important; }

    /* Buttons & Summary */
    .boarding-summary { padding: 20px; background: #fff7f0; border: 1px solid #ffd8bf; border-radius: 10px; margin-top: 25px; }
    .summary-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 13px; }
    .summary-total { display: flex; justify-content: space-between; border-top: 1px dashed #ffd8bf; padding-top: 10px; margin-top: 10px; font-size: 16px; font-weight: 800; color: #ff6600; }

    .btn-main { width: 100%; padding: 16px; border: none; border-radius: 8px; background: #ff6600; color: #fff; font-weight: 800; text-transform: uppercase; cursor: pointer; margin-top: 20px; transition: 0.3s; display: flex; align-items: center; justify-content: center; gap: 10px; }
    .btn-main:hover { background: #e65c00; }
    .btn-main.ready { background: #059669 !important; }

    .btn-pay-now { display: block; width: 100%; padding: 15px; margin-top: 12px; border: 2px solid #ff6600; border-radius: 8px; background: #fff; color: #ff6600; text-align: center; text-decoration: none; font-weight: 800; text-transform: uppercase; transition: 0.3s; }
    .btn-pay-now:hover { background: #ff6600; color: #fff; }
    .no-slots-message { padding: 30px; text-align: center; color: #6b7280; background: #f9fafb; border-radius: 8px; border: 1px dashed #d1d5db; }
  </style>
</head>
<body>

<%@include file="header.jsp" %>

<div class="container">
  <h2><i class="bi bi-calendar-check"></i> Book Appointment</h2>

  <form action="Booking" method="post" id="bookingForm">
    <div class="booking-wrapper">

      <!-- LEFT PANEL - Owner & Cats -->
      <div class="left-panel">
        <div class="card">
          <div class="card-header"><h4><i class="bi bi-person-badge"></i> Owner Information</h4></div>
          <div class="owner-info">
            <p style="margin-bottom: 8px;"><strong>Name:</strong> ${acc.fullName}</p>
            <p style="margin-bottom: 8px;"><strong>Phone:</strong> ${acc.phone}</p>
            <p><strong>Email:</strong> ${acc.email}</p>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <h4><i class="bi bi-github"></i> Your Cats</h4>
            <a href="${pageContext.request.contextPath}/cats/cat-add?from=booking"
               style="background: #22a06b; color: #fff; padding: 6px 12px; border-radius: 6px; font-size: 11px; text-decoration: none; font-weight: 600;">
              <i class="bi bi-plus-lg"></i> Add New
            </a>
          </div>
          <table>
            <thead>
            <tr>
              <th style="width: 50px;">Select</th>
              <th style="width: 70px;">Photo</th>
              <th>Name</th>
              <th style="width: 80px;">Age</th>
              <th>Gender</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${catList}" var="c">
              <tr>
                <td><input type="radio" name="catID" value="${c.catID}" ${(param.catID == c.catID || selectedCatID == c.catID) ? 'checked' : ''} required></td>
                <td><img src="${pageContext.request.contextPath}/${c.img}" class="pet-img" alt="${c.name}"></td>
                <td><strong>${c.name}</strong></td>
                <td>${c.age} Yr${c.age > 1 ? 's' : ''}</td>
                <td><span class="badge ${c.gender == 1 ? 'male' : 'female'}">${c.gender == 1 ? 'Male' : 'Female'}</span></td>
              </tr>
            </c:forEach>
            <c:if test="${empty catList}">
              <tr><td colspan="5" class="no-slots-message">No cats registered yet.</td></tr>
            </c:if>
            </tbody>
          </table>

          <c:if test="${not empty totalPages && totalPages > 1}">
            <div style="margin-top: 20px; display: flex; justify-content: center; gap: 8px;">
              <c:if test="${currentPage > 1}">
                <button type="button" onclick="changePage(${currentPage - 1})" class="pagination-btn">Prev</button>
              </c:if>
              <c:forEach begin="1" end="${totalPages}" var="i">
                <button type="button" onclick="changePage(${i})" class="pagination-btn ${i == currentPage ? 'active' : ''}">${i}</button>
              </c:forEach>
              <c:if test="${currentPage < totalPages}">
                <button type="button" onclick="changePage(${currentPage + 1})" class="pagination-btn">Next</button>
              </c:if>
            </div>
          </c:if>
        </div>
      </div>

      <!-- RIGHT PANEL - Appointment Details -->
      <div class="right-panel">
        <div class="card">
          <div class="card-header"><h4><i class="bi bi-pencil-square"></i> Appointment Details</h4></div>

          <div class="selection-grid">
            <div>
              <label>Category</label>
              <select name="categoryID" onchange="submitForm()" required>
                <option value="">-- Select Category --</option>
                <c:forEach items="${categoryList}" var="cat">
                  <option value="${cat.categoryID}" ${selectedCategoryID == cat.categoryID ? 'selected' : ''}>${cat.categoryName}</option>
                </c:forEach>
              </select>
            </div>
            <div>
              <label>Service</label>
              <select name="serviceID" id="serviceSelect" onchange="calculatePrice(); updateConfirmButton();" required>
                <option value="">-- Select Service --</option>
                <c:forEach items="${serviceList}" var="ser">
                  <option value="${ser.serviceID}" data-price="${ser.price}" ${(param.serviceID == ser.serviceID || selectedServiceID == ser.serviceID) ? 'selected' : ''}>${ser.nameService}</option>
                </c:forEach>
              </select>
            </div>

            <c:if test="${needsVet}">
              <div>
                <label>Veterinarian</label>
                <select name="assigneeInfo" onchange="submitForm()">
                  <option value="">-- Select Veterinarian --</option>
                  <c:forEach items="${listPerson}" var="p">
                    <option value="${p.userID}" ${param.assigneeInfo == p.userID ? 'selected' : ''}>${p.fullName}</option>
                  </c:forEach>
                </select>
              </div>
            </c:if>

            <div>
              <label>${isBoarding ? 'Start Date' : 'Preferred Date'}</label>
              <input type="date" name="startDate" id="startDate"
                     value="${not empty selectedStartDate ? selectedStartDate : (empty param.startDate ? currentDate : param.startDate)}"
                     min="${currentDate}"
                     onchange="${needsVet ? 'submitForm()' : 'calculatePrice(); updateConfirmButton(); updateEndDateMin();'}">
            </div>

            <c:if test="${isBoarding}">
              <div>
                <label>End Date</label>
                <input type="date" name="endDate" id="endDate"
                       value="${not empty selectedEndDate ? selectedEndDate : param.endDate}"
                       min="${param.startDate}"
                       onchange="calculatePrice(); updateConfirmButton();">
              </div>
              <div>
                  <%-- Trong phần lựa chọn Check-in Time của Boarding --%>
                <c:if test="${isBoarding}">
                  <div>
                    <label>Check-in Time</label>
                    <select name="checkInTime" id="checkInTime" onchange="updateConfirmButton()">
                      <c:forEach begin="7" end="17" var="h">
                        <c:if test="${h != 12}">
                          <%-- Tạo biến tạm để so sánh, tránh lỗi lồng biểu thức EL --%>
                          <c:set var="currentHour" value="${h < 10 ? '0' : ''}${h}:00" />
                          <c:set var="fullTimeValue" value="${currentHour}:00" />

                          <option value="${currentHour}" ${param.checkInTime == currentHour ? 'selected' : ''}>
                              ${currentHour}
                          </option>
                        </c:if>
                      </c:forEach>
                    </select>
                  </div>
                </c:if>
              </div>
            </c:if>
          </div>

          <!-- Available Slots for Vet Services -->
          <c:if test="${needsVet}">
          <label style="font-size: 11px; font-weight: 700; color: #9ca3af; text-transform: uppercase; margin-bottom: 12px; display: block;">
            <i class="bi bi-clock"></i> Available Slots:
          </label>
          <c:choose>
          <c:when test="${not empty slotList}">
          <div class="schedule-scroll">
            <c:set var="lastDate" value="" />
            <c:forEach items="${slotList}" var="slot" varStatus="status">
            <fmt:formatDate value="${slot.slotDate}" pattern="yyyy-MM-dd" var="currentDate" />

            <c:if test="${lastDate != currentDate}">
            <c:if test="${not empty lastDate}">
          </div></div>
        </c:if>
        <div class="day-group">
                                                <span class="day-title">
                                                    <i class="bi bi-calendar3"></i>
                                                    <fmt:formatDate value="${slot.slotDate}" pattern="EEEE, dd/MM/yyyy" />
                                                </span>
          <div class="slot-grid">
            <c:set var="lastDate" value="${currentDate}" />
            </c:if>

            <label class="slot ${param.slotID == slot.slotID ? 'active-slot' : ''}">
              <input type="radio" name="slotID" value="${slot.slotID}"
                ${param.slotID == slot.slotID ? 'checked' : ''}
                     onchange="updateConfirmButton()" required>
                ${fn:substring(slot.startTime.toString(), 0, 5)}
            </label>

            <c:if test="${status.last}">
          </div></div>
        </c:if>
        </c:forEach>
      </div>
      </c:when>
      <c:otherwise>
        <div class="no-slots-message">
          <i class="bi bi-calendar-x" style="font-size: 24px; display: block; margin-bottom: 10px;"></i>
          No slots available for the selected veterinarian and date.
        </div>
      </c:otherwise>
      </c:choose>
      </c:if>

      <!-- Cost Summary Area -->
      <div id="costSummaryArea" class="boarding-summary" style="display: none;">
        <div class="summary-row"><span>Service Price:</span><span id="priceBaseDisplay">0 VND</span></div>
        <div class="summary-total"><span>Total Cost:</span><span id="totalDisplay">0 VND</span></div>
        <div class="summary-row" style="color:#6b7280; font-size:12px;"><span>Deposit (20%):</span><span id="depositDisplay">0 VND</span></div>
      </div>

      <!-- Action Buttons -->
      <div class="action-buttons">
        <button type="submit" name="action" value="book" class="btn-main" id="confirmBtn">Confirm Appointment</button>
        <button type="submit" name="action" value="pay" class="btn-pay-now">Pay Deposit Now</button>
        <c:if test="${not empty error}">
          <div style="color:#b91c1c; font-size:13px; margin-top:10px;">
            <i class="bi bi-exclamation-circle"></i> ${error}
          </div>
        </c:if>
      </div>
    </div>
</div>
</div>
</form>
</div>

<script>
  // 1. Submit form function
  function submitForm() {
    document.body.classList.add('submitting');
    document.getElementById('bookingForm').submit();
  }

  // 2. Change page function
  function changePage(page) {
    const form = document.getElementById('bookingForm');
    const formData = new FormData(form);
    const params = new URLSearchParams();
    for (const [key, value] of formData.entries()) {
      if (value && key !== 'action') params.append(key, value);
    }
    params.set('catPage', page);
    document.body.classList.add('submitting');
    window.location.href = '${pageContext.request.contextPath}/Booking?' + params.toString();
  }

  // 3. Update min date for endDate
  function updateEndDateMin() {
    const startDate = document.getElementById('startDate');
    const endDate = document.getElementById('endDate');
    if (startDate && endDate) {
      endDate.min = startDate.value;
      if (endDate.value && endDate.value < startDate.value) {
        endDate.value = startDate.value;
      }
    }
  }

  // 4. Calculate price function
  function calculatePrice() {
    const sel = document.getElementById('serviceSelect');
    const area = document.getElementById('costSummaryArea');

    if (!sel || !sel.value) {
      if (area) area.style.display = 'none';
      return;
    }

    const price = parseFloat(sel.selectedOptions[0].dataset.price) || 0;
    let total = price;
    const isB = ${isBoarding ? 'true' : 'false'};

    if (isB) {
      const start = document.getElementById('startDate').value;
      const end = document.getElementById('endDate')?.value;
      if (start && end) {
        const startDate = new Date(start);
        const endDate = new Date(end);
        const days = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24)) + 1;
        if (days > 0) total = price * days;
      }
    }

    if (area) {
      area.style.display = 'block';
      document.getElementById('priceBaseDisplay').textContent = price.toLocaleString('vi-VN') + " VND";
      document.getElementById('totalDisplay').textContent = total.toLocaleString('vi-VN') + " VND";
      document.getElementById('depositDisplay').textContent = (total * 0.2).toLocaleString('vi-VN') + " VND";
    }
  }

  // 5. Update confirm button text
  function updateConfirmButton() {
    const btn = document.getElementById('confirmBtn');
    const date = document.getElementById('startDate')?.value;
    const isB = ${isBoarding ? 'true' : 'false'};
    let time = "";

    if (!btn || !date) return;

    if (isB) {
      time = document.getElementById('checkInTime')?.value?.substring(0, 5);
    } else {
      const sel = document.querySelector('input[name="slotID"]:checked');
      if (sel) {
        const slotLabel = sel.closest('.slot');
        time = slotLabel ? slotLabel.textContent.trim() : "";
      }
    }

    const d = new Date(date);
    const formattedDate = d.getDate().toString().padStart(2,'0') + '/' +
            (d.getMonth()+1).toString().padStart(2,'0');

    if (time) {
      btn.innerHTML = '<i class="bi bi-check-circle-fill"></i> Confirm: ' + time + ' - ' + formattedDate;
      btn.classList.add('ready');
    } else {
      btn.innerHTML = 'Confirm Appointment (' + formattedDate + ')';
      btn.classList.remove('ready');
    }
  }

  // 6. Initialize on page load
  document.addEventListener('DOMContentLoaded', function() {
    // Calculate price and update button
    calculatePrice();
    updateConfirmButton();

    // Update end date min if in boarding mode
    if (${isBoarding ? 'true' : 'false'}) {
      updateEndDateMin();
    }

    // Handle slot selection
    document.querySelectorAll('.slot').forEach(el => {
      el.addEventListener('click', function() {
        const radio = this.querySelector('input');
        if (radio) {
          radio.checked = true;
          document.querySelectorAll('.slot').forEach(s => s.classList.remove('active-slot'));
          this.classList.add('active-slot');
          updateConfirmButton();
        }
      });
    });

    // Pre-select slot if param exists
    const checkedSlot = document.querySelector('input[name="slotID"]:checked');
    if (checkedSlot) {
      checkedSlot.closest('.slot').classList.add('active-slot');
    }
  });
</script>

</body>
</html>