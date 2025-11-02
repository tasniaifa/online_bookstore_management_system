/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.Date;
import java.util.Random;

public interface ShippingService {
    void dispatch(Order order);
}

public class ShippingServiceDetails implements ShippingService {

    private ShippingServiceDetails(String name, String email, String default_address) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    @Override
public void dispatch(Order order) {
    ShippingServiceDetails sd = order.getShippingDetails();
    if (sd == null) {
        sd = new ShippingServiceDetails(order.getCustomer().getName(), order.getCustomer().getEmail(), "default address");
        order.setShippingDetails(sd);
    }

    
    System.out.println("Order dispatched for " + order.getId() + " via LocalShip-" + new Random().nextInt(1000));
}
}

