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
<jsp:include page="/WEB-INF/views/common/header_1.jsp" />

        <section class="hero">
            <!-- Decoration -->
            <div class="hero-bg-circle"></div>

            <div class="hero-text">
                <span class="badge"> Trusted Pet Care</span>

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
                    <img src="image/imageHome/download.jpg" alt=""/>
                    <div class="image-badge"> Professional Vets</div>
                </div>
            </div>
        </section>

   <!-- FOOTER -->
<jsp:include page="/WEB-INF/views/common/footer_1.jsp" />
    </body>
</html>
