# Object-Oriented Design 
### - Learning to program with design patterns.

## The Project
Developing one single object-oriented program; Point Of Sale.
This project is based on assignments for a course in 'Object-Oriented Design' given at KTH Royal Institute of Technology in Stockholm.

## Content and learning outcomes
### Course contents
Examples of fields that are treated:
* Object-oriented design and design pattern  
* Architecture and architectural patterns  
* Guidelines for object-oriented programming for example refactoring and unit testing
* UML (Unified Modeling Language)  
* Object-oriented analysis
### Intended learning outcome
One shall be able to 
* develop an object-oriented programme by applying established guidelines for object-oriented architecture, design and programming
* discuss the quality of a programme and then refer to established guidelines for object-oriented architecture, design and programming
* interpret and clarify a given specification by applying established guidelines for analysis. 
# Output Samples
## Basic Flow
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_basic_flow.gif)

## Alternative Flow - Checked Exception, Business Logic 
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_alternative_flows3a.gif)

## Alternative Flow - Same Item Identification Number.
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_alternative_flows3b.gif)

## Alternative Flow - Multiple Items of The Same Goods
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_alternative_flows3c.gif)

## Alternative Flow - Customer eligible for discount
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_alternative_flows9a.gif)

## Alternative Flow - Unchecked Exception 
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/samples/peek_exceptions_unchecked.gif)

# UML
## Class Diagrams (CD)
### CD
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_cd.png)

### CD, DTO
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_cd_dto.png)

### Ref - CD, Integration
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_cd_ref_integration.png)

### Ref - CD, Pricing
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_cd_ref_pricing.png)

## Sequence Diagrams (SD)
### SD, Start up, Main System Operation
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_start_up.png)

### SD, Start up, Main System Operation
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_start_up.png)

### SD, Start Sale System Operation
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_start_sale.png)

### SD, Register Item System Operation
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_register_item.png)

### Ref - SD, Item not found
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_ref_item_not_found.png)

### Ref - SD, Notify Sale Observers
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_ref_notify_sale_observers.png)

### Ref - SD, Notify Users and Developers
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_ref_notify_user_developer.png)

### SD, End of Sale
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_end_of_sale.png)

### SD, Register Customer to Sale
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_register_customer.png)

### SD, Payment System Operation
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_pay.png)

### Ref - SD, Notify Cash Register Observer
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_sd_ref_notify_cash_register.png)

## Task 1 - Inheritance, refactoring to template method  
![](https://github.com/VincentFerrigan/kth-iv1350-object-oriented-design/blob/main/output/uml/uml_adp_tasks.png)
Since the observers perform similar steps in the same order,
the Template Method design-pattern can be applied. This UML-example is an extract from the main class diagram.
