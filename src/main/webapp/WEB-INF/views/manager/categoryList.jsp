<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Service List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-list.css">
</head>
<body>

<div class="container">
    <div class="header">
    <h2 class="title">Category List</h2>

    <a class="btn-primary"
       href="${pageContext.request.contextPath}/CreateCategory">
        + Add New Category
    </a>
</div>

    <table class="styled-table">
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
        <c:forEach var="s" items="${categoryList}">
            <tr>
                <td>${s.categoryID}</td>
                <td>${s.categoryName}</td>
                <td>
                    <img class="banner-img" src="${s.banner}">
                </td>
                <td>${s.description}</td>
                <td>
                    <span class="${s.active ? 'status-active' : 'status-inactive'}">
                        ${s.active ? 'Active' : 'Inactive'}
                    </span>
                </td>
                <td class="action-buttons">
                    <a class="btn-view"
                       href="${pageContext.request.contextPath}/ViewServiceList?id=${s.categoryID}">
                        View Service
                    </a>

                    <a class="btn-edit"
                       href="${pageContext.request.contextPath}/EditCategory?id=${s.categoryID}">
                        Edit
                    </a>

                    <form action="${pageContext.request.contextPath}/HideCategory"
                          method="post"
                          onsubmit="return confirmAction(this)">
                        <input type="hidden" name="id" value="${s.categoryID}">
                        <input type="hidden" name="action"
                               value="${s.active ? 'hide' : 'show'}">

                        <button type="submit"
                                class="${s.active ? 'btn-hide' : 'btn-show'}">
                            ${s.active ? 'Hide' : 'Show'}
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script src="${pageContext.request.contextPath}/js/service.js"></script>
</body>
</html>
