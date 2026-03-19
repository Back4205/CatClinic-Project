<%@ page contentType="text/html;charset=UTF-8" %>

<%
    int totalAccounts = 20;
    int totalCategories = 8;
    int cancelledBookings = 5;
%>

<!DOCTYPE html>
<html>
<head>

    <title>Admin Dashboard</title>

    <!-- Dashboard CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">

    <!-- Sidebar CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/sidebar-admin.css">

    <!-- Header CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/header-admin.css">

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>

<body>

<div class="admin-layout">

    <!-- SIDEBAR -->
    <jsp:include page="sidebar.jsp"/>

    <!-- MAIN CONTENT -->
    <div class="admin-main">

        <!-- HEADER -->
        <jsp:include page="header.jsp"/>

        <!-- DASHBOARD CONTENT -->
        <div class="admin-dashboard">

            <h2 class="admin-title">
                Admin Dashboard
            </h2>

            <!-- STATISTIC CARDS -->
            <div class="admin-cards">

                <div class="admin-card">

                    <i class="fa-solid fa-users"></i>

                    <p>Total Accounts</p>

                    <h3><%= totalAccounts %></h3>

                </div>

                <div class="admin-card">

                    <i class="fa-solid fa-list"></i>

                    <p>Total Categories</p>

                    <h3><%= totalCategories %></h3>

                </div>

                <div class="admin-card">

                    <i class="fa-solid fa-money-bill-wave"></i>

                    <p>Cancelled Bookings</p>

                    <h3><%= cancelledBookings %></h3>

                </div>

            </div>

            <!-- WELCOME BOX -->
            <div class="admin-welcome">

                <h3>Welcome Admin</h3>

                <p>
                    This dashboard allows administrators to manage system data
                    including accounts, service categories, and refund
                    processing for cancelled bookings.
                </p>

            </div>

        </div>

    </div>

</div>

</body>
</html>