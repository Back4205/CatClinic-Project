<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside class="sidebar">
    <div class="profile-section">
        <img src="https://cdn-icons-png.flaticon.com/512/4140/4140047.png" alt="Avatar" class="profile-img">
        <h3 class="profile-name">${sessionScope.acc.fullName != null ? sessionScope.acc.fullName : 'Receptionist'}</h3>
        <span class="profile-role">CATCLINIC PORTAL</span>
    </div>
    <nav class="nav-menu">
        <a href="${pageContext.request.contextPath}/reception/counter-booking" class="nav-item ${activePage == 'counter-booking' ? 'active' : ''}">
            <i class="fa-regular fa-calendar-plus"></i> Counter Booking
        </a>
        <a href="${pageContext.request.contextPath}/reception/" class="nav-item ${activePage == 'dashboard' ? 'active' : ''}">
            <i class="fa-solid fa-table-cells-large"></i> View Booking List
        </a>
    </nav>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
        <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
    </a>
</aside>