package additional_higher_grade_tasks.task2;

import java.util.Random;

public class LoggingRandomHasRandom {
    private final Random random = new Random();

    public int nextInt() {
        int result = random.nextInt();
        System.out.println("Generated random dice number: " + result);
        return result;
    }
}
