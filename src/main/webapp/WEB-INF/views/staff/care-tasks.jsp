<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Care Tasks | Cat Clinic</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <link href="${pageContext.request.contextPath}/css/carestaff-style.css" rel="stylesheet">
</head>
<body>
<div class="layout-wrapper">

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
                <a href="${pageContext.request.contextPath}/care/tasks?catId=${cat.CatID}&bookingId=${cat.BookingID}"
                   class="cat-item ${param.catId == cat.CatID ? 'active' : ''}">
                    <div class="cat-avatar">
                        <img src="${pageContext.request.contextPath}/${cat.CatImg}"
                             alt="${cat.CatName}"
                             style="width:40px;height:40px;border-radius:50%;object-fit:cover;">
                    </div>
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


    <div class="main-content">
        <c:choose>
            <c:when test="${empty petDetail}">
                <div style="text-align: center; margin-top: 150px; color: #9ca3af;">
                    <i class="fa-solid fa-hand-pointer" style="font-size: 50px; margin-bottom: 20px; color: #fdba74;"></i>
                    <h2>Select a Patient</h2>
                    <p>Click on a hospitalized cat from the left menu to view their care tasks.</p>
                </div>
            </c:when>

            <c:otherwise>

                <div class="patient-card">
                    <div class="patient-details">
                        <div class="avatar-large"><img src="${pageContext.request.contextPath}/${petDetail.Image}" style="width: 100%; height: 100%; object-fit: cover; border-radius: 20px;"></div>
                        <div>
                            <h2 style="margin: 0; font-size: 24px; color: #1f2937;">
                                    ${petDetail.CatName}
                                <span style="background: #ffedd5; color: #ea580c; font-size: 11px; padding: 4px 8px; border-radius: 12px; margin-left: 10px;">INPATIENT</span>
                            </h2>
                            <p style="margin: 5px 0 10px 0; color: #6b7280; font-size: 14px;">
                                    ${petDetail.Breed} • ${petDetail.Age} years • Owner: ${petDetail.OwnerName}
                            </p>

                            <button onclick="document.getElementById('petDetailModal').style.display='block'"
                                    style="background: white; border: 1px solid #f97316; color: #f97316; padding: 5px 12px; border-radius: 6px; font-size: 12px; cursor: pointer; font-weight: 600;">
                                <i class="fa-solid fa-address-card"></i> View Pet Details
                            </button>
                        </div>
                    </div>
                </div>

                <div class="grid-2">

                    <div>
                        <h3 style="color: #f97316; margin-bottom: 20px; font-size: 18px;">
                            <i class="fa-solid fa-clipboard-list"></i> View Care Task (Today)
                        </h3>

                        <c:forEach items="${dailyTasks}" var="task">
                            <div class="task-card">
                                <form action="${pageContext.request.contextPath}/care/tasks" method="POST" style="margin:0;">
                                    <input type="hidden" name="action" value="markTask">
                                    <input type="hidden" name="catId" value="${selectedCatId}">
                                    <input type="hidden" name="bookingId" value="${selectedBookingId}">
                                    <input type="hidden" name="taskId" value="${task.CareTaskID}">


                                    <button type="submit" class="btn-check ${task.Status == 'Completed' ? 'completed' : ''}"
                                        ${task.Status == 'Completed' ? 'disabled' : ''} title="Update status">
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


                    <div>
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                            <h3 style="color: #f97316; font-size: 18px; margin: 0;">
                                <i class="fa-solid fa-heart-pulse"></i> Record Care Diary
                            </h3>

                            <form action="${pageContext.request.contextPath}/care/tasks" method="POST" style="display: flex; gap: 10px;">
                                <input type="hidden" name="action" value="addLog">
                                <input type="hidden" name="catId" value="${selectedCatId}">
                                <input type="hidden" name="bookingId" value="${selectedBookingId}">

                                <select name="taskId" style="padding: 6px; border-radius: 6px; border: 1px solid #cbd5e1; outline: none;" required>
                                    <c:forEach items="${careTasks}" var="t">
                                        <option value="${t.CareTaskID}">${t.TaskName}</option>
                                    </c:forEach>
                                </select>

                                <input type="text" name="note" placeholder="Note behavior..." style="padding: 6px; border-radius: 6px; border: 1px solid #cbd5e1; outline: none; flex:1;" required>
                                <button type="submit" class="btn-orange">+ Add Log</button>
                            </form>
                        </div>

                        <c:if test="${empty observations}">
                            <div style="text-align: center; color: #9ca3af; padding: 20px;">No diaries recorded yet.</div>
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

                <div id="petDetailModal" class="modal">
                    <div class="modal-content">
                        <span class="close-btn" onclick="document.getElementById('petDetailModal').style.display='none'">&times;</span>
                        <h2 style="color: #f97316; margin-bottom: 20px;"><i class="fa-solid fa-paw"></i> Pet Details</h2>
                        <div class="detail-row"><strong>Name:</strong> <span>${petDetail.CatName}</span></div>
                        <div class="detail-row"><strong>Breed:</strong> <span>${petDetail.Breed}</span></div>
                        <div class="detail-row"><strong>Gender:</strong> <span>${petDetail.Gender}</span></div>
                        <div class="detail-row"><strong>Age:</strong> <span>${petDetail.Age} years</span></div>
                        <div class="detail-row"><strong>Owner Name:</strong> <span>${petDetail.OwnerName}</span></div>
                        <div class="detail-row"><strong>Contact:</strong> <span>${petDetail.Phone} - ${petDetail.Email}</span></div>
                        <div class="detail-row"><strong>Address:</strong> <span>${petDetail.Address}</span></div>
                    </div>
                </div>

            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>