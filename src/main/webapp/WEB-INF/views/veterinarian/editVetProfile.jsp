<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<html>
<head>
    <title>Edit Profile</title>

<!-- GLOBAL CSS -->
<link rel="stylesheet" href="css/DashboardVeteStyle.css">
<link rel="stylesheet" href="css/sidebar.css">
<link rel="stylesheet" href="css/headerVeteStyle.css">

<!-- PAGE CSS -->
<link rel="stylesheet" href="css/edit-profile.css">

<!-- ICON -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

</head>

<body>

<div class="layout">

<!-- SIDEBAR -->
<jsp:include page="sidebar.jsp"/>

<!-- MAIN -->
<main class="main">

    <!-- HEADER -->
    <jsp:include page="header.jsp"/>

    <div class="dashboard-content">


        <form action="EditVeterinarianProfile" method="post" enctype="multipart/form-data" class="edit-wrapper">

            <!-- ===== AVATAR ===== -->
            <div class="card avatar-card">

                <img src="${vet.image}" alt="Avatar" class="avatar-img" id="previewImage">

                <label class="upload-btn">
                    <i class="fa fa-upload"></i> Upload Image
                    <input type="file" name="image" id="imageInput" accept="image/*">
                </label>

            </div>

            <!-- ===== FORM ===== -->
            <div class="card form-card">

                <div class="card-header">
                    <h3>Profile Information</h3>
                </div>

                <!-- MESSAGE -->
                <c:if test="${not empty error}">
                    <p class="msg-error">${error}</p>
                </c:if>

                <c:if test="${param.success == 1}">
                    <p class="msg-success">Update successful!</p>
                </c:if>

                <div class="form-grid">

                    <div class="form-group">
                        <label>FULL NAME</label>
                        <input type="text" name="fullName" value="${vet.fullName}">
                    </div>

                    <div class="form-group">
                        <label>EMAIL</label>
                        <input type="text" name="email" value="${vet.email}">
                    </div>

                    <div class="form-group">
                        <label>PHONE</label>
                        <input type="text" name="phone" value="${vet.phone}">
                    </div>

                    <div class="form-group">
                        <label>GENDER</label>
                        <select name="male">
                            <option value="true" ${vet.male ? "selected" : ""}>Male</option>
                            <option value="false" ${!vet.male ? "selected" : ""}>Female</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>DEGREE</label>
                        <input type="text" name="degree" value="${vet.degree}">
                    </div>

                    <div class="form-group">
                        <label>EXPERIENCE</label>
                        <input type="number" name="experienceYear" value="${vet.experienceYear}">
                    </div>

                    <div class="form-group full">
                        <label>BIO</label>
                        <textarea name="bio">${vet.bio}</textarea>
                    </div>

                </div>

                <div class="form-actions">
                    <a href="VetProfile" class="btn-cancel">
                        Cancel
                    </a>

                    <button type="submit" class="btn-save">
                        <i class="fa fa-save"></i> Save Profile
                    </button>
                </div>

            </div>

        </form>

    </div>

</main>
</div>

<!-- ===== PREVIEW IMAGE SCRIPT ===== -->

<script>
    const imageInput = document.getElementById("imageInput");
    const previewImage = document.getElementById("previewImage");

    imageInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {

            // Validate file type
            if (!file.type.startsWith("image/")) {
                alert("Please upload an image file!");
                return;
            }

            // Validate size (max 2MB)
            if (file.size > 2 * 1024 * 1024) {
                alert("Image must be smaller than 2MB!");
                return;
            }

            const reader = new FileReader();

            reader.onload = function (e) {
                previewImage.src = e.target.result;
            };

            reader.readAsDataURL(file);
        }
    });
</script>

</body>
</html>
