package online_bookstore_management_system;

public class PaymentFactory {

    public static PaymentProcessor createCreditCardProcessor(
            PricingService pricingService,
            InvoiceService invoiceService,
            String cardNumber,
            String expiry,
            String cvv
    ) {
        return new CreditCardPaymentProcessor(
                pricingService,
                invoiceService,
                cardNumber,
                expiry,
                cvv
        );
    }

    public static PaymentProcessor createCashProcessor(
            PricingService pricingService
    ) {
        return new CashPaymentProcessor(pricingService);
    }
}
