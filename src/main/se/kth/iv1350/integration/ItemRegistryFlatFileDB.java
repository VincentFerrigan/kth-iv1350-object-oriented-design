package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A Singleton that creates an instance representing an external inventory system.
 * Contains all the item data that are stored in the store.
 * This Singleton is a placeholder for a future external inventory system.
 */
public class ItemRegistryFlatFileDB implements ItemRegistry {
    private static volatile ItemRegistryFlatFileDB instance;
    private static final String CSV_DELIMITER = System.getProperty("se.kth.iv1350.database.file.csv_delimiter");
    private final String FILE_PATH_KEY = "se.kth.iv1350.database.file.location";
    private final String FLAT_FILE_DB_NAME_KEY = "se.kth.iv1350.database.file.inventory_db";
    private final int DATABASE_NOT_FOUND = 404;
    private File flatFileDb;
    private String recordHeader;
    private Map<Integer, ItemData> inventoryTable = new HashMap<>();

    private ItemRegistryFlatFileDB() throws ItemRegistryException{
        flatFileDb = new File(
                System.getProperty(FILE_PATH_KEY) +
                        System.getProperty("file.separator") +
                        System.getProperty(FLAT_FILE_DB_NAME_KEY));
        addItemData();
    }
    /**
     * @return The only instance of this singleton.
     * @throws ItemRegistryException database I/O failure
     */
    public static ItemRegistryFlatFileDB getInstance() throws ItemRegistryException{
        ItemRegistryFlatFileDB result = instance;
        if (result == null) {
            synchronized (ItemRegistryFlatFileDB.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ItemRegistryFlatFileDB();
                }
            }
        }
        return result;
    }

    /**
     * Adds items to the hashmap from the flat file database.
     * @throws ItemRegistryException database I/O failure
     */
    private void addItemData () throws ItemRegistryException {
        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null){
                String [] splitArray = line.split(CSV_DELIMITER);
                ItemData item = new ItemData(
                        Integer.parseInt(splitArray[0]),    //itemID
                        splitArray[1],                      //name
                        splitArray[2],                      //description
                        Double.parseDouble(splitArray[3]),  //price
                        Integer.parseInt(splitArray[4]),    //vatRateGroupCode
                        Integer.parseInt(splitArray[5]),    //quantity
                        Integer.parseInt(splitArray[6]));   //quantity
                this.inventoryTable.put(item.articleNo, item);
            }
        } catch (FileNotFoundException ex){
            throw new ItemRegistryException("Flat-file db %s not found in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        } catch (IOException ex){
            throw new ItemRegistryException("Unable to read from flat file db %s in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        }
    }

    /**
     * Searches for item in the inventory system with specified ID.
     * @param  dataID The items unique article number a.k.a item identifier.
     * @return Item information as a {@link ItemDTO}.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory.
     * @throws ItemRegistryException when database call failed.
     */
    @Override
    public ItemDTO getDataInfo(Object dataID) throws ItemNotFoundInItemRegistryException, ItemRegistryException{
        Integer itemID = (Integer) dataID;
        if (itemID == DATABASE_NOT_FOUND) {
            throw new ItemRegistryException("Detailed message about database fail");
        } else if (inventoryTable.containsKey(itemID)) {
            ItemData item = this.inventoryTable.get(itemID);
            return new ItemDTO(
                    item.articleNo, item.name, item.description, item.price, item.vatGroupCode);
        } else {
            throw new ItemNotFoundInItemRegistryException(itemID);
        }
    }

    /**
     * Updates the inventory system by adding the specified {@link Sale}.
     * @param closedSale contains the sale details
     * @throws ItemRegistryException when database call failed.
     */
    public void updateRegistry(Sale closedSale){
        List<ShoppingCartItem> listOfShoppingCartItems = new ArrayList<>(closedSale.getCollectionOfItems());
        for (ShoppingCartItem shoppingCartItem : listOfShoppingCartItems) {
            int key = shoppingCartItem.getItemID();
            inventoryTable.get(key).sold = (shoppingCartItem.getQuantity());
            inventoryTable.get(key).inStore -= (shoppingCartItem.getQuantity());
        }
        updateDatabase();
    }

    /**
     * Update database by writing to the flat file database
     * @throws ItemRegistryException database I/O failure
     */
    private void updateDatabase() throws ItemRegistryException{
        try (FileWriter fileWriter = new FileWriter(flatFileDb.getPath().replace(".csv", "_" + LocalDate.now() + ".csv"));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (ItemData item : inventoryTable.values()) {
                bufferedWriter.write(item.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            throw new ItemRegistryException("Flat-file db %s not found in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        } catch (IOException ex){
            throw new ItemRegistryException("Unable to write to flat file db %s in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        }
    }

    private static class ItemData {
        private int articleNo;
        private String name;
        private String description;
        private Amount price;
        private int vatGroupCode;
        private int inStore;
        private int sold;

        /**
         * @param articleNo         Unique article number
         * @param name              Article name
         * @param description       Article description
         * @param price             Price incl. VAT
         * @param vatGroupCode      The code for the VAT rate group, such as 0, 1, 2 or 3. Where currently
         *                          0 is VAT Exempt e.g. Mus,
         *                          1 is 25 %,
         *                          2 is 12 % and
         *                          3 is 6 %
         * @param inStore           The quantity of items in stock
         * @param sold              The amount of items sold
         */
        public ItemData(int articleNo, String name, String description, double price, int vatGroupCode, int inStore, int sold) {
            this.articleNo = articleNo;
            this.name = name;
            this.description = description;
            this.price = new Amount(price);
            this.vatGroupCode = vatGroupCode;
            this.inStore = inStore;
            this.sold = sold;
        }

        @Override
        public String toString() {
            String csv_delimiter = ItemRegistryFlatFileDB.CSV_DELIMITER;

            StringBuilder builder = new StringBuilder();
            builder.append(articleNo);
            builder.append(csv_delimiter);
            builder.append(name);
            builder.append(csv_delimiter);
            builder.append(description);
            builder.append(csv_delimiter);
            builder.append(price.getAmount());
            builder.append(csv_delimiter);
            builder.append(vatGroupCode);
            builder.append(csv_delimiter);
            builder.append(inStore);
            builder.append(csv_delimiter);
            builder.append(sold);

            return builder.toString();
        }
    }
}
