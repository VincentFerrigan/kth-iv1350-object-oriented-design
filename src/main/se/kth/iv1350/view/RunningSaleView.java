package se.kth.iv1350.view;

import se.kth.iv1350.model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TODO UML:a och skriva JavaDocs
public class RunningSaleView extends SaleView {
    @Override
    protected void printCurrentState(SaleDTO saleInfo) {
        System.out.println("----------------- Display follows ----------------");
        System.out.println(createSaleItemsInfoString());
        System.out.println("%-40s%s".formatted("Running total:", saleInfo.getTotalPrice()));
        System.out.println("%-40s%s".formatted("Including VAT:", saleInfo.getTotalVATCost()));
        System.out.println("------------------ End of Display ----------------");
    }

    @Override
    protected void sortShoppingCart(List<ShoppingCartItemDTO> listOfShoppingCartItemsInfo) {
        Collections.sort(listOfShoppingCartItemsInfo, Comparator.comparing(ShoppingCartItemDTO::getTimeOfUpdate).reversed());
    }
}
