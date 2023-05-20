package se.kth.iv1350.integration.pricing;

import java.lang.reflect.InvocationTargetException;

public class DiscountFactory {
    private static volatile DiscountFactory instance;
    private static final String DISCOUNT_CLASS_NAME_KEY = "discountstrategy.class.name";

    private DiscountFactory() {
        // -Ddiscountstrategy.class.name=se.kth.iv1350.integration.pricing.MemberDiscount,se.kth.iv1350.integration.pricing.StaffDiscount
        System.setProperty(DISCOUNT_CLASS_NAME_KEY, "se.kth.iv1350.integration.pricing.MemberDiscount,se.kth.iv1350.integration.pricing.StaffDiscount");
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
