package Spaces;

import Players.Player;
import java.util.ArrayList;

public class JailSpace extends Space {
    
    private static ArrayList<Player> playersInJail;
    
    public JailSpace() {
        super("Jail", 10);
        playersInJail = new ArrayList<>();
    }
    
    public static void putPlayerInJail(Player player) {
        playersInJail.add(player);
    }
}
