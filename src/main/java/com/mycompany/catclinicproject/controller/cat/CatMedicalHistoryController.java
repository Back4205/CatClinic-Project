package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.MedicalRecordDAO;
import com.mycompany.catclinicproject.model.MedicalHistoryDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.util.List;
@WebServlet(name = "CatMedicalHistoryController", urlPatterns = {"/cats/medical-history"})
public class CatMedicalHistoryController extends HttpServlet {
    private static final int PAGE_SIZE = 2;
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws jakarta.servlet.ServletException, java.io.IOException {
        int catId = Integer.parseInt(request.getParameter("catId"));
        String searchDate = request.getParameter("searchDate");
        String searchDoctor = request.getParameter("searchDoctor");
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
        int totalRecords = medicalRecordDAO.countMedicalHistoryWithFilter(catId, searchDate, searchDoctor);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        List<MedicalHistoryDTO> medicalHistoryList = medicalRecordDAO.getMedicalHistoryWithFilterPaging(catId,searchDate,searchDoctor,page,PAGE_SIZE);
        //  List<MedicalHistoryDTO> medicalHistoryList = medicalRecordDAO.getMedicalHistoryByCatID(catId);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("catId", catId);
        request.setAttribute("medicalHistoryList", medicalHistoryList);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-medical-history.jsp").forward(request, response);
    }

}