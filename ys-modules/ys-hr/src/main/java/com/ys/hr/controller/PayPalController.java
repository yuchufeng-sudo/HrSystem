package com.ys.hr.controller;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import com.ys.hr.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @Author：hzz
 * @Date ：2025/6/18 15:44
 */

@RestController
@RequestMapping("/paypal")
@SessionAttributes("authorizationToken")
public class PayPalController {
    @Autowired
    private PayPalService payPalService;

    // Create payment and Redirect to PayPal
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(
            @RequestParam("amount") Double amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        request.getSession().setAttribute("authorizationToken", token);
        System.out.println("Session ID in /create: " + request.getSession().getId());
        try {

            String baseUrl = "http://xz-ai.info:9084/prod-api/hr";

            String cancelUrl = baseUrl + "/paypal/cancel";
            String successUrl = baseUrl + "/paypal/success"; 

            // Call PayPalService to create payment
            Payment payment = payPalService.createPayment(
                    amount,
                    currency,
                    "paypal",
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            // Extract Redirect URL
            String redirectUrl = null;
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    redirectUrl = link.getHref();
                    break;
                }
            }

            if (redirectUrl == null) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("message", "Cannot get the PayPalRedirectURL."));
            }

            return ResponseEntity.ok(Collections.singletonMap("redirectUrl", redirectUrl));
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Create PayPal payment failed.：" + e.getMessage()));
        }
    }

    // Payment success callback
    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            HttpSession session) {
        // Retrieve the token from the session.
        String token = (String) session.getAttribute("authorizationToken");
        System.out.println("Retrieved the token: " + token);
        System.out.println("Session ID in /success: " + session.getId());
        // Use the token to execute business logic.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        try {
          // Print the received parameters
            System.out.println("Received PayPal success callback:");
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Payer ID: " + payerId);

            // Execute payment confirmation
            Payment payment = payPalService.executePayment(paymentId, payerId);
            // Extract transaction details
            Transaction transaction = payment.getTransactions().get(0);
            Amount amount = transaction.getAmount();
            String total = amount.getTotal();
            String currency = amount.getCurrency();

            String transactionId = "N/A";
            if (!transaction.getRelatedResources().isEmpty()) {
                Sale sale = transaction.getRelatedResources().get(0).getSale();
                if (sale != null) {
                    transactionId = sale.getId();
                }
            }

           // Build successful RedirectURL
            String successUrl = "http://localhost:8080/PayPalResult" +
                    "?success=true" +
                    "&amount=" + total +
                    "¤cy=" + currency +
                    "&txnId=" + transactionId;

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", successUrl)
                    .build();

        } catch (PayPalRESTException e) {
            
            String errorMsg = "Payment processing error: " + e.getDetails().getMessage();
            String errorUrl = null;
            try {
                errorUrl = "http://localhost:8080/PayPalResult?error=" +
                        URLEncoder.encode(errorMsg, String.valueOf(StandardCharsets.UTF_8));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", errorUrl)
                    .build();
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<Map<String, Object>> paymentCancel(
            @RequestParam(value = "orderId", required = false) String orderId
    ) {
        System.out.println("Payment canceled");
        // Build the successful RedirectURL
        String cancelUrl = "http://localhost/PayPalResult?cancel=true";
        if (orderId != null) {
            cancelUrl += "&orderId=" + orderId;
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", cancelUrl)
                .body(Collections.singletonMap("message", "User canceled the payment."));
    }
}
