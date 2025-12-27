package online_bookstore_management_system;

import java.util.UUID;

public class CreditCardPaymentProcessor implements PaymentProcessor {

    private final PricingService pricingService;
    private final InvoiceService invoiceService;
    private final String cardNumber;
    private final String expiry;
    private final String cvv;

    public CreditCardPaymentProcessor(
            PricingService pricingService,
            InvoiceService invoiceService,
            String cardNumber,
            String expiry,
            String cvv
    ) {
        this.pricingService = pricingService;
        this.invoiceService = invoiceService;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    @Override
    public Payment process(Order order) {
        double amount = pricingService.calculateTotal(order.getItems(), order.getDiscount());

        if (!validateCard(cardNumber, expiry)) {
            return new Payment(
                    PaymentMethod.CREDIT_CARD,
                    amount,
                    false,
                    null
            );
        }

        String txnRef = "TXN-" + Math.abs(UUID.randomUUID().getMostSignificantBits());

        order.markPaid();
        invoiceService.generateInvoice(order);

        try {
            order.getCustomer().saveProfile("customers.log");
        } catch (Exception ignored) {}

        return new Payment(
                PaymentMethod.CREDIT_CARD,
                amount,
                true,
                txnRef
        );
    }

    private boolean validateCard(String cardNumber, String expiry) {
        if (cardNumber == null || expiry == null) return false;
        return cardNumber.length() >= 12 &&
               expiry.contains("/") &&
               (cardNumber.charAt(0) == '4' || cardNumber.charAt(0) == '5');
    }
}

