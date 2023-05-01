package src.se.kth.iv1350.view;

import src.se.kth.iv1350.controller.Controller;
import src.se.kth.iv1350.model.Amount;

import java.util.Scanner;

public class View {
    private Controller contr;

    Scanner input = new Scanner(System.in);

    // 1. En med prompt
    // 2. Fil med itemID och ev. quantity
    // 3. Hårdkodade

// 3.
    public void testPromt() {
        System.out.println("Welcome Veggie Shop \n");
        contr.startSale();
        System.out.println("New buy: \n Add items to the shoppingcart by typing (ID Quantity)\n");
        contr.registerItem(input.nextInt(),input.nextInt());


        String answer = "y";
        while (answer.equalsIgnoreCase("y")) {
            System.out.println("\nDo you want to add more items (y/n) ");
            answer = input.next();

            if (answer.equalsIgnoreCase("y")) {
                System.out.println("Enter next item (ID Quantity)\n");
                contr.registerItem(input.nextInt(),input.nextInt());
                System.out.println("\n");

            } else if (answer.equalsIgnoreCase("n")) {
                System.out.println("The purchase is completed...");
                System.out.println("Enter personalID for discount (YYMMDDXXXX)");
                //Går och kontrollerar ifall ID är korrekt för discounut och ändrar totalbeloppet.

            } else {
                System.out.println("error message:.. WRONG INPUT");
                answer = "y";

            }
        }
    }

    public void hardkodadegrejer() {
        contr.startSale();
        contr.registerItem(5);
        contr.registerItem(5);
        contr.registerItem(8, 2);
        contr.registerItem(5);
        contr.registerItem(1);
        contr.registerItem(1, 2);
        contr.endSale();
        // FRÅGA OM DISCOUNT???
        contr.discountRequest(880822);
//        contr.discountRequest(810222);
        contr.pay(new Amount(220));
    }


    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr) {
        this.contr = contr;
    }

    /**
     * Performs a fake sale by calling all system operations in the controller.
     */
    public void runFakeExecution(){     //TODO delete this when done
        contr.startSale();
        System.out.println("A new sale i started");
    }
}
