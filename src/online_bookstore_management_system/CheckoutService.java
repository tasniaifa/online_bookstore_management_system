package online_bookstore_management_system;

import java.util.LinkedHashMap;
import java.util.Map;

public class CheckoutService {

    private final PricingService pricingService;
    private final InvoiceService invoiceService;

    private final Customer customer;

    private final Map<Book, Integer> cartItems = new LinkedHashMap<>();
    private Discount discount;

    public CheckoutService(Customer customer,
                           PricingService pricingService,
                           InvoiceService invoiceService) {
        this.customer = customer;
        this.pricingService = pricingService;
        this.invoiceService = invoiceService;
    }

    /* CART OPERATIONS (Facade) */

    public void addBook(Book book, int quantity) {
        if (quantity <= 0) return;
        cartItems.put(book, cartItems.getOrDefault(book, 0) + quantity);
    }

    public void removeBook(Book book) {
        cartItems.remove(book);
    }

    public void applyDiscount(Discount discount) {
        this.discount = discount;
    }

    /* CHECKOUT OPERATIONS */

    public Payment checkoutWithCard(String cardNumber, String expiry, String cvv) {
        Order order = createOrder();

        PaymentProcessor processor = PaymentAbstractFactory.createCreditCardProcessor(
                        pricingService,
                        invoiceService,
                        cardNumber,
                        expiry,
                        cvv
                );

        return processor.process(order);
    }

    public Payment checkoutWithCash() {
        Order order = createOrder();

        PaymentProcessor processor = PaymentAbstractFactory.createCashProcessor(pricingService);

        return processor.process(order);
    }

    /* INTERNAL SUBSYSTEM LOGIC */

    private Order createOrder() {
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty cart");
        }

        Order order = new Order(customer, pricingService);

        for (Map.Entry<Book, Integer> entry : cartItems.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            
            book.reduceStock(book.getIsbn(), quantity);

            
            order.addItem(new OrderItem(book, quantity));
        }

        if (discount != null) {
            order.applyDiscount(discount);
        }

        cartItems.clear();
        discount = null;

        return order;
    }

    CheckoutService checkout = new CheckoutService(customer, pricingService, invoiceService);

    checkout.addBook(book1, 2);
    checkout.addBook(book2, 1);
    checkout.applyDiscount(discount);

    Payment payment = checkout.checkoutWithCard("4111111111111111", "12/26", "123");

    if (payment.isSuccess()) {
        System.out.println("Order placed successfully!");
    }

}
