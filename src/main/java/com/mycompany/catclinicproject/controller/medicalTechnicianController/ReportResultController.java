
package com.mycompany.catclinicproject.controller.medicalTechnicianController;
import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.LabDAO;
import com.mycompany.catclinicproject.model.TestOrders;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name="ReportResultController", urlPatterns={"/ReportResultController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class ReportResultController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        LabDAO ldao = new LabDAO();
        TestOrders testOrder = ldao.getTestOrderById(id);
        request.setAttribute("testOrder", testOrder);
      request.getRequestDispatcher("/WEB-INF/views/technician/report-result.jsp").forward(request, response);
    }


    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    int testOrderID = Integer.parseInt(request.getParameter("testOrderID"));
    String resultName = request.getParameter("resultName");
    String action = request.getParameter("action");

    Part filePart = request.getPart("resultFile");
    String result;

    LabDAO ldao = new LabDAO();

    // Nếu có upload file mới
    if (filePart != null && filePart.getSize() > 0) {
        String fileName = "test_" + System.currentTimeMillis();
        result = CloudinaryUntil.uploadImage(filePart, fileName);

        if (result == null) {
            request.setAttribute("error", "Upload failed!");
            request.getRequestDispatcher("/WEB-INF/views/technician/report-result.jsp")
                   .forward(request, response);
            return;
        }
    } else {
        // Lấy ảnh cũ từ DB (an toàn hơn hidden field)
        TestOrders old = ldao.getTestOrderById(testOrderID);
        result = old.getResult();
    }

    if ("draft".equals(action)) {
        ldao.saveDraft(testOrderID, resultName, result);
    } 
    else if ("submit".equals(action)) {
        ldao.submitResult(testOrderID, resultName, result);
    }

    response.sendRedirect(request.getContextPath() + "/technician/home");
}
}