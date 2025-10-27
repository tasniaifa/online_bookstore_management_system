/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author tasniafarinifa
 */
public class ShippingDetails {
    private String recipientName;
    private String recipientEmail;
    private String address;
    private String phone;
    private String trackingNumber;
    private String carrier;
    private Date dispatchedAt;

    public ShippingDetails(String recipientName, String recipientEmail, String address) {
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.address = address;
        this.trackingNumber = "TRK-" + new Random().nextInt(1000000);
        this.carrier = "LocalShip";
        this.dispatchedAt = null;
    }

    public void dispatch(String carrierOverride) {
        this.carrier = carrierOverride == null ? this.carrier : carrierOverride;
        this.dispatchedAt = new Date();
        System.out.println("Dispatched via " + this.carrier + " tracking=" + this.trackingNumber);
    }

    public String getTrackingNumber() { return trackingNumber; }
}
    

