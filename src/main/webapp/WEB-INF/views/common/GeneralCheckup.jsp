<%-- 
    Document   : GeneralCheckup
    Created on : Jan 29, 2026, 2:28:51 PM
    Author     : Son
--%>
<link href="css/all.css" rel="stylesheet" type="text/css"/>
<link href="css/GeneralCheckupStyle.css" rel="stylesheet" type="text/css"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<jsp:include page="/WEB-INF/views/common/header_1.jsp" />

        <section class="hero">
            <div class="hero-content">
                <h1>Pet Examination and Consultation</h1>
                <p>
                    At CatClinic, each consultation is designed as a comprehensive care package,
                    combining general health check-ups, 
                    in-depth advice, and guidance on disease prevention.
                </p>

                <a href="#" class="hero-btn">Booking</a>
            </div>

            <div class="hero-image">
                <img src="image/imageHome/download.jpg" alt=""/>
            </div>
        </section>
        
        <section class="service-detail">
  <div class="service-grid">
    <div class="service-images">
        <img src="image/imageHome/imgbig.jpg" alt="" class="img-big"/>
        <img src="image/imageHome/imgsmall.jpg" alt="" class="img-small"/>
        <img src="image/imageHome/imgwide.jpg" alt="" class="img-wide"/>
    </div>

    <div class="service-text">
      <div class="service-item">
        <span>✔</span>
        <div>
          <h4>Dedicated doctors - transparent costs</h4>
          <p>
           Each appointment is a supportive experience: the doctor listens, explains clearly, and

provides a transparent treatment plan.
          </p>
        </div>
      </div>

      <div class="service-item">
        <span>✔</span>
        <div>
          <h4>Comprehensive and personalized solutions</h4>
          <p>
            From preventative care and nutrition to specialized, personalized treatment for each pet.
          </p>
        </div>
      </div>

      <div class="service-item">
        <span>✔</span>
        <div>
          <h4>Friendly space - reduces stress</h4>
          <p>
          The safe, gentle design helps keep pets at ease throughout the examination process.
          </p>
        </div>
      </div>

      <a href="#" class="book-btn">Booking</a>
    </div>
  </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer_1.jsp" />

    </body>
</html>
