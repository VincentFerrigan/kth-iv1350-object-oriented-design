package se.kth.iv1350.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// TODO add javadoc and test it
// TODO add it to the facade
public class FlatFileDatabaseFactory implements IRegistryFactory{
    private static volatile FlatFileDatabaseFactory instance;
    private static final String ACCOUNTING_CLASS_NAME_KEY = "se.kth.iv1350.database.accounting_db.classname";
    private static final String CUSTOMER_CLASS_NAME_KEY = "se.kth.iv1350.database.customer_db.classname";
    private static final String INVENTORY_CLASS_NAME_KEY = "se.kth.iv1350.database.inventory_db.classname";

    private FlatFileDatabaseFactory() {}

    /**
     * @return The only instance of this singleton.
     */
    public static FlatFileDatabaseFactory getInstance() {
        FlatFileDatabaseFactory result = instance;
        if (result == null) {
            synchronized (FlatFileDatabaseFactory.class) {
                result = instance;
                if (result == null) {
                    instance = result = new FlatFileDatabaseFactory();
                }
            }
        }
        return result;
    }

    @Override
    public ItemRegistry getDefaultItemRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = System.getProperty(INVENTORY_CLASS_NAME_KEY);
        Class c = Class.forName(className);
        Method factoryMethod = c.getDeclaredMethod("getInstance");
        Object singleton = factoryMethod.invoke(null, null);
        return (ItemRegistry) singleton;
    }
    @Override
    public CustomerRegistry getDefaultCustomerRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = System.getProperty(CUSTOMER_CLASS_NAME_KEY);
        Class c = Class.forName(className);
        Method factoryMethod = c.getDeclaredMethod("getInstance");
        Object singleton = factoryMethod.invoke(null, null);
        return (CustomerRegistry) singleton;
    }
    @Override
    public AccountingSystem getDefaultAccountingSystem() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = System.getProperty(ACCOUNTING_CLASS_NAME_KEY);
        Class c = Class.forName(className);
        Method factoryMethod = c.getDeclaredMethod("getInstance");
        Object singleton = factoryMethod.invoke(null, null);
        return (AccountingSystem) singleton;
    }
}