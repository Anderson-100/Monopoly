package Cards;

import Spaces.ChanceSpace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChanceCardDeck {
    
    private ArrayList<ChanceCard> deck;
    
    public ChanceCardDeck(String fileName) {
        try {
            deck = new ArrayList<>(20);
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(", ");
                deck.add(new ChanceCard(splitData[0],       //description
                        Integer.parseInt(splitData[1])));   //value
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot find Chance Cards file.");
            e.printStackTrace();
        }
    }
    
    public void shuffleDeck() {
        for(int i = 0; i < deck.size(); i++) {
            deck.add((int) (Math.random() * deck.size()), deck.remove(0));
        }
    }
    
    public ChanceCard drawCard() {
        ChanceCard card = deck.remove(0);
        deck.add(card);
        return card;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(ChanceCard card : deck) {
            s += card.toString() + "\n";
        }
        return s;
    }
}
