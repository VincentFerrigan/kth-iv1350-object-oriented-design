package se.kth.iv1350.startup;
import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.integration.RegisterCreator;
import se.kth.iv1350.view.View;
import se.kth.iv1350.integration.Printer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        Properties properties = new Properties();
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            System.setProperty("se.kth.iv1350.database.file.location",
                    properties.getProperty("se.kth.iv1350.database.file.location"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Printer printer = new Printer();
            RegisterCreator registerCreator = new RegisterCreator();
            Controller contr = new Controller(printer, registerCreator);

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
