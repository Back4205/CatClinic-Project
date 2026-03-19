<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>News Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DashboardAdminStyle.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar-admin.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header-admin.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news-list.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>

    <body>
        <div class="admin-layout">
            <jsp:include page="sidebar.jsp"/>

            <div class="admin-main">
                <jsp:include page="header.jsp"/>

                <div class="admin-dashboard">
                    <div class="news-top-bar">
                        <h2 class="news-title-text">News Management</h2>
                        <a href="CreateNew" class="btn-add-news">
                            <i class="fa-solid fa-plus"></i> Add New
                        </a>
                    </div>

                    <form action="ViewNewList" method="get" class="news-toolbar">
                        <input type="text" name="search" placeholder="Search news title..." value="${currentSearch}">

                        <select name="status">
                            <option value="ALL">All Status</option>
                            <option value="Active" <c:if test="${currentStatus=='Active'}">selected</c:if>>Active</option>
                            <option value="Inactive" <c:if test="${currentStatus=='Inactive'}">selected</c:if>>Inactive</option>
                            </select>

                            <button type="submit">Search</button>
                        </form>

                        <table class="news-table">
                            <thead>
                                <tr>
                                    <th class="col-id">ID</th>
                                    <th class="col-banner">Banner</th>
                                    <th class="col-title">Title</th>
                                    <th>Description</th> 
                                    <th class="col-date">Date</th>
                                    <th class="col-status">Status</th>
                                    <th class="col-action">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${newList}" var="n">
                                <tr>
                                    <td>${n.newsId}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty n.banner}">
                                                <img src="${n.banner}" class="news-banner-img">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="no-image-placeholder">No Image</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-bold text-break">${n.title}</td>
                                    <td class="text-break">
                                        <c:out value="${n.description.length() > 80 ? n.description.substring(0,80).concat('...') : n.description}" />
                                    </td>
                                    <td>${n.createdDate}</td>
                                    <td>
                                        <span class="news-status ${n.isActive ? 'active' : 'inactive'}">
                                            ${n.isActive ? 'Active' : 'Inactive'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="news-action-group">
                                            <a href="EditNew?newsId=${n.newsId}" class="btn-news-action edit">
                                                <i class="fa-solid fa-pen"></i> Edit
                                            </a>
                                            <c:choose>
                                                <c:when test="${n.isActive}">
                                                    <a href="ChangeStatus?id=${n.newsId}" class="btn-news-action toggle btn-toggle-hide">
                                                        <i class="fa-solid fa-eye-slash"></i> Hide
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="ChangeStatus?id=${n.newsId}" class="btn-news-action toggle btn-toggle-show">
                                                        <i class="fa-solid fa-eye"></i> Preview
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <c:if test="${totalPage > 1}">
                        <div class="news-pagination">
                            <c:forEach begin="1" end="${totalPage}" var="i">
                                <a href="ViewNewList?page=${i}&search=${currentSearch}&status=${currentStatus}" 
                                   class="${i == currentPage ? 'active-page' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>