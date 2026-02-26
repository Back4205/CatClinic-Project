<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>CatClinic</title>
        <link href="css/all.css" rel="stylesheet" type="text/css"/>
        <link href="css/herox.css" rel="stylesheet" type="text/css"/>
        <link href="css/footerx.css" rel="stylesheet" type="text/css"/>
        <link href="css/headerx.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>

        <!-- HEADER -->
        <jsp:include page="headerSelection.jsp"/>

        <section class="hero">
            <!-- Decoration -->
            <div class="hero-bg-circle"></div>

            <div class="hero-text">
                <span class="badge"> </span>

                <h1>
                    Professional Care <br>
                    for Your <span>Lovely Cats</span>
                </h1>

                <p>
                    Manage appointments, medical records, and cat healthcare easily.
                    A specialized, feline-friendly environment designed just for them.
                </p>
                <div class="buttons">
                    <a href="#" class="btn-primary"> <i class="fa-solid fa-calendar"></i> Book Appointment</a>
                </div>
            </div>
            <div class="hero-image">
                <div class="image-card">
                    <div class="image-card">
                        <img 
                            src="https://res.cloudinary.com/dydnbzspg/image/upload/v1772093271/The_Therapeutic_Energy_of_Aromatherapy_for_Canines_and_Cats_jj02fi.jpg"
                            alt="Cat Clinic Service"
                            />

                        <div class="image-badge">Professional Vets</div>
                    </div>
                    <div class="image-badge"> Professional Vets</div>
                </div>
            </div>
        </section>
        <section class="services">
            <h2 class="section-title">Cat Clinic's services</h2>
            <div class="line"></div>
            <div class="service-container">
                <c:forEach var="s" items="${listService}" begin="0" end="5">
                    <a href="service?id=${s.serviceID}" class="service-link">
                        <div class="service-item">
                            <div class="image-box">
                                <img src="${s.imgUrl}">                            
                            </div>
                            <h3>${s.nameService}</h3>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <div class="view-all">
                <a href="ViewAllService">View All</a>
            </div>
        </section>
        <section class="why-choose">
            <div class="container">

                <div class="section-header">
                    <h2>
                        <span class="big-number">03</span>
                        Reasons to Choose <span class="brand">GAIA</span>
                    </h2>
                    <div class="underline"></div>
                </div>

                <div class="why-grid">

                    <!-- Item 1 -->
                    <div class="why-item">
                        <div class="icon-circle">
                            <i class="fa-solid fa-star"></i>
                        </div>
                        <h3>Japanese experience and background</h3>
                        <p>
                            The PetClinic team is highly trained in Japan and has over 15 years of experience, providing standard and reliable veterinary services.
                        </p>
                    </div>

                    <!-- Item 2 -->
                    <div class="why-item">
                        <div class="icon-circle">
                            <i class="fa-solid fa-heart"></i>
                        </div>
                        <h3>Dedicated consultation, transparent pricing.</h3>
                        <p>
                            The veterinarian is always there to support the pet owner, clearly explaining each step, and providing a transparent and appropriate treatment plan.
                        </p>
                    </div>

                    <!-- Item 3 -->
                    <div class="why-item">
                        <div class="icon-circle">
                            <i class="fa-solid fa-hospital"></i>
                        </div>
                        <h3>Comprehensive and convenient service.</h3>
                        <p>
                            GAIA offers a comprehensive pet care solution:including medical check-ups, vaccinations, spa treatments, pet hotel services,and immigration assistance – all in one system.
                        </p>
                    </div>

                </div>

            </div>
        </section>
        <section class="contact-section" id="contact">
            <div class="container contact-wrapper">

                <!-- LEFT -->
                <div class="contact-left">
                    <h2>Liên Hệ</h2>

                    <p class="contact-desc">
                        Thú cưng của bạn cần được quan tâm ngay hôm nay?
                        Gọi cho PetClinic hoặc để lại thông tin, chúng tôi sẽ phản hồi nhanh nhất.
                    </p>

                    <div class="contact-social">
                        <i class="fa-brands fa-facebook"></i>
                        <i class="fa-solid fa-comment"></i>
                        <i class="fa-solid fa-phone"></i>
                    </div>
                    <form action="Contact" method="post" class="contact-form">

                        <input type="text" name="fullName" id="fullName" placeholder="Họ và tên">
                        <span class="error" id="errorFullName"></span>

                        <input type="text" name="email" id="email" placeholder="E-mail">
                        <span class="error" id="errorEmail"></span>

                        <input type="text" name="phone" id="phone" placeholder="Số điện thoại">
                        <span class="error" id="errorPhone"></span>

                        <textarea name="message" id="message" placeholder="Nội dung"></textarea>
                        <span class="error" id="errorMessage"></span>

                        <button type="submit">Gửi phản hồi</button>

                    </form>
                    <%
                        String success = (String) session.getAttribute("success");
                        String error = (String) session.getAttribute("error");

                        if (success != null) {
                    %>
                    <div style="color:green;"><%= success%></div>
                    <%
                            session.removeAttribute("success");
                        }

                        if (error != null) {
                    %>
                    <div style="color:red;"><%= error%></div>
                    <%
                            session.removeAttribute("error");
                        }
                    %>
                </div>


                <!-- RIGHT -->
                <div class="contact-right">
                    <iframe
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
        <script>
            document.getElementById("contactForm").addEventListener("submit", function (e) {

                e.preventDefault(); // chặn gửi form

                let isValid = true;

                let fullName = document.getElementById("fullName").value.trim();
                let email = document.getElementById("email").value.trim();
                let phone = document.getElementById("phone").value.trim();
                let message = document.getElementById("message").value.trim();

                // reset lỗi
                document.querySelectorAll(".error").forEach(el => el.innerText = "");

                // Validate Họ tên
                if (fullName.length < 2) {
                    document.getElementById("errorFullName").innerText = "Họ tên phải ít nhất 2 ký tự";
                    isValid = false;
                }

                // Validate Email
                let emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;
                if (!emailRegex.test(email)) {
                    document.getElementById("errorEmail").innerText = "Email không hợp lệ";
                    isValid = false;
                }

                // Validate Phone
                let phoneRegex = /^[0-9]{10,11}$/;
                if (!phoneRegex.test(phone)) {
                    document.getElementById("errorPhone").innerText = "Số điện thoại phải 10-11 số";
                    isValid = false;
                }

                // Validate Nội dung
                if (message.length < 10) {
                    document.getElementById("errorMessage").innerText = "Nội dung phải ít nhất 10 ký tự";
                    isValid = false;
                }

                if (isValid) {
                    alert("Gửi thành công!");
                    document.getElementById("contactForm").reset();
                }
            });
        </script>
        <script>
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
