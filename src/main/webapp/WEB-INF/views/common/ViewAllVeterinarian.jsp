<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Our Veterinarians | Cat Clinic</title>
    <!-- Nạp Font Awesome để các icon hoạt động (thiếu trong file cũ) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Nạp file CSS đúng đường dẫn, giữ nguyên 100% thứ tự bạn cung cấp -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footerx.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/all.css" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ViewAllVeterinarian.css" type="text/css"/>
</head>
<body>
    <!-- Sửa đường dẫn include headerSelection.jsp đúng với cấu trúc thư mục WEB-INF của bạn -->
    <jsp:include page="/WEB-INF/views/common/headerSelection.jsp"/>

    <!-- Hero section giữ nguyên 100% -->
    <section class="hero vet-page-hero">
    <div class="hero-inner">
        <small>OUR EXPERTS</small>
        <h1>Meet Our Professional Veterinarians</h1>
        <p>
            Our team of highly qualified and compassionate veterinarians 
            is dedicated to providing the best possible care.
        </p>
    </div>
</section>

    <!-- Sửa class search-filter-container thành search-box KHỚP VỚI CSS ViewAllVeterinarian.css -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/ViewAllVeterinarian" method="get">
            <input 
                type="text" 
                name="search" 
                placeholder="Tìm tên bác sĩ..." 
                value="${currentSearch}"
            >
            <button type="submit" class="search-btn">
                <i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm
            </button>
        </form>
    </div>

    <div class="vet-container">
        <!-- Xử lý trạng thái trống dữ liệu -->
        <c:if test="${empty vetList}">
            <div class="empty-state">
                <i class="fa-solid fa-user-doctor" style="font-size: 48px; color:#ccc; margin-bottom:15px;"></i>
                <p>Không tìm thấy bác sĩ nào phù hợp với từ khóa tìm kiếm</p>
                <a href="${pageContext.request.contextPath}/ViewAllVeterinarian" class="btn-reset">Xem tất cả bác sĩ</a>
            </div>
        </c:if>

        <!-- Hiển thị danh sách bác sĩ giữ nguyên cấu trúc cũ -->
        <c:forEach var="v" items="${vetList}">
            <div class="vet-card">
                <div class="img-wrapper">
                    <img class="vet-img"
                         src="${v.image}"
                         alt="Doctor ${v.fullName}">
                    <div class="experience-badge">
                        ⭐ ${v.experienceYear} Years Exp.
                    </div>
                </div>
                <div class="card-content">
                    <div class="vet-name">
                        ${v.fullName}
                    </div>
                    <div class="degree">
                        ${v.degree}
                    </div>
                    <a href="${pageContext.request.contextPath}/ViewVeterianarianDetail?id=${v.vetID}" class="btn-profile">
                        View Profile
                    </a>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Phân trang giữ nguyên logic, class đã khớp với CSS -->
    <c:if test="${totalPage > 1}">
        <div class="pagination-container">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/ViewAllVeterinarian?page=${currentPage-1}&search=${currentSearch}" class="page-btn prev-btn">
                    <i class="fa-solid fa-chevron-left"></i> Trang trước
                </a>
            </c:if>

            <div class="page-numbers">
                <c:forEach begin="1" end="${totalPage}" var="i">
                    <a 
                        href="${pageContext.request.contextPath}/ViewAllVeterinarian?page=${i}&search=${currentSearch}" 
                        class="page-number ${i == currentPage ? 'active' : ''}"
                    >
                        ${i}
                    </a>
                </c:forEach>
            </div>

            <c:if test="${currentPage < totalPage}">
                <a href="${pageContext.request.contextPath}/ViewAllVeterinarian?page=${currentPage+1}&search=${currentSearch}" class="page-btn next-btn">
                    Trang sau <i class="fa-solid fa-chevron-right"></i>
                </a>
            </c:if>
        </div>
    </c:if>

    <!-- Sửa đường dẫn include footer đúng với cấu trúc thư mục -->
    <jsp:include page="/WEB-INF/views/common/footer_1.jsp"/>
</body>
</html>