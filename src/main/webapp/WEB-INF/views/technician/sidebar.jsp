<aside class="sidebar">
    <div class="profile-section">
        <img src="https://cdn-icons-png.flaticon.com/512/4140/4140047.png" alt="Avatar" class="profile-img">
        <h3 class="profile-name">${sessionScope.acc.fullName != null ? sessionScope.acc.fullName : 'Receptionist'}</h3>
        <span class="profile-role">CATCLINIC PORTAL</span>
    </div>
    <nav class="nav-menu">
        <a href="${pageContext.request.contextPath}/technician/lab-hub" class="nav-item ${activePage == 'lab-hub' ? 'active' : ''}">
            <i class="fa-solid fa-table-cells-large"></i> TEST LAB
        </a>

        <a href="${pageContext.request.contextPath}/profile" class="nav-item ${activePage == 'Profile' ? 'active' : ''}">
            <i class="fa-solid fa-address-card"></i> profile
        </a>




    </nav>
    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
        <i class="fa-solid fa-arrow-right-from-bracket"></i> Logout
    </a>
</aside>


