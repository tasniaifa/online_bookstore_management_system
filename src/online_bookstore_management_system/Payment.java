package online_bookstore_management_system;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Payment {

    private final String id;
    private double amount;
    private PaymentMethod method;
    private boolean success;
    private String transactionRef;

    private static final List<Payment> RECENT = new CopyOnWriteArrayList<>();

    private final InvoiceService invoiceService;
    private final PricingService pricingService;

    public Payment(InvoiceService invoiceService, PricingService pricingService) {
        this.id = UUID.randomUUID().toString();
        this.invoiceService = invoiceService;
        this.pricingService = pricingService;
    }

    public boolean processCardPayment(Order order, String cardNumber, String expiry, String cvv) {
        this.method = PaymentMethod.CARD;
        this.amount = pricingService.calculateTotal(order.getItems(), order.getDiscount());

        if (!validateCard(cardNumber, expiry)) {
            this.success = false;
            return false;
        }

        this.transactionRef = "TXN-" + Math.abs(UUID.randomUUID().getMostSignificantBits());
        this.success = true;

        // mark order paid and generate invoice; consider extracting these side effects to a higher-level service
        order.markPaid();
        invoiceService.generateInvoice(order);

        // Customer persistence logic probably belongs to another service (ProfileService) — keep for now
        try {
            order.getCustomer().saveProfile("customers.log");
        } catch (Exception e) {
            // swallow file errors here but log ideally
        }

        RECENT.add(this);
        return true;
    }

    public boolean processCashOnDelivery(Order order) {
        this.method = PaymentMethod.COD;
        this.amount = pricingService.calculateTotal(order.getItems(), order.getDiscount());

        this.transactionRef = "COD-" + Math.abs(UUID.randomUUID().getMostSignificantBits());
        this.success = true;
        order.markPaid();

        RECENT.add(this);
        return true;
    }

    private boolean validateCard(String cardNumber, String expiry) {
        if (cardNumber == null || expiry == null) return false;
        return cardNumber.length() >= 12 && expiry.contains("/") &&
               (cardNumber.charAt(0) == '4' || cardNumber.charAt(0) == '5');
    }

    public String getId() { return id; }
    public boolean isSuccess() { return success; }
    public String getTransactionRef() { return transactionRef; }
    public double getAmount() { return amount; }

    // small helper to expose recent payments (read-only copy)
    public static List<Payment> getRecentPayments() {
        return List.copyOf(RECENT);
    }
}
