<%-- 
    Document   : Vaccination
    Created on : Jan 29, 2026, 2:29:18 PM
    Author     : Son
--%>
<link href="css/petHotelStyle.css" rel="stylesheet" type="text/css"/>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <section class="pet-transfer">
            <div class="container">
                <h2>Taking care of your beloved cat</h2>
                <div class="line"></div>
                <p class="desc">
                    Whether you're busy with business trips, vacations, or away for an extended period, we can help you take care of your beloved cat.
                </p>

                <div class="features">
                    <div class="feature">
                        <span class="icon">✔</span>
                        <div>
                            <h3>Hotel-standard pet accommodation</h3>
                            <p>Clean, airy living spaces with private rooms for each pet, ensuring comfort and safety throughout their stay.</p>
                        </div>
                    </div>

                    <div class="feature">
                        <span class="icon">✔</span>
                        <div>
                            <h3>Daily Monitoring and Updates</h3>
                            <p>We provide daily updates on your pet's photos, videos, and eating and living habits, giving you peace of mind when you leave your pet with us.</p>
                        </div>
                    </div>

                    <div class="feature">
                        <span class="icon">✔</span>
                        <div>
                            <h3>Basic grooming, hygiene, and spa services</h3>
                            <p>Regular bathing, ear cleaning, nail trimming, and grooming will help keep your pet clean and healthy during their stay at the hotel.</p>
                        </div>
                    </div>

                    <div class="feature">
                        <span class="icon">✔</span>
                        <div>
                            <h3>Exercise and play with your pet</h3>
                            <p>Daily playtime, walks, and interaction help reduce stress and prevent boredom when your pet is away from you.</p>
                        </div>
                    </div>
                </div>

                <div class="btn-wrap">
                    <a href="#" class="btn-book">Booking</a>
                </div>
            </div>
        </section>

        <!-- GALLERY -->
        <section class="pet-gallery">
            <div class="gallery-grid">
                <img src="image/imageHome/1.jpg" alt="" class="g1"/>
                <img src="image/imageHome/2.jpg" alt="" class="g2"/>
                <img src="image/imageHome/7.jpg" alt="" class="g3"/>

                <img src="image/imageHome/4.jpg" alt="" class="g4"/>
                <img src="image/imageHome/5.jpg" alt="" class="g5"/>
                <img src="image/imageHome/g6.jpg" alt="" class="g6"/>
                <img src="image/imageHome/3.jpg" alt="" class="g7"/>
                <img src="image/imageHome/g8.jpg" alt="" class="g8"/>
            </div>
        </section>
        <jsp:include page="footer.jsp" />

    </body>
</html>
