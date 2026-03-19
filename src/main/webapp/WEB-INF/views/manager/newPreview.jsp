<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Preview: ${news.title} | Cat Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/new_preview.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <div class="preview-container">
        <c:choose>
            <c:when test="${not empty news.banner}">
                <img src="${news.banner}" alt="Banner" class="preview-banner">
            </c:when>
            <c:otherwise>
                <div style="height:200px; background:#eee; display:flex; align-items:center; justify-content:center; color:#999;">
                    No Banner Image Provided
                </div>
            </c:otherwise>
        </c:choose>

        <div class="preview-body">
            <div class="meta-info">
                <span><i class="fa-regular fa-calendar-check"></i> Published: ${news.createdDate}</span>
                <c:if test="${news.isActive}">
                    <span style="color: #27ae60;"><i class="fa-solid fa-circle-check"></i> Publicly Visible</span>
                </c:if>
            </div>

            <h1 class="article-title">${news.title}</h1>
            <div class="divider"></div>

            <div class="article-content">
                ${content}
            </div>
        </div>

        <div class="preview-footer">
            <a href="ViewNewList" class="btn-back">
                <i class="fa-solid fa-arrow-left"></i> Back to List
            </a>
            
            <div style="display: flex; gap: 15px;">
                <a href="EditNew?newsId=${news.newsId}" style="text-decoration:none; color:var(--primary-color); font-weight:600; padding:12px;">
                    <i class="fa-solid fa-pen"></i> Edit Again
                </a>
                <a href="ConfirmUpload?id=${news.newsId}" class="btn-publish">
                    <i class="fa-solid fa-rocket"></i> Upload
                </a>
            </div>
        </div>
    </div>

    <p style="text-align:center; color:#95a5a6; font-size:12px; margin-top:20px;">
        This is a preview mode. The layout above matches exactly what users will see on the homepage.
    </p>

</body>
</html>