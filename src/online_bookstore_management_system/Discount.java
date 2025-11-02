package online_bookstore_management_system;

import java.util.Date;

public class Discount {
    private final String code;
    private final int percentage;
    private final Date validUntil;

    public Discount(String code, int percentage, Date validUntil) {
        this.code = code;
        this.percentage = percentage;
        this.validUntil = validUntil;
    }

    public boolean isValid() { 
        return new Date().before(validUntil); 
    }

    public int getPercentage() { 
        return percentage; 
    }

    public double apply(double subtotal) {
        return subtotal * (percentage / 100.0);
    }
}

