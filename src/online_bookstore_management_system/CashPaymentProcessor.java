package online_bookstore_management_system;

import java.util.UUID;

public class CashPaymentProcessor implements PaymentProcessor {

    private final PricingService pricingService;

    public CashPaymentProcessor(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public Payment process(Order order) {
        double amount = pricingService.calculateTotal(order.getItems(), order.getDiscount());

        String txnRef = "COD-" + Math.abs(UUID.randomUUID().getMostSignificantBits());
        order.markPaid();

        return new Payment(
                PaymentMethod.CASH,
                amount,
                true,
                txnRef
        );
    }
}

