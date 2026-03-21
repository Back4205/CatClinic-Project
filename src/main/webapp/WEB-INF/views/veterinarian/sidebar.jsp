<%@ page contentType="text/html;charset=UTF-8" %>

<aside class="sidebar">
    <div>
        <div class="logo">
            <i class="fa-solid fa-paw"></i>
            <span>CatClinic</span>
        </div>

        <nav class="menu">
            <a href="DashboardController?page=${i}&dateFrom=${dateFrom}&dateTo=${dateTo}" class="menu-item ${activePage == 'dashboard' ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> Dashboard
            </a>

            <a href="assignedCases" class="menu-item ${activePage == 'assigned' ? 'active' : ''}">
                <i class="fa-solid fa-folder-open"></i> Treatment records
            </a>

            <a href="testlist" class="menu-item ${activePage == 'testlist' ? 'active' : ''}">
                <i class="fa-solid fa-vials"></i> Lab / X-ray
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