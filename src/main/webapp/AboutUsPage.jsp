<%-- 
    Document   : AboutUsPage
    Created on : Jan 27, 2026, 10:41:56 PM
    Author     : Son
--%>
<link href="css/footerx.css" rel="stylesheet" type="text/css"/>
<link href="css/aboutUsStyle.css" rel="stylesheet" type="text/css"/>
<link href="css/header.css" rel="stylesheet" type="text/css"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link href="css/all.css" rel="stylesheet" type="text/css"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<jsp:include page="/header.jsp" />
<section class="about-section">
    <div class="about-container">
        
        <!-- Image -->
        <div class="about-image">
            <img src="image/imageHome/Veterinarian examination of a golden retriever dog medical clinic.jpg" alt=""/>
        </div>

        <!-- Content -->
        <div class="about-content">
            <span class="about-badge">ABOUT US</span>
            <h2>
                Dedicated Experts for <br>
                Your <span>Feline Friends</span>
            </h2>

            <p>
                Founded in 2015, PurrfectCare Clinic has dedicated nearly a decade
                to providing specialized, low-stress veterinary care exclusively
                for cats. Our mission is to ensure every feline receives gentle,
                professional, and compassionate treatment.
            </p>

            <ul class="about-features">
                <li>🐾 Cat-only specialized clinic</li>
                <li>🐾 Low-stress handling techniques</li>
                <li>🐾 Experienced & certified veterinarians</li>
            </ul>

            <a href="about" class="about-btn">Learn More</a>
        </div>

    </div>
</section>

    <jsp:include page="footer.jsp" />

</body>
</html>
