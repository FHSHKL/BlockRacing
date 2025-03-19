package cf.fhshkl.blockracing.cron;

import org.bukkit.scheduler.BukkitRunnable;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameScoreboard;
import cf.fhshkl.blockracing.core.GameState;
import cf.fhshkl.blockracing.core.GameTeam;

public class CollectChecker extends BukkitRunnable {

    @Override
    public void run() {
        if(Game.gameState != GameState.INGAME){
            this.cancel();
            return;
        }
        
        for(GameTeam team : Game.teamList){
            team.checkInventory();
        }

        GameScoreboard.repaint();
    }
    
}
