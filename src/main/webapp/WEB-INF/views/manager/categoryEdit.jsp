<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/category-edit.css">
</head>

<body>

<div class="container">
    <div class="card">
        <h2 class="title">Edit Category</h2>

        <form action="${pageContext.request.contextPath}/EditCategory"
              method="post"
              enctype="multipart/form-data">

            <input type="hidden" name="id"
                   value="${categoryList.categoryID}"/>
            <input type="hidden" name="oldimage"
           value="${categoryList.banner}"/>
            <!-- Category Name -->
            <div class="form-group">
                <label>Category Name</label>
                <input type="text"
                       name="name"
                       value="${categoryList.categoryName}"
                       required>
            </div>

            <!-- Current Image -->
            <div class="form-group">
                <label>Current Banner</label>
                <div class="current-image-box">
                    <img src="${categoryList.banner}"
                         class="current-image">
                </div>
            </div>

            <!-- Change Image -->
            <div class="form-group">
                <label>Change Banner</label>

                <div class="upload-box"
                     onclick="document.getElementById('imageInput').click()">

                    <input type="file"
                           name="image"
                           id="imageInput"
                           accept="image/*"
                           hidden>

                    <div class="upload-content">
                        <p>Click to upload new banner</p>
                    </div>

                    <img id="previewImage"
                         class="preview-image"/>
                </div>
            </div>

            <!-- Description -->
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4">
${categoryList.description}
                </textarea>
            </div>

            <!-- Buttons -->
            <div class="button-group">
                <button type="submit" class="btn-submit">
                    Update Category
                </button>

                <a class="btn-cancel"
                   href="${pageContext.request.contextPath}/ViewCategoryList">
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
