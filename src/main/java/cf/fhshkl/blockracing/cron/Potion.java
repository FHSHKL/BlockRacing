package cf.fhshkl.blockracing.cron;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import cf.fhshkl.blockracing.core.Game;
import cf.fhshkl.blockracing.core.GameState;

public class Potion extends BukkitRunnable {
    @Override
    public void run() {
        if(Game.gameState != GameState.INGAME){
            this.cancel();
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 255));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 10, 255));
        }
    }
}
