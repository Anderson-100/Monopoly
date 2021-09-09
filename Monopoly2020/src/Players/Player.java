package Players;

import Spaces.*;
import java.util.ArrayList;

public class Player implements Comparable<Player> {
    
    private String name;
    private int position;
    private int money;
    private int points;
    private ArrayList<PropertySpace> propertiesOwned;
    
    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.money = 1500;  //Each player starts off with $1500.
        this.points = 0;
        this.propertiesOwned = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public int movePlayer(int steps) {
        position += steps;
        if (position > 40)
            position -= 40;
        return position;
    }
    
    public void addMoney(int amount) {
        money += amount;
    }
    
    //Returns money left, or -1 if can't pay.
    public boolean removeMoney(int amount) {
        if (amount <= money) {
            money -= amount;
            return true;
        }
        return false;
    }
    
    public int getMoney() {
        return money;
    }
    
    private void addPoints(int points) {
        this.points += points;
    }
    
    public int getPoints() {
        return points;
    }
    
    //Add property to the ArrayList of properties and removes the cost from money.
    //Returns true if it was successfully bought or false if it wasn't.
    public boolean buyProperty(PropertySpace property, int price) {
        boolean canBuy = removeMoney(price);
        if(!canBuy)
            return false;
        
        propertiesOwned.add(property);
        addPoints(property.getPoints());
        return true;
    }
    
    public int compareTo(Player other) {
        return points - other.getPoints();
    }
    
    public ArrayList<PropertySpace> getPropertiesOwned() {
        return propertiesOwned;
    }
    
    //Called when a player is getting kicked out of the game.
    public void transferAssetsTo(Player other) {
        other.addProperties(propertiesOwned);
        other.addMoney(money);
        other.addPoints(points);
    }
    
    private void addProperties(ArrayList<PropertySpace> spaces) {
        propertiesOwned.addAll(spaces);
    }
    
    @Override
    public String toString() {
        return name + "\n" +
                "Position on Board: " + position + "\n" +
                "Money: $" + money + "\n" +
                "Points: " + points + "\n" +
                "Number of Properties Owned: " + propertiesOwned.size();
    }
}
