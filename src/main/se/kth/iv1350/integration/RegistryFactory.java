package se.kth.iv1350.integration;

import se.kth.iv1350.integration.vat.VATFactory;

import java.lang.reflect.InvocationTargetException;

// TODO add javadoc and test it
// TODO add it to the facade
public class RegistryFactory {
    private static volatile RegistryFactory instance;
    private static final String ACCOUNTING_CLASS_NAME_KEY = "se.kth.iv1350.database.accounting_db.classname";
    private static final String CUSTOMER_CLASS_NAME_KEY = "se.kth.iv1350.database.customer_db.classname";
    private static final String INVENTORY_CLASS_NAME_KEY = "se.kth.iv1350.database.inventory_db.classname";

    private RegistryFactory() {}

    /**
     * @return The only instance of this singleton.
     */
    public static RegistryFactory getInstance() {
        RegistryFactory result = instance;
        if (result == null) {
            synchronized (RegistryFactory.class) {
                result = instance;
                if (result == null) {
                    instance = result = new RegistryFactory();
                }
            }
        }
        return result;
    }



    public IRegistry getDefaultItemRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        String className = System.getProperty(INVENTORY_REGISTER_CLASS_NAME_KEY);
        String className = "se.kth.iv1350.integration.ItemRegister";
        return instantiateRegister(className);
    }
    public IRegistry getDefaultCustomerRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        String className = System.getProperty(CUSTOMER_REGISTER_CLASS_NAME_KEY);
        String className = "se.kth.iv1350.integration.CustomerRegister";
        return instantiateRegister(className);
    }
    public IRegistry getDefaultAccountingRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        String className = System.getProperty(ACCOUNTING_REGISTER_CLASS_NAME_KEY);
        String className = "se.kth.iv1350.integration.AccountSystem";
        return instantiateRegister(className);
    }
    private IRegistry instantiateRegister(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class registryClass = Class.forName(className);
        return (IRegistry) registryClass.getDeclaredConstructor().newInstance();
    }
}