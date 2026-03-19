<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Article | Cat Clinic Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/new_edit.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<form action="EditNew" method="post" enctype="multipart/form-data" class="container">
    <input type="hidden" name="newsId" value="${news.newsId}">
    <input type="hidden" id="oldBannerUrl" name="oldBanner" value="${news.banner}"/>

    <div class="left-col">
        <div class="card">
            <div class="card-title">
                <i class="fa-regular fa-file-lines"></i> Content Description
            </div>
            <textarea name="description" placeholder="Edit your article content... Use [IMG1], [IMG2] tags for placement.">${news.description}</textarea>
        </div>
    </div>

    <div class="right-col">
        <div class="card">
            <div class="card-title">
                <i class="fa-solid fa-pen-to-square"></i> Title
            </div>
            <input type="text" name="title" value="${news.title}" required>

            <div class="switch-container" style="margin-top: 15px;">
                <div>
                    <div style="font-weight: bold; font-size: 14px;">Active Status</div>
                    <div style="font-size: 12px; color: #888;">Visible to the public</div>
                </div>
                <label class="switch">
                    <input type="checkbox" name="isActive" ${news.isActive ? 'checked' : ''}>
                    <span class="slider"></span>
                </label>
            </div>
        </div>

        <div class="card">
            <div class="card-title">
                <i class="fa-regular fa-image"></i> Banner
            </div>
            <div class="current-media-box">
                <p style="font-size: 12px; color: #888; margin-bottom: 8px;">Current Banner:</p>
                <img id="bannerPreview" src="${not empty news.banner ? news.banner : '#'}" class="current-img-preview" alt="Banner">
            </div>
            <div class="upload-box" onclick="document.getElementById('bannerInput').click();">
                <div style="font-size: 13px; color: var(--primary-color); font-weight: 600;">
                    <i class="fa-solid fa-arrows-rotate"></i> Change Banner
                </div>
                <input type="file" id="bannerInput" name="bannerFile" accept="image/*" style="display: none;">
            </div>
        </div>

        <div class="card">
            <div class="card-title">
                <i class="fa-solid fa-images"></i> Gallery Images
            </div>
            
            <div id="galleryPreviewContainer" class="gallery-grid">
                <c:forEach items="${imageList}" var="img" varStatus="st">
                    <div class="gallery-item-wrapper">
                        <img src="${img.imgUrl}" class="gallery-thumb">
                        <br>
                        <span>[IMG${st.index + 1}]</span>
                    </div>
                </c:forEach>
            </div>

            <div id="dbGalleryBackup" style="display: none;">
                <c:forEach items="${imageList}" var="img" varStatus="st">
                    <div class="gallery-item-wrapper">
                        <img src="${img.imgUrl}" class="gallery-thumb">
                        <br>
                        <span>[IMG${st.index + 1}]</span>
                    </div>
                </c:forEach>
            </div>

            <div class="upload-box" style="margin-top: 15px;" onclick="document.getElementById('galleryInput').click();">
                <div style="font-size: 13px; color: var(--primary-color); font-weight: 600;">
                    <i class="fa-solid fa-upload"></i> Replace All Gallery Images
                </div>
                <input type="file" id="galleryInput" name="images" accept="image/*" multiple style="display: none;">
            </div>
            <p style="color:#d9534f; font-size:11px; margin-top: 10px; text-align: center;">
                * Note: Uploading new files will remove all existing gallery images.
            </p>
        </div>
    </div>

    <div class="actions">
        <a href="ViewNewList" style="text-decoration: none; color: #777; font-weight: 600; margin-top: 12px;">Cancel</a>
        <button type="submit" class="btn-submit">
            <i class="fa-solid fa-floppy-disk"></i> Update
    </div>
</form>

<script>
    // --- RESTORE LOGIC ---
    function restoreOriginals(type) {
        if (type === 'banner') {
            const originalUrl = document.getElementById('oldBannerUrl').value;
            document.getElementById('bannerPreview').src = originalUrl;
        } else if (type === 'gallery') {
            const container = document.getElementById('galleryPreviewContainer');
            const backup = document.getElementById('dbGalleryBackup');
            container.innerHTML = backup.innerHTML;
        }
    }

    // --- BANNER LISTENER ---
    document.getElementById('bannerInput').addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (!file) {
            restoreOriginals('banner'); // Restore if user clicked cancel
            return;
        }
        const reader = new FileReader();
        reader.onload = function(event) {
            document.getElementById('bannerPreview').src = event.target.result;
        }
        reader.readAsDataURL(file);
    });

    // --- GALLERY LISTENER ---
    document.getElementById('galleryInput').addEventListener('change', function(e) {
        const files = e.target.files;
        const container = document.getElementById('galleryPreviewContainer');
        
        if (files.length === 0) {
            restoreOriginals('gallery'); // Restore original DB images if selection is empty
            return;
        }

        container.innerHTML = ''; // Clear to show new previews
        Array.from(files).forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(event) {
                const wrapper = document.createElement('div');
                wrapper.className = 'gallery-item-wrapper';
                wrapper.innerHTML = `
                    <img src="\${event.target.result}" class="gallery-thumb">
                    <br>
                    <span>[IMG\${index + 1}]</span>
                `;
                container.appendChild(wrapper);
            }
            reader.readAsDataURL(file);
        });
    });
</script>

</body>
</html>