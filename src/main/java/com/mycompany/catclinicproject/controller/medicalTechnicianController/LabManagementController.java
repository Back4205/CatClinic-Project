package com.mycompany.catclinicproject.controller.medicalTechnicianController;

import com.mycompany.catclinicproject.dao.LabDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LabManagementController",
        urlPatterns = {
                "/technician/lab-hub",
                "/technician/home",
                "/technician/update-test"
        })
public class LabManagementController extends HttpServlet {

    private static final int PAGE_SIZE = 5; // mỗi trang 5 record

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

            if (filter == null || filter.isEmpty()) {
                filter = "All";
            }

            response.sendRedirect(request.getContextPath()
                    + "/technician/lab-hub?status=" + filter);

            return;
        }

        // ========================
        // 2️⃣ LOAD DASHBOARD + PAGINATION
        // ========================

        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "All";
        }

        // Lấy page
        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
                if (page <= 0) page = 1;
            }
        } catch (Exception e) {
            page = 1;
        }

        // Lấy full list từ DAO (không phân trang trong DAO)
        List<?> fullList = dao.getLabQueue(status);

        int totalRecords = fullList.size();
        int totalPage = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        // Nếu page vượt quá tổng trang
        if (page > totalPage && totalPage != 0) {
            page = totalPage;
        }

        int fromIndex = (page - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalRecords);

        List<?> pagedList;

        if (totalRecords == 0) {
            pagedList = new ArrayList<>();
        } else {
            pagedList = fullList.subList(fromIndex, toIndex);
        }

        // Set attribute cho JSP
        request.setAttribute("currentStatus", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("labQueue", pagedList);
        request.setAttribute("stats", dao.getLabStats());

        request.getRequestDispatcher("/WEB-INF/views/technician/lab-hub.jsp")
                .forward(request, response);
    }
}