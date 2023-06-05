package task2;

import java.util.Random;

public class LoggingRandomAsRandom extends Random {
    @Override
    public int nextInt() {
        int result = super.nextInt();
        System.out.println("Generated random dice number: " + result);
        return result;
    }
}
