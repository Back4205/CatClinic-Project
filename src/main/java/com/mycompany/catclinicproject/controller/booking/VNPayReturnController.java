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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            /* ========= 1. VERIFY ========= */
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = fields.remove("vnp_SecureHash");
            String signValue = VNPayConfig.hashAllFields(fields);

            if (!signValue.equals(vnp_SecureHash)) {
                request.setAttribute("msg", "invalid_signature");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            /* ========= 2. LẤY DATA ========= */
            String responseCode = request.getParameter("vnp_ResponseCode");
            String txnRef = request.getParameter("vnp_TxnRef");
            String amountStr = request.getParameter("vnp_Amount");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            int invoiceID = Integer.parseInt(txnRef.split("_")[0]);

            InvoiceDAO invoiceDAO = new InvoiceDAO();
            BookingDAO bookingDAO = new BookingDAO();
            PaymentDAO paymentDAO = new PaymentDAO();

            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);
            if (invoice == null) {
                request.setAttribute("msg", "invoice_not_found");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            int bookingID = invoice.getBookingID();

            /* ========= 3. CANCEL ========= */
            if ("24".equals(responseCode)) {
                bookingDAO.cancelBooking(
                        bookingID,
                        bookingDAO.getSlotIDByBookingID(bookingID)
                );
                request.setAttribute("msg", "failed");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            /* ========= 4. FAIL ========= */
            if (!"00".equals(responseCode)) {
                request.setAttribute("msg", "failed");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            /* ========= 5. CHỐNG DOUBLE ========= */
            if (paymentDAO.isTransactionExists(transactionNo)) {
                request.setAttribute("msg", "success");
                request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
                return;
            }

            /* ========= 6. INSERT PAYMENT ========= */
            long amountPaid = Long.parseLong(amountStr) / 100;

            paymentDAO.insertPayment(
                    invoiceID,
                    amountPaid,
                    "VNPay",
                    transactionNo
            );

            /* ========= 7. UPDATE TRẠNG THÁI ========= */

            // 🔥 Vì đây là đặt cọc 20% → chỉ cần >0 là Confirm
            invoiceDAO.updatePaymentStatus(invoiceID, "PartiallyPaid");
            bookingDAO.confirmBooking(bookingID);

            request.setAttribute("transactionCode", transactionNo);
            request.setAttribute("msg", "success");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "system_error");
            request.getRequestDispatcher("/WEB-INF/views/client/PaymentResult.jsp").forward(request, response);
        }
    }
}