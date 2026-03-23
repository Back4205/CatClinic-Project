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
import java.util.List;

@WebServlet(name = "RecordDiaryController", urlPatterns = {"/staff/record-care-diary"})
public class RecordDiaryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user != null) {
            UserDAO userDAO = new UserDAO();
            Integer staffId = userDAO.getStaffIDByUserID(user.getUserID());
            if (staffId != null) {
                CareDAO dao = new CareDAO();
                List<CareTaskDTO> allTasks = dao.getDailyTasks(staffId);
                request.setAttribute("allTasks", allTasks);
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/staff/record-care-diary.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("saveDiary".equals(action)) {
            int careJID = Integer.parseInt(request.getParameter("careJID"));
            String status = request.getParameter("status");
            String combinedNote = request.getParameter("combinedNote"); // Đã được JS nối chuỗi

            CareDAO dao = new CareDAO();
            dao.updateCareDiary(careJID, combinedNote, status);
        }
        response.sendRedirect(request.getContextPath() + "/staff/record-care-diary");
    }
}