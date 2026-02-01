package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.MedicalRecordDAO;
import com.mycompany.catclinicproject.model.MedicalHistoryView;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.util.List;
@WebServlet(name = "CatMedicalHistoryController", urlPatterns = {"/cats/medical-history"})
public class CatMedicalHistoryController extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws jakarta.servlet.ServletException, java.io.IOException {
        int catId = Integer.parseInt(request.getParameter("catId"));
        MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
        List<MedicalHistoryView> medicalHistoryList = medicalRecordDAO.getMedicalHistoryByCatID(catId);
        request.setAttribute("medicalHistoryList", medicalHistoryList);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-medical-history.jsp").forward(request, response);
    }

}
