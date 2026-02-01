package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId; // Quan trọng: Để lấy đúng giờ Việt Nam
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BookingHistoryController", urlPatterns = {"/booking-history"})
public class BookingHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // --- FIX CỨNG USER ID = 5 ---
        int userID = 5;

        // 1. LẤY DỮ LIỆU TỪ FORM TÌM KIẾM
        String keyword = request.getParameter("search"); 
        String filterStatus = request.getParameter("status"); 

        // 2. LẤY DANH SÁCH GỐC TỪ DATABASE
        BookingDAO dao = new BookingDAO();
        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);
        
        // 🔥 LOGIC THÔNG MINH: TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI THEO NGÀY 🔥
        // Lấy ngày hiện tại theo giờ Việt Nam
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")); 
        
        for (BookingHistoryDTO b : fullList) {
            // Xác định ngày để so sánh (CheckDate)
            LocalDate checkDate;
            
            // Nếu là Pet Hotel (có ngày kết thúc): Dùng ngày kết thúc để so sánh
            // Ví dụ Milo: EndDate là 04/02
            if (b.getEndDate() != null) {
                checkDate = b.getEndDate().toLocalDate();
            } else {
                // Nếu là Khám bệnh (chỉ có ngày bắt đầu): Dùng ngày khám
                // Ví dụ Luna: AppointmentDate là 01/02
                if (b.getAppointmentDate() != null) {
                    checkDate = b.getAppointmentDate().toLocalDate();
                } else {
                    continue; // Skip nếu dữ liệu lỗi không có ngày
                }
            }
            
            // Logic: Chỉ khi nào ngày CheckDate < Hôm nay (Quá khứ) thì mới coi là Xong
            // Milo: 04/02 < 02/02 => SAI (Vẫn giữ Upcoming)
            // Luna: 01/02 < 02/02 => ĐÚNG (Đổi thành Completed)
            if (checkDate.isBefore(today) && "Confirmed".equalsIgnoreCase(b.getStatus())) {
                b.setStatus("Completed");
            }
        }
        
        // 3. TÍNH TOÁN THỐNG KÊ (Sau khi đã update trạng thái ở trên)
        int total = fullList.size();
        int scheduled = 0;
        int completed = 0;
        
        for (BookingHistoryDTO b : fullList) {
            String s = b.getStatus();
            if (s != null) {
                if (s.equalsIgnoreCase("Confirmed") || s.equalsIgnoreCase("Pending") || s.equalsIgnoreCase("Upcoming")) {
                    scheduled++;
                } else if (s.equalsIgnoreCase("Completed") || s.equalsIgnoreCase("Done")) {
                    completed++;
                }
            }
        }

        // 4. LỌC DANH SÁCH (Theo từ khóa & Trạng thái người dùng chọn)
        List<BookingHistoryDTO> filteredList = new ArrayList<>();
        
        for (BookingHistoryDTO b : fullList) {
            boolean isMatchKeyword = true;
            boolean isMatchStatus = true;

            // -- Lọc theo từ khóa (Tên mèo hoặc Tên dịch vụ) --
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String catName = (b.getCatName() != null) ? b.getCatName().toLowerCase() : "";
                String service = (b.getServiceName() != null) ? b.getServiceName().toLowerCase() : "";
                
                if (!catName.contains(k) && !service.contains(k)) {
                    isMatchKeyword = false;
                }
            }

            // -- Lọc theo trạng thái (Nút bấm) --
            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {
                if (b.getStatus() == null || !b.getStatus().equalsIgnoreCase(filterStatus)) {
                    isMatchStatus = false;
                }
            }

            // Nếu thỏa mãn cả 2 thì thêm vào danh sách hiển thị
            if (isMatchKeyword && isMatchStatus) {
                filteredList.add(b);
            }
        }

        // 5. GỬI DỮ LIỆU SANG JSP
        request.setAttribute("bookingList", filteredList); // Danh sách đã lọc
        request.setAttribute("total", total);
        request.setAttribute("scheduled", scheduled);
        request.setAttribute("completed", completed);
        
        // Gửi lại để giữ giá trị trong ô tìm kiếm
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }
}