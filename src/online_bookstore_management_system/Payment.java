package online_bookstore_management_system;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Payment {

    private final String id;
    private final PaymentMethod method;
    private final double amount;
    private final boolean success;
    private final String transactionRef;

    private static final List<Payment> RECENT = new CopyOnWriteArrayList<>();

    public Payment(PaymentMethod method, double amount, boolean success, String transactionRef) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.amount = amount;
        this.success = success;
        this.transactionRef = transactionRef;

        RECENT.add(this);
    }

    public String getId() { return id; }
    public PaymentMethod getMethod() { return method; }
    public double getAmount() { return amount; }
    public boolean isSuccess() { return success; }
    public String getTransactionRef() { return transactionRef; }

    public static List<Payment> getRecentPayments() {
        return List.copyOf(RECENT);
    }
}
