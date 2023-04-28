package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.model.Item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventorySystem {
    private Map<Integer, Item> inventoryTable = new HashMap<>();


    //    public InventorySystem(File csvFile){}
    public InventorySystem(){
        // TODO skall läsa in från csv fil som skickas med som argument.
        // TODO Nedan är en hårdkodning tillsvidare
        ItemDTO marlboro = new ItemDTO(2001, "Marlboro", "Marlboro Cigaretter. 20 st/pkt", 70, 1);
        ItemDTO festis = new ItemDTO(1001, "Festis Apelsin", "Festis. 50cl.", 22, 1);

        inventoryTable.put(marlboro.getItemID(), new Item(marlboro, 10));
        inventoryTable.put(festis.getItemID(), new Item(festis, 4));
    }

    /**
     *
     * @param file filename with relative path
     */
    public InventorySystem(String file) {
        // TODO ska vi flytta ut hela inläsningsprocessen till en separat metod????
        String filePath = file;
        String splitCsvBy = ";";
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = "";
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String [] splitArray = line.split(splitCsvBy);
                Item item = new Item(new ItemDTO(
                                Integer.parseInt(splitArray[0]),    //itemID
                                splitArray[1],                      //name
                                splitArray[2],                      //description
                                Integer.parseInt(splitArray[3]),    //price
                                Integer.parseInt(splitArray[4])),   //vatRateGroupCode
                                Integer.parseInt(splitArray[5]));   //quantity
                this.inventoryTable.put(item.getItemDTO().getItemID(), item);
            }
        } catch (FileNotFoundException e){
            System.out.println("The file was not found");
            e.printStackTrace(); //Skriver ut vart felet var någonstans.

        } catch (IOException e){
            System.out.println("IOE exception");
            e.printStackTrace();
        }
       }

    public ItemDTO getItemInfo(int itemID){
        Item item = this.inventoryTable.get(itemID);
        return item.getItemDTO();
    }

    public void updateInventory(){
        // TODO Flat based database. What parameters are needed?
    }
}
