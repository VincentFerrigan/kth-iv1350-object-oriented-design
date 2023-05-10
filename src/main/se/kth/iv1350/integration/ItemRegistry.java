package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Item;
import se.kth.iv1350.model.VAT;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an external inventory system. Contains all the item data that are stored in the store.
 * This class is a placeholder for a future external inventory system.
 */
public class ItemRegistry {
    private static final String CSV_DELIMITER = ";";
    private String recordHeader;
    private final String flatFileDb;
    private final String filePath;
    private Map<Integer, ItemData> inventoryTable = new HashMap<>();

    /**
     * Creates a new instance of an inventory system as {@link ItemRegistry}.
     * @param filePath relative file path to the flat file database (csv)
     * @param fileName filename of the flat file database (csv)
     */
    ItemRegistry(String filePath, String fileName) throws IOException {
        // TODO ska vi flytta ut hela inläsningsprocessen till en separat metod????
        this.filePath = filePath;
        this.flatFileDb = fileName;
        addItemData();
    }
    /**
     * Adds items to the hashmap from the flat file database.
     */
    private void addItemData () throws IOException {
        try (FileReader reader = new FileReader(this.filePath + this.flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String [] splitArray = line.split(CSV_DELIMITER);
                ItemData item = new ItemData(
                        Integer.parseInt(splitArray[0]),    //itemID
                        splitArray[1],                      //name
                        splitArray[2],                      //description
                        Double.parseDouble(splitArray[3]),    //price
                        Integer.parseInt(splitArray[4]),   //vatRateGroupCode
                        Integer.parseInt(splitArray[5]),   //quantity
                        Integer.parseInt(splitArray[6]));   //quantity
                this.inventoryTable.put(item.articleNo, item);
            }
        } catch (IOException e){
            // TODO. Logga här, catch och rethrow eller ingen try catch alls?
            System.out.println("IOE exception");
            // Logga här?
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *
     * @param  articleNo The items unique article number a.k.a item identifier.
     * @return Item information as a {@link ItemDTO}.
     */
    public ItemDTO getItemInfo(int articleNo){
        ItemData item = this.inventoryTable.get(articleNo);
        return new ItemDTO(
                item.articleNo, item.name, item.description, item.price, item.vat);
    }

    /**
     * Updates the inventory system by adding the bag of items sold as a collection of {@Item}
     * @param items The bag (collection) of items sold
     */
    public void updateInventory(Collection<Item> items){
        for (Item item : items) {
            int key = item.getItemID();
            inventoryTable.get(key).sold = (item.getQuantity()); // TODO alt. ta bort och ha enbart inStore?
            inventoryTable.get(key).inStore -= (item.getQuantity());
        }
        updateDatabase();
    }

    /**
     * Update database by writing to the flat file database
     */
    private void updateDatabase() {
        try (FileWriter fileWriter = new FileWriter(this.filePath + "inventory_" + LocalDate.now() + ".csv");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (ItemData item : inventoryTable.values()) {
                bufferedWriter.write(item.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException e){
            System.out.println("The file was not found");
            e.printStackTrace(); //Skriver ut vart felet var någonstans.

        } catch (IOException e){
            System.out.println("IOE exception");
            e.printStackTrace();
        }
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
        public ItemData(int articleNo, String name, String description, double price, int vatRateGroupCode, int inStore, int sold) {
            this.articleNo = articleNo;
            this.name = name;
            this.description = description;
            this.price = new Amount(price);
            this.vat = new VAT(vatRateGroupCode);
            this.inStore = inStore;
            this.sold = sold;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(articleNo);
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(name);
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(description);
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(price.getAmount());
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(vat.getVATRateGroupCode());
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(inStore);
            builder.append(ItemRegistry.CSV_DELIMITER);
            builder.append(sold);

            return builder.toString();
        }
    }
}
