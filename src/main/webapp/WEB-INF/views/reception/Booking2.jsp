<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Book Appointment | CatClinic</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/receptiondashboard-style.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-booking-list.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Invoice/Billing_booking.css">

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

  <style>
    /* CSS tuỳ chỉnh mở rộng dựa trên style guide của dự án (màu cam chủ đạo) */
    .booking-wrapper {
      display: flex;
      gap: 25px;
      align-items: flex-start;
      margin-top: 15px;
    }

    .left-panel {
      flex: 0 0 40%;
      display: flex;
      flex-direction: column;
      gap: 25px;
    }

    .right-panel {
      flex: 1;
    }

    /* Đồng bộ Form input với thiết kế bảng của dự án */
    .form-group {
      margin-bottom: 16px;
    }

    .form-label {
      display: block;
      font-size: 13px;
      font-weight: 700;
      color: #64748B;
      margin-bottom: 8px;
      text-transform: uppercase;
    }

    .form-control {
      width: 100%;
      padding: 10px 14px;
      border: 1px solid #E2E8F0;
      border-radius: 8px;
      font-size: 14px;
      color: #1E293B;
      background-color: #fff;
      transition: all 0.3s ease;
      outline: none;
      height: 42px;
    }

    .form-control:focus {
      border-color: #ea580c;
      box-shadow: 0 0 5px rgba(234, 88, 12, 0.2);
    }

    textarea.form-control {
      min-height: 80px;
      resize: vertical;
      height: auto;
    }

    select.form-control {
      cursor: pointer;
    }

    /* Đồng bộ Time Slots: Ép cứng chia đều 4 cột lấp kín không gian */
    .slot-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20px;
      margin-bottom: 20px;
    }

    .slot-pill {
      background: #F1F5F9;
      border: 1px solid #E2E8F0;
      padding: 10px 0;
      border-radius: 8px;
      font-size: 13px;
      font-weight: 600;
      cursor: pointer;
      color: #444;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;
      transition: background 0.2s ease;
    }

    .slot-pill:hover {
      background: #E2E8F0;
    }

    .slot-pill.active-slot {
      background: #ea580c;
      color: #ffffff;
      border-color: #ea580c;
    }

    /* Lưới chọn phía bên phải */
    .selection-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;
      margin-bottom: 25px;
    }

    .selection-grid .full-width {
      grid-column: 1 / -1;
    }

    /* Summary & Button */
    .cost-summary {
      background: #FFF7ED;
      border: 1px solid #fed7aa;
      border-radius: 12px;
      padding: 16px 20px;
      margin-top: 25px;
    }

    .cost-row {
      display: flex;
      justify-content: space-between;
      margin-bottom: 10px;
      color: #64748B;
      font-size: 14px;
    }

    .cost-total {
      display: flex;
      justify-content: space-between;
      font-weight: 800;
      font-size: 18px;
      color: #EA580C;
      border-top: 1px solid #fed7aa;
      padding-top: 12px;
    }

    .btn-confirm-booking {
      width: 100%;
      background: #FF5C00;
      color: white;
      padding: 14px;
      border-radius: 12px;
      border: none;
      font-size: 16px;
      font-weight: 700;
      cursor: pointer;
      transition: 0.2s;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      margin-top: 25px;
    }

    .btn-confirm-booking:hover {
      background: #EA580C;
      box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3);
    }

    /* Phone search status icon */
    .phone-row-wrapper {
      position: relative;
      display: flex;
      align-items: center;
    }

    .phone-status {
      position: absolute;
      right: 15px;
      font-size: 16px;
    }

    /* Alerts */
    .alert {
      padding: 12px 16px;
      border-radius: 8px;
      margin-top: 15px;
      font-size: 14px;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .alert-error {
      background: #FEF2F2;
      color: #EF4444;
      border: 1px solid #FCA5A5;
    }

    .alert-success {
      background: #DCFCE7;
      color: #16A34A;
      border: 1px solid #86EFAC;
    }

    /* Responsive - Xử lý thu nhỏ màn hình */
    @media (max-width: 1024px) {
      .booking-wrapper { flex-direction: column; }
      .left-panel { flex: unset; width: 100%; }
      .selection-grid { grid-template-columns: 1fr; }
      .slot-grid { grid-template-columns: repeat(4, 1fr); }
    }

    @media (max-width: 768px) {
      .slot-grid { grid-template-columns: repeat(3, 1fr); }
    }

    .pet-img {
      width: 45px;
      height: 45px;
      border-radius: 8px;
      object-fit: cover;
      border: 1px solid #eee;
    }
  </style>
</head>

<body>

<%@include file="header.jsp" %>

<div class="app-container">

  <c:set var="activePage" value="booking" scope="request"/>
  <%@include file="sidebar.jsp" %>

  <main class="main-content">

    <div class="hub-header" style="margin-bottom: 25px;">
      <h2>Book Appointment</h2>
      <p>Receptionist: ${acc.fullName} Create a new booking on behalf of customer.</p>
    </div>

    <form action="Booking2" method="post" id="bookingForm">
      <input type="hidden" name="action" value="Confirm" id="actionField">
      <input type="hidden" name="slotDate" id="slotDate" value="${not empty savedSlotDate ? savedSlotDate : param.slotDate}">

      <div class="booking-wrapper">

        <div class="left-panel">

          <div class="table-section">
            <div class="table-header">
              <h3><i class="fa-regular fa-address-card table-icon"></i> Owner Information </h3>
            </div>

            <div class="form-group">
              <label class="form-label">Phone Number <span style="color:#ef4444">*</span></label>
              <div class="phone-row-wrapper">
                <input type="text" id="ownerPhone" name="phone" class="form-control" min="10" max="11"
                       placeholder="Enter customer phone..."
                       value="${not empty param.phone ? param.phone : (not empty customerInfo ? customerInfo.phone : '')}"
                       onchange="submitForm()" required>
                <span class="phone-status" id="searchStatus">
                                    <c:if test="${not empty customerInfo and customerInfo.userID gt 0}">
                                      <i class="fa-solid fa-circle-check" style="color: #ea580c;"></i>
                                    </c:if>
                                    <c:if test="${empty customerInfo and not empty param.phone}">
                                      <i class="fa-solid fa-circle-info" style="color: #3B82F6;"></i>
                                    </c:if>
                                </span>
              </div>
            </div>


            <div class="form-group">
              <label class="form-label">Full Name</label>
              <input type="text" id="ownerName" name="fullName" class="form-control"
                     value="${not empty param.fullName ? param.fullName : (not empty customerInfo ? customerInfo.fullName : '')}"
                     placeholder="Customer full name" onchange="submitForm()">
            </div>

            <div class="form-group" style="margin-bottom: 0;">
              <label class="form-label">Email Address</label>
              <input type="email" id="ownerEmail" name="email" class="form-control"
                     value="${not empty param.email ? param.email : (not empty customerInfo ? customerInfo.email : '')}"
                     placeholder="example@mail.com" onchange="submitForm()">
            </div>
            <c:if test="${not empty sessionScope.error}">
              <div class="alert alert-error" style="margin-bottom: 15px;">
                <i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}
              </div>
              <c:remove var="error" scope="session" />
            </c:if>
          </div>

          <div class="table-section">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
              <div class="table-header" style="margin-bottom: 0;">
                <h3><i class="fa-solid fa-paw table-icon"></i>Cat List</h3>
              </div>
              <c:choose>
                <c:when test="${not empty customerInfo and customerInfo.userID gt 0}">
                  <a href="${pageContext.request.contextPath}/cats/cat-add?from=booking" class="btn-new-booking" style="padding: 8px 16px; font-size: 13px;">
                    <i class="fa-solid fa-plus"></i> Add New
                  </a>
                </c:when>
                <c:otherwise>
                  <a href="${pageContext.request.contextPath}/cats/cat-add?f=Counterbooking&phone=${param.phone}&fullName=${param.fullName}&email=${param.email}"
                     class="btn-new-booking" style="padding: 8px 16px; font-size: 13px;">
                    <i class="fa-solid fa-plus"></i>Add New Owner and  cat
                  </a>
                </c:otherwise>
              </c:choose>

            </div>

            <table class="modern-table" style="margin-bottom: 0;">
              <thead>
              <tr>
                <th>SELECT</th>
                <th>PHOTO</th>
                <th>NAME</th>
                <th>AGE</th>
                <th>GENDER</th>
              </tr>
              </thead>
              <tbody>
              <c:choose>
                <c:when test="${not empty catList}">
                  <c:forEach items="${catList}" var="c">
                    <tr>
                      <td style="width: 50px;">
                        <input type="radio" name="catID" value="${c.catID}"
                          ${(param.catID == c.catID || selectedCatID == c.catID) ? 'checked' : ''}
                               onchange="submitForm()" required style="accent-color: #ea580c; width: 16px; height: 16px; cursor: pointer;">
                      </td>
                      <td>
                        <div class="patient-cell">
                          <div>
                            <img src="${pageContext.request.contextPath}/${c.img}" class="pet-img" alt="Pet">
                          </div>

                        </div>

                      </td>
                      <td>
                        <div class="patient-cell"> <div>
                          <p class="patient-name">${c.name}</p>
                        </div></div>
                      </td>
                      <td><span class="time-text"> ${c.age} year${c.age > 1 ? 's' : ''}</span></td>
                      <td>
                                                    <span class="badge ${c.gender == 1 ? 'waiting' : 'pending'}">
                                                        ${c.gender == 1 ? 'Male' : 'Female'}
                                                    </span>
                      </td>
                    </tr>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <tr>
                    <td colspan="4" class="empty-message">No cats registered yet. Add one to continue.</td>
                  </tr>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>

            <c:if test="${not empty totalPages && totalPages > 1}">
              <div style="margin-top: 20px; display: flex; justify-content: center; gap: 8px;">
                <c:if test="${currentPage > 1}">
                  <button type="button" onclick="changePage(${currentPage - 1})" class="pagination-btn">
                    <i class="bi bi-chevron-left"></i> Prev
                  </button>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                  <button type="button" onclick="changePage(${i})" class="pagination-btn ${i == currentPage ? 'active' : ''}">
                      ${i}
                  </button>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                  <button type="button" onclick="changePage(${currentPage + 1})" class="pagination-btn">
                    Next <i class="bi bi-chevron-right"></i>
                  </button>
                </c:if>
              </div>
            </c:if>
          </div>

        </div>

        <div class="right-panel">
          <div class="table-section">
            <div class="table-header">
              <h3><i class="fa-solid fa-list-check table-icon"></i> Appointment Details</h3>
            </div>

            <div class="selection-grid">
              <div>
                <label class="form-label">Category <span style="color:#ef4444">*</span></label>
                <select id="categorySelect" name="categoryID" class="form-control" onchange="submitForm()" required>
                  <option value="">-- Select Category --</option>
                  <c:forEach items="${categoryList}" var="cat">
                    <option value="${cat.categoryID}" ${selectedCategoryID == cat.categoryID ? 'selected' : ''}>${cat.categoryName}</option>
                  </c:forEach>
                </select>
              </div>

              <div>
                <label class="form-label">Service <span style="color:#ef4444">*</span></label>
                <select id="serviceSelect" name="serviceID" class="form-control" onchange="calculatePrice(); updateConfirmButton(); submitForm();" required>
                  <option value="">-- Select Service --</option>
                  <c:forEach items="${serviceList}" var="ser">
                    <option value="${ser.serviceID}" data-price="${ser.price}" ${(param.serviceID == ser.serviceID || selectedServiceID == ser.serviceID) ? 'selected' : ''}>${ser.nameService}</option>
                  </c:forEach>
                </select>
              </div>

              <c:if test="${needsVet and not isBoarding and not isParaclinical}">
                <div>
                  <label class="form-label">Veterinarian <span style="color:#ef4444">*</span></label>
                  <select id="vetSelect" name="assigneeInfo" class="form-control" onchange="submitForm()" required>
                    <option value="">-- Select Veterinarian --</option>
                    <c:forEach items="${listPerson}" var="p">
                      <option value="${p.userID}" ${param.assigneeInfo == p.userID ? 'selected' : ''}>${p.fullName}</option>
                    </c:forEach>
                  </select>
                </div>
              </c:if>

              <div>
                <label class="form-label">
                  <c:choose>
                    <c:when test="${isBoarding or isParaclinical}">Start Date</c:when>
                    <c:otherwise>Preferred Date</c:otherwise>
                  </c:choose>
                  <span style="color:#ef4444">*</span>
                </label>
                <input type="date" id="startDate" name="startDate" class="form-control"
                       value="${not empty selectedStartDate ? selectedStartDate : (empty param.startDate ? currentDate : param.startDate)}" min="${currentDate}"
                       onchange="${needsVet ? 'submitForm()' : 'calculatePrice(); updateConfirmButton(); updateEndDateMin(); filterFutureTime();'}" required>
              </div>

              <c:if test="${isBoarding}">
                <div>
                  <label class="form-label">End Date <span style="color:#ef4444">*</span></label>
                  <input type="date" id="endDate" name="endDate" class="form-control"
                         value="${not empty selectedEndDate ? selectedEndDate : param.endDate}" min="${param.startDate}"
                         onchange="calculatePrice(); updateConfirmButton();" required>
                </div>
              </c:if>

              <c:if test="${isBoarding or isParaclinical}">
                <div>
                  <label class="form-label"> ${isParaclinical ? 'Assigned Technician Staff' : 'Assigned Care Staff'} <span style="color:#ef4444">*</span></label>
                  <select name="StaffInfor" class="form-control" onchange="submitForm()" required>
                    <option value="">-- Select Staff --</option>
                    <c:forEach items="${listStaff}" var="s">
                      <option value="${s.staffID}" ${param.StaffInfor == s.staffID ? 'selected' : ''}>${s.fullName}</option>
                    </c:forEach>
                  </select>
                </div>

                <div>
                  <label class="form-label">${isBoarding ? 'Check-in Time' : 'Appointment Time'} <span style="color:#ef4444">*</span></label>
                  <select id="checkInTime" name="checkInTime" class="form-control" onchange="updateConfirmButton()" required>
                    <option value="">-- Select Time --</option>
                    <c:forEach begin="7" end="17" var="h">
                      <c:if test="${h != 12}">
                        <c:set var="hSlot" value="${h < 10 ? '0' : ''}${h}:00"/>
                        <option value="${hSlot}" ${param.checkInTime == hSlot ? 'selected' : ''}>${hSlot}</option>
                      </c:if>
                    </c:forEach>
                  </select>
                </div>
              </c:if>
            </div>

            <c:if test="${needsVet and not isBoarding and not isParaclinical}">
            <div style="margin-top: 10px;">
              <label class="form-label"><i class="fa-regular fa-clock"></i> Available Time Slots</label>
              <c:choose>
              <c:when test="${not empty slotList}">
              <div style="max-height: 250px; overflow-y: auto; padding-right: 8px;">
                <c:set var="lastDate" value=""/>
                <c:forEach items="${slotList}" var="slot" varStatus="status">
                <fmt:formatDate value="${slot.slotDate}" pattern="yyyy-MM-dd" var="cDate"/>
                <fmt:formatDate value="${slot.slotDate}" pattern="dd/MM/yyyy" var="displayDate"/>

                <c:if test="${lastDate != cDate}">
                <c:if test="${not empty lastDate}">
              </div> </div> </c:if>
            <div style="margin-bottom: 20px;">
              <div style="font-size: 13px; font-weight: 700; color: #EA580C; margin-bottom: 12px; text-transform: uppercase;">
                <i class="fa-regular fa-calendar-check"></i> <fmt:formatDate value="${slot.slotDate}" pattern="EEEE, dd/MM/yyyy"/>
              </div>
              <div class="slot-grid">
                <c:set var="lastDate" value="${cDate}"/>
                </c:if>

                <c:set var="isChecked" value="false"/>
                <c:if test="${not empty param.slotID and param.slotID == slot.slotID and not empty param.slotDate and param.slotDate == cDate}">
                  <c:set var="isChecked" value="true"/>
                </c:if>

                <label class="slot-pill ${isChecked ? 'active-slot' : ''}" onclick="selectSlot(this, ${slot.slotID}, '${cDate}')">
                  <input type="radio" name="slotID" value="${slot.slotID}"
                         data-slot-date="${cDate}" data-date="${displayDate}"
                         data-time="${fn:substring(slot.startTime.toString(), 0, 5)}"
                         data-vet-id="${slot.vetID}"
                    ${isChecked ? 'checked' : ''} style="display:none"/>
                    ${fn:substring(slot.startTime.toString(), 0, 5)}
                </label>

                <c:if test="${status.last}">
              </div> </div> </c:if>
            </c:forEach>
          </div>
          </c:when>
          <c:otherwise>
            <div class="empty-message">No available slots. Please select a different date or veterinarian.</div>
          </c:otherwise>
          </c:choose>
        </div>
        </c:if>

        <hr style="border: none; border-top: 1px solid #E2E8F0; margin: 25px 0;">

        <div class="form-group" style="margin-bottom: 0;">
          <label class="form-label"><i class="fa-regular fa-comment-dots"></i> Note / Special Request</label>
          <textarea name="note" class="form-control" placeholder="Enter Note ">${not empty savedNote ? savedNote : param.note}</textarea>
        </div>

        <div id="costSummaryArea" class="cost-summary" style="display:${not empty param.serviceID ? 'block' : 'none'}">
          <div class="cost-row">
            <span>Service Price</span>
            <span id="priceBaseDisplay">
                                    <fmt:formatNumber value="${servicePrice}" type="currency" currencySymbol="VND" maxFractionDigits="0"/>
                                </span>
          </div>
          <div class="cost-total">
            <span>Total Cost</span>
            <span id="totalDisplay">
                                    <fmt:formatNumber value="${totalCost}" type="currency" currencySymbol="VND" maxFractionDigits="0"/>
                                </span>
          </div>
        </div>

        <button type="submit" class="btn-confirm-booking" id="confirmBtn">
          <i class="fa-solid fa-calendar-check"></i> Booking Now
        </button>

        <c:if test="${not empty error}">
          <div class="alert alert-error"><i class="fa-solid fa-circle-exclamation"></i> ${error}</div>
        </c:if>


      </div>
</div>

</div>
</form>

</main>
</div>

<script>
  const contextPath    = '${pageContext.request.contextPath}';
  const isBoardingMode = ${not empty isBoarding and isBoarding ? 'true' : 'false'};
  const isParaclinicalMode  = ${not empty isParaclinical  and isParaclinical  ? 'true' : 'false'};
  const needsVetMode   = ${not empty needsVet   and needsVet   ? 'true' : 'false'};

  function submitForm() {
    document.getElementById('actionField').value = 'load';
    document.getElementById('bookingForm').submit();
  }

  function setSlotDate() {
    const r = document.querySelector('input[name="slotID"]:checked');
    if (r) document.getElementById('slotDate').value = r.getAttribute('data-slot-date');
  }

  function highlightSlot(radio) {
    document.querySelectorAll('.slot-pill').forEach(p => p.classList.remove('active-slot'));
    if (radio && radio.checked) {
      const lbl = radio.closest('.slot-pill');
      if (lbl) lbl.classList.add('active-slot');
    }
  }

  function selectSlot(labelEl, slotID, slotDate) {
    const radio = labelEl.querySelector('input[type="radio"]');
    if (radio) {
      radio.checked = true;
      highlightSlot(radio);
      setSlotDate();
      updateConfirmButton();
    }
  }

  function changePage(page) {
    const params = new URLSearchParams();
    for (const [k, v] of new FormData(document.getElementById('bookingForm')).entries()) {
      if (v && k !== 'action') params.append(k, v);
    }
    params.set('catPage', page);
    window.location.href = contextPath + '/Booking?' + params.toString();
  }

  function updateEndDateMin() {
    const s = document.getElementById('startDate');
    const e = document.getElementById('endDate');
    if (!s || !e) return;
    e.min = s.value;
    if (e.value && e.value < s.value) e.value = s.value;
  }

  function filterFutureTime() {
    if (!isBoardingMode && !isParaclinicalMode) return;
    const d = document.getElementById('startDate');
    const t = document.getElementById('checkInTime');
    if (!d || !t || !d.value) return;

    const now   = new Date();
    const today = now.toISOString().slice(0, 10);
    const curH  = now.getHours();
    const curM  = now.getMinutes();

    Array.from(t.options).forEach(opt => {
      if (!opt.value) return;
      const h = parseInt(opt.value);
      opt.style.display = (d.value === today && (h < curH || (h === curH && curM > 0))) ? 'none' : 'block';
    });

    const sel = t.selectedOptions[0];
    if (sel && sel.style.display === 'none') t.value = '';
  }

  function calculatePrice() {
    const sel  = document.getElementById('serviceSelect');
    const area = document.getElementById('costSummaryArea');
    if (!sel || !sel.value) {
      if (area) area.style.display = 'none';
      return;
    }

    const price = parseFloat(sel.selectedOptions[0].dataset.price) || 0;
    let total   = price;

    if (isBoardingMode) {
      const s = document.getElementById('startDate')?.value;
      const e = document.getElementById('endDate')?.value;
      if (s && e) {
        const days = Math.ceil((new Date(e) - new Date(s)) / 86400000) + 1;
        if (days > 0) total = price * days;
      }
    }

    if (!area) return;
    area.style.display = 'block';
    document.getElementById('priceBaseDisplay').textContent = price.toLocaleString('vi-VN') + ' VND';
    document.getElementById('totalDisplay').textContent     = total.toLocaleString('vi-VN') + ' VND';
  }

  function updateConfirmButton() {
    const btn = document.getElementById('confirmBtn');
    if (!btn) return;

    let time = '', fDate = '';
    if (isBoardingMode || isParaclinicalMode) {
      const d = document.getElementById('startDate')?.value;
      const t = document.getElementById('checkInTime')?.value;
      if (d && t) {
        const p = d.split('-');
        fDate = p[2] + '/' + p[1] + '/' + p[0];
        time  = t.substring(0, 5);
      }
    } else if (needsVetMode) {
      const r = document.querySelector('input[name="slotID"]:checked');
      if (r) {
        fDate = r.dataset.date;
        time = r.dataset.time;
      }
    }

    if (time && fDate) {
      btn.innerHTML = '<i class="fa-solid fa-circle-check"></i> Confirm: ' + time + ' — ' + fDate;
    } else {
      btn.innerHTML = '<i class="fa-solid fa-calendar-check"></i> Booking Now';
    }
  }

  document.addEventListener('DOMContentLoaded', function () {
    calculatePrice();
    updateConfirmButton();
    filterFutureTime();
    if (isBoardingMode) updateEndDateMin();

    const checked = document.querySelector('input[name="slotID"]:checked');
    if (checked) {
      highlightSlot(checked);
      setSlotDate();
      updateConfirmButton();
    }

    const phone = document.getElementById('ownerPhone');
    if (phone) {
      phone.addEventListener('blur', function () {
        if (this.value.trim()) submitForm();
      });
    }
  });
</script>

</body>
</html>