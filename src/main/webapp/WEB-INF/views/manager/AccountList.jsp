<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Account List</title>
        <link href="css/AccountListStyle.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slidebar-admin.css">
        <link rel="stylesheet"
href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <jsp:include page="slidebar.jsp" />
        <div class="account-container">
            <!-- ===== HEADER ===== -->
            <div class="top-bar">
                <div>
                    <h2>ACCOUNT LIST</h2>
                    <p>DIRECTORY & ROLE MANAGEMENT</p>
                </div>

                <div class="top-actions">
                    <button type="button" onclick="history.back()" class="btn btn-back">BACK</button>
                    <a href="addAccount" class="btn btn-primary">ADD ACCOUNT</a>
                </div>
            </div>

            <!-- ===== SEARCH & FILTER ===== -->
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

                <button class="btn btn-dark">SEARCH</button>
            </form>
            <c:if test="${not empty sessionScope.error_account}">
                <p class="error_account">
                    ${sessionScope.error_account}
                </p>

                <!-- XÓA SAU KHI HIỂN THỊ -->
                <c:remove var="error_account" scope="session"/>
            </c:if>

            <!-- ===== TABLE ===== -->
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
                            <td colspan="7" style="text-align:center;color:#999;">
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
                                <a href="author?userID=${u.userID}&userName=${u.userName}" class="btn-action auth">AUTHOR</a>
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

            <!-- ===== PAGINATION ===== -->
            <c:if test="${totalPage > 1}">
                <div class="pagination">

                    <!-- FIRST PAGE -->
                    <c:if test="${page > 1}">
                        <a href="account?page=1&keyword=${keyword}&role=${role}">««</a>
                    </c:if>

                    <!-- PREV -->
                    <c:if test="${page > 1}">
                        <a href="account?page=${page-1}&keyword=${keyword}&role=${role}">«</a>
                    </c:if>

                    <!-- TÍNH START & END (TỐI ĐA 3 TRANG) -->
                    <c:set var="start" value="${page - 1}" />
                    <c:set var="end" value="${page + 1}" />

                    <c:if test="${start < 1}">
                        <c:set var="start" value="1"/>
                        <c:set var="end" value="3"/>
                    </c:if>

                    <c:if test="${end > totalPage}">
                        <c:set var="end" value="${totalPage}"/>
                        <c:set var="start" value="${totalPage - 2}"/>
                    </c:if>

                    <c:if test="${totalPage < 3}">
                        <c:set var="start" value="1"/>
                        <c:set var="end" value="${totalPage}"/>
                    </c:if>

                    <!-- PAGE NUMBER -->
                    <c:forEach begin="${start}" end="${end}" var="i">
                        <a href="account?page=${i}&keyword=${keyword}&role=${role}"
                           class="${i == page ? 'active' : ''}">
                            ${i}
                        </a>
                    </c:forEach>

                    <!-- NEXT -->
                    <c:if test="${page < totalPage}">
                        <a href="account?page=${page+1}&keyword=${keyword}&role=${role}">»</a>
                    </c:if>

                    <!-- LAST PAGE -->
                    <c:if test="${page < totalPage}">
                        <a href="account?page=${totalPage}&keyword=${keyword}&role=${role}">»»</a>
                    </c:if>

                </div>
            </c:if>

        </div>

    </body>
</html>
