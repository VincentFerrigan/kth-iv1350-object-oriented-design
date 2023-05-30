package se.kth.iv1350.startup;
import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.integration.TotalRevenueFileOutput;
import se.kth.iv1350.view.View;
import se.kth.iv1350.integration.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains the <code>main</code> method. Performs all startup
 * of the application.
 * Properties set up base on:
 * <a href=https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html>The Java™ Tutorials - System Properties</a>.
 * If you're having trouble loading the resource file <code>config.properties></code>,
 * first check that <code>src/main/resources</code>
 * is correctly configured as a resources directory in your IDE.
 */
public class Main {
    /**
     * Starts the application.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        Properties properties = new Properties(System.getProperties());
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            System.setProperties(properties);
        } catch (IOException ex) {
            System.out.println("Unable to set up configuration");
            ex.printStackTrace();
        }
        try {
            Printer printer = new Printer();
            Controller contr = new Controller(printer);
            contr.addCashRegisterObserver(TotalRevenueFileOutput.getInstance());
            View view = new View(contr);
            view.basicFlow();
            view.AlternativeFlow();
            view.basicFlowWithExceptions();
        } catch (IOException ex) {
            System.out.println("Unable to start the application");
            ex.printStackTrace();
        }
    }
}
