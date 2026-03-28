<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
                <a href="${pageContext.request.contextPath}/staff/record-care-diary" class="nav-item">
                    <i class="fa-solid fa-book-medical"></i> Record Care Diary
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-item">
                    <i class="fa-solid fa-address-card"></i> Profile
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

            <div style="margin-bottom: 25px; position: relative;">
                <i class="bi bi-search" style="position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: #6b7280;"></i>
                <input type="text" id="searchTaskInput" onkeyup="searchTasks()" placeholder="Tìm kiếm theo tên mèo, tên chủ hoặc SĐT..."
                       style="width: 100%; padding: 12px 15px 12px 40px; border: 1px solid #e5e7eb; border-radius: 8px; outline: none; font-size: 14px; box-shadow: 0 1px 2px rgba(0,0,0,0.05);">
            </div>

            <div class="task-container">
                <c:forEach items="${allTasks}" var="t">
                    <c:set var="isJourneyDone" value="${t.completedTaskIds.size() == 4}" />

                    <div class="cat-card ${isJourneyDone ? 'card-completed' : ''}" data-search="${t.catName} ${t.ownerName} ${t.ownerPhone}">
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
                                <%-- 1. Nếu Lễ tân đã thu tiền và Check-out thành công --%>
                                <c:when test="${not empty t.checkOutTime}">
                                    <div class="waiting-reception" style="color: #16a34a; border-color: #16a34a; background: #dcfce7;">
                                        <i class="bi bi-check2-all"></i> Checked Out Successfully
                                    </div>
                                </c:when>

                                <%-- 2. Khi bạn đã ấn Early/Ready Checkout (Status = Completed) => Đổi thành Waiting --%>
                                <c:when test="${t.status == 'Completed'}">
                                    <div class="waiting-reception">
                                        <i class="bi bi-clock-history"></i> Waiting for Receptionist
                                    </div>
                                </c:when>

                                <%-- 3. Giao diện 3 nút bấm khi chưa Check-out --%>
                                <c:otherwise>
                                    <div style="display: flex; flex-direction: column; gap: 8px; width: 100%;">

                                            <%-- Chỉ hiện 1 trong 2 nút Checkout NẾU ĐÃ LÀM XONG 4 TASK --%>
                                        <c:if test="${isJourneyDone}">
                                            <form action="${pageContext.request.contextPath}/staff/daily-care-tasks" method="POST" style="margin:0;">
                                                <input type="hidden" name="action" value="checkout">
                                                <input type="hidden" name="bookingID" value="${t.bookingID}">

                                                    <%-- Phân biệt Early Checkout (Xanh) và Ready for Checkout (Cam) dựa vào EndDate --%>
                                                <button type="submit" class="btn-checkout-ready" style="width: 100%; ${t.endDate.toString() != today ? 'background-color: #3b82f6;' : ''}">
                                                    <i class="bi bi-bell-fill"></i>
                                                        ${t.endDate.toString() != today ? 'Early Checkout' : 'Ready for Checkout'}
                                                </button>
                                            </form>
                                        </c:if>

                                            <%-- NÚT GIA HẠN: Luôn hiện để Care Staff có thể gia hạn bất kỳ lúc nào --%>
                                        <button type="button" class="btn-checkout-ready" style="background-color: #f59e0b; width: 100%;"
                                                onclick="openExtendModal(${t.bookingID}, '${t.endDate}')">
                                            <i class="bi bi-calendar-plus"></i> Extend (Gia hạn)
                                        </button>

                                            <%-- NÚT XEM CHI TIẾT --%>
                                        <button type="button" class="btn-view-detail" style="width: 100%; margin:0;"
                                                onclick="openDetailModal(${t.bookingID},'${t.catName}', '${t.breed}', '${t.gender}', '${t.catAge}', '${t.ownerName}', '${t.ownerPhone}', '${t.address}', '${t.catImage}')">
                                            View Cat Detail <i class="bi bi-chevron-right"></i>
                                        </button>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div id="history-data-${t.bookingID}" style="display: none;">
                        <c:choose>
                            <c:when test="${not empty careHistories[t.bookingID]}">
                                <c:forEach items="${careHistories[t.bookingID]}" var="log">

                                    <%-- Dùng substring để tách chuẩn xác trước và sau chuỗi ||| --%>
                                    <c:set var="datePart" value="${fn:substringBefore(log, '|||')}"/>
                                    <c:set var="notePart" value="${fn:substringAfter(log, '|||')}"/>

                                    <details style="margin-bottom: 12px; border: 1px solid #fed7aa; border-radius: 8px; background: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.05);">
                                        <summary style="padding: 14px 16px; font-weight: bold; font-size: 15px; color: #ea580c; cursor: pointer; background-color: #fff7ed; border-radius: 8px; outline: none;">
                                            <i class="bi bi-calendar2-check-fill" style="margin-right: 8px;"></i> ${datePart}
                                        </summary>

                                        <div style="padding: 16px; border-top: 1px solid #fed7aa; white-space: pre-wrap; font-size: 14px; line-height: 1.6; color: #374151;">${notePart}</div>
                                    </details>

                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p style="color: #6b7280; font-size: 14px; font-style: italic; padding: 10px;">Chưa có lịch sử ghi chú bệnh án nào.</p>
                            </c:otherwise>
                        </c:choose>
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
    <div class="modal-content" style="width: 1000px; max-width: 100%; min-height: 900px;">
        <div class="modal-header">
            <h3><i class="bi bi-info-circle"></i> Cat Details</h3>
            <button class="close-btn" onclick="closeDetailModal()"><i class="bi bi-x-lg"></i></button>
        </div>
        <div class="modal-body">
            <!-- Cột trái: Ảnh -->
            <img id="mdImage" src="" alt="Cat Image" class="md-img">

            <!-- Cột phải: Thông tin -->
            <div class="md-info">
                <div id="mdName" class="md-name"></div>

                <div class="md-grid">
                    <div><span>Breed</span><strong id="mdBreed"></strong></div>
                    <div><span>Gender</span><strong id="mdGender"></strong></div>
                    <div><span>Age</span><strong id="mdAge"></strong></div>
                </div>

                <div class="md-owner-box">
                    <h4>Owner Information</h4>
                    <div><i class="bi bi-person"></i> <span id="mdOwner"></span></div>
                    <div><i class="bi bi-telephone"></i> <span id="mdPhone"></span></div>
                    <div><i class="bi bi-geo-alt"></i> <span id="mdAddress"></span></div>
                </div>
                <div class="md-owner-box" style="margin-top: 15px;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;">
                        <h4 style="margin: 0;"><i class="bi bi-clock-history"></i> Care History</h4>
                        <!-- Thanh search nhỏ gọn -->
                        <div style="position: relative; width: 55%;">
                            <i class="bi bi-search" style="position: absolute; left: 10px; top: 50%; transform: translateY(-50%); color: #9ca3af; font-size: 13px;"></i>
                            <input type="text" id="searchHistoryInput" onkeyup="filterCareHistory()" placeholder="Search date or note..."
                                   style="width: 100%; padding: 6px 10px 6px 30px; border: 1px solid #d1d5db; border-radius: 6px; font-size: 13px; outline: none;">
                        </div>
                    </div>
                    <!-- Vùng chứa danh sách Ngày 1, Ngày 2... -->
                    <div id="mdHistory" style="max-height: 350px; overflow-y: auto; padding-right: 5px;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- MODAL GIA HẠN (EXTEND) -->
<div id="extendModal" class="modal-overlay" style="display: none;">
    <div class="modal-content" style="max-width: 400px;">
        <div class="modal-header">
            <h3><i class="bi bi-calendar-plus"></i> Extend End Date</h3>
            <button type="button" class="close-btn" onclick="closeExtendModal()"><i class="bi bi-x-lg"></i></button>
        </div>
        <form action="${pageContext.request.contextPath}/staff/daily-care-tasks" method="POST">
            <div class="modal-body" style="padding: 20px;">
                <input type="hidden" name="action" value="extend">
                <input type="hidden" name="bookingID" id="extendBookingID">

                <label style="display: block; margin-bottom: 8px; font-weight: bold; color: #374151;">Chọn ngày xuất viện mới:</label>
                <input type="date" name="newEndDate" id="newEndDateInput" style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px;" required>
            </div>
            <div style="padding: 15px 20px; text-align: right; border-top: 1px solid #eee; background: #f9fafb;">
                <button type="button" onclick="closeExtendModal()" style="padding: 8px 15px; border: none; background: #9ca3af; color: white; border-radius: 5px; cursor: pointer; margin-right: 10px;">Cancel</button>
                <button type="submit" style="padding: 8px 15px; border: none; background: #ea580c; color: white; border-radius: 5px; cursor: pointer; font-weight: bold;">Save Update</button>
            </div>
        </form>
    </div>
</div>
<!-- SCRIPT XỬ LÝ POPUP -->
<script>
    function openDetailModal(bookingID,name, breed, gender, age, owner, phone, address, img) {
        document.getElementById('mdName').innerText = name;
        document.getElementById('mdBreed').innerText = breed;
        document.getElementById('mdGender').innerText = gender;
        document.getElementById('mdAge').innerText = age + ' years';
        document.getElementById('mdOwner').innerText = owner;
        document.getElementById('mdPhone').innerText = phone;
        document.getElementById('mdAddress').innerText = (address && address !== 'null') ? address : 'N/A';
        document.getElementById('mdImage').src = '${pageContext.request.contextPath}/' + img;
        document.getElementById('mdHistory').innerHTML = document.getElementById('history-data-' + bookingID).innerHTML;

        var searchInput = document.getElementById('searchHistoryInput');
        if(searchInput) {
            searchInput.value = '';
        }
        filterCareHistory(); // Reset hiển thị danh sách

        document.getElementById('petDetailModal').style.display = 'flex';


    }

    function filterCareHistory() {
        var input = document.getElementById("searchHistoryInput").value.toLowerCase();
        var container = document.getElementById("mdHistory");
        // Lấy tất cả các khối accordion
        var items = container.getElementsByTagName("details");

        for (var i = 0; i < items.length; i++) {
            var summary = items[i].querySelector("summary"); // Phần Tiêu đề ngày
            var content = items[i].querySelector("div");     // Phần Nội dung ghi chú
            var textToSearch = "";

            if (summary) textToSearch += summary.innerText.toLowerCase();
            if (content) textToSearch += " " + content.innerText.toLowerCase();

            // Nếu tiêu đề hoặc nội dung có chứa từ khóa thì hiện, ngược lại thì ẩn
            if (textToSearch.includes(input)) {
                items[i].style.display = "";
            } else {
                items[i].style.display = "none";
            }
        }
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

        function openExtendModal(bookingID, currentEndDate) {
            document.getElementById('extendBookingID').value = bookingID;
            document.getElementById('newEndDateInput').value = currentEndDate;
            document.getElementById('newEndDateInput').min = currentEndDate; // Ngăn chọn ngày lùi về quá khứ
            document.getElementById('extendModal').style.display = 'flex';
        }
        function closeExtendModal() {
            document.getElementById('extendModal').style.display = 'none';
        }
    function searchTasks() {
        let input = document.getElementById('searchTaskInput').value.toLowerCase();
        let cards = document.getElementsByClassName('cat-card');

        for (let i = 0; i < cards.length; i++) {
            let searchData = cards[i].getAttribute('data-search').toLowerCase();
            if (searchData.includes(input)) {
                cards[i].style.display = "";
            } else {
                cards[i].style.display = "none";
            }
        }
    }
</script>
</body>
</html>