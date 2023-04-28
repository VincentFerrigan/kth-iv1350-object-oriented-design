package src.se.kth.iv1350.startup;
import src.se.kth.iv1350.controller.Controller;
import src.se.kth.iv1350.view.View;
import src.se.kth.iv1350.integration.Printer;
import src.se.kth.iv1350.integration.SaleLog;

/**
 * Contains the <code>main</code> method. Performs all startup
 * of the application.
 */
public class Main {
    /**
     * Starts the application.
     * @param args The application does not take any command line parameters.
     */
    public static void main (String[] args){
        //TODO look at these things: they are not in the UML CD
        Printer printer = new Printer();
        SaleLog saleLog = new SaleLog();
        Controller contr = new Controller(printer, saleLog);

        View view = new View(contr);
        // TODO remove after we've tested the hardkodadegrejer
        view.hardkodadegrejer();
//        view.testPromt();
    }
}
