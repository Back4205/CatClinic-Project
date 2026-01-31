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
    <title>Cat Profile Management</title>
    <script src="${pageContext.request.contextPath}/js/cats/view-avatar-cat.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cat/cat-from.css">
</head>

<body>

<form method="post"
      action="${pageContext.request.contextPath}${empty cat ? '/cats/cat-add' : '/cats/cat-update'}"

      enctype="multipart/form-data">

    <div class="profile-box">
        <div class="profile-image">
            <c:choose>
                <c:when test="${not empty cat.img && not empty cat}">
                    <img id="viewImg" src="${pageContext.request.contextPath}/${cat.img}" alt="Pet Image">
                </c:when>
                <c:otherwise>
                    <img id="viewImg" src="${pageContext.request.contextPath}/img/cats/default.jpg" alt="Default Pet">
                </c:otherwise>
            </c:choose>

            <input class="file-upload-input" type="file" name="image" accept="image/*" onchange="previewImage(this)" >

            <c:if test="${not empty cat}">
                <input type="hidden" name="oldImage" value="${cat.img}">
                <input type="hidden" name="catId" value="${cat.catID}">
            </c:if>
        </div>

        <div class="profile-form">
            <div class="form-name">
                <h2>
                    <c:choose>
                        <c:when test="${empty cat}">Add New Pet</c:when>
                        <c:otherwise>Pet Profile Details</c:otherwise>
                    </c:choose>
                </h2>
            </div>

            <div class="field">
                <label>Owner ID</label>
                <input type="number" name="ownerID" value="${cat.ownerID}"
                ${not empty cat ? 'readonly' : ''} required>
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
                    <div class="note"></div>

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
</body>
</html>
