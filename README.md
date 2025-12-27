# Introduction

The Online Bookstore was refactored using SOLID principles, cleaning up code smells and transforming tightly-coupled classes into modular, maintainable, and testable components. Core features like pricing, inventory, and payments were moved to dedicated services. Design patterns such as Factory, Abstract Factory, and Facade were used to streamline workflows, while architectural patterns like Client–Server, Layered Architecture, and MVC provide clear separation of concerns, making the system more flexible, scalable, and easier to maintain.

## 1. SOLID-Based Refactoring & Modularization of Core Domain Classes

Refactored core classes and services to enforce SRP, OCP, LSP, ISP, and DIP, transforming tightly-coupled monoliths into modular, testable, and maintainable components.

### Commit 1–6: Initial Setup & Cleanup
- Initialized project structure with core domain classes  
- Added .gitignore to exclude IDE configs and generated outputs  
- Synced local branch with remote  
- Cleaned repository by removing nbproject and invoices directories  

### Commit 7: Update AdminAudit.java
- **Before:** Duplicate logic, inconsistent comments; tight coupling to file I/O.  
- **After:** Removed redundancies; clarified comments; prepared class for SOLID refactor.  
- **Impact:** Improves readability and SRP compliance; easier to apply DIP later.  

### Commit 8: Book Class Refactored
- **Before:** Book handled pricing, inventory, persistence, rendering, logging, analytics → monolithic, high coupling.  
- **After:** Responsibilities split: InventoryService, PricingService, BookRepository, BookRenderer, AnalyticsService.  
- **Impact:** Strong SRP, OCP, ISP, DIP; modular, testable, easier to extend.  

### Commit 9: Review Class Refactored
- **Before:** Review coupled to Book, Customer, storage, popularity logic → hard to modify/test.  
- **After:** Review became immutable; logic moved to ReviewService, PopularityService, PurchaseService, ReviewRepository.  
- **Impact:** SRP and DIP compliance; better separation of concerns, testable design.  

### Commit 10: Admin Class Refactored
- **Before:** Admin handled refund, backup, discount, book operations → God class, multiple responsibilities.  
- **After:** Extracted PaymentService, BackupService, DiscountService, BookService.  
- **Impact:** SRP applied; modular, maintainable, focused responsibilities.  

### Commit 11: Analytics Class Refactored
- **Before:** Analytics mixed logging, formatting, output → tightly coupled, inflexible.  
- **After:** Introduced Logger interface; added ConsoleLogger & FileLogger; delegated logging.  
- **Impact:** SRP, OCP, DIP applied; flexible, testable, and extensible logging.  

### Commit 12: BadReviewStore Class Refactored
- **Before:** Stored reviews + indirect business logic → violated SRP.  
- **After:** Reduced responsibility to storage only; removed business logic coupling.  
- **Impact:** Cohesion improved; SRP compliance achieved.  

### Commit 13: AdminAudit Class Refactored
- **Before:** Direct dependency on FileWriter; hardcoded logging → violates DIP.  
- **After:** Introduced AuditLogger interface; AdminAudit depends on abstraction.  
- **Impact:** DIP & SRP applied; high-level class depends on interface, testable, maintainable.  

### Commit 15: Payment and Order Methods refactored
**SRP: The original**  
Order class handled: Order data storage, Shipping fee calculation, Discount application, Payment status management, Order status transition.  
ShoppingCart handled checkout, persistence, analytics, and file writing  
Payment handled payment processing, invoice generation, and customer updates  
Invoice handled formatting, persistence, email sending, and console printing  
OrderItem modified inventory directly  
This caused high coupling and made changes risky.  

**Responsibilities were separated:**  
- Order class holds order state and delegates calculations  
- PricingService calculator totals and shipping  
- PaymentProcessor processes payment only  
- InvoiceService generates invoices  
- ShippingService handles shipping dispatch  
- OrderItem holds item data  
- Discount represents discount data  

Each class now has one clear reason to change, satisfying SRP.  

**OCP: the Original Code**  
Shipping fee was hardcoded in Order  
Payment methods were hardcoded (CARD, COD)  
Discount logic was embedded directly inside order calculation  
Status progression used string comparisons ("NEW", "SHIPPED")  
Any new rule, new payment method or shipping type, required modifying existing classes, increasing the risk of bugs.  

**Extension is now done via abstractions:**  
- New pricing rules → create a new PricingService  
- New payment methods → implement PaymentProcessor  
- New shipping behavior → implement ShippingService  
- New order states → add new OrderStatus implementations  

New behavior can be added without changing existing code, fulfilling OCP.  

**LSP: the Original Code**  
Order status was represented as strings, making invalid states possible  
No formal contract ensured valid transitions  
Behavior depended on string comparisons, which are error-prone  
This meant that replacing one state behavior with another could easily break logic.  
Order states were modeled using the State pattern. Each concrete status (NewStatus, PaidStatus, etc.) follows the same contract. Any OrderStatus can replace another safely, satisfying LSP.  

**ISP: the Original Code**  
Payment class exposed many unrelated methods  
Invoice mixed invoice creation, persistence, and delivery  
ShoppingCart was forced to know about file writing and analytics  
Classes were dependent on methods they did not logically need.  

Small, focused interfaces were introduced:  
- PricingService calculates order totals  
- PaymentProcessor processes Payment  
- InvoiceService generates invoices  
- ShippingService dispatches shipments  

**DIP: the Original Code**  
Order directly calculated tax and shipping  
Payment directly created invoices  
ShoppingCart directly wrote files  
Concrete implementations were tightly coupled  
This made the system hard to test, replace components and makes it difficult to reuse logic  

Dependencies are now injected via interfaces. High-level classes depend on abstractions, not concrete implementations. This enables loose coupling and easy replacement of components, satisfying DIP.  

### Commit 16: Update Book.java
- **Before:**  
Book class contained domain data + inventory, pricing, rendering, logging, analytics logic → violated SRP.  
High-level orchestration mixed inside entity → tight coupling, hard to test, hard to extend.  

- **After:**  
Book now purely a domain entity (SRP).  
Separated responsibilities into:  
- InventoryService → stock management  
- PricingService → price & discount calculations  
- BookRenderer → presentation (HTML/Text)  
- BookService → high-level orchestration/delegation  
- BookLogger & AnalyticsService → logging & analytics  

BookService delegates tasks, no heavy logic inside the entity.  

**Impact:**  
- SRP: Book handles only domain state.  
- OCP: New pricing, rendering, analytics behaviors can be added via service implementations.  
- ISP: Clients depend only on interfaces they use (InventoryService, PricingService, etc.).  
- DIP: BookService depends on abstractions, not concrete implementations.  
Modular, testable, maintainable, and fully SOLID-compliant design for the Book domain.  

### Commit 17–19, 21: Refactored Publisher
**Before:** Publisher handled data, book list, inventory updates, revenue tracking, and analytics → high coupling, multiple reasons to change.  
**After:** Responsibilities separated:  
- Publisher → data + book list  
- InventoryService → stock management  
- RevenueService → revenue tracking  
- AnalyticsService → events recording  
- Interfaces introduced; services injected.  
**Impact:** SRP, ISP, DIP applied; loose coupling, modular, testable, maintainable.  

### Commit 20: Refactored Customer
**Before:** Customer handled data, persistence, blacklisting, payment preferences, analytics → tightly coupled, difficult to extend.  
**After:** Responsibilities separated:  
- Customer → data + orders  
- CustomerPersistence → data saving  
- CustomerReviewService → blacklisting/review  
- PaymentPreferenceService → manages payment  
- CustomerAnalyticsService → analytics  
- Interfaces define behavior; services injected.  
**Impact:** SRP, OCP, LSP, ISP, DIP applied; high cohesion, extensible, testable.  

### Commit 22: Refactored Category
**Before:** Category stored metadata, handled search, maintained cache, and input logic → multiple reasons to change, tightly coupled.  
**After:**  
- Category → metadata + books only  
- CategorySearchService → search logic  
- Cache moved outside, injected via interface  
**Impact:** SRP, OCP, LSP, ISP, DIP applied; God class removed, flexible search implementations.  

### Commit 23: Refactored OrderItem
**Before:** OrderItem stored snapshot data + updated book stock → mixed responsibilities, tightly coupled to Book.  
**After:**  
- OrderItem → immutable order snapshot  
- InventoryService → handles stock updates  
- OrderItemEnrichment → additional item info  
- Interfaces used; high-level logic depends on abstractions.  
**Impact:** SRP, OCP, LSP, ISP, DIP applied; high cohesion, low coupling, easily extensible and testable.  

## 2. Code Smells Refactoring & Factory Pattern Implementation
Refactored God classes, long methods, naming issues, duplicated code, and feature envy in Category, Customer, OrderItem, and Payment classes; applied Factory Pattern for Customer service instantiation to improve SRP, DIP, and overall maintainability.  

### Commit 24–25: Refactored Customer, OrderItem, and Category to Fix Code Smells and Remove God Class
**OrderItem Class**  
- SRP Violation: Moved inventory update logic to InventoryService; OrderItem now stores only immutable snapshot data.  
- Inappropriate Naming: Renamed priceAtOrder → priceSnapshot for clarity.  

**Category Class**  
- God Class / SRP: Limited Category to metadata and book relationships; search/filter/display logic moved to services.  
- Feature Envy: Book-related behavior moved closer to domain; improved cohesion and encapsulation.  

**Customer Class**  
- Long Methods & Tight Coupling: Split into focused helpers; introduced service abstractions and dependency injection (DIP).  
- Naming: Methods renamed (updateProfile(), placeOrder(), viewOrderHistory()) for clear intent.  

### Commit 26: Payment Classes Code Smell Refactoring
- Naming: Renamed unclear/abbreviated fields and methods (recalcTotal() → recalculateTotal(), priceAtOrder → priceSnapshot, service classes renamed to match responsibility).  
- Long Methods: Split large methods (e.g., processCardPayment()) into smaller helpers; moved side effects to dedicated services.  
- Duplicated Code: Centralized pricing logic in DefaultPricingService; unified payment finalization.  
- Oddball/Hacky Code: Removed reflection hack for discount access; exposed via Order.getDiscount() and passed explicitly to services.  
- Large Class: Pushed multiple responsibilities into PricingService, InvoiceService, ShippingService; Order and Payment now have focused roles.  
- Feature Envy: Inventory mutation moved to ShoppingCart.checkout(); customer persistence handled in higher-level service to reduce coupling.  
**Impact:** SRP, DIP applied; improved readability, maintainability, testability, and domain-encapsulation; classes now cohesive and loosely coupled.  

## 3. Design Pattern Applied Across System
### Commit 27: Factory Pattern Applied in Payment class**  
Payment processing was reorganized using the Factory Pattern. CheckoutService now creates orders and delegates payment to CreditCardPaymentProcessor or CashPaymentProcessor via PaymentFactory. This makes the code cleaner, easier to test, and more flexible. ShoppingCart was removed as its responsibilities moved to CheckoutService.  

### Commit 28: Introduce BookstoreFacade to simplify main workflow**  
Refactored Online_bookstore_management_system to use BookstoreFacade, removing global scanner dependency and collapsing the large, tightly coupled main method. All entity creation, shopping cart operations, checkout, payment processing, and invoice generation are now delegated to the facade, enforcing SRP, reducing coupling, and improving maintainability and testability.  

### Commit 29: Simplify main class using BookstoreFacade**  
Added BookstoreFacade to centralize entity creation, shopping, checkout, payment processing, and invoice/admin operations. The facade abstracts complex workflows from main, reducing coupling, enforcing SRP, and improving readability, testability, and maintainability. Includes runDemoFacade() for demo execution without direct class-level orchestration.  

### Commit 30-32: Abstract Factory Pattern and Facade Pattern Implemented**  
- Abstract Factory in Payment Class: The system supports multiple payment methods such as credit card, cash, PayPal, and bank transfer. Each payment method requires different validation rules, processing logic, and side effects (e.g., invoicing, transaction references). Embedding all of this logic inside a single class would lead to tight coupling and poor scalability.  
- How it fits the system design:  
  - A payment factory is responsible for creating appropriate payment processors based on the selected payment method.  
  - Each concrete payment processor encapsulates the logic for a specific payment type.  
  - The client does not need to know how a payment is processed internally.  

- Facade in Checkout Service class: The checkout process involves multiple subsystems, including: Cart management, Order creation, Inventory updates, Pricing and discount application, Payment processing, Invoice generation  
- How it fits the system design: The Checkout Service acts as a Facade by coordinating all checkout-related operations internally while exposing only a small set of high-level methods to the client. The client interacts with the system through simple operations such as adding items and completing payment, without needing to understand how these actions are implemented internally.  

### Commit 34, 35: Factory design Pattern Applied**  
- Factory Pattern: The Customer class no longer directly instantiates service implementations (new).  
- Instead, a CustomerServiceFactory creates and provides the required services.  
- Handles service object creation, removing new() from the Customer class and improving extensibility.  

## 4. Architectural pattern application (proposed)
### Commit 33: designed the system using architectural pattern**  
The proposed design uses the following architectural patterns:  
- Client–Server Architecture (defines interaction between users and the system)  
- Layered Architecture (structures the server internally)  
- Model–View–Controller (MVC) (organizes responsibility within the upper layers)  
These patterns complement each other and are not applied at the same level of abstraction.  

#### 1. Client–Server Architecture
The Client–Server pattern defines the overall system boundary.  
- Clients: Admin and Customer  
- Server: Online Bookstore Management System  
Users interact with the system by sending requests, while the server centrally processes business logic and manages data. This approach reflects real-world online systems and provides a clear separation between users and system logic.  

#### 2. Layered Architecture
The internal structure of the server is organized using a Layered Architecture, ensuring separation of concerns and controlled dependencies.  

**Layers:**  
- Presentation Layer: Handles user interaction and system input/output.  
  - Example: Online_bookstore_management_system  
- Business Logic Layer: Contains core workflows and rules.  
  - Example Classes: CheckoutService, PricingServiceDetails, ShippingServiceDetails, InvoiceServiceDetails, Analytics  
- Domain / Data Layer: Represents core entities and data models.  
  - Example Classes: Book, Order, OrderItem, Customer, Review, Publisher  

This structure improves readability, maintainability, and scalability of the system.  

#### 3. Model–View–Controller (MVC)
Within the layered architecture, the MVC pattern is applied to organize user interaction and control flow.  
- Model: Domain classes such as Book, Order, Customer, and Review  
- View: Console-based user interface implemented in the main application class  
- Controller: Classes handling user requests and workflows, including:  
  - UserAuthentication  
  - CheckoutService  
  - PaymentProcessor  

MVC separates data, control logic, and user interaction, making the system easier to modify and extend.  

**Conclusion:** The system is now cleaner, more flexible, and easier to test and scale.
