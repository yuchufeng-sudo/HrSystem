package com.ys.hr;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    private final PayPalConfig payPalConfig;

    @Autowired
    public PayPalService(PayPalConfig payPalConfig) {
        this.payPalConfig = payPalConfig;
    }


public Payment createPayment(
        Double total,
        String currency,
        String method,
        String intent,
        String description,
        String cancelUrl,
        String successUrl)
        throws PayPalRESTException {

    // Set the total payment amount.
    Amount amount = new Amount();
    amount.setCurrency(currency);
    amount.setTotal(String.format("%.2f", total)); // Format to two decimal places.

    // Set up the amount details (subtotal of goods, shipping fees, taxes, etc.).
    Details details = new Details();
    details.setSubtotal(String.format("%.2f", total)); // **Subtotal**
    details.setShipping("0.00"); // Shipping fee
    details.setTax("0.00"); // Tax
    amount.setDetails(details); // Set amount details

    // Set product information.
    ItemList itemList = new ItemList();
    List<Item> items = new ArrayList<>();

    Item item = new Item();
    item.setName("Test Product");
    item.setDescription("Sample product for PayPal payment testing.");
    item.setQuantity("1");
    item.setPrice(String.format("%.2f", total)); // The product price is consistent with the total amount.
    item.setCurrency(currency);
    items.add(item);

    itemList.setItems(items);

    //Set transaction information.
    Transaction transaction = new Transaction();
    transaction.setDescription(description);
    transaction.setAmount(amount);
    transaction.setItemList(itemList); // Set the product list.

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction);

    // Set the payment method.
    Payer payer = new Payer();
    payer.setPaymentMethod(method);

    // Set Redirect URLs
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setCancelUrl(cancelUrl);
    redirectUrls.setReturnUrl(successUrl);

    // Create payment object.
    Payment payment = new Payment();
    payment.setIntent(intent);
    payment.setPayer(payer);
    payment.setTransactions(transactions);
    payment.setRedirectUrls(redirectUrls);

    // Call the API to create a payment.
    return payment.create(payPalConfig.getAPIContext());
}
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        // Call the PayPal API to execute the payment.
        return payment.execute(payPalConfig.getAPIContext(), paymentExecution);
    }

}