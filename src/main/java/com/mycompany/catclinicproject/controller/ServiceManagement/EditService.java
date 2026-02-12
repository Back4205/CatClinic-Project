/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDAO;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EditService", urlPatterns = {"/EditService"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class EditService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int serviceID = Integer.parseInt(request.getParameter("serviceID"));

        ServiceDAO sdao = new ServiceDAO();
        CategoryDAO cdao = new CategoryDAO();

        Service service = sdao.getServiceById(serviceID);
        List<Category> category = cdao.getAllCategory();

        request.setAttribute("service", service);
        request.setAttribute("category", category);
        request.getRequestDispatcher("/WEB-INF/views/manager/serviceEdit.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceID = Integer.parseInt(request.getParameter("serviceID"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        int time = Integer.parseInt(request.getParameter("time"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        Part filePart = request.getPart("image");
        String imageUrl;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "service_" + System.currentTimeMillis();
            imageUrl = CloudinaryUntil.uploadImage(filePart, fileName);
        } else {
            imageUrl = request.getParameter("oldImage");
        }


        Service s = new Service();
        s.setServiceID(serviceID);
        s.setNameService(name);
        s.setPrice(price);
        s.setDescription(description);
        s.setTimeService(time);
        s.setCategoryID(categoryID);
        s.setImgUrl(imageUrl);
        ServiceDAO dao = new ServiceDAO();
        dao.updateService(s);

        response.sendRedirect(request.getContextPath() + "/ViewServiceList?id=" + categoryID);
    }
}
