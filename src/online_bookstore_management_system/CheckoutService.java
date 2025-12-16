package online_bookstore_management_system;

import java.util.LinkedHashMap;
import java.util.Map;

public class CheckoutService {

    private final Customer owner;
    private final PricingService pricingService;
    private final InvoiceService invoiceService;

    private final Map<Book, Integer> items = new LinkedHashMap<>();
    private Discount discount;

    public CheckoutService(Customer owner,
                           PricingService pricingService,
                           InvoiceService invoiceService) {
        this.owner = owner;
        this.pricingService = pricingService;
        this.invoiceService = invoiceService;
    }

    // SHOPPING CART FUNCTIONS
    

    public void addItem(Book book, int qty) {
        if (qty <= 0) return;
        items.put(book, items.getOrDefault(book, 0) + qty);
    }

    public void removeItem(Book book) {
        items.remove(book);
    }

    public void applyDiscount(Discount discount) {
        this.discount = discount;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private Order createOrder() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with empty cart");
        }

        Order order = new Order(owner, pricingService);

        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            Book book = entry.getKey();
            int qty = entry.getValue();

            // inventory update
            book.reduceStock(book.getIsbn(), qty);

            // order item creation
            order.addItem(new OrderItem(book, qty));
        }

        if (discount != null) {
            order.applyDiscount(discount);
        }

        items.clear();
        discount = null;

        return order;
    }

    //PAYMENT (FACTORY METHOD) 

    public Payment checkoutAndPayByCard(
            String cardNumber,
            String expiry,
            String cvv
    ) {
        Order order = createOrder();

        PaymentProcessor processor =
                PaymentFactory.createCreditCardProcessor(
                        pricingService,
                        invoiceService,
                        cardNumber,
                        expiry,
                        cvv
                );

        return processor.process(order);
    }

    public Payment checkoutAndPayByCash() {
        Order order = createOrder();

        PaymentProcessor processor =
                PaymentFactory.createCashProcessor(pricingService);

        return processor.process(order);
    }
}

