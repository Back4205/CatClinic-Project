<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Category Management</title>

<!-- Dashboard Layout -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">

<!-- Sidebar -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">

<!-- Header -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">

<!-- Page CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-list.css">

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

            <div class="category-page">
            <div class="category-container">

                <!-- TITLE -->
                <div class="top-bar">

                    <div>
                        <h2>CATEGORY MANAGEMENT</h2>
                        <p>Manage clinic service categories</p>
                    </div>

                    <a class="btn-add"
                       href="${pageContext.request.contextPath}/CreateCategory">
                        <i class="fa-solid fa-plus"></i> Add Category
                    </a>

                </div>


                <!-- SEARCH + FILTER -->
                <form action="${pageContext.request.contextPath}/ViewCategoryList"
                      method="GET"
                      class="toolbar">

                    <input type="text"
                           name="search"
                           placeholder="Search category..."
                           value="${currentSearch}">

                    <button type="submit" name="status" value="ALL">All</button>
                    <button type="submit" name="status" value="Active">Active</button>
                    <button type="submit" name="status" value="Inactive">Inactive</button>

                </form>


                <!-- CATEGORY TABLE -->
                <table class="category-table">

                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Banner</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>

                        <!-- Empty message -->
                        <c:if test="${empty categoryList}">
                            <tr>
                                <td colspan="6" style="text-align:center;color:#999;">
                                    No category found
                                </td>
                            </tr>
                        </c:if>

                        <!-- Category list -->
                        <c:forEach var="c" items="${categoryList}">

                            <tr>

                                <td>${c.categoryID}</td>

                                <td>${c.categoryName}</td>

                                <td>
                                    <img class="banner-img"
                                         src="${c.banner}"
                                         alt="Category Banner">
                                </td>

                                <td>${c.description}</td>

                                <td>
                                    <span class="status ${c.active ? 'active' : 'inactive'}">
                                        ${c.active ? 'Active' : 'Inactive'}
                                    </span>
                                </td>

                                <td class="action">

                                    <!-- VIEW -->
                                    <a class="btn-action"
                                       href="${pageContext.request.contextPath}/ViewServiceList?id=${c.categoryID}">
                                        <i class="fa-solid fa-eye"></i> View
                                    </a>

                                    <!-- EDIT -->
                                    <a class="btn-action edit"
                                       href="${pageContext.request.contextPath}/EditCategory?id=${c.categoryID}">
                                        <i class="fa-solid fa-pen"></i> Edit
                                    </a>

                                    <!-- HIDE / SHOW -->
                                    <form action="${pageContext.request.contextPath}/HideCategory"
                                          method="post">

                                        <input type="hidden"
                                               name="id"
                                               value="${c.categoryID}">

                                        <input type="hidden"
                                               name="action"
                                               value="${c.active ? 'hide' : 'show'}">

                                        <button class="btn-action" type="submit">

                                            <c:choose>

                                                <c:when test="${c.active}">
                                                    <i class="fa-solid fa-eye-slash"></i> Hide
                                                </c:when>

                                                <c:otherwise>
                                                    <i class="fa-solid fa-eye"></i> Show
                                                </c:otherwise>

                                            </c:choose>

                                        </button>

                                    </form>

                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>

                </table>


                <!-- PAGINATION -->
                <c:if test="${totalPage > 1}">

                    <div class="pagination">

                        <!-- PREV -->
                        <c:if test="${currentPage > 1}">
                            <a href="${pageContext.request.contextPath}/ViewCategoryList?page=${currentPage-1}&search=${currentSearch}&status=${currentStatus}">
                                Prev
                            </a>
                        </c:if>


                        <!-- PAGE NUMBERS -->
                        <c:forEach begin="1" end="${totalPage}" var="i">

                            <a class="${i == currentPage ? 'active-page' : ''}"
                               href="${pageContext.request.contextPath}/ViewCategoryList?page=${i}&search=${currentSearch}&status=${currentStatus}">
                                ${i}
                            </a>

                        </c:forEach>


                        <!-- NEXT -->
                        <c:if test="${currentPage < totalPage}">
                            <a href="${pageContext.request.contextPath}/ViewCategoryList?page=${currentPage+1}&search=${currentSearch}&status=${currentStatus}">
                                Next
                            </a>
                        </c:if>

                    </div>

                </c:if>


            </div>
            </div>

        </div>

    </div>

</div>

</body>
</html>