package se.kth.iv1350.startup;
import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.integration.Display;
import se.kth.iv1350.integration.RegisterCreator;
import se.kth.iv1350.view.View;
import se.kth.iv1350.integration.Printer;
import se.kth.iv1350.util.LogHandler;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Contains the <code>main</code> method. Performs all startup
 * of the application.
 */
public class Main {
    /**
     * Starts the application.
     * @param args The application does not take any command line parameters.
     */
    public static void main (String[] args) {
        //TODO look at these things: they are not in the UML CD
        try {
            LogHandler logger = new LogHandler();
            Printer printer = new Printer();
            Display display = new Display();
            RegisterCreator registerCreator = new RegisterCreator(logger);
            Controller contr = new Controller(printer, display, registerCreator);

            View view = new View(contr, logger);
            // TODO remove after we've tested the hardKodadeGrejer
            view.hardKodadeGrejer();
            // view.testPrompt();
        } catch (IOException ex) {
            System.out.println("Unable to start the application");
            ex.printStackTrace();
        }
    }
}
