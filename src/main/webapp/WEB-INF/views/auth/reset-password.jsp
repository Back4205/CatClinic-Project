<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="${pageContext.request.contextPath}/css/reset-password-style.css" rel="stylesheet">
</head>
<body>
<div class="auth-card">
    <div class="header">
        <div class="icon-shield">
            <i class="bi bi-shield-check"></i>
        </div>
        <h3>Reset Password</h3>
        <p>Please enter a new password for your account.</p>
    </div>

    <c:if test="${not empty errorMess}">
        <div class="alert-error">${errorMess}</div>
    </c:if>

    <form action="reset-password" method="post">
        <input type="hidden" name="token" value="${token}">

        <div class="form-group">
            <label class="form-label">NEW PASSWORD</label>
            <div class="input-wrapper">
                <i class="bi bi-lock input-icon"></i>
                <input type="password" class="form-control" name="newPassword" placeholder="••••••••" required pattern=".{6,}" title="Min 6 characters">
            </div>
        </div>

        <div class="form-group">
            <label class="form-label">CONFIRM PASSWORD</label>
            <div class="input-wrapper">
                <i class="bi bi-lock input-icon"></i>
                <input type="password" class="form-control" name="confirmPassword" placeholder="••••••••" required>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">RESET PASSWORD</button>

        <div class="text-center">
            <a href="login" class="back-link"><i class="bi bi-arrow-left"></i> Back to login</a>
        </div>
    </form>
</div>
</body>
</html>