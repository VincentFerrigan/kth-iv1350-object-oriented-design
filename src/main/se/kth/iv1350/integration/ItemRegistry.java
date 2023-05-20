package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.model.ShoppingCartItemDTO;
import se.kth.iv1350.model.VAT;
import se.kth.iv1350.util.DBParameters;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A Singleton that creates an instance representing an external inventory system.
 * Contains all the item data that are stored in the store.
 * This Singleton is a placeholder for a future external inventory system.
 */
public class ItemRegistry {
    private static volatile ItemRegistry instance;
    private static final String CSV_DELIMITER = ";";
    private static final int DATABASE_NOT_FOUND = 404;
    private File flatFileDb;
    private String recordHeader;
    private Map<Integer, ItemData> inventoryTable = new HashMap<>();
    private ErrorFileLogHandler logger;

    private ItemRegistry() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        DBParameters dBParams = DBParameters.getInstance();
        flatFileDb = dBParams.getInventoryFlatFileDb();

        addItemData();
    }
    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static ItemRegistry getInstance() throws IOException {
        ItemRegistry result = instance;
        if (result == null) {
            synchronized (ItemRegistry.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ItemRegistry();
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
            // TODO Kan man kasta bara ex? Kommer den då skickas som en IOException?
            logger.log(ex);
            throw ex;
        } catch (IOException ex){
            // TODO ska addItemData loggas här?
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
    public ItemDTO getItemInfo(int itemID) throws ItemNotFoundInItemRegistryException {
        if (itemID == DATABASE_NOT_FOUND) {
            throw new ItemRegistryException("Detailed message about database fail");
        } else if (inventoryTable.containsKey(itemID)) {
            ItemData item = this.inventoryTable.get(itemID);
            return new ItemDTO(
                    item.articleNo, item.name, item.description, item.price, item.vat);
        } else {
            throw new ItemNotFoundInItemRegistryException(itemID);
        }
    }

    /**
     * Updates the inventory system.
     * @param saleInfo contains the sale details
     */
    public void updateInventory(SaleDTO saleInfo){
        List<ShoppingCartItemDTO> shoppingCartInfo = saleInfo.getSaleItemsInfo();
        for (ShoppingCartItemDTO shoppingCartItemInfo : shoppingCartInfo) {
            int key = shoppingCartItemInfo.getItemID();
            inventoryTable.get(key).sold = (shoppingCartItemInfo.getQuantity());
            inventoryTable.get(key).inStore -= (shoppingCartItemInfo.getQuantity());
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
