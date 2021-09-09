import Cards.ChanceCardDeck;
import Spaces.Board;

import java.io.*;
import java.util.Scanner;

public class Tester {
    
    public static void main(String[] args) {
        //Board board = new Board("/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeSpaces.csv");
        //System.out.println(board);
        
        ChanceCardDeck deck = new ChanceCardDeck("/Users/AndersonHsiao/IdeaProjects/Monopoly2020/src/CarThemeChanceCards.csv");
        System.out.println(deck);
        System.out.println("---------------------");
        
        deck.shuffleDeck();
        System.out.println(deck);
    }
    
}
