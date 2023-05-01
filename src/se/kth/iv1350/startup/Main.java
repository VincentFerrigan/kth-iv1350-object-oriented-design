package src.se.kth.iv1350.startup;
import src.se.kth.iv1350.controller.Controller;
import src.se.kth.iv1350.integration.Display;
import src.se.kth.iv1350.integration.RegisterCreator;
import src.se.kth.iv1350.view.View;
import src.se.kth.iv1350.integration.Printer;

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
        Display display = new Display();
        RegisterCreator registerCreator = new RegisterCreator();
        Controller contr = new Controller(printer, display, registerCreator);

        View view = new View(contr);
        // TODO remove after we've tested the hardkodadegrejer
        view.hardkodadegrejer();
//        view.testPromt();
    }
}
