package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookingHistoryController", urlPatterns = {"/booking-history"})
public class BookingHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        // 1. Kiểm tra quyền truy cập
        if (user == null || user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO dao = new BookingDAO();
        int userID = user.getUserID();

        // 2. Lấy tham số tìm kiếm, lọc status và NGÀY
        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");
        String dateFilter = request.getParameter("dateFilter"); // MỚI THÊM: Lấy ngày từ ô input date

        // Chuẩn hóa status
        if (filterStatus == null || filterStatus.trim().isEmpty() || "ALL".equalsIgnoreCase(filterStatus)) {
            filterStatus = "ALL";
        }

        // 3. Xử lý phân trang
        int pageSize = 5;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        int offset = (currentPage - 1) * pageSize;

        // 4. Lấy dữ liệu từ Database (NHỚ: Sửa hàm này trong DAO để nhận thêm dateFilter)
        List<BookingHistoryDTO> pagedList = dao.getClientHistoryPaging(userID, keyword, filterStatus, dateFilter, offset, pageSize);
        int totalRecord = dao.countClientBookings(userID, keyword, filterStatus, dateFilter);
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        // Lấy danh sách đầy đủ để tính toán các con số thống kê (Stats)
        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);

        int total = fullList.size();
        int confirmedCount = 0;
        int completed = 0;
        int pendingPaymentCount = 0;
        int cancelledCount = 0;
        int pendingCancelCount = 0;

        LocalDate today = LocalDate.now();

        // 5. Đồng bộ trạng thái ảo cho danh sách hiển thị
        for (BookingHistoryDTO b : pagedList) {
            updateVirtualStatus(b, today);
        }

        // 6. Cập nhật trạng thái ảo cho danh sách tổng và đếm số lượng
        for (BookingHistoryDTO b : fullList) {
            updateVirtualStatus(b, today);

            String s = b.getStatus();
            if (s != null) {
                if (s.equalsIgnoreCase("PendingPayment")) pendingPaymentCount++;
                else if (s.equalsIgnoreCase("Confirmed")) confirmedCount++;
                else if (s.equalsIgnoreCase("Completed")) completed++;
                else if (s.equalsIgnoreCase("Cancelled")) cancelledCount++;
                else if (s.equalsIgnoreCase("PendingCancel")) pendingCancelCount++;
            }
        }

        // 7. Đẩy dữ liệu ra JSP
        request.setAttribute("user", user);
        request.setAttribute("bookingList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);

        request.setAttribute("pendingPaymentCount", pendingPaymentCount);
        request.setAttribute("cancelledCount", cancelledCount);
        request.setAttribute("total", total);
        request.setAttribute("confirmedCount", confirmedCount);
        request.setAttribute("completed", completed);
        request.setAttribute("pendingCancelCount", pendingCancelCount);

        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);
        request.setAttribute("currentDate", dateFilter); // MỚI THÊM: Gửi lại ngày về JSP

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }

    private void updateVirtualStatus(BookingHistoryDTO b, LocalDate today) {
        LocalDate checkDate = (b.getEndDate() != null) ? b.getEndDate().toLocalDate() :
                ((b.getAppointmentDate() != null) ? b.getAppointmentDate().toLocalDate() : null);

        if (checkDate != null && checkDate.isBefore(today) && "Confirmed".equalsIgnoreCase(b.getStatus())) {
            b.setStatus("Completed");
        }
    }
}