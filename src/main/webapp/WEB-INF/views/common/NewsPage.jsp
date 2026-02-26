<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tin tức & Sự kiện</title>
        <link href="css/NewStyle.css" rel="stylesheet" type="text/css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <jsp:include page="headerSelection.jsp"/>
        <section class="news-wrapper">
            <div class="news-container">
                <!-- ================= SIDEBAR ================= -->
                <aside class="sidebar">
                    <h3>Bài Viết Mới Nhất</h3>

                    <c:forEach var="l" items="${LastNews}">
                        <div class="sidebar-item">
                            <img src="${l.img}" alt="">
                            <div class="sidebar-content">
                                <a href="ViewNewsDetail?id=${l.newID}">
                                    ${l.title}
                                </a>
                            </div>
                        </div>
                    </c:forEach>

                </aside>


                <div class="main-content">

                    <!-- SEARCH -->
                    <div class="search-box">
                        <form action="new" method="post">
                            <input type="text" 
                                   name="keyword" 
                                   value="${param.keyword}" 
                                   placeholder="Tìm kiếm bài viết...">
                            <button type="submit">Tìm</button>
                        </form>
                    </div>


                    <!-- BLOG GRID -->
                    <div class="blog-grid">

                        <c:forEach var="n" items="${NewsList}">
                            <div class="blog-card">

                                <a href="ViewNewsDetail?id=${n.newID}">
                                    <img src="${n.img}" alt="">
                                </a>
                                <div class="blog-body">
                                    <h4>${n.title}</h4>

                                    <p>
                                        ${n.description}
                                    </p>

                                    <a href="ViewNewsDetail?id=${n.newID}" class="read-more">
                                        More →
                                    </a>
                                </div>

                            </div>
                        </c:forEach>

                    </div>

                </div>

            </div>
            <div class="pagination">
                <c:forEach begin="1" end="${totalPage}" var="i">
                    <a href="new?page=${i}" 
                       class="${i == currentPage ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>
            </div>
        </div>

    </section>

    <jsp:include page="/WEB-INF/views/common/footer_1.jsp" />

</body>
</html>