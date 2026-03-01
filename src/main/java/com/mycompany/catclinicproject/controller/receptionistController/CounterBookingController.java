package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.ReceptionDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CounterBookingController", urlPatterns = {"/reception/counter-booking"})
public class CounterBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ReceptionDAO dao = new ReceptionDAO();

        // Xử lý AJAX lấy danh sách khung giờ (KHÔNG DÙNG GSON)
        String action = request.getParameter("action");
        if ("getSlots".equals(action)) {
            try {
                int vetId = Integer.parseInt(request.getParameter("vetId"));
                String date = request.getParameter("date");
                List<Map<String, Object>> slots = dao.getAvailableSlots(vetId, date);

                // Tự tạo chuỗi JSON thủ công bằng Java thuần
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < slots.size(); i++) {
                    Map<String, Object> slot = slots.get(i);
                    json.append("{\"slotId\":").append(slot.get("slotId"))
                            .append(",\"startTime\":\"").append(slot.get("startTime")).append("\"}");
                    if (i < slots.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return; // Trả về JSON xong là dừng, không load JSP
        }

        // NẾU LOAD TRANG BÌNH THƯỜNG
        request.setAttribute("serviceList", dao.getAllActiveServices());
        request.setAttribute("vetList", dao.getAllVeterinarians());
        request.getRequestDispatcher("/WEB-INF/views/reception/counter-booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String phone = request.getParameter("phone");
            String fullName = request.getParameter("fullName");
            String petName = request.getParameter("petName");
            String breed = request.getParameter("breed");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");
            int vetId = Integer.parseInt(request.getParameter("doctor"));
            int serviceId = Integer.parseInt(request.getParameter("service"));
            String note = request.getParameter("note");
            String appointmentDate = request.getParameter("appointmentDate");
            String appointmentTime = request.getParameter("appointmentTime");

            String slotIdStr = request.getParameter("slotId");
            if (slotIdStr == null || slotIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Please+select+a+valid+time+slot!");
                return;
            }
            int slotId = Integer.parseInt(slotIdStr);

            // VALIDATE THỜI GIAN
            java.time.LocalDate selectedDate = java.time.LocalDate.parse(appointmentDate);
            java.time.LocalTime selectedTime = java.time.LocalTime.parse(appointmentTime);
            java.time.LocalDateTime selectedDateTime = java.time.LocalDateTime.of(selectedDate, selectedTime);

            if (selectedDateTime.isBefore(java.time.LocalDateTime.now())) {
                response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Invalid+Time!+Cannot+book+in+the+past.");
                return;
            }

            ReceptionDAO dao = new ReceptionDAO();
            boolean success = dao.createFullCounterBooking(phone, fullName, petName, breed, age, gender, vetId, serviceId, appointmentDate, appointmentTime, note, slotId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/reception/check-in?mess=Booking+Created+Successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Failed+to+create+booking");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Invalid+Input+Data");
        }
    }
}
