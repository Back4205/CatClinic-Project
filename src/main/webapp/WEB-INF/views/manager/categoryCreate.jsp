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
              enctype="multipart/form-data">

            <div class="form-group">
                <label>Category Name</label>
                <input type="text" name="name" required>
            </div>

            <div class="form-group">
                <label>Banner</label>

                <div class="upload-box" onclick="document.getElementById('imageInput').click()">
                    <input type="file" name="image" id="imageInput" accept="image/*" hidden>
                    <div class="upload-content">
                        <p>Click to upload banner</p>
                    </div>
                    <img id="previewImage" class="preview-image" />
                </div>
            </div>


            <div class="form-group">
                <label>Description</label>
                <textarea name="description" rows="4"></textarea>
            </div>

            <div class="form-group checkbox-group">
                <input type="checkbox" name="isActive" id="isActive">
                <label for="isActive">Active</label>
            </div>

            <div class="button-group">
                <button type="submit" class="btn-submit">
                    Create Category
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
