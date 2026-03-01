<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Care Tasks | Cat Clinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- LINK ĐẾN FILE CSS VỪA TẠO BÊN TRÊN -->
    <link href="${pageContext.request.contextPath}/css/carestaff-style.css" rel="stylesheet">
</head>
<body>
<div class="layout-wrapper">
    <!-- CỘT TRÁI: HOSPITALIZED CATS -->
    <div class="sidebar-left">
        <div class="sidebar-header">
            <h2 style="color: #f97316; font-size: 22px;"><i class="fa-solid fa-cat"></i> Cat Clinic</h2>
            <div style="margin-top: 15px; font-weight: 600; color: #475569;">
                <i class="fa-solid fa-user-nurse"></i> ${sessionScope.acc.fullName}
            </div>
        </div>
        <div style="padding: 15px; font-size: 11px; font-weight: 700; color: #9ca3af; letter-spacing: 1px;">HOSPITALIZED CATS</div>

        <div class="cat-list">
            <c:forEach items="${inpatientCats}" var="cat">
                <a href="${pageContext.request.contextPath}/staff/tasks?catId=${cat.CatID}&bookingId=${cat.BookingID}"
                   class="cat-item ${param.catId == cat.CatID ? 'active' : ''}">
                    <div class="cat-avatar"><i class="fa-solid fa-cat"></i></div>
                    <div class="cat-info">
                        <h4>${cat.CatName}</h4>
                        <p>Breed: ${cat.Breed}</p>
                    </div>
                </a>
            </c:forEach>
        </div>

        <div style="padding: 20px; text-align: center; border-top: 1px solid #fce7d1;">
            <a href="${pageContext.request.contextPath}/logout" style="color: #ef4444; text-decoration: none; font-weight: 600; font-size: 14px;">
                <i class="fa-solid fa-right-from-bracket"></i> Logout
            </a>
        </div>
    </div>

    <!-- CỘT PHẢI: CHI TIẾT CHĂM SÓC -->
    <div class="main-content">
        <c:choose>
            <c:when test="${empty selectedCat}">
                <div style="text-align: center; margin-top: 150px; color: #9ca3af;">
                    <i class="fa-solid fa-hand-pointer" style="font-size: 50px; margin-bottom: 20px; color: #fdba74;"></i>
                    <h2>Select a Patient</h2>
                    <p>Click on a hospitalized cat from the left menu to view their care schedule.</p>
                </div>
            </c:when>

            <c:otherwise>
                <!-- THẺ THÔNG TIN MÈO (HEADER) -->
                <div class="patient-card">
                    <div class="patient-details">
                        <div class="avatar-large"><i class="fa-solid fa-cat" style="color: white; font-size: 40px; margin: 20px;"></i></div>
                        <div>
                            <h2 style="margin: 0; font-size: 24px; color: #1f2937;">
                                    ${selectedCat.CatName}
                                <span style="background: #ffedd5; color: #ea580c; font-size: 11px; padding: 4px 8px; border-radius: 12px; vertical-align: middle; margin-left: 10px;">INPATIENT</span>
                            </h2>
                            <p style="margin: 5px 0 10px 0; color: #6b7280; font-size: 14px;">
                                    ${selectedCat.Breed} • ${selectedCat.Age} years • Owner: ${selectedCat.OwnerName}
                            </p>
                        </div>
                    </div>
                    <div style="text-align: right;">
                        <div style="background: #dcfce7; color: #059669; font-weight: 700; padding: 8px 20px; border-radius: 20px; font-size: 15px;">
                            STATUS: STABLE
                        </div>
                    </div>
                </div>

                <div class="grid-2">
                    <!-- CỘT 1: CARE SCHEDULE (UC40 & UC42) -->
                    <div>
                        <h3 style="color: #f97316; margin-bottom: 20px; font-size: 18px;">
                            <i class="fa-solid fa-clipboard-list"></i> Care Schedule (Today)
                        </h3>

                        <c:forEach items="${dailyTasks}" var="task">
                            <div class="task-card">
                                <form action="${pageContext.request.contextPath}/staff/tasks" method="POST" style="margin:0;">
                                    <input type="hidden" name="action" value="markTask">
                                    <input type="hidden" name="catId" value="${selectedCat.CatID}">
                                    <input type="hidden" name="bookingId" value="${param.bookingId}">
                                    <input type="hidden" name="taskId" value="${task.CareTaskID}">

                                    <!-- UC42: NÚT CHECK HOÀN THÀNH -->
                                    <button type="submit" class="btn-check ${task.Status == 'Completed' ? 'completed' : ''}"
                                        ${task.Status == 'Completed' ? 'disabled' : ''} title="Mark as Completed">
                                        <i class="fa-solid fa-check"></i>
                                    </button>
                                </form>
                                <div>
                                    <h4 style="margin: 0; color: #374151; font-size: 15px; text-decoration: ${task.Status == 'Completed' ? 'line-through; color: #9ca3af' : 'none'};">${task.TaskName}</h4>
                                    <span style="font-size: 11px; font-weight: 600; color: #9ca3af; text-transform: uppercase;">
                                            ${task.Status == 'Completed' ? 'Done' : 'Pending'}
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- CỘT 2: OBSERVATIONS LOG (UC41) -->
                    <div>
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                            <h3 style="color: #f97316; font-size: 18px; margin: 0;">
                                <i class="fa-solid fa-heart-pulse"></i> Observations Log
                            </h3>

                            <!-- FORM ADD LOG NHANH -->
                            <form action="${pageContext.request.contextPath}/staff/tasks" method="POST" style="display: flex; gap: 10px;">
                                <input type="hidden" name="action" value="addLog">
                                <input type="hidden" name="catId" value="${selectedCat.CatID}">
                                <input type="hidden" name="bookingId" value="${param.bookingId}">

                                <select name="taskId" style="padding: 6px; border-radius: 6px; border: 1px solid #cbd5e1; outline: none;" required>
                                    <c:forEach items="${careTasks}" var="t">
                                        <option value="${t.CareTaskID}">${t.TaskName}</option>
                                    </c:forEach>
                                </select>

                                <input type="text" name="note" placeholder="Quick note..." style="padding: 6px; border-radius: 6px; border: 1px solid #cbd5e1; outline: none; width: 140px;" required>
                                <button type="submit" class="btn-orange">+ Add Log</button>
                            </form>
                        </div>

                        <c:if test="${empty observations}">
                            <div style="text-align: center; color: #9ca3af; padding: 20px;">No observations recorded yet.</div>
                        </c:if>

                        <c:forEach items="${observations}" var="obs">
                            <div class="log-card">
                                <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                                    <span style="font-size: 12px; font-weight: 600; color: #6b7280;">${obs.RecordTime}</span>
                                    <span style="background: #e0f2fe; color: #0284c7; font-size: 11px; padding: 2px 8px; border-radius: 10px; font-weight: 600;">${obs.TaskName}</span>
                                </div>
                                <p style="margin: 0 0 10px 0; color: #374151; font-size: 14px;">${obs.Note}</p>
                                <div style="font-size: 11px; color: #9ca3af;"><i class="fa-solid fa-stethoscope"></i> Logged by: ${obs.StaffName}</div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>