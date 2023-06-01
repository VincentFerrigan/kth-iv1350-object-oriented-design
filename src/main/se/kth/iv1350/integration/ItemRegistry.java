package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.Sale;

public interface ItemRegistry<T> {

    /**
     * Searches for item in the inventory system with specified ID.
     * @param  dataID The items unique article number a.k.a item identifier.
     * @return Item information as a {@link ItemDTO}.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory.
     * @throws ItemRegistryException when database call failed.
     */
    ItemDTO getDataInfo(T dataID) throws ItemNotFoundInItemRegistryException, ItemRegistryException;

    /**
     * Updates the inventory system.
     * @param closedSale contains the sale details
     * @throws ItemRegistryException when database call failed.
     */
    void updateRegistry(Sale closedSale) throws ItemRegistryException;
}
