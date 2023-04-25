package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.model.Item;

import java.util.HashMap;
import java.util.Map;

public class InventorySystem {
    private Map<Integer, Item> inventoryTable = new HashMap<>();
    private Map<String, String> testTable = new HashMap<>(); // TODO enbart test. Ta bort efter


//    public InventorySystem(File csvFile){}

    public InventorySystem(){
        // TODO skall läsa in från csv fil som skickas med som argument.
        //  Nedan är en hårdkodning tillsvidare
        ItemDTO marlboro = new ItemDTO(2001, "Marlboro", " American brand of cigarettes,", 70, 1);
        ItemDTO festis = new ItemDTO(1001, "Festis Apelsin", "Festis. 50cl.", 22, 1);
//    Item m = new Item(marlboro, 10);
//    Item f = new Item(festis, 10);

        inventoryTable.put(marlboro.getItemID(), new Item(marlboro, 10));
        inventoryTable.put(festis.getItemID(), new Item(festis, 4));
    }

    public ItemDTO registerItem(int itemID){
        Item item = this.inventoryTable.get(itemID);
        item.decrement();
        this.inventoryTable.put(itemID, item); // Någåot osäker om replace är korrekt
        return item.getItemDTO();
    }

    public void updateInventory(SaleDTO saleInfo){
//        saleInfo
        //TODO do it
    }
}
