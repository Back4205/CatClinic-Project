<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<div class="header">

    <div class="header-left">
        <h1>Cat Clinic</h1>
        <span class="today-date">${todayDate}</span>
    </div>

    <div class="header-right">

        <div class="notification-wrapper" onclick="toggleNoti()">

            <i class="fa-regular fa-bell notification"></i>

            <span id="notiCount" class="noti-count">
                ${unreadNoti}
            </span>

            <div id="notiList" class="noti-list">
                <ul id="notiUl">
                    <c:forEach var="noti" items="${listNoti}">
                        <li class="${noti.isRead ? 'noti-read' : 'noti-unread'}">
                            <a href="notification?NotificationId=${noti.notificationID}">
                                ${noti.message} id = ${noti.notificationID}
                                <small>${noti.createdAt}</small>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="user-info">

            <span class="user-name">
                Dr. ${sessionScope.acc.fullName}
            </span>

            <div class="avatar">
                <i class="fa-solid fa-user"></i>
            </div>

        </div>

    </div>



</div>

<script>
    let vetId = "${sessionScope.vetID}";

    if (vetId && vetId !== "null") {
        const socket = new WebSocket(
                "ws://localhost:9999/CatClinicProject/notification/" + vetId
                );

        socket.onopen = function () {
            console.log("WebSocket connected for vet:", vetId);
        };

        socket.onmessage = function (event) {
            let data = JSON.parse(event.data); // {id, message, type, createdAt}

            let count = document.getElementById("notiCount");
            let ul = document.getElementById("notiUl");

            if (count) {
                let current = parseInt(count.innerText) || 0;
                count.innerText = current + 1;
            }

            if (ul) {
                let li = document.createElement("li");
                li.classList.add("noti-unread");

                let timestamp = data.createdAt ? new Date(data.createdAt) : new Date(); // nếu server không gửi thì dùng thời gian hiện tại
                li.innerHTML = `
        <a href="notification?NotificationId=${data.id}">
    ${data.message}
            <small>${timestamp.toLocaleString()}</small>
        </a>
    `;
                ul.prepend(li);
            }

            // Nếu muốn reload page cho loại notification nhất định
            if (data.type === "record_changed") {
                location.reload(); // reload page hiện tại
            }
        };
    }

// toggle hiển thị notification
    function toggleNoti() {
        let list = document.getElementById("notiList");
        list.style.display = (list.style.display === "block") ? "none" : "block";
    }
    function checkStatus() {
        fetch("checkRecordStatus")
                .then(response => response.text())
                .then(data => {
                    console.log("Server:", data);

                    if (data === "changed") {
                        location.reload();
                    }
                });

    }

    setInterval(checkStatus, 5000);
</script>