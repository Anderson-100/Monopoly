package Spaces;

import Players.Player;

public class GoSpace extends Space {
    
    private static final int PASS_GO_MONEY = 200;
    
    public GoSpace() {
        super("Go", 0);
    }
    
    public static void passGo(Player player) {
        player.addMoney(PASS_GO_MONEY);
    }
}
