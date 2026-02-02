<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Service List</title>
    <link href="css/ServiceListStyle.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<div class="container">
    <div class="header">
        <h2>Service List</h2>
        <a href="${pageContext.request.contextPath}/CreateService"
           class="btn-create">
            + Create New Service
        </a>
    </div>
    <div class="filter">
        <input type="text" id="searchInput" placeholder="Search service name...">
        <select id="statusFilter">
            <option value="all">All</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
        </select>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Time</th>
                <th>Status</th>
                <th>Manager</th>
            </tr>
        </thead>
        <tbody id="serviceTable">
            <c:forEach var="s" items="${serviceList}">
                <tr>
                    <td>${s.serviceID}</td>
                    <td>${s.nameService}</td>
                    <td>${s.price}</td>
                    <td>${s.timeService}</td>
                    <td>
                        <c:choose>
                            <c:when test="${s.isActive}">
                                Active
                            </c:when>
                            <c:otherwise>
                                Inactive
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/ViewServiceDetail?id=${s.serviceID}">View</a>
                        <a href="${pageContext.request.contextPath}/EditService?id=${s.serviceID}">Edit</a>
                        <form action="${pageContext.request.contextPath}/HideService"
                              method="post" >
                            <input type="hidden" name="id" value="${s.serviceID}">
                            <input type="hidden" name="action"
                                   value="${s.isActive ? 'hide' : 'show'}">
                            <button type="submit" class="btn-hide"> 
                                ${s.isActive ? 'Hide' : 'Show'}
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script>
    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const rows = document.querySelectorAll("#serviceTable tr");

    function applyFilter() {
        const keyword = searchInput.value.trim().toLowerCase();
        const status = statusFilter.value;

        rows.forEach(row => {
            // RESET hi?n th? tr??c
            row.style.display = "";

            const serviceName = row.cells[1].innerText.toLowerCase();
            const serviceStatus = row.cells[4].innerText.toLowerCase();

            const matchName = serviceName.includes(keyword);
            const matchStatus =
                status === "all" || serviceStatus === status;

            if (!(matchName && matchStatus)) {
                row.style.display = "none";
            }
        });
    }

    searchInput.addEventListener("keyup", applyFilter);
    statusFilter.addEventListener("change", applyFilter);
</script>