<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Category</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-create.css">
</head>

<body>

<div class="container">
    <div class="card">

        <h2 class="title">Create New Category</h2>

        <form action="${pageContext.request.contextPath}/CreateCategory"
              method="post"
              enctype="multipart/form-data"
              id="categoryForm">

            <!-- Category Name -->
            <div class="form-group">
                <label>Category Name</label>
                <input type="text"
                       name="name"
                       value="${param.name}"
                       required>
            </div>

            <!-- Error -->
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>

            <!-- Banner Upload -->
            <div class="form-group">
                <label>Banner</label>

                <div class="upload-box"
                     onclick="document.getElementById('imageInput').click()">

                    <div class="upload-content">
                        <div class="upload-icon">📷</div>
                        <p>Click to upload banner</p>
                    </div>

                    <input type="file"
                           name="image"
                           id="imageInput"
                           accept="image/*"
                           hidden>

                    <img id="previewImage"
                         class="preview-image"/>
                </div>
            </div>

            <!-- Description -->
            <div class="form-group">
                <label>Description</label>
                <textarea name="description"
                          rows="4">${param.description}</textarea>
            </div>

            <!-- Active Checkbox -->
            <div class="form-group checkbox-group">
                <input type="checkbox"
                       name="isActive"
                       id="isActive"
                       <c:if test="${param.isActive != null}">checked</c:if>>

                <label for="isActive">Active</label>
            </div>

            <!-- Buttons -->
            <div class="button-group">

                <button type="submit" class="btn-submit">
                    Create Category
                </button>

                <a href="${pageContext.request.contextPath}/ViewCategoryList"
                   class="btn-cancel">
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