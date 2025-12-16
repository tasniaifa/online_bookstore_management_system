package online_bookstore_management_system;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart {
    private final Customer owner;
    private final Map<Book, Integer> items = new LinkedHashMap<>();
    private Discount discount;

    public ShoppingCart(Customer owner) { this.owner = owner; }

    public void addItem(Book book, int qty) { items.put(book, items.getOrDefault(book, 0) + qty); }
    public void removeItem(Book book) { items.remove(book); }
    public void applyDiscount(Discount discount) { this.discount = discount; }

    // checkout now reduces inventory (one place), creates order items, and returns order
    public Order checkout(PricingService pricingService) {
        Order order = new Order(owner, pricingService);
        for (Map.Entry<Book, Integer> e : items.entrySet()) {
            Book book = e.getKey();
            int qty = e.getValue();
            String isbn = book.getIsbn();

            // move inventory mutation here so OrderItem is pure and has no side effects
            book.reduceStock(isbn, qty);

            order.addItem(new OrderItem(book, qty));
        }
        if (discount != null) order.applyDiscount(discount);
        items.clear();
        return order;
    }
}
