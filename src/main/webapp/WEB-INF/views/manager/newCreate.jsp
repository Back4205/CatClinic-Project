<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create News | Cat Clinic Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/new_create.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
    .error-text {
        color: #e53935;
        font-size: 13px;
        margin-top: 8px;
        padding: 6px 10px;
        background-color: #ffeaea;
        border-radius: 6px;
        display: inline-block;
    }

    .input-error {
        border: 1px solid #e53935 !important;
        background-color: #fff5f5;
    }
</style>
</head>
<body>

    <form action="CreateNew" method="post" enctype="multipart/form-data" class="container">

        <div class="left-col">
            <div class="card">
                <div class="card-title">
                    <i class="fa-regular fa-file-lines"></i> Description
                </div>
                <textarea name="description" placeholder="Tip: Insert [IMG1], [IMG2]... where you want the images to appear within the article content."></textarea>
            </div>
        </div>

        <div class="right-col">
            <div class="card">
                <div class="card-title">
                    <i class="fa-solid fa-pen-to-square"></i> Title
                </div>
                <input type="text" name="title" placeholder="Enter article title..." required>
                <c:if test="${not empty error}">
                    <div class="error-text">
                        ${error}
                    </div>
                </c:if>
                <div class="switch-container">
                    <div>
                        <div style="font-weight: bold; font-size: 14px;">Active Status</div>
                        <div style="font-size: 12px; color: #888;">Visible to the public</div>
                    </div>
                <label class="switch">
                    <input type="checkbox" name="isActive" checked>
                    <span class="slider"></span>
                </label>
            </div>
        </div>

        <div class="card">
            <div class="card-title">
                <i class="fa-regular fa-image"></i> Banner
            </div>
            <div class="upload-box" onclick="document.getElementById('bannerInput').click();">
                <div id="bannerText">
                    <div class="upload-icon"><i class="fa-solid fa-cloud-arrow-up"></i></div>
                    <div style="font-weight: 600; font-size: 14px;">Select cover image</div>
                </div>
                <img id="bannerImg" class="banner-preview" src="#" alt="Banner Preview">
                <input type="file" id="bannerInput" name="image" accept="image/*" style="display: none;" >
            </div>
        </div>

        <div class="card">
            <div class="card-title">
                <i class="fa-solid fa-images"></i> Gallery Images
            </div>
            <div class="upload-box" onclick="document.getElementById('galleryInput').click();">
                <div class="upload-icon"><i class="fa-solid fa-plus"></i></div>
                <div style="font-size: 13px; color: #666;">Click to select multiple images</div>
                
                <div id="galleryPreview" class="gallery-preview-container"></div>
                <input type="file" id="galleryInput" name="images" accept="image/*" multiple style="display: none;">
            </div>
            <p style="font-size: 11px; color: #aaa; margin-top: 10px; text-align: center;">
                The first image in the Gallery will be [IMG1], followed by [IMG2], etc.
            </p>
        </div>
    </div>

    <div class="actions">
        <button type="button" class="btn-cancel" onclick="window.location.href='ViewNewList'">Cancel</button>
        <button type="submit" class="btn-submit"><i class="fa-solid fa-check"></i> Create </button>
    </div>

</form>

<script>
    // Preview Banner (Single image)
    document.getElementById('bannerInput').addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(event) {
                const img = document.getElementById('bannerImg');
                img.src = event.target.result;
                img.style.display = 'block';
                document.getElementById('bannerText').style.display = 'none';
            }
            reader.readAsDataURL(file);
        }
    });

    // Preview Gallery (Multiple images)
    document.getElementById('galleryInput').addEventListener('change', function(e) {
        const files = e.target.files;
        const container = document.getElementById('galleryPreview');
        container.innerHTML = ''; // Clear previous previews

        Array.from(files).forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(event) {
                const img = document.createElement('img');
                img.src = event.target.result;
                img.className = 'gallery-item';
                img.title = "IMG" + (index + 1);
                container.appendChild(img);
            }
            reader.readAsDataURL(file);
        });
    });
</script>

</body>
</html>