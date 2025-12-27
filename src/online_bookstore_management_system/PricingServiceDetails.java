/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.List;

public interface PricingService {
    double calculateTotal(List<OrderItem> items, Discount discount);
}

public class PricingServiceDetails implements PricingService {

    @Override
    public double calculateTotal(List<OrderItem> items, Discount discount) {
        double subtotal = 0.0;
        for (OrderItem item : items) {
            subtotal += item.getSubtotal();
        }

        if (discount != null && discount.isValid()) {
            subtotal -= discount.apply(subtotal);
        }

        return Math.max(subtotal, 0.0);
    }
}


