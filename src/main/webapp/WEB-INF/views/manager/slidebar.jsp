<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="sidebar">
    <div class="logo">
        <i class="fa-solid fa-cat"></i>
        <span>Cat Clinic</span>
    </div>

    <ul class="menu">

        <li>
            <a href="${pageContext.request.contextPath}/AdminDashboardController">
                <i class="fa-solid fa-chart-line"></i> Dashboard
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/account">
                <i class="fa-solid fa-users"></i> Account Management
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/ViewCategoryList">
                <i class="fa-solid fa-list"></i> Category Management
            </a>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/ViewCancelBookingList">
                <i class="fa-solid fa-money-bill-wave"></i> Refund Management
            </a>
        </li>
         <li>
            <a href="${pageContext.request.contextPath}/viewschedule">
                <i class="fa-solid fa-money-bill-wave"></i> Schedule
            </a>
        </li>


    </ul>
</div>