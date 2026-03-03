package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.ReceptionDAO;
import com.mycompany.catclinicproject.dao.TimeSlotDAO;
import com.mycompany.catclinicproject.model.TimeSlot;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CounterBookingController", urlPatterns = {"/reception/counter-booking"})
public class CounterBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReceptionDAO dao = new ReceptionDAO();
        String action = request.getParameter("action");

        if ("getSlots".equals(action)) {
            try {
                int vetId = Integer.parseInt(request.getParameter("vetId"));
                String date = request.getParameter("date");
                TimeSlotDAO slotDao = new TimeSlotDAO();
                java.sql.Date sqlDate = java.sql.Date.valueOf(date);

                List<TimeSlot> slots = slotDao.getSlotsNext7Days1(vetId, sqlDate);

                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < slots.size(); i++) {
                    TimeSlot slot = slots.get(i);
                    json.append("{\"slotId\":").append(slot.getSlotID())
                            .append(",\"startTime\":\"").append(slot.getStartTime().toString().substring(0, 5)).append("\"}");
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
            }
            return;
        }

        if ("getServicesByCategory".equals(action)) {
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                com.mycompany.catclinicproject.dao.ServiceDAO serviceDao = new com.mycompany.catclinicproject.dao.ServiceDAO();
                List<com.mycompany.catclinicproject.model.Service> services = serviceDao.getServicesByCategoryID(categoryId);

                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < services.size(); i++) {
                    com.mycompany.catclinicproject.model.Service s = services.get(i);
                    json.append("{\"serviceID\":").append(s.getServiceID())
                            .append(",\"nameService\":\"").append(s.getNameService())
                            .append("\",\"price\":").append(s.getPrice()).append("}");
                    if (i < services.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if ("getStaffByPosition".equals(action)) {
            try {
                String position = request.getParameter("position");
                List<Map<String, Object>> staffList = dao.getStaffByPosition(position);

                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < staffList.size(); i++) {
                    Map<String, Object> staff = staffList.get(i);
                    json.append("{\"staffID\":").append(staff.get("staffID"))
                            .append(",\"fullName\":\"").append(staff.get("fullName")).append("\"}");

                    if (i < staffList.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("]");

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        com.mycompany.catclinicproject.dao.CategoryDao categoryDao = new com.mycompany.catclinicproject.dao.CategoryDao();
        request.setAttribute("categoryList", categoryDao.getAllCategories());
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
            String note = request.getParameter("note");

            String categoryStr = request.getParameter("category");
            int categoryId = (categoryStr != null && !categoryStr.isEmpty()) ? Integer.parseInt(categoryStr) : 0;

            String serviceStr = request.getParameter("service");
            int serviceId = (serviceStr != null && !serviceStr.isEmpty()) ? Integer.parseInt(serviceStr) : 0;

            String doctorStr = request.getParameter("doctor");
            int vetId = (doctorStr != null && !doctorStr.isEmpty()) ? Integer.parseInt(doctorStr) : 0;

            String staffStr = request.getParameter("staffId");
            int staffId = (staffStr != null && !staffStr.isEmpty()) ? Integer.parseInt(staffStr) : 0;

            String appointmentDate = request.getParameter("appointmentDate");

            String slotIdStr = request.getParameter("slotId");
            int slotId = 0;
            if (categoryId == 1 || categoryId == 2) {
                if (slotIdStr == null || slotIdStr.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Please+select+a+valid+time+slot!");
                    return;
                }
                slotId = Integer.parseInt(slotIdStr);
            }

            String appointmentTime = request.getParameter("appointmentTime");
            if (appointmentTime == null || appointmentTime.isEmpty()) {
                appointmentTime = request.getParameter("checkInTime");
            }

            ReceptionDAO dao = new ReceptionDAO();
            boolean success = dao.createFullCounterBooking(phone, fullName, petName, breed, age, gender, vetId, serviceId, appointmentDate, appointmentTime, note, slotId, staffId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/reception/home?mess=Booking+Created+Successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Failed+to+create+booking");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/reception/counter-booking?error=Invalid+Input+Data");
        }
    }
}