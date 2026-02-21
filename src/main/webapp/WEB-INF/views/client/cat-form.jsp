<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/24/2026
  Time: 4:09 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cat Profile | CatClinic</title>

    <!-- Layout CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/clientcss/sidebar.css">

    <!-- Page CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/cat-from.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <script src="${pageContext.request.contextPath}/js/cats/view-avatar-cat.js"></script>
</head>

<body>

<%@include file="header.jsp" %>

<div class="container">

    <c:set var="activePage" value="cats" />
    <%@include file="sidebar.jsp" %>

    <main class="content">

        <div class="page-header">
            <div class="page-title">
                <h2>
                    <c:choose>
                        <c:when test="${empty cat}">ADD NEW CAT</c:when>
                        <c:otherwise>CAT PROFILE DETAILS</c:otherwise>
                    </c:choose>
                </h2>
                <p>Manage cat profile information.</p>
            </div>
        </div>

        <div class="form-wrapper">

            <form method="post"
                  action="${pageContext.request.contextPath}${empty cat ? '/cats/cat-add' : '/cats/cat-update'}"
                  enctype="multipart/form-data">
                <input type="hidden" name="from" value="${from}">
                <div class="profile-box">

                    <!-- IMAGE -->
                    <div class="profile-image">
                        <c:choose>
                            <c:when test="${not empty cat.img && not empty cat}">
                                <img id="viewImg" src="${pageContext.request.contextPath}/${cat.img}" alt="Pet Image">
                            </c:when>
                            <c:otherwise>
                                <img id="viewImg" src="${pageContext.request.contextPath}/image/cats/default.jpg" alt="Default Pet">
                            </c:otherwise>
                        </c:choose>

                        <input type="file" name="image" accept="image/*" onchange="previewImage(this)">

                        <c:if test="${not empty cat}">
                            <input type="hidden" name="oldImage" value="${cat.img}">
                            <input type="hidden" name="catId" value="${cat.catID}">
                        </c:if>
                    </div>

                    <!-- FORM -->
                    <div class="profile-form">

                        <div class="field">
                            <label>Owner</label>
                            <input type="text" name="ownerID" value="${acc.fullName}" readonly>
                        </div>

                        <div class="field">
                            <label>Pet Name</label>
                            <input type="text" name="name" value="${cat.name}" required>
                        </div>

                        <div class="field">
                            <label>Breed</label>
                            <input type="text" name="breed" value="${cat.breed}"
                            ${not empty cat ? 'readonly' : ''} required>
                        </div>

                        <div class="row">
                            <div class="field">
                                <label>Age</label>
                                <input type="number" name="age" value="${cat.age}" min="0" required>
                                <div class="note">Set 0 if under 1 year.</div>
                            </div>

                            <div class="field">
                                <label>Gender</label>
                                <select name="gender" ${not empty cat ? 'disabled' : ''}>
                                    <option value="0" ${cat.gender == 0 ? "selected" : ""}>Male</option>
                                    <option value="1" ${cat.gender == 1 ? "selected" : ""}>Female</option>
                                </select>

                                <c:if test="${not empty cat}">
                                    <input type="hidden" name="gender" value="${cat.gender}">
                                </c:if>
                            </div>
                        </div>

                        <div class="btn-group">
                            <button class="btn btn-primary" type="submit">
                                <c:out value="${empty cat ? 'ADD PET' : 'UPDATE PROFILE CAT'}" />
                            </button>
                            <a href="${pageContext.request.contextPath}/cats" class="btn btn-cancel">CANCEL</a>
                        </div>

                        <c:if test="${not empty message}">
                            <div class="message">${message}</div>
                        </c:if>

                    </div>
                </div>
            </form>

        </div>

    </main>
</div>

<footer style="background: #ffffff; border-top: 1px solid #e5e7eb; padding: 25px 0; text-align: center; color: #64748b; font-size: 14px; margin-top: auto;">
    <div class="footer-content">
        &copy; 2026 CatClinic. All rights reserved.
    </div>
</footer>

</body>
</html>