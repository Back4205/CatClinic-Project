package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

// Thêm đường dẫn chi tiết vào urlPatterns
@WebServlet(name = "ViewBookingListController", urlPatterns = {
        "/reception/view-booking-list",
        "/reception/home",
        "/reception/update-status",
        "/reception/appointment-detail"
})
public class ViewBookingListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        BookingDAO dao = new BookingDAO();

        // NHÁNH 1: Xử lý Xem chi tiết (UC49)
        if (path.contains("appointment-detail")) {
            try {
                int bookingID = Integer.parseInt(request.getParameter("id"));
                BookingHistoryDTO detail = dao.getBookingDetailByID(bookingID);
                if (detail != null) {
                    request.setAttribute("booking", detail); // Tên biến khớp với JSP
                    request.getRequestDispatcher("/WEB-INF/views/reception/appointment-detail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("home");
                }
                return; // Ngắt hàm tại đây sau khi forward
            } catch (Exception e) { e.printStackTrace(); response.sendRedirect("home"); return; }
        }

        // NHÁNH 2: Xử lý Cập nhật trạng thái (Confirm/Check-in)
        else if (path.contains("update-status")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");
                dao.updateBookingStatus(id, status);
                response.sendRedirect("home");
                return;
            } catch (Exception e) { e.printStackTrace(); }
        }

        // NHÁNH 3: Hiển thị danh sách chính (Dashboard)
        String searchQuery = request.getParameter("searchQuery");
        Map<String, Integer> stats = dao.getBookingDashboardStats();
        List<BookingHistoryDTO> bookingList = dao.getReceptionBookingList(searchQuery);

        request.setAttribute("stats", stats);
        request.setAttribute("bookingList", bookingList);
        request.setAttribute("searchQuery", searchQuery);

        request.getRequestDispatcher("/WEB-INF/views/reception/view-booking-list.jsp").forward(request, response);
    }
}