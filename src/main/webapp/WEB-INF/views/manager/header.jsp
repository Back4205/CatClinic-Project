<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    String currentDate = today.format(formatter);
%>

<div class="admin-header">

    <div class="admin-header-left">

        <div class="admin-header-title">
            Cat Clinic
        </div>

        <div class="admin-date">
            <%= new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy")
                    .format(new java.util.Date()) %>
        </div>

    </div>

    <div class="admin-header-right">

        <span class="admin-user">Admin</span>

        <div class="admin-avatar">
            <i class="fa-solid fa-user"></i>
        </div>

    </div>

</div>