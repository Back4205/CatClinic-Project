package com.mycompany.catclinicproject.controller.medicalTechnicianController;

import com.mycompany.catclinicproject.dao.LabDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Thêm "/technician/home" vào đây để LoginController gọi được
@WebServlet(name = "LabManagementController",
        urlPatterns = {
                "/technician/lab-hub",
                "/technician/home",
                "/technician/update-test"
        })
public class LabManagementController extends HttpServlet {
    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        LabDAO dao = new LabDAO();

        // ========================
        // 1️⃣ UPDATE TEST
        // ========================
        if (path.equals("/technician/update-test")) {

            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");
            String filter = request.getParameter("filter");

            dao.updateTestStatus(id, status);

            if (filter == null) {
                filter = "All";
            }

            response.sendRedirect(request.getContextPath()
                    + "/technician/lab-hub?status=" + filter);
            return;
        }

        // ========================
        // 2️⃣ LOAD DASHBOARD
        // ========================
        String status = request.getParameter("status");

        if (status == null) {
            status = "All";
        }

        request.setAttribute("currentStatus", status);
        request.setAttribute("labQueue", dao.getLabQueue(status));
        request.setAttribute("stats", dao.getLabStats());

        request.getRequestDispatcher("/WEB-INF/views/technician/lab-hub.jsp")
                .forward(request, response);
    }
}