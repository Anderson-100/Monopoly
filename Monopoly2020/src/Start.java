/**
 * The main class. Monopoly game uwu.
 *
 * Author: Anderson Hsiao
 * Version: 1.0
 */

import Cards.*;
import Dice.*;
import Players.*;
import Spaces.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Start {
    
    private static Board gameBoard;
    private static Dice dice = new Dice();
    private static ChanceCardDeck cardDeck;
    private static ArrayList<Player> players;
    
    private static Scanner reader = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        //Initializing Board and Chance Cards.
        System.out.println("Welcome to Monopoly. Which theme would you like to play?" +
                "\n\t1. Cars" +
                "\n\t2. 2020");
        int theme = reader.nextInt();
        
        String boardFile = "", cardFile = "";
        switch (theme) {
            case 1:
                boardFile = "/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeSpaces.csv";
                cardFile = "/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeChanceCards.csv";
        }
        
        gameBoard = new Board(boardFile);
        cardDeck = new ChanceCardDeck(cardFile);
        cardDeck.shuffleDeck();
        
        //Initializing Players.
        int numPlayers;
        do {
            System.out.println("How many players are playing? (2-6)");
            numPlayers = reader.nextInt();
            reader.nextLine();
        } while (numPlayers < 2 || numPlayers > 6);
        
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Player " + (i+1) + " name: ");
            String name = reader.nextLine();
            players.add(new Player(name));
        }
    
        System.out.println("Welcome to the game! Here is a preview of the board:" + "\n" +
                gameBoard + "\n" +
                "For this game, you will need " + gameBoard.pointsToWin(numPlayers) + " points to win.");
        
        int currentPlayer = (int) (Math.random() * numPlayers);
        System.out.println("\n\n------------\n" +
                "Player " + (currentPlayer+1) + " starts!");
        
        int numDoubles = 0;
        boolean gameNotOver = true;
        Player winner = null;
        while(gameNotOver) {
            playerTurn(players.get(currentPlayer));
            
            //TODO: Test if the double thing works.
            if(dice.isDouble()) {
                System.out.println("You rolled a double so you get to go again!");
                numDoubles++;
                if (numDoubles >= 3)
                    JailSpace.putPlayerInJail(players.get(currentPlayer));
            } else {
                numDoubles = 0;
                currentPlayer = nextPlayer(currentPlayer);
                System.out.println("\n\n----------\n" +
                        "NEXT PLAYER\n");
            }
            
            //Checks if anyone has won
            for(Player player : players) {
                if (player.getPoints() >= gameBoard.pointsToWin(players.size())) {
                    gameNotOver = false;
                    winner = player;
                }
            }
        }
    
        System.out.println(winner.getName() + " has won! Game over!" +
                "\nFinal results:");
        
    }
    
    private static int nextPlayer(int currentPlayer) {
        if(currentPlayer == players.size()-1)
            return 0;
        return ++currentPlayer;
    }
    
    private static void playerTurn(Player player) {
        System.out.println(player.getName() + ", it is your turn!");
        int steps = dice.rollDice();
        System.out.println("You rolled " + dice);
        
        int newPosition = player.movePlayer(steps);
        Space newSpace = gameBoard.getSpaceAtPosition(newPosition);
    
        System.out.println(player.getName() + ", you moved " + steps + " steps to:\n" + newSpace);
        
        if (newSpace instanceof FreeParkingSpace || newSpace instanceof JailSpace)
            return;
        else if (newSpace instanceof GoSpace)
            GoSpace.passGo(player);
        else if (newSpace instanceof GoToJailSpace)
            JailSpace.putPlayerInJail(player);
        else if (newSpace instanceof ChanceSpace)
            executeChanceCard(player, cardDeck.drawCard());
        else
            executePropertySpace(player, (PropertySpace) newSpace);
    }
    
    private static void executeChanceCard(Player player, ChanceCard card) {
        System.out.println(card);
        if (card.getValue() < 0) {
            boolean canBuy = player.removeMoney(-1 * card.getValue());
            if (!canBuy)
                kickPlayerOut(null, player);
            else
                System.out.println("You have $" + player.getMoney() + " remaining after that transaction.");
        } else {
            player.addMoney(card.getValue());
        }
        System.out.println("Your balance is now $" + player.getMoney());
    }
    
    private static void executePropertySpace(Player player, PropertySpace property) {
        
        if (!property.isPurchased()) {
            //Ask if they want to buy the property. If not, auction the property.
            while(true) {
                System.out.println("Would you like to buy this property?" + "\n" +
                        "You have $" + player.getMoney() + ".\n" +
                        "1. Yes" + "\n" +
                        "2. No");
                int choice = reader.nextInt();
                if (choice == 2) {
                    propertyAuction(property);
                    break;
                }
                else {
                    boolean canBuyProperty = property.buyProperty(player, property.getPrice());
                    if (canBuyProperty) {
                        System.out.println("You have successfully bought this property!" + "\n" +
                                "You now have " + player.getPoints() + " points " +
                                "and $" + player.getMoney() + " remaining.");
                        break;
                    } else
                        System.out.println("You do not have enough money to buy this property!");
                }
            }
        } else {
            //Pay rent
            System.out.println("You landed on " + property.getOwner().getName() + "\'s property." + "\n" +
                    "You owe " + property.getOwner().getName() + " $" + property.getRent() + ".");
            
            boolean canPay = player.removeMoney(property.getRent());
            if(!canPay)
                kickPlayerOut(property.getOwner(), player);
            else {
                property.getOwner().addMoney(property.getRent());
                System.out.println("You have $" + player.getMoney() + " remaining.");
            }
        }
    }
    
    private static void propertyAuction(PropertySpace property) {
        System.out.println("\n\n-----------\n" +
                "AUCTION TIME!!!\n" +
                "The auction has started for:\n" + property);
        
        Player highestBidder = null;
        int highestBid = 0;
        
        int cnt = 0;
        while (true) {
            Player currentPlayer = players.get(cnt);
            if (currentPlayer == highestBidder)
                break;
            
            if (currentPlayer.getMoney() <= highestBid) {
                System.out.println(currentPlayer.getName() + ", you do not have enough money to bid! :(");
                cnt++;
                continue;
            }
            System.out.println(currentPlayer.getName() + ", would you like to bid on this property?" +
                    "\n1. Yes" +
                    "\n2. No");
            int choice = reader.nextInt();
            
            if (choice  == 1) {
                int bid;
                do {
                    System.out.println(currentPlayer.getName() + ", how much would like to bid?" +
                            "\n(Current bid: $" + highestBid + ")" +
                            "\nYour money: $" + currentPlayer.getMoney());
                    bid = reader.nextInt();
                    if (bid > highestBid)
                        break;
                    System.out.println("Error: Your bid must be greater than the current highest bid. Try again.");
                } while (true);
                
                highestBidder = currentPlayer;
                highestBid = bid;
            }
            cnt = nextPlayer(cnt);
        }
    
        System.out.println(highestBidder.getName() + " has won the auction at the price of $" + highestBid + "!\n" +
                "Enjoy your new acquisition!");
        property.buyProperty(highestBidder, highestBid);
    }
    
    private static void kickPlayerOut(Player winner, Player loser) {
        if(winner != null) {
            loser.transferAssetsTo(winner);
            players.remove(loser);
        } else {
            for (PropertySpace property : loser.getPropertiesOwned()) {
                property.sellToBank();  //sets each property's owner back to null
            }
        }
    }
}
