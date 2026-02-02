        <link href="css/folder/css/all.min.css" rel="stylesheet" type="text/css"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="css/headerx.css" rel="stylesheet" type="text/css"/>
<header class="navbar">

    <!-- LEFT: LOGO -->
   <div class="logo">
        <span>  <a href="loadinfo">PetClinic</a></span>
    </div>
    <!-- CENTER: MENU -->
    <nav class="main-nav">
        <a href="loadinfo">Home</a>

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
        <a href="login" class="login">Login</a>
        <a href="register" class="register">Register</a>
    </div>

</header>
