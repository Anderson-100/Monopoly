package Dice;

public class Die {
    
    private int num;
    
    public Die() {
        rollDie();
    }
    
    public int rollDie() {
        num = (int) (Math.random()*6 + 1);
        return getNum();
    }
    
    public int getNum() {
        return num;
    }
}
