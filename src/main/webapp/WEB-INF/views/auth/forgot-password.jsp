<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>

    <link href="${pageContext.request.contextPath}/css/forgot-password-style.css" rel="stylesheet">
</head>
<body>
<div class="auth-card">
    <div class="header">
        <h3>Forgot Password</h3>
        <p>Enter your details to receive a reset link.</p>
    </div>

    <c:if test="${not empty errorMess}">
        <div class="alert-error">${errorMess}</div>
    </c:if>
    <c:if test="${not empty successMess}">
        <div class="alert-success">${successMess}</div>
    </c:if>

    <form action="forgot-password" method="post">
        <div class="form-group">
            <label class="form-label">ENTER YOUR EMAIL</label>
            <input type="text" class="form-control" name="email" value="${email}" placeholder="name@example.com" required>
        </div>

        <button type="submit" class="btn btn-primary">SEND LINK</button>

        <div class="divider">OR</div>

        <div class="text-center">
            Remembered your password? <a href="login">Sign In</a>
        </div>
    </form>
</div>
</body>
</html>