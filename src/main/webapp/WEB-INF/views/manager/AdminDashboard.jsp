<%-- 
    Document   : AdminDashboard
    Created on : Jan 30, 2026, 4:20:38 PM
    Author     : Son
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/AdminDashboardStyle.css" rel="stylesheet" type="text/css"/>

    </head>
    <body>
        <header class="admin-header">
    <div class="header-left">
        <h2>Admin Dashboard</h2>
    </div>
    <div class="header-right">
        <span>Welcome,some one</span>
        <div class="user-icon">👤</div>
    </div>
</header>

<!-- Main Content -->
<main class="admin-main">
    <div class="card-container">

        <!-- Account Management -->
        <div class="admin-card">
            <div class="card-icon">👥</div>
            <h3>Account Management</h3>
            <p>
                View, add, edit, or delete user accounts within the system.
            </p>
            <a href="account" class="btn-manage">Manage Now</a>
        </div>

        <!-- Service Management -->
        <div class="admin-card">
            <div class="card-icon">📦</div>
            <h3>Service Management</h3>
            <p>
                View, add, edit, or delete pet clinic services and prices.
            </p>
            <a href="#" class="btn-manage">Manage Now</a>
        </div>

    </div>
    </body>
</html>
