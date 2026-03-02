<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Service Detail</title>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/service-detail.css">
</head>

<body>

<div class="container">

    <h2>Service Detail</h2>
    <c:if test="${service == null}">
        <p class="not-found">Service not found.</p>

        <a class="btn btn-back"
           href="${pageContext.request.contextPath}/ViewServiceList">
            Back to List
        </a>
    </c:if>
    <c:if test="${service != null}">
        <table>
            <tr>
                <th>ID</th>
                <td>${service.serviceID}</td>
            </tr>

            <tr>
                <th>Name</th>
                <td>${service.nameService}</td>
            </tr>

            <tr>
                <th>Price</th>
                <td>${service.price}</td>
            </tr>

            <tr>
                <th>Description</th>
                <td>${service.description}</td>
            </tr>

            <tr>
                <th>Time (minutes)</th>
                <td>${service.timeService}</td>
            </tr>

            <tr>
                <th>Status</th>
                <td>
                    <c:choose>
                        <c:when test="${service.isActive}">
                            <span class="status-active">Active</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-inactive">Inactive</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>

        <div class="actions">
            <a class="btn btn-edit"
               href="${pageContext.request.contextPath}/EditService?id=${service.serviceID}">
                Edit Service
            </a>

            <a class="btn btn-back"
               href="${pageContext.request.contextPath}/ViewServiceList">
                Back to List
            </a>
        </div>
    </c:if>

</div>

</body>
</html>
