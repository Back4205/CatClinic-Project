<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${news.title}</title>
    <link rel="stylesheet" href="css/news-detail.css">
    <link href="css/NewsDetailStyle.css" rel="stylesheet" type="text/css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>
        <jsp:include page="headerSelection.jsp"/>

<!-- ===== Banner Image ===== -->
<section class="news-banner">
    <img src="${news.img}" alt="${news.title}">
</section>

<!-- ===== Content ===== -->
<section class="news-container">


    <!-- Title -->
    <h1 class="news-title">
        ${news.title}
    </h1>

    <div class="title-line"></div>

    <!-- Content -->
    <div class="news-content">
        ${news.description}
    </div>

</section>


        <jsp:include page="footer_1.jsp"/>


</body>
</html>