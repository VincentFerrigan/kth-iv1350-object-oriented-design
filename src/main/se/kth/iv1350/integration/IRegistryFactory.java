package se.kth.iv1350.integration;

import java.lang.reflect.InvocationTargetException;

// TODO: Or should the AbstractFactory be abstract?
// How do I switch from Factory to Factory?
public interface IRegistryFactory {
    IRegistry getDefaultItemRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    IRegistry getDefaultCustomerRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    IRegistry getDefaultAccountingRegister() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
