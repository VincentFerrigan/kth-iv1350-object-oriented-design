package src.se.kth.iv1350.view;

import src.se.kth.iv1350.controller.Controller;

public class View {
    //TODO where is this attr from?
    private Controller contr;

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
