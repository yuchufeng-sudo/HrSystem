package com.ys.hr.service;

import com.ys.hr.domain.HrOrderDetail;

import java.io.IOException;

/**
 * PayPal payment service interface
 */
public interface PayPalService {

    /**
     * Create a PayPal order.
     */
    HrOrderDetail createOrder(HrOrderDetail orderDetail) throws IOException;

    HrOrderDetail captureOrder(HrOrderDetail orderDetail) throws IOException;

    /**
     * Capture PayPal order (payment completed)
     */
//    HrOrderDetail captureOrder(String orderId) throws IOException;

}
