package Cards;

public class ChanceCard {
    
    private String description;
    private int value;
    
    public ChanceCard(String description, int value) {
        this.description = description;
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public String toString() {
        return "Chance Card:\n" + description + "\n";
    }
}
