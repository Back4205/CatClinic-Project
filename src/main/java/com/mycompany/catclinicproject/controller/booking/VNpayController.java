package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.config.VNPayConfig;
import com.mycompany.catclinicproject.dao.InvoiceDAO;
import com.mycompany.catclinicproject.model.Invoice;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "VNpayController", urlPatterns = {"/vnpay"})
public class VNpayController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookingID = request.getParameter("bookingID");
        if (bookingID == null || bookingID.isEmpty()) {
            response.sendRedirect("/WEB-INF/views/client/error.jsp?msg=Invalid Booking ID");
            return;
        }

        int bookingIdInt;
        try {
            bookingIdInt = Integer.parseInt(bookingID);
        } catch (NumberFormatException e) {

            return;
        }

        // Lấy Invoice
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        Invoice invoice = invoiceDAO.getInvoiceByBookingID(bookingIdInt);

        if (invoice == null) {
            response.sendRedirect("/WEB-INF/views/client/error.jsp?msg=Invalid Invoice ID");
            return;
        }



        // Tính tiền đặt cọc 20%
        long deposit = Math.round(invoice.getTotalAmount() * 0.2);
        long vnpAmount = deposit * 100; // VNPay yêu cầu nhân 100

        //Tạo mã giao dịch UNIQUE
        String vnp_TxnRef = invoice.getInvoiceID() + "_" + System.currentTimeMillis();
        String vnp_IpAddr = request.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan booking " + bookingID);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thời gian Việt Nam
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String createDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", createDate); // tg giao dịch

        cld.add(Calendar.MINUTE, 5); // hết hạn sau 5p
        String expireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", expireDate); // tg het han

        //  Sắp xếp key
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {

            String fieldName = fieldNames.get(i);
            String value = vnp_Params.get(fieldName);

            if (value != null && !value.isEmpty()) {

                String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);

                hashData.append(fieldName).append("=").append(encodedValue);

                query.append(fieldName).append("=").append(encodedValue); // url den trang thanh toan
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        //  Tạo chữ ký
        String secureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query;
        response.sendRedirect(paymentUrl);
    }
}