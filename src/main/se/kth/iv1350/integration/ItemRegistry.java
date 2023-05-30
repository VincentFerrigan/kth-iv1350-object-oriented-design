package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.Sale;

public interface ItemRegistry<T> {
    ItemDTO getDataInfo(T dataID) throws ItemNotFoundInItemRegistryException;
    void updateRegistry(Sale closedSale);
}
