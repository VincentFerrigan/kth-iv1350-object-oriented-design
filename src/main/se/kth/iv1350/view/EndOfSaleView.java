package se.kth.iv1350.view;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.model.SaleObserver;
import se.kth.iv1350.model.ShoppingCartItemDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EndOfSaleView extends SaleView {
    @Override
    protected void sortShoppingCart(List<ShoppingCartItemDTO> listOfShoppingCartItemsInfo) {
        Collections.sort(listOfShoppingCartItemsInfo, Comparator.comparing(ShoppingCartItemDTO::getName));
    }
    @Override
    protected void printCurrentState(SaleDTO saleInfo) {
        System.out.println("--------------- End of Sale follows --------------");
        System.out.println(createSaleItemsInfoString());
        if (!saleInfo.getTotalDiscounts().equals(new Amount(0))) {
            System.out.println("%-40s-%s".formatted("Total discount:", saleInfo.getTotalDiscounts()));
        }
        System.out.println("%-40s%s".formatted("Total Price:", saleInfo.getTotalPrice()));
        System.out.println("%-40s%s".formatted("Including VAT:", saleInfo.getTotalVATAmount()));
        System.out.println("---------------- End of End of Sale --------------");
    }
}
