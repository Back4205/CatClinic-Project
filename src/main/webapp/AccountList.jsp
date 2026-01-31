<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Account List</title>
        <link rel="stylesheet" href="css/AccountListStyle.css"/>
    </head>
    <body>

        <div class="account-container">

            <!-- ===== HEADER ===== -->
            <div class="top-bar">
                <div>
                    <h2>ACCOUNT LIST</h2>
                    <p>DIRECTORY & ROLE MANAGEMENT</p>
                </div>
                <a href="addAccount" class="btn btn-primary">+ ADD ACCOUNT</a>
            </div>

            <!-- ===== TOOLBAR ===== -->
            <div class="toolbar">
                <input type="text" id="searchInput"
                       placeholder="Search name, username, email...">

                <select id="roleFilter">
                    <option value="all">ALL ROLES</option>
                    <option value="manager">MANAGER</option>
                    <option value="veterinarian">VETERINARIAN</option>
                    <option value="receptionist">STAFF</option>
                    <option value="receptionist">CUSTOMER</option>

                </select>

                <button id="btnSearch" class="btn btn-dark">Search</button>
            </div>

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
                    <c:forEach var="u" items="${UserList}">
                        <tr data-role="${u.roleName}">
                            <td>${u.userID}</td>
                            <td>${u.userName}</td>
                            <td>${u.fullName}</td>
                            <td>
                                <span class="badge ${u.roleName.toLowerCase()}">
                                    ${u.roleName}
                                </span>
                            </td>
                            <td>${u.email}</td>
                            <td>${u.phone}</td>
                            <td class="actions">
                                <a href="authorizeUser?id=${u.userID}" class="btn-action auth">AUTHOR</a>
                                <a href="editUser?id=${u.userID}" class="btn-action edit">EDIT</a>
                                <a href="deleteUsers?id=${u.userID}"
                                   class="btn-action delete"
                                   onclick="return confirm('Are you sure?')">
                                    DELETE
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>

        <!-- ===== SCRIPT SEARCH + FILTER ===== -->
        <script>
            const searchInput = document.getElementById("searchInput");
            const roleFilter = document.getElementById("roleFilter");
            const btnSearch = document.getElementById("btnSearch");
            const rows = document.querySelectorAll(".account-table tbody tr");

            function filterTable() {
                const keyword = searchInput.value.toLowerCase().trim();
                const role = roleFilter.value;

                rows.forEach(row => {
                    const rowText = row.innerText.toLowerCase();
                    const rowRole = row.dataset.role.toLowerCase();

                    const matchSearch = rowText.includes(keyword);
                    const matchRole = (role === "all") || (rowRole === role);

                    row.style.display = (matchSearch && matchRole) ? "" : "none";
                });
            }

            // Click search
            btnSearch.addEventListener("click", filterTable);

            // Gõ là search luôn
            searchInput.addEventListener("keyup", filterTable);

            // Change role
            roleFilter.addEventListener("change", () => {
                if (roleFilter.value === "all") {
                    searchInput.value = "";
                }
                filterTable();
            });
        </script>

    </body>
</html>
