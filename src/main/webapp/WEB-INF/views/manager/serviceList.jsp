<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Service List</title>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/service.css">
</head>

<body data-context="${pageContext.request.contextPath}">

<div class="container">

    <div class="header">
        <h2>Service List</h2>

        <a href="${pageContext.request.contextPath}/CreateService"
           class="btn-primary">
            + Create New Service
        </a>
    </div>

    <!-- FILTER BAR -->
    <div class="filter-bar">
        <input type="text"
               id="searchInput"
               placeholder="Search service name..."
               onkeyup="loadService(1)">

        <select id="statusFilter" onchange="loadService(1)">
            <option value="all">All</option>
            <option value="true">Active</option>
            <option value="false">Inactive</option>
        </select>

        <select id="sortPrice" onchange="loadService(1)">
            <option value="">Sort by price</option>
            <option value="asc">Increase</option>
            <option value="desc">Reduce</option>
        </select>
    </div>

    <!-- TABLE AREA (AJAX LOAD) -->
    <div id="tableArea"></div>

</div>

<script src="${pageContext.request.contextPath}/js/service.js"></script>
<script>
    window.onload = function () {
        loadService(1);
    }
</script>

</body>
</html>
