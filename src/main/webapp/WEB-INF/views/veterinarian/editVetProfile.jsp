<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Veterinarian Profile</title>

    <link rel="stylesheet" href="css/DashboardVeteStyle.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/headerVeteStyle.css">

    <link rel="stylesheet" href="css/edit-profile.css">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">

    <style>
        /* Style bổ sung cho thông báo */
        .msg-error {
            color: #721c24;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
            display: flex;
            align-items: center;
        }
        .msg-error::before {
            content: "\f06a"; /* FontAwesome Exclamation Circle */
            font-family: "Font Awesome 6 Free";
            font-weight: 900;
            margin-right: 10px;
        }
        .msg-success {
            color: #155724;
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .form-group label {
            font-weight: bold;
            font-size: 12px;
            color: #555;
            margin-bottom: 5px;
            display: block;
        }
        /* Hiệu ứng focus cho input khi có lỗi (tùy chọn) */
        .input-error {
            border: 1px solid #dc3545 !important;
        }
    </style>
</head>

<body>

<div class="layout">

    <jsp:include page="sidebar.jsp"/>

    <main class="main">

        <jsp:include page="header.jsp"/>

        <div class="dashboard-content">

            <form action="EditVeterinarianProfile" method="post" enctype="multipart/form-data" class="edit-wrapper">

                <div class="card avatar-card">
                    <img src="${not empty vet.image ? vet.image : 'images/default-avatar.png'}" 
                         alt="Avatar" class="avatar-img" id="previewImage">

                    <label class="upload-btn">
                        <i class="fa fa-upload"></i> Upload Photo
                        <input type="file" name="image" id="imageInput" accept="image/*">
                    </label>
                    <p style="font-size: 11px; color: #888; margin-top: 10px;">Max size: 2MB (JPG, PNG)</p>
                </div>

                <div class="card form-card">

                    <div class="card-header">
                        <h3><i class="fa-solid fa-user-doctor"></i> Profile Information</h3>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="msg-error">
                            ${error}
                        </div>
                    </c:if>

                    <c:if test="${param.success == 1}">
                        <div class="msg-success">
                            <i class="fa fa-check-circle"></i> Your profile has been updated successfully!
                        </div>
                    </c:if>

                    <div class="form-grid">

                        <div class="form-group">
                            <label>FULL NAME</label>
                            <input type="text" name="fullName" value="${vet.fullName}" placeholder="Enter your full name" required>
                        </div>

                        <div class="form-group">
                            <label>EMAIL</label>
                            <input type="email" name="email" value="${vet.email}" placeholder="example@clinic.com" required>
                        </div>

                        <div class="form-group">
                            <label>PHONE</label>
                            <input type="text" name="phone" value="${vet.phone}" placeholder="0123456789">
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
                            <input type="text" name="degree" value="${vet.degree}" placeholder="e.g. Master of Veterinary">
                        </div>

                        <div class="form-group">
                            <label>EXPERIENCE (YEARS)</label>
                            <input type="number" name="experienceYear" value="${vet.experienceYear}" min="0">
                        </div>

                        <div class="form-group full">
                            <label>BIO / INTRODUCTION</label>
                            <textarea name="bio" rows="4" placeholder="Tell us about your professional background...">${vet.bio}</textarea>
                        </div>

                    </div>

                    <div class="form-actions">
                        <a href="VetProfile" class="btn-cancel">
                            Cancel
                        </a>

                        <button type="submit" class="btn-save">
                            <i class="fa fa-save"></i> Save Changes
                        </button>
                    </div>

                </div>

            </form>

        </div>

    </main>
</div>

<script>
    const imageInput = document.getElementById("imageInput");
    const previewImage = document.getElementById("previewImage");

    imageInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            // 1. Validate file type (Client-side)
            if (!file.type.startsWith("image/")) {
                alert("Please select an image file (jpg, png, etc.)");
                this.value = "";
                return;
            }

            // 2. Validate size (Max 2MB)
            if (file.size > 2 * 1024 * 1024) {
                alert("Image is too large! Please select a file under 2MB.");
                this.value = "";
                return;
            }

            // 3. Show Preview
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