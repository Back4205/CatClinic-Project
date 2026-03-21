<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Appointment | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking/Booking.css">
  <script src="${pageContext.request.contextPath}/js/booking/booking.js"></script>
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
                                <td><input type="radio" name="catID"
                                           value="${c.catID}" ${(param.catID == c.catID || selectedCatID == c.catID) ? 'checked' : ''}
                                           required></td>
                                <td><img src="${pageContext.request.contextPath}/${c.img}" class="pet-img"
                                         alt="${c.name}"></td>
                                <td><strong>${c.name}</strong></td>
                                <td>${c.age} Yr${c.age > 1 ? 's' : ''}</td>
                                <td><span
                                        class="badge ${c.gender == 1 ? 'male' : 'female'}">${c.gender == 1 ? 'Male' : 'Female'}</span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty catList}">
                            <tr>
                                <td colspan="5" class="no-slots-message">No cats registered yet.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>

                    <c:if test="${not empty totalPages && totalPages > 1}">
                        <div style="margin-top: 20px; display: flex; justify-content: center; gap: 8px;">
                            <c:if test="${currentPage > 1}">
                                <button type="button" onclick="changePage(${currentPage - 1})" class="pagination-btn">
                                    Prev
                                </button>
                            </c:if>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <button type="button" onclick="changePage(${i})"
                                        class="pagination-btn ${i == currentPage ? 'active' : ''}">${i}</button>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <button type="button" onclick="changePage(${currentPage + 1})" class="pagination-btn">
                                    Next
                                </button>
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
                            <select name="serviceID" id="serviceSelect"
                                    onchange="calculatePrice(); updateConfirmButton();" required>
                                <option value="">-- Select Service --</option>
                                <c:forEach items="${serviceList}" var="ser">
                                    <option value="${ser.serviceID}"
                                            data-price="${ser.price}" ${(param.serviceID == ser.serviceID || selectedServiceID == ser.serviceID) ? 'selected' : ''}>${ser.nameService}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <c:if test="${needsVet and not isBoarding and not isCheckup}">
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
                            <label>${(isBoarding or isCheckup) ? 'Start Date' : 'Preferred Date'}</label>
                            <input type="date" name="startDate" id="startDate"
                                   value="${not empty selectedStartDate ? selectedStartDate : (empty param.startDate ? currentDate : param.startDate)}"
                                   min="${currentDate}"
                                   onchange="${needsVet ? 'submitForm()'  : 'calculatePrice(); updateConfirmButton(); updateEndDateMin(); filterFutureTime();'}">
                        </div>

                        <!-- End Date chỉ cho Boarding -->
                        <c:if test="${isBoarding}">
                            <div>
                                <label>End Date</label>
                                <input type="date" name="endDate" id="endDate"
                                       value="${not empty selectedEndDate ? selectedEndDate : param.endDate}"
                                       min="${param.startDate}"
                                       onchange="calculatePrice(); updateConfirmButton();">
                            </div>
                        </c:if>

                        <!-- Time cho Boarding + Checkup -->
                        <c:if test="${isBoarding or isCheckup}">
                            <div>
                                <label>${isBoarding ? 'Check-in Time' : 'Appointment Time'}</label>
                                <select name="checkInTime" id="checkInTime" onchange="updateConfirmButton()" required>
                                    <option value="">-- Select Time --</option>
                                    <c:forEach begin="7" end="17" var="h">
                                        <c:if test="${h != 12}">
                                            <c:set var="currentHour" value="${h < 10 ? '0' : ''}${h}:00"/>
                                            <option value="${currentHour}" ${param.checkInTime == currentHour ? 'selected' : ''}>
                                                    ${currentHour}
                                            </option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                    </div>

                    <!-- Available Slots for Vet Services -->
                    <c:if test="${needsVet and not isBoarding and not isCheckup}">
                    <label style="font-size: 11px; font-weight: 700; color: #9ca3af; text-transform: uppercase; margin-bottom: 12px; display: block;">
                        <i class="bi bi-clock"></i> Available Slots:
                    </label>
                    <c:choose>
                    <c:when test="${not empty slotList}">
                    <div class="schedule-scroll">
                        <c:set var="lastDate" value=""/>
                        <c:forEach items="${slotList}" var="slot" varStatus="status">
                        <fmt:formatDate value="${slot.slotDate}" pattern="yyyy-MM-dd" var="currentDate"/>

                        <c:if test="${lastDate != currentDate}">
                        <c:if test="${not empty lastDate}">
                    </div>
                </div>
                </c:if>
                <div class="day-group">
                  <span class="day-title">
                    <i class="bi bi-calendar3"></i>
                    <fmt:formatDate value="${slot.slotDate}" pattern="EEEE, dd/MM/yyyy"/>
                  </span>
                    <div class="slot-grid">
                        <c:set var="lastDate" value="${currentDate}"/>
                        </c:if>

                        <label class="slot ${param.slotID == slot.slotID ? 'active-slot' : ''}">
<%--                            <input type="radio" name="slotID" value="${slot.slotID}"--%>
<%--                                ${param.slotID == slot.slotID ? 'checked' : ''}--%>
<%--                                   onchange="updateConfirmButton()" required>--%>
<%--                                ${fn:substring(slot.startTime.toString(), 0, 5)}--%>

                            <input type="radio"
                                   name="slotID"
                                   value="${slot.slotID}"
                                   data-date="<fmt:formatDate value='${slot.slotDate}' pattern='dd/MM/yyyy'/>"
                                   data-time="${fn:substring(slot.startTime.toString(), 0, 5)}"
                                ${param.slotID == slot.slotID ? 'checked' : ''}
                                   onchange="updateConfirmButton()"
                                   required>
                                ${fn:substring(slot.startTime.toString(), 0, 5)}
                        </label>

                        <c:if test="${status.last}">
                    </div>
                </div>
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
                <div class="summary-row" style="color:#6b7280; font-size:12px;"><span>Deposit (20%):</span><span
                        id="depositDisplay">0 VND</span></div>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <div class="booking-preview" id="confirmBtn">
                    Confirm Appointment
                </div>
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


<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>
</body>
<script>

    const isBoardingMode = ${isBoarding ? 'true' : 'false'};
    const isCheckupMode = ${isCheckup ? 'true' : 'false'};
    const needsVetMode = ${needsVet ? 'true' : 'false'};
    const contextPath = '${pageContext.request.contextPath}';

</script>

</html>