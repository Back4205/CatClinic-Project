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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/Booking2.css">
  <script src="${pageContext.request.contextPath}/js/booking/reception-booking.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

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
              <c:set var="passPhone"    value="${not empty param.phone    ? param.phone    : customerInfo.phone}"/>
              <c:set var="passFullName" value="${not empty param.fullName ? param.fullName : customerInfo.fullName}"/>
              <c:set var="passEmail"    value="${not empty param.email    ? param.email    : customerInfo.email}"/>

              <c:choose>
                <c:when test="${not empty customerInfo and customerInfo.userID gt 0}">
                  <a href="${pageContext.request.contextPath}/cats/cat-add?f=Counterbooking&phone=${passPhone}&fullName=${passFullName}&email=${passEmail}"
                     class="btn-new-booking" style="padding: 8px 16px; font-size: 13px;">
                    <i class="fa-solid fa-plus"></i> Add New
                  </a>
                </c:when>
                <c:otherwise>
                  <a href="${pageContext.request.contextPath}/cats/cat-add?f=Counterbooking&phone=${passPhone}&fullName=${passFullName}&email=${passEmail}"
                     class="btn-new-booking" style="padding: 8px 16px; font-size: 13px;">
                    <i class="fa-solid fa-plus"></i> Add New Owner and cat
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
                                                    <span class="badge ${c.gender == 1 ? 'male' : 'female'}">
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

            <!-- PREV -->
            <div style="display: flex; align-items: center; justify-content: center; gap: 6px; margin-top: 16px;">

              <c:if test="${currentPage > 1}">
                <a href="javascript:void(0)"
                   onclick="changePage(${currentPage - 1})"
                   class="pagination-btn" style="width: auto; padding: 0 14px;">
                  <i class="fa-solid fa-chevron-left" style="font-size:11px; margin-right:4px;"></i> Prev
                </a>
              </c:if>

              <!-- NUMBER -->
              <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="javascript:void(0)"
                   onclick="changePage(${i})"
                   class="pagination-btn ${i == currentPage ? 'active' : ''}">
                    ${i}
                </a>
              </c:forEach>

              <!-- NEXT -->
              <c:if test="${currentPage < totalPages}">
                <a href="javascript:void(0)"
                   onclick="changePage(${currentPage + 1})"
                   class="pagination-btn" style="width: auto; padding: 0 14px;">
                  Next <i class="fa-solid fa-chevron-right" style="font-size:11px; margin-left:4px;"></i>
                </a>
              </c:if>

            </div>
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


</script>

</body>
</html>