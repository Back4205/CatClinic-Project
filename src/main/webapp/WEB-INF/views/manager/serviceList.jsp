<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Service List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/service-list.css">
</head>
<body>

<div class="container">

    <!-- HEADER -->
<div class="header">

    <a href="${pageContext.request.contextPath}/ViewCategoryList"
       class="btn-back">
        Back to Category List
    </a>

    <h2 class="title">Service List</h2>

    <a href="${pageContext.request.contextPath}/CreateService?categoryID=${categoryID}"
       class="btn-create">
        + Create New Service
    </a>
</div>


    <!-- FILTER -->
    <div class="filter-card">
        <input type="text" id="searchInput"
               placeholder="Search service name...">

        <select id="statusFilter">
            <option value="all">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
        </select>
    </div>

    <!-- TABLE -->
    <div class="table-wrapper">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price (VND)</th>
                    <th>Description</th>
                    <th>Time (Min)</th>
                    <th>Status</th>
                    <th>Image</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody id="serviceTable">
                <c:forEach var="s" items="${serviceList}">
                    <tr>
                        <td>${s.serviceID}</td>
                        <td>${s.nameService}</td>
                        <td>${s.price}</td>
                        <td>${s.description}</td>
                        <td>${s.timeService}</td>

                        <td>
                            <span class="${s.isActive ? 'status-active' : 'status-inactive'}">
                                ${s.isActive ? 'Active' : 'Inactive'}
                            </span>
                        </td>

                        <td>
                            <img src="${s.imgUrl}" class="service-img">
                        </td>

                        <td class="action-buttons">

                            <a href="${pageContext.request.contextPath}/EditService?serviceID=${s.serviceID}"
                               class="btn-edit">
                                Edit
                            </a>

                            <form action="${pageContext.request.contextPath}/HideService"
                                  method="post"
                                  onsubmit="return confirmAction(this)">
                                <input type="hidden" name="serviceID" value="${s.serviceID}">
                                <input type="hidden" name="categoryID" value="${s.categoryID}">
                                <input type="hidden" name="action"
                                       value="${s.isActive ? 'hide' : 'show'}">

                                <button type="submit"
                                        class="${s.isActive ? 'btn-hide' : 'btn-show'}">
                                    ${s.isActive ? 'Hide' : 'Show'}
                                </button>
                            </form>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<script>
    const searchInput = document.getElementById("searchInput");
    const statusFilter = document.getElementById("statusFilter");
    const rows = document.querySelectorAll("#serviceTable tr");

    function applyFilter() {
        const keyword = searchInput.value.trim().toLowerCase();
        const status = statusFilter.value;

        rows.forEach(row => {
            row.style.display = "";

            const serviceName = row.cells[1].innerText.toLowerCase();
            const serviceStatus = row.cells[5].innerText.toLowerCase();

            const matchName = serviceName.includes(keyword);
            const matchStatus =
                status === "all" || serviceStatus === status;

            if (!(matchName && matchStatus)) {
                row.style.display = "none";
            }
        });
    }

    function confirmAction(form) {
        const action = form.querySelector("input[name='action']").value;
        return confirm(
            action === "hide"
            ? "Are you sure you want to hide this service?"
            : "Are you sure you want to show this service?"
        );
    }

    searchInput.addEventListener("keyup", applyFilter);
    statusFilter.addEventListener("change", applyFilter);
</script>

</body>
</html>
