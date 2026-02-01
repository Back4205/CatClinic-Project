<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>User Authorization</title>
        <link href="css/authorStyle.css" rel="stylesheet"/>
    </head>
    <body>

        <div class="container">

            <div class="page-header">
                <a href="account" class="back-link">← Back to Account List</a>
            </div>

            <div class="card">

                <div class="card-header">
                    <h2>USER AUTHORIZATION</h2>
                    <span class="user-info">${userName}</span>
                </div>

                <!-- MESSAGE -->
                <c:if test="${not empty sessionScope.error}">
                    <p class="msg error">${sessionScope.error}</p>
                </c:if>

                <c:if test="${not empty sessionScope.success}">
                    <p class="msg success">${sessionScope.success}</p>
                </c:if>

                <!-- FORM -->
                <form action="author" method="post">
                    <input type="hidden" name="userID" value="${userID}">
                    <p class="section-title">SELECT ROLE</p>

                    <div class="role-grid">
                        <label><input type="radio" name="role" value="Admin"> Admin</label>
                        <label><input type="radio" name="role" value="Veterinarian"> Veterinarian</label>
                        <label><input type="radio" name="role" value="Receptionist"> Receptionist</label>
                        <label><input type="radio" name="role" value="Staff"> Staff</label>
                        <label><input type="radio" name="role" value="Customer"> Customer</label>
                    </div>

                    <div class="impact-box">
                        <strong>Impact</strong>
                        <p>Role change will update permissions immediately.</p>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn submit">Update Role</button>
                    </div>
                </form>

            </div>
        </div>

    </body>
</html>
