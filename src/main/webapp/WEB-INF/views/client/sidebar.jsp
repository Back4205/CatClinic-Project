<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<aside class="sidebar">
    <div class="profile-card">
        <div class="avatar large">
            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">
        </div>
        <h3>${user.userName}</h3>
        <p>CATCLINIC PORTAL</p>
    </div>
    <nav class="menu">
        <a href="${pageContext.request.contextPath}/cats" class="${activePage == 'cats' ? 'active' : ''}">
            <i class="bi bi-grid-fill"></i> Cat List
        </a>
        <a href="booking-history" class="${activePage == 'history' ? 'active' : ''}">
            <i class="bi bi-calendar-event"></i> Visit History
        </a>
        <a href="accessprofile" class="${activePage == 'profile' ? 'active' : ''}">
            <i class="bi bi-person-gear"></i> Profile & Security
        </a>
        <a href="#" class="${activePage == 'home' ? 'active' : ''}">
            <i class="bi bi-house"></i> Home
        </a>
        <a href="#" style="color: red; margin-top: 20px;">
            <i class="bi bi-box-arrow-right"></i> Logout
        </a>
    </nav>
</aside>