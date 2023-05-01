package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.Item;
import src.se.kth.iv1350.model.VAT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private final String flat_file_db;
    private Map<Integer, ItemData> inventoryTable = new HashMap<>();

    /**
     *
     * @param file filename with relative path
     */
    ItemRegistry(String file) {
        // TODO ska vi flytta ut hela inläsningsprocessen till en separat metod????
        this.flat_file_db = file;
        addItemData();
    }
    private void addItemData() {
        String splitCsvBy = ";";
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.flat_file_db));
            String line = "";
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String [] splitArray = line.split(splitCsvBy);
                ItemData item = new ItemData(
                        Integer.parseInt(splitArray[0]),    //itemID
                        splitArray[1],                      //name
                        splitArray[2],                      //description
                        Integer.parseInt(splitArray[3]),    //price
                        Integer.parseInt(splitArray[4]),   //vatRateGroupCode
                        Integer.parseInt(splitArray[5]),   //quantity
                        Integer.parseInt(splitArray[6]));   //quantity
                this.inventoryTable.put(item.articleNo, item);
            }
        } catch (FileNotFoundException e){
            System.out.println("The file was not found");
            e.printStackTrace(); //Skriver ut vart felet var någonstans.

        } catch (IOException e){
            System.out.println("IOE exception");
            e.printStackTrace();
        }
       }

    /**
     *
     * @param  articleNo The items unique article number.
     * @return Item information in a Data Transfer Object.
     */
    public ItemDTO getItemInfo(int articleNo){
        ItemData item = this.inventoryTable.get(articleNo);
        return new ItemDTO(
                item.articleNo, item.name, item.description, item.price, item.vat);
    }

    public void updateInventory(Collection<Item> items){
        for (Item item : items) {
            int key = item.getItemID();
            inventoryTable.get(key).sold = (item.getQuantity()); // TODO alt. ta bort och ha enbart inStore?
            inventoryTable.get(key).inStore -= (item.getQuantity());
        }
        // TODO Store in Flat based database
    }

    private static class ItemData {
        private int articleNo;
        private String name;
        private String description;
        private Amount price;
        private VAT vat;
        private int inStore;
        private int sold;

        /**
         * @param articleNo         Unique article number
         * @param name              Article name
         * @param description       Article description
         * @param price             Price incl. VAT
         * @param vatRateGroupCode  The code for the VAT rate group, such as 0, 1, 2 or 3. Where currently
         *                          0 is VAT Exempt e.g. Mus,
         *                          1 is 25 %,
         *                          2 is 12 % and
         *                          3 is 6 %
         * @param inStore           The quantity of items in stock
         * @param sold              The amount of items sold
         */
        public ItemData(int articleNo, String name, String description, int price, int vatRateGroupCode, int inStore, int sold) {
            this.articleNo = articleNo;
            this.name = name;
            this.description = description;
            this.price = new Amount(price);
            this.vat = new VAT(vatRateGroupCode);
            this.inStore = inStore;
            this.sold = sold;
        }
    }
}
