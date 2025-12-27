package online_bookstore_management_system;

public interface PaymentProcessor {
    Payment process(Order order);
}
