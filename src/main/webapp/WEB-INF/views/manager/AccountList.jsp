<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">
<title>Account List</title>

<!-- Layout -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">

<!-- Sidebar -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">

<!-- Header -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">

<!-- Page CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/AccountListStyle.css">

<link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

</head>

<body>

<div class="admin-layout">

    <!-- SIDEBAR -->
    <jsp:include page="sidebar.jsp"/>

    <div class="admin-main">

        <!-- HEADER -->
        <jsp:include page="header.jsp"/>

        <!-- PAGE -->
        <div class="admin-dashboard account-page">

            <div class="account-container">

                <!-- HEADER -->
                <div class="top-bar">

                    <div>
                        <h2>ACCOUNT LIST</h2>
                        <p>DIRECTORY & ROLE MANAGEMENT</p>
                    </div>

                    <div class="top-actions">

                        <button onclick="history.back()" class="btn btn-back">
                            BACK
                        </button>

                        <a href="addAccount" class="btn btn-primary">
                            ADD ACCOUNT
                        </a>

                    </div>

                </div>

                <!-- SEARCH -->
                <form action="account" method="get" class="toolbar">

                    <input type="text"
                           name="keyword"
                           value="${keyword}"
                           placeholder="Search name, username, email...">

                    <select name="role">

                        <option value="">ALL ROLES</option>

                        <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>

                        <option value="VETERINARIAN" ${role == 'VETERINARIAN' ? 'selected' : ''}>VETERINARIAN</option>

                        <option value="STAFF" ${role == 'STAFF' ? 'selected' : ''}>STAFF</option>

                        <option value="CUSTOMER" ${role == 'CUSTOMER' ? 'selected' : ''}>CUSTOMER</option>

                        <option value="RECEPTIONIST" ${role == 'RECEPTIONIST' ? 'selected' : ''}>RECEPTIONIST</option>

                    </select>

                    <button class="btn btn-dark">
                        SEARCH
                    </button>

                </form>

                <!-- ERROR -->
                <c:if test="${not empty sessionScope.error_account}">
                    <p class="error_account">${sessionScope.error_account}</p>
                    <c:remove var="error_account" scope="session"/>
                </c:if>

                <!-- TABLE -->
                <table class="account-table">

                    <thead>

                        <tr>
                            <th>ID</th>
                            <th>USERNAME</th>
                            <th>FULL NAME</th>
                            <th>ROLE</th>
                            <th>EMAIL</th>
                            <th>PHONE</th>
                            <th>ACTIONS</th>
                        </tr>

                    </thead>

                    <tbody>

                        <c:if test="${empty UserList}">
                            <tr>
                                <td colspan="7" style="text-align:center;color:#999">
                                    No account found
                                </td>
                            </tr>
                        </c:if>

                        <c:forEach var="u" items="${UserList}">

                            <tr>

                                <td>${u.userID}</td>

                                <td>${u.userName}</td>

                                <td>${u.fullName}</td>

                                <td>
                                    <span class="role ${u.roleName.toLowerCase()}">
                                        ${u.roleName}
                                    </span>
                                </td>

                                <td>${u.email}</td>

                                <td>${u.phone}</td>

                                <td class="actions">

                                    <a href="author?userID=${u.userID}&userName=${u.userName}"
                                       class="btn-action auth">
                                        AUTHOR
                                    </a>

                                    <a href="deleteUsers?id=${u.userID}&page=${page}&keyword=${keyword}&role=${role}"
                                       class="btn-action delete"
                                       onclick="return confirm('Are you sure?')">
                                        DELETE
                                    </a>

                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>

                </table>

                <!-- PAGINATION -->
                <c:if test="${totalPage > 1}">

                    <div class="pagination">

                        <c:if test="${page > 1}">
                            <a href="account?page=1&keyword=${keyword}&role=${role}">««</a>
                            <a href="account?page=${page-1}&keyword=${keyword}&role=${role}">«</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPage}" var="i">

                            <a href="account?page=${i}&keyword=${keyword}&role=${role}"
                               class="${i == page ? 'active' : ''}">
                                ${i}
                            </a>

                        </c:forEach>

                        <c:if test="${page < totalPage}">
                            <a href="account?page=${page+1}&keyword=${keyword}&role=${role}">»</a>
                            <a href="account?page=${totalPage}&keyword=${keyword}&role=${role}">»»</a>
                        </c:if>

                    </div>

                </c:if>

            </div>

        </div>

    </div>

</div>

</body>
</html>