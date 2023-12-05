package additional_higher_grade_tasks.task2;

public class Main{
    public static void main(String[] args) {
        LoggingRandomAsRandom randomAsRandom =new LoggingRandomAsRandom();
        LoggingRandomHasRandom randomHasRandom =new LoggingRandomHasRandom();

        LoggingRandomDiceNumbersISARandom diceIsARandom = new LoggingRandomDiceNumbersISARandom();
        LoggingRandomDiceNumbersHasRandom diceHasRandom = new LoggingRandomDiceNumbersHasRandom();

        int randomNumber = randomAsRandom.nextInt();
        randomNumber = randomHasRandom.nextInt();
        randomNumber = diceIsARandom.nextInt();
        randomNumber = diceHasRandom.nextInt();
    }
}