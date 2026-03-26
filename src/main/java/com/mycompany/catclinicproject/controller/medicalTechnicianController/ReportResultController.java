
package com.mycompany.catclinicproject.controller.medicalTechnicianController;
import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.LabDAO;
import com.mycompany.catclinicproject.dao.NotificationDAO;
import com.mycompany.catclinicproject.model.TestOrders;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.websocket.NotificationSocket;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));   
        LabDAO ldao = new LabDAO();
        TestOrders testOrder = ldao.getTestOrderById(id);
        request.setAttribute("testOrder", testOrder);
        request.getRequestDispatcher("/WEB-INF/views/technician/report-result.jsp").forward(request, response);
    }


     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         NotificationDAO ndao = new NotificationDAO();
        int testOrderID = Integer.parseInt(request.getParameter("testOrderID"));
        String action = request.getParameter("action");

        LabDAO ldao = new LabDAO();
        TestOrders testOrder = ldao.getTestOrderById(testOrderID);


        String testName = testOrder.getTestName();
        String result = testOrder.getResult(); 

        if ("X-Ray".equalsIgnoreCase(testName)) {

            Part filePart = request.getPart("resultFile");

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = "test_" + System.currentTimeMillis();
                String uploadedUrl = CloudinaryUntil.uploadImage(filePart, fileName, "my_tech");

                if (uploadedUrl == null) {
                    request.setAttribute("error", "Upload failed!");
                    request.setAttribute("testOrder", testOrder);
                    request.getRequestDispatcher("/WEB-INF/views/technician/report-result.jsp")
                            .forward(request, response);
                    return;
                }

                result = uploadedUrl;
            }
        }
        else{
            result = request.getParameter("resultFile");
        }

        if ("draft".equals(action)) {

                ldao.saveDraftFull(testOrderID, result);
        } else if ("submit".equals(action)) {
            ldao.submitFull(testOrderID, result);
            if ("Blood Test".equalsIgnoreCase(testName)) {
                int medicalRecordID = ldao.getMedicalRecordIdByTestOrderId(testOrderID);
                int VetID = ldao.getVetIdByTestOrderId(testOrderID);
                String message = "You have just received result a bloodtest.";
                int notiID = ndao.createNotification(VetID, message, medicalRecordID, "request BL");
                if (notiID != -1) {
                    NotificationSocket.sendNotification(VetID, notiID, message, "request BL");
                }
            } else {
                int medicalRecordID = ldao.getMedicalRecordIdByTestOrderId(testOrderID);
                int VetID = ldao.getVetIdByTestOrderId(testOrderID);
                String message = "You have just received result a X-Ray.";
                int notiID = ndao.createNotification(VetID, message, medicalRecordID, "requestX");
                if (notiID != -1) {
                    NotificationSocket.sendNotification(VetID, notiID, message, "requestX");
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/technician/home");
    }
}
