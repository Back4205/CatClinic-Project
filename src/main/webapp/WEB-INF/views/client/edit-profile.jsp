<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Edit Profile | CatClinic</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/edit-profile.css">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    </head>    <body>

        <%@include file="header.jsp" %>

        <div class="container">
            <c:set var="activePage" value="profile" />
            <%@include file="sidebar.jsp" %>
            <main class="content">
                <section class="card">
                    <h2>Edit Profile</h2>

                    <c:if test="${not empty message}">
                        <div style="padding: 15px; margin-bottom: 25px; border-radius: 10px; font-weight: bold; text-align: center;
                             ${messageType == 'success' ? 'background: #dcfce7; color: #166534; border: 1px solid #bbf7d0;' : 'background: #fee2e2; color: #991b1b; border: 1px solid #fecaca;'}">
                            ${message}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/edit" method="post">

                        <div class="form-grid">
                            <div class="field">
                                <label>Full Name</label>
                                <input type="text" name="userName" value="${user.userName}" required>
                            </div>
                            <div class="field">
                                <label>Phone Number</label>
                                <input type="text" name="phone" value="${user.phone}">
                            </div>
                            <div class="field full">
                                <label>Email Address</label>
                                <input type="email" name="email" value="${user.email}" required>
                            </div>
                            <div class="field full">
                                <label>Home Address</label>
                                <input type="text" name="address" value="${user.address}">
                            </div>
                        </div>

                        <div class="buttons">
                            <button type="submit" class="btn primary">
                                ${messageType == 'success' ? 'Update Again' : 'Save Changes'}
                            </button>

                            <a href="profile" class="btn">
                                <c:choose>
                                    <c:when test="${messageType == 'success'}">
                                        <i class="bi bi-arrow-left"></i> Back to Profile
                                    </c:when>
                                    <c:otherwise>
                                        Cancel
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </div>
                    </form>
                </section>
            </main>

        </div>
        <footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
            <div class="footer-content">
                &copy; 2026 CatClinic. All rights reserved.
            </div>
        </footer>                
    </body>
</html>