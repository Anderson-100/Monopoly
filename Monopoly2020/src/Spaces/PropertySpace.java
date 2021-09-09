/**
 * Author: Anderson Hsiao
 * 2020/09/01
 * Version 1.0
 */

package Spaces;

import Players.Player;

public class PropertySpace extends Space {
    
    private int price, groupNumber, points, rent;
    private Player owner;
    
    public PropertySpace(String name, int id, int groupNumber, int price, int points, int rent) {
        super(name, id);
        this.groupNumber = groupNumber;
        this.price = price;
        this.points = points;
        this.rent = rent;
        owner = null;
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getPrice() {
        return price;
    }
    
    public int getRent() {
        return rent;
    }
    
    public boolean isPurchased() {
        return owner != null;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public boolean buyProperty(Player player, int price) {
        boolean canBuyProperty = player.buyProperty(this, price);
        if (canBuyProperty) {
            owner = player;
            return true;
        }
        return false;
    }
    
    public void sellToBank() {
        owner = null;
    }
    
    @Override
    public String toString() {
        return super.toString() +
                "Group: " + groupNumber + "\n" +
                "Price: $" + price + "\n" +
                "Points: " + points + "\n" +
                "Owner: " + owner + "\n";
    }
}
