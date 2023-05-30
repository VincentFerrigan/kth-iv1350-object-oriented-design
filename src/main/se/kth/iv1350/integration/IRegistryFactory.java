package se.kth.iv1350.integration;

import java.lang.reflect.InvocationTargetException;

// TODO: Or should the AbstractFactory be abstract?
// How do I switch from Factory to Factory?
public interface IRegistryFactory {
    ItemRegistry getDefaultItemRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    CustomerRegistry getDefaultCustomerRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    AccountingSystem getDefaultAccountingSystem() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
