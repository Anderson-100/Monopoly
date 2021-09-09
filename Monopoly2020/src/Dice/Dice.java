package Dice;

public class Dice {
    
    private Die[] dice;
    
    public Dice() {
        dice = new Die[2];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = new Die();
        }
    }
    
    public int rollDice() {
        int sum = 0;
        for (int i = 0; i < dice.length; i++) {
            sum += dice[i].rollDie();
        }
        return sum;
    }
    
    public boolean isDouble() {
        return dice[0].getNum() == dice[1].getNum();
    }
    
    @Override
    public String toString() {
        return dice[0].getNum() + " and " + dice[1].getNum();
    }
}
