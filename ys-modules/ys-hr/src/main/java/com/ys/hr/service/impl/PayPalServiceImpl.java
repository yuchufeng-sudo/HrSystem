package com.ys.hr.service.impl;

import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;
import com.ys.hr.PayPalClient;
import com.ys.hr.domain.HrOrderDetail;
import com.ys.hr.domain.vo.EnterpriseVo;
import com.ys.hr.service.EnterpriseService;
import com.ys.hr.service.IHrOrderDetailService;
import com.ys.hr.service.PayPalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
public class PayPalServiceImpl implements PayPalService {

    private static final Logger log = LoggerFactory.getLogger(PayPalServiceImpl.class);

    private final String clientId = "AWO79hd3gEJoFODR25LF_ZkFRsVlk_eEjRDYQAJamR50ERwGnH0zev72XVV1HJHRD-E6uRtceUHn7C0R";
    private final String clientSecret = "EHI-vPa74X8wnG4rbUIgL_TIQYeQkVd436e7JnPJjIvtBWvNEHRLcxTXyj-0oE17-z6gxZbPeFhaScEo";
    private final String mode = "sandbox";

    public static final String CAPTURE = "CAPTURE";
    public static final String BRANDNAME = "Supernote";
    public static final String LANDINGPAGE = "NO_PREFERENCE";
    public static final String USERACTION = "PAY_NOW";
    public static final String SHIPPINGPREFERENCE = "GET_FROM_FILE";

    private final PayPalClient payPalClient;

    @Autowired
    public PayPalServiceImpl(PayPalClient payPalClient) {
        this.payPalClient = payPalClient;
    }
    
    @Autowired
    private IHrOrderDetailService hrOrderDetailService;

    @Autowired
    private EnterpriseService enterpriseService;
//    @Override
//    @Transactional
//    public HrOrderDetail createOrder(HrOrderDetail orderDetail) throws IOException {
//        OrderRequest orderRequest = buildRequestBody(orderDetail);
//        OrdersCreateRequest request = new OrdersCreateRequest();
//        request.header("prefer", "return=representation");
//        request.requestBody(orderRequest);
//
//        try {
//            HttpResponse<Order> response = payPalClient.client(mode, clientId, clientSecret).execute(request);
//            EnterpriseVo enterpriseById = enterpriseService.getEnterpriseById(orderDetail.getEnterpriseId());
//            HrOrderDetail hrOrderDetail = hrOrderDetailService.selectHrOrderDetailByOrderId(enterpriseById.getPlanOrderId());
//
//            if (response.statusCode() == 201) {
//               
//
//                // 
//                orderDetail.setPaypalOrderId(response.result().id());
//
//                // 
//                if (response.result().purchaseUnits() != null && !response.result().purchaseUnits().isEmpty()) {
//                    AmountWithBreakdown amount = response.result().purchaseUnits().get(0).amountWithBreakdown();
//                    if (amount != null) {
//                        orderDetail.setOrderPrice(Double.parseDouble(amount.value()));
//                    }
//                }
//
//                // 
//                orderDetail.setOrderState(1L); // 
//                orderDetail.setPlanTime(orderDetail.getPlanTime());
////              
////                orderDetail.setPurchaseTime(new Date());
//
//               
//                String approveUrl = "";
//                for (LinkDescription link : response.result().links()) {
//                    if(link.rel().equals("approve")) {
//                        approveUrl = link.href();
//                    }
//                }
//                orderDetail.setApproveUrl(approveUrl);
//              
////                saveOrderDetail(orderDetail);
//                hrOrderDetailService.insertHrOrderDetail(orderDetail);
//                
//                return orderDetail;
//            } else {
//                log.error("PayPal，: {}", response.statusCode());
//                throw new IOException("Failed to create PayPal order: " + response.statusCode());
//            }
//        } catch (HttpException e) {
//            log.error("PayPal : {}", e.getMessage());
//            throw new IOException("PayPal API error: " + e.getMessage(), e);
//        }
//    }

    @Override
    @Transactional
    public HrOrderDetail createOrder(HrOrderDetail orderDetail) throws IOException {

        EnterpriseVo enterpriseById = enterpriseService.getEnterpriseById(orderDetail.getEnterpriseId());


        boolean isPriceAdjustment = false;
//        boolean b = orderDetail.getPlanId() == enterpriseById.getPlanId();
        boolean b = orderDetail.getPlanId().equals(enterpriseById.getPlanId());
        if (enterpriseById.getPlanOrderId() != null && enterpriseById.getIsPlan() == 1 && b == false) {
            HrOrderDetail hrOrderDetail = hrOrderDetailService.selectHrOrderDetailByPlanOrderId(enterpriseById.getPlanOrderId());
            if (hrOrderDetail != null ) {

                double originalOrderPrice = hrOrderDetail.getOrderPrice();
                double originalPlanTime = hrOrderDetail.getPlanTime();
                double enterprisePlanTimeNums = enterpriseById.getPlanTimeNums();
                double newOrderPrice = orderDetail.getOrderPrice();
                double transmittedPlanTime = orderDetail.getPlanTime();


                double priceDifference = ((newOrderPrice/hrOrderDetail.getPlanTime())*enterprisePlanTimeNums) - (originalOrderPrice - ((originalPlanTime - enterprisePlanTimeNums) / originalPlanTime * originalOrderPrice));

                priceDifference = Math.round(priceDifference * 100.0) / 100.0;
                orderDetail.setOrderPrice(priceDifference);


                double updatedPlanTime = transmittedPlanTime - (originalPlanTime - enterprisePlanTimeNums);
                orderDetail.setPlanTime((long) updatedPlanTime);

                isPriceAdjustment = true;
            }
        }
        OrderRequest orderRequest = buildRequestBody(orderDetail);
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.header("prefer", "return=representation");
        request.requestBody(orderRequest);

        try {
            HttpResponse<Order> response = payPalClient.client(mode, clientId, clientSecret).execute(request);

            if (response.statusCode() == 201) {
                log.info("The PayPal order has been successfully created. Order ID:{}", response.result().id());
                orderDetail.setPaypalOrderId(response.result().id());


                if (!isPriceAdjustment) {
                    if (response.result().purchaseUnits() != null && !response.result().purchaseUnits().isEmpty()) {
                        AmountWithBreakdown amount = response.result().purchaseUnits().get(0).amountWithBreakdown();
                        if (amount != null) {

                            double paypalAmount = Double.parseDouble(amount.value());
                            if (Math.abs(paypalAmount - orderDetail.getOrderPrice()) > 0.01) {
                                log.warn("The amount returned by PayPal does not match the requested amount: Request = {}, Return = {}.",
                                        orderDetail.getOrderPrice(), paypalAmount);
                            }
                            orderDetail.setOrderPrice(paypalAmount);
                        }
                    }
                }


                orderDetail.setOrderState(1L);
                String approveUrl = response.result().links().stream()
                        .filter(link -> "approve".equals(link.rel()))
                        .findFirst()
                        .map(LinkDescription::href)
                        .orElse("");
                orderDetail.setApproveUrl(approveUrl);
                orderDetail.setIsRenew(1L);

                hrOrderDetailService.insertHrOrderDetail(orderDetail);

                return orderDetail;
            } else {
                log.error("PayPal order creation failed, status code:\r\n" + //
                                        " {}", response.statusCode());
                throw new IOException("Failed to create PayPal order: " + response.statusCode());
            }
        } catch (HttpException e) {
            log.error("PayPal API call exception: {}", e.getMessage());
            throw new IOException("PayPal API error: " + e.getMessage(), e);
        }
    }
    @Override
    @Transactional
    public HrOrderDetail captureOrder(HrOrderDetail orderDetail) throws IOException {

    String paypalOrderId = orderDetail.getPaypalOrderId();
    if (paypalOrderId == null || paypalOrderId.isEmpty()) {
        log.error("The PayPal order ID is empty, so the capture operation cannot be performed");
        throw new IOException("The PayPal order ID is empty");
    }

    OrdersCaptureRequest request = new OrdersCaptureRequest(paypalOrderId);
    request.requestBody(new OrderRequest());

    try {
        HttpResponse<Order> response = payPalClient.client(mode, clientId, clientSecret).execute(request);
        log.info("PayPal order capture successful, Order ID: {}", paypalOrderId);

        if (response.statusCode() == 201) {
            
            HrOrderDetail updatedOrderDetail = new HrOrderDetail();
            updatedOrderDetail.setOrderId(orderDetail.getOrderId());
            updatedOrderDetail.setPaypalOrderId(paypalOrderId); 

           
            if (response.result().purchaseUnits() != null && !response.result().purchaseUnits().isEmpty()) {
                AmountWithBreakdown amount = response.result().purchaseUnits().get(0).amountWithBreakdown();
                if (amount != null) {
                    updatedOrderDetail.setOrderPrice(Double.parseDouble(amount.value()));
                }
            }

            
            String status = response.result().status();
            if ("COMPLETED".equalsIgnoreCase(status)) {
                updatedOrderDetail.setOrderState(2L); 
                
                EnterpriseVo enterpriseById = enterpriseService.getEnterpriseById(orderDetail.getEnterpriseId());
            
                enterpriseById.setPlanOrderId(orderDetail.getPaypalOrderId());
      
                long planTimeMillis = orderDetail.getPlanTime() * 24L * 60 * 60 * 1000;
                Date newEndTime;

                if (enterpriseById.getIsPlan() == 1) {
      
                    if (orderDetail.getIsRenew() == 1) {
                      
                        enterpriseById.setPlanId(orderDetail.getPlanId());

           
                        newEndTime = new Date(System.currentTimeMillis() + planTimeMillis);
                        enterpriseById.setPlanEndTime(newEndTime);

                 
                        enterpriseById.setPlanTimeNums(orderDetail.getPlanTime());

                        log.info("Enterprise subscription upgrade: New plan ID={}, New expiration date={}, New days of use={}",
                                orderDetail.getPlanId(), newEndTime, orderDetail.getPlanTime());
                    } else {
                  
                        if (enterpriseById.getPlanEndTime() != null) {
                     
                            newEndTime = new Date(enterpriseById.getPlanEndTime().getTime() + planTimeMillis);
                        } else {
                          
                            newEndTime = new Date(System.currentTimeMillis() + planTimeMillis);
                        }
                        enterpriseById.setPlanEndTime(newEndTime);

                 
                        enterpriseById.setPlanTimeNums(enterpriseById.getPlanTimeNums() + orderDetail.getPlanTime());

                        log.info("Enterprise subscription renewal: Original expiration date={}, New expiration date={}, Added days={}",
                                enterpriseById.getPlanEndTime(), newEndTime, orderDetail.getPlanTime());
                    }
                } else {
                   
                    enterpriseById.setIsPlan(1L);
                    enterpriseById.setPlanId(orderDetail.getPlanId());

                    newEndTime = new Date(System.currentTimeMillis() + planTimeMillis);
                    enterpriseById.setPlanEndTime(newEndTime);
                    enterpriseById.setPlanTimeNums(orderDetail.getPlanTime());
                }

                enterpriseService.updateEnterprise(enterpriseById.getId(), enterpriseById);

            } else {
                updatedOrderDetail.setOrderState(3L);
            }
            updatedOrderDetail.setPurchaseTime(new Date());
      
            hrOrderDetailService.updateHrOrderDetail(updatedOrderDetail);

            return updatedOrderDetail;
        } else {
            log.error("PayPal order capture failed, status code: {}", response.statusCode());
            throw new IOException("Failed to capture PayPal order: " + response.statusCode());
        }
    } catch (HttpException e) {
        log.error("PayPal order capture exception, error: {}", e.getMessage());
        throw new IOException("Failed to capture PayPal order: " + e.getMessage(), e);
    }
}

    /**
     * 
     */
    private OrderRequest buildRequestBody(HrOrderDetail orderDetail) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(CAPTURE);

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName(BRANDNAME)
                .landingPage(LANDINGPAGE)
                .cancelUrl("http://xz-ai.info:9084/prod-api/hr/paypal/cancel")
                .returnUrl("http://xz-ai.info:9084/prod-api/hr/paypal/success")
                .userAction(USERACTION)
                .shippingPreference(SHIPPINGPREFERENCE);
        orderRequest.applicationContext(applicationContext);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();

      
        List<Item> items = new ArrayList<>();
        Item item = new Item()
                .name("Subscription Plan")
                .description("Monthly subscription service")
                .unitAmount(new Money()
                        .currencyCode("USD")
                        .value(String.format("%.2f", orderDetail.getOrderPrice())))
                .quantity("1");

        items.add(item);

 
        String totalAmountStr = String.format("%.2f", orderDetail.getOrderPrice());

        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .description("Subscription payment")
                .customId(orderDetail.getOrderId())
                .invoiceId(orderDetail.getOrderId())
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(totalAmountStr)
                        .amountBreakdown(new AmountBreakdown()
                                .itemTotal(new Money().currencyCode("USD").value(totalAmountStr))
                                .shipping(new Money().currencyCode("USD").value("0.00"))
                                .handling(new Money().currencyCode("USD").value("0.00"))
                                .taxTotal(new Money().currencyCode("USD").value("0.00"))
                                .shippingDiscount(new Money().currencyCode("USD").value("0.00"))))
                .items(items);

        
        if (orderDetail.getEnterpriseId() != null && !orderDetail.getEnterpriseId().isEmpty()) {
            purchaseUnitRequest.shippingDetail(new ShippingDetail()
                    .name(new Name().fullName(orderDetail.getEnterpriseId()))
                    .addressPortable(new AddressPortable()
                            .addressLine1("123 Main St")
                            .adminArea2("San Jose")
                            .adminArea1("CA")
                            .postalCode("95131")
                            .countryCode("US")));
        }

        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);

        return orderRequest;
    }
}