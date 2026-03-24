<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${news.title}</title>
<link href="css/footerx.css" rel="stylesheet" type="text/css"/>
    <!-- DÙNG CSS PREVIEW -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/new_preview.css">
<link href="css/header.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link href="css/all.css" rel="stylesheet" type="text/css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>

<jsp:include page="headerSelection.jsp"/>

<!-- ===== GIỐNG PREVIEW ===== -->
<div class="preview-container">

    <!-- Banner -->
    <c:choose>
        <c:when test="${not empty news.banner}">
            <img src="${news.banner}" alt="banner" class="preview-banner">
        </c:when>
        <c:otherwise>
            <div style="height:200px; background:#eee; display:flex; align-items:center; justify-content:center; color:#999;">
                No Banner Image
            </div>
        </c:otherwise>
    </c:choose>

    <div class="preview-body">

        <!-- Meta -->
        <div class="meta-info">
            <span>
                <i class="fa-regular fa-calendar-check"></i>
                ${news.createdDate}
            </span>
        </div>

        <!-- Title -->
        <h1 class="article-title">
            <c:out value="${news.title}" />
        </h1>

        <div class="divider"></div>

        <!-- Content -->
        <div class="article-content">
            ${content}
        </div>

    </div>

</div>
<!-- ===== END ===== -->

<jsp:include page="footer_1.jsp"/>

</body>
</html>