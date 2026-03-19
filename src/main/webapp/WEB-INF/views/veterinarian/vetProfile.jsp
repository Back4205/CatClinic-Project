<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
<head>
    <title>Veterinarian Profile</title>
<!-- GLOBAL CSS -->
<link rel="stylesheet" href="css/DashboardVeteStyle.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/headerVeteStyle.css">

<!-- PAGE CSS -->
<link rel="stylesheet" href="css/vetprofile.css">

<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
</head>

<body>

<div class="layout">
<jsp:include page="sidebar.jsp"/>

<main class="main">

    <jsp:include page="header.jsp"/>

    <div class="dashboard-content">


        <!-- ===== PROFILE (TOP) ===== -->
        <div class="card profile-card">

            <div class="card-header">
                <h3>Profile Information</h3>

                <a href="EditVeterinarianProfile" class="btn-edit">
                    <i class="fa fa-pen"></i> Edit
                </a>
            </div>

            <!-- MESSAGE -->
            <c:if test="${not empty message}">
                <p class="${messageType == 'success' ? 'msg-success' : 'msg-error'}">
                    ${message}
                </p>
            </c:if>

            <div class="profile-grid">

                <div class="item">
                    <label>FULL NAME</label>
                    <p>${vet.fullName}</p>
                </div>

                <div class="item">
                    <label>EMAIL</label>
                    <p>${vet.email}</p>
                </div>

                <div class="item">
                    <label>PHONE</label>
                    <p>${vet.phone}</p>
                </div>

                <div class="item">
                    <label>GENDER</label>
                    <p>
                        <c:choose>
                            <c:when test="${vet.male}">Male</c:when>
                            <c:otherwise>Female</c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <div class="item">
                    <label>DEGREE</label>
                    <p>${vet.degree}</p>
                </div>

                <div class="item">
                    <label>EXPERIENCE</label>
                    <p>${vet.experienceYear} Years</p>
                </div>

                <div class="item full">
                    <label>BIO</label>
                    <p>${vet.bio}</p>
                </div>

            </div>

        </div>

        <!-- ===== CHANGE PASSWORD (BOTTOM) ===== -->
        <div class="card password-card">

            <div class="card-header">
                <h3>Change Password</h3>
            </div>

            <form action="VetProfile" method="post" class="password-form">

                <input type="hidden" name="action" value="changePassword">

                <div class="form-row">
                    <div class="form-group">
                        <label>Current Password</label>
                        <input type="password" name="oldPass" required>
                    </div>

                    <div class="form-group">
                        <label>New Password</label>
                        <input type="password" name="newPass" required>
                    </div>

                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input type="password" name="confirmPass" required>
                    </div>
                </div>

                <button type="submit" class="btn-save">
                    <i class="fa fa-key"></i> Change Password
                </button>

            </form>

        </div>

    </div>

</main>
</div>

</body>
</html>
