<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Record Care Diary | CatClinic</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- CSS dùng chung của Care Staff -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/carestaff-style.css">
    <!-- CSS riêng cho màn hình Diary vừa được tách -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/record-care-diary.css">
</head>
<body>
<div class="page-shell">
    <header class="topbar">
        <div class="logo"><a style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 10px;"><i class="bi bi-hospital"></i> CatClinic</a></div>
        <div class="right-section">
            <div class="user-info">
                <span class="name">Welcome ${sessionScope.acc.fullName}</span>
                <div class="avatar"><img src="${pageContext.request.contextPath}/image/default.jpg"></div>
            </div>
        </div>
    </header>

    <div class="main-body">
        <!-- SIDEBAR -->
        <aside class="sidebar">
            <div class="profile-section">
                <img src="https://cdn-icons-png.flaticon.com/512/4140/4140047.png" class="profile-img">
                <h3 class="profile-name">${sessionScope.acc.fullName}</h3>
                <span class="profile-role">CATCLINIC PORTAL</span>
            </div>
            <nav class="nav-menu">
                <a href="${pageContext.request.contextPath}/staff/daily-care-tasks" class="nav-item">
                    <i class="fa-regular fa-calendar-check"></i> View Care Task
                </a>
                <a href="${pageContext.request.contextPath}/staff/record-care-diary" class="nav-item active">
                    <i class="fa-solid fa-book-medical"></i> Record Care Diary
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="nav-item"><i class="fa-solid fa-address-card"></i> Profile</a>
            </nav>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn"><i class="fa-solid fa-arrow-right-from-bracket"></i> Logout</a>
        </aside>

        <!-- CONTENT -->
        <div class="content-area">
            <h1 class="page-title"><i class="bi bi-journal-text"></i> Record Care Diary</h1>
            <p style="color: #6b7280; margin-bottom: 20px;">Select a patient to record their daily health and behavior log.</p>

            <div style="margin-bottom: 20px; position: relative;">
                <i class="bi bi-search" style="position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: #6b7280;"></i>
                <input type="text" id="searchDiaryInput" onkeyup="searchDiary()" placeholder="Tìm kiếm theo tên mèo, tên chủ hoặc SĐT..."
                       style="width: 100%; padding: 12px 15px 12px 40px; border: 1px solid #e5e7eb; border-radius: 8px; outline: none; font-size: 14px; box-shadow: 0 1px 2px rgba(0,0,0,0.05);">
            </div>

            <div style="display: flex; gap: 15px; margin-bottom: 20px; font-weight: bold; font-size: 13px;">
                <span><i class="dot bg-green"></i> Updated</span>
                <span><i class="dot bg-orange"></i> Needs Update</span>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <c:forEach items="${allTasks}" var="t">
                    <c:set var="isUpdated" value="${not empty t.note}" />


                    <%-- Thêm flex-direction: column để nội dung thẻ có thể xếp dọc (Header trên, Note dưới) --%>
                    <div class="diary-card" style="display: flex; flex-direction: column;" data-search="${t.catName} ${t.ownerName} ${t.ownerPhone}">

                            <%-- PHẦN 1: HEADER CỦA CARD (Nằm trên 1 hàng ngang) --%>
                        <div style="display: flex; justify-content: space-between; align-items: center; width: 100%;">

                                <%-- Thông tin Mèo (Bên trái) --%>
                            <div class="d-info">
                                <img src="${pageContext.request.contextPath}/${t.catImage}" style="width:50px; height:50px; border-radius:8px; object-fit:cover;">
                                <div>
                                    <h3 style="margin:0; font-size: 16px;">${t.catName}
                                        <i class="dot ${isUpdated ? 'bg-green' : 'bg-orange'}"></i>
                                    </h3>
                                    <span style="font-size:12px; color: #6b7280;"><i class="bi bi-check-circle-fill ${isUpdated ? 'text-green' : ''}"></i> ${isUpdated ? 'Updated' : 'Pending'}</span>
                                </div>
                            </div>

                                <%-- Trạng thái & Nút bấm (Bên phải) --%>
                            <div style="display: flex; gap: 20px; align-items: center;">
                                <div class="d-status">
                                    <span>STATUS</span>
                                    <strong class="${t.status == 'Completed' ? 'text-green' : 'text-orange'}">${t.status}</strong>
                                </div>
                                <c:choose>
                                    <%-- Nếu checkOutTime CÓ dữ liệu => Đã xuất viện => Hiện ổ khóa đỏ --%>
                                    <c:when test="${not empty t.checkOutTime}">
                                        <div style="color: #dc2626; text-align: center; margin-left: 10px;">
                                            <i class="bi bi-lock-fill" style="font-size: 18px;"></i>
                                            <span style="font-size: 10px; font-weight: bold; display: block; text-transform: uppercase;">Checked Out</span>
                                        </div>
                                    </c:when>

                                    <%-- Nếu chưa xuất viện => Hiện nút bấm dấu + như bình thường --%>
                                    <c:otherwise>
                                        <button class="btn-add" onclick="openDiaryModal(${t.careJID}, '${t.catName}', '${t.status}')">
                                            <i class="bi bi-plus-lg"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                            <%-- KẾT THÚC PHẦN 1 (HEADER) --%>

                            <%-- PHẦN 2: NỘI DUNG NOTE (Nằm dưới cùng, chiếm full chiều rộng) --%>
                        <c:if test="${not empty t.note}">
                            <div style="
                margin-top: 15px;
                padding: 12px 15px;
                background-color: #fff7ed;
                border-left: 4px solid #ea580c;
                border-radius: 6px;
                font-size: 13px;
                color: #374151;
                width: 100%;">

                                <div style="font-weight: bold; color: #ea580c; margin-bottom: 5px; text-transform: uppercase; font-size: 11px;">
                                    <i class="bi bi-journal-text"></i> Today's Care Record
                                </div>

                                    <%-- white-space: pre-wrap giúp giữ nguyên các dấu xuống dòng khi JS gom chuỗi --%>
                                <div style="white-space: pre-wrap; line-height: 1.5;">${t.note}</div>
                            </div>
                        </c:if>

                    </div>
                    <%-- KẾT THÚC THẺ DIARY-CARD Ở ĐÂY MỚI ĐÚNG NHÉ! --%>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- MODAL UI -->
<div id="diaryModal" class="modal-overlay" style="display: none;">
    <div class="modal-content" style="width: 550px;">
        <div class="modal-header" style="background: var(--orange); color: white;">
            <h3 style="color: white;"><i class="bi bi-clipboard2-pulse"></i> Daily Care Diary</h3>
            <p style="margin: 5px 0 0 0; font-size: 13px; color: #ffedd5;">
                <span id="diaryCatName" style="font-weight: bold; font-size: 15px;"></span> • ${today}
            </p>
            <button class="close-btn" style="color: white;" onclick="closeDiaryModal()">&times;</button>
        </div>

        <form id="diaryForm" action="${pageContext.request.contextPath}/staff/record-care-diary" method="POST">
            <input type="hidden" name="action" value="saveDiary">
            <input type="hidden" name="careJID" id="mdCareJID">
            <input type="hidden" name="combinedNote" id="mdCombinedNote"> <!-- Nơi hứng text gom từ JS -->

            <div class="modal-body" style="flex-direction: column; overflow-y: auto; max-height: 60vh;">
                <div class="dm-grid" style="width: 100%;">
                    <div>
                        <span class="dm-label">Current Status</span>
                        <input type="hidden" name="status" id="mdStatusHidden">
                        <select id="mdStatus" class="dm-input" disabled style="background-color: #f3f4f6; color: #9ca3af; cursor: not-allowed; border-color: #e5e7eb;">

                            <option value="Pending">Pending</option>
                            <option value="In Progress">In Progress</option>
                            <option value="Completed">Completed</option>
                        </select>
                        <p style="font-size: 11px; color: #9ca3af; margin-top: 5px; font-style: italic;">
                            * Status updates automatically based on task completion.
                        </p>
                    </div>
                </div>

                <span class="dm-label"><i class="bi bi-cup-hot"></i> Feeding & Cleaning</span>
                <div class="dm-grid" style="width: 100%;">
                    <textarea id="feedInput" class="dm-input" rows="3" placeholder="Amount, type of food..."></textarea>
                    <textarea id="cleanInput" class="dm-input" rows="3" placeholder="Litter box status..."></textarea>
                </div>

                <span class="dm-label"><i class="bi bi-heart-pulse"></i> Medical & Observations</span>
                <input type="text" id="medInput" class="dm-input" style="margin-bottom: 15px;" placeholder="Dosage, time administered...">

                <div class="dm-grid" style="grid-template-columns: 1fr 1fr 1fr; width: 100%;">
                    <select id="appetiteInput" class="dm-input"><option>Normal</option><option>Poor</option><option>Great</option><option>None</option></select>
                    <select id="behaviorInput" class="dm-input"><option>Calm</option><option>Active</option><option>Lethargic</option><option>Aggressive</option></select>
                    <select id="vitalsInput" class="dm-input"><option>Stable</option><option>Abnormal</option></select>
                </div>
            </div>

            <div style="padding: 15px 20px; background: #fafafa; border-top: 1px solid #e5e7eb; display: flex; justify-content: space-between;">
                <button type="button" style="background: transparent; border: none; color: #6b7280; font-weight: bold; cursor: pointer;" onclick="closeDiaryModal()">Cancel</button>
                <button type="button" class="btn-view-detail" style="margin: 0;" onclick="submitDiary()">Save Care Record</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openDiaryModal(careJID, catName, status) {
        document.getElementById('mdCareJID').value = careJID;
        document.getElementById('mdStatus').value = status;
        document.getElementById('diaryCatName').innerText = catName;
        document.getElementById('mdStatusHidden').value = status;

        // Clear input cũ
        document.getElementById('feedInput').value = "";
        document.getElementById('cleanInput').value = "";
        document.getElementById('medInput').value = "";

        document.getElementById('diaryModal').style.display = 'flex';
    }

    function closeDiaryModal() {
        document.getElementById('diaryModal').style.display = 'none';
    }

    // GOM CHUỖI TRƯỚC KHI SUBMIT (BẢN AN TOÀN CHO JSP)
    function submitDiary() {
        let appetite = document.getElementById('appetiteInput').value;
        let behavior = document.getElementById('behaviorInput').value;
        let vitals = document.getElementById('vitalsInput').value;
        let feed = document.getElementById('feedInput').value;
        let clean = document.getElementById('cleanInput').value;
        let med = document.getElementById('medInput').value;

        // Lấy Giờ:Phút hiện tại
        let now = new Date();
        let timeString = now.getHours().toString().padStart(2, '0') + ':' + now.getMinutes().toString().padStart(2, '0');

        // Chèn timeString vào đầu câu (Sửa lại biến finalNote cũ của bạn)
        let finalNote = "[" + timeString + "] [Appetite: " + appetite + " | Behavior: " + behavior + " | Vitals: " + vitals + "]\n";

        if (feed) finalNote += "- Feeding: " + feed + "\n";
        if (clean) finalNote += "- Cleaning: " + clean + "\n";
        if (med) finalNote += "- Med/Others: " + med;

        document.getElementById('mdCombinedNote').value = finalNote.trim();
        document.getElementById('diaryForm').submit();
    }

    function searchDiary() {
        let input = document.getElementById('searchDiaryInput').value.toLowerCase();
        let cards = document.getElementsByClassName('diary-card');

        for (let i = 0; i < cards.length; i++) {
            let searchData = cards[i].getAttribute('data-search').toLowerCase();
            if (searchData.includes(input)) {
                cards[i].style.display = "flex";
            } else {
                cards[i].style.display = "none";
            }
        }
    }

</script>
</body>
</html>