package online_bookstore_management_system;

abstract class PaymentAbstractFactory implements PaymentProcessor{

    abstract PaymentProcessor createCreditCardProcessor(
            PricingService pricingService
    );

    abstract PaymentProcessor createCashProcessor(
            PricingService pricingService
    );
}
