

<aside class="sidebar">
    <div class="profile-card">
        <div class="avatar large">
            <img src="${pageContext.request.contextPath}/image/default.jpg" alt="Profile Picture">
        </div>
        <h3>${acc.fullName}</h3>
        <p>CATCLINIC PORTAL</p>
    </div>
    <nav class="menu">
        <a href="${pageContext.request.contextPath}/reception/counter-booking" class="${activePage == 'counter-booking' ? 'active' : ''}">
            <i class="fa-regular fa-calendar-plus"></i> Counter Booking
        </a>

        <a href="${pageContext.request.contextPath}/reception/counter-cancellation" class="${activePage == 'counter-cancellation' ? 'active' : ''}">
            <i class="fa-regular fa-rectangle-xmark"></i> Counter Cancellation
        </a>

        <a href="${pageContext.request.contextPath}/reception/home" class="${activePage == 'dashboard' ? 'active' : ''}">
            <i class="fa-solid fa-table-cells-large"></i> View Booking List
        </a>

        <a href="${pageContext.request.contextPath}/reception/check-in" class="${activePage == 'check-in' ? 'active' : ''}">
            <i class="fa-regular fa-circle-check"></i> Check-in Patient
        </a>
        <a href="${pageContext.request.contextPath}/loadinfo" class="${activePage == 'home' ? 'active' : ''}">
            <i class="bi bi-house"></i> Home
        </a>
        <a href="${pageContext.request.contextPath}/logout" style="color: red; margin-top: 20px;">
            <i class="bi bi-box-arrow-right"></i> Logout
        </a>
    </nav>
</aside>