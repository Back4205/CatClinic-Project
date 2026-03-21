<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link href="css/footerx.css" rel="stylesheet" type="text/css"/>
<link href="css/aboutUsStyle.css" rel="stylesheet" type="text/css"/>
<link href="css/header.css" rel="stylesheet" type="text/css"/>
<link href="css/all.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="css/ViewAllVeterinarian.css">
<!DOCTYPE html>
<html>
    <head>
    </head>
    <body>
        <jsp:include page="headerSelection.jsp"/>
        <section class="hero">
            <small>OUR EXPERTS</small>
            <h1>Meet Our Professional Veterinarians</h1>
            <p>
                Our team of highly qualified and compassionate veterinarians 
                is dedicated to providing the best possible care.
            </p>
        </section>
<div class="vet-container">
    <c:forEach var="v" items="${vetList}">
        <div class="vet-card">

            <div class="img-wrapper">
                <img class="vet-img"
                     src="${v.image}"
                     alt="Doctor Image">
                     
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
                <a href="ViewVeterianarianDetail?id=${v.vetID}" class="btn-profile">
                    View Profile
                </a>
            </div>

        </div>
    </c:forEach>
</div>

        <jsp:include page="footer_1.jsp"/>
    </body>
</html>