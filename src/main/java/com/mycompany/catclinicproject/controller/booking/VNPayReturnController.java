package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.config.VNPayConfig;
import com.mycompany.catclinicproject.dao.InvoiceDAO;
import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.dao.PaymentDAO;
import com.mycompany.catclinicproject.model.Invoice;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "VNPayReturnController", urlPatterns = {"/vnpay-return"})
public class VNPayReturnController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String> fields = new HashMap<>();

        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = fields.remove("vnp_SecureHash");

        //  VERIFY CHỮ KÝ
        String signValue = VNPayConfig.hashAllFields(fields);

        if (!signValue.equals(vnp_SecureHash)) {
            request.setAttribute("msg", "invalid_signature");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
            return;
        }

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        String amountStr = request.getParameter("vnp_Amount");

        // Lấy bookingID từ txnRef (format: bookingID_timestamp)
        String bookingID = txnRef.split("_")[0];
        int bookingIdInt = Integer.parseInt(bookingID);

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        Invoice invoice = invoiceDAO.getInvoiceByBookingID(bookingIdInt);

        if (invoice == null) {
            request.setAttribute("msg", "invoice_not_found");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
            return;
        }

        int invoiceID = invoice.getInvoiceID();

        //  NGƯỜI DÙNG HỦY (24 là cancel bên VNPay sandbox)
        if ("24".equals(responseCode)) {

            bookingDAO.updateBookingStatus(bookingIdInt, "Cancelled");
            int slotID = bookingDAO.getSlotIDByBookingID(bookingIdInt);
            bookingDAO.cancelBooking(bookingIdInt, slotID);
            request.setAttribute("msg", "failed");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
            return;
        }

        //  THANH TOÁN KHÔNG THÀNH CÔNG
        if (!"00".equals(responseCode)) {

            request.setAttribute("msg", "failed");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
            return;
        }

        //  THANH TOÁN THÀNH CÔNG
        long amountPaid = Long.parseLong(amountStr) / 100;

        // Insert Payment
        paymentDAO.insertPayment(invoiceID, amountPaid, "VNPay", request.getParameter("vnp_TransactionNo"));

        // Tính tổng đã thanh toán
        double totalPaid = paymentDAO.getTotalPaidAmount(invoiceID);
        double totalAmount = invoice.getTotalAmount();

        if (totalPaid >= totalAmount) {

            invoiceDAO.updatePaymentStatus(invoiceID, "Paid");
            bookingDAO.updateBookingStatus(bookingIdInt, "Completed");

        } else if (totalPaid > 0) {

            invoiceDAO.updatePaymentStatus(invoiceID, "PartiallyPaid");
            bookingDAO.updateBookingStatus(bookingIdInt, "Confirmed");

        }

        request.setAttribute("msg", "success");
        request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
    }
}