package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.LogHandler;
import java.util.Scanner;

/**
 * Represents the interface of the program. Since the program does not have
 * an interface or view on its own, this class is a placeholder.
 */
public class View {
    private Controller contr;
    private LogHandler logger;

    private Scanner input = new Scanner(System.in);

    // 1. En med prompt
    // 2. Fil med itemID och ev. quantity
    // 3. Hårdkodade

// 3.
    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr, LogHandler logger) {
        this.contr = contr;
        this.logger = logger;
    }

    /**
     * Manual tests in the form of a prompt.
     */
    @Deprecated
    public void testPrompt() {
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

    /**
     * Simulates a user input that generates calls to all system operations,
     * in all possible ways.
     */
    public void hardKodadeGrejer() {
        // FirstSale - with staff discount, not yet implemented
        contr.startSale();
        contr.registerItem(5);
        contr.registerItem(5);
        contr.registerItem(8, 2);
        contr.registerItem(5);
        contr.registerItem(1);
        contr.registerItem(1, 2);
        contr.endSale();
//        contr.discountRequest(880822);
        contr.endSale();
        contr.pay(new Amount(500));

        // SecondSale - with member discount, not yet implemented
        contr.startSale();
        contr.registerItem(5);
        contr.registerItem(5);
        contr.registerItem(7, 2);
        contr.registerItem(5);
        contr.registerItem(1);
        contr.registerItem(1, 2);
        contr.endSale();
//        contr.discountRequest(810222);
        contr.endSale();
        contr.pay(new Amount(500));

        // ThirdSale - without discount
        contr.startSale();
        contr.registerItem(5);
        contr.registerItem(7, 2);
        contr.registerItem(1);
        contr.endSale();
        contr.pay(new Amount(500));
    }



    /**
     * Performs a fake sale by calling all system operations in the controller.
     */
    @Deprecated
    public void runFakeExecution(){     //TODO delete this when done
        contr.startSale();
        System.out.println("A new sale i started");
    }
}
