<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 2/28/2026
  Time: 10:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8f9fa;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .error-box {
      background: white;
      padding: 40px;
      border-radius: 10px;
      box-shadow: 0 0 20px rgba(0,0,0,0.1);
      text-align: center;
      width: 400px;
    }

    .error-box h2 {
      color: #dc3545;
      margin-bottom: 20px;
    }

    .error-message {
      margin-bottom: 25px;
      font-size: 16px;
      color: #555;
    }

    .btn {
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      text-decoration: none;
      border-radius: 5px;
    }

    .btn:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<div class="error-box">
  <h2>⚠ Có lỗi xảy ra</h2>

  <div class="error-message">
    <c:choose>
      <c:when test="${not empty param.msg}">
        ${param.msg}
      </c:when>
      <c:otherwise>
        Đã xảy ra lỗi trong quá trình xử lý.
      </c:otherwise>
    </c:choose>
  </div>

  <a href="${pageContext.request.contextPath}/home" class="btn">
    Quay về trang chủ
  </a>
</div>

</body>
</html>