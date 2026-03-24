<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link href="css/ScheduleViewStyle.css" rel="stylesheet" type="text/css"/>    </head>
    <body>
        <div class="schedule-container">
             <a href="DashboardController" class="btn-back">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="19" y1="12" x2="5" y2="12"></line>
                    <polyline points="12 19 5 12 12 5"></polyline>
                    </svg>
                    Back
                </a>
            <header class="sched-header">
               
                <div class="vet-profile">
                    <div class="icon-wrapper"><i class="fas fa-paw"></i></div>
                    <div class="header-text">
                        <h2>Vet Schedule</h2>
                    </div>
                </div>
                <form method="get" action="scheduleviewforvet" class="filter-form">
                    <input type="hidden" name="VetID" value="${VetID}"> 
                    <div class="filter-group">
                        <label>Year</label>
                        <select name="year" onchange="this.form.submit()">
                            <c:forEach var="y" items="${years}">
                                <option value="${y}" ${y == year ? "selected" : ""}>${y}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label>Week</label>
                        <select name="week" onchange="this.form.submit()">
                            <c:forEach var="w" items="${weeks}">
                                <option value="${w}" ${w == week ? "selected" : ""}>${w}</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </header>
            <table class="sched-table">
                <thead>
                    <tr>
                        <th class="time-column">KHUNG GIỜ</th>
                            <c:forEach var="day" items="${days}">
                            <th>
                                <span class="day-name">
                                    <c:choose>
                                        <c:when test="${day.dayOfWeek.value == 7}">
                                            CHỦ NHẬT
                                        </c:when>
                                        <c:otherwise>
                                            THỨ ${day.dayOfWeek.value + 1}
                                        </c:otherwise>
                                    </c:choose>
                                </span>

                                <span class="day-date">
                                    ${day.dayOfMonth}/${day.monthValue}
                                </span>
                            </th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="slot" items="${slots}">
                        <tr>
                            <td class="time-column">
                                ${slot.slotID}
                                <span class="time-sub">${slot.startTime}</span>
                            </td>
                            <c:forEach var="day" items="${days}">
                                <td>
                                    <c:set var="found" value="false"/>
                                    <c:forEach var="sc" items="${schedule}">
                                        <c:if test="${sc.slotID == slot.slotID 
                                                      && sc.date.toString() eq day.toString()}">
                                            <c:set var="found" value="true"/>
                                            <c:choose>
                                                <c:when test="${sc.status eq 'Booked'}">
                                                    <div class="booking-card">
                                                        <span class="pet-title">${sc.catName}</span>
                                                        <span class="owner-info">
                                                            Chủ: ${sc.fullName}
                                                        </span><br>
                                                        <span class="status-pill">
                                                            <c:if test="${sc.statusBooking == 'Confirmed'}">Booked</c:if>
                                                            <c:if test="${sc.statusBooking == 'Completed'}">Checked In </c:if>
                                                            </span>
                                                        </div>
                                                </c:when>
                                                <c:when test="${sc.status eq 'Absent'}">
                                                    <div class="absent">
                                                        Nghỉ làm
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                </c:otherwise>

                                            </c:choose>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${!found}">
                                        <span class="available"></span>
                                    </c:if>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <footer class="sched-footer">
                <div class="legend-item">
                    <span class="box" style="background: var(--status-booked);"></span> Đã có lịch
                </div>
                <div class="legend-item">
                    <span class="box" style="border: 1px dashed var(--primary-orange); background: #fff;"></span> Còn trống
                </div>
                <div class="legend-item">
                    <span class="box" style="background: #E0E0E0;"></span> Nghỉ / Khóa
                </div>
            </footer>
        </div>
    </body>
</html>
