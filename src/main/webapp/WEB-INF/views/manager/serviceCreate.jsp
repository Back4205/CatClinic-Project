<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Service</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-service.css">
</head>
<body>

<div class="container">
    <div class="card">
        <h2 class="title">Create New Service</h2>

        <form action="${pageContext.request.contextPath}/CreateService"
              method="post"
              enctype="multipart/form-data">

            <!-- Service Name -->
            <div class="form-group">
                <label>Service Name</label>
                <input type="text" name="name" required>
            </div>

            <!-- Price -->
            <div class="form-group">
                <label>Price (VND)</label>
                <input type="number" name="price" step="0.01" required>
            </div>

            <!-- Time -->
            <div class="form-group">
                <label>Time (minutes)</label>
                <input type="number" name="time" required>
            </div>

            <!-- Description -->
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4"></textarea>
            </div>

            <!-- Category -->
            <div class="form-group">
                <label>Category</label>
                <div class="radio-group">
                    <c:forEach var="c" items="${categoryList}">
                        <label class="radio-item">
                            <input type="radio"
                                   name="categoryID"
                                   value="${c.categoryID}"
                                   required>
                            <span>${c.categoryName}</span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <!-- Active -->
            <div class="form-group checkbox-group">
                <input type="checkbox" name="isActive" id="isActive">
                <label for="isActive">Active</label>
            </div>

            <!-- Image Upload -->
            <div class="form-group">
                <label>Service Image</label>

                <div class="upload-box"
                     onclick="document.getElementById('imageInput').click()">

                    <input type="file"
                           name="image"
                           id="imageInput"
                           accept="image/*"
                           hidden>

                    <div class="upload-content">
                        <p>Click to upload image</p>
                    </div>

                    <img id="previewImage"
                         class="preview-image"/>
                </div>
            </div>

            <!-- Buttons -->
            <div class="button-group">
                <button type="submit" class="btn-submit">
                    Create Service
                </button>

                <a class="btn-cancel"
                   href="${pageContext.request.contextPath}/ViewServiceList?id=${categoryID}">
                    Cancel
                </a>
            </div>

        </form>
    </div>
</div>

<script>
    const input = document.getElementById("imageInput");
    const preview = document.getElementById("previewImage");

    input.addEventListener("change", function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function () {
                preview.src = reader.result;
                preview.style.display = "block";
            };
            reader.readAsDataURL(file);
        }
    });
</script>

</body>
</html>
