package Spaces;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    
    private Space[] spaces;
    
    public Board(String fileName) {
        spaces = new Space[40];
        
        //Corner Spaces
        spaces[0] = new GoSpace();
        spaces[10] = new JailSpace();
        spaces[20] = new FreeParkingSpace();
        spaces[30] = new GoToJailSpace();
        
        //Chance Spaces - picks 3 random spots of each side
        for (int i = 0; i < 12; i++) {
            int randomSpace;
            do {
                randomSpace = (int) (Math.random() * 9 + 1) + (i / 3 * 10);
            } while (spaces[randomSpace] != null);
            
            spaces[randomSpace] = new ChanceSpace(randomSpace);
        }
        
        //Fills in the rest of the spaces with Properties
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            
            int counter = 1;
            while (myReader.hasNextLine()) {
                if (spaces[counter] == null) {
                    String data = myReader.nextLine();
                    String[] splitData = data.split(", "); //Name, Group, Price, Points, Rent
                    spaces[counter] = new PropertySpace(splitData[0], //name
                            counter,
                            Integer.parseInt(splitData[1]),  //group
                            Integer.parseInt(splitData[2]),  //price
                            Integer.parseInt(splitData[3]),  //points
                            Integer.parseInt(splitData[4])); //rent
                }
                counter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot find Property Spaces file.");
            e.printStackTrace();
        }
    }
    
    public int pointsToWin(int numPlayers) {
        int points = 0;
        for(Space property : spaces) {
            if (property instanceof PropertySpace)
                points += ((PropertySpace) property).getPoints();
        }
        
        return points / numPlayers;
    }
    
    public Space getSpaceAtPosition(int position) {
        return spaces[position];
    }
    
    @Override
    public String toString() {
        String s = "";
        for (Space space : spaces) {
            s += space + "\n";
        }
        return s;
    }
    
    /*
    public void previewBoard() {
        String emptyLabel = "";
        
        JFrame frame = new JFrame("FrameDemo");
        frame.getContentPane().add;
    }
    */
}
