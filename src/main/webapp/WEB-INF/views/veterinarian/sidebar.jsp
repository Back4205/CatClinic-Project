<%@ page contentType="text/html;charset=UTF-8" %>

<aside class="sidebar">
    <div>
        <div class="logo">
            <i class="fa-solid fa-paw"></i>
            <span>CatClinic</span>
        </div>

        <nav class="menu">
            <a href="DashboardController" class="menu-item ${activePage == 'dashboard' ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> Dashboard
            </a>

            <a href="assignedCases" class="menu-item ${activePage == 'assigned' ? 'active' : ''}">
                <i class="fa-solid fa-folder-open"></i> Assigned Cases
            </a>

            <a href="emr" class="menu-item ${activePage == 'emr' ? 'active' : ''}">
                <i class="fa-solid fa-file-medical"></i> EMR Management
            </a>

            <a href="lab" class="menu-item ${activePage == 'lab' ? 'active' : ''}">
                <i class="fa-solid fa-vials"></i> Lab / X-ray
            </a>

            <a href="hospital" class="menu-item ${activePage == 'hospital' ? 'active' : ''}">
                <i class="fa-solid fa-bed"></i> Hospitalization
            </a>

            <a href="schedule" class="menu-item ${activePage == 'schedule' ? 'active' : ''}">
                <i class="fa-solid fa-calendar-days"></i> Schedule
            </a>
        </nav>
    </div>

    <div class="bottom-menu">
        <a href="settings" class="menu-item">
            <i class="fa-solid fa-gear"></i> Settings
        </a>

        <a href="logout" class="menu-item logout">
            <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>
    </div>
</aside>