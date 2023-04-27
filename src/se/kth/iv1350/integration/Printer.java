package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.Display;
import src.se.kth.iv1350.model.Receipt;

public class Printer {
    public Printer(){
        //TODO construct
    }

    public void print(Receipt receipt) {
        System.out.println("----------------- Receipt follows --------------");
        System.out.println(receipt);
        System.out.println("------------------ End of receipt --------------");

    }
    public void print(SaleLog saleLog) {
        // TODO

    }

    public void print(Display display) {
        System.out.println("----------------- Display follows --------------");
        System.out.println(display);
        System.out.println("------------------ End of Display --------------");

    }
}
