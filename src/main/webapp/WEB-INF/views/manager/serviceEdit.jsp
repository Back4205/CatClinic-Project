<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Service</title>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/service-edit.css">
</head>

<body>

<div class="container">

    <h2>Edit Service</h2>

    <c:if test="${service == null}">
        <p class="not-found">Service not found.</p>

        <a href="${pageContext.request.contextPath}/ViewServiceList"
           class="btn btn-cancel">
            Back to List
        </a>
    </c:if>

    <c:if test="${service != null}">
        <form action="${pageContext.request.contextPath}/EditService"
              method="post">

            <input type="hidden" name="id"
                   value="${service.serviceID}"/>

            <div class="form-group">
                <label>Service Name</label>
                <input type="text" name="name"
                       value="${service.nameService}" required>
            </div>

            <div class="form-group">
                <label>Price</label>
                <input type="number" name="price" step="0.01"
                       value="${service.price}" required>
            </div>

            <div class="form-group">
                <label>Description</label>
                <textarea name="description">${service.description}</textarea>
            </div>

            <div class="form-group">
                <label>Time (minutes)</label>
                <input type="number" name="time"
                       value="${service.timeService}" required>
            </div>

            <div class="actions">
                <button type="submit"
                        class="btn btn-update">
                    Update Service
                </button>

                <a href="${pageContext.request.contextPath}/ViewServiceList"
                   class="btn btn-cancel">
                    Cancel
                </a>
            </div>

        </form>
    </c:if>

</div>

</body>
</html>
