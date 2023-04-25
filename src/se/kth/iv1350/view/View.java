package src.se.kth.iv1350.view;

import src.se.kth.iv1350.controller.Controller;
import src.se.kth.iv1350.dto.CurrentSaleDTO;

public class View {
    private CurrentSaleDTO saleInfo;
    private Controller contr;

    // 1. En med prompt
    // 2. Fil med itemID och ev. quantity
    // 3. Hårdkodade

// 3.
    public void hardkodadegrejer() {
        contr.startSale();
        System.out.println(contr.registerItem(5));
        System.out.println(contr.registerItem(5));
        System.out.println(contr.registerItem(8, 2));
        System.out.println(contr.registerItem(1));
        System.out.println(contr.registerItem(1, 2));
    }

    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr) {
        this.contr = contr;
    }

    /**
     * Performs a fake sale by calling all system operations in the controller.
     */
    public void runFakeExecution(){     //TODO delete this when done
        contr.startSale();
        System.out.println("A new sale i started");
    }
}
