package se.kth.iv1350;

import org.junit.jupiter.api.BeforeAll;
import se.kth.iv1350.startup.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class POSTestSuperClass {
    /**
     * Properties set up base on:
     * <a href=
     * https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html>
     * The Java™ Tutorials - System Properties</a>.
     * If you're having trouble loading the resource file
     * <code>config.properties></code>,
     * first check that <code>src/test/resources</code>
     * is correctly configured as a resources directory in your IDE.
     */
    @BeforeAll
    static void setup() {
        Properties properties = new Properties(System.getProperties());
        try {
            InputStream inputStream = Main.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            properties.load(inputStream);
            System.setProperties(properties);
        } catch (IOException ex) {
            System.out.println("Unable to set up configuration");
            ex.printStackTrace();
        }
    }
}
