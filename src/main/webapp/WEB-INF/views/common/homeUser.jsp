<link href="css/all.css" rel="stylesheet" type="text/css"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CatClinic</title>
   <link href="css/footerx.css" rel="stylesheet" type="text/css"/>
   <link href="css/folder/css/all.min.css" rel="stylesheet" type="text/css"/>
   <link href="css/herox.css" rel="stylesheet" type="text/css"/>
   <link href="css/headerx.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<header class="navbar">

    <!-- LEFT: LOGO -->
    <div class="logo">
        <span>  <a href="loadinfo">PetClinic</a></span>
    </div>

    <!-- CENTER: MENU -->
    <nav class="main-nav">
        <a href="home">Home</a>

        <!-- Services Dropdown -->
        <div class="nav-item">
            <a href="#" class="services-link">
                Services <span class="arrow"><i class="fa-solid fa-angle-down"></i></i></span>
            </a>
           <ul class="dropdown-menu">
    <c:forEach var="s" items="${serviceList}">
    <li>
        <a href="service?id=${s.serviceID}">
            ${s.nameService}
        </a>
    </li>
</c:forEach>

</ul>

        </div>

        <a href="aboutUs">About Us</a>
        <a href="new">News</a>
        <a href="vete">Veterinarian</a>

    </nav>

    <!-- RIGHT: AUTH -->
    <div class="auth">
    <i class="fa-regular fa-user"></i>
    <ul class="dropdown-user">
        <li><a href="accessprofile">My Profile</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</div>

</header>



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
            <a href="#" class="btn-primary">Book Appointment</a>
        </div>
    </div>

    <div class="hero-image">
        <div class="image-card">
            <img src="image/imageHome/download.jpg" alt=""/>
            <div class="image-badge"> Professional Vets</div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/views/common/footer_1.jsp" />
</body>
</html>
