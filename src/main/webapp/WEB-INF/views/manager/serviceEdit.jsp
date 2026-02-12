<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Service</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edit-service.css">
</head>
<body>

<div class="container">
    <div class="card">
        <h2 class="title">Edit Service</h2>

        <form action="${pageContext.request.contextPath}/EditService"
              method="post"
              enctype="multipart/form-data">

            <!-- Hidden -->
            <input type="hidden" name="serviceID"
                   value="${service.serviceID}"/>

            <input type="hidden" name="oldImage"
                   value="${service.imgUrl}"/>

            <!-- Name -->
            <div class="form-group">
                <label>Service Name</label>
                <input type="text"
                       name="name"
                       value="${service.nameService}"
                       required>
            </div>

            <!-- Price -->
            <div class="form-group">
                <label>Price (VND)</label>
                <input type="number"
                       name="price"
                       step="0.01"
                       value="${service.price}"
                       required>
            </div>

            <!-- Time -->
            <div class="form-group">
                <label>Time (minutes)</label>
                <input type="number"
                       name="time"
                       value="${service.timeService}"
                       required>
            </div>

            <!-- Description -->
            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4">
${service.description}
                </textarea>
            </div>

            <!-- Category -->
            <div class="form-group">
                <label>Category</label>
                <div class="radio-group">
                    <c:forEach var="c" items="${category}">
                        <label class="radio-item">
                            <input type="radio"
                                   name="categoryID"
                                   value="${c.categoryID}"
                                   <c:if test="${service.categoryID == c.categoryID}">
                                       checked
                                   </c:if>>
                            <span>${c.categoryName}</span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <!-- Current Image -->
            <div class="form-group">
                <label>Current Image</label>
                <div class="current-image-box">
                    <img src="${service.imgUrl}"
                         class="current-image">
                </div>
            </div>

            <!-- Change Image -->
            <div class="form-group">
                <label>Change Image</label>

                <div class="upload-box"
                     onclick="document.getElementById('imageInput').click()">

                    <input type="file"
                           name="image"
                           id="imageInput"
                           accept="image/*"
                           hidden>

                    <div class="upload-content">
                        <p>Click to upload new image</p>
                    </div>

                    <img id="previewImage"
                         class="preview-image"/>
                </div>
            </div>

            <!-- Buttons -->
            <div class="button-group">
                <button type="submit" class="btn-submit">
                    Update Service
                </button>

                <a class="btn-cancel"
                   href="${pageContext.request.contextPath}/ViewServiceList?id=${service.categoryID}">
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
