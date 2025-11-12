package com.ys.hr.controller;

import com.ys.common.core.aspect.RateLimit;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.hr.domain.HrOrderDetail;
import com.ys.hr.service.IHrOrderDetailService;
import com.ys.hr.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Order Detail Controller
 *
 * @author ys
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/orderDetail")
public class HrOrderDetailController extends BaseController
{
    @Autowired
    private IHrOrderDetailService hrOrderDetailService;


    @Autowired
    private PayPalService payPalService;
    /**
     * Query Order Detail list
     */

    @GetMapping("/list")
    public TableDataInfo list(HrOrderDetail hrOrderDetail)
    {
        startPage();
        List<HrOrderDetail> list = hrOrderDetailService.selectHrOrderDetailList(hrOrderDetail);
        return getDataTable(list);
    }

    /**
     * Export Order Detail list
     */

    @Log(title = "Order Detail", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrOrderDetail hrOrderDetail)
    {
        List<HrOrderDetail> list = hrOrderDetailService.selectHrOrderDetailList(hrOrderDetail);
        ExcelUtil<HrOrderDetail> util = new ExcelUtil<HrOrderDetail>(HrOrderDetail.class);
        util.exportExcel(response, list, "Order Detail Data");
    }

    /**
     * Get Order Detail details
     */
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(hrOrderDetailService.selectHrOrderDetailByOrderId(orderId));
    }

    /**
     * Add Order Detail
     */

    @Log(title = "Order Detail", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrOrderDetail hrOrderDetail) {
        return toAjax(hrOrderDetailService.insertHrOrderDetail(hrOrderDetail));
    }

    /**
     * Update Order Detail
     */

    @Log(title = "Order Detail", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrOrderDetail hrOrderDetail) {
        return toAjax(hrOrderDetailService.updateHrOrderDetail(hrOrderDetail));
    }

    /**
     * Delete Order Detail
     */

    @Log(title = "Order Detail", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds) {
        return toAjax(hrOrderDetailService.removeByIds(Arrays.asList(orderIds)));
    }



    /**
     * Create a PayPal order.
     */
    @RateLimit(limit = 5, prefix = "paypal:create_order")
    @PostMapping("/createOrder")
    public AjaxResult createOrder(@RequestBody HrOrderDetail orderDetail) {
        try {
            HrOrderDetail result = payPalService.createOrder(orderDetail);
            return AjaxResult.success("The order was created successfully.", result);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error("The order creation failed.: " + e.getMessage());
        }
    }

    /**
     * Capture PayPal order (payment completed)
     */
    @PostMapping("/captureOrder")
    public AjaxResult captureOrder(@RequestBody HrOrderDetail orderDetail) {
        try {
            // Invoke the service layer for order capture.
            HrOrderDetail result = payPalService.captureOrder(orderDetail);

            if (result.getOrderState() == 2L) {
                return AjaxResult.success("Payment succeeded", result);
            } else {
                return AjaxResult.error("Payment is processing, status.: " + result.getOrderState());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error("Payment processing failed: " + e.getMessage());
        }
    }
    /**
     *
     */
//    @PostMapping("/success")
//    public AjaxResult paymentSuccess(@RequestParam String orderId) {
//        try {
//            return captureOrder(orderId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return AjaxResult.error(": " + e.getMessage());
//        }
//    }
//
//    /**
//     *
//     */
//    @PostMapping("/cancel")
//    public AjaxResult paymentCancel(@RequestParam String orderId) {
//        //
//        HrOrderDetail orderDetail = new HrOrderDetail();
//        orderDetail.setOrderId(orderId);
//        return AjaxResult.success("", orderId);
//    }
}
