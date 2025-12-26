package online_bookstore_management_system;

abstract class PaymentFactory {

    abstract PaymentProcessor createCreditCardProcessor(
            PricingService pricingService
    );

    abstract PaymentProcessor createCashProcessor(
            PricingService pricingService
    );
}
