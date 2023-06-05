package task2;

import java.util.Random;

public class LoggingRandomDiceNumbersHasRandom {
    private Random random = new Random();

    public int nextInt() {
        int result = random.nextInt(6) +1;
        System.out.println("Generated random dice number: " + result);
        return result;
    }
}