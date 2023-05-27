package se.kth.iv1350.integration.pricing;

import java.lang.reflect.InvocationTargetException;

/**
 * A Singleton that creates instances of the algorithm used for calculating total price with
 * discounts or promotions.
 * All such instances must implement <code>DiscountStrategy</code>.
 */
public class DiscountFactory {
    private static volatile DiscountFactory instance;
    private static final String DISCOUNT_CLASS_NAME_KEY = "se.kth.iv1350.discountstrategy.classname";

    private DiscountFactory() {
        // -Dse.kth.iv1350.discountstrategy.classname=se.kth.iv1350.integration.pricing.MemberDiscount,se.kth.iv1350.integration.pricing.StaffDiscount
        System.setProperty(DISCOUNT_CLASS_NAME_KEY,
                "se.kth.iv1350.integration.pricing.MemberDiscount," +
                "se.kth.iv1350.integration.pricing.StudentDiscount," +
                "se.kth.iv1350.integration.pricing.Promotion");
    }
    /**
     * @return The only instance of this singleton.
     */
    public static DiscountFactory getInstance() {
        DiscountFactory result = instance;
        if (result == null) {
            synchronized (DiscountFactory.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DiscountFactory();
                }
            }
        }
        return result;
    }

    /**
     <p>
       Returns a <code>DiscountStrategy</code> performing the default discount or promotion algorithm.
       The class name of the default <code>DiscountStrategy</code> implementation
       is read from the system property <code>se.kth.iv1350.discountstrategy.classname</code>.
       It is possible to specify more than
       one matcher, by setting this system property to a comma-separated string with class names of
       all desired matchers. When this is the case, all specified discount and promotion algorithms
       are executed and the lowest total price is found.
     </p>
     *
     * @return The default matcher
     * @throws ClassNotFoundException If unable to load the default matcher class.
     * @throws InstantiationException If unable to instantiate the default matcher class.
     * @throws IllegalAccessException If unable to instantiate the default matcher class.
     * @throws java.lang.NoSuchMethodException If unable to instantiate the default matcher class.
     * @throws java.lang.reflect.InvocationTargetException If unable to instantiate the default matcher class.
     */
    public DiscountStrategy getDiscountStrategy()
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        String[] classNames = System.getProperty(DISCOUNT_CLASS_NAME_KEY).split(",");
        return createComposite(classNames);
    }

    private DiscountStrategy createComposite(String[] classNames)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        CompositeDiscountStrategy composite = new CompositeDiscountStrategy();
        for (String className : classNames) {
            composite.addDiscountStrategy(instantiateDiscountStrategy(className));
        }
        return composite;
    }
    private DiscountStrategy instantiateDiscountStrategy(String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Class matcherClass = Class.forName(className);
        DiscountStrategy discountStrategy = (DiscountStrategy) matcherClass.getDeclaredConstructor().newInstance();
        return discountStrategy;
    }
}
