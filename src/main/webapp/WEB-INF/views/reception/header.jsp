<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<header class="topbar">
  <div class="logo">
    <a class="logo-link">
      <i class="bi bi-hospital"></i> <span>CatClinic</span>
    </a>
  </div>

  <div class="right-section">
    <div class="notification">
      <input type="checkbox" id="noti-toggle" class="noti-checkbox">

      <label for="noti-toggle" class="bell-wrapper">
        <i class="bi bi-bell bell-icon"></i>
        <c:if test="${sessionScope.notificationCount > 0}">
          <span class="noti-count">${sessionScope.notificationCount}</span>
        </c:if>
      </label>

      <div class="noti-dropdown">
        <div class="noti-header">
          <span>New Notification </span>
          <a href="${pageContext.request.contextPath}/reception/clear-notification" class="clear-btn">
           Has seen
          </a>
        </div>

        <div class="noti-list">
          <c:choose>
            <c:when test="${not empty sessionScope.notifications}">
              <c:forEach var="n" items="${sessionScope.notifications}">
                <div class="noti-item"
                     onclick="window.location.href='${pageContext.request.contextPath}/appointmentdetail?id=${n.bookingID}'"
                     style="cursor: pointer;">
                  <div class="noti-icon">
                    <i class="bi bi-calendar-check"></i>
                  </div>
                  <div class="noti-content">
                    <p class="noti-title">ID: ${n.bookingID} - <strong>${n.ownerName}</strong></p>
                    <p class="noti-detail">
                      <i class="bi bi-clock"></i>
                      <fmt:formatDate value="${n.appointmentDate}" pattern="dd/MM/yyyy" />
                      to <fmt:formatDate value="${n.appointmentTime}" pattern="HH:mm" />
                    </p>
                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div class="noti-empty">Do not New Notification</div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </div>

    <div class="user-info">
      <span class="name">WellCome, <strong>${acc.fullName}</strong></span>
      <div class="avatar">
        <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile">
      </div>
    </div>
  </div>
</header>