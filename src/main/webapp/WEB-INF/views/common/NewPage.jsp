<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tin tức & Sự kiện</title>

    <!-- ===== GLOBAL CSS ===== -->
    <link href="css/all.css" rel="stylesheet"/>
    <link href="css/header.css" rel="stylesheet"/>
    <link href="css/footerx.css" rel="stylesheet"/>

    <!-- ===== PAGE CSS ===== -->
    <link href="css/NewStyle.css" rel="stylesheet"/>
    <link href="css/news.css" rel="stylesheet"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>

<jsp:include page="headerSelection.jsp"/>

<!-- ================= MAIN CONTENT ================= -->
<div class="container">
    <div class="news-layout">

        <!-- ========== SIDEBAR ========== -->
        <aside class="sidebar">
            <h3>Bài Viết Mới Nhất</h3>

            <!-- Static demo - sau này thay bằng JSTL -->
            <div class="sidebar-item">
                <img src="https://picsum.photos/100/100?1" alt="">
                <div>
                    <div class="date">08/09/2025</div>
                    <div class="title">Khoá đào tạo nâng cao về chấn thương...</div>
                </div>
            </div>

            <div class="sidebar-item">
                <img src="https://picsum.photos/100/100?2" alt="">
                <div>
                    <div class="date">08/09/2025</div>
                    <div class="title">Tổng kết khoá đào tạo chỉnh hình...</div>
                </div>
            </div>
        </aside>

        <!-- ========== MAIN CONTENT ========== -->
        <main>

            <!-- TAG FILTER -->
            <div class="tag-list">
                <div class="tag active" data-filter="all">Tất cả</div>
                <div class="tag" data-filter="cho">Bệnh của chó</div>
                <div class="tag" data-filter="meo">Bệnh của mèo</div>
                <div class="tag" data-filter="huanluyen">Huấn luyện thú cưng</div>
                <div class="tag" data-filter="thuoc">Thuốc cho thú cưng</div>
            </div>

            <!-- BLOG GRID -->
            <div class="blog-grid">

                <div class="blog-card" data-category="cho">
                    <img src="https://picsum.photos/400/250?3" alt="">
                    <div class="content">
                        <div class="date">08/09/2025</div>
                        <h4>Khoá đào tạo nâng cao về chấn thương chỉnh hình</h4>
                        <p>Tổng quan về bệnh lệch xương bánh chè trong thú nhỏ...</p>
                    </div>
                </div>

                <div class="blog-card" data-category="meo">
                    <img src="https://picsum.photos/400/250?4" alt="">
                    <div class="content">
                        <div class="date">08/09/2025</div>
                        <h4>Thúc đẩy hợp tác về Một sức khoẻ</h4>
                        <p>Dr. Alain tới thăm và chia sẻ tại học viện GAIA...</p>
                    </div>
                </div>

                <div class="blog-card" data-category="huanluyen">
                    <img src="https://picsum.photos/400/250?5" alt="">
                    <div class="content">
                        <div class="date">08/09/2025</div>
                        <h4>Huấn luyện chó cơ bản tại nhà</h4>
                        <p>Các phương pháp huấn luyện hiệu quả cho thú cưng...</p>
                    </div>
                </div>

            </div>
        </main>
    </div>

    <!-- ========== FAQ ========== -->
    <section class="faq-section">
        <h2>Các Câu Hỏi Thường Gặp</h2>

        <div class="faq-item active">
            <div class="faq-question">
                1. GAIA cung cấp những dịch vụ gì?
                <span>-</span>
            </div>
            <div class="faq-answer">
                GAIA là hệ sinh thái chăm sóc thú cưng toàn diện: phòng khám thú y,
                phẫu thuật, khách sạn & spa thú cưng, cửa hàng thú cưng,
                vận chuyển quốc tế và GAIA Academy đào tạo nhân sự thú y.
            </div>
        </div>

        <div class="faq-item">
            <div class="faq-question">
                2. Khách sạn thú cưng của GAIA có gì khác biệt?
                <span>+</span>
            </div>
            <div class="faq-answer">
                Không gian riêng biệt, chăm sóc 24/7, camera theo dõi và đội ngũ thú y giám sát.
            </div>
        </div>

        <div class="faq-item">
            <div class="faq-question">
                3. GAIA có dịch vụ vận chuyển thú cưng quốc tế không?
                <span>+</span>
            </div>
            <div class="faq-answer">
                GAIA hỗ trợ vận chuyển thú cưng quốc tế trọn gói theo tiêu chuẩn hàng không.
            </div>
        </div>
    </section>
</div>

<div class="float-buttons">
    <button id="scrollTopBtn">↑</button>
</div>

<jsp:include page="/WEB-INF/views/common/footer_1.jsp" />

<script>
    // ===== TAG FILTER =====
    const tags = document.querySelectorAll('.tag');
    const cards = document.querySelectorAll('.blog-card');

    tags.forEach(tag => {
        tag.addEventListener('click', () => {
            tags.forEach(t => t.classList.remove('active'));
            tag.classList.add('active');

            const filter = tag.dataset.filter;

            cards.forEach(card => {
                if (filter === 'all' || card.dataset.category === filter) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        });
    });

    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(item => {
        const question = item.querySelector('.faq-question');

        question.addEventListener('click', () => {
            faqItems.forEach(i => {
                if (i !== item) i.classList.remove('active');
            });

            item.classList.toggle('active');

            const icon = item.querySelector('span');
            icon.textContent = item.classList.contains('active') ? '-' : '+';
        });
    });

    // ===== SCROLL TOP =====
    document.getElementById('scrollTopBtn').addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
</script>

</body>
</html>
