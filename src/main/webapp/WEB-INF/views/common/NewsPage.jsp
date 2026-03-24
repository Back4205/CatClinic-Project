<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>News & Events</title>
    <link href="css/NewStyle.css" rel="stylesheet" type="text/css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>

<jsp:include page="headerSelection.jsp"/>

<section class="news-wrapper">
    <div class="news-container">

        <aside class="sidebar">
            <h3>Latest Posts</h3> <c:forEach var="l" items="${top3List}">
                <div class="sidebar-item">
                    <img src="${l.banner}" alt="">
                    <div class="sidebar-content">
                        <a href="ViewNewsDetail?id=${l.newsId}">
                            <c:out value="${l.title}" />
                        </a>
                    </div>
                </div>
            </c:forEach>

        </aside>

        <div class="main-content">

            <div class="search-box">
                <form action="new" method="get">
                    <input type="text" 
                           name="search" 
                           value="${currentSearch}" 
                           placeholder="Search posts..."> <button type="submit">Search</button> </form>
            </div>

            <div class="blog-grid">

                <c:forEach var="n" items="${newList}">
                    <div class="blog-card">

                        <a href="ViewNewsDetail?id=${n.newsId}">
                            <img src="${n.banner}" alt="">
                        </a>

                        <div class="blog-body">
                            <h4><c:out value="${n.title}" /></h4>

                            <small class="date">
                                ${n.createdDate}
                            </small>

                            <p>
                                ${fn:length(n.description) > 100 
                                  ? fn:substring(n.description, 0, 100).concat("...") 
                                  : n.description}
                            </p>

                            <a href="ViewNewsDetail?id=${n.newsId}" class="read-more">
                                Read More → </a>
                        </div>

                    </div>
                </c:forEach>

            </div>

        </div>
    </div>

    <div class="pagination">
        <c:forEach begin="1" end="${totalPage}" var="i">
            <a href="new?page=${i}&search=${currentSearch}" 
               class="${i == currentPage ? 'active' : ''}">
                ${i}
            </a>
        </c:forEach>
    </div>

</section>

<jsp:include page="/WEB-INF/views/common/footer_1.jsp" />

</body>
</html>