package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.*;
import se.kth.iv1350.util.ErrorFileLogHandler;

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
public class ItemRegister {
//public class ItemRegister implements IRegistry<ItemDTO, Integer> {
    private static volatile ItemRegister instance;
    private static final String CSV_DELIMITER = System.getProperty("se.kth.iv1350.database.file.csv_delimiter");
    private final String FILE_PATH = System.getProperty("se.kth.iv1350.database.file.location");
    private final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private final String FLAT_FILE_DB_NAME = System.getProperty("se.kth.iv1350.database.file.inventory_db");
    private final int DATABASE_NOT_FOUND = 404;
    private File flatFileDb;
    private String recordHeader;
    private Map<Integer, ItemData> inventoryTable = new HashMap<>();
    private ErrorFileLogHandler logger;

    private ItemRegister() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        flatFileDb = new File(FILE_PATH + FILE_SEPARATOR+ FLAT_FILE_DB_NAME);

        addItemData();
    }
    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static ItemRegister getInstance() throws IOException {
        ItemRegister result = instance;
        if (result == null) {
            synchronized (ItemRegister.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ItemRegister();
                }
            }
        }
        return result;
    }

    /**
     * Adds items to the hashmap from the flat file database.
     */
    private void addItemData () throws IOException {
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
            logger.log(ex);
            throw ex;
        } catch (IOException ex){
            logger.log(ex);
            throw ex;
        }
    }

    /**
     * Searches for item in the inventory system with specified ID.
     * @param  itemID The items unique article number a.k.a item identifier.
     * @return ShoppingCartItem information as a {@link ItemDTO}.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory.
     * @throws ItemRegistryException when database call failed.
     */
    //TODO Are we supposed to throw ItemRegistryException as well with method?
//    @Override
    public ItemDTO getDataInfo(Integer itemID) throws ItemNotFoundInItemRegistryException {
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
     * Updates the inventory system.
     * @param closedSale contains the sale details
     */
    public void updateRegister(Sale closedSale){
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
     */
    private void updateDatabase() {
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
            logger.log(ex);
            throw new ItemRegistryException("Detailed message about database fail");
        } catch (IOException ex){
            logger.log(ex);
            throw new ItemRegistryException("Detailed message about database fail");
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
            StringBuilder builder = new StringBuilder();
            builder.append(articleNo);
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(name);
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(description);
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(price.getAmount());
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(vatGroupCode);
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(inStore);
            builder.append(ItemRegister.CSV_DELIMITER);
            builder.append(sold);

            return builder.toString();
        }
    }
}
