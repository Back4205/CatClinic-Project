<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Vet Schedule | Orange Theme</title>
    <!-- Nạp các file CSS chung của sidebar và header để hiển thị đúng giao diện hệ thống -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">
    <!-- Giữ nguyên toàn bộ file CSS riêng của trang lịch bạn đã viết -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="${pageContext.request.contextPath}/css/ScheduleViewStyle.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <!-- Cấu trúc layout chung admin giống hệt AccountList.jsp để sidebar/header hoạt động đúng -->
    <div class="admin-layout">
        <!-- Lồng sidebar chung của hệ thống -->
        <jsp:include page="sidebar.jsp"/>

        <div class="admin-main">
            <!-- Lồng header chung của hệ thống -->
            <jsp:include page="header.jsp"/>

            <!-- TOÀN BỘ NỘI DUNG TRANG LỊCH CỦA BẠN ĐƯỢC GIỮ NGUYÊN 100%, KHÔNG SỬA GÌ -->
            <div class="schedule-container">
                <header class="sched-header">
                    <div class="vet-profile">
                        <div class="icon-wrapper"><i class="fas fa-paw"></i></div>
                        <div class="header-text">
                            <h2>Vet Schedule</h2>
                        </div>
                    </div>
                    <div class="change">
                        <c:forEach var="v" items="${listVet}">
                            <c:if test="${v.vetID == VetID}">
                                <div class="change">
                                    <a href="changvetschedule?VetID=${VetID}">
                                        Change schedule for ${v.fullName}
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>               
                    </div>
                    <form method="get" action="viewschedule" class="filter-form">
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

                        <div class="filter-group">
                            <label>Veterinarian</label>
                            <select name="VetID" onchange="this.form.submit()">
                                <c:forEach var="v" items="${listVet}">
                                    <option value="${v.vetID}" ${v.vetID == VetID ? "selected" : ""}>${v.fullName}</option>
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
                                                            <span class="status-pill">BOOKED</span> 
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
            <!-- HẾT NỘI DUNG TRANG LỊCH CỦA BẠN -->
        </div>
    </div>
</body>
</html>