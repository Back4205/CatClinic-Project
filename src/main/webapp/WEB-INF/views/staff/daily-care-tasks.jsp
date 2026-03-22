<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Daily Care Tasks | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carestaff-style.css">
</head>

<body>
<div class="page-shell">

    <header class="topbar">
        <div class="logo">
            <a style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 10px;">
                <i class="bi bi-hospital"></i> CatClinic
            </a>
        </div>
        <div class="right-section">
            <div class="user-info">
                <span class="name">Welcome ${sessionScope.acc.fullName != null ? sessionScope.acc.fullName : 'Care Staff'}</span>
                <div class="avatar">
                    <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">
                </div>
            </div>
        </div>
    </header>

    <div class="main-body">

        <aside class="sidebar">
            <div class="profile-section">
                <img src="https://cdn-icons-png.flaticon.com/512/4140/4140047.png" alt="Avatar" class="profile-img">
                <h3 class="profile-name">${sessionScope.acc.fullName != null ? sessionScope.acc.fullName : 'Care Staff'}</h3>
                <span class="profile-role">CATCLINIC PORTAL</span>
            </div>
            <nav class="nav-menu">
                <a href="${pageContext.request.contextPath}/staff/daily-care-tasks" class="nav-item active">
                    <i class="fa-regular fa-calendar-check"></i> View Care Task
                </a>
                <a href="#" class="nav-item">
                    <i class="fa-solid fa-book-medical"></i> Record Care Diary
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-item">
                    <i class="fa-solid fa-address-card"></i> profile
                </a>
            </nav>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
            </a>
        </aside>

        <div class="content-area">
            <h1 class="page-title">
                <i class="bi bi-calendar-check"></i> Daily Care Tasks
            </h1>

            <p style="color: #6b7280; margin-bottom: 20px;">Manage and complete your assigned tasks for today.</p>

            <div class="task-container">
                <c:forEach items="${allTasks}" var="t">
                    <c:set var="isJourneyDone" value="${t.status == 'Completed'}" />

                    <div class="cat-card ${isJourneyDone ? 'card-completed' : ''}">
                        <c:if test="${isJourneyDone}">
                            <div class="ribbon">COMPLETED</div>
                        </c:if>

                        <div class="card-left">
                            <img src="${pageContext.request.contextPath}/${t.catImage}" alt="cat" class="cat-avatar">
                            <div>
                                <div class="cat-name">
                                        ${t.catName}
                                    <span class="badge ${isJourneyDone ? 'complete' : 'pending'}">
                                            ${isJourneyDone ? 'COMPLETED' : 'IN PROGRESS'}
                                    </span>
                                    <span class="task-count">${t.completedTaskIds.size()}/${totalCount} Tasks</span>
                                </div>

                                <div class="cat-details">
                                    <span><i class="bi bi-person"></i> ${t.ownerName}</span>
                                    <c:if test="${t.endDate.toString() == today}">
                                        <span class="discharge-warning"><i class="bi bi-exclamation-circle"></i> Discharge Today</span>
                                    </c:if>
                                </div>

                                <div class="task-group">
                                    <c:forEach items="${masterTasks}" var="master">
                                        <c:set var="isTaskDone" value="${t.completedTaskIds.contains(master.key)}" />
                                        <form action="${pageContext.request.contextPath}/staff/daily-care-tasks" method="POST" style="margin:0; display:inline-block;">
                                            <input type="hidden" name="action" value="markTask">
                                            <input type="hidden" name="careJID" value="${t.careJID}">
                                            <input type="hidden" name="taskID" value="${master.key}">
                                            <button type="submit" class="task-btn ${isTaskDone ? 'done' : ''}" ${isTaskDone ? 'disabled' : ''}>
                                                <i class="bi ${isTaskDone ? 'bi-check-circle-fill' : 'bi-circle'}"></i> ${master.value}
                                            </button>
                                        </form>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="card-right">
                            <c:choose>
                                <%-- 1. Nếu Booking đã ở trạng thái Ready for Checkout --%>
                                <c:when test="${t.bookingStatus == 'Ready for Checkout'}">
                                    <div class="waiting-reception">
                                        <i class="bi bi-clock-history"></i> Waiting for Reception
                                    </div>
                                </c:when>

                                <%-- 2. Nếu Journey hoàn thành và là ngày xuất viện => Hiện nút bấm --%>
                                <c:when test="${isJourneyDone and t.endDate.toString() == today}">
                                    <form action="${pageContext.request.contextPath}/staff/daily-care-tasks" method="POST">
                                        <input type="hidden" name="action" value="checkout">
                                        <input type="hidden" name="bookingID" value="${t.bookingID}">
                                        <button type="submit" class="btn-checkout-ready">
                                            <i class="bi bi-bell-fill"></i> Ready for Checkout
                                        </button>
                                    </form>
                                </c:when>

                                <%-- 3. Mặc định: Chỉ hiện nút xem chi tiết --%>
                                <c:otherwise>
                                    <button type="button" class="btn-view-detail"
                                            onclick="openDetailModal('${t.catName}', '${t.breed}', '${t.gender}', '${t.catAge}', '${t.ownerName}', '${t.ownerPhone}', '${t.address}', '${t.catImage}')">
                                        View Cat Detail <i class="bi bi-chevron-right"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <footer class="footer">
        <div class="footer-content">
            &copy; 2026 CatClinic. All rights reserved.
        </div>
    </footer>
</div>

<div id="petDetailModal" class="modal-overlay" style="display: none;">
</div>
<!-- SCRIPT XỬ LÝ POPUP -->
<script>
    function openDetailModal(name, breed, gender, age, owner, phone, address, img) {
        document.getElementById('mdName').innerText = name;
        document.getElementById('mdBreed').innerText = breed;
        document.getElementById('mdGender').innerText = gender;
        document.getElementById('mdAge').innerText = age + ' years';
        document.getElementById('mdOwner').innerText = owner;
        document.getElementById('mdPhone').innerText = phone;
        document.getElementById('mdAddress').innerText = (address && address !== 'null') ? address : 'N/A';
        document.getElementById('mdImage').src = '${pageContext.request.contextPath}/' + img;
        document.getElementById('petDetailModal').style.display = 'flex';
    }

    function closeDetailModal() {
        document.getElementById('petDetailModal').style.display = 'none';
    }

    // Đóng popup khi click ra ngoài vùng đen
    window.onclick = function(event) {
        var modal = document.getElementById('petDetailModal');
        if (event.target == modal) {
            closeDetailModal();
        }
    }
</script>
</body>
</html>