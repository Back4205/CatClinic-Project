<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int totalAccounts = 3;
    int totalCategories = 4;
    int cancelledBookings = 3;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cat Clinic Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Admin-Dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slidebar-admin.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>

<div class="container">

    <!-- INCLUDE SIDEBAR HERE -->
    <jsp:include page="slidebar.jsp" />

    <!-- MAIN CONTENT -->
    <div class="main">

        <!-- HEADER -->
        <div class="header">
            <h2>Cat Clinic Admin Dashboard</h2>
            <div class="admin-info">
                <span>Admin User</span>
                <img src="https://i.pravatar.cc/40" alt="Admin Avatar">
            </div>
        </div>

        <!-- DASHBOARD CONTENT -->
        <div class="dashboard">

            <div class="cards">
                <div class="card">
                    <div class="icon orange">
                        <i class="fa-solid fa-users"></i>
                    </div>
                    <div>
                        <p>Total Accounts</p>
                        <h3><%= totalAccounts %></h3>
                    </div>
                </div>

                <div class="card">
                    <div class="icon blue">
                        <i class="fa-solid fa-tags"></i>
                    </div>
                    <div>
                        <p>Total Categories</p>
                        <h3><%= totalCategories %></h3>
                    </div>
                </div>

                <div class="card">
                    <div class="icon red">
                        <i class="fa-solid fa-xmark"></i>
                    </div>
                    <div>
                        <p>Cancelled Bookings</p>
                        <h3><%= cancelledBookings %></h3>
                    </div>
                </div>
            </div>

            <div class="welcome-box">
                <h3>Welcome to Cat Clinic Admin Panel</h3>
                <p>
                    This system allows you to manage staff accounts, service categories,
                    and handle cancelled bookings with refund processing efficiently.
                </p>
            </div>

        </div>
    </div>

</div>

</body>
</html>