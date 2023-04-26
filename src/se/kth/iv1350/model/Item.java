package src.se.kth.iv1350.model;
import src.se.kth.iv1350.dto.ItemDTO;

public class Item {
    // TODO kan man göra den generiskt? Så den kör samma typ som ItemDTO
    private final int itemID;
    private final ItemDTO itemInfo;
    private int quantity;
    private Amount totalAmount;


    public Item (ItemDTO item, int quantity){
        this.itemID = item.getItemID();
        this.itemInfo = item;
        this.totalAmount = new Amount(itemInfo.getPrice());
        this.quantity = quantity;
        this.totalAmount.multiptyAmount(quantity);
    }
    public Item (ItemDTO item){
        this(item, 1);
    }

    // För salelog/sales
    /**
     * Increment an already added item when it's more than one of the same.
     */
    public void increment(){
        this.quantity++;
    }

    // För när vi uppdaterar inventory.
    public void decrement(){
        this.quantity--;
    }

    /**
     * Sets the quantity of items with same ID number.
     * @param   quantity    nbr of items
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void addToQuantity(int additionalQuantity){
        this.quantity += additionalQuantity;
    }
    public ItemDTO getItemDTO() {
        return itemInfo;
    }

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }
    //TODO ny metod!

    /**
     * Returns to total amount i.e. the item price x quantity.
     */
    public Amount getTotalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {return true;}

        Item c = (Item) o;
        return Integer.compare(itemID, c.getItemID()) == 0;
    }

    // exemption?
    public void addItem(Item termItem){
        if (this.equals(termItem)) {
            addToQuantity(termItem.getQuantity());
            totalAmount.addAmount(termItem.getTotalAmount());
        }
    }

    public Amount getVatAmount() {
        Amount vatAmount = new Amount(this.totalAmount);
        double vatRate = itemInfo.getVATRate();
        vatAmount.multiptyAmount(vatRate);
        return vatAmount;
    }
}