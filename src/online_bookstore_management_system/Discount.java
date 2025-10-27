package online_bookstore_management_system;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Discount {
    private String code;
    private int percentage;
    private Date validUntil;
    private int usesLeft;
    private String campaignName;

    public Discount(String code, int percentage) {
        this.code = code;
        this.percentage = percentage;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        this.validUntil = c.getTime();
        this.usesLeft = 1000;
        this.campaignName = "generic";
    }

    public static Discount readFromInput(Scanner sc) {
        System.out.print("Discount code: ");
        String code = sc.nextLine();
        System.out.print("Percentage (int): ");
        int pct = Integer.parseInt(sc.nextLine());
        Discount d = new Discount(code, pct);
        System.out.print("Campaign name (optional): ");
        String camp = sc.nextLine();
        if (!camp.isBlank()) d.campaignName = camp;
        return d;
    }

    public boolean isValid() {
        return new Date().before(validUntil) && usesLeft > 0;
    }

    public void consume() {
        usesLeft = Math.max(0, usesLeft - 1);
    }

    public String getCode() { return code; }
    public int getPercentage() { return percentage; }
    public String getCampaignName() { return campaignName; }
}
