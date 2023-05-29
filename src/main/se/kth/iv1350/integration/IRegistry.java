package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

public interface IRegistry<T, U> {
    T getDataInfo(U dataID) throws Exception;
    void updateRegister(Sale closedSale);

}
