<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>CatClinic - Professional Cat Care</title>
        <link href="css/all.css" rel="stylesheet" type="text/css"/>
        <link href="css/herox.css" rel="stylesheet" type="text/css"/>
        <link href="css/footerx.css" rel="stylesheet" type="text/css"/>
        <link href="css/headerx.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700;800&family=Fredoka:wght@400;600;700&display=swap" rel="stylesheet">
    </head>
    <body>

        <!-- HEADER -->
        <jsp:include page="headerSelection.jsp"/>

        <!-- HERO SECTION -->
        <section class="hero">
            <div class="hero-bg-circle"></div>
            <div class="hero-bg-circle-2"></div>

            <div class="hero-text">
                <span class="badge">
                    <i class="fas fa-paw"></i> Welcome to CatClinic
                </span>

                <h1>
                    Professional Care <br>
                    for Your <span class="gradient-text">Lovely Cats</span>
                </h1>

                <p>
                    Manage appointments, medical records, and cat healthcare easily.
                    A specialized, feline-friendly environment designed just for them.
                </p>
                <div class="buttons">
                    <a href="${pageContext.request.contextPath}/Booking" class="btn-primary">
                        <i class="fa-solid fa-calendar"></i> Book Appointment
                    </a>
                </div>
            </div>
            <div class="hero-image">
                <div class="image-card">
                    <img 
                        src="https://res.cloudinary.com/dydnbzspg/image/upload/v1773934252/download_1_ldtuvx.jpg"
                        alt="Cat Clinic Service"
                        loading="lazy"
                    />
                    <div class="image-badge">
                        <i class="fas fa-star"></i> Professional Vets
                    </div>
                </div>
            </div>
        </section>

        <!-- SERVICES SECTION -->
        <section class="services">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">
                        <span class="title-icon">🐱</span>
                        Cat Clinic's Services
                    </h2>
                    <div class="line"></div>
                    <p class="section-subtitle">Comprehensive care for your beloved feline friends</p>
                </div>

                <div class="service-wrapper">
                    <div class="service-container">
                        <c:forEach var="s" items="${listService}" begin="0" end="5">
                            <a href="service?id=${s.serviceID}" class="service-link">
                                <div class="service-item">
                                    <!-- Decoration elements -->
                                    <div class="service-decoration">
                                        <div class="deco-circle deco-1"></div>
                                        <div class="deco-circle deco-2"></div>
                                        <div class="deco-paw"></div>
                                    </div>

                                    <div class="image-box">
                                        <img src="${s.imgUrl}" alt="${s.nameService}" loading="lazy">
                                        <div class="image-overlay"></div>
                                    </div>

                                    <div class="service-content">
                                        <h3>${s.nameService}</h3>
                                        <div class="service-badge">
                                            <i class="fas fa-heart"></i>
                                        </div>
                                    </div>

                                    <!-- Hover effect element -->
                                    <div class="service-hover-effect"></div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>

                <div class="view-all">
                    <a href="ViewAllService" class="btn-view-all">
                        View All Services
                        <i class="fas fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </section>

        <!-- WHY CHOOSE SECTION -->
        <section class="why-choose">
            <div class="container">
                <div class="section-header">
                    <h2>
                        <span class="big-number">03</span>
                        Reasons to Choose <span class="brand">CatClinic</span>
                    </h2>
                    <div class="underline"></div>
                </div>

                <div class="why-grid">
                    <!-- Item 1 -->
                    <div class="why-item">
                        <div class="why-decoration">
                            <div class="deco-bg"></div>
                        </div>
                        <div class="icon-circle">
                            <i class="fa-solid fa-star"></i>
                        </div>
                        <h3>Japanese experience and background</h3>
                        <p>
                            The CatClinic team is highly trained in Japan and has over 15 years of experience, providing standard and reliable veterinary services.
                        </p>
                    </div>

                    <!-- Item 2 -->
                    <div class="why-item">
                        <div class="why-decoration">
                            <div class="deco-bg"></div>
                        </div>
                        <div class="icon-circle">
                            <i class="fa-solid fa-heart"></i>
                        </div>
                        <h3>Dedicated consultation, transparent pricing</h3>
                        <p>
                            The veterinarian is always there to support the pet owner, clearly explaining each step, and providing a transparent and appropriate treatment plan.
                        </p>
                    </div>

                    <!-- Item 3 -->
                    <div class="why-item">
                        <div class="why-decoration">
                            <div class="deco-bg"></div>
                        </div>
                        <div class="icon-circle">
                            <i class="fa-solid fa-hospital"></i>
                        </div>
                        <h3>Comprehensive and convenient service</h3>
                        <p>
                            CatClinic offers a comprehensive pet care solution: including medical check-ups, vaccinations, spa treatments, pet hotel services, and immigration assistance – all in one system.
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <!-- NEWS SECTION -->
        <section class="news">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">
                        <span class="title-icon">📰</span>
                        Latest News
                    </h2>
                    <div class="line"></div>
                </div>

                <div class="newslist-container">
                    <c:forEach var="n" items="${NewsList}">
                        <div class="blog-card">
                            <div class="blog-image-wrapper">
                                <a href="ViewNewsDetail?id=${n.newID}">
                                    <img src="${n.img}" alt="${n.title}" loading="lazy">
                                </a>
                                <div class="blog-date-badge">
                                    <i class="fas fa-calendar-alt"></i>
                                </div>
                            </div>
                            <div class="blog-body">
                                <h4>${n.title}</h4>
                                <p>${n.description}</p>
                                <a href="ViewNewsDetail?id=${n.newID}" class="read-more">
                                    Read More
                                    <i class="fas fa-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="view-all">
                    <a href="new" class="btn-view-all">
                        View All News
                        <i class="fas fa-arrow-right"></i>
                    </a>
                </div>
            </div>
        </section>

        <!-- FEEDBACK SECTION -->
        <section class="feedback">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">
                        <span class="title-icon">⭐</span>
                        Customer Feedback
                    </h2>
                    <div class="line"></div>
                </div>

                <div class="feedback-container">
                    <c:forEach var="feedback" items="${listfeedback}" varStatus="stat">
                        <c:if test="${stat.index < 3}">
                            <div class="card-feedback">
                                <div class="feedback-decoration">
                                    <div class="deco-quote"></div>
                                </div>

                                <div class="card-header">
                                    <div class="stars">
                                        <c:forEach begin="1" end="5" var="i">
                                            <i class="fas fa-star ${i <= feedback.rating ? 'star-active' : 'star-inactive'}"></i>
                                        </c:forEach>
                                    </div>
                                </div>

                                <div class="feedback-text">
                                    "${feedback.comment}"
                                </div>

                                <div class="user-info">
                                    <div class="avatar">${fn:substring(feedback.fullName, 0, 1)}</div>
                                    <div class="user-details">
                                        <h4>${feedback.fullName}</h4>
                                        <span>${feedback.createdAt}</span>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </section>

        <!-- CONTACT SECTION -->
        <section class="contact-section" id="contact">
            <div class="container contact-wrapper">
                <!-- LEFT -->
                <div class="contact-left">
                    <div class="contact-decoration">
                        <div class="deco-circle-large"></div>
                    </div>

                    <h2>Get In Touch</h2>

                    <p class="contact-desc">
                        Your pet needs care today?
                        Contact CatClinic or leave your information, we'll respond as soon as possible.
                    </p>

                    <div class="contact-social">
                        <a href="#" class="social-icon" title="Facebook">
                            <i class="fa-brands fa-facebook"></i>
                        </a>
                        <a href="#" class="social-icon" title="Message">
                            <i class="fa-solid fa-comment"></i>
                        </a>
                        <a href="#" class="social-icon" title="Phone">
                            <i class="fa-solid fa-phone"></i>
                        </a>
                    </div>

                    <form action="Contact" method="post" class="contact-form" id="contactForm">
                        <div class="form-group">
                            <input type="text" name="fullName" id="fullName" placeholder="Full Name" required>
                            <span class="error" id="errorFullName"></span>
                        </div>

                        <div class="form-group">
                            <input type="email" name="email" id="email" placeholder="Email Address" required>
                            <span class="error" id="errorEmail"></span>
                        </div>

                        <div class="form-group">
                            <input type="tel" name="phone" id="phone" placeholder="Phone Number" required>
                            <span class="error" id="errorPhone"></span>
                        </div>

                        <div class="form-group">
                            <textarea name="message" id="message" placeholder="Your Message" required></textarea>
                            <span class="error" id="errorMessage"></span>
                        </div>

                        <button type="submit" class="btn-submit">
                            <i class="fas fa-paper-plane"></i> Send Message
                        </button>
                    </form>

                    <%
                        String success = (String) session.getAttribute("success");
                        String error = (String) session.getAttribute("error");

                        if (success != null) {
                    %>
                    <div class="alert alert-success">
                        <i class="fas fa-check-circle"></i> <%= success%>
                    </div>
                    <%
                            session.removeAttribute("success");
                        }

                        if (error != null) {
                    %>
                    <div class="alert alert-error">
                        <i class="fas fa-exclamation-circle"></i> <%= error%>
                    </div>
                    <%
                            session.removeAttribute("error");
                        }
                    %>
                </div>

                <!-- RIGHT -->
                <div class="contact-right">
                    <iframe
                        title="CatClinic Location"
                        src="https://www.google.com/maps?q=Truong+Dai+Hoc+FPT+Ha+Noi,+Khu+Cong+Nghe+Cao+Hoa+Lac,+Thach+That,+Ha+Noi&z=17&output=embed"
                        width="100%"
                        height="100%"
                        style="border:0;"
                        allowfullscreen=""
                        loading="lazy">
                    </iframe>
                </div>
            </div>
        </section>

        <!-- SCRIPTS -->
        <script>
            // Form Validation
            function validateContactForm(fullName, email, phone, message) {
                const errors = {};

                if (fullName.length < 2) {
                    errors.fullName = "Full name must be at least 2 characters";
                }

                const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
                if (!emailRegex.test(email)) {
                    errors.email = "Please enter a valid email";
                }

                const phoneRegex = /^[0-9]{10,11}$/;
                if (!phoneRegex.test(phone)) {
                    errors.phone = "Phone must be 10-11 digits";
                }

                if (message.length < 10) {
                    errors.message = "Message must be at least 10 characters";
                }

                return errors;
            }

            document.getElementById("contactForm").addEventListener("submit", function (e) {
                e.preventDefault();

                const fullName = document.getElementById("fullName").value.trim();
                const email = document.getElementById("email").value.trim();
                const phone = document.getElementById("phone").value.trim();
                const message = document.getElementById("message").value.trim();

                // Reset errors
                document.querySelectorAll(".error").forEach(el => el.innerText = "");

                const errors = validateContactForm(fullName, email, phone, message);

                if (Object.keys(errors).length === 0) {
                    this.submit();
                } else {
                    Object.keys(errors).forEach(field => {
                        const errorElement = document.getElementById(`error${field.charAt(0).toUpperCase() + field.slice(1)}`);
                        if (errorElement) {
                            errorElement.innerText = errors[field];
                        }
                    });
                }
            });

            // Header scroll behavior
            let lastScrollTop = 0;
            const header = document.querySelector('header');

            if (header) {
                window.addEventListener('scroll', () => {
                    const currentScrollTop = window.pageYOffset || document.documentElement.scrollTop;

                    if (currentScrollTop > lastScrollTop && currentScrollTop > 100) {
                        header.classList.add('hidden');
                    } else {
                        header.classList.remove('hidden');
                    }

                    if (currentScrollTop > 50) {
                        header.classList.add('scrolled');
                    } else {
                        header.classList.remove('scrolled');
                    }

                    lastScrollTop = currentScrollTop;
                }, { passive: true });
            }

            // Remove hash from URL after navigation
            if (window.location.hash === "#contact") {
                setTimeout(function () {
                    history.replaceState(null, null, window.location.pathname);
                }, 500);
            }
        </script>

        <!-- FOOTER -->
        <jsp:include page="/WEB-INF/views/common/footer_1.jsp" />
    </body>
</html>
