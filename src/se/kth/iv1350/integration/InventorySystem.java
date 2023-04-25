package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.model.Item;

import java.util.Hashtable;

public class InventorySystem {
    Hashtable<String, Item> inventoryTable = new Hashtable<String, Item>();
    Hashtable<String, String> testTable = new Hashtable<String, String>(); // TODO enbart test. Ta bort efter

    // TODO skall läsa in från csv fil. Nedan är en hårdkodning tillsvidare
    ItemDTO marlboro = new ItemDTO(2001, "Marlboro", " American brand of cigarettes,", 70, 1);
    ItemDTO festis = new ItemDTO(1001, "Festis Apelsin", "Festis. 50cl.", 22, 1);
    Item m = new Item(marlboro, 10);
    Item f = new Item(festis, 10);

//    inventoryTable.put(Integer.toString(cigg.getItemID()), new Item(marlboro, 10));
//    inventoryTable.put(Integer.toString(festis.getItemID()), new Item(festis, 4));
    inventoryTable.put(1001, m);
    testTable.put("testkey", "testval");

    public InventorySystem(){
        //TODO  Constructor
        // set up hashtable here? From txt file
    }

    public ItemDTO registerItem(int itemID){
        Item item = this.inventoryTable.get(Integer.toString(itemID));
        item.decrement();
        this.inventoryTable.replace(Integer.toString(itemID), item); // Någåot osäker om replace är korrekt
        return item.getItemDTO;

        return this.inventoryTable.get(Integer.toString(itemID)).getItem();
    }

    public void updateInventory(SaleDTO saleInfo){
        saleInfo
        //TODO do it
    }
}
