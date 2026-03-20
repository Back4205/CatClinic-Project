package com.mycompany.catclinicproject.controller.Admin;

import com.mycompany.catclinicproject.dao.DashboardDAO;
import com.mycompany.catclinicproject.model.DashboardDTO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminDashboard", urlPatterns = {"/AdminDashboard"})
public class AdminDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        DashboardDAO dao = new DashboardDAO();
        DashboardDTO data = dao.getDashboardData();

        // 1. Xử lý cộng dồn Account từ Checkbox
        String[] selectedRoles = request.getParameterValues("roles");
        int totalSelected = 0;
        if (selectedRoles != null && data.getAccountStats() != null) {
            for (String role : selectedRoles) {
                totalSelected += data.getAccountStats().getOrDefault(role, 0);
            }
        }

        // 2. Logic tính toán chuỗi Gradient cho biểu đồ (Để JSP không bị lỗi đỏ)
        List<DashboardDTO.ServiceUsage> usages = data.getServiceUsages();
        String pieGradient = "#e2e8f0 0% 100%"; // Mặc định màu xám nếu không có dữ liệu

        if (usages != null && !usages.isEmpty()) {
            double p1 = (usages.size() > 0) ? usages.get(0).getPercent() : 0;
            double p2 = (usages.size() > 1) ? usages.get(1).getPercent() : 0;
            double p3 = (usages.size() > 2) ? usages.get(2).getPercent() : 0;

            // Tạo chuỗi mốc màu: Màu1 p1%, Màu2 (p1+p2)%, Màu3 (p1+p2+p3)%, Còn lại Màu4
            pieGradient = String.format(
                "#4338ca 0%% %.2f%%, " +        // Indigo
                "#10b981 %.2f%% %.2f%%, " +    // Emerald
                "#f59e0b %.2f%% %.2f%%, " +    // Amber
                "#ef4444 %.2f%% 100%%",        // Red
                p1, p1, (p1 + p2), (p1 + p2), (p1 + p2 + p3), (p1 + p2 + p3)
            );
        }

        // 3. Gửi dữ liệu sang JSP
        request.setAttribute("data", data);
        request.setAttribute("totalSelected", totalSelected);
        request.setAttribute("pieGradient", pieGradient);

        // Chú ý: Kiểm tra lại đường dẫn file jsp của bạn (ví dụ: "admin/dashboard.jsp")
        request.getRequestDispatcher("WEB-INF/views/manager/AdminDashboard.jsp").forward(request, response);
    }

}