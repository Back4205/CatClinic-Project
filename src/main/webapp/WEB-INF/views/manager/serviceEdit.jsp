<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Service</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #ffffff;
            color: #333;
            padding: 40px;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            border: 1px solid #eee;
            border-radius: 10px;
            padding: 28px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }

        h2 {
            margin-bottom: 24px;
        }

        .form-group {
            margin-bottom: 16px;
        }

        label {
            display: block;
            font-weight: 600;
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
        }

        textarea {
            resize: vertical;
            min-height: 80px;
        }

        .actions {
            margin-top: 24px;
            display: flex;
            gap: 12px;
        }

        .btn {
            padding: 10px 18px;
            border-radius: 6px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            border: none;
        }

        .btn-update {
            background: #f97316;
            color: #fff;
        }

        .btn-update:hover {
            opacity: 0.9;
        }

        .btn-cancel {
            border: 1px solid #f97316;
            background: #fff;
            color: #f97316;
        }

        .btn-cancel:hover {
            background: #f97316;
            color: #fff;
        }

        .not-found {
            color: #999;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Edit Service</h2>

    <c:if test="${service == null}">
        <p class="not-found">Service not found.</p>
    </c:if>

    <c:if test="${service != null}">
        <form action="${pageContext.request.contextPath}/EditService" method="post">

            <!-- GIỮ NGUYÊN -->
            <input type="hidden" name="id" value="${service.serviceID}"/>

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
                <button type="submit" class="btn btn-update">
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