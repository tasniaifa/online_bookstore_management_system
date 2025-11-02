/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

public class OrderStatus {

    public enum StatusType {
        NEW,
        PAID,
        PROCESSING,
        SHIPPED,
        DELIVERED
    }

    private StatusType currentStatus;

    public OrderStatus() {
        this.currentStatus = StatusType.NEW;
    }

    
    public OrderStatus(StatusType type) {
        this.currentStatus = type;
    }

    public String getName() {
        return currentStatus.name();
    }

    public OrderStatus nextStatus() {
        switch (currentStatus) {
            case NEW:
                currentStatus = StatusType.PROCESSING;
                break;
            case PAID:
                currentStatus = StatusType.PROCESSING;
                break;
            case PROCESSING:
                currentStatus = StatusType.SHIPPED;
                break;
            case SHIPPED:
                currentStatus = StatusType.DELIVERED;
                break;
            case DELIVERED:
                // no change — final stage
                break;
        }
        return this;
    }

    // helper constructors for backward compatibility
    public static class NewStatus extends OrderStatus {
        public NewStatus() {
            super(StatusType.NEW);
        }
    }

    public static class PaidStatus extends OrderStatus {
        public PaidStatus() {
            super(StatusType.PAID);
        }
    }
}

