package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.ReceptionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckInController", urlPatterns = {"/reception/check-in"})
public class CheckInController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReceptionDAO dao = new ReceptionDAO();
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        // Logic Check-in: Cập nhật trạng thái trong Database thành 'Waiting'
        if ("confirm".equals(action) && idStr != null) {
            dao.updateStatus(Integer.parseInt(idStr), "Waiting");
            response.sendRedirect(request.getContextPath() + "/reception/check-in");
            return;
        }

        // Lấy danh sách khách hàng chưa check-in ngày hôm nay đổ ra giao diện
        request.setAttribute("pendingList", dao.getPendingCheckinsToday());

        // Trỏ về trang JSP
        request.getRequestDispatcher("/WEB-INF/views/reception/checkin-patient.jsp").forward(request, response);
    }
}