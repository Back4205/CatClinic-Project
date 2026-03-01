package com.mycompany.catclinicproject.controller.carestaffController;

import com.mycompany.catclinicproject.dao.CareStaffDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CareStaffController", urlPatterns = {"/staff/tasks"})
public class CareStaffController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");
        if (acc == null || acc.getRoleID() != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CareStaffDAO dao = new CareStaffDAO();
        List<Map<String, Object>> cats = dao.getInpatientCats();
        request.setAttribute("inpatientCats", cats);
        request.setAttribute("careTasks", dao.getAllCareTasks()); // For dropdown in Log Entry

        // Nếu Staff click chọn 1 con mèo cụ thể từ danh sách bên trái
        String catIdParam = request.getParameter("catId");
        String bookingIdParam = request.getParameter("bookingId");

        if (catIdParam != null && !catIdParam.isEmpty()) {
            int catId = Integer.parseInt(catIdParam);

            // Tìm thông tin con mèo đang được chọn
            Map<String, Object> selectedCat = null;
            for (Map<String, Object> cat : cats) {
                if ((Integer) cat.get("CatID") == catId) {
                    selectedCat = cat;
                    break;
                }
            }

            request.setAttribute("selectedCat", selectedCat);
            request.setAttribute("selectedBookingId", bookingIdParam);

            // Lấy riêng Care Schedule (UC40, UC42) và Observations (UC41) của mèo này
            request.setAttribute("dailyTasks", dao.getDailyTasksStatus(catId));
            request.setAttribute("observations", dao.getObservations(catId));
        }

        request.getRequestDispatcher("/WEB-INF/views/staff/care-tasks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            User acc = (User) session.getAttribute("acc");
            CareStaffDAO dao = new CareStaffDAO();
            int staffId = dao.getStaffIdByUserId(acc.getUserID());

            String action = request.getParameter("action");
            int catId = Integer.parseInt(request.getParameter("catId"));
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int taskId = Integer.parseInt(request.getParameter("taskId"));

            // UC42: Đánh dấu check "Completed" nhanh
            if ("markTask".equals(action)) {
                dao.addCareJourney(catId, bookingId, staffId, taskId, "Task marked as Completed.");
            }
            // UC41: Ghi chú "Log Entry" chi tiết
            else if ("addLog".equals(action)) {
                String note = request.getParameter("note");
                dao.addCareJourney(catId, bookingId, staffId, taskId, note);
            }

            // Reload lại trang và giữ nguyên bé mèo đang chọn
            response.sendRedirect(request.getContextPath() + "/staff/tasks?catId=" + catId + "&bookingId=" + bookingId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/staff/tasks?error=Invalid+Input");
        }
    }
}