package com.mycompany.catclinicproject.controller.carestaffController;

import com.mycompany.catclinicproject.dao.CareDAO;
import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.model.CareTaskDTO;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CareTaskController", urlPatterns = {"/staff/daily-care-tasks"})
public class CareTaskController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        // Cập nhật lấy ID chuẩn
//        int staffId = 1; // Tạm fix cứng staffId = 1 để test.

        int userID = user.getUserID();
        UserDAO userDAO = new UserDAO();
        Integer staffId = userDAO.getStaffIDByUserID(userID);
        CareDAO dao = new CareDAO();
        Map<Integer, String> masterTasks = dao.getMasterCareTasks();
        dao.generateDailyJourneysIfMissing(staffId);
        List<CareTaskDTO> allTasks = dao.getDailyTasks(staffId);

        String today = LocalDate.now().toString();

        Map<Integer, List<String>> careHistories = new java.util.HashMap<>();
        for (CareTaskDTO t : allTasks) {
            careHistories.put(t.getBookingID(), dao.getCareHistoryLogs(t.getBookingID()));
        }
        request.setAttribute("careHistories", careHistories);

        request.setAttribute("masterTasks", masterTasks);
        request.setAttribute("allTasks", allTasks);
        request.setAttribute("today", today);
        request.setAttribute("totalCount", masterTasks.size()); // Tổng số 4 task

        request.getRequestDispatcher("/WEB-INF/views/staff/daily-care-tasks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        CareDAO dao = new CareDAO();

        if ("markTask".equals(action)) {
            int careJID = Integer.parseInt(request.getParameter("careJID"));
            int taskID = Integer.parseInt(request.getParameter("taskID"));
            dao.markTaskAsDone(careJID, taskID);
        }
        else if ("checkout".equals(action)) {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            dao.setReadyForCheckout(bookingID); // Bàn giao cho Lễ tân
        }
        else if ("saveDiary".equals(action)) {
            int careJID = Integer.parseInt(request.getParameter("careJID"));
            String note = request.getParameter("note"); // Chuỗi đã được JS gom lại
            String status = request.getParameter("status"); // Trạng thái Pending/In Progress/Completed

            dao.updateCareDiary(careJID, note, status);
        }
        else if ("extend".equals(action)) {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            String newEndDate = request.getParameter("newEndDate");
            dao.extendBookingEndDate(bookingID, newEndDate);
        }


        response.sendRedirect(request.getContextPath() + "/staff/daily-care-tasks");
    }
}