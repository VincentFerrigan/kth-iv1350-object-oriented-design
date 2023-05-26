package se.kth.iv1350.integration.vat;

import java.lang.reflect.InvocationTargetException;

/**
 * A Singleton that creates an instance of the algorithm used for calculating
 * VAT for a specific item
 */
public class VATFactory {
    private static volatile VATFactory instance;

    private static final String VAT_CLASS_NAME_KEY = "se.kth.iv1350.vatstrategy.classname";
    private VATFactory() {
        // -Dse.kth.iv1350.vatstrategy.classname=se.kth.iv1350.integration.vat.SwedishVAT
        System.setProperty(VAT_CLASS_NAME_KEY,
                "se.kth.iv1350.integration.vat.SwedishVAT");
    }
    /**
     * @return The only instance of this singleton.
     */
    public static VATFactory getInstance() {
        VATFactory result = instance;
        if (result == null) {
            synchronized (VATFactory.class) {
                result = instance;
                if (result == null) {
                    instance = result = new VATFactory();
                }
            }
        }
        return result;
    }

    /**
     <p>
     Returns a <code>VATStrategy</code> performing the default VAT algorithm.
     The class name of the default <code>VATStrategy</code> implementation
     is read from the system property <code>se.kth.iv1350.vatstrategy.classname</code>.
     </p>
     *
     * @return The default matcher
     * @throws ClassNotFoundException If unable to load the default matcher class.
     * @throws InstantiationException If unable to instantiate the default matcher class.
     * @throws IllegalAccessException If unable to instantiate the default matcher class.
     * @throws java.lang.NoSuchMethodException If unable to instantiate the default matcher class.
     * @throws java.lang.reflect.InvocationTargetException If unable to instantiate the default matcher class.
     */
    public VATStrategy getDefaultVATStrategy()
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        String className = System.getProperty(VAT_CLASS_NAME_KEY);
        return instantiateVATStrategy(className);
    }

    private VATStrategy instantiateVATStrategy(String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Class matcherClass = Class.forName(className);
        VATStrategy vatStrategy = (VATStrategy) matcherClass.getDeclaredConstructor().newInstance();
        return vatStrategy;
    }
}
