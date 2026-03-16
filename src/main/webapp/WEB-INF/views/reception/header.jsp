<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 3/11/2026
  Time: 3:16 PM
  To change this template use File | Settings | File Templates.
--%>


<%--<header class="topbar">--%>
<%--    <div class="logo"><i class="bi bi-hospital"></i> CatClinic</div>--%>
<%--    <div class="user-info">--%>
<%--        <span class="name">${acc.fullName}</span>--%>
<%--        <div class="avatar">--%>
<%--            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</header>--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="topbar">
  <div class="logo">
    <a  style="text-decoration: none; color: inherit; display: flex; align-items: center; gap: 10px;">
      <i class="bi bi-hospital"></i> CatClinic
    </a>
  </div>


  <%--    <div class="user-info">--%>
  <%--        <span class="name">Wellcome ${acc.fullName}</span>--%>
  <%--        <div class="avatar">--%>
  <%--            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">--%>
  <%--        </div>--%>
  <%--    </div>--%>
  <div class="right-section">

    <div class="notification">
      <a href="${pageContext.request.contextPath}/reception/clear-notification" >
        <i class="bi bi-bell bell-icon"></i>

        <span class="noti-count">
          ${sessionScope.notificationCount}
        </span>
      </a>


      <!-- Dropdown -->
      <div class="noti-dropdown">

        <div class="noti-header">
          New Bookings
        </div>

        <div class="noti-list">

          <c:forEach var="n" items="${sessionScope.notifications}">
            <div class="noti-item">

              <b>${n.ownerName} - ${n.phone}</b>[${n.appointmentDate} ${n.appointmentTime}]





            </div>
          </c:forEach>

        </div>

      </div>

    </div>

    <!-- User -->
    <div class="user-info">

      <span class="name">Welcome ${acc.fullName}</span>

      <div class="avatar">
        <img src="${pageContext.request.contextPath}/image/default.jpg"
             alt="Profile Picture">
      </div>

    </div>

  </div>
</header>
