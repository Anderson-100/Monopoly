/*
 * The main class. Monopoly game uwu.
 *
 * Author: Anderson Hsiao
 * Version: 2.0 - with GUI
 */

import Cards.ChanceCard;
import Cards.ChanceCardDeck;
import Dice.Dice;
import Players.Player;
import Spaces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class StartGUI {
    
    private static Board gameBoard;
    private static Dice dice = new Dice();
    private static ChanceCardDeck cardDeck;
    private static ArrayList<Player> players;
    
    private static Scanner reader = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        //Initializing Board and Chance Cards.
        /*System.out.println("Welcome to Monopoly. Which theme would you like to play?" +
                "\n\t1. Cars" +
                "\n\t2. 2020");*/
        
        String[] themes = {"Cars", "Yeah that's it."};
        String themeChosen = (String) inputView("Welcome to Monopoly. Which theme would you like to play?",
                "Welcome!",
                themes);
        
        String boardFile = "", cardFile = "";
        switch (themeChosen) {
            case "Cars":
                boardFile = "/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeSpaces.csv";
                cardFile = "/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeChanceCards.csv";
        }
        
        gameBoard = new Board(boardFile);
        cardDeck = new ChanceCardDeck(cardFile);
        cardDeck.shuffleDeck();
        
        Integer[] numPlayerChoices = {2,3,4,5,6};
        int numPlayers = (int) inputView("How many players are playing?",
                "Game Setup",
                numPlayerChoices);
        
        players = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            String name = (String) inputView("Player " + (i+1) + " name: ", "Game Setup", null);
            players.add(new Player(name));
        }
    
        System.out.println("Welcome to the game! Here is a preview of the board:" + "\n" +
                gameBoard + "\n" +
                "For this game, you will need " + gameBoard.pointsToWin(numPlayers) + " points to win.");
        
        int currentPlayer = (int) (Math.random() * numPlayers);
        infoView("Player " + (currentPlayer+1) + " starts!", "Game Start!");
        
        int numDoubles = 0;
        boolean gameNotOver = true;
        Player winner = null;
        while(gameNotOver) {
            playerTurn(players.get(currentPlayer));
            
            //Checks if the dice rolled was a double. If so, they get to go again.
            if(dice.isDouble()) {
                infoView("You rolled a double so you get to go again!",
                        players.get(currentPlayer).getName() + "\'s Turn.");
                numDoubles++;
                if (numDoubles >= 3)
                    JailSpace.putPlayerInJail(players.get(currentPlayer));
            } else {
                numDoubles = 0;
                currentPlayer = nextPlayer(currentPlayer);
                infoView("NEXT PLAYER TURN", "NEXT PLAYER TURN");
            }
            
            //Checks if anyone has won
            for(Player player : players) {
                if (player.getPoints() >= gameBoard.pointsToWin(players.size())) {
                    gameNotOver = false;
                    winner = player;
                }
            }
        }
    
        Collections.sort(players);
        String sortedPlayers = "";
        for(Player player : players)
            sortedPlayers += player + "\n";
        
        infoView(winner.getName() + " has won! Game over!" +
                "\nFinal results:\n" + sortedPlayers, "Game Over!");
        
    }
    
    private static int nextPlayer(int currentPlayer) {
        if(currentPlayer == players.size()-1)
            return 0;
        return ++currentPlayer;
    }
    
    private static void playerTurn(Player player) {
        String title = player.getName() + "\'s Turn";
        
        infoView(player.getName() + ", it is your turn!", title);
        int steps = dice.rollDice();
        infoView("You rolled " + dice + ".", title);
        
        int newPosition = player.movePlayer(steps);
        Space newSpace = gameBoard.getSpaceAtPosition(newPosition);
    
        infoView(player.getName() + ", you moved " + steps + " steps to:\n" + newSpace, title);
        
        if (newSpace instanceof GoSpace)
            GoSpace.passGo(player);
        else if (newSpace instanceof GoToJailSpace)
            JailSpace.putPlayerInJail(player);
        else if (newSpace instanceof ChanceSpace)
            executeChanceCard(player, cardDeck.drawCard());
        else if (newSpace instanceof PropertySpace)
            executePropertySpace(player, (PropertySpace) newSpace);
    }
    
    private static void executeChanceCard(Player player, ChanceCard card) {
        infoView(card.toString(), player.getName() + "\'s Turn");
        if (card.getValue() < 0) {
            boolean canBuy = player.removeMoney(-1 * card.getValue());
            if (!canBuy)
                kickPlayerOut(null, player);
        } else {
            player.addMoney(card.getValue());
        }
        infoView("Your balance is now $" + player.getMoney(), player.getName() + "\'s Turn");
    }
    
    private static void executePropertySpace(Player player, PropertySpace property) {
        String title = player.getName() + "\'s Turn";
        
        if (!property.isPurchased()) {
            //Ask if they want to buy the property. If not, auction the property.
            while(true) {
                String[] options = {"Yes", "No"};
                int choice = optionView("Would you like to buy this property?" + "\n" +
                                property + "\n" +
                                "You have $" + player.getMoney() + ".",
                        title,
                        options);
                if (choice == 1) {
                    propertyAuction(property);
                    break;
                }
                else {
                    boolean canBuyProperty = property.buyProperty(player, property.getPrice());
                    if (canBuyProperty) {
                        infoView("You have successfully bought this property!" + "\n" +
                                        "You now have " + player.getPoints() + " point(s) " +
                                        "and $" + player.getMoney() + " remaining.",
                                title);
                        break;
                    } else
                        infoView("You do not have enough money to buy this property!", title);
                }
            }
        } else {
            //Pay rent
            infoView("You landed on " + property.getOwner().getName() + "\'s property." + "\n" +
                            "You owe " + property.getOwner().getName() + " $" + property.getRent() + ".",
                    title);
            
            boolean canPay = player.removeMoney(property.getRent());
            if(!canPay)
                kickPlayerOut(property.getOwner(), player);
            else {
                property.getOwner().addMoney(property.getRent());
                infoView("You have $" + player.getMoney() + " remaining.",
                        title);
            }
        }
    }
    
    private static void propertyAuction(PropertySpace property) {
        String title = "AUCTION TIME!!!";
        
        infoView("AUCTION TIME!!!\n" + "The auction has started for:\n" + property,
                title);
        
        Player highestBidder = null;
        int highestBid = 0;
        
        int cnt = 0;
        while (true) {
            Player currentPlayer = players.get(cnt);
            if (currentPlayer == highestBidder)
                break;
            
            if (currentPlayer.getMoney() <= highestBid) {
                infoView(currentPlayer.getName() + ", you do not have enough money to bid! :(",
                        title);
                cnt++;
                continue;
            }
            String[] options = {"Yes", "No"};
            int choice = optionView(currentPlayer.getName() + ", would you like to bid on this property?",
                    title,
                    options);
            
            if (choice  == 0) {
                int bid;
                do {
                    String bidString = (String) inputView(currentPlayer.getName() + ", how much would like to bid?" +
                            "\nCurrent bid: $" + highestBid +
                            "\nYour money: $" + currentPlayer.getMoney(),
                            title,
                            null);
                    bid = Integer.parseInt(bidString);
                    if (bid > highestBid)
                        break;
                    infoView("Error: Your bid must be greater than the current highest bid. Try again.",
                            title);
                } while (true);
                
                highestBidder = currentPlayer;
                highestBid = bid;
            }
            cnt = nextPlayer(cnt);
        }
    
        infoView(highestBidder.getName() + " has won the auction at the price of $" + highestBid + "!\n" +
                "Enjoy your new acquisition!" + "\n" +
                "You now have " + highestBidder.getPoints() + " point(s) and $" + highestBidder.getMoney() + "reminaing.",
                title);
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
    
    private static int optionView(String message, String title, String[] options) {
        return JOptionPane.showOptionDialog(null,
                message,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
    }
    
    private static Object inputView(String message, String title, Object[] options) {
        Object response;
        if (options == null)
            response = JOptionPane.showInputDialog(null,
                    message + "\nPlease click OK when done.",
                    title,
                    JOptionPane.PLAIN_MESSAGE);
        else
            response = JOptionPane.showInputDialog(null,
                    message + "\nPlease click OK when done.",
                    title,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
        
        return response;
    }
    
    private static void infoView(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
