<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 3/11/2026
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside class="sidebar">
  <div class="profile-section">
    <img src="https://cdn-icons-png.flaticon.com/512/4140/4140047.png" alt="Avatar" class="profile-img">
    <h3 class="profile-name">${sessionScope.acc.fullName != null ? sessionScope.acc.fullName : 'Receptionist'}</h3>
    <span class="profile-role">CATCLINIC PORTAL</span>
  </div>
  <nav class="nav-menu">
    <a href="${pageContext.request.contextPath}/view-booking-list" class="nav-item ${activePage == 'dashboard' ? 'active' : ''}">
      <i class="fa-solid fa-table-cells-large"></i> View Booking List
    </a>
    <a href="${pageContext.request.contextPath}/Booking2" class="nav-item ${activePage == 'booking' ? 'active' : ''}">
      <i class="fa-regular fa-calendar-plus"></i> Counter Booking
    </a>


    <a href="${pageContext.request.contextPath}/reception/billing-bookings" class="nav-item ${activePage == 'billing' ? 'active' : ''}">
      <i class="fa-solid fa-file-invoice-dollar"></i> Billing Bookings
    </a>

    <a href="${pageContext.request.contextPath}/reception/checkout-queue" class="nav-item ${activePage == 'CheckOut' ? 'active' : ''}">
      <i class="fa-solid fa-right-to-bracket"></i> Counter Check-out
    </a>

    <a href="${pageContext.request.contextPath}/profile" class="nav-item ${activePage == 'Profile' ? 'active' : ''}">
      <i class="fa-solid fa-address-card"></i> Profile
    </a>
  </nav>
  <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
    <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
  </a>
</aside>