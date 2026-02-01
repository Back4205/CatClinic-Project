<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Service Detail</title>

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
            color: #111;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            text-align: left;
            width: 35%;
            background: #f97316;
            color: #fff;
            padding: 10px;
            font-weight: 600;
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }

        tr:last-child td {
            border-bottom: none;
        }

        .status-active {
            color: green;
            font-weight: 600;
        }

        .status-inactive {
            color: #999;
            font-weight: 600;
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
            display: inline-flex;
            align-items: center;
        }

        .btn-edit {
            background: #f97316;
            color: white;
        }

        .btn-edit:hover {
            opacity: 0.9;
        }

        .btn-back {
            border: 1px solid #f97316;
            color: #f97316;
        }

        .btn-back:hover {
            background: #f97316;
            color: white;
        }

        .not-found {
            color: #999;
            font-size: 16px;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Service Detail</h2>

    <c:if test="${service == null}">
        <p class="not-found">Service not found.</p>
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

        <!-- ACTIONS -->
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
