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


    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">




    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/Booking.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link href="${pageContext.request.contextPath}/css/receptiondashboard-style.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/counter-booking-style.css">


</head>
</head>
<body>


<div class="page-shell">


    <%@include file="header.jsp" %>


    <div class="main-body">


        <%-- SIDEBAR — sidebar.jsp render <aside class="sidebar"> --%>
        <%@include file="sidebar.jsp" %>


        <%-- BOOKING CONTENT --%>
        <div class="booking-content-area">


            <h1 class="page-title">
                <i class="bi bi-calendar-check"></i> Book Appointment
            </h1>


            <form action="${pageContext.request.contextPath}/reception/counter-booking" method="post" id="bookingForm">
                <input type="hidden" name="action"   value="Confirm" id="actionField">
                <input type="hidden" name="slotDate" id="slotDate"
                       value="${not empty savedSlotDate ? savedSlotDate : param.slotDate}">


                <div class="booking-wrapper">


                    <%-- ========== LEFT PANEL ========== --%>
                    <div class="left-panel">


                        <%-- Customer Info --%>
                        <div class="bk-card">
                            <div class="bk-card-header">
                                <h4><i class="bi bi-person-badge"></i> Customer Information</h4>
                            </div>
                            <div class="bk-card-body">


                                <div class="form-group">
                                    <label class="bk-label">Phone Number <span style="color:var(--danger)">*</span></label>
                                    <div class="phone-row">
                                        <input type="text" id="ownerPhone" name="phone" class="bk-input"
                                               placeholder="Enter customer phone..."
                                               value="${not empty param.phone ? param.phone : (not empty customerInfo ? customerInfo.phone : '')}"
                                               onchange="submitForm()" required>
                                        <span class="phone-status" id="searchStatus">
                                           <c:if test="${not empty customerInfo and customerInfo.userID gt 0}">
                                               <i class="bi bi-check-circle-fill" style="color:var(--orange)"></i>
                                           </c:if>
                                           <c:if test="${empty customerInfo and not empty param.phone}">
                                               <i class="bi bi-info-circle-fill" style="color:#3b82f6"></i>
                                           </c:if>
                                       </span>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <label class="bk-label">Full Name</label>
                                    <input type="text" id="ownerName" name="fullName" class="bk-input"
                                           value="${not empty param.fullName ? param.fullName : (not empty customerInfo ? customerInfo.fullName : '')}"
                                           placeholder="Customer full name" onchange="submitForm()">
                                </div>


                                <div class="form-group">
                                    <label class="bk-label">Email Address</label>
                                    <input type="email" id="ownerEmail" name="email" class="bk-input"
                                           value="${not empty param.email ? param.email : (not empty customerInfo ? customerInfo.email : '')}"
                                           placeholder="example@mail.com" onchange="submitForm()">
                                </div>


                            </div>
                        </div>


                        <%-- Cat Selection --%>
                        <div class="bk-card">
                            <div class="bk-card-header">
                                <h4><i class="bi bi-heart-pulse"></i> Select Your Cat</h4>
                                <%-- Add New Cat = MÀU XANH --%>
                                <a href="${pageContext.request.contextPath}/cats/cat-add?from=booking" class="btn-add-cat">
                                    <i class="bi bi-plus-lg"></i> Add New
                                </a>
                            </div>


                            <table class="cat-table">
                                <thead>
                                <tr>
                                    <th style="width:46px">Pick</th>
                                    <th style="width:62px">Photo</th>
                                    <th>Name</th>
                                    <th style="width:62px">Age</th>
                                    <th>Gender</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty catList}">
                                        <c:forEach items="${catList}" var="c">
                                            <tr>
                                                <td style="text-align:center">
                                                    <input type="radio" class="cat-radio"
                                                           name="catID" value="${c.catID}"
                                                        ${(param.catID == c.catID || selectedCatID == c.catID) ? 'checked' : ''}
                                                           onchange="submitForm()" required>
                                                </td>
                                                <td>
                                                    <img src="${pageContext.request.contextPath}/${c.img}"
                                                         class="cat-img" alt="${c.name}">
                                                </td>
                                                <td><strong>${c.name}</strong></td>
                                                <td>${c.age} yr${c.age > 1 ? 's' : ''}</td>
                                                <td>
                                                   <span class="badge-gender ${c.gender == 1 ? 'male' : 'female'}">
                                                           ${c.gender == 1 ? 'Male' : 'Female'}
                                                   </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="no-data-row">
                                            <td colspan="5">No cats registered yet. Add one to continue.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>


                            <c:if test="${not empty totalPages && totalPages > 1}">
                                <div class="pagination-row">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <button type="button" onclick="changePage(${i})"
                                                class="pg-btn ${i == currentPage ? 'active' : ''}">${i}</button>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>


                    </div><%-- /left-panel --%>


                    <%-- ========== RIGHT PANEL ========== --%>
                    <div class="right-panel">
                        <div class="bk-card">


                            <div class="bk-card-header">
                                <h4><i class="bi bi-pencil-square"></i> Appointment Details</h4>
                            </div>


                            <div class="selection-grid">


                                <div>
                                    <label class="bk-label">Category <span style="color:var(--danger)">*</span></label>
                                    <select id="categorySelect" name="categoryID" class="bk-select"
                                            onchange="submitForm()" required>
                                        <option value="">-- Select Category --</option>
                                        <c:forEach items="${categoryList}" var="cat">
                                            <option value="${cat.categoryID}"
                                                ${selectedCategoryID == cat.categoryID ? 'selected' : ''}>
                                                    ${cat.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>


                                <div>
                                    <label class="bk-label">Service <span style="color:var(--danger)">*</span></label>
                                    <select id="serviceSelect" name="serviceID" class="bk-select"
                                            onchange="calculatePrice(); updateConfirmButton(); submitForm();" required>
                                        <option value="">-- Select Service --</option>
                                        <c:forEach items="${serviceList}" var="ser">
                                            <option value="${ser.serviceID}" data-price="${ser.price}"
                                                ${(param.serviceID == ser.serviceID || selectedServiceID == ser.serviceID) ? 'selected' : ''}>
                                                    ${ser.nameService}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>


                                <c:if test="${needsVet and not isBoarding and not isCheckup}">
                                    <div class="full">
                                        <label class="bk-label">Veterinarian <span style="color:var(--danger)">*</span></label>
                                        <select id="vetSelect" name="assigneeInfo" class="bk-select"
                                                onchange="submitForm()" required>
                                            <option value="">-- Select Veterinarian --</option>
                                            <c:forEach items="${listPerson}" var="p">
                                                <option value="${p.userID}"
                                                    ${param.assigneeInfo == p.userID ? 'selected' : ''}>
                                                        ${p.fullName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:if>


                                <div>
                                    <label class="bk-label">
                                        <c:choose>
                                            <c:when test="${isBoarding or isCheckup}">Start Date</c:when>
                                            <c:otherwise>Preferred Date</c:otherwise>
                                        </c:choose>
                                        <span style="color:var(--danger)">*</span>
                                    </label>
                                    <input type="date" id="startDate" name="startDate" class="bk-input"
                                           value="${not empty selectedStartDate ? selectedStartDate : (empty param.startDate ? currentDate : param.startDate)}"
                                           min="${currentDate}"
                                           onchange="${needsVet ? 'submitForm()' : 'calculatePrice(); updateConfirmButton(); updateEndDateMin(); filterFutureTime();'}"
                                           required>
                                </div>


                                <c:if test="${isBoarding}">
                                    <div>
                                        <label class="bk-label">End Date <span style="color:var(--danger)">*</span></label>
                                        <input type="date" id="endDate" name="endDate" class="bk-input"
                                               value="${not empty selectedEndDate ? selectedEndDate : param.endDate}"
                                               min="${param.startDate}"
                                               onchange="calculatePrice(); updateConfirmButton();" required>
                                    </div>
                                </c:if>


                                <c:if test="${isBoarding or isCheckup}">
                                    <div class="full">
                                        <label class="bk-label">Assigned Staff <span style="color:var(--danger)">*</span></label>
                                        <select name="StaffInfor" class="bk-select" onchange="submitForm()" required>
                                            <option value="">-- Select Staff --</option>
                                            <c:forEach items="${listStaff}" var="s">
                                                <option value="${s.staffID}"
                                                    ${param.StaffInfor == s.staffID ? 'selected' : ''}>
                                                        ${s.fullName} (${s.position})
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>


                                    <div>
                                        <label class="bk-label">
                                                ${isBoarding ? 'Check-in Time' : 'Appointment Time'}
                                            <span style="color:var(--danger)">*</span>
                                        </label>
                                        <select id="checkInTime" name="checkInTime" class="bk-select"
                                                onchange="updateConfirmButton()" required>
                                            <option value="">-- Select Time --</option>
                                            <c:forEach begin="7" end="17" var="h">
                                                <c:if test="${h != 12}">
                                                    <c:set var="hSlot" value="${h < 10 ? '0' : ''}${h}:00"/>
                                                    <option value="${hSlot}"
                                                        ${param.checkInTime == hSlot ? 'selected' : ''}>${hSlot}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </c:if>


                            </div><%-- /selection-grid --%>


                            <%-- Available Slots --%>
                            <c:if test="${needsVet and not isBoarding and not isCheckup}">
                            <div class="slots-section">
                                <div class="slots-section-title">
                                    <i class="bi bi-clock"></i> Available Time Slots
                                </div>
                                <c:choose>
                                <c:when test="${not empty slotList}">
                                <div class="schedule-scroll">
                                    <c:set var="lastDate" value=""/>
                                    <c:forEach items="${slotList}" var="slot" varStatus="status">
                                    <fmt:formatDate value="${slot.slotDate}" pattern="yyyy-MM-dd" var="cDate"/>
                                    <fmt:formatDate value="${slot.slotDate}" pattern="dd/MM/yyyy"  var="displayDate"/>
                                    <c:if test="${lastDate != cDate}">
                                    <c:if test="${not empty lastDate}"></div></div></c:if>
                            <div class="day-group">
                                <div class="day-title">
                                    <i class="bi bi-calendar3"></i>
                                    <fmt:formatDate value="${slot.slotDate}" pattern="EEEE, dd/MM/yyyy"/>
                                </div>
                                <div class="slot-grid">
                                    <c:set var="lastDate" value="${cDate}"/>
                                    </c:if>


                                    <c:set var="isChecked" value="false"/>
                                    <c:if test="${not empty param.slotID and param.slotID == slot.slotID
                                                                 and not empty param.slotDate and param.slotDate == cDate}">
                                        <c:set var="isChecked" value="true"/>
                                    </c:if>


                                    <label class="slot-pill ${isChecked ? 'active-slot' : ''}"
                                           onclick="selectSlot(this, ${slot.slotID}, '${cDate}')">
                                        <input type="radio" name="slotID" value="${slot.slotID}"
                                               data-slot-date="${cDate}"
                                               data-date="${displayDate}"
                                               data-time="${fn:substring(slot.startTime.toString(), 0, 5)}"
                                               data-vet-id="${slot.vetID}"
                                            ${isChecked ? 'checked' : ''}
                                               style="display:none"/>
                                            ${fn:substring(slot.startTime.toString(), 0, 5)}
                                    </label>


                                    <c:if test="${status.last}"></div></div></c:if>
                            </c:forEach>
                        </div>
                        </c:when>
                        <c:otherwise>
                            <div class="no-slots-message">
                                <i class="bi bi-calendar-x" style="font-size:28px;display:block;margin-bottom:8px;color:var(--text-light)"></i>
                                No available slots. Please select a different date or veterinarian.
                            </div>
                        </c:otherwise>
                        </c:choose>
                    </div>
                    </c:if>


                    <%-- Note --%>
                    <div class="note-section">
                        <hr class="bk-divider">
                        <label class="bk-label">
                            <i class="bi bi-chat-left-text"></i> Note / Special Request
                        </label>
                        <textarea name="note" class="bk-textarea"
                                  placeholder="Ex: Cat is aggressive, requires careful handling...">${not empty savedNote ? savedNote : param.note}</textarea>
                    </div>


                    <%-- Cost Summary --%>
                    <div id="costSummaryArea" class="cost-summary"
                         style="display:${not empty param.serviceID ? 'block' : 'none'}">
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


                    <%-- Action --%>
                    <div class="action-section">
                        <button type="submit" class="btn-confirm" id="confirmBtn">
                            <i class="bi bi-calendar-check"></i> Booking Now
                        </button>
                        <c:if test="${not empty error}">
                            <div class="alert alert-error">
                                <i class="bi bi-exclamation-circle-fill"></i> ${error}
                            </div>
                        </c:if>
                        <c:if test="${not empty success}">
                            <div class="alert alert-success">
                                <i class="bi bi-check-circle-fill"></i> ${success}
                            </div>
                        </c:if>
                    </div>


                </div><%-- /bk-card --%>
        </div><%-- /right-panel --%>


    </div><%-- /booking-wrapper --%>
    </form>


</div><%-- /booking-content-area --%>
</div><%-- /main-body --%>


<%@ include file="footer.jsp" %>


</div><%-- /page-shell --%>


<script>
    const contextPath    = '${pageContext.request.contextPath}';
    const isBoardingMode = ${not empty isBoarding and isBoarding ? 'true' : 'false'};
    const isCheckupMode  = ${not empty isCheckup  and isCheckup  ? 'true' : 'false'};
    const needsVetMode   = ${not empty needsVet   and needsVet   ? 'true' : 'false'};


    function submitForm() {
        document.getElementById('actionField').value = 'load';
        document.body.classList.add('submitting');
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
        if (radio) { radio.checked = true; highlightSlot(radio); setSlotDate(); updateConfirmButton(); }
    }


    function changePage(page) {
        const params = new URLSearchParams();
        for (const [k, v] of new FormData(document.getElementById('bookingForm')).entries()) {
            if (v && k !== 'action') params.append(k, v);
        }
        params.set('catPage', page);
        document.body.classList.add('submitting');
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
        if (!isBoardingMode && !isCheckupMode) return;
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
        if (!sel || !sel.value) { if (area) area.style.display = 'none'; return; }
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
        if (isBoardingMode || isCheckupMode) {
            const d = document.getElementById('startDate')?.value;
            const t = document.getElementById('checkInTime')?.value;
            if (d && t) {
                const p = d.split('-');
                fDate = p[2] + '/' + p[1] + '/' + p[0];
                time  = t.substring(0, 5);
            }
        } else if (needsVetMode) {
            const r = document.querySelector('input[name="slotID"]:checked');
            if (r) { fDate = r.dataset.date; time = r.dataset.time; }
        }
        if (time && fDate) {
            btn.innerHTML = '<i class="bi bi-check-circle-fill"></i> Confirm: ' + time + ' — ' + fDate;
            btn.classList.add('ready');
        } else {
            btn.innerHTML = '<i class="bi bi-calendar-check"></i> Booking Now';
            btn.classList.remove('ready');
        }
    }


    document.addEventListener('DOMContentLoaded', function () {
        calculatePrice();
        updateConfirmButton();
        filterFutureTime();
        if (isBoardingMode) updateEndDateMin();


        const checked = document.querySelector('input[name="slotID"]:checked');
        if (checked) { highlightSlot(checked); setSlotDate(); updateConfirmButton(); }


        document.querySelectorAll('.slot-pill').forEach(pill => {
            pill.addEventListener('click', function () {
                const r = this.querySelector('input');
                if (r) { r.checked = true; highlightSlot(r); setSlotDate(); updateConfirmButton(); }
            });
        });
    });
</script>


</body>
</html>

