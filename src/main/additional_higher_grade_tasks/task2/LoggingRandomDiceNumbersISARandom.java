package additional_higher_grade_tasks.task2;

import java.util.Random;

public class LoggingRandomDiceNumbersISARandom extends Random {
    @Override
    public int nextInt() {
        int result = super.nextInt(6) + 1;
        System.out.println("Generated random dice number: " + result);
        return result;
    }
}